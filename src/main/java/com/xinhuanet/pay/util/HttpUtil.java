package com.xinhuanet.pay.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
	/**
	 * 获取用户IP地址
	 * @param request 请求
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
    /**
     * 用apache代理后，获得真正的客户端地址
     * @param request
     * @return
     */
    public static String getRealClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    
	public static boolean isWAP(HttpServletRequest request) {
		String agent = request.getHeader("user-agent");
		// String agent =
		// "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/534.51.22 (KHTML, like Gecko) Version/5.1.1 Safari/534.51.22";
		String agentcheck = agent.trim().toLowerCase();
		boolean isWAP = false;
		String[] keywords = { "mobile", "android", "symbianos", "iphone",
				"wp\\d*", "windows phone", "mqqbrowser", "nokia", "samsung",
				"midp-2", "untrusted/1.0", "windows ce", "blackberry", "ucweb",
				"brew", "j2me", "yulong", "coolpad", "tianyu", "ty-",
				"k-touch", "haier", "dopod", "lenovo", "huaqin", "aigo-",
				"ctc/1.0", "ctc/2.0", "cmcc", "daxian", "mot-", "sonyericsson",
				"gionee", "htc", "zte", "huawei", "webos", "gobrowser",
				"iemobile", "wap2.0", "WAPI" };
		Pattern pf = Pattern.compile("wp\\d*");
		Matcher mf = pf.matcher(agentcheck);
		if (agentcheck != null
				&& (agentcheck.indexOf("windows nt") == -1 && agentcheck
						.indexOf("Ubuntu") == -1)
				|| (agentcheck.indexOf("windows nt") > -1 && mf.find())) {
			for (int i = 0; i < keywords.length; i++) {
				Pattern p = Pattern.compile(keywords[i]);
				Matcher m = p.matcher(agentcheck);
				// 排除 苹果桌面系统 和ipad 、iPod
				if (m.find() && agentcheck.indexOf("ipad") == -1
						&& agentcheck.indexOf("ipod") == -1
						&& agentcheck.indexOf("macintosh") == -1) {
					isWAP = true;
					break;
				}
			}
		}

		return isWAP;
	}
}
