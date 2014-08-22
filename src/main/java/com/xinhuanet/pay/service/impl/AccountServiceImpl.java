package com.xinhuanet.pay.service.impl;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.dao.AccountDao;
import com.xinhuanet.pay.exception.AccountErrorException;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.service.AccountService;

@Component
public class AccountServiceImpl implements AccountService {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	public AccountDao dao;
	
	@Override
	public Account getAccount(String uid) {
		Account account = dao.getAccount(uid);
		return account;
	}
	@Override
	public int addAccountDeposit(String uid, double money, String lastOrderId) {
		return dao.addAccountDeposit(uid, money, lastOrderId);
	}

	@Override
	public int addAccountCash(String uid, double money,String lastOrderId) {
		return dao.addAcountCash(uid, money,lastOrderId);
	}

	@Override
	public int subAccountAmt(String uid, double money) {
		return dao.subAccountAmt(uid, money);
	}
	
	@Override
	public boolean checkAccountExists(String uid, String loginName) {
		Account account  = dao.getAccount(uid, loginName);
		boolean exists = true;
		if(account == null){
			exists = false;
		}
		return exists;
	}

	@Override
	public boolean initializeAccount(String uid, String loginName) {
		int i = dao.initializeAccount(uid, loginName);
		boolean b = true;
		if(i != 1){
			b= false;
		}
		return b;
	}

	@Override
	public void filterAccount(String uid, String loginName) throws AccountErrorException {
		boolean exists = this.checkAccountExists(uid, loginName);
		if(!exists){
			if(!this.initializeAccount(uid, loginName)){
				throw new AccountErrorException("创建用户失败");
			} else {
				logger.info("系统新增用户,用户名:"+loginName + ",用户ID:"+uid);
			}
		}
	}
	@Override
	public boolean updAccountEmail(String uid, String loginName, String emailID) {
		int i = dao.updAccountEmail(uid, loginName, emailID);
		boolean b = true;
		if(i != 1){
			b= false;
		}
		return b;
	}
	
	
	@Override
	public int queryAccountByEmail(String emailID) {
		return dao.queryAccountByEmail(emailID);
	}
}
