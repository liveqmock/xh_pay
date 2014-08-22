package com.xinhuanet.pay.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.ChinapnrProperties;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.AlipayService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.SysConfigService;
import com.xinhuanet.pay.service.alipay.AlipayConfig;
import com.xinhuanet.pay.service.alipay.AlipayCore;
import com.xinhuanet.pay.service.alipay.AlipayNotify;
import com.xinhuanet.pay.service.alipay.AlipaySubmit;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;

/**
 * 第三方支付平台，支付宝控制器
 * @author duanwc
 */
@Controller
public class AlipayController extends BaseController {
	
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
	 * 系统配置项
	 */
	private @Autowired
	SysConfigService configService;
	
	/**
	 * 支付宝服务
	 */
	private @Autowired
	AlipayService alipayService;
	
	/**
	 * 支付宝通知服务
	 */
	private @Autowired
	AlipayNotify alipayNotify;
	
	
	/**
	 * 确认订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/third/choose")
	public ModelAndView showConfirmChinaPnrOrder(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		this.getAccount(request, response);
		String id = request.getParameter("id");
		AppOrder order = appOrderService.getAppOrderById(id);
		if (order == null) {
			mav.setViewName("pay/chinapnr/failed");
			return mav;
		}
		mav.addObject("order", order);
		mav.setViewName("pay/third/party");
		return mav;
	}
	
	
	/**
	 * 展现订单信息 再跳转至三方平台
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/third/order/pay")
	public ModelAndView sendOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException, UnsupportedEncodingException{
		ModelAndView mav = new ModelAndView();
		this.getAccount(request, response);
		
		//展现订单
		String id = request.getParameter("id");
		
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
		od.setPayType(ThirdPartyGateway.alipay);
		od.setGateId(ThirdPartyGateway.alipay);
		od.setGateName(ThirdPartyGateway.alipay_name);
		od.setPayTime(Function.getDateTime());
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		od.setBeforeMoney(account.getMoney());
		od.setOrderType(ao.getOrderType());
		int i = payOrderService.addOrder(od);
		
		if(i>0){
			////////////////////////////////////请求参数//////////////////////////////////////

			//支付类型
			String payment_type = "1";
			//必填，不能修改
			//服务器异步通知页面路径
			String notify_url = props.getString("Alipay.PCPay.BgRetUrl");
			//需http://格式的完整路径，不能加?id=123这类自定义参数

			//页面跳转同步通知页面路径
//			String return_url = "http://pay.home.news.cn:7001/xh_pay/order/alipay/pcorderpayreturn.do";
			String return_url = props.getString("Alipay.PCPay.RetUrl");
			//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

			//卖家支付宝帐户
			String seller_email = props.getString("Alipay.seller_email");
			//必填

			//商户订单号
			String out_trade_no = od.getId();
			//商户网站订单系统中唯一订单号，必填

			//订单名称
			//String subject = "个人账户测试";
			String subject = "新华网支付平台";
			if (StringUtils.isNotBlank(ao.getPname())){
				subject = ao.getPname();
			}
			//必填

			//付款金额
			//String total_fee = "0.01";
			String total_fee = Function.formtDecimalPoint2(ao.getMoney());
			//必填

			//订单描述

			//String body = "个人账户投入，每次贡献一分钱";
			String body = subject;
			//商品展示地址
			String show_url = "";
			//需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html

			//防钓鱼时间戳
			String anti_phishing_key = "";
			//若要使用请调用类文件submit中的query_timestamp函数

			//客户端的IP地址
			//String exter_invoke_ip = "";
			String exter_invoke_ip = HttpUtil.getIpAddr(request);
			//非局域网的外网IP地址，如：221.0.0.1
			
			//公用回传参数
			String extra_common_param = od.getMerpriv();
			//如果用户请求时传递了该参数，则返回给商户时会回传该参数。
			
			//////////////////////////////////////////////////////////////////////////////////
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "create_direct_pay_by_user");
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", payment_type);
			sParaTemp.put("notify_url", notify_url);
			sParaTemp.put("return_url", return_url);
			sParaTemp.put("seller_email", seller_email);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("body", body);
			sParaTemp.put("show_url", show_url);
			sParaTemp.put("anti_phishing_key", anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
			sParaTemp.put("extra_common_param", extra_common_param);
			
	        //待请求参数数组,配置
	        Map<String, String> sPara = buildRequestPara(sParaTemp);
	        List<String> keys = new ArrayList<String>(sPara.keySet());
	        String action = AlipaySubmit.ALIPAY_GATEWAY_NEW + "_input_charset=" + AlipayConfig.input_charset;	//form action 地址
	        String describe = "正在为您转向支付宝，请稍候...";	//跳转时描述
	        
	        mav.addObject("action", action);
	        mav.addObject("describe", describe);
	        mav.addObject("keys", keys);
	        mav.addObject("sPara", sPara);
	        mav.setViewName("pay/send");
		}
		return mav;
	}
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = AlipaySubmit.buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayConfig.sign_type);

        return sPara;
    }
    
    
	/**
	 * pc支付时汇付回调前台url地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/alipay/pcorderpayreturn.do")
	public ModelAndView pcorderPayReturn(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		System.out.println("支付宝同步通知页面");
		//获取支付宝GET过来反馈信息
		Map<String,String> params = getRequestParam(request);
		System.out.println("同步通知："+params);
		
		//计算得出通知验证结果
		boolean verify_result = alipayNotify.verify(params);

		//交易状态
		String trade_status = request.getParameter("trade_status");
		

		if(verify_result){//验证成功
			updateAppOrderAndOrder(params);
			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				// 页面获取信息再跳转到应用通知地址中
				mav.setView(new RedirectView(request.getContextPath()
						+ "/pay/ebank/complete.do?OrdId="+params.get("out_trade_no")));
			} else {
				String message = "交易失败";
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}

		} else {
			//该页面可做页面美工编辑
			String message = "验签失败";
			logger.info("alipay交易验签失败");
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
		}
		
		return mav;
	}
	
	/**
	 * pc支付时支付宝回调后台url地址
	 * @param request
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/order/alipay/pcorderpaybgreturn.do")
	public String pcorderBgPayReturn(HttpServletRequest request) {
		String result = "fail";
		logger.info("支付宝后台通知处理>>>>>start");
		//获取支付宝GET过来反馈信息
		Map<String,String> params = getRequestParam(request);
		System.out.println("异步通知："+params);
		
		String out_trade_no = request.getParameter("out_trade_no");
		boolean verify_result = alipayNotify.verify(params);//验签且验证notify_id  结果
		
		if (verify_result){//验证成功
			updateAppOrderAndOrder(params);
			result = "success";
		} else {
			logger.info("Alipay交易(订单号:"+out_trade_no+")认证签名失败");
		}
		logger.info("支付宝后台通知处理>>>>>end");
		return result;
	}
	
	/**
	 * 获取支付宝返回的参数
	 * @param request
	 * @return
	 */
	public Map<String, String> getRequestParam(HttpServletRequest request){
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			System.out.println(valueStr);
			params.put(name, valueStr);
		}
		
