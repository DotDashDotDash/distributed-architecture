package org.codwh.redis.custom.dao;

import org.codwh.redis.custom.model.redis.RedisClusterSlave;

import java.util.List;

public interface IRedisClusterSlaveDao extends IMyBatisRepository<RedisClusterSlave>, IPaginationDao<RedisClusterSlave> {

    /**
     * 根据主键来更新版本号，乐观锁
     *
     * @param clusterSlave
     * @return
     */
    int updateVersionByIdAndVersion(RedisClusterSlave clusterSlave);

    /**
     * 根据masterId来获取集群信息，查询前50条数据
     *
     * @param masterId
     * @return
     */
    List<RedisClusterSlave> getListByMasterId(Integer masterId);
}
