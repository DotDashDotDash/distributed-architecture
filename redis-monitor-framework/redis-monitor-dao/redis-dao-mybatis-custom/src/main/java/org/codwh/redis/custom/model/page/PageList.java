package org.codwh.redis.custom.model.page;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 分页查询
 */
public class PageList<T> implements Serializable {

    private static final long serialVersionUID = -5683917844520247517L;

    /*业务数据列表*/
    private final List<T> data;

    /*分页的基本信息*/
    private final Page page;

    private PageList(List<T> data, Page page) {
        this.data = (data == null) ? new LinkedList<T>() : data;
        this.page = (page == null) ? Page.createNewBlankPage() : page;
    }

    /**
     * 一个新的空白实例
     *
     * @param <T>
     * @return
     */
    public static <T> PageList<T> getEmptyInstance() {
        return new PageList<T>(null, null);
    }

    /**
     * 获得一个新的实例，静态方法调用
     *
     * @param data
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageList<T> getInstance(List<T> data, Page page) {
        if (data == null) {
            throw new RuntimeException("业务数据不能为空!");
        }
        if (page == null) {
            throw new RuntimeException("分页数据不能为空!");
        }
        return new PageList<T>(data, page);
    }

    public List<T> getData() {
        return data;
    }

    public Page getPage() {
        return page;
    }
}
