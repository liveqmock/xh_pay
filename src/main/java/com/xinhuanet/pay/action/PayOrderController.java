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
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class PayOrderController extends BaseController{
	/**
	 * 订单服务
	 */
	private @Autowired PayOrderService payOrderService;
	
	
//	/**
//	 * 反馈用户是否充值成功，并且显示账户余额
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/feedback.do", method = RequestMethod.GET)
//	public ModelAndView balanceAccount(HttpServletRequest request,HttpServletResponse response) {
//		//获取账户信息
//		Account account = this.getAccount(request,response);
//		//获取订单信息
//		PayOrder order = payOrderService.getOrder(account.getLastOrderId(),account.getUid());
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("order", order);
//		ModelAndView mav = new ModelAndView("pay/card/succee");
//		mav.addAllObjects(map);
//		return mav;
//	}
	
	/**
	 * 查询用户所有的订单列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order/orderlist.do", method = RequestMethod.GET)
	public ModelAndView orderList(HttpServletRequest request,HttpServletResponse response) {
		PageRollModel pageModel = new PageRollModel();
		/**
		 * 封装账户属性
		 */
		Account account = this.getAccount(request,response,false);
		
		String uid = account.getUid();
		String page = request.getParameter("page");
		if(!Function.isNumber(page)){
			page = "1";
		}
		int totalCount = payOrderService.getOrderCount(uid);
		pageModel.setCurrentPage(Integer.parseInt(page));
		pageModel.setTotalCount(totalCount);
		pageModel.setPageCount(5);
		List<Order> list = payOrderService.getOrderList(uid,pageModel);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderlist", list);
		//分页参数 start
		map.put("maxRowCountAN", pageModel.getTotalCount());
		map.put("maxPageCountAN", pageModel.getPageSize());
		map.put("currentPageAN", pageModel.getCurrentPage());
		map.put("startRow", pageModel.getStartRow());
		//分页参数 end
		ModelAndView mav = new ModelAndView("order/orderlist");
		mav.addAllObjects(map);
		return mav;
	}
}
