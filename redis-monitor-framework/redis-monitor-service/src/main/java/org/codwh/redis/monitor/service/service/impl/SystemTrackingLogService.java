package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemTrackingLogDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemTrackingLog;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemTrackingLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SystemTrackingLogService extends AbstractBaseService<SystemTrackingLog> implements ISystemTrackingLogService {

    @Resource
    private ISystemTrackingLogDao systemTrackingLogDao;

    @Override
    protected IMyBatisRepository<SystemTrackingLog> getMyBatisRepository() {
        return this.systemTrackingLogDao;
    }

    @Override
    public PageList<SystemTrackingLog> queryPageList(PageProperties pageProperties, SystemTrackingLog queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemTrackingLog>(systemTrackingLogDao)
                .queryPageList(pageProperties, queryObject);
    }
}
