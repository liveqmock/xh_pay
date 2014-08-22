package com.xinhuanet.pay.cache.redis;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xinhuanet.pay.cache.CacheUtil;


/**
 * 缓存公共类
 * 分两组操作，一组是带超时时间的，另外一组不带超时时间
 * 所有get操作会先从缓存中取数据，如果缓存中存在对应数据，就会立即返回；如果缓存中不存在，则会使用cacheName和cacheKey调用CacheFactory.factory方法
 * 并将factory方法返回的数据放入缓存中
 * 
 * @author Ethan
 *
 */
@Repository
public class RedisCacheManImpl implements RedisCacheMan {
	private static Logger logger = Logger.getLogger(RedisCacheManImpl.class);
	/**
	 * 缓存对象工厂
	 */
	private @Autowired RedisCacheFactory cacheFactory;
	/**
	 * 直接可操作的缓存对象
	 */
	private @Autowired JedisUtil jedisCache;
	
	/**
	 * 从缓存中根据缓存功能模块名称，缓存键值获取对象，如果对象不存在，则使用cacheName和cacheKey调用CacheFactory.factory方法
	 * 来获取数据，再置入缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @return 执行成功会返回对象，失败返回空值
	 */
	@Override
	public Object getObject(String cacheName, Object cacheKey) {
		
		try {
			Object obj = null;
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			obj = jedisCache.getObject(key);
			if (obj == null) {
				obj = cacheFactory.factory(cacheName, cacheKey);
				if (obj != null){
					jedisCache.setObject(key, obj);
				}
			}
			return obj;
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			return null;
		}
	}

	/**
	 * 设置缓存对象，无超时时间，也就是永久缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param obj 放入缓存的对象
	 * @return 执行成功返回原对象，失败返回null
	 */
	@Override
	public Object setObject(String cacheName, Object cacheKey, Object obj) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.setObject(key, obj);
			return obj;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 从缓存中获取对象，如果对象不存在，则调用setObjectTimer设置缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回对象，失败返回null
	 */
	@Override
	public Object getObject(String cacheName, Object cacheKey, int time) {
		try {
			Object obj = null;
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			obj = jedisCache.getObject(key);
			if (obj == null) {
				if(logger.isDebugEnabled()){
					logger.debug("cache hit failure");
				}
				obj = cacheFactory.factory(cacheName, cacheKey);
				if (obj != null)
					setObject(cacheName, cacheKey, obj, time);
			}
			return obj;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			return null;
		}
	}
	/**
	 * 带有过期时间的设置缓存函数
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param obj 放入缓存的对象
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回原对象，失败返回null
	 */
	@Override
	public Object setObject(String cacheName, Object cacheKey,
			Object obj, int time) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.setObject(key, obj);
			jedisCache.setExpire(key, time);
			return obj;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
	}
	
	
	

	@Override
	public String getStringTimer(String cacheName, Object cacheKey, int time) {
		String str = null;
		String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
		try {
			str = jedisCache.getString(key);
			if (str == null) {
				str = (String) cacheFactory.factory(cacheName, cacheKey);
				if (str != null) {
					setStringTimer(cacheName, cacheKey, str, time);
					jedisCache.addString(key, str);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
		return str;
	}

	@Override
	public String getString(String cacheName, Object cacheKey) {
		String str = null;
		String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
		try {
			str = jedisCache.getString(key);
			if (str == null) {
				str = (String) cacheFactory.factory(cacheName, cacheKey);
				if (str != null) {
					jedisCache.addString(key, str);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
		return str;
	}

	@Override
	public String setStringTimer(String cacheName, Object cacheKey, String str, int time) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.addString(key, str);
			jedisCache.setExpire(key, time);
			return str;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
	}

	@Override
	public String setString(String cacheName, Object cacheKey, String str) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.addString(key, str);
			return str;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return null;
		}
	}

	/**
	 * 删除指定缓存对象
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 */
	@Override
	public void remove(Object cacheName, Object cacheKey) {
		try {
			jedisCache.delRedis(CacheUtil.getRedisCacheName(cacheName, cacheKey));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * <strong>清除当前缓存库下所有缓存对象，慎用</strong>
	 */
	@Override
	public void removeAll() {
		try {
			jedisCache.removeDBAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void removeZSetObject(Object cacheName, Object cacheKey, Object member) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.delZSet(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public void removeZSet(Object cacheName, Object cacheKey, String member) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.delZSet(key, member);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}

	@Override
	public Long countZSet(String cacheName, Object cacheKey) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			return jedisCache.countZSet(key);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@Override
	public <T> List<T> getZSet(String cacheName, Object cacheKey, int start,
			int end, Class<T> clzss) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			return jedisCache.getZSet(key, start, end, clzss);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Set<String> getZSet(String cacheName, Object cacheKey, int start,
			int end) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			return jedisCache.getZSet(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

    @Override
	public Set<String> getZSetL(String cacheName, Object cacheKey, int start,
			int end) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			return jedisCache.getZSetL(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
    public Object setZSet(String cacheName, Object cacheKey,Double score, String member) {
        try {
            String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
            jedisCache.addZSet(key, score, member);
            return member;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

	@Override
	public Object setZSet(String cacheName, Object cacheKey,Double score, Object member) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.addZSet(key, score, member);
			return member;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void setZSet(String cacheName, Object cacheKey,
			String... members) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.addZSet(key, members);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void zdiffstore(String cacheName, Object cacheKey,
			String diffCacheName, Object diffCacheKey) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			String diffkey = CacheUtil.getRedisCacheName(diffCacheName, diffCacheKey);
			jedisCache.zdiffstore(key, diffkey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	@Override
	public void zSetTrim(String cacheName, Object cacheKey, int length) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			jedisCache.zSetTrim(key, length);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void zunionstore(String cacheName, Object cacheKey,
			String diffCacheName, Object diffCacheKey) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			String diffkey = CacheUtil.getRedisCacheName(diffCacheName, diffCacheKey);
			jedisCache.zunionstore(key, diffkey);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	@Override
	public boolean isExists(String cacheName, Object cacheKey) {
		try {
			String key = CacheUtil.getRedisCacheName(cacheName, cacheKey);
			return jedisCache.isExists(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	public static void main(String[] args){
//		CacheManImpl cmi = new CacheManImpl();
//		cmi.remove(CacheName.lstDictCacheName, "tb_wish_para");
//		cmi.remove(CacheName.mapDictCacheName, "tb_wish_para");
//		DictManImpl dmi = new DictManImpl();
//		System.out.println(dmi.getDictType("tb_wish_para", 3).getDictValue());
//		System.out.println(CacheUtil.getRedisCacheName(CacheName.chinaViewImageNews, "http://news.xinhuanet.com/english/photo/2012-11/21/c_131988737.htm"));
		
		
	}
}
