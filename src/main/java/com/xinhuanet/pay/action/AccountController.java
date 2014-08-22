package com.xinhuanet.pay.action;

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

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.Constants;
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.po.SysConfig;
import com.xinhuanet.pay.service.PayDetailService;
import com.xinhuanet.pay.service.SysConfigService;
import com.xinhuanet.pay.util.Arith;
import com.xinhuanet.pay.util.Function;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class AccountController extends BaseController{
	
	/**
	 * 消费服务
	 */
	private @Autowired PayDetailService payGadgetService;
	
	/**
	 * 系统配置项
	 */
	private @Autowired SysConfigService configService;
	
	/**
	 * 查询账户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/account/info.do", method = RequestMethod.GET)
	public ModelAndView accountInfo(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		this.getAccount(request,response);
		ModelAndView mav = new ModelAndView("account/info");
		return mav;
	}
	
	/**
	 * 支付平台首页信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/account/index", method = RequestMethod.GET)
	public ModelAndView accountIndex(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		Account account = this.getAccount(request,response);
		ModelAndView mav = new ModelAndView();
		
		String uid = account.getUid();
		String page = request.getParameter("page");
		if(!Function.isNumber(page)){
			page = "1";
		}
		PageRollModel pageModel = new PageRollModel();
		int totalCount = payGadgetService.getDetailCount(uid);
		pageModel.setCurrentPage(Integer.parseInt(page));
		pageModel.setTotalCount(totalCount);
		pageModel.setPageCount(5);
		List<PayDetail> list = payGadgetService.getDetailList(uid,pageModel);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("gadgetlist", list);
		//分页参数 start
		map.put("maxRowCountAN", pageModel.getTotalCount());
		map.put("maxPageCountAN", pageModel.getPageSize());
		map.put("currentPageAN", pageModel.getCurrentPage());
		map.put("startRow", pageModel.getStartRow());
		//分页参数 end
		
		SysConfig config = configService.getSysConfig(Constants.DEPOSIT);
		Double sysDeposit =  new Double(config.getCfgValue());
		request.setAttribute("sysDeposit", sysDeposit);
		if(account.getDeposit()<sysDeposit){ //如果账户保证金金额小于系统要求保证金数额时
			request.setAttribute("depositStatus", 1);
			request.setAttribute("shortfall", Arith.sub(sysDeposit, account.getDeposit()));
		} else{//达到保证金要求
			request.setAttribute("depositStatus", 0);
		}
		
		mav.addAllObjects(map);
		mav.setViewName("account/index");
		return mav;
	}
	
	/**
	 * 查询账户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pay/charge.do")
	public ModelAndView accountCharge(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		this.getAccount(request,response);
		ModelAndView mav = new ModelAndView("account/charge");
		return mav;
	}
}
