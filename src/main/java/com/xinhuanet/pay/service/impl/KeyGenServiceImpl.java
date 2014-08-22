package com.xinhuanet.pay.service.impl;

import com.xinhuanet.pay.dao.KeyGenDao;
import com.xinhuanet.pay.po.KeyGen;
import com.xinhuanet.pay.service.KeyGenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 下午3:58
 */
@Service
public class KeyGenServiceImpl implements KeyGenService {
    private
    @Autowired
    KeyGenDao keyGenDao;

    @Override
    public boolean addKey(KeyGen keyGen) {
        return keyGenDao.addKey(keyGen);
    }

    @Override
    public KeyGen getKey(String id) {
        return keyGenDao.getKey(id);
    }
}
