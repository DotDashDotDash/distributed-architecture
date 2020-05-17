package org.codwh.redis.monitor.service.util;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import org.codwh.common.util.SpringUtils;
import org.codwh.redis.custom.model.system.SystemTrackingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.management.ObjectName;

public class RedisDistributedLock extends DistributedLock {

    Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    private String redisKey;
    private int timeout;
    private int heartbeat;
    private long versionTime;
    private ShardedJedisPool shardedJedisPool;

    public RedisDistributedLock(String redisKey, int timeout) {
        this(redisKey, timeout * 1000, timeout);
    }

    public RedisDistributedLock(String redisKey, int heartbeat, int timeout) {
        this.redisKey = redisKey;
        this.heartbeat = heartbeat;
        this.timeout = timeout;
        this.shardedJedisPool = (ShardedJedisPool) SpringUtils.getBean("sharedJedisPool");
    }


    @Override
    protected boolean lock() throws Exception {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();

        try {
            boolean setResult = setnx(this.redisKey, buildVal(), this.timeout);
            if (setResult) {
                return true;
            }
            //setResult为false，说明锁被其他占用，检查被占用的锁是否超时
            long oldValue = getLong(shardedJedis.get(redisKey));
            if (oldValue > System.currentTimeMillis()) {
                return false;   //在设置redis value的时候，value的值为设置时间+timeout，这个条件就说明锁还没有到期
            }
            //到了这一步说明对方持有的锁已经过期，开始获取锁
            Long getSetValue = getLong(shardedJedis.getSet(redisKey, buildVal()));
            if (getSetValue == 0) {
                //true代表成功，false代表锁已经被其他的获取
                return setnx(redisKey, buildVal(), timeout);
            }
            if (getSetValue != oldValue) {
                return false;
            }
            //获取成功，续租过期时间
            if (shardedJedis.expire(redisKey, timeout) == 1) {
                return true;
            }
            //续租失败，key可能失效了，再获取一次
            return setnx(redisKey, buildVal(), timeout);
        } catch (Exception e) {
            return false;
        } finally {
            shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    /**
     * 检查锁的有效性
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkLockValidity() throws Exception {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            long expiration = getLong(shardedJedis.get(redisKey));
            if (System.currentTimeMillis() < expiration && versionTime == expiration) {
                return true;
            }
        } finally {
            shardedJedisPool.returnResourceObject(shardedJedis);
        }
        return false;
    }

    /**
     * 检测心跳连接
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean heartbeat() throws Exception {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            return checkLockValidity() && getLong(shardedJedis.getSet(redisKey, buildVal())) != 0;
        } finally {
            shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    /**
     * 解锁
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean unlock() throws Exception {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            return checkLockValidity() && shardedJedis.del(redisKey) != 0;
        } finally {
            shardedJedisPool.returnResourceObject(shardedJedis);
        }
    }

    /**
     * 转换字符串至Long
     *
     * @param value
     * @return
     */
    private Long getLong(String value) {
        return value == null ? 0L : Long.valueOf(value);
    }

    /**
     * 构建versionTime
     *
     * @return
     */
    private String buildVal() {
        this.versionTime = System.currentTimeMillis() + this.heartbeat + 1;
        return String.valueOf(this.versionTime);
    }

    /**
     * 设置key-value
     *
     * @param redisKey
     * @param value
     * @param timeout
     * @return
     */
    @SuppressWarnings("all")
    private Boolean setnx(String redisKey, String value, int timeout) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();

        try {
            long v1 = shardedJedis.setnx(redisKey, value);
            long v2 = shardedJedis.expire(redisKey, timeout);

            if (v1 == 1 && v2 == 1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            shardedJedisPool.returnResourceObject(shardedJedis);
        }
        return false;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public long getVersionTime() {
        return versionTime;
    }

    public void setVersionTime(long versionTime) {
        this.versionTime = versionTime;
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }
}
