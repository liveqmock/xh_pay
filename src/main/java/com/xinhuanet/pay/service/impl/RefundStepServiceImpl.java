package com.xinhuanet.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.dao.RefundStepDao;
import com.xinhuanet.pay.po.RefundStep;
import com.xinhuanet.pay.service.RefundStepService;

@Component
public class RefundStepServiceImpl implements RefundStepService {
	@Autowired
	public RefundStepDao dao;

	@Override
	public int add(RefundStep step) {
		int i = dao.add(step);
		return i;
	}

	@Override
	public RefundStep get(Integer id) {
		RefundStep step = dao.get(id);
		return step;
	}

	@Override
	public List<RefundStep> getList(String orderId) {
		List<RefundStep> list = dao.getList(orderId);
		return list;
	}

	
}