package com.xinhuanet.pay.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.PayAppOrderDao;
import com.xinhuanet.pay.mapper.AppOrderRowMapper;
import com.xinhuanet.pay.po.AppOrder;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-6-3
 * Time: 下午4:12
 */
@Component
public class PayAppOrderDaoImpl extends BaseDAO implements PayAppOrderDao {

    public List<AppOrder> getQuartzList(){
        String sql = "select * from PAY_APPORDER where status != 0 and quartzstatus != 1 ";
        List<AppOrder> list = getJdbcTemplate(READ).query(sql, new AppOrderRowMapper());
        return list;
    }

    @Override
    public boolean updateQuartzStatus(String id,int status) {
        String sql = "update PAY_APPORDER set quartzstatus = ? where id = ?";
        Object[] params = {
            status,id
        };
        int result = getJdbcTemplate().update(sql, params);
        return result > 0;
    }
    
	
	@Override
	public int insertAppOrder(AppOrder order) {
		String sql = "insert into PAY_APPORDER(" +
							"id,orderid,uid,loginname," +
							"version,appid,pid,pname," +
							"merpriv,money,bgreturl,returl," +
							"status,ipaddress,addtime,ext," +
							"orderType,orderTime" +
						") " +
					" values(" +
							"?,?,?,?," +
							"?,?,?,?," +
							"?,?,?,?," +
							"?,?,?,?," +
							"?,?" +
						")";
		Object[] params = new Object[] {
				order.getId(),
				order.getOrderId(),
				order.getUid(),
				order.getLoginName(),
				order.getVersion(),
				order.getAppId(),
				order.getPid(),
				order.getPname(),
				order.getMerPriv(),
				order.getMoney(),
				order.getBgRetUrl(),
				order.getRetUrl(),
				order.getStatus(),
				order.getIpAddress(),
				order.getAddTime(),
				order.getExt(),
				order.getOrderType(),
				order.getOrderTime()
                };
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
	
	@Override
	public AppOrder getAppOrderById(String id) {
		String  sql = "SELECT * FROM PAY_APPORDER WHERE id=:id ";
		Map<String,String> parmap = new HashMap<String,String>();
		parmap.put("id", id);
		List<AppOrder> list = getNamedParameterJdbcTemplate(READ).query(sql, parmap,
				new AppOrderRowMapper());
		return (list == null || list.size() == 0) ? null : list.get(0);
	}
	
	
	@Override
	public AppOrder getAppOrder(String appOrderId,int appId) {
		String  sql = "SELECT * FROM PAY_APPORDER WHERE orderid=:appOrderId and appid=:appId";
		Map<String,Object> parmap = new HashMap<String,Object>();
		parmap.put("appOrderId", appOrderId);
		parmap.put("appId", appId);
		return getNamedParameterJdbcTemplate(READ).queryForObject(sql, parmap, new AppOrderRowMapper());
	}
	
	

	@Override
	public AppOrder getAppOrder(String trxId) {
		String sql = "SELECT * FROM PAY_APPORDER WHERE trxId=?";
		Object[] params = new Object[] { trxId };
		List<AppOrder> list = getJdbcTemplate(READ).query(sql, params,
				new AppOrderRowMapper());
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	@Override
	public int updateAppOrderStatus(AppOrder aod) {
		String sql = "update PAY_APPORDER set status=?,trxId=?,changetime=? where orderid=? and uid=?";
		Object[] params = new Object[] {
				aod.getStatus(),
				aod.getTrxId(),
				aod.getChangeTime(),
				aod.getOrderId(),
                aod.getUid()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
	
	@Override
	public int updateTradeStatus(AppOrder aod) {
		String sql = "update PAY_APPORDER set tradestatus=?,changetime=? where orderid=? and appid=?";
		Object[] params = new Object[] {
				aod.getTradeStatus(),
				aod.getChangeTime(),
				aod.getOrderId(),
                aod.getAppId()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
	
	@Override
	public int updateChangeTime(AppOrder aod) {
		String sql = "update PAY_APPORDER set changetime=? where orderid=? and appid=?";
		Object[] params = new Object[] {
				aod.getChangeTime(),
				aod.getOrderId(),
                aod.getAppId()};
		int i = getJdbcTemplate().update(sql, params);
		return i;
	}
}
