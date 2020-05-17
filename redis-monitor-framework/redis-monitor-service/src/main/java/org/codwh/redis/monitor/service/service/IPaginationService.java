package org.codwh.redis.monitor.service.service;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.model.page.PageList;

import java.util.Map;

public interface IPaginationService<T> {

    PageList<T> queryPageList(PageProperties pageProperties, T queryObject, Map<String, Object> otherParams);
}
