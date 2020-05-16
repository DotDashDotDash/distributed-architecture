package org.codwh.redis.custom.dao;

import java.util.List;

/**
 * 所有的Dao定义接口并继承IMyBatisRepository，由Spring Context扫描，动态生成代理类
 */
public interface IMyBatisRepository<T> {

    /**
     * 插入操作
     *
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据主键来更新
     *
     * @param t
     * @return
     */
    int updateById(T t);

    /**
     * 根据主键来删除
     *
     * @param id
     * @return
     */
    int deleteById(int id);

    /**
     * 通过主键来获取
     *
     * @param id
     * @return
     */
    T getById(int id);

    /**
     * 根据Criteria查询
     *
     * @param queryObject
     * @return
     */
    List<T> getListByCriteria(T queryObject);
}
