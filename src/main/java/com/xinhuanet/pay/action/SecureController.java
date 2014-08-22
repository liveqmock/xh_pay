package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.util.Md5Util;

/**
 * @author duanwc
 * 安全中心控制器
 */
@Controller
public class SecureController extends BaseController{
	
	
	/**
	 * 安全中心首页
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/secure")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		Account account = this.getAccount(request,response);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("secure/index");
		return mav;
	}
	
	/**
	 * 验证用户支付密码是否正确
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/secure/verify")
	@ResponseBody
	public String verify(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		Account account = this.getAccount(request,response);
		JSONObject json = new JSONObject();
		
		String payPassword = request.getParameter("payPassword");
		
		if(account.getPayCode().equals(Md5Util.MD5Encode(payPassword))){
			json.put("code", 0);
			json.put("message", "支付密码正确");
		} else{
			json.put("code", 1);
			json.put("message", "支付密码错误");
		}
		return json.toJSONString();
	}
}
