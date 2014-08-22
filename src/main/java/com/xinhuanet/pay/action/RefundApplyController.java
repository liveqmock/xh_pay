package com.xinhuanet.pay.action;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.RefundChangeState;
import com.xinhuanet.pay.exception.AccountUnmatchErrorException;
import com.xinhuanet.pay.exception.OrderException;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.po.RefundStep;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.RefundApplyService;
import com.xinhuanet.pay.service.RefundOrderService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class RefundApplyController extends BaseController{
	
	/**
	 * 退款申请服务
	 */
	private @Autowired RefundApplyService applyService;
	/**
	 * 获取退款明细服务
	 */
	private @Autowired RefundOrderService refundService;
	/**
	 * 订单服务
	 */
	private @Autowired PayOrderService payOrderService;
	

	/**
	 * 新增退款申请
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/refund/apply.do")
	public ModelAndView refundApplyAdd(HttpServletRequest request,HttpServletResponse response,RefundApply refApply) {
		/**
		 * 封装账户属性
		 */
		Account account = this.getAccount(request,response,false);
		String uid = account.getUid();
//		refApply.setIpAddress(HttpUtil.getRealClientIp(request)); //获取IP
		try {
			refundService.add(uid,refApply);
		} catch (OrderException e) {
			e.printStackTrace();
			System.out.println("返回错误信息："+e.getMessage());
		} catch (AccountUnmatchErrorException e) {
			e.printStackTrace();
			System.out.println("返回错误信息："+e.getMessage());
		}

		ModelAndView mav = new ModelAndView("order/orderlist");
		return mav;
	}
	
	/**
	 * 用户退款申请
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/refund/apply.do")
	public ModelAndView refundUserApply(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		this.getAccount(request,response);
		String orderId = request.getParameter("orderId");
		String refundMoney = request.getParameter("refundMoney");
		ModelAndView mav = new ModelAndView();
		mav.addObject("orderId", orderId);
		mav.addObject("refundMoney", refundMoney);
		mav.setViewName("account/refund");
		return mav;
	}
	
	/**
	 * 用户退款申请确认
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/user/refund/apply/confirm.do")
	public ModelAndView userRefundApplyConfirm(HttpServletRequest request,HttpServletResponse response) {
		//获取账户信息
		Account account = this.getAccount(request,response);
		String orderId = request.getParameter("orderId");
		String reason = request.getParameter("reason");
		Order order = payOrderService.getOrderById(orderId);
		String msg = "";
		ModelAndView mav = new ModelAndView();
		RedirectView redirect = new RedirectView("/account/index");
		if(null == order){//确定订单是否存在
			msg = "该笔订单异常，请联系管理员！";
			mav.addObject("msg", msg);
			mav.setViewName("account/error");
			return mav;
		}
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(order.getChangeTime());
		calendar.add(Calendar.DATE, 7);
		Long orderLong = calendar.getTimeInMillis();
		Long currentLong = new Date().getTime();//当前时间
		if(orderLong < currentLong){//判断订单是否在有效期内
			msg = "该笔订单已经超过退款期限，不可以退款！";
			mav.addObject("msg", msg);
			mav.setViewName("account/error");
			return mav;
		}
		
		// 原订单支付成功
		if (1 != order.getPayStatus()){//判断订单是否支付成功
			msg = "该笔订单异常，请联系管理员！";
			mav.addObject("msg", msg);
			mav.setViewName("account/error");
			return mav;
		}
		
		//构造退款步骤对象
		RefundStep refundStep = new RefundStep();
		refundStep.setOrderId(order.getId());//当前退款的原订单ID
		refundStep.setReason(reason);//退款理由
		refundStep.setStep(RefundChangeState.ROLE_USER);//退款步骤,0-用户,1-应用管理员,2-财务
		refundStep.setStatus(RefundChangeState.REFUND_APPLY);//处理状态,1-退款申请,2-撤销申请,3-拒绝退款,4-同意退款,5-完成
		refundStep.setHandleTime(Function.getDateTime());//申请时间
		refundStep.setIpAddress(HttpUtil.getIpAddr(request));//用户Ip地址
		
		//判断订单退款表中时候有记录,状态为2,3的可以退款，其他状态为正在退款中
		RefundApply refundApply = applyService.getByOrderId(orderId);
		if(null != refundApply){//更新退款应用表，并且插入退款步骤表
			if(RefundChangeState.REFUND_UNDO == refundApply.getStatus() || RefundChangeState.REFUND_REFUSE == refundApply.getStatus()){
				refundApply.setReason(reason);//退款理由
				refundApply.setStep(RefundChangeState.ROLE_USER);//处理完成状态,0-用户处理完成，1-应用管理员处理完成，2-财务处理完成
				refundApply.setStatus(RefundChangeState.REFUND_APPLY);//流程状态，1-退款申请,2-撤销申请,3-拒绝退款,4-同意退款,5-处理完成	
				refundApply.setHandleTime(Function.getDateTime());//申请时间
				int i = refundService.update(refundApply, refundStep);
				if(i>0){
					redirect.setEncodingScheme("UTF-8");
					redirect.setContextRelative(true); 
					mav.setView(redirect);
				}else{
					msg = "申请失败，请重新申请!";
					mav.addObject("msg", msg);
					mav.setViewName("account/error");
				}
			}else {
				msg = "该笔订单正在退款或已退款，请查询退款详情！";
				mav.addObject("msg", msg);
				mav.setViewName("account/error");
				return mav;
			}
		}else {
			//构造退款请求对象
			refundApply = new RefundApply();		
			refundApply.setOrderId(order.getId());//当前退款的原订单ID
			refundApply.setUid(account.getUid());//用户ID
			refundApply.setLoginName(account.getLoginName());//用户登录名称
			refundApply.setTrxId(order.getTrxId());//交易号，来源于第三方交易平台
			refundApply.setAppId(order.getAppId()); //应用ID
			refundApply.setMoney(order.getMoney());//充值金额
			refundApply.setPayType(order.getPayType());//第三方网关（支付宝-alipay、通联支付-allinpay、汇付天下-chinapnr）
			refundApply.setReason(reason);//退款理由
			refundApply.setStep(RefundChangeState.ROLE_USER);//处理完成状态,0-用户处理完成，1-应用管理员处理完成，2-财务处理完成
			refundApply.setStatus(RefundChangeState.REFUND_APPLY);//流程状态，1-退款申请,2-撤销申请,3-拒绝退款,4-同意退款,5-处理完成	
			refundApply.setApply(RefundChangeState.APPLY_ACTIVE);//主动申请或被动申请(主动-0，被动-1)
			refundApply.setHandleTime(Function.getDateTime());//申请时间
			
			int i = refundService.add(refundApply, refundStep);
			if(i>0){
				redirect.setEncodingScheme("UTF-8");
				redirect.setContextRelative(true); 
				mav.setView(redirect);
			}else{
				msg = "申请失败，请重新申请!";
				mav.addObject("msg", msg);
				mav.setViewName("account/error");
			}
		}
		return mav;
	}
}
