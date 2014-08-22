package com.xinhuanet.pay.service;

import com.xinhuanet.pay.exception.AccountUnmatchErrorException;
import com.xinhuanet.pay.exception.OrderException;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.po.RefundStep;

public interface RefundOrderService {
	/**
	 * 生成一条退款申请实例
	 * @param uid 当前用户id
	 * @param order 初始化的订单对象
	 */
	public int add(String uid,RefundApply refApply) throws AccountUnmatchErrorException,OrderException ;
	
	/**
	 * 同时插入退款申请与退款步骤表中
	 * @param refundApply
	 * @param refundStep
	 * @return
	 */
	public int add(RefundApply refundApply, RefundStep refundStep);

	/**
	 * 更新应用表，并且插入步骤表
	 * @param refundApply
	 * @param refundStep
	 * @return
	 */
	public int update(RefundApply refundApply, RefundStep refundStep);
}
