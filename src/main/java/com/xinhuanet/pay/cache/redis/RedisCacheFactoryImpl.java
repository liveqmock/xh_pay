/*
 * Created on 2005-4-21
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.xinhuanet.pay.cache.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinhuanet.pay.cache.CacheName;
import com.xinhuanet.pay.dao.PayAppOrderDao;
import com.xinhuanet.pay.dao.PayOrderDao;
import com.xinhuanet.pay.dao.SysConfigDao;



@Repository
public class RedisCacheFactoryImpl implements RedisCacheFactory {
	private static Logger logger =  Logger.getLogger(RedisCacheFactoryImpl.class);
	
	/**
	 * 应用订单DAO
	 */
    private @Autowired PayAppOrderDao appOrderDao;
	/**
	 * 网银订单DAO
	 */
    private @Autowired PayOrderDao orderDao;
    /**
     * 系统动态属性配置DAO
     */
    private @Autowired SysConfigDao sysConfigDao;

	@Override
	public Object factory(String cacheName, Object key) throws Exception {
		Object obj = null;
		if (cacheName != null) {
			if(cacheName.equals(CacheName.APPORDER)){//获取应用订单
				logger.debug("缓存名:"+cacheName + key + "通过appOrderDao.getAppOrderById获取数据库信息");
				obj = appOrderDao.getAppOrderById(key.toString());
			} else if(cacheName.equals(CacheName.ORDER)){//获取系统配置的属性
				logger.debug("缓存名:"+cacheName + key + "通过orderDao.getOrderById获取数据库信息");
				obj = orderDao.getOrderById(key.toString());
			} else if(cacheName.equals(CacheName.SYSCONFIG)){//获取系统配置的属性
				logger.debug("缓存名:"+cacheName + key + "通过sysConfigDao.getSysConfig获取数据库信息");
				obj = sysConfigDao.getSysConfig(key.toString());
			}
		}
		return obj;
	}
}
