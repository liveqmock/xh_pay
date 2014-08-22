package com.xinhuanet.pay.common;

import org.springframework.stereotype.Component;

@Component
public class PageRollModel {
	
	/**
	 * 每一页显示的元素个数
	 */
	private int pageCount=30;
	/**
	 * 页数
	 */
	private int pageSize;
	/**
	 * 当前页
	 */
	private int currentPage=1;
	/**
	 * 记录总数
	 */
	private int totalCount = 0;
	
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageSize() {
		this.setPageSize((totalCount-1)/pageCount+1);
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * 依据[当前页-1]*每页条数，获取开始索引
	 * @return 返回开始的索引
	 */
	public int getStartIndex(){
		return (currentPage-1)*pageCount;
	}
	/**
	 * WEB页面显示的行号
	 * @return
	 */
	public int getStartRow(){
		return (currentPage - 1) * pageCount + 1;
	}
}
