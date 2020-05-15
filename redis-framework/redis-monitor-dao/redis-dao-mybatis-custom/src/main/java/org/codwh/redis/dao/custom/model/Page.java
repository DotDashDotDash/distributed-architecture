package org.codwh.redis.dao.custom.model;

import java.io.Serializable;

public final class Page implements Serializable {

    private static final long serialVersionUID = -8554742105896450917L;

    private int firstPage;
    private int lastPage;
    private int nextPage;
    private int currentPage;
    private int previousPage;
    private int totalPage;
    private int rowCount;
    private int pageSize;
    private boolean hasNext;
    private boolean hasLast;
    /*页面最多显示的切页选项数*/
    private int switchablePageCount = 5;

    private Page(int currentPage, int pageSize, int rowCount){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.rowCount = rowCount;
        this.totalPage = this.rowCount % pageSize == 0 ? this.rowCount / pageSize : this.rowCount / pageSize + 1;

    }
}
