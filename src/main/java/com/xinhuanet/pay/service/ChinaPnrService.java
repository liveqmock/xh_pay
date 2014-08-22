package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.ChinapnrProperties;


public interface ChinaPnrService extends ThridPartyService<ChinapnrProperties>{
	/**
	 * 汇付天下查询订单被支付状态
	 * @param ordId 订单号
	 * @return ChinapnrProperties对象。
	 * <br/>1.签名/验签错误或异常返回，验签状态
	 * <br/>2.请求数据为null或""时返回，处理状态
	 * <br/>3.正确时返回汇付返回的结果
	 */
	public ChinapnrProperties queryStatus(String ordId);
	/**
	 * 汇付天下单笔订单结算
	 * @param ordId 订单号
	 * @return ChinapnrProperties对象。
	 * <br/>1.签名/验签错误或异常返回，验签状态
	 * <br/>2.请求数据为null或""时返回，处理状态
	 * <br/>3.正确时返回汇付返回的结果
	 */
	public ChinapnrProperties paymentConfirm(String ordId);
	/**
	 * 汇付天下查询退款订单状态
	 * @param ordId 订单号
	 * @return ChinapnrProperties对象。
	 * <br/>1.签名/验签错误或异常返回，验签状态
	 * <br/>2.请求数据为null或""时返回，处理状态
	 * <br/>3.正确时返回汇付返回的结果
	 */
	public ChinapnrProperties queryRefundStatus(String ordId);
}
