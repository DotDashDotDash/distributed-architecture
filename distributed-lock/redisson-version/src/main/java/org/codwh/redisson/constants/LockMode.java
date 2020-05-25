package org.codwh.redisson.constants;

/**
 * 分布式锁的模式
 */
public enum LockMode {

    /**
     * 公平锁
     */
    FAIR,

    /**
     * 可重入锁
     */
    REENTRANT,

    /**
     * 红锁
     */
    RED,

    /**
     * 读锁
     */
    READ,

    /**
     * 写锁
     */
    WRITE,

    /**
     * 联锁
     */
    MULTI
}
