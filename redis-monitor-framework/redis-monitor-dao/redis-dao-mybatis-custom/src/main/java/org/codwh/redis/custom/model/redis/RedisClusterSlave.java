package org.codwh.redis.custom.model.redis;

import java.io.Serializable;
import java.util.Date;

public class RedisClusterSlave implements Serializable {

    private static final long serialVersionUID = -5987866960486048456L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * redis server主机
     */
    private String redisServerHost;

    /**
     * redis端口号
     */
    private Integer redisServerPort;

    /**
     * redis集群运行状态
     */
    private Integer clusterStatus;

    /**
     * redis server状态
     */
    private Integer redisServerStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * redis master主服务主键
     */
    private Integer redisMasterId;

    /**
     * redis master主机
     */
    private String redisMasterHost;

    /**
     * redis master端口号
     */
    private Integer redisMasterPort;

    /**
     * 操作状态
     */
    private Integer operationStatus;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 权重
     */
    private Integer weight;

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

    public Integer getClusterStatus() {
        return clusterStatus;
    }

    public void setClusterStatus(Integer clusterStatus) {
        this.clusterStatus = clusterStatus;
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

    public Integer getRedisMasterId() {
        return redisMasterId;
    }

    public void setRedisMasterId(Integer redisMasterId) {
        this.redisMasterId = redisMasterId;
    }

    public String getRedisMasterHost() {
        return redisMasterHost;
    }

    public void setRedisMasterHost(String redisMasterHost) {
        this.redisMasterHost = redisMasterHost;
    }

    public Integer getRedisMasterPort() {
        return redisMasterPort;
    }

    public void setRedisMasterPort(Integer redisMasterPort) {
        this.redisMasterPort = redisMasterPort;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }
}
