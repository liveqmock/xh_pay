package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.KeyGen;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 下午3:37
 */
public interface KeyGenDao {

    public boolean addKey(KeyGen key);

    public KeyGen getKey(String id);

}
