package com.xinhuanet.pay.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.dao.PayDetailDao;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.po.PayDetailAndRefundApply;
import com.xinhuanet.pay.service.PayDetailService;

@Component
public class PayDetailServiceImpl implements PayDetailService {
	@Autowired
	public PayDetailDao dao;

	public void addDetail(PayDetail pg) {
		dao.addDetail(pg);
	}

	public List<PayDetail> getDetailList(String uid,PageRollModel model) {
		return dao.getDetailList(uid,model);
	}

	public int getDetailCount(String uid) {
		return dao.getDetailCount(uid);
	}

	@Override
	public PayDetail get(int id) {
		return dao.get(id);
	}

	@Override
	public List<PayDetailAndRefundApply> getDetailListAndRefundApply(
			String uid, PageRollModel pageModel) {
		return dao.getDetailListAndRefundApply(uid, pageModel);
	}
	
	
}