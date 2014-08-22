package com.xinhuanet.pay.cache.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.cache.CacheName;
import com.xinhuanet.pay.cache.redis.RedisCacheMan;

@Component
public class AuthCodeCache {
	/**
	 * 过期时间
	 */
	private static final int expire = 3 * 60;

	private @Autowired RedisCacheMan cache;

	/**
	 * 设置支付密码验证码缓存
	 * 
	 * @param loginName
	 * @param value
	 * @throws Exception
	 */
	public void setPayPswCode(String loginName, String value) throws Exception {
		cache.setStringTimer(CacheName.PAY_PSW_CODE, loginName, value, expire);
	}

	/**
	 * 获取得设置支付密码验证码
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public String getPayPswCode(String loginName) throws Exception {
		return cache.getString(CacheName.PAY_PSW_CODE, loginName);
	}

	/**
	 * 设置邮箱认证验证码缓存
	 * 
	 * @param loginName
	 * @param value
	 * @throws Exception
	 */
	public void setPayEmailCode(String loginName, String value)
			throws Exception {
		cache.setStringTimer(CacheName.PAY_EMAIL_CODE, loginName, value, expire);
	}

	/**
	 * 获取得邮箱认证验证码
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public String getPayEmailCode(String loginName) throws Exception {
		return cache.getString(CacheName.PAY_EMAIL_CODE, loginName);
	}
}
