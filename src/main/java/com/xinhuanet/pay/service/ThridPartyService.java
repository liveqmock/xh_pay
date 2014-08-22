package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;


public interface ThridPartyService<T> {
	/**
	 * 第三方支付平台交易失败后的通知
	 * @param order 支付平台订单类
	 * @param appOrder 应用订单类
	 */
	public void thridOrdFailedNotify(Order order, AppOrder appOrder);
	/**
	 * 第三方支付平台交易成功后的通知
	 * @param order 支付平台订单类
	 * @param appOrder 应用订单类
	 */
	public void thridOrdSucceedNotify(Order order,AppOrder appOrder);
	
	/**
	 * 第三方支付平台查询订单被支付状态
	 * @param ordId 订单号
	 * @return ChinapnrProperties对象。
	 * <br/>1.签名/验签错误或异常返回，验签状态
	 * <br/>2.请求数据为null或""时返回，处理状态
	 * <br/>3.正确时返回第三方平台返回的结果
	 */
	public T queryOrder(String ordId);
	
	/**
	 * 第三方支付平台单笔订单退款
	 * @param oldOrdId 原订单号
	 * @return ChinapnrProperties对象。
	 * <br/>1.签名/验签错误或异常返回，验签状态
	 * <br/>2.请求数据为null或""时返回，处理状态
	 * <br/>3.正确时返回汇付返回的结果
	 */
	public T refundOrder(String oldOrdId);
}
