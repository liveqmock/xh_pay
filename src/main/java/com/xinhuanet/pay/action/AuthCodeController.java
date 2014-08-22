package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xinhuanet.pay.common.BaseController;


/**
 * 服务器端对输入的验证码进行验证.
 */
@Controller
public class AuthCodeController extends BaseController{
	/**
	 * 服务器端验证输入验证码是否正确
	 * @return json数据类型，authCode正确返回true，错误返回false
	 */
	@RequestMapping(value = "/authcode.do", method = RequestMethod.POST)
	@ResponseBody
	public boolean authCode(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		String c = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String parm = (String) request.getParameter("authCode");
		
		boolean flag = false;
		if (c != null && parm != null) {
			if (c.equals(parm)) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}
}