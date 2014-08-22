package com.xinhuanet.pay.dao.impl;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.PayIncomeDao;
import com.xinhuanet.pay.po.PayIncome;

@Component
public class PayIncomeDaoImpl extends BaseDAO implements PayIncomeDao {

	@Override
	public int addIncome(PayIncome pi) {
		String sql = "insert into PAY_INCOME(id,appid,money,payflatform,paytime) " +
		" values(:id,:appId,:money,:payflatform,:payTime)";
		SqlParameterSource paramSource=new BeanPropertySqlParameterSource(pi);
		int i = getNamedParameterJdbcTemplate().update(sql, paramSource);
		return i;
	}

}
