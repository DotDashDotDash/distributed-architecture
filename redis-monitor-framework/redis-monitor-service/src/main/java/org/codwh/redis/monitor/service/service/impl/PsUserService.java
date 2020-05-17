package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IPsUserDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.ps.PsUser;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IPsUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PsUserService extends AbstractBaseService<PsUser> implements IPsUserService {

    @Resource
    private IPsUserDao psUserDao;

    @Override
    protected IMyBatisRepository<PsUser> getMyBatisRepository() {
        return this.psUserDao;
    }

    @Override
    public PageList<PsUser> queryPageList(PageProperties pageProperties, PsUser queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<PsUser>(psUserDao)
                .queryPageList(pageProperties, queryObject);
    }
}
