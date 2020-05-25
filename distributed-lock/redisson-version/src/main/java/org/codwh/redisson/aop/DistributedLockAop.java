package org.codwh.redisson.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codwh.redisson.annotation.DLock;
import org.codwh.redisson.constants.LockMode;
import org.codwh.redisson.property.RedissonProperties;
import org.codwh.redisson.util.SpelUtils;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Aspect
@Order(-1)
@SuppressWarnings("all")
@EnableConfigurationProperties(RedissonProperties.class)
public class DistributedLockAop {

    Logger logger = LoggerFactory.getLogger(DistributedLockAop.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SpelUtils spelUtils;

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    private static final String suffix = "codwh";

    /**
     * 只有方法上添加了DLock注解才会启动这个切面，例如
     * @DLock(...)
     * returnType methodName(Parameters...){}
     *
     * @param lock
     */
    @Pointcut("@annotation(lock)")
    public void lockPointcut(DLock lock){}

    @Around("lockPointcut(lock)")
    public Object aroundDLock(ProceedingJoinPoint joinPoint, DLock lock){
        //首先解析出注解DLock中的参数的值，用Spel解析器解析，因为有可能DLock中的值是动态根据方法参数而变化的
        String[] locks = lock.locks();
        if(locks.length == 0){
            throw new RuntimeException("DLock.locks()不能为空");
        }

        Config config = (Config) ctx.getBean("config");
        long watchDogTimeout = lock.maxHoldTime() == 0 ? config.getLockWatchdogTimeout() : lock.maxHoldTime();
        long waitLockTimeout = lock.maxWaitTime() == 0 ? watchDogTimeout : watchDogTimeout;

        LockMode lockMode = lock.mode();    //加锁的模式
        if(lockMode == null){
            lockMode = LockMode.REENTRANT;
        }else if(locks.length > 1){
            //当锁的个数大于0的时候就开始使用RedLock了
            lockMode = LockMode.RED;
        }

        if(!lockMode.equals(LockMode.RED) && !lockMode.equals(LockMode.MULTI) && locks.length > 1){
            throw new RuntimeException("DLock中的锁有多个，但是锁的模式不支持多锁，程序出错");
        }

        logger.info("[DistributedLockAop]: 分布式开始加锁, 当前锁模式: {}, 最大持锁时间: {}, 等待锁的最大时间: {}",
                lockMode.name(), waitLockTimeout, waitLockTimeout);

        RLock coreLock = null;

        switch (lockMode){
            case FAIR:
                coreLock = redissonClient.getFairLock(spelUtils.parseSpelStr(locks[0], joinPoint, suffix).get(0));
                break;
            case RED:   //red lock groups multiple locks while multiple lock groups several single RLock
                List<RLock> lockList = new ArrayList<RLock>();
                for(String eachLock : locks){
                    List<String> eachEachLocks = spelUtils.parseSpelStr(eachLock, joinPoint, suffix);
                    for(String eachEachLock : eachEachLocks){
                        lockList.add(redissonClient.getLock(eachEachLock));
                    }
                }
                RLock[] rlocks = new RLock[lockList.size()];
                int lockIndex = 0;
                for(RLock rlock : lockList){
                    rlocks[lockIndex++] = rlock;
                }
                coreLock = new RedissonRedLock(rlocks);
                break;
            case MULTI:
                lockList = new ArrayList<RLock>();
                for(String eachLock : locks){
                    List<String> eachEachLocks = spelUtils.parseSpelStr(eachLock, joinPoint, suffix);
                    for(String eachEachLock : eachEachLocks){
                        lockList.add(redissonClient.getLock(eachEachLock));
                    }
                }
                rlocks = new RLock[lockList.size()];
                lockIndex = 0;
                for(RLock rLock : lockList){
                    rlocks[lockIndex++] = rLock;
                }
                coreLock = new RedissonMultiLock(rlocks);
                break;
            case REENTRANT:
                List<String> parsedSpel = spelUtils.parseSpelStr(locks[0], joinPoint, suffix);
                if(parsedSpel.size() == 1){
                    //说明是可重入锁
                    coreLock = redissonClient.getLock(parsedSpel.get(0));
                }else{
                    //说明是Red锁
                    rlocks = new RLock[parsedSpel.size()];
                    lockIndex = 0;
                    for(String eachLock : parsedSpel){
                        rlocks[lockIndex++] = redissonClient.getLock(eachLock);
                    }
                    coreLock = new RedissonRedLock(rlocks);
                }
                break;
            case READ:
                RReadWriteLock readLock = redissonClient.getReadWriteLock(spelUtils.parseSpelStr(locks[0], joinPoint, suffix).get(0));
                coreLock = readLock.readLock();
                break;
            case WRITE:
                RReadWriteLock writeLock = redissonClient.getReadWriteLock(spelUtils.parseSpelStr(locks[0], joinPoint, suffix).get(0));
                coreLock = writeLock.writeLock();
                break;
        }

        //aop entrance
        if(coreLock != null){
            //@TODO 完善分布式锁的加速流程
            try {
                logger.info("分布式锁开始加锁，加锁时间: {}", System.currentTimeMillis());
                coreLock.lock(watchDogTimeout, TimeUnit.MILLISECONDS);
            }catch (Exception e){
                throw new RuntimeException("分布式锁加锁失败!");
            }finally {
                coreLock.unlock();
                logger.info("分布式锁解锁，解锁时间: {}", System.currentTimeMillis());
            }
        }

        try {
            return joinPoint.proceed();
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}
