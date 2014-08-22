package com.xinhuanet.pay.service;

import java.util.List;

import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.Order;

public interface PayOrderService {
	/**
	 * 通过订单号获取一条订单的实例
	 * @param orderId
	 * @return 订单实例
	 */
	public Order getOrderById(String orderId);
	
	/**
	 * 生成一条订单
	 * @param order 初始化的订单对象
	 */
	public int addOrder(Order od);
	
	/**
	 * 获取用户的所有订单列表
	 * @param uid 用户ID
	 * @param model 分页模型
	 * @return
	 */
	public List<Order> getOrderList(String uid,PageRollModel model);
	/**
	 * 获取用户的所有订单总数
	 * @param uid
	 * @return
	 */
	public int getOrderCount(String uid);
	
	/**
	 * 获取订单下所有退款记录，因三方支付平台支持部分金额退款，所以有可能会产生多个退款订单
	 * @param oldOrdId 原始订单号
	 * @return 该订单下的所有子订单
	 */
	public List<Order> getOrderByOldOrdIdList(String oldOrdId);
	
	/**
	 * 获取一个新的订单号
	 * @return
	 */
	public String getOrderIdNumber();
	
	/**
	 * 支付平台返回成功通知，更新订单
	 * @param od
	 * @return
	 */
	public int succeedOrder(Order od);
	/**
	 * 支付平台返回失败通知，更新订单
	 * @param od
	 * @return
	 */
	public int failOrder(Order od);
	

}
