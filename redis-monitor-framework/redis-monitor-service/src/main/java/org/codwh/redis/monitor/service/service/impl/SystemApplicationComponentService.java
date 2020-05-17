package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemApplicationComponentDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemApplicationComponent;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemApplicationComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SystemApplicationComponentService extends AbstractBaseService<SystemApplicationComponent> implements ISystemApplicationComponentService {

    @Resource
    private ISystemApplicationComponentDao systemApplicationComponentDao;

    @Override
    protected IMyBatisRepository<SystemApplicationComponent> getMyBatisRepository() {
        return this.systemApplicationComponentDao;
    }

    @Override
    public PageList<SystemApplicationComponent> queryPageList(PageProperties pageProperties, SystemApplicationComponent queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemApplicationComponent>(systemApplicationComponentDao)
                .queryPageList(pageProperties, queryObject);
    }
}
