package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;

/**
 * 登录页面
 */
@Controller
public class LogoutController extends BaseController {
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public void logout(HttpServletRequest request,
			HttpServletResponse response) throws LoginUserNotFoundException {
		String userName = this.getLoginName(request, response);
		if(!StringUtils.isBlank(userName)){
			logger.info("用户: "+userName+" 退出系统");
		}		
        String callback = request.getContextPath()+"/doLogout?returnurl=/";
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", callback);
	}
}
