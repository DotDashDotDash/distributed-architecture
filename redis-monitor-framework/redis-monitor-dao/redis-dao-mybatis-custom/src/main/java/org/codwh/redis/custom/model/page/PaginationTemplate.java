package org.codwh.redis.custom.model.page;

import org.codwh.redis.custom.constant.PageProperties;
import org.codwh.redis.custom.dao.IPaginationDao;

import java.util.List;

public class PaginationTemplate<T> {

    /**
     * MyBatisDao
     */
    private IPaginationDao<T> paginationDao;

    /**
     * 封闭无参构造器，必须持有Dao
     */
    public PaginationTemplate(IPaginationDao<T> paginationDao) {
        this.paginationDao = paginationDao;
    }

    /**
     * 分页查询数据方法
     *
     * @param pageProperties 分页参数对象
     * @param queryObject    过滤条件对象
     */
    public PageList<T> queryPageList(PageProperties pageProperties, T queryObject) {

        if (null == pageProperties)
            pageProperties = PageProperties.getInstance();

        //count记录数
        int count = paginationDao.count(queryObject);
        if (0 == count)
            return PageList.<T>getEmptyInstance();
        List<T> data = paginationDao.queryPageList(pageProperties, queryObject);
        return PageList.getInstance(data, Page.getInstance(pageProperties, count));
    }


}
