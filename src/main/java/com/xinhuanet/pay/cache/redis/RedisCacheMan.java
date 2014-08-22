package com.xinhuanet.pay.cache.redis;

import java.util.List;
import java.util.Set;

public interface RedisCacheMan {
	//---------------------操作实体对象接口 Start------------------------
	/**
	 * 从缓存中根据缓存功能模块名称，缓存键值获取对象，如果对象不存在，则使用cacheName和cacheKey调用CacheFactory.factory方法
	 * 来获取数据，再置入缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @return 执行成功会返回对象，失败返回空值
	 */
	public Object getObject(String cacheName, Object cacheKey);
	/**
	 * 设置缓存对象，无超时时间，也就是永久缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param obj 放入缓存的对象
	 * @return 执行成功返回原对象，失败返回null
	 */
	public Object setObject(String cacheName, Object cacheKey, Object obj);
	/**
	 * 从缓存中获取对象，如果对象不存在，则调用setObjectTimer设置缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回对象，失败返回null
	 */
	public Object getObject(String cacheName, Object cacheKey, int time);
	/**
	 * 带有过期时间的设置缓存函数
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param obj 放入缓存的对象
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回原对象，失败返回null
	 */
	public Object setObject(String cacheName, Object cacheKey, Object obj,
			int time);
	//---------------------操作实体对象接口 End------------------------
	//---------------------操作字符串接口Start------------------------
	/**
	 * 从缓存中根据缓存功能模块名称，缓存键值获取对象，如果字符串对象不存在，则使用cacheName和cacheKey调用CacheFactory.factory方法
	 * 来获取数据，再置入缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @return 执行成功会返回对象，失败返回空值
	 */
	public String getString(String cacheName, Object cacheKey);
	/**
	 * 设置缓存字符串对象，无超时时间，也就是永久缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param str 放入缓存的字符串
	 * @return 执行成功返回原对象，失败返回null
	 */
	public String setString(String cacheName, Object cacheKey, String str);
	/**
	 * 从缓存中获取字符串对象，如果字符串对象不存在，则调用setString设置缓存
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回对象，失败返回null
	 */
	public String getStringTimer(String cacheName, Object cacheKey, int time);
	/**
	 * 带有过期时间的设置字符串缓存函数
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param str 放入缓存的字符串
	 * @param time 过期时间，以<strong>秒</strong>为单位
	 * @return 执行成功返回原对象，失败返回null
	 */
	public String setStringTimer(String cacheName, Object cacheKey, String str,int time);
	//---------------------操作字符串接口End------------------------
	//---------------------操作链表接口Start------------------------
	/**
	 * 将列表id索引存入redis的zset中
	 * @param cacheName
	 * @param cacheKey
	 * @param members
	 * @return
	 */
	public void setZSet(String cacheName, Object cacheKey,String...members);
    /**
     * 将String对象存入redis的zset中
     * @param cacheName
     * @param cacheKey
     * @param member
     * @return
     */
    public Object setZSet(String cacheName, Object cacheKey,Double score, String member);
	/**
	 * 将Object实体对象存入redis的zset中
	 * @param cacheName
	 * @param cacheKey
	 * @param member
	 * @return
	 */
	public Object setZSet(String cacheName, Object cacheKey,Double score, Object member);
	/**
	 * 保留一定长度的zset
	 * @param cacheName
	 * @param cacheKey
	 * @param length
	 */
	public void zSetTrim(String cacheName, Object cacheKey,int length);
	/**
	 * 获取有序集合的并集
	 * @param key
	 * @param unikey
	 */
	public void zunionstore(String cacheName, Object cacheKey,String diffCacheName, Object diffCacheKey);
	/**
	 * 获取有序集合的差集
	 * @param key
	 * @param diffkey
	 */
	public void zdiffstore(String cacheName, Object cacheKey,String diffCacheName, Object diffCacheKey);
	/**
	 * 获取有序集合的实体对象列表，start=0，end=-1时，表示全部数据
	 * @param <T>
	 * @param key
	 * @param start
	 * @param end
	 * @param clzss
	 * @return
	 */
	public <T> List<T> getZSet(String cacheName, Object cacheKey, int start, int end,Class<T> clzss);
	/**
	 * 获取指定长度的有序列表，start=0，end=-1时，表示全部数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> getZSet(String cacheName, Object cacheKey, int start, int end);
	
	/**
	 * 获取指定长度的正序列表，start=0，end=-1时，表示全部数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> getZSetL(String cacheName, Object cacheKey, int start, int end);
	
	/**
	 * 获取指定有序列表的长度
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long countZSet(String cacheName, Object cacheKey);
	
	/**
	 * 检测redis对象是否存在
	 * @param key
	 * @return 如果存在返回true,否则返回false
	 * @throws Exception
	 */
	public boolean isExists(String cacheName, Object cacheKey);
	
	/**
	 * 从ZSet中删除指定成员
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param member String值
	 */
	public void removeZSet(Object cacheName, Object cacheKey,String member);
	/**
	 * 从ZSet中删除指定成员对象
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 * @param member Object对象
	 */
	public void removeZSetObject(Object cacheName, Object cacheKey,Object member);
	/**
	 * 删除指定缓存对象
	 * @param cacheName 缓存对应的功能模块名称
	 * @param cacheKey 缓存对象的键值
	 */
	public void remove(Object cacheName, Object cacheKey);
	/**
	 * <strong>清除所有缓存对象，慎用</strong>
	 */
	public void removeAll();
}
