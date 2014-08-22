package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.PayIncome;

public interface PayIncomeService {

	/**
	 * 像汇总表添加一条成功订单信息
	 * @param pi
	 * @return
	 */
	public int addIncome(PayIncome pi);
}
