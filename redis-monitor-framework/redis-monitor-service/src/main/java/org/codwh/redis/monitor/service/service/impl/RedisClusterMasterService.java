package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IRedisClusterMasterDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.redis.RedisClusterMaster;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IRedisClusterMasterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RedisClusterMasterService extends AbstractBaseService<RedisClusterMaster> implements IRedisClusterMasterService {

    @Resource
    private IRedisClusterMasterDao redisClusterMasterDao;

    @Override
    protected IMyBatisRepository<RedisClusterMaster> getMyBatisRepository() {
        return this.redisClusterMasterDao;
    }

    @Override
    public PageList<RedisClusterMaster> queryPageList(PageProperties pageProperties, RedisClusterMaster queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<RedisClusterMaster>(redisClusterMasterDao)
                .queryPageList(pageProperties, queryObject);
    }
}
