package org.codwh.redis.custom.dao;

import org.apache.ibatis.annotations.Param;
import org.codwh.redis.custom.constant.PageProperties;

import java.util.List;

/**
 * 分页接口，定义了DAO分页的规范，如果有DAO需要实现分页，那么需要继承这个接口
 */
public interface IPaginationDao<T> {

    /**
     * 根据过滤条件查询出数据列表(用于分页)
     *
     * @param page        分页参数对象
     * @param queryObject 过滤条件对象
     * @return
     */
    List<T> queryPageList(@Param("page") PageProperties page, @Param("queryObject") T queryObject);

    /**
     * 根据过滤条件统计出记录条数(用于分页)
     *
     * @param queryObject 过滤条件对象
     * @return
     */
    int count(@Param("queryObject") T queryObject);
}
