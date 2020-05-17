package org.codwh.redis.monitor.service.service;

import org.codwh.redis.custom.model.redis.RedisConfigInfo;

public interface IRedisService {

    /**
     * 动态获取redis 使用情况
     *
     * @param host
     * @return
     */
    public RedisConfigInfo getRedisConfigInfo(String host, int port);
}
