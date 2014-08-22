package com.xinhuanet.pay.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.SysConfigDao;
import com.xinhuanet.pay.mapper.SysConfigRowMapper;
import com.xinhuanet.pay.po.SysConfig;


@Component
public class SysConfigDaoImpl extends BaseDAO implements SysConfigDao{
	@Override
	public SysConfig getSysConfig(String key) {
	    String sql = "select * from PAY_SYS_CONFIG where cfg_key=?";
	    Object[] params = {key};
	    List<SysConfig> scList = this.getJdbcTemplate(READ).query(sql,params,new SysConfigRowMapper());
	    return scList!=null&&scList.size()>0?scList.get(0):null;
	}
}

