package org.codwh.redisson.annotation;

import org.codwh.redisson.constants.LockMode;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DLock {

    /**
     * 分布式锁的模式
     *
     * @return
     */
    LockMode mode() default LockMode.REENTRANT;

    /**
     * 该锁持有的锁的个数，如果locks长度大于1，说明一个锁持有不止一个锁
     * 该锁的模式为联锁MULTI
     *
     * @return
     */
    String[] locks();

    /**
     * 看门狗时长，即一个锁最长持有的时间，在高并发的环境下很容易出现这样一个问题
     * 就是持有锁的对象突然挂了，导致锁一直被占用，设置看门狗能够看管锁持有的对象
     * 到达一定的时长就会释放
     *
     * @return
     */
    long watchDogTimeout() default 0;

    /**
     * 没有持有锁的对象的最长等待锁的时间
     *
     * @return
     */
    long waitLockTimeout() default 0;
}