		return params;
	}
	
	/**
	 * 更新订单操作(同一笔订单的更新操作保持同步)
	 * @param params
	 */
	private void updateAppOrderAndOrder(Map<String,String> params){
		
		String lock = params.get("out_trade_no").intern();
		synchronized (lock) {
			boolean transFlag = false;
			Order order = payOrderService.getOrderById(params.get("out_trade_no"));
			AppOrder appOrder = appOrderService.getAppOrderById(params.get("extra_common_param"));
			order.setTrxId(params.get("trade_no"));//支付宝交易流水号
			order.setMoney(Double.parseDouble(params.get("total_fee")));
			if ( "TRADE_FINISHED".equals(params.get("trade_status")) || "TRADE_SUCCESS".equals(params.get("trade_status")) ){
				transFlag = true;
			}
			
			if (EBank.isOperate(order.getPayStatus(), transFlag)) {
				if (transFlag){
					alipayService.thridOrdSucceedNotify(order,appOrder);
					logger.info("通联支付(订单号:"+params.get("out_trade_no")+")交易成功,后台更新订单成功");
				} else {
					alipayService.thridOrdFailedNotify(order, appOrder);
					logger.info("通联支付(订单号:"+params.get("out_trade_no")+")交易失败,后台更新订单成功");
				}
			}
		}
		
	}

	/**
	 * 支付宝批量退款有密接口
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@RequestMapping(value="/order/alipay/refundOrder.do")
	public ModelAndView refundOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		
		ModelAndView mav = new ModelAndView();
		
		String oldOrdId = request.getParameter("oldOrdId");
		Order order = payOrderService.getOrderById(oldOrdId); // 原订单信息
		String ordId = payOrderService.getOrderIdNumber(); //新生成订单号
		
		////////////////////////////////////请求参数//////////////////////////////////////

		//支付类型
		String payment_type = "1";
		//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = props.getString("Alipay.Refund.BgRetUrl");
		//需http://格式的完整路径，不能加?id=123这类自定义参数

		//卖家支付宝帐户
		String seller_email = props.getString("Alipay.seller_email");
		//必填
		
		//退款当天日期
		String refund_date = Function.getDateTimeString();
		//必填，格式：年[4位]-月[2位]-日[2位] 小时[2位 24小时制]:分[2位]:秒[2位]，如：2007-10-01 13:13:13

		//批次号
		String batch_no = ordId;
		//必填，格式：当天日期[8位]+序列号[3至24位]，如：201008010000001
		
		//退款笔数
		String batch_num = "1";
		//必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）
		
		//退款详细数据
		//单笔退款格式:2014031127904413^0.01^无理由退款，含义:原付款支付宝交易号^退款总金额^退款理由
		String detail_data = order.getTrxId()+"^"+order.getMoney()+"^"+"无";
		//必填，具体格式请参见接口技术文档
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("refund_date", refund_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);
		
		
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());
        String action = AlipaySubmit.ALIPAY_GATEWAY_NEW + "_input_charset=" + AlipayConfig.input_charset;	//form action 地址
        String describe = "正在为您转向支付宝，请稍候...";	//跳转时描述
        
        mav.addObject("action", action);
        mav.addObject("describe", describe);
        //TODO 记录退款订单
        mav.addObject("keys", keys);
        mav.addObject("sPara", sPara);
        mav.setViewName("pay/send");
		return mav;
	}
	
	/**
	 * 支付宝批量退款有密接口
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@ResponseBody
	@RequestMapping(value="/alipay/refund/notify.do")
	public String refundOrderNotify(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		String result = "";
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//批次号

		String batch_no = request.getParameter("batch_no");

		//批量退款数据中转账成功的笔数

		String success_num = request.getParameter("success_num");

		//批量退款数据中的详细信息
		String result_details = request.getParameter("result_details");
		
		//TODO 需解析支付宝返回数据
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(alipayNotify.verify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			//判断是否在商户网站中已经做过了这次通知返回的处理
				//如果没有做过处理，那么执行商户的业务程序
				//如果有做过处理，那么不执行商户的业务程序
			result = "success";	
//			out.println("success");	//请不要修改或删除

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			result = "fail";	
//			out.println("fail");
		}
		

		return "{'name':'支付宝退款'}";
	}
}
