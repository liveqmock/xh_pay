package com.xinhuanet.pay.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.SysConfigService;
import com.xinhuanet.pay.service.TenpayService;
import com.xinhuanet.pay.service.tenpay.TenpayHttpClient;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;
import com.xinhuanet.pay.util.Md5Util;
import com.xinhuanet.pay.util.XMLUtil;

/**
 * 第三方支付平台，财付通控制器
 * @author xinhuanet
 *
 */
@Controller
public class TenpayController extends BaseController {
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
	 * 财付通服务
	 */
	private @Autowired
	TenpayService tenpayService;
	
	@RequestMapping(value = "/third/order/tenpay")
	public ModelAndView sendOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException, UnsupportedEncodingException{
		ModelAndView mav = new ModelAndView();
		this.getAccount(request, response);
		String id = request.getParameter("id");
		
		//获取用户信息
		Account account = accountService.getAccount(this.getUserId(request, response));
		AppOrder ao = appOrderService.getAppOrderById(id);
		String subject = "新华网支付平台";
		if (StringUtils.isNotBlank(ao.getPname())){
			subject = ao.getPname();
		}
		
		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
		String rd = Function.getFixLenthString(4);// 4位随机数
		Order od = new Order();
		//接着做订单order
		od.setId(nt+rd);
		od.setUid(ao.getUid());
		od.setLoginName(ao.getLoginName());
		od.setMoney(ao.getMoney());
		od.setAppId(ao.getAppId());
		od.setAppOrderId(ao.getOrderId());
		od.setPayStatus(OrderStatus.ORDER_STATUS_INIT);//新订单默认为初始状态
		od.setPayType(ThirdPartyGateway.tenpay);
		od.setGateId(ThirdPartyGateway.tenpay);
		od.setGateName(ThirdPartyGateway.tenpay_name);
		od.setPayTime(Function.getDateTime());
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		od.setBeforeMoney(account.getMoney());
		od.setOrderType(ao.getOrderType());
		int i = payOrderService.addOrder(od);
		
		if(i>0){
			//组织财付通支付接口数据
			String tenpayKey = props.getString("Tenpay.key");
			String tenpayGateUrl = props.getString("Tenpay.GateUrl");
			
			//-----------------------------
			//设置支付参数
			//-----------------------------
			//将数据放入集合中准备加签数据
			TreeMap<String,String> tenpayParam = new TreeMap<String,String>();
			tenpayParam.put("partner", props.getString("Tenpay.partner"));		        //商户号
			tenpayParam.put("out_trade_no", od.getId());		//商家订单号
			tenpayParam.put("total_fee", String.valueOf(Function.yuan2Cents(od.getMoney())));			        //商品金额,以分为单位
			tenpayParam.put("return_url", props.getString("Tenpay.PCPay.RetUrl"));		    //交易完成后跳转的URL
			tenpayParam.put("notify_url", props.getString("Tenpay.PCPay.BgRetUrl"));		    //接收财付通通知的URL
			tenpayParam.put("body", subject);	                    //商品描述
			tenpayParam.put("bank_type", "DEFAULT");		    //银行类型(中介担保时此参数无效)
			tenpayParam.put("spbill_create_ip",request.getRemoteAddr());   //用户的公网ip，不是商户服务器IP
			tenpayParam.put("fee_type", "1");                    //币种，1人民币
			tenpayParam.put("subject", subject);              //商品名称(中介交易时必填)
            
            //系统可选参数         
			tenpayParam.put("input_charset", "UTF-8");            //字符编码
            
            //业务可选参数          
			tenpayParam.put("attach", ao.getId());              //附加数据，原样返回
			
			String sign = createSign(tenpayParam,tenpayKey);
			tenpayParam.put("sign", sign);
			String reqPars = getUrlParams(tenpayParam);
			String gateUrl = tenpayGateUrl+"?"+reqPars;
			RedirectView view = new RedirectView(gateUrl);
			mav.setView(view);
		}
		return mav;
	}
	
