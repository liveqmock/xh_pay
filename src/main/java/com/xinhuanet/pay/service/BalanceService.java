package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.AppOrder;

public interface BalanceService {
	
	public Boolean balancePaying(AppOrder appOrder);
}
