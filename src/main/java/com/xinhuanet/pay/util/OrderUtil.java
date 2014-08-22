package com.xinhuanet.pay.util;

public class OrderUtil {
	/**
	 * 创建一个订单号，
	 * 生成原则为：系统时间（精确到毫秒）+两位随机数（范围：10-99）
	 * @return OrdId 订单号
	 */
	public static String createOrderNumber(){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
		String OrdId = formatter.format(currentTime); //将日期时间格式化 
		//获取10-99之间的随机数，为了保证订单号长度一致
		int random = (int)(Math.random()*89+10);
		return OrdId + random;
	}
}
