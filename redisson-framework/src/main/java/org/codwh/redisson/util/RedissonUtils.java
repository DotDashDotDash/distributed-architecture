package org.codwh.redisson.util;

import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public class RedissonUtils {

    Logger logger = LoggerFactory.getLogger(RedissonUtils.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ApplicationContext ctx;

    /**
     * 同步设置obj值
     *
     * @param old
     * @param obj
     * @param <T>
     */
    public <T> void setValue(String old, T obj){
        RBucket<T> bucket = redissonClient.getBucket(old);
        bucket.set(obj, 1000 * 60 * 30, TimeUnit.SECONDS);
    }

    /**
     * 带过期时间的设置obj值
     *
     * @param old
     * @param obj
     * @param timeout
     * @param <T>
     */
    public <T> void setValue(String old, T obj, Long timeout){
        RBucket<T> bucket = redissonClient.getBucket(old);
        if(timeout == -1){
            setValue(old, obj);
        }else {
            bucket.set(obj, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 先获取再设置
     *
     * @param old
     * @param obj
     * @param <T>
     * @return
     */
    public <T> T getAndSetValue(String old, T obj){
        RBucket<T> bucket = redissonClient.getBucket(old);
        return bucket.getAndSet(obj);
    }

    /**
     * 带过期时间先获取再设置
     *
     * @param old
     * @param obj
     * @param timeout
     * @param <T>
     * @return
     */
    public <T> T getAndSetValue(String old, T obj, Long timeout){
        RBucket<T> bucket = redissonClient.getBucket(old);
        if(timeout == -1){
            return bucket.getAndSet(obj, 1000 * 60 * 300, TimeUnit.SECONDS);
        }else{
            return bucket.getAndSet(obj, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取存储的obj值
     *
     * @param bkt
     * @param obj
     * @param timeout
     * @param <T>
     * @return
     */
    public <T> T getValue(String bkt){
        RBucket<T> bucket = redissonClient.getBucket(bkt);
        return bucket.get();
    }

    /**
     * 尝试设置值
     *
     * @param old
     * @param obj
     * @param timeout
     * @param <T>
     * @return
     */
    public <T> Boolean trySetValue(String old, T obj, Long timeout){
        RBucket<T> bucket = redissonClient.getBucket(old);
        if(timeout == -1){
            //尝试设置默认30min
            return bucket.trySet(obj, 1000 * 60 * 30, TimeUnit.SECONDS);
        }else{
            return bucket.trySet(obj, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除obj
     *
     * @param old
     * @param <T>
     */
    public <T> void delteValue(String old){
        RBucket<T> bucket = redissonClient.getBucket(old);
        bucket.delete();
    }

    /**
     * 删除之前获得
     *
     * @param old
     * @param <T>
     * @return
     */
    public <T> T getAndDeleteValue(String old){
        RBucket<T> bucket = redissonClient.getBucket(old);
        return bucket.getAndDelete();
    }

    /**
     * 存储obj，默认30min过期
     *
     * @param old
     * @param obj
     * @param <T>
     */
    public <T> void setAsync(String old, T obj){
        RBucket<T> bucket = redissonClient.getBucket(old);
        RFuture<Void> setFuture = bucket.setAsync(obj);
        setFuture.onComplete((value, exception) -> {
            if(exception == null) {
                logger.info("RedissonUtil.Async.setValue Complete--> bucket: {}", bucket);
            } else{
                logger.error("RedissonUtil.Async.setValue Error--> exception: {}", exception.getMessage());
            }
        });
    }

    /**
     * 存储obj，使用指定过期时间
     *
     * @param old
     * @param obj
     * @param timeout
     * @param <T>
     */
    public <T> void setAsync(String old, T obj, Long timeout){
        RBucket<T> bucket = redissonClient.getBucket(old);
        RFuture<Void> setFuture = null;
        if(timeout == -1){
            setFuture = bucket.setAsync(obj, 1000 * 60 * 30, TimeUnit.SECONDS);
        }else{
            setFuture = bucket.setAsync(obj, timeout, TimeUnit.SECONDS);
        }
        setFuture.onComplete((value, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.setValue Complete--> bucket: {}", bucket);
            }else{
                logger.error("RedissonUtil.Async.setValue Error--> exception: {}", exception.getMessage());
            }
        });
    }

    /**
     * 获取并设置
     *
     * @param old
     * @param obj
     * @param <T>
     * @return
     */
    public <T> T getAndSetAsync(String old, T obj){
        RBucket<T> bucket = redissonClient.getBucket(old);
        RFuture<T> future = bucket.getAndSetAsync(obj);
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.getAndSetValue Complete--> bucket: {}");
            }else{
                logger.error("RedissonUtil.Async.getAndSetValue Error--> exception: {}", exception.getMessage());
            }
        });
        return future.getNow();
    }

    /**
     * 获取并设置
     *
     * @param old
     * @param obj
     * @param <T>
     * @return
     */
    public <T> T getAndSetAsync(String old, T obj, Long timeout){
        RBucket<T> bucket = redissonClient.getBucket(old);
        RFuture<T> future = null;
        if(timeout == -1){
            future = bucket.getAndSetAsync(obj, 1000 * 60 * 30, TimeUnit.SECONDS);
        }else{
            future = bucket.getAndSetAsync(obj, timeout, TimeUnit.SECONDS);
        }
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.getAndSetValue Complete--> bucket: {}");
            }else{
                logger.error("RedissonUtil.Async.getAndSetValue Error--> exception: {}", exception.getMessage());
            }
        });
        return future.getNow();
    }

    /**
     * 异步删除
     *
     * @param bkt
     * @param <T>
     * @return
     */
    public <T> Boolean deleteAsync(String bkt){
        RBucket<T> bucket = redissonClient.getBucket(bkt);
        RFuture<Boolean> future = bucket.deleteAsync();
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.deleteValue Complete--> bucket: {}");
            }else{
                logger.error("RedissonUtils.Async.deleteValue Error--> bucket: {}");
            }
        });
        return future.getNow();
    }

    /**
     * 尝试异步获取并设置value
     *
     * @param bkt
     * @param obj
     * @param <T>
     * @return
     */
    public <T> Boolean trySetAsync(String bkt, T obj){
        RBucket<T> bucket = redissonClient.getBucket(bkt);
        RFuture<Boolean> future = bucket.trySetAsync(obj);
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.trySet Complete--> bucket: {}", bucket);
            }else{
                logger.error("RedissonUtils.Async.trySet Error--> bucket: {}", bucket);
            }
        });
        return future.getNow();
    }

    /**
     * 尝试异步设置
     *
     * @param bkt
     * @param obj
     * @param timeout
     * @param <T>
     * @return
     */
    public <T> Boolean trySetAsync(String bkt, T obj, Long timeout) {
        RBucket<T> bucket = redissonClient.getBucket(bkt);
        RFuture<Boolean> future = null;
        if(timeout == -1){
            future = bucket.trySetAsync(obj, 1000 * 60 * 30, TimeUnit.SECONDS);
        }else{
            future = bucket.trySetAsync(obj, timeout, TimeUnit.SECONDS);
        }
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.trySetAsync Complete--> bucket: {}", bucket);
            }else{
                logger.error("RedissonUtils.Async.trySetAsync Error--> bucket: {}", bucket);
            }
        });
        return future.getNow();
    }

    /**
     * 尝试异步删除
     *
     * @param bkt
     * @param <T>
     * @return
     */
    public <T> T getAndDeletAsync(String bkt){
        RBucket<T> bucket = redissonClient.getBucket(bkt);
        RFuture<T> future = bucket.getAndDeleteAsync();
        future.whenComplete((res, exception) -> {
            if(exception == null){
                logger.info("RedissonUtils.Async.getAndDeleteAsync Complete--> bucket: {}", bucket);
            }else{
                logger.error("RedissonUtils.Async.getAndDeleteAsync Error--> bucket: {}", bucket);
            }
        });
        return future.getNow();
    }

    /**
     * 获取Map
     *
     * @param name
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> RMap<K, V> getMap(String name){
        return redissonClient.getMap(name);
    }

    /**
     * 向Map中设置值
     *
     * @param name
     * @param data
     * @param <K>
     * @param <V>
     */
    public <K, V> void setMapValues(String name, Map<K, V> data){
        RMap<K, V> map = redissonClient.getMap(name);
        map.putAll(data);
    }

    /**
     * 向Map中设置值
     *
     * @param name
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     */
    public <K, V> void setMapValue(String name, K key, V value){
        RMap<K, V> map = redissonClient.getMap(name);
        map.put(key, value);
    }

    /**
     * 从Map中获取值
     *
     * @param name
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> V getMapValue(String name, K key){
        RMap<K, V> map = redissonClient.getMap(name);
        return map.get(key);
    }

    /**
     * 删除Map
     *
     * @param name
     */
    public void deleteMap(String name){
        RMap map = redissonClient.getMap(name);
        map.delete();
    }
}
