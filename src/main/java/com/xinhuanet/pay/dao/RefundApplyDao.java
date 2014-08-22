package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.RefundApply;

public interface RefundApplyDao {
	/**
	 * 通过id获取一条退款申请实例
	 * @param orderId 原订单ID
	 * @return 退款申请实例
	 */
	public RefundApply get(Long id);
	
	/**
	 * 通过id获取一条退款申请实例
	 * @param orderId 原订单ID
	 * @return 退款申请实例
	 */
	public RefundApply getByOrderId(String orderId);
	
	/**
	 * 生成一条退款申请实例
	 * @param order 初始化的订单对象
	 */
	public int add(RefundApply refApply);

	/**
	 * 更新应用表
	 * @param refundApply
	 * @return
	 */
	public int update(RefundApply refundApply);
}
