package com.xinhuanet.pay.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.OperateLogDao;
import com.xinhuanet.pay.po.OperateLog;

@Component
public class OperateLogDaoImpl extends BaseDAO implements OperateLogDao {
	
	private final class OperateLogRowMapper implements RowMapper<OperateLog>{

		@Override
		public OperateLog mapRow(ResultSet rs, int rowNum) throws SQLException {
			return null;
		}
		
	} 

	@Override
	public int addOperateLog(OperateLog operaLog) {
		String sql ="insert into PAY_LOG (uid,loginname,appid,actiondesc,requestUrl,referer,userAgent,requestParam,addtime,ipaddress)" +
				"values (?,?,?,?,?,?,?,?,now(),?)";
		List<Object> params = new ArrayList<Object>();
		params.add(operaLog.getUuid());
		params.add(operaLog.getLoginName());
		params.add(operaLog.getAppId());
		params.add(operaLog.getActionDesc());
		params.add(operaLog.getRequestUrl());
		params.add(operaLog.getReferer());
		params.add(operaLog.getUserAgent());
		params.add(operaLog.getRequestParam());
		params.add(operaLog.getIpaddress());
		return this.getJdbcTemplate().update(sql,params.toArray());
	}

}
