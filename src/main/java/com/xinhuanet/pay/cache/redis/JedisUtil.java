package com.xinhuanet.pay.cache.redis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ZParams;

public class JedisUtil {
	
	private  JedisPool pool;
	
	/**
	 * @param pool the pool to set
	 */
	public void setPool(JedisPool pool) {
		this.pool = pool;
	}
	
	/**
	 * 向String添加数据
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void addString(String key, String value) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.set(key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从String中取数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getString(String key) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.get(key);
		} catch (Exception e) {
			throw new Exception();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向zSet添加数据
	 * @param key
	 * @param score
	 * @param value
	 * @throws Exception
	 */
	public void addZSet(String key, double score, String value) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.zadd(key, score, value);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	/**
	 * 从zSet中取数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Set<String> getZSet(String key, int start, int end) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从zSet中取数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public Set<String> getZSetL(String key, int start, int end) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向zSet添加中添加一组数据
	 * @param key
	 * @param members
	 * @throws Exception
	 */
	public void addZSet(String key, String[] members) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			for(String s:members){
				jedis.zadd(key, Double.parseDouble(s), s);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向zSet添加数据
	 * @param key
	 * @param score
	 * @param object
	 * @throws Exception
	 */
	public void addZSet(String key, double score, Object object) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.zadd(key.getBytes(), score, SerializeUtil.serialize(object));
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	/**
	 * 从zset中返回一个有序的List对象
	 * @param <T>
	 * @param key
	 * @param start 开始
	 * @param end 结束
	 * @param clzss 对象的Class
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getZSet(String key, int start, int end,Class<T> clzss) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<byte[]> set = jedis.zrevrange(key.getBytes(), start, end);
			Object o = null;
			List<T> list = new ArrayList<T>();
			for(byte[] b:set){
				o = SerializeUtil.unserialize(b);
				T row = clzss.cast(o);
				list.add(row);
			}
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向Set添加数据
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void addSet(String key, String value) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.sadd(key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从Set中取数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Set<String> getSet(String key) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.smembers(key);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从Set中删除成员
	 * @param key
	 * @param member
	 * @throws Exception
	 */
	public void delSet(String key, String member) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.srem(key, member);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从ZSet中删除成员
	 * @param key
	 * @param member
	 * @throws Exception
	 */
	public void delZSet(String key, String member) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.zrem(key, member);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从ZSet中删除对象成员
	 * @param key
	 * @param member
	 * @throws Exception
	 */
	public void delZSet(String key, Object member) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.zrem(key.getBytes(), SerializeUtil.serialize(member));
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从Zset中取成员数量
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Long countZSet(String key) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.zcard(key);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 从Redis中删除对象
	 * @param key
	 * @throws Exception
	 */
	public void delRedis(String key)throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 按日期分组ZSet集合
	 * @param key
	 * @param dateList
	 * @return
	 * @throws Exception
	 */
	public List<Set<String>> getZSetByScore(String key, List<Date> dateList) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			List<Set<String>> zSetList = new ArrayList<Set<String>>();
			SimpleDateFormat fromYMD = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fromHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Date date : dateList){
				String dateStr = fromYMD.format(date);
				Date dateMin = fromHMS.parse(dateStr+" 00:00:00");
				Date dateMax = fromHMS.parse(dateStr+" 23:59:59");
				double min = (double)dateMin.getTime();
				double max = (double)dateMax.getTime();
				Set<String> zset = jedis.zrevrangeByScore(key, max, min);
				//正序Set<String> zset = jedis.zrangeByScore(key, min, max);
				zSetList.add(zset);
			}
			return zSetList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 取指定日期ZSet集合
	 * @param key
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public Set<String> getZSetByScore(String key, String dateStr) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(dateStr+"-01"));
			cal.add(Calendar.MONTH,1);//月增加1天 
			cal.add(Calendar.DAY_OF_MONTH,-1);//日期倒数一日,既得到本月最后一天
			SimpleDateFormat fromHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateMin = fromHMS.parse(dateStr+"-01"+" 00:00:00");
			Date dateMax = fromHMS.parse(dateStr+"-"+cal.get(Calendar.DAY_OF_MONTH)+" 23:59:59");
			double min = (double)dateMin.getTime();
			double max = (double)dateMax.getTime();
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 取ZSet中成员的Score
	 * @param key
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Double getZSetElementScore(String key, String member) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.zscore(key, member);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向list头部添加数据
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void addListL(String key, String value) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.lpush(key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 向list尾部添加数据
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public void addListR(String key, String value) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.rpush(key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	/**
	 * 保留一定长度的list
	 * @param key
	 * @param start
	 * @param end
	 * @throws Exception
	 */
	public void listTrim(String key,int start,int end) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.ltrim(key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	/**
	 * 从list取数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<String> getList(String key, int start, int end) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 设置对象生存时间
	 * @param key
	 * @param seconds
	 * @throws Exception
	 */
	public void setExpire(String key, int seconds) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			jedis.expire(key, seconds);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 对象是否存在
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean isExists(String key) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			
			return jedis.exists(key);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	/**
	 * 移除当前DB内所有数据
	 * @throws Exception
	 */
	public void removeDBAll() throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.flushDB();
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 将一个Object对象存入Redis
	 * @param key
	 * @param object
	 * @throws Exception
	 */
	public void setObject(String key, Object object) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.set(key.getBytes(), SerializeUtil.serialize(object));
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 获取一个Object实体对象
	 * @param key
	 * @return Object实体对象
	 * @throws Exception
	 */
	public Object getObject(String key) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[] object = jedis.get(key.getBytes());
			if(object==null){
				return null;
			}
			return SerializeUtil.unserialize(object);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 保留一定长度的zset
	 * @param key
	 * @throws Exception
	 */
	public void zSetTrim(String key,int length)throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long count = countZSet(key);
			if(count>length){
				jedis.zremrangeByRank(key, 0, (int)(count - (length + 1)));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 求zSet并集
	 * @param key
	 * @param unikey
	 * @throws Exception
	 */
	public void zunionstore(String key,String unikey) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			ZParams params = new ZParams();
	        params.weights(1, 1);
	        params.aggregate(ZParams.Aggregate.MAX);
//	        //并集
	        jedis.zunionstore(key, params, key,unikey);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
	
	/**
	 * 求zSet差集
	 * @param key
	 * @param diffkey
	 * @throws Exception
	 */
	public void zdiffstore(String key,String diffkey) throws Exception{
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> set = getZSet(key, 0, -1);
			Set<String> diffset = getZSet(diffkey,0,-1);
			set.removeAll(diffset);
			delRedis(key);
			String[] members = set.toArray(new String[]{});
			addZSet(key, members);
		} catch (Exception e) {
			throw e;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}
}
