package com.xinhuanet.pay.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.dao.PayOrderDao;
import com.xinhuanet.pay.mapper.OrdersRowMapper;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.util.Function;

@Component
public class PayOrderDaoImpl extends BaseDAO implements PayOrderDao {
	private static final Logger logger = Logger.getLogger(PayOrderDaoImpl.class);
	
	@Override
	public int addOrder(Order od) {
		String sql = "insert into PAY_ORDERS(" +
				"id,uid,loginname,money,appid,apporderid,paystatus,paytime," +
				"paytype,gateid,gatename,ipaddress,pid,merpriv,ext,orderType," +
				"beforemoney,oldOrderId,settleAccounts,settleDate,repair,repairDate) " +
		" values(?,?,?,?,?,?,?,?," +
		"		?,?,?,?,?,?,?,?," +
		"		?,?,?,?,?,?)";
		
		Object[] params = new Object[] {
				od.getId(),
				od.getUid(),
				od.getLoginName(),
				od.getMoney(),
				od.getAppId(),
				od.getAppOrderId(),
				od.getPayStatus(),
				od.getPayTime(),
				od.getPayType(),
				od.getGateId(),
				od.getGateName(),
				od.getIpAddress(),
				od.getPid(),
				od.getMerpriv(),
				od.getExt(),
				od.getOrderType(),
				od.getBeforeMoney(),
				od.getOldOrderId(),
				od.getSettleAccounts(),
				od.getSettleDate(),
				od.getRepair(),
				od.getRepairDate()
			};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public List<Order> getOrderList(String uid,PageRollModel model) {
		String sql = "SELECT * FROM PAY_ORDERS WHERE uid=? and paystatus <> ?  order by paytime desc limit ?,?";
		Object[] params = new Object[] {
				uid,
				OrderStatus.ORDER_STATUS_INVALID,
				model.getStartIndex(),
				model.getPageCount()};
		List<Order> list = getJdbcTemplate(READ).query(sql, params, new OrdersRowMapper());
		return list;
	}
	
	@Override
	public List<Order> getOrderByOldOrdIdList(String oldOrdId) {
		String sql = "SELECT * FROM PAY_ORDERS WHERE oldOrderId = ? order by paytime desc";
		Object[] params = new Object[] {
				oldOrdId
				};
		List<Order> list = getJdbcTemplate(READ).query(sql, params, new OrdersRowMapper());
		return list;
	}
	
	@Override
	public int getOrderCount(String uid) {
		String sql = "SELECT count(*) as totalCount FROM PAY_ORDERS WHERE uid=? and paystatus <> ? order by paytime desc";
		Object[] params = new Object[] {
				uid,
				OrderStatus.ORDER_STATUS_INVALID};
		int count = getJdbcTemplate(READ).queryForInt(sql, params);
		return count;
	}


	@Override
	public Order getOrderById(String orderId) {
		String  sql = "SELECT * FROM PAY_ORDERS WHERE id=:id ";
		Map<String,Object> parmap = new HashMap<String,Object>();
		parmap.put("id", orderId);
		List<Order> list = getNamedParameterJdbcTemplate(READ).query(sql, parmap, new OrdersRowMapper());
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public int updateOrderStatus(Order od) {
		String sql = "update PAY_ORDERS set changetime=?,paystatus=?,exception=?,trxId=? where id=? and uid=?";
		Object[] params = new Object[] {
				od.getChangeTime(),
				od.getPayStatus(),
				od.getException(),
				od.getTrxId(),
				od.getId(),
                od.getUid()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
	
	@Override
	public int updateOrderChangeTime(Order od) {
		String sql = "update PAY_ORDERS set changetime=? where id=? and uid=?";
		Object[] params = new Object[] {
				od.getChangeTime(),
				od.getId(),
                od.getUid()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
}