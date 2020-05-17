package org.codwh.redis.zookeeper;

import org.codwh.redis.zookeeper.util.ZkUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RedisZooKeeperAutoConfiguration {

    @Bean("redisZooKeeperUtils")
    public ZkUtils zkUtils() {
        return new ZkUtils();
    }
}