	/**
	 * pc支付时财付通回调前台url地址
	 * @param request
	 * @return
	 * 
	 */
	@RequestMapping(value="/order/tenpay/pcorderpayreturn.do")
	public ModelAndView pcorderPayReturn(HttpServletRequest request){
		//获取财付通返回的参数
		ModelAndView mav = new ModelAndView();
		TreeMap<String,String> params = getRequestParam(request);
		
		//验签
		String sign = params.get("sign");
		String signRes = createSign(params,props.getString("Tenpay.key"));
		
		//验签成功后处理逻辑
		if (signRes.equals(sign)){
			
			String out_trade_no = params.get("out_trade_no");     //商户订单号
			String transaction_id = params.get("transaction_id"); //财付通订单号
			String total_fee = params.get("total_fee");           //金额,以分为单位
			String notify_id = params.get("notify_id");           //通知id
			String trade_state = params.get("trade_state");       //支付结果
			String attach = params.get("attach");                 //附加数据(保存appOrder id)
			String errMsg = params.get("pay_info");               //交易结果信息，成功时为空
			boolean transFlag = "0".equals(trade_state);          //true:表示交易成功
			
			Order order = payOrderService.getOrderById(out_trade_no);
			AppOrder appOrder = appOrderService.getAppOrderById(attach);
			order.setTrxId(transaction_id); 
			order.setMoney(Function.cents2Yuan(Long.parseLong(total_fee)));
			order.setException(errMsg);
			
			boolean vertifyResult = vertifyNotigyId(params);      //校验notify_id
			if (vertifyResult) {
				if (EBank.isOperate(order.getPayStatus(), transFlag)) {
					if (transFlag){
						tenpayService.thridOrdSucceedNotify(order,appOrder);
						mav.setView(new RedirectView(request.getContextPath() + "/pay/ebank/complete.do?OrdId="+out_trade_no));
					} else {
						tenpayService.thridOrdFailedNotify(order, appOrder);
						String message = "交易失败";
						logger.info("交易失败");
						mav.addObject("msg", message);
						mav.setViewName("pay/ebank/error");
					}
				}
			} else {
				String message = "验证通知失败";
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}
		} else {
			System.out.println("验证失败");
			String message = "验证失败";
			logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")认证签名失败");
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
		}
		return mav;
	}
	
	/**
	 * pc支付时财付通回调后台url地址
	 * @param request
	 * @return
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/order/tenpay/pcorderpaybgreturn.do")
	public String pcorderPayBgReturn(HttpServletRequest request) {
		String result = "fail";
		TreeMap<String,String> params = getRequestParam(request);
		//验签
		String sign = params.get("sign");
		String signRes = createSign(params,props.getString("Tenpay.key"));
		if (signRes.equals(sign)){//验签成功
			String out_trade_no = params.get("out_trade_no");
			String attach = params.get("attach");
			String transaction_id = params.get("transaction_id");
			String total_fee = params.get("total_fee");//金额,以分为单位
			String errMsg = params.get("pay_info");//交易结果信息，成功时为空
			String trade_state = params.get("trade_state");//支付结果
			boolean transFlag = "0".equals(trade_state);//0 表示成功
			boolean vertifyResult = vertifyNotigyId(params);//校验notify_id
			
			if (vertifyResult){
				Order order = payOrderService.getOrderById(out_trade_no);
				AppOrder appOrder = appOrderService.getAppOrderById(attach);
				order.setTrxId(transaction_id); 
				order.setMoney(Function.cents2Yuan(Long.parseLong(total_fee)));
				order.setException(errMsg);
				if (EBank.isOperate(order.getPayStatus(), transFlag)) {
					if (transFlag){
						tenpayService.thridOrdSucceedNotify(order,appOrder);
						logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")成功，后台处理成功更新订单");
					} else {
						tenpayService.thridOrdFailedNotify(order, appOrder);
						logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")失败，后台处理成功更新订单");
					}
				}
				result = "success";
			}
		} else {//验签失败
			logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")认证签名失败");
		}
		return result;
	}
	
	/**
	 * 校验notify_id
	 * @return
	 * 
	 */
	public boolean vertifyNotigyId(TreeMap<String,String> params){
		boolean resultFlag = false;
		String notifyGateway = props.getString("Tenpay.Notify.GateUrl");
		
		TreeMap<String,String> tenpayNotifyParam = new TreeMap<String,String>();
		tenpayNotifyParam.put("input_charset", "UTF-8");
		tenpayNotifyParam.put("partner", props.getString("Tenpay.partner"));
		tenpayNotifyParam.put("notify_id", params.get("notify_id"));
		String signResult = createSign(tenpayNotifyParam,props.getString("Tenpay.key"));
		tenpayNotifyParam.put("sign", signResult);
		
		TenpayHttpClient httpClient = new TenpayHttpClient();
		String reqPars = getUrlParams(tenpayNotifyParam);
		String gateUrl = notifyGateway+"?"+reqPars;
		httpClient.setTimeOut(5);
		httpClient.setReqContent(gateUrl);
		
		if (httpClient.call()) {
			try {
				String resultContent = httpClient.getResContent();
				TreeMap<String, String> resultParams;
				resultParams = XMLUtil.doXMLParse(resultContent);
				//获取id验证返回状态码，0表示此通知id是财付通发起
				String retcode = resultParams.get("retcode");
				String signNotyfy = resultParams.get("sign");
				String signNotyfyRes = createSign(resultParams,props.getString("Tenpay.key"));
				if ( "0".equals(retcode) ){
					if (signNotyfyRes.equals(signNotyfy)) {
						resultFlag = true;
					} else {
						logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")，通知id:"+params.get("notify_id")+" 返回结果验签失败");
					}
				} else if("88222005".equals(retcode)) {
					logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")，通知id:"+params.get("notify_id")+" 超时");
				} else {
					logger.info("Tenpay交易(订单号:"+params.get("out_trade_no")+")，通知id:"+params.get("notify_id")+" 验证失败");
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultFlag;
	}
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * @param tenpayParam
	 * @param tenpayKey
	 * @return
	 */
	protected String createSign(TreeMap<String,String> tenpayParam,String tenpayKey) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = tenpayParam.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while(it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + tenpayKey);
		String sign = Md5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		
		//debug信息
		logger.info(sb.toString() + " => sign:" + sign);
		return sign;
	}
	
	/**
	 * 拼接Tenpay支付网关 参数
	 * @param tenpayParam
	 * @return
	 */
	public String getUrlParams(SortedMap<String,String> tenpayParam){
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = tenpayParam.entrySet();
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			try {
				sb.append(k + "=" + URLEncoder.encode(v, "UTF-8") + "&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.substring(0, sb.lastIndexOf("&"));
	}
	
	/**
	 * 获取财付通返回的参数
	 * @param request
	 * @return
	 */
	public TreeMap<String,String> getRequestParam(HttpServletRequest request){
		TreeMap<String,String> params = new TreeMap<String,String>();
		Map m = request.getParameterMap();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];			
			params.put(k, v);
		}
		return params;
	}
	
	
}
