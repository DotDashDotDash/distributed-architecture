package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IPsMenuDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.ps.PsMenu;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IPsMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class PsMenuService extends AbstractBaseService<PsMenu> implements IPsMenuService {

    @Resource
    private IPsMenuDao psMenuDao;

    @Override
    protected IMyBatisRepository<PsMenu> getMyBatisRepository() {
        return this.psMenuDao;
    }

    @Override
    public List<PsMenu> getMenusByUserId(PsMenu psMenu) {
        return psMenuDao.getMenusByUserId(psMenu);
    }

    @Override
    public PageList<PsMenu> queryPageList(PageProperties pageProperties, PsMenu queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<PsMenu>(psMenuDao)
                .queryPageList(pageProperties, queryObject);
    }
}
