package com.xinhuanet.pay.dao;

import java.util.List;

import com.xinhuanet.pay.po.RefundStep;

public interface RefundStepDao {
	/**
	 * 生成一条退款申请流程实例
	 * @param step 申请过程对象
	 */
	public int add(RefundStep step);
	
	/**
	 * 通过id获取一条申请过程实例
	 * @param id 申请过程ID
	 * @return 申请过程实例
	 */
	public RefundStep get(Integer id);
	
	/**
	 * 通过订单ID获取该订单所有的退款申请过程实例
	 * @param orderId 原订单ID
	 * @return 退款申请实例
	 */
	public List<RefundStep> getList(String orderId);
}
