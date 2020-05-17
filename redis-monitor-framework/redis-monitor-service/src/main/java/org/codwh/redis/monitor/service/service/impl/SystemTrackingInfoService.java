package org.codwh.redis.monitor.service.service.impl;

import org.codwh.common.exception.DataPersistentException;
import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemTrackingInfoDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemTrackingInfo;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemTrackingInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemTrackingInfoService extends AbstractBaseService<SystemTrackingInfo> implements ISystemTrackingInfoService {

    @Resource
    private ISystemTrackingInfoDao systemTrackingInfoDao;

    @Override
    protected IMyBatisRepository<SystemTrackingInfo> getMyBatisRepository() {
        return this.systemTrackingInfoDao;
    }

    @Override
    public PageList<SystemTrackingInfo> queryPageList(PageProperties pageProperties, SystemTrackingInfo queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemTrackingInfo>(systemTrackingInfoDao)
                .queryPageList(pageProperties, queryObject);
    }
}
