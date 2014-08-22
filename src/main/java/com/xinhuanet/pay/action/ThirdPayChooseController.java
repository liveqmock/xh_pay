package com.xinhuanet.pay.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xinhuanet.pay.common.BaseController;

/**
 * 第三方支付平台  选择器(alipay or tenpay)
 * @author xinhuanet
 */
@Controller
public class ThirdPayChooseController extends BaseController {
	
	@RequestMapping(value = "/pay/chooseplatform")
	public ModelAndView chooseplatform(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String id = request.getParameter("id");
		
		mav.addObject("id", id);
		String platform_name_value = request.getParameter("platform_name");
		if ("alipay".equals(platform_name_value)){
			RedirectView redirect = new RedirectView("/xh_pay/third/order/pay.do");
			mav.setView(redirect);
		} else if ("tenpay".equals(platform_name_value)){
			RedirectView redirect = new RedirectView("/xh_pay/third/order/tenpay.do");
			mav.setView(redirect);
		}
		return mav;
	}
	
	
}
