package com.xinhuanet.pay.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.dao.PayDetailDao;
import com.xinhuanet.pay.mapper.PayDetailRowMapper;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.util.Function;

@Component
public class PayDetailDaoImpl extends BaseDAO implements PayDetailDao {
	private static final Logger logger = Logger.getLogger(PayDetailDaoImpl.class);
	
	@Override
	public void addDetail(PayDetail pg) {
		String sql = "insert into PAY_DETAIL(uid,loginname,pid,pname,orderid,money,beforemoney,aftermoney,orderType,status,type,appid,addtime,ipaddress,ext) " +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] {
				pg.getUid(),
				pg.getLoginName(),
				pg.getPid(),
				pg.getPname(),
				pg.getOrderId(),
				pg.getMoney(),
				pg.getBeforeMoney(),
				pg.getAfterMoney(),
				pg.getOrderType(),
				pg.getStatus(),
				pg.getType(),
				pg.getAppId(),
				Function.getDateTime(),
				pg.getIpAddress(),
				pg.getExt()
				};
		int i = getJdbcTemplate().update(sql, params);
	}
	
	public int getDetailCount(String uid) {
		String sql = "SELECT count(id) as totalCount FROM PAY_DETAIL WHERE uid=? order by addtime desc";
		Object[] params = new Object[] {
				uid};
		int count = getJdbcTemplate(READ).queryForInt(sql, params);
		return count;
	}

	public List<PayDetail> getDetailList(String uid,PageRollModel model) {
		String sql = "SELECT * FROM PAY_DETAIL WHERE uid=? order by addtime desc limit ?,?";
		Object[] params = new Object[] {
				uid,
				model.getStartIndex(),
				model.getPageCount()};
		List<PayDetail> list = getJdbcTemplate(READ).query(sql, params, new PayDetailRowMapper());
		return list;
	}

	@Override
	public PayDetail get(int id) {
		String sql = "SELECT * FROM PAY_DETAIL WHERE id=?";
		Object[] params = new Object[] {
				id
			};
		List<PayDetail> list = getJdbcTemplate(READ).query(sql, params, new PayDetailRowMapper());
        return (list == null || list.size() == 0) ? null : list.get(0);
	}
}