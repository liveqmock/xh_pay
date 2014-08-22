package com.xinhuanet.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.dao.PayPasswordDao;
import com.xinhuanet.pay.service.PayPasswordService;

@Service
public class PayPasswordServiceImpl implements PayPasswordService {
	private @Autowired PayPasswordDao payPwdDao;

	@Override
	public int updatePayPsw(String loginName, String pwd) {
		return payPwdDao.updatePayPsw(loginName, pwd);
	}

	@Override
	public int updatePayPsw(Long userId, String pwd) {
		return payPwdDao.updatePayPsw(userId, pwd);
	}

}
