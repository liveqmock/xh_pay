package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.common.RefundChangeState;
import com.xinhuanet.pay.po.RefundStep;
import com.xinhuanet.pay.util.Function;

public class RefundStepRowMapper implements RowMapper<RefundStep> {

	@Override
	public RefundStep mapRow(ResultSet rs, int num) throws SQLException {
		RefundStep o = new RefundStep();
		o.setId(rs.getInt("id"));
		o.setOrderId(rs.getString("orderid"));
		o.setReason(rs.getString("reason"));
		o.setStatus(rs.getInt("status"));
		o.setStep(rs.getInt("step"));
		o.setHandleTime(rs.getTimestamp("handleTime"));
		o.setIpAddress(rs.getString("ipaddress"));
		o.setAdmin(rs.getString("admin"));
		o.setStatusName(RefundChangeState.convertStatus(o.getStatus()));
		o.setStepName(RefundChangeState.convertStep(o.getStep()));
		o.setFormateDate(Function.getDateTimeString((Timestamp) o.getHandleTime()));
		return o;
	}

}
