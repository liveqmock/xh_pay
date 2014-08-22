package com.xinhuanet.pay.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.service.BalanceService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.util.Md5Util;

/**
 * 账户余额支付控制器
 * @author duanwc
 */
@Controller
public class BalanceController extends BaseController {
	/**
	 * app订单服务
	 */
	private @Autowired
	PayAppOrderService appOrderService;

	/**
	 * 余额支付服务
	 */
	private @Autowired BalanceService service;


	/**
	 * 打开余额支付页
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/balance/pay")
	public ModelAndView accountInfo(HttpServletRequest request,
			HttpServletResponse response) {
		// 获取账户信息
		Account account = this.getAccount(request, response);
		ModelAndView mav = new ModelAndView();
		String id = request.getParameter("id");
		AppOrder order = appOrderService.getAppOrderById(id);
		if (order == null) {
			mav.setViewName("pay/chinapnr/failed");
			return mav;
		}
		
		if(order.getOrderType() == 0){
			mav.setView(new RedirectView(request.getContextPath()
					+ "//ebank/choose?id="+id));
			return mav;
		}
		
		Boolean lessThan = false; // 余额是否小于订单金额，默认不小于
		if (account.getMoney() < order.getMoney()) {// 余额小于订单金额
			lessThan = true;
//			mav.addObject("message", "您的余额不足");
			logger.info(account.getLoginName()+","+account.getUid()+": 余额不足");
		}
		Boolean isUsingPwd = false; // 是否开启支付密码
		if (account.getPayCodeStatus() == 0) {// 启用支付密码
			isUsingPwd = true;
			logger.info(account.getLoginName()+","+account.getUid()+": 尚未启用支付密码");
//			mav.addObject("message", "您尚未启用支付密码");
		}
		mav.addObject("isUsingPwd", isUsingPwd);
		mav.addObject("lessThan", lessThan);
		mav.addObject("order", order);
		mav.setViewName("pay/balance/index");
		return mav;
	}

	@RequestMapping(value = "/balance/paying")
	public ModelAndView showOrder(HttpServletRequest request,
			HttpServletResponse response) throws LoginUserNotFoundException {
		ModelAndView mav = new ModelAndView();
		
		String id = request.getParameter("id"); // 获取订单ID
		
		Account account = this.getAccount(request, response);
		String payPassword = request.getParameter("payPassword");
		if(!account.getPayCode().equals(Md5Util.MD5Encode(payPassword))){
			request.setAttribute("code", 1);
			request.setAttribute("message", "支付密码错误");
			return accountInfo(request, response);
		} 

		/**
		 * 获取订单信息
		 */
		AppOrder appOrder = appOrderService.getAppOrderById(id);
		/**
		 * 余额进行支付
		 */
		Boolean isSucceed = service.balancePaying(appOrder);
		
		if(isSucceed){ //成功
//			mav.addObject("OrdId", appOrder.getAppId()+appOrder.getOrderId());
//			mav.setViewName("pay/balance/sendorder");
			
			//把请求参数打包成数组
			Map<String, String> sPara = new HashMap<String, String>();
			sPara.put("OrdId", appOrder.getAppId()+appOrder.getOrderId());
			List<String> keys = new ArrayList<String>(sPara.keySet());
			
	        String action = request.getContextPath()+"/pay/ebank/complete.do";	//form action 地址
	        String describe = "正在为您支付，请稍候...";	//跳转时描述
	        
	        mav.addObject("action", action);
	        mav.addObject("describe", describe);
	        mav.addObject("keys", keys);
	        mav.addObject("sPara", sPara);
			mav.setViewName("pay/send");
		} else{ //失败
			mav.addObject("msg", "扣款失败");
			mav.setViewName("pay/ebank/error");
		}
		return mav;
	}
}
