package com.xinhuanet.pay.dao.impl;

import com.xinhuanet.pay.common.BaseDAO;
import com.xinhuanet.pay.dao.KeyGenDao;
import com.xinhuanet.pay.po.KeyGen;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 下午3:43
 */
@Component
public class KeyGenDaoImpl extends BaseDAO implements KeyGenDao {

    private static final class KeyGenRowMapper implements RowMapper<KeyGen>{

        @Override
        public KeyGen mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            KeyGen key = new KeyGen();
            key.setId(resultSet.getString("id"));
            key.setPublicKey(resultSet.getString("public"));
            key.setPrivateKey(resultSet.getString("private"));
            key.setAddTime(resultSet.getTimestamp("addtime"));
            key.setUpdateTime(resultSet.getTimestamp("updateTime"));
            key.setStatus(resultSet.getInt("status"));
            return key;
        }
    }

    @Override
    public boolean addKey(KeyGen key) {
        String sql = "insert into  PAY_KEY(id,public,private,addtime,updateTime) values(?,?,?,NOW(),NOW())";
        Object[] params = {
                key.getId(),key.getPublicKey(),key.getPrivateKey()
        };
        int num = getJdbcTemplate().update(sql, params);
        return num > 0;
    }

    @Override
    public KeyGen getKey(String id) {
        String sql = "select * from PAY_KEY where id = ?";
        Object[] params = {
                id
        };

        List<KeyGen> keyList = getJdbcTemplate(READ).query(sql, params, new KeyGenRowMapper());
        return (keyList == null || keyList.size() == 0) ? null : keyList.get(0);
    }
}
