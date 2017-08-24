package com.portal.common.page;

import java.util.ArrayList;
import java.util.List;

public class PageResponseUtil {

	/**
	 * used to generate a PageResponse with list
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param list
	 * @return
	 */
	public static <T> PageResponse<T> getPageResponse(int pageIndex, int pageSize, List<T> list) {
		if (list == null) {
			list = new ArrayList<T>();
		}
		int count = list.size();
		PageResponse<T> ret = new PageResponse<T>();
		ret.setPageSize(pageSize);
		ret.setRecordCount(count);
		if (count < 1)
			return ret;
		int pageCount = (count / pageSize);
		if (pageCount * pageSize < count) {
			pageCount++;
		}

		ret.setPageCount(pageCount);
		ret.setPageIndex(pageIndex);
		if (pageCount < pageIndex)
			return ret;
		if (count < pageSize)
			pageSize = count;

		int retCount = pageSize;
		if ((count - pageIndex * pageSize) <= pageSize) {
			retCount = count;
		} else {
			retCount = (pageIndex + 1) * pageSize;
		}
		ret.setDetails(list.subList(pageIndex * pageSize, retCount));
		ret.setPageRecordCount(retCount);
		return ret;
	}

	/**
	 * used to generate a PageResponse with pageData
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param pageCount
	 * @param pageData
	 * @param totalElementCount
	 * @return
	 */
	public static <T> PageResponse<T> getPageResponse(int pageIndex, int pageSize, int pageCount, List<T> pageData,
			int totalElementCount) {
		PageResponse<T> ret = new PageResponse<T>();
		ret.setDetails(pageData);
		ret.setPageCount(pageCount);
		ret.setPageIndex(pageIndex);
		ret.setPageRecordCount(pageData.size());
		ret.setPageSize(pageSize);
		ret.setRecordCount(totalElementCount);
		return ret;
	}
}
