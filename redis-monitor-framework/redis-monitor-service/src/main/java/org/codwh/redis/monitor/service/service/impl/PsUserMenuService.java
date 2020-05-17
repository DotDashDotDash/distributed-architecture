package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IPsMenuUserDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.ps.PsUserMenu;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IPsUserMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PsUserMenuService extends AbstractBaseService<PsUserMenu> implements IPsUserMenuService {

    @Resource
    private IPsMenuUserDao psMenuUserDao;

    @Override
    protected IMyBatisRepository<PsUserMenu> getMyBatisRepository() {
        return this.psMenuUserDao;
    }

    @Override
    public PageList<PsUserMenu> queryPageList(PageProperties pageProperties, PsUserMenu queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<PsUserMenu>(psMenuUserDao)
                .queryPageList(pageProperties, queryObject);
    }
}
