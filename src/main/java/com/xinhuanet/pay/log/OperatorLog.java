package com.xinhuanet.pay.log;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.po.OperateLog;
import com.xinhuanet.pay.service.OperateLogService;
import com.xinhuanet.pay.util.HttpUtil;

@Component
public class OperatorLog {
	
	@Autowired
	private OperateLogService operaLogService;
	
	/**
	 * 请求操作日志
	 * @param request
	 * @param operName 操作项名称
	 */
	public void requestOperLog(HttpServletRequest request, String operName){
		String referer = request.getHeader("referer");
		String userAgent = request.getHeader("User-Agent");
		String appId = request.getParameter("appId");
		String loginName = request.getParameter("loginName");
		Enumeration<?> e = request.getParameterNames();
		StringBuilder sb = new StringBuilder();
		while(e.hasMoreElements()){
			String pname = (String) e.nextElement();
			sb.append(pname).append("=").append(request.getParameter(pname)).append("&");
		}
		String ipaddress = HttpUtil.getIpAddr(request);
		OperateLog operaLog = new OperateLog();
		operaLog.setActionDesc(operName);
		operaLog.setAppId(StringUtils.isNotBlank(appId)?Integer.parseInt(appId):0);
		operaLog.setIpaddress(ipaddress);
		operaLog.setUserAgent(userAgent);
		operaLog.setReferer(referer);
		operaLog.setLoginName(loginName);
		operaLog.setRequestParam(sb.toString());
		operaLog.setRequestUrl(request.getRequestURL().toString());
		operaLogService.addOperateLog(operaLog);
	}
	
}
