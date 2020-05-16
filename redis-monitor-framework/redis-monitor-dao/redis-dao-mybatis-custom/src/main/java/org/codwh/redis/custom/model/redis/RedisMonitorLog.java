package org.codwh.redis.custom.model.redis;

import java.io.Serializable;
import java.util.Date;

public class RedisMonitorLog implements Serializable {

    private static final long serialVersionUID = 176999289690746809L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * redis server主机
     */
    private String redisServerHost;

    /**
     * redis server端口号
     */
    private Integer redisServerPort;

    /**
     * 日志描述
     */
    private String description;

    /**
     * Log创建时间
     */
    private Date createTime;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
