package com.xinhuanet.pay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.xinhuanet.pay.po.AppOrder;

public class AppOrderRowMapper implements RowMapper<AppOrder> {

	/**
	 * 应用订单对象
	 */
	@Override
	public AppOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		AppOrder order = new AppOrder();
        order.setId(rs.getString("id"));
        order.setOrderId(rs.getString("orderid"));
        order.setUid(rs.getString("uid"));
        order.setLoginName(rs.getString("loginname"));
        order.setVersion(rs.getString("version"));
        order.setAppId(rs.getInt("appid"));
        order.setPid(rs.getString("pid"));
        order.setPname(rs.getString("pname"));
        order.setMerPriv(rs.getString("merpriv"));
        order.setMoney(rs.getDouble("money"));
        order.setRetUrl(rs.getString("returl"));
        order.setBgRetUrl(rs.getString("bgreturl"));
        order.setStatus(rs.getInt("status"));
        order.setQuartzStatus(rs.getInt("quartzstatus"));
        order.setIpAddress(rs.getString("ipaddress"));
        order.setAddTime(rs.getTimestamp("addtime"));
        order.setExt(rs.getString("ext"));
        order.setOrderType(rs.getInt("orderType"));
        order.setOrderTime(rs.getTimestamp("orderTime"));
        order.setTrxId(rs.getString("trxId"));
        order.setChangeTime(rs.getTimestamp("changetime"));
        order.setTradeStatus(rs.getInt("tradestatus"));
        return order;
	}

}
