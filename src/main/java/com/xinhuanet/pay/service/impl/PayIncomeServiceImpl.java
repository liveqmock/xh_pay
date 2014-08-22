package com.xinhuanet.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.dao.PayIncomeDao;
import com.xinhuanet.pay.po.PayIncome;
import com.xinhuanet.pay.service.PayIncomeService;

@Service
public class PayIncomeServiceImpl implements PayIncomeService{
	
	@Autowired
	private PayIncomeDao pidao;

	@Override
	public int addIncome(PayIncome pi) {
		return pidao.addIncome(pi);
	}

}
