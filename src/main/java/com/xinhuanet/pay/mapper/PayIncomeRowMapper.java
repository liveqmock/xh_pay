package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.PayIncome;

public class PayIncomeRowMapper implements RowMapper<PayIncome> {

	@Override
	public PayIncome mapRow(ResultSet rs, int num) throws SQLException {
		PayIncome pi = new PayIncome();
		pi.setId(rs.getString("id"));
		pi.setAppId(rs.getInt("appid"));
		pi.setMoney(rs.getDouble("money"));
		pi.setPayflatform(rs.getString("payflatform"));
		pi.setPayTime(rs.getDate("paytime"));
		return pi;
	}

}
