package org.codwh.redis.custom.constant;

import java.io.Serializable;

public class PageProperties implements Serializable {

    private static final long serialVersionUID = -3466241739977408361L;

    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 9999;

    /*默认每页记录数*/
    private int pageIndex = DEFAULT_PAGE_INDEX;
    /*不分页时查询记录数*/
    private int pageSize;
    /*查询记录开始下标*/
    private int begin;

    public PageProperties(Integer pageIndex, Integer pageSize) {
        this.pageIndex = (pageIndex != null && pageIndex > 0) ? pageIndex : DEFAULT_PAGE_INDEX;
        this.pageSize = (pageSize != null && pageSize > 0) ? pageSize : DEFAULT_PAGE_SIZE;
        this.begin = (this.pageIndex - 1) * this.pageSize;
    }

    public PageProperties(Integer pageIndex, Integer pageSize, Integer offset) {
        this.pageIndex = (pageIndex != null && pageIndex > 0) ? pageIndex : DEFAULT_PAGE_INDEX;
        this.pageSize = (pageSize != null && pageSize > 0) ? pageSize : DEFAULT_PAGE_SIZE;
        this.begin = offset + (this.pageIndex - 1) * this.pageSize;
    }

    /**
     * 获取默认分页参数(10页)
     *
     * @param pageIndex
     * @return
     */
    public static PageProperties getInstance(Integer pageIndex){
        return new PageProperties(pageIndex, null);
    }

    /**
     * 不分页查询限制记录条数(避免内存崩溃
     *
     * @return
     */
    public static PageProperties getInstance(){
        return new PageProperties(DEFAULT_PAGE_INDEX, MAX_PAGE_SIZE);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }
}
