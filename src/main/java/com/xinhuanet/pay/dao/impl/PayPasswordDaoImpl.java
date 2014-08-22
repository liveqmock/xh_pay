package com.xinhuanet.pay.dao.impl;

import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.PayPasswordDao;

@Component
public class PayPasswordDaoImpl extends BaseDAO implements PayPasswordDao {

	@Override
	public int updatePayPsw(String loginName, String pwd) {
		String sql = "update PAY_ACCOUNT set paycode=?,paycodeStatus=1 where loginname=?";
		Object[] params = {pwd,loginName};
		return this.getJdbcTemplate().update(sql, params);
	}

	@Override
	public int updatePayPsw(Long userId, String pwd) {
		String sql = "update PAY_ACCOUNT set paycode=?,paycodeStatus=1 where uid=?";
		Object[] params = {pwd,userId};
		return this.getJdbcTemplate().update(sql, params);
	}

}
