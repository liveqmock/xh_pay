package com.xinhuanet.pay.action;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.dao.impl.AccountDaoImpl;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.Allinpay;
import com.xinhuanet.pay.gateway.AllinpayGateway;
import com.xinhuanet.pay.gateway.BankGatewayEntry;
import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AllinpayProperties;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AllinpayService;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;

/**
 * 第三方支付平台，通联支付控制器
 * @author duanwc
 */
@Controller
public class AllinpayController extends BaseController {
	private static final Logger logger = Logger.getLogger(AllinpayController.class);
	/**
	 * 账户服务
	 */
	private @Autowired
	AccountService accountService;
	/**
	 * 订单服务
	 */
	private @Autowired
	PayOrderService payOrderService;
	
	/**
	 * app订单服务
	 */
	private @Autowired
	PayAppOrderService appOrderService;
	
	/**
	 * 通联支付业务service
	 */
	private @Autowired
	AllinpayService allinpayService;
	

	
	/**
	 * 展现订单信息 再跳转至三方平台
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	public ModelAndView sendOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		ModelAndView mav = new ModelAndView();
		//展现订单
		String id = request.getParameter("id");
		String bank = request.getParameter("bank");
		//获得适配对象
		Map<String,Object> config = AllinpayGateway.getInstance().getConfigMap();
		
		//获取用户信息
		Account account = accountService.getAccount(this.getUserId(request, response));
		AppOrder ao = appOrderService.getAppOrderById(id);
		
		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
		String rd = Function.getFixLenthString(6);// 6位随机数
		
		Order od = new Order();
		//接着做订单order
		od.setId(nt+rd);
		od.setUid(ao.getUid());
		od.setLoginName(ao.getLoginName());
		od.setMoney(ao.getMoney());
		od.setAppId(ao.getAppId());
		od.setAppOrderId(ao.getOrderId());
		od.setPayStatus(OrderStatus.ORDER_STATUS_INIT);//新订单默认为初始状态
		od.setPayType(ThirdPartyGateway.allinpay);
		od.setPayTime(Function.getDateTime());
		od.setGateId(((BankGatewayEntry)config.get(bank)).getKey());
		od.setGateName(((BankGatewayEntry)config.get(bank)).getValue());
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		od.setBeforeMoney(account.getMoney());
		od.setOrderType(ao.getOrderType());
		int i = payOrderService.addOrder(od);
		if(i>0){
			//设置通联支付所需属性
			AllinpayProperties allinpay = new AllinpayProperties();
			allinpay.setPickupUrl(this.getProperty("Allinpay.PCPay.RetUrl"));//2. 取货地址
			allinpay.setReceiveUrl(this.getProperty("Allinpay.PCPay.BgRetUrl"));//3. 商户系统通知地址
			allinpay.setVersion("v1.0"); //4. 版本号
			allinpay.setSignType(1); //6. 签名类型 默认填1，固定选择值：0、1；0表示客户用MD5验签，1表示客户用证书验签
			
			allinpay.setMerchantId(this.getProperty("Allinpay.MerId")); //7. 商户号
			allinpay.setOrderNo(StringUtils.trimToEmpty(od.getId())); //13. 商户系统订单号
			allinpay.setOrderAmount(Function.yuan2Cents(od.getMoney())); //14. 订单金额(单位分)
			allinpay.setOrderDatetime(Allinpay.formatAllinpayTime(od.getPayTime())); //16. 商户的订单提交时间
			allinpay.setExt1(od.getMerpriv());	//扩展字段，应用的订单ID
			//TODO 通联需要时间为必要参数，需将DAO时间由controller传递
//			allinpay.setPayType(0);//支付方式
			allinpay.setPayType(1);//直达银行
			allinpay.setIssuerId(od.getGateId());
			allinpay.setKey(this.getProperty("Allinpay.key"));//用于计算signMsg的key值
			
			//构造订单请求对象，生成signMsg。
			com.allinpay.ets.client.RequestOrder requestOrder = new com.allinpay.ets.client.RequestOrder();
			requestOrder.setPickupUrl(allinpay.getPickupUrl());
			requestOrder.setReceiveUrl(allinpay.getReceiveUrl());
			requestOrder.setVersion(allinpay.getVersion());
			requestOrder.setSignType(allinpay.getSignType());
			requestOrder.setPayType(allinpay.getPayType());
			requestOrder.setMerchantId(allinpay.getMerchantId());
			requestOrder.setOrderNo(allinpay.getOrderNo());
			requestOrder.setOrderAmount(allinpay.getOrderAmount());
			requestOrder.setOrderDatetime(allinpay.getOrderDatetime());
			requestOrder.setExt1(allinpay.getExt1());
			requestOrder.setIssuerId(allinpay.getIssuerId());
			requestOrder.setKey(allinpay.getKey()); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。

//			String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
			String strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
			
			allinpay.setSignMsg(strSignMsg);	//签名串
			
			
			//把请求参数打包成数组
			Map<String, String> sPara = new HashMap<String, String>();

			sPara.put("pickupUrl", allinpay.getPickupUrl());
			sPara.put("receiveUrl", allinpay.getReceiveUrl());
			sPara.put("version", allinpay.getVersion());
			sPara.put("signType", String.valueOf(allinpay.getSignType()));
			sPara.put("merchantId", allinpay.getMerchantId());
			sPara.put("orderNo", allinpay.getOrderNo());
			sPara.put("orderAmount", String.valueOf(allinpay.getOrderAmount()));
			sPara.put("orderDatetime", allinpay.getOrderDatetime());
			sPara.put("ext1", allinpay.getExt1());
			sPara.put("payType", String.valueOf(allinpay.getPayType()));
			sPara.put("issuerId", allinpay.getIssuerId());
			sPara.put("signMsg", allinpay.getSignMsg());
			List<String> keys = new ArrayList<String>(sPara.keySet());
			
//	        String action = "http://ceshi.allinpay.com/gateway/index.do";	//form action 地址
	        String action = props.getString("Allinpay.form.action");
	        String describe = "正在为您转向通联支付，请稍候...";	//跳转时描述
	        
	        mav.addObject("action", action);
	        mav.addObject("describe", describe);
	        mav.addObject("keys", keys);
	        mav.addObject("sPara", sPara);
			mav.setViewName("pay/send");
		}else{
			mav.addObject("msg", "生成支付订单失败！");
			mav.setViewName("pay/ebank/error");
		}
		return mav;
	}
	
	/**
	 * pc支付时第三方回调前台url地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/allinpay/pcorderpayreturn.do")
	public ModelAndView pcorderPayReturn(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		System.out.println("通联支付前台页返回……前台通知页面");
		
		String message = "";// 提示信息
		boolean bStatus = true;// 是否成功
		
		//将通联返回的数据封装到PaymentResult
		com.allinpay.ets.client.PaymentResult paymentResult = converToPaymentResult(request);
		
		//signType为"1"时，必须设置证书路径。
		URL keyFile = getClass().getResource("/allinpay/TLCert-test.cer");
		if(keyFile!=null){
			paymentResult.setCertPath(keyFile.getPath());
		} else{
			//TODO 跳转到提示页
		}
		
		//验证签名：返回true代表验签成功；否则验签失败。
		boolean verifyResult = paymentResult.verify();
		
		if(verifyResult){
			updateAppOrderAndOrder(paymentResult);
			boolean transFlag = "1".equals(paymentResult.getPayResult());//true 表示交易成功
			if(transFlag){
				mav.setView(new RedirectView(request.getContextPath()
						+ "/pay/ebank/complete.do?OrdId="+paymentResult.getOrderNo()));
			}else{
				message = "交易失败";
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}
		}else{
			message = "交易失败";
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
			logger.info("通联支付后台通知(订单号:"+paymentResult.getOrderNo()+")验签失败");
		}
		
		return mav;
	}
	
	/**
	 * pc支付时第三方回调后台url地址
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/order/allinpay/pcorderpaybgreturn.do")
	public String pcorderPayBgreturn(HttpServletRequest request,HttpServletResponse response){
		StringBuffer strBuf = new StringBuffer();
		//将通联返回的数据封装到PaymentResult
		logger.info("通联后台通知处理>>>>>start");
		com.allinpay.ets.client.PaymentResult paymentResult = converToPaymentResult(request);
		
		URL keyFile = getClass().getResource("/allinpay/TLCert-test.cer");
		if (keyFile != null){
			paymentResult.setCertPath(keyFile.getPath());
		} else {
			logger.info("通联支付后台通知(订单号:"+paymentResult.getOrderNo()+"),/allinpay/TLCert-test.cer 证书未找到");
		}
		boolean verifyResult = paymentResult.verify();
		if (verifyResult){
			updateAppOrderAndOrder(paymentResult);
			strBuf.append("商户订单处理完成");
			strBuf.append("Server接收处理完成");
		} else {
			logger.info("通联支付后台通知(订单号:"+paymentResult.getOrderNo()+")验签失败");
		}
		logger.info("通联后台通知处理>>>>>end");
		return strBuf.toString();
	}
	
	
	/**
	 * 通联支付查询订单详细
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/allinpay/queryOrder.do")
	public String queryOrder(HttpServletRequest request,HttpServletResponse response) {
		String ordId = request.getParameter("orderId");
		System.out.println("-------------------orderID:"+ordId);
		AllinpayProperties allinpay = allinpayService.queryOrder(ordId);
		System.out.println("-------------------allinpay:"+allinpay);
		JSONObject json = (JSONObject) JSON.toJSON(allinpay);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 通联支付退款
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@ResponseBody
	@RequestMapping(value="/order/allinpay/refundOrder.do")
	public String refundOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		String oldOrdId = request.getParameter("oldOrdId");
		AllinpayProperties allinpay = allinpayService.refundOrder(oldOrdId);
		JSONObject json = (JSONObject) JSON.toJSON(allinpay);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 通联支付批量查询
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@ResponseBody
	@RequestMapping(value="/order/allinpay/batchQuery.do")
	public String batchQuery(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		String beginDateTime = Allinpay.getBeginDateTime();
		String endDateTime = Allinpay.getEndDateTime();
		String pageNo = "1";
		
		String result = allinpayService.batchQuery(beginDateTime, endDateTime, pageNo);
		return result;
	}

	/**
	 * 封装通联支付返回结果到PaymentResult
	 * @param request
	 * @return
	 */
	public com.allinpay.ets.client.PaymentResult converToPaymentResult(HttpServletRequest request){
		com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult(); 
		String merchantId=request.getParameter("merchantId");
		String version=request.getParameter("version");
		String language=request.getParameter("language");
		String signType=request.getParameter("signType");
		String payType=request.getParameter("payType");
		String issuerId=request.getParameter("issuerId");
		String paymentOrderId=request.getParameter("paymentOrderId");
		String orderNo=request.getParameter("orderNo");
		String orderDatetime=request.getParameter("orderDatetime");
		String orderAmount=request.getParameter("orderAmount");
		String payDatetime=request.getParameter("payDatetime");
		String payAmount=request.getParameter("payAmount");
		String ext1=request.getParameter("ext1");
		String ext2=request.getParameter("ext2");
		String payResult=request.getParameter("payResult");
		String errorCode=request.getParameter("errorCode");
		String returnDatetime=request.getParameter("returnDatetime");
		String signMsg=request.getParameter("signMsg");
		
		//构造订单结果对象，验证签名。
		paymentResult.setMerchantId(merchantId);
		paymentResult.setVersion(version);
		paymentResult.setLanguage(language);
		paymentResult.setSignType(signType);
		paymentResult.setPayType(payType);
		paymentResult.setIssuerId(issuerId);
		paymentResult.setPaymentOrderId(paymentOrderId);
		paymentResult.setOrderNo(orderNo);
		paymentResult.setOrderDatetime(orderDatetime);
		paymentResult.setOrderAmount(orderAmount);
		paymentResult.setPayDatetime(payDatetime);
		paymentResult.setPayAmount(payAmount);
		paymentResult.setExt1(ext1);
		paymentResult.setExt2(ext2);
		paymentResult.setPayResult(payResult);
		paymentResult.setErrorCode(errorCode);
		paymentResult.setReturnDatetime(returnDatetime);
		paymentResult.setSignMsg(signMsg);
		
		return paymentResult;
	}
	
	/**
	 * 更新订单操作(同一笔订单的更新操作保持同步)
	 * @param paymentResult
	 */
	private void updateAppOrderAndOrder(com.allinpay.ets.client.PaymentResult paymentResult){
		
		String lock = paymentResult.getOrderNo().intern();
		synchronized (lock) {
			Order order = payOrderService.getOrderById(paymentResult.getOrderNo());
			AppOrder appOrder = appOrderService.getAppOrderById(paymentResult.getExt1());
			order.setTrxId(paymentResult.getOrderNo());
			order.setMoney(Function.cents2Yuan(Long.parseLong(paymentResult.getPayAmount())));
			boolean transFlag = "1".equals(paymentResult.getPayResult());//true 表示交易成功
			
			if (EBank.isOperate(order.getPayStatus(), transFlag)) {
				if (transFlag){
					allinpayService.thridOrdSucceedNotify(order,appOrder);
					logger.info("通联支付通知(订单号:"+paymentResult.getOrderNo()+")交易成功,后台更新订单成功");
				} else {
					allinpayService.thridOrdFailedNotify(order, appOrder);
					logger.info("通联支付通知(订单号:"+paymentResult.getOrderNo()+")交易失败,后台更新订单成功");
				}
			}
		}
		
	}
	
}