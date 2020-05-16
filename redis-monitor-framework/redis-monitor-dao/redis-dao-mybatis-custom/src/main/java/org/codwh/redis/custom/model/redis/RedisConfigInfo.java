package org.codwh.redis.custom.model.redis;

import java.io.Serializable;

public class RedisConfigInfo implements Serializable {

    private static final long serialVersionUID = -1448357890863435157L;

    /**
     * info时间戳
     */
    private String timestamp;

    /**
     * 客户端连接数
     */
    private Integer clientConnections;

    /**
     * 总命令数
     */
    private Integer totalCommands;

    /**
     * 系统分配的内存空间大小
     */
    private Double sysMemory;

    /**
     * 已经使用的内存大小
     */
    private Double usedMemory;

    /**
     * 缓存命令次数
     */
    private Double cacheHits;

    /**
     * 缓存未命中次数
     */
    private Double cacheMisses;

    /**
     * 系统使用CPU的多少
     */
    private Double usedSystemCPU;

    /**
     * Redis使用CPU的多少
     */
    private Double usedHumanCPU;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getClientConnections() {
        return clientConnections;
    }

    public void setClientConnections(Integer clientConnections) {
        this.clientConnections = clientConnections;
    }

    public Integer getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(Integer totalCommands) {
        this.totalCommands = totalCommands;
    }

    public Double getSysMemory() {
        return sysMemory;
    }

    public void setSysMemory(Double sysMemory) {
        this.sysMemory = sysMemory;
    }

    public Double getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Double usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Double getCacheHits() {
        return cacheHits;
    }

    public void setCacheHits(Double cacheHits) {
        this.cacheHits = cacheHits;
    }

    public Double getCacheMisses() {
        return cacheMisses;
    }

    public void setCacheMisses(Double cacheMisses) {
        this.cacheMisses = cacheMisses;
    }

    public Double getUsedSystemCPU() {
        return usedSystemCPU;
    }

    public void setUsedSystemCPU(Double usedSystemCPU) {
        this.usedSystemCPU = usedSystemCPU;
    }

    public Double getUsedHumanCPU() {
        return usedHumanCPU;
    }

    public void setUsedHumanCPU(Double usedHumanCPU) {
        this.usedHumanCPU = usedHumanCPU;
    }
}
