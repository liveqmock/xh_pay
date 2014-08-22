package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xinhuanet.pay.common.BaseController;


/**
 * Handles requests for the application welcome page.
 */
@Controller
public class RemittanceController extends BaseController{

	
	/**
	 * 汇款业务
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "pay/remittance.htm")
	public ModelAndView remittance(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		this.getAccount(request,response);
		ModelAndView mav = new ModelAndView("pay/remittance/remit");
		return mav;
	}
}
