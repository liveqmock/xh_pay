package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.RefundApply;

public class RefundApplyRowMapper implements RowMapper<RefundApply> {

	@Override
	public RefundApply mapRow(ResultSet rs, int num) throws SQLException {
		RefundApply o = new RefundApply();
		o.setUid(rs.getString("uid"));
		o.setLoginName(rs.getString("loginname"));
		o.setOrderId(rs.getString("orderid"));
		o.setTrxId(rs.getString("trxid"));
		o.setRefOrdId(rs.getString("refordid"));
		o.setAppId(rs.getInt("appid"));
		o.setMoney(rs.getDouble("money"));
		o.setPayType(rs.getString("paytype"));
		o.setReason(rs.getString("reason"));
		o.setStatus(rs.getInt("status"));
		o.setStep(rs.getInt("step"));
		o.setApply(rs.getInt("apply"));
		o.setHandleTime(rs.getTimestamp("handleTime"));
		o.setComment(rs.getString("comment"));
		return o;
	}

}
