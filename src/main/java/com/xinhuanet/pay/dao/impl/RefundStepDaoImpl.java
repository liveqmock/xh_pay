package com.xinhuanet.pay.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.RefundApplyDao;
import com.xinhuanet.pay.dao.RefundStepDao;
import com.xinhuanet.pay.mapper.RefundApplyRowMapper;
import com.xinhuanet.pay.mapper.RefundStepRowMapper;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.po.RefundStep;

@Component
public class RefundStepDaoImpl extends BaseDAO implements RefundStepDao {
	private static final Logger logger = Logger.getLogger(RefundStepDaoImpl.class);
		
	@Override
	public int add(RefundStep step) {
		String sql = "insert into pay_refund_step("
			+ "orderid,reason,status,step,handleTime,ipaddress,admin"
			+ ") values ("
			+ "?,?,?,?,?,?,?)";
		
		Object[] params = new Object[] {
				step.getOrderId(),
				step.getReason(),
				step.getStatus(),
				step.getStep(),
				step.getHandleTime(),
				step.getIpAddress(),
				step.getAdmin()
			};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}

	@Override
	public RefundStep get(Integer id) {
		String sql = "select * from pay_refund_step where id=?";
		Object[] params = new Object[] {
				id
			};
		List<RefundStep> list = getJdbcTemplate(READ).query(sql, params, new RefundStepRowMapper());
        return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public List<RefundStep> getList(String orderId) {
		String sql = "select * from pay_refund_step where orderid=? order by handleTime desc";
		Object[] params = new Object[] {
				orderId
			};
		List<RefundStep> list = getJdbcTemplate(READ).query(sql, params, new RefundStepRowMapper());
        return list;
	}
	

}