package org.codwh.redis.monitor.service.util.quartz.impl;

import com.alibaba.fastjson.JSONObject;
import com.mysql.fabric.Server;
import jdk.internal.dynalink.linker.LinkerServices;
import org.codwh.redis.custom.dao.IRedisClusterMasterDao;
import org.codwh.redis.custom.dao.IRedisClusterMonitorLogDao;
import org.codwh.redis.custom.dao.IRedisClusterSlaveDao;
import org.codwh.redis.custom.model.redis.RedisClusterMaster;
import org.codwh.redis.custom.model.redis.RedisClusterSlave;
import org.codwh.redis.custom.model.redis.RedisMonitorLog;
import org.codwh.redis.monitor.service.constants.OpSystemStatusType;
import org.codwh.redis.monitor.service.constants.OpSystemType;
import org.codwh.redis.monitor.service.constants.ServerStatusType;
import org.codwh.redis.monitor.service.constants.TomcatStatusType;
import org.codwh.redis.monitor.service.util.RedisDistributedLock;
import org.codwh.redis.monitor.service.util.quartz.IQuartzFramework;
import org.codwh.redis.zookeeper.constants.ZkType;
import org.codwh.redis.zookeeper.util.ZkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;

@Service("redisQuartz")
@SuppressWarnings("all")
public class RedisQuartzImpl implements IQuartzFramework {

    @Autowired
    private IRedisClusterSlaveDao redisClusterSlaveDao;

    @Autowired
    private IRedisClusterMasterDao redisClusterMasterDao;

    @Autowired
    private ZkUtils zkUtils;

    @Autowired
    private IRedisClusterMonitorLogDao redisClusterMonitorLogDao;

    @Override
    public String invoke(int times) {
        String res = "";
        try {
            checkMasterStatus(times);
        } finally {
            res = "执行成功";
        }
        return res;
    }

    /**
     * 检查redis master server的健康性
     *
     * @param times
     */
    private void checkMasterStatus(int times) {
        List<RedisClusterMaster> redisClusterMasters = getRunningRedisClusterMaster();
        if (redisClusterMasters != null) {
            for (RedisClusterMaster master : redisClusterMasters) {
                master.setOperationStatus(OpSystemStatusType.DONE.getCode());   //DONE
                int operateTime = updateRunningRedisClusterMaster(master);
                if (operateTime > 0) {
                    //设置操作状态，防止被别的并行的服务器操作到
                    master.setVersion(master.getVersion() + 1);
                    //获取redis server的状态
                    boolean flag = getRedisStatusFlag(master.getRedisServerHost(), master.getRedisServerPort(), times);

                    if (flag) {   //如果状态正常
                        master.setOperationStatus(OpSystemStatusType.DOING.getCode());
                        updateRunningRedisClusterMaster(master);
                        checkSlaveStatus(times, master);    //检查该主服务器对应的slave服务器状态
                    } else {      //如果master挂了
                        master.setOperationStatus(OpSystemStatusType.DOING.getCode());
                        master.setRunnerStatus(TomcatStatusType.STOP.getCode());
                        master.setRedisServerStatus(ServerStatusType.DISABLED.getCode());
                        redisClusterMasterDao.updateById(master);

                        List<RedisClusterSlave> slaves = getClusterSlave(master.getId());  //根据master id获取slaves
                        RedisClusterSlave healthySlave = getHealthySlave(times, slaves, master);   //选择健康的slave
                        if (healthySlave != null) {
                            slaves.remove(healthySlave);    //移除该健康的slave，升任至master，后面持久化请删除数据库中的记录
                            RedisClusterMaster newMaster = createRedisClusterMaster(healthySlave, master);
                            for (RedisClusterSlave slave : slaves) {
                                slave.setRedisMasterId(newMaster.getId());
                                redisClusterSlaveDao.updateById(slave); //将对象中的保存持久化到数据库当中
                            }
                            //注册到zookeeper
                            registerToZooKeeper(master, newMaster);
                            //改变redis server之间的主从关系
                            changeMasterSlaveRelationship(slaves, newMaster);
                            //创建变化的log
                            saveMasterChangingLog(master, newMaster);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查redis slave server的运行状况
     *
     * @param times
     * @param master
     */
    private void checkSlaveStatus(int times, RedisClusterMaster master) {
        List<RedisClusterSlave> slaves = getClusterSlave(master.getId());
        if (slaves != null) {
            for (RedisClusterSlave slave : slaves) {
                slave.setOperationStatus(OpSystemStatusType.DONE.getCode());
                int operateTime = updateRunningRedisClusterSlave(slave);
                if (operateTime > 0) {
                    slave.setVersion(slave.getVersion() + 1);
                    boolean flag = getRedisStatusFlag(slave.getRedisServerHost(), slave.getRedisServerPort(), times);
                    if (flag) {
                        slave.setOperationStatus(OpSystemStatusType.DOING.getCode());
                        updateRunningRedisClusterSlave(slave);
                    } else {
                        slave.setOperationStatus(OpSystemStatusType.DOING.getCode());
                        slave.setClusterStatus(TomcatStatusType.STOP.getCode());
                        slave.setRedisServerStatus(ServerStatusType.DISABLED.getCode());

                        int count = redisClusterSlaveDao.updateById(slave);
                        if (count > 0) {
                            //表示确实有slave挂了，slave挂了我们需要直接删除这个slave
                            StringBuilder builder = new StringBuilder();
                            builder.append(master.getRedisServerHost()).append(":").append(master.getRedisServerPort());
                            String parentPath = builder.toString();

                            builder = new StringBuilder();
                            builder.append(slave.getRedisServerHost()).append(":").append(slave.getRedisServerPort());
                            String childrenPath = builder.toString();

                            zkUtils.deleteNode(parentPath, childrenPath, ZkType.AVAILABLE_SERVER);

                            //slave的变化打进数据库
                            saveSlaveLog(slave);
                        }
                    }
                }
            }
        }
    }

    /**
     * slave服务器变化log
     *
     * @param redisClusterSlave
     */
    private void saveSlaveLog(RedisClusterSlave redisClusterSlave) {
        RedisMonitorLog clusterMonitorLog = new RedisMonitorLog();
        clusterMonitorLog.setCreateTime(new Date());
        clusterMonitorLog.setRedisServerHost(redisClusterSlave.getRedisServerHost());
        clusterMonitorLog.setRedisServerPort(redisClusterSlave.getRedisServerPort());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("从Redis主服务器:").append(redisClusterSlave.getRedisServerHost()).append(":").append(redisClusterSlave.getRedisServerPort()).append("已经DOWN");
        clusterMonitorLog.setDescription(stringBuilder.toString());
        redisClusterMonitorLogDao.insert(clusterMonitorLog);
    }

    /**
     * redis master server变化日志记录
     *
     * @param master
     * @param newMaster
     */
    private void saveMasterChangingLog(RedisClusterMaster master, RedisClusterMaster newMaster) {
        RedisMonitorLog log = new RedisMonitorLog();

        log.setCreateTime(new Date());
        log.setRedisServerHost(master.getRedisServerHost());
        log.setRedisServerPort(master.getRedisServerPort());

        StringBuilder builder = new StringBuilder();
        builder.append("Monitor检测到redis主服务器节点宕机，宕机主服务器host: ").append(master.getRedisServerHost())
                .append(" 宕机主服务器port: ").append(master.getRedisServerPort());
        builder.append("已有新的从服务器升级至主服务器，新主服务器host: ").append(newMaster.getRedisServerHost())
                .append(" 新主服务器port: ").append(newMaster.getRedisServerPort());
        log.setDescription(builder.toString());

        redisClusterMonitorLogDao.insert(log);
    }

    /**
     * 重新调整redis master-slave关系
     *
     * @param slaves
     * @param newMaster
     */
    private void changeMasterSlaveRelationship(List<RedisClusterSlave> slaves, RedisClusterMaster newMaster) {
        Jedis jedisMaster = new Jedis(newMaster.getRedisServerHost(), newMaster.getRedisServerPort());
        jedisMaster.slaveofNoOne(); //表示newMaster已经不是任何redis server的slave了
        jedisMaster.eval("return redis.call('CONFIG', 'REWRITE')"); //更新配置文件
        for (RedisClusterSlave slave : slaves) {
            Jedis jedisSlave = new Jedis(slave.getRedisServerHost(), slave.getRedisServerPort());
            jedisSlave.slaveof(newMaster.getRedisServerHost(), newMaster.getRedisServerPort());
            jedisSlave.eval("return redis.call('CONFIG', 'REWRITE')");
            jedisSlave.close();
        }
        jedisMaster.close();
    }

    /**
     * 注册到zookeeper
     *
     * @param master
     * @param newMaster
     */
    private void registerToZooKeeper(RedisClusterMaster master, RedisClusterMaster newMaster) {
        StringBuilder builder = new StringBuilder();
        builder.append(master.getRedisServerHost()).append(":").append(master.getRedisServerPort());
        String parentPath = builder.toString();

        StringBuilder childrenBuilder = new StringBuilder();
        childrenBuilder.append(newMaster.getRedisServerHost()).append(":").append(newMaster.getRedisServerPort());
        String childrenPath = childrenBuilder.toString();

        zkUtils.deleteNode(parentPath, childrenPath, ZkType.AVAILABLE_SERVER);  //删除对应的slave

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("host", newMaster.getRedisServerHost());
        jsonObject.put("port", newMaster.getRedisServerPort());

        zkUtils.setNodeData(null, parentPath, jsonObject.toJSONString());   //注册新的数据
    }

    /**
     * 创建新的master，删除slave但是不删除旧的master
     *
     * @param healthySlave
     * @param deadMaster
     * @return
     */
    private RedisClusterMaster createRedisClusterMaster(RedisClusterSlave healthySlave, RedisClusterMaster deadMaster) {
        //先把升任的slave从数据库中删除
        this.redisClusterSlaveDao.deleteById(healthySlave.getId());

        //用slave创建新的master
        RedisClusterMaster createdMaster = new RedisClusterMaster();
        createdMaster.setCreateTime(new Date());
        createdMaster.setId(healthySlave.getId());
        createdMaster.setRedisServerHost(healthySlave.getRedisServerHost());
        createdMaster.setRedisServerPort(healthySlave.getRedisServerPort());
        createdMaster.setRedisServerStatus(healthySlave.getRedisServerStatus());
        createdMaster.setOperationStatus(healthySlave.getOperationStatus());
        createdMaster.setRunnerStatus(healthySlave.getClusterStatus());
        createdMaster.setVersion(1);    //新的version
        createdMaster.setZkPath(healthySlave.getZkPath());

        this.redisClusterMasterDao.insert(createdMaster);
        return createdMaster;
    }

    /**
     * 获取redis集群中运行着的主节点
     *
     * @return
     */
    private List<RedisClusterMaster> getRunningRedisClusterMaster() {
        //根据status，status设置为AVAILABLE，表示获取所有运行着的master节点
        List<RedisClusterMaster> redisClusterMasters = this.redisClusterMasterDao.getListByStatus();
        if (redisClusterMasters != null && redisClusterMasters.size() > 0) {
            return redisClusterMasters;
        }
        return null;
    }

    /**
     * 获得健康的slave server
     *
     * @param times
     * @param slaves
     * @param master
     * @return
     */
    private RedisClusterSlave getHealthySlave(int times, List<RedisClusterSlave> slaves, RedisClusterMaster master) {
        if (slaves != null) {
            for (RedisClusterSlave slave : slaves) {
                slave.setOperationStatus(OpSystemStatusType.DONE.getCode());
                int operateTime = updateRunningRedisClusterSlave(slave);
                if (operateTime > 0) {
                    slave.setVersion(slave.getVersion() + 1);
                    boolean flag = getRedisStatusFlag(slave.getRedisServerHost(), slave.getRedisServerPort(), times);
                    if (flag) { //表示运行正常
                        slave.setOperationStatus(OpSystemStatusType.DONE.getCode());
                        updateRunningRedisClusterSlave(slave);
                        return slave;
                    } else {
                        slave.setOperationStatus(OpSystemStatusType.DOING.getCode());
                        slave.setClusterStatus(TomcatStatusType.STOP.getCode());
                        slave.setRedisServerStatus(ServerStatusType.DISABLED.getCode());

                        int count = redisClusterSlaveDao.updateById(slave);
                        if (count > 0) {
                            //表示确实有slave挂了，slave挂了我们需要直接删除这个slave
                            StringBuilder builder = new StringBuilder();
                            builder.append(master.getRedisServerHost()).append(":").append(master.getRedisServerPort());
                            String parentPath = builder.toString();

                            builder = new StringBuilder();
                            builder.append(slave.getRedisServerHost()).append(":").append(slave.getRedisServerPort());
                            String childrenPath = builder.toString();

                            zkUtils.deleteNode(parentPath, childrenPath, ZkType.AVAILABLE_SERVER);

                            //slave的变化打进数据库
                            saveSlaveLog(slave);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 通过jedis连接判断redis server是否健康运行没有崩溃
     *
     * @param host
     * @param port
     * @param tryTimes
     * @return
     */
    private boolean getRedisStatusFlag(String host, int port, int tryTimes) {
        //循环10次获取Redis主是否挂掉
        try {
            Jedis jedis = new Jedis(host, port);
            jedis.info();
            jedis.close();
        } catch (Exception e) {
            if (tryTimes <= 5) {
                tryTimes = tryTimes + 1;
                return getRedisStatusFlag(host, port, tryTimes);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 更新master
     *
     * @param master
     * @return
     */
    private Integer updateRunningRedisClusterMaster(RedisClusterMaster master) {
        return this.redisClusterMasterDao.updateById(master);
    }

    /**
     * 更新slave
     *
     * @param slave
     * @return
     */
    private Integer updateRunningRedisClusterSlave(RedisClusterSlave slave) {
        return this.redisClusterSlaveDao.updateById(slave);
    }

    /**
     * 通过masterId获取slave
     *
     * @param masterId
     * @return
     */
    private List<RedisClusterSlave> getClusterSlave(Integer masterId) {
        return this.redisClusterSlaveDao.getListByMasterId(masterId);
    }
}
