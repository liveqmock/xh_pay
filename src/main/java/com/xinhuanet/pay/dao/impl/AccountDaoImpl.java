package com.xinhuanet.pay.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.AccountDao;
import com.xinhuanet.pay.mapper.AccountRowMapper;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.util.Function;

@Component
public class AccountDaoImpl extends BaseDAO implements AccountDao {
	private static final Logger logger = Logger.getLogger(AccountDaoImpl.class);
	
	
	@Override
	public Account getAccount(String uid) {
		String sql = "SELECT uid,loginname,money,deposit,lastorderid,level,status,type," +
				"createtime,updatetime,comment,email,emailStatus,mobile,mobileStatus,paycode,paycodeStatus FROM PAY_ACCOUNT WHERE uid=:uid";
		Account account = null;
		try{
			Map<String,String> namedParameters = Collections.singletonMap("uid", uid);
			account = (Account) getNamedParameterJdbcTemplate(READ).queryForObject(sql, namedParameters, new AccountRowMapper());
		} catch(EmptyResultDataAccessException e){//结果集长度小于1
			return null;
		} catch(IncorrectResultSizeDataAccessException e){//结果集长度大于1
			logger.error("查询用户记录大于1条", e);
			return null;
		}
		return account;
	}

	@Override
	public Account getAccount(String uid, String loginName) {
		String sql = "SELECT uid,loginname,money,deposit,lastorderid,level,status,type," +
				"createtime,updatetime,comment,email,emailStatus,mobile,mobileStatus,paycode,paycodeStatus FROM PAY_ACCOUNT WHERE uid=:uid and loginname=:loginName";
		Account account = null;
		try{
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("uid", uid);
			paramMap.put("loginName", loginName);
			Map<String,String> namedParameters = Collections.unmodifiableMap(paramMap);
			account = (Account) getNamedParameterJdbcTemplate(READ).queryForObject(sql, namedParameters, new AccountRowMapper());
		} catch(EmptyResultDataAccessException e){//结果集长度小于1
			return null;
		} catch(IncorrectResultSizeDataAccessException e){//结果集长度大于1
			logger.error("查询用户记录大于1条", e);
			return null;
		}
		return account;
	}

	@Override
	public int addAccountDeposit(String uid, double money, String lastOrderId) {
		String sql = "update PAY_ACCOUNT set deposit=deposit+?,lastorderid=?,updatetime=? where uid=?";
		Object[] params = new Object[] {
				money,
				lastOrderId,
				Function.getDateTime(),
                uid};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public int addAcountCash(String uid, double money, String lastOrderId) {
		String sql = "update PAY_ACCOUNT set money=money+?,lastorderid=?,updatetime=? where uid=?";
		Object[] params = new Object[] {
				money,
				lastOrderId,
				Function.getDateTime(),
                uid};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
	
	@Override
	public int subAccountAmt(String uid, double money) {
		String sql = "update PAY_ACCOUNT set money=money-?,updatetime=? where uid=?";
		Object[] params = new Object[] {
				money,
				Function.getDateTime(),
                uid};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public int initializeAccount(String uid, String loginName) {
		String sql = "insert into PAY_ACCOUNT(uid,loginname,createtime,comment) values(?,?,?,?)";
		Object[] params = new Object[] {
				uid,
				loginName,
				Function.getDateTime(),
				"系统初始化用户"};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public int updAccountEmail(String uid, String loginName,String emailID) {
		String sql = "update PAY_ACCOUNT set email=?,emailStatus=?,updatetime=? where uid=? and loginname=?";
		System.out.println(uid + loginName);
		Object[] params = new Object[] {
				emailID,
				1,
				Function.getDateTime(),
                uid,
                loginName
                };
		int i = getJdbcTemplate().update(sql, params);
		System.out.println("更新邮箱认证:"+i);
		return i;
	}

	@Override
	public int queryAccountByEmail(String emailID) {
		String sql = "select * from  PAY_ACCOUNT where email=?";
		Object[] params = new Object[] {emailID};
		List<Object> list = getJdbcTemplate(READ).query(sql, params, new AccountRowMapper());
		return list.size();
	}
	
}