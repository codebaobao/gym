package com.portal.common.page;

import java.util.List;

/**
 * Created on 2015/8/24.
 */
public class PageRes<T>{

    private List<T> details;
    private int recordCount;
    private int pageIndex;
    private int pageSize;
    private int pageCount;

    public List<T> getDetails() {
        return details;
    }
    public void setDetails(List<T> details) {
        this.details = details;
    }
    public int getRecordCount() {
        return recordCount;
    }
    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
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
    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    @Override
    public String toString() {
        return "PageResponse [details=" + details + ", recordCount="
                + recordCount + ", pageIndex=" + pageIndex + ", pageSize="
                + pageSize + ", pageCount=" + pageCount + "]";
    }
}
