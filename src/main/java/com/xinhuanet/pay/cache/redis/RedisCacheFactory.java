package com.xinhuanet.pay.cache.redis;

public interface RedisCacheFactory {
	Object factory(String cacheName, Object obj) throws Exception;
}
