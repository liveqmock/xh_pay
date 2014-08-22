package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.Version;


/**
 * 版本控制器
 */
@Controller
public class VersionController extends BaseController{
	/**
	 * 获取支付系统当前版本
	 */
	@RequestMapping(value = "/version")
	@ResponseBody
	public String getVersion(HttpServletRequest request){
		String version = "version: "+Version.CURRENT_VERSION;
		return version;
	}
}