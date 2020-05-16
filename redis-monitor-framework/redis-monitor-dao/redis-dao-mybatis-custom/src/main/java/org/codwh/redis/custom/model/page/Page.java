package org.codwh.redis.custom.model.page;

import org.codwh.redis.custom.constant.PageProperties;

import java.io.Serializable;

public final class Page implements Serializable {

    private static final long serialVersionUID = -8554742105896450917L;

    private int firstPage;
    private int lastPage;
    private int nextPage;
    private int currentPage;
    private int previousPage;
    private int totalPage;
    /*总记录条数*/
    private int rowCount;
    /*一页的大小*/
    private int pageSize;
    private boolean hasNext;
    private boolean hasLast;
    private boolean hasFirst;
    private boolean hasPrev;
    /*页面最多显示的切页选项数*/
    private int switchablePageCount = 5;

    private Page() {
        this(1, PageProperties.DEFAULT_PAGE_SIZE, 0);
    }

    private Page(int currentPage, int pageSize, int rowCount) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        this.totalPage = this.rowCount % pageSize == 0 ? this.rowCount / pageSize : this.rowCount / pageSize + 1;

        if (totalPage > 0) {
            this.hasFirst = true;
            this.firstPage = this.currentPage - switchablePageCount / 2;    //当前页居中
            if (this.firstPage < 0) {
                //不足以二分居中显示
                this.firstPage = 1;
            }
        }
        if (this.currentPage > 1) {
            this.hasPrev = true;
            this.previousPage = this.currentPage - 1;
        }
        if (this.totalPage > 0 && this.currentPage < this.totalPage) {
            this.hasNext = true;
            this.nextPage = this.currentPage + 1;
        }
        if (this.totalPage > 0) {
            this.hasLast = true;
            this.lastPage = this.currentPage + switchablePageCount / 2;
            if (this.lastPage > this.totalPage) {
                this.lastPage = this.totalPage;
            }
        }
    }

    /**
     * 获得一个空白的页，其中记录数为0
     *
     * @return
     */
    public static Page createNewBlankPage() {
        return new Page();
    }

    /**
     * 根据PageProperties创建一个新的页面
     *
     * @param properties
     * @param rowCount
     * @return
     */
    public static Page getInstance(PageProperties properties, int rowCount) {
        return new Page(properties.getPageIndex(), properties.getPageSize(), rowCount);
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasLast() {
        return hasLast;
    }

    public void setHasLast(boolean hasLast) {
        this.hasLast = hasLast;
    }

    public boolean isHasFirst() {
        return hasFirst;
    }

    public void setHasFirst(boolean hasFirst) {
        this.hasFirst = hasFirst;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

    public int getSwitchablePageCount() {
        return switchablePageCount;
    }

    public void setSwitchablePageCount(int switchablePageCount) {
        this.switchablePageCount = switchablePageCount;
    }
}
