package com.xinhuanet.pay.gateway;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.xinhuanet.pay.util.Arith;
import com.xinhuanet.pay.util.Function;
/**
 * 通联支付专用
 * @author duanwc
 *
 */
public class Allinpay {
	protected static final Logger logger = Logger.getLogger(Allinpay.class);

	/**
	 * 将 时间转换为 Allinpay所支持的时间
	 * @param date 时间对象
	 * @return Allinpay所支持的时间
	 */
	public static String formatAllinpayTime(Date date){
		String dateTime = Function.getDateString(date, "yyyyMMddhhmmss");
		return dateTime;
	}
	
	/**
	 * 批量查询时间范围为当天的00:00:00~23:59:59 <br/>
	 * 获取批量查询的开始时间<br/>
	 * 因 Allinpay所支持目前只支持当前天的结果查询，且格式为yyyyMMddhh，所以获取服务器当前时间yyyyMMdd + "00"
	 * @return 批量查询时Allinpay所需要的开始时间，结果为当前天的零点
	 */
	public static String getBeginDateTime(){
		String beginDateTime = Function.getDateString(new Date(), "yyyyMMdd") + "00";
		return beginDateTime;
	}
	
	/**
	 * 批量查询时间范围为当天的00:00:00~23:59:59 <br/>
	 * 获取批量查询的结束时间<br/>
	 * 因 Allinpay所支持目前只支持当前天的结果查询，且格式为yyyyMMddhh，所以获取服务器当前时间yyyyMMdd + "23"
	 * @return 批量查询时Allinpay所需要的结束时间，新华支付平台默认为全天，结果为当前天的二十三点
	 */
	public static String getEndDateTime(){
		String endDateTime = Function.getDateString(new Date(), "yyyyMMdd") + "23";
		return endDateTime;
	}
	
	/**
	 * 通联支付返回值解析到map内
	 * @param strResponse
	 * @return map 通联支付解析完成后的数据
	 */
	public static HashMap<String, String> registerMap(String strResponse){
		HashMap<String, String> result = new HashMap<String, String>();
		// 解析查询返回结果
		try {
			strResponse = URLDecoder.decode(strResponse, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("URLDecoder.decode通联支付返回值错误",e);
			return result;
		}
		logger.debug("通联返回值：" + strResponse);
		String[] parameters = strResponse.split("&");
		for (int i = 0; i < parameters.length; i++) {
			String msg = parameters[i];
			int index = msg.indexOf('=');
			if (index > 0) {
				String name = msg.substring(0, index);
				String value = msg.substring(index + 1);
				result.put(name, value);
			}
		}
		return result;
	}
}
