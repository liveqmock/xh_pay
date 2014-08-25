package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.PayDetailAndRefundApply;
import com.xinhuanet.platform.base.AppType;

/**
 * 封装订单详细信息与订单退款信息
 * @author bahaidong
 *
 */
public class PayDetailAndRefundApplyRowMapper implements RowMapper<PayDetailAndRefundApply> {

	@Override
	public PayDetailAndRefundApply mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PayDetailAndRefundApply p = new PayDetailAndRefundApply();
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
		
		p.setTrxId(rs.getString("trxid"));
		p.setRefOrdId(rs.getString("refordid"));
		p.setRefundMoney(rs.getDouble("refundmoney"));
		p.setPayType(rs.getString("paytype"));
		p.setReason(rs.getString("reason"));
		p.setRefundStatus(rs.getInt("refundstatus"));
		p.setStep(rs.getInt("step"));
		p.setApply(rs.getInt("apply"));
		p.setHandleTime(rs.getTimestamp("handleTime"));
		p.setComment(rs.getString("comment"));
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(p.getAddTime());
		calendar.add(Calendar.DATE, 7);
		Long orderLong = calendar.getTimeInMillis();
		Long currentLong = new Date().getTime();//当前时间
		if(orderLong < currentLong){//判断订单是否在有效期内
			p.setCanRefund("0");
		}else{
			p.setCanRefund("1");
		}
		
		return p;
	}

}
