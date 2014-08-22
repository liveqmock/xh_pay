package com.xinhuanet.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AlipayService;

@Component
public class AlipayServiceImpl extends ThridPartyServiceImpl<Object> implements AlipayService<Object> {
    private static final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

	@Override
	public void thridOrdFailedNotify(Order order, AppOrder appOrder) {
		super.thridOrdFailedNotify(order, appOrder);
	}

	@Override
	public void thridOrdSucceedNotify(Order order, AppOrder appOrder) {
		super.thridOrdSucceedNotify(order, appOrder);
	}

	@Override
	public Object refundOrder(String oldOrdId) {
		
		return null;
	}
	
}
