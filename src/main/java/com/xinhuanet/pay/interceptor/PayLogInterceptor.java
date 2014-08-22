package com.xinhuanet.pay.interceptor;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.common.BaseController;

/**
 * 记录用户访问信息拦截器
 * @author xinhuanet
 */
public class PayLogInterceptor extends BaseController implements HandlerInterceptor {
	
	/**
	 * 记录用户访问信息
	 * @param request 
	 * @param response
	 * @param handler
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		JSONObject json = new JSONObject();
		StringBuffer path = request.getRequestURL();
		Map<String, String> params = request.getParameterMap();
		Set<String> keys = params.keySet();
		JSONObject json_params = new JSONObject();
		for (String key : keys) {
			json_params.put(key, params.get(key));
		}
		String ip = request.getRemoteAddr();
		Cookie[] cookies = request.getCookies();
//		Enumeration<String> enums = request.getHeaderNames();
//		while (enums.hasMoreElements()) {
//			System.err.println(enums.nextElement());
//		}
//		logger.info(enums.toString());
		String userAgent = request.getHeader("User-Agent");
		String userId = getUserId(request, response);
		String userName = getUserName(request, response);
		
		json.put("userId", userId);
		json.put("userName", userName);
		json.put("url", path);
		json.put("ip", ip);
		json.put("params", json_params);
		json.put("cookies", cookies);
		json.put("User-Agent", userAgent);
		
		logger.info(json.toJSONString());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
