package com.xinhuanet.pay.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinhuanet.pay.cache.CacheName;
import com.xinhuanet.pay.cache.redis.RedisCacheMan;
import com.xinhuanet.pay.po.SysConfig;
import com.xinhuanet.pay.service.SysConfigService;

/**
 * Created with IntelliJ IDEA.
 * User: Ronny
 * Date: 13-11-20
 * Time: 上午9:59
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SysConfigServiceImpl implements SysConfigService{

    private @Autowired RedisCacheMan cache;

    @Override
    public SysConfig getSysConfig(String key) {
    	SysConfig sysConfig = (SysConfig)cache.getObject(CacheName.SYSCONFIG, key);
        return sysConfig;
    }
}
