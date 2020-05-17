package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.ISystemApplicationDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.system.SystemApplication;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.ISystemApplicationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SystemApplicationService extends AbstractBaseService<SystemApplication> implements ISystemApplicationService {

    @Resource
    private ISystemApplicationDao systemApplicationDao;

    @Override
    protected IMyBatisRepository<SystemApplication> getMyBatisRepository() {
        return this.systemApplicationDao;
    }

    @Override
    public int updateVersionByIdAndVersion(SystemApplication systemApplication) {
        return systemApplicationDao.updateVersionByIdAndVersion(systemApplication);
    }

    @Override
    public SystemApplication getSystemApplicationByHostAndPort(String host, Integer port) throws Exception {
        SystemApplication systemApplication = new SystemApplication();
        systemApplication.setJmxHost(host);
        systemApplication.setJmxPort(port);
        List<SystemApplication> applications = super.queryList(systemApplication);
        if (applications != null && applications.size() > 0) {
            return applications.get(0);
        }
        return null;
    }

    @Override
    public PageList<SystemApplication> queryPageList(PageProperties pageProperties, SystemApplication queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<SystemApplication>(systemApplicationDao)
                .queryPageList(pageProperties, queryObject);
    }
}
