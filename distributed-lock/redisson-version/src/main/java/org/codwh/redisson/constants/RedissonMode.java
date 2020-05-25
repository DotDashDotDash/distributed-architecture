package org.codwh.redisson.constants;

@SuppressWarnings("all")
public enum RedissonMode {

    /**
     * 单机模式
     */
    SINGLE,

    /**
     * 哨兵模式
     */
    SENTINEL,

    /**
     * 集群模式
     */
    CLUSTER,

    /**
     * 云托管模式
     */
    REPLICATED,

    /**
     * 主从模式
     */
    MASTER,

    /**
     * 代理模式
     */
    PROXY
}
