package com.xinhuanet.pay.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.RefundApplyDao;
import com.xinhuanet.pay.mapper.RefundApplyRowMapper;
import com.xinhuanet.pay.po.RefundApply;

@Component
public class RefundApplyDaoImpl extends BaseDAO implements RefundApplyDao {
	private static final Logger logger = Logger.getLogger(RefundApplyDaoImpl.class);
	
	@Override
	public RefundApply get(Long id) {
		String sql = "select * from pay_refund_apply where orderid=?";
		Object[] params = new Object[] {
				id
			};
		List<RefundApply> list = getJdbcTemplate(READ).query(sql, params, new RefundApplyRowMapper());
        return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public RefundApply getByOrderId(String orderId) {
		String sql = "select * from pay_refund_apply where orderid=? order by handleTime desc";
		Object[] params = new Object[] {
				orderId
			};
		List<RefundApply> list = getJdbcTemplate(READ).query(sql, params, new RefundApplyRowMapper());
        return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public int add(RefundApply refApply) {
		String sql = "insert into pay_refund_apply("
			+ "orderid,uid,loginname,trxid,refordid,appid,money,paytype,"
			+ "reason,step,status,apply,handleTime,comment" 
			+ ") values ("
			+ "?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?" 
			+ ")";
		
		Object[] params = new Object[] {
				refApply.getOrderId(),
				refApply.getUid(),
				refApply.getLoginName(),
				refApply.getTrxId(),
				refApply.getRefOrdId(),
				refApply.getAppId(),
				refApply.getMoney(),
				refApply.getPayType(),
				
				refApply.getReason(),
				refApply.getStep(),
				refApply.getStatus(),
				refApply.getApply(),
				refApply.getHandleTime(),
				refApply.getComment()
			};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public int update(RefundApply refundApply) {
		String sql = "update pay_refund_apply set reason=?,step=?,status=?,handleTime=? where orderid=?";
		Object[] params = new Object[] {
				refundApply.getReason(),
				refundApply.getStep(),
				refundApply.getStatus(),
				refundApply.getHandleTime(),
				refundApply.getOrderId()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
}