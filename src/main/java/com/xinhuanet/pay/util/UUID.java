package com.xinhuanet.pay.util;


public class UUID {
	/**
	 * 获取UUID，去掉"-"字符，长度32
	 * @return s 处理完成的uuid
	 */
	public static String create(){
		String s= java.util.UUID.randomUUID().toString().replace("-", "");
		return s;
	}
	/**
	 * 获取UUID，去掉"-"字符并增加前缀名
	 * @param s 前缀名
	 * @return 处理完成的uuid
	 */
	public static String create(String s){
		return s+create();
	}
}
