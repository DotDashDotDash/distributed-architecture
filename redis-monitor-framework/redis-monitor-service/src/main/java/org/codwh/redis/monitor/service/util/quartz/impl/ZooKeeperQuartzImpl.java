package org.codwh.redis.monitor.service.util.quartz.impl;

import org.codwh.redis.custom.dao.IRedisClusterMasterDao;
import org.codwh.redis.custom.dao.IRedisClusterSlaveDao;
import org.codwh.redis.monitor.service.util.quartz.IQuartzFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("zooKeeperQuartz")
public class ZooKeeperQuartzImpl implements IQuartzFramework {

    @Autowired
    private IRedisClusterMasterDao redisClusterMasterDao;

    @Autowired
    private IRedisClusterSlaveDao redisClusterSlaveDao;

    @Override
    public String invoke(int times) {
        return null;
    }
}
