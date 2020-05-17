package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemComponentDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemComponent;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemComponentService extends AbstractBaseService<SystemComponent> implements ISystemComponentService {

    @Resource
    private ISystemComponentDao systemComponentDao;

    @Override
    protected IMyBatisRepository<SystemComponent> getMyBatisRepository() {
        return this.systemComponentDao;
    }

    @Override
    public List<SystemComponent> getListByApplicationId(Integer applicationId) {
        return systemComponentDao.getListByApplicationId(applicationId);
    }

    @Override
    public PageList<SystemComponent> queryPageList(PageProperties pageProperties,
                                                   SystemComponent queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemComponent>(systemComponentDao).queryPageList(pageProperties, queryObject);
    }
}
