package com.xinhuanet.pay.action;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.service.PayDetailService;
import com.xinhuanet.pay.util.Function;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class PayDetailController extends BaseController{
	/**
	 * 消费服务
	 */
	private @Autowired PayDetailService payGadgetService;
	
	@RequestMapping(value = "/consume/gadgetlist.do", method = RequestMethod.GET)
	public ModelAndView gadgetList(HttpServletRequest request,HttpServletResponse response) {
		/**
		 * 封装账户属性
		 */
		Account account = this.getAccount(request,response,false);
		
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
		ModelAndView mav = new ModelAndView("consume/gadgetlist");
		mav.addAllObjects(map);
		return mav;
	}
	
	@RequestMapping(value = "/consume/detail")
	@ResponseBody
	public String getDetail(HttpServletRequest request,HttpServletResponse response) {
		/**
		 * 封装账户属性
		 */
		Account account = this.getAccount(request,response,false);
		String id = request.getParameter("id");
		
		PayDetail detail = payGadgetService.get(new Integer(id));
		JSONObject json = new JSONObject();
		if(detail!=null){//成功
			String orderTypeName = "";
			if(detail.getOrderType() == 0){
				orderTypeName = "充值";
			} else if(detail.getOrderType() == 1){
				orderTypeName = "即时交易";
			} else{
				orderTypeName = "保证金";
			}
			json.put("code", 0);
			json.put("message", "成功");
			json.put("afterMoney", detail.getAfterMoney());//结余
			json.put("pname", detail.getPname());//商品名称
			json.put("orderId", detail.getOrderId());//订单号
			json.put("addTime", Function.getDateTimeString((Timestamp) detail.getAddTime()));//交易时间
			json.put("money", detail.getMoney());//金额
			json.put("orderType", detail.getOrderType());//交易类型，充值/即时交易/保证金
			json.put("orderTypeName", orderTypeName);//交易类型，充值/即时交易/保证金
			json.put("appName", detail.getAppName());//应用名称
			json.put("type", detail.getType());//类型，收入/支出
			json.put("typeName", detail.getType()==0?"收入":"支出");//类型，收入/支出
		} else{//失败
			json.put("code", 1);
			json.put("message", "网络存在异常，请稍候重试");
		}
		
		
		return json.toJSONString();
	}
}
