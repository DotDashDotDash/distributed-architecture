package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IRedisClusterSlaveDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.redis.RedisClusterSlave;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IRedisClusterSlaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RedisClusterSlaveService extends AbstractBaseService<RedisClusterSlave> implements IRedisClusterSlaveService {

    @Resource
    private IRedisClusterSlaveDao redisClusterSlaveDao;

    @Override
    protected IMyBatisRepository<RedisClusterSlave> getMyBatisRepository() {
        return this.redisClusterSlaveDao;
    }

    @Override
    public PageList<RedisClusterSlave> queryPageList(PageProperties pageProperties, RedisClusterSlave queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<RedisClusterSlave>(redisClusterSlaveDao)
                .queryPageList(pageProperties, queryObject);
    }
}
