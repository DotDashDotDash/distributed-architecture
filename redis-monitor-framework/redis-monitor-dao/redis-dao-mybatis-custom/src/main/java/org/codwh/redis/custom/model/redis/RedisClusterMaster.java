package org.codwh.redis.custom.model.redis;

import java.io.Serializable;
import java.util.Date;

public class RedisClusterMaster implements Serializable {

    private static final long serialVersionUID = -6568985731036515795L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * redis主机
     */
    private String redisServerHost;

    /**
     * redis server端口号
     */
    private Integer redisServerPort;

    /**
     * redis server运行状态
     */
    private Integer runnerStatus;

    /**
     * redis server机器状态
     */
    private Integer redisServerStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 操作状态
     */
    private Integer operationStatus;

    /**
     * zookeeper路径
     */
    private String zkPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRedisServerHost() {
        return redisServerHost;
    }

    public void setRedisServerHost(String redisServerHost) {
        this.redisServerHost = redisServerHost;
    }

    public Integer getRedisServerPort() {
        return redisServerPort;
    }

    public void setRedisServerPort(Integer redisServerPort) {
        this.redisServerPort = redisServerPort;
    }

    public Integer getRunnerStatus() {
        return runnerStatus;
    }

    public void setRunnerStatus(Integer runnerStatus) {
        this.runnerStatus = runnerStatus;
    }

    public Integer getRedisServerStatus() {
        return redisServerStatus;
    }

    public void setRedisServerStatus(Integer redisServerStatus) {
        this.redisServerStatus = redisServerStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }
}
