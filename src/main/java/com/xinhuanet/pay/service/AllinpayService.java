package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.AllinpayProperties;

public interface AllinpayService extends ThridPartyService<AllinpayProperties> {
	/**
	 * 通联支付 批量查询订单
	 * @param beginDateTime 开始时间
	 * @param endDateTime 结束时间
	 * @param pageNo 页码
	 * @return
	 */
	public String batchQuery(String beginDateTime, String endDateTime, String pageNo);
}
