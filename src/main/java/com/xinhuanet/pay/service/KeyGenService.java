package com.xinhuanet.pay.service;

import com.xinhuanet.pay.po.KeyGen;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 下午3:58
 */
public interface KeyGenService {

    public boolean addKey(KeyGen keyGen);

    public KeyGen getKey(String id);
}
