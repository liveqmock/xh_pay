package com.xinhuanet.pay.cache.util;

import java.util.HashMap;


/**
 * 用来存储参数的HashMap，主要重写toString方法，将遍历所有参数，取各个对象的hashCode，然后累加，生成一个唯一的HASH码
 * 这个HASH码将作为缓存时的KEY值
 * @author WangQi
 *
 */
public class ParaHashMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		long hashCode=0L;
		for(String obj : this.keySet()) {
			if(this.get(obj) instanceof ParaHashMap){
				hashCode += Long.parseLong(this.get(obj).toString());
			} else {
				hashCode += this.get(obj).hashCode();
			}
		}
		
		return Long.toString(hashCode);
	}
	
	public String toFullString(){
		StringBuilder builder = new StringBuilder();
		builder.append("ParaHashMap [ ");
		for(String obj : this.keySet()) {
			builder.append(", ");
			builder.append(obj);
			builder.append(" : ");
			builder.append(this.get(obj));
		}
		builder.deleteCharAt(0);
		builder.append(" ]");
		return builder.toString();
	}
	
}
