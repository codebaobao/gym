package com.portal.common.page;

import java.util.List;

public class PageResponse<T> {
	private List<T> details;
	private int recordCount;
	private int pageIndex;
	private int pageSize;
	private int pageCount;
	private int pageRecordCount;
	
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

	public int getPageRecordCount() {
		return pageRecordCount;
	}

	public void setPageRecordCount(int pageRecordCount) {
		this.pageRecordCount = pageRecordCount;
	}

	@Override
	public String toString() {
		return "PageResponse [details=" + details + ", recordCount="
				+ recordCount + ", pageIndex=" + pageIndex + ", pageSize="
				+ pageSize + ", pageCount=" + pageCount + "]";
	}
	
}
