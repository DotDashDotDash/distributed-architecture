package org.codwh.redis.monitor.service.service.impl;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IMyBatisRepository;
import org.codwh.redis.custom.dao.IPsDictDao;
import org.codwh.redis.custom.model.page.PageList;
import org.codwh.redis.custom.model.page.PaginationTemplate;
import org.codwh.redis.custom.model.ps.PsDict;
import org.codwh.redis.monitor.service.service.AbstractBaseService;
import org.codwh.redis.monitor.service.service.IPsDictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PsDictService extends AbstractBaseService<PsDict> implements IPsDictService {

    @Resource
    private IPsDictDao psDictDao;

    @Override
    protected IMyBatisRepository<PsDict> getMyBatisRepository() {
        return this.psDictDao;
    }

    @Override
    public PageList<PsDict> queryPageList(PageProperties pageProperties, PsDict queryObject, Map<String, Object> otherParams) {
        return new PaginationTemplate<PsDict>(psDictDao)
                .queryPageList(pageProperties, queryObject);
    }
}
