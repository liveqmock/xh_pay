package com.xinhuanet.pay.mapper;

import com.xinhuanet.pay.po.SysConfig;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class SysConfigRowMapper implements RowMapper<SysConfig> {

    @Override
    public SysConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
        SysConfig sc = new SysConfig();
        sc.setCfgKey(rs.getString("cfg_key"));
        sc.setCfgValue(rs.getString("cfg_value"));
        sc.setCfgDesc(rs.getString("cfg_desc"));
        sc.setUpdateTime(rs.getTimestamp("updateTime"));
        sc.setStatus(rs.getInt("status"));
        sc.setIp(rs.getString("ip"));
        sc.setUserName(rs.getString("userName"));
        return sc;
    }
}
