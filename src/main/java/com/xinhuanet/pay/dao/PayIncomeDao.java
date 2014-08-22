package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.PayIncome;


public interface PayIncomeDao {

	/**
	 * 像汇总表添加一条成功订单信息
	 * @param pi
	 * @return
	 */
	public int addIncome(PayIncome pi);
}
