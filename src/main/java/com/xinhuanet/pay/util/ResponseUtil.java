package com.xinhuanet.pay.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 回写ajax内容到响应结果
 * @author duanwc
 */
public abstract class ResponseUtil {
	/**
	 * 回写ajax内容响应到页面
	 * @param response
	 * @param str 需要回写的内容
	 * @param resType 返回值类型
	 */
	public static void responseBody(HttpServletResponse response,String str,String resType){
		response.setContentType(resType + ";charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
    		out.print(str);
    		out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 回写ajax内容响应到页面，resType类型默认为text/html
	 * @param response
	 * @param str 需要回写的内容
	 */
	public static void responseBody(HttpServletResponse response,String str){
		responseBody(response, str, "text/html");
	}
}