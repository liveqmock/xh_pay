package com.xinhuanet.pay.cache;

import java.sql.Date;

import com.xinhuanet.pay.cache.util.Md5Util;


public class CacheUtil {
	public static String getCacheName(Object cacheName, Object value){
		return Md5Util.MD5Encode(new StringBuilder((String)cacheName).append(value).toString());
	}
	public static String getRedisCacheName(Object cacheName, Object value){
		return new StringBuilder((String)cacheName).append(value).toString();
	}
	//已分钟为单位
	public static Date getCacheTime(int time){
		return new Date(time * 60 * 1000);
	} 
	public static Date getCacheTime(String time) throws Exception{
		return new Date(Integer.valueOf(time) * 60 * 1000);
	} 
	public static void main(String [] args){
//		Date d = getCacheTime(120*24);
//		System.out.println(d);
	}
}
