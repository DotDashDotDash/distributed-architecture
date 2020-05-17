package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemApplicationMonitorDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemApplicationMonitor;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemApplicationMonitorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SystemApplicationMonitorService extends AbstractBaseService<SystemApplicationMonitor> implements ISystemApplicationMonitorService {

    @Resource
    private ISystemApplicationMonitorDao systemApplicationMonitorDao;

    @Override
    protected IMyBatisRepository<SystemApplicationMonitor> getMyBatisRepository() {
        return this.systemApplicationMonitorDao;
    }

    @Override
    public PageList<SystemApplicationMonitor> queryPageList(PageProperties pageProperties, SystemApplicationMonitor queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemApplicationMonitor>(systemApplicationMonitorDao)
                .queryPageList(pageProperties, queryObject);
    }
}
