package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.Account;

/**
 * @author duanwc
 * 封装Account对象
 */
public class AccountRowMapper implements RowMapper<Object> {
	public Object mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Account p = new Account();
		p.setUid(rs.getString("uid"));
		p.setLoginName(rs.getString("loginname"));
		p.setMoney(rs.getDouble("money"));
		p.setDeposit(rs.getDouble("deposit"));
		p.setLevel(rs.getInt("level"));
		p.setStatus(rs.getInt("status"));
		p.setType(rs.getInt("type"));
		p.setCreatetime(rs.getTimestamp("createtime"));
		p.setUpdatetime(rs.getTimestamp("updatetime"));
		p.setComment(rs.getString("comment"));
		p.setEmail(rs.getString("email"));
		p.setEmailStatus(rs.getInt("emailStatus"));
		p.setMobile(rs.getString("mobile"));
		p.setMobileStatus(rs.getInt("mobileStatus"));
		p.setPayCode(rs.getString("paycode"));
		p.setPayCodeStatus(rs.getInt("paycodeStatus"));
		return p;
	}
}
