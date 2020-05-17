package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IRedisClusterMonitorLogDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.redis.RedisMonitorLog;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IRedisClusterMonitorLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RedisMonitorLogService extends AbstractBaseService<RedisMonitorLog> implements IRedisClusterMonitorLogService {

    @Resource
    private IRedisClusterMonitorLogDao redisClusterMonitorLogDao;

    @Override
    protected IMyBatisRepository<RedisMonitorLog> getMyBatisRepository() {
        return this.redisClusterMonitorLogDao;
    }

    @Override
    public PageList<RedisMonitorLog> queryPageList(PageProperties pageProperties, RedisMonitorLog queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<RedisMonitorLog>(redisClusterMonitorLogDao)
                .queryPageList(pageProperties, queryObject);
    }
}
