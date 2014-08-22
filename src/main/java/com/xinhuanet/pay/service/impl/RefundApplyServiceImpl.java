package com.xinhuanet.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.dao.RefundApplyDao;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.service.RefundApplyService;

@Component
public class RefundApplyServiceImpl implements RefundApplyService {
	@Autowired
	public RefundApplyDao dao;

	@Override
	public RefundApply get(Long id) {
		RefundApply refApply = dao.get(id);
		return refApply;
	}

	@Override
	public RefundApply getByOrderId(String orderId) {
		RefundApply refApply = dao.getByOrderId(orderId);
		return refApply;
	}

	@Override
	public int add(RefundApply refApply) {
		int i = dao.add(refApply);
		return i;
	}

	@Override
	public int update(RefundApply refundApply) {
		int i = dao.update(refundApply);
		return i;
	}

	
	
}