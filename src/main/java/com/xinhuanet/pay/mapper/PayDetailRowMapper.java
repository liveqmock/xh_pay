package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.platform.base.AppType;

/**
 * @author duanwc
 * 封装PayGadget对象
 */
public final class PayDetailRowMapper implements RowMapper<PayDetail> {
	public PayDetail mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PayDetail p = new PayDetail();
		p.setId(rs.getString("id"));
		p.setUid(rs.getString("uid"));
		p.setLoginName(rs.getString("loginname"));
		p.setPid(rs.getString("pid"));
		p.setPname(rs.getString("pname"));
		p.setOrderId(rs.getString("orderid"));
		p.setType(rs.getInt("type"));
		p.setMoney(rs.getDouble("money"));
		p.setBeforeMoney(rs.getDouble("beforemoney"));
		p.setAfterMoney(rs.getDouble("aftermoney"));
		p.setAppId(rs.getInt("appid"));
		p.setAppName(AppType.getAppName(rs.getInt("appid")));
		p.setAddTime(rs.getTimestamp("addtime"));
		p.setIpAddress(rs.getString("ipaddress"));
		p.setExt(rs.getString("ext"));
		p.setStatus(rs.getInt("status"));
		p.setOrderType(rs.getInt("orderType"));
		return p;
	}
}