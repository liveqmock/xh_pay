package com.xinhuanet.pay.service.impl;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.allinpay.ets.client.PaymentResult;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.StringUtil;
import com.allinpay.ets.client.util.Base64;
import com.xinhuanet.pay.gateway.Allinpay;
import com.xinhuanet.pay.po.AllinpayProperties;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AllinpayService;
import com.xinhuanet.pay.service.HttpClient4Service;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;

@Component
public class AllinpayServiceImpl extends ThridPartyServiceImpl<AllinpayProperties> implements AllinpayService {
    private static final Logger logger = LoggerFactory.getLogger(AllinpayServiceImpl.class);
    
	/**
	 * 订单服务
	 */
	private @Autowired
	PayOrderService payOrderService;
	
	/**
	 * 读取配置文件服务
	 */
	protected @Autowired PropertiesConfiguration props;
	/**
	 * HttpClient服务
	 */
	protected @Autowired HttpClient4Service clientService;

	

	@Override
	public void thridOrdFailedNotify(Order order, AppOrder appOrder) {
		super.thridOrdFailedNotify(order, appOrder);
	}

	@Override
	public void thridOrdSucceedNotify(Order order, AppOrder appOrder) {
		super.thridOrdSucceedNotify(order, appOrder);
	}

	@Override
	public AllinpayProperties queryOrder(String ordId) {
		
		AllinpayProperties allinpay = new AllinpayProperties();
		
		Order order = payOrderService.getOrderById(ordId);// 查询当前订单的信息
		// 设置通联支付查询订单明细所需属性
		String signSrc = "";
		String key = "1234567890";
		String merchantId = props.getString("Allinpay.MerId");
		String version = "v1.5";
		String signType = "1";
		String orderNo = ordId;
		String orderDatetime = Allinpay.formatAllinpayTime(order.getPayTime());
		String queryDatetime = Allinpay.formatAllinpayTime(new Date());

		// 处理签名
		StringBuffer bufSignSrc = new StringBuffer();
		StringUtil.appendSignPara(bufSignSrc, "merchantId", merchantId);
		StringUtil.appendSignPara(bufSignSrc, "version", version);
		StringUtil.appendSignPara(bufSignSrc, "signType", signType);
		StringUtil.appendSignPara(bufSignSrc, "orderNo", orderNo);
		StringUtil.appendSignPara(bufSignSrc, "orderDatetime", orderDatetime);
		StringUtil.appendSignPara(bufSignSrc, "queryDatetime", queryDatetime);
		StringUtil.appendLastSignPara(bufSignSrc, "key", key);
		signSrc = bufSignSrc.toString();
		String signMsg = SecurityUtil.MD5Encode(bufSignSrc.toString());

		try {
			// 提交查询请求
			boolean isSuccess = false;
			String resultMsg = "";
			Map<String, String> result = new HashMap<String, String>();

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("merchantId", merchantId));
			qparams.add(new BasicNameValuePair("version", version));
			qparams.add(new BasicNameValuePair("signType", signType));
			qparams.add(new BasicNameValuePair("orderNo", orderNo));
			qparams.add(new BasicNameValuePair("orderDatetime", orderDatetime));
			qparams.add(new BasicNameValuePair("queryDatetime", queryDatetime));
			qparams.add(new BasicNameValuePair("signMsg", signMsg));

			String returnValue = clientService.getResponeResult(
					"ceshi.allinpay.com" + "/" + "gateway/index.do", qparams);
			
			result = Allinpay.registerMap(returnValue); //将内容解析到map

			// 查询结果会以Server方式通知商户(同支付返回)；
			// 若无法取得Server通知结果，可以通过解析查询返回结果，更新订单状态(参考如下).
			if (null != result.get("ERRORCODE")) {
				// 未查询到订单
				System.out.println("ERRORCODE=" + result.get("ERRORCODE"));
				System.out.println("ERRORMSG=" + result.get("ERRORMSG"));
				resultMsg = result.get("ERRORMSG");

			} else {
				// 查询到订单
				String payResult = result.get("payResult");
				if (payResult.equals("1")) {
					System.out.println("订单付款成功！");

					// 支付成功，验证签名
					PaymentResult paymentResult = new PaymentResult();
					paymentResult.setMerchantId(result.get("merchantId"));
					paymentResult.setVersion(result.get("version"));
					paymentResult.setLanguage(result.get("language"));
					paymentResult.setSignType(result.get("signType"));
					paymentResult.setPayType(result.get("payType"));
					paymentResult.setIssuerId(result.get("issuerId"));
					paymentResult.setPaymentOrderId(result.get("paymentOrderId"));
					paymentResult.setOrderNo(result.get("orderNo"));
					paymentResult.setOrderDatetime(result.get("orderDatetime"));
					paymentResult.setOrderAmount(result.get("orderAmount"));
					paymentResult.setPayAmount(result.get("payAmount"));
					paymentResult.setPayDatetime(result.get("payDatetime"));
					paymentResult.setExt1(result.get("ext1"));
					paymentResult.setExt2(result.get("ext2"));
					paymentResult.setPayResult(result.get("payResult"));
					paymentResult.setErrorCode(result.get("errorCode"));
					paymentResult.setReturnDatetime(result.get("returnDatetime"));
					paymentResult.setKey(key);
					paymentResult.setSignMsg(result.get("signMsg"));
					URL keyFile = getClass().getResource("/allinpay/TLCert-test.cer");
					if (keyFile != null) {
						paymentResult.setCertPath(keyFile.getPath());
					}
					boolean verifyResult = paymentResult.verify();

					if (verifyResult) {
						System.out.println("验签成功！商户更新订单状态。");
						resultMsg = "订单支付成功，验签成功！商户更新订单状态。";
						isSuccess = true;
						allinpay = (AllinpayProperties)Function.RegisterBean(AllinpayProperties.class, result);
						System.out.println("1="+result);
						System.out.println("2="+allinpay);
					} else {
						System.out.println("验签失败！");
						resultMsg = "订单支付成功，验签失败！";
					}

				} else {
					System.out.println("订单尚未付款！");
					resultMsg = "订单尚未付款！";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allinpay;
	}

	@Override
	public AllinpayProperties refundOrder(String oldOrdId) {
		AllinpayProperties allinpay = new AllinpayProperties();
		
		Order order = payOrderService.getOrderById(oldOrdId); // 原订单信息
		
		String ordId = payOrderService.getOrderIdNumber(); //新生成订单号
		
		//页面编码要与参数inputCharset一致，否则服务器收到参数值中的汉字为乱码而导致验证签名失败。
		String version = "v1.3";
		String signType = "0";
		String merchantId = props.getString("Allinpay.MerId");
		String orderNo = oldOrdId;
		String refundAmount = Long.toString(Function.yuan2Cents(order.getMoney()));
		String orderDatetime = Allinpay.formatAllinpayTime(order.getPayTime());
		String key = "1234567890";
		
		
		//构造订单请求对象，生成signMsg。
		com.allinpay.ets.client.RequestOrder requestOrder = new com.allinpay.ets.client.RequestOrder();
		requestOrder.setVersion(version);
		requestOrder.setSignType(Integer.parseInt(signType));
		requestOrder.setMerchantId(merchantId);
		requestOrder.setOrderNo(orderNo);
		requestOrder.setRefundAmount(Long.parseLong(refundAmount));
		requestOrder.setOrderDatetime(orderDatetime);
		requestOrder.setKey(key); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。
		String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
		String signMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
		
		
		
		
		Map<String, String> result = new HashMap<String, String>();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("merchantId", merchantId));
		qparams.add(new BasicNameValuePair("version", version));
		qparams.add(new BasicNameValuePair("signType", signType));
		qparams.add(new BasicNameValuePair("orderNo", orderNo));
		qparams.add(new BasicNameValuePair("orderDatetime", orderDatetime));
		qparams.add(new BasicNameValuePair("refundAmount", refundAmount));
		qparams.add(new BasicNameValuePair("signMsg", signMsg));

		String returnValue = clientService.getResponeResult(
				"ceshi.allinpay.com" + "/" + "gateway/index.do", qparams);
		
		result = Allinpay.registerMap(returnValue); //将内容解析到map
		
		
		//验签是商户为了验证接收到的报文数据确实是支付网关发送的。
		//构造订单结果对象，验证签名。
		com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult();
		if ("".equals(result.get("ERRORCODE"))
				|| null == result.get("ERRORCODE")) {
			// 如果errorCode为空，说明返回正确退款报文信息，接下来对报文进行解析验签
			paymentResult.setMerchantId(result.get("merchantId").toString());
			paymentResult.setVersion(result.get("version").toString());
			paymentResult.setSignType(result.get("signType").toString());
			paymentResult.setOrderNo(result.get("orderNo").toString());
			paymentResult.setOrderDatetime(result.get("orderDatetime").toString());
			paymentResult.setOrderAmount(result.get("orderAmount").toString());
			paymentResult.setErrorCode(null == result.get("errorCode") ? "" : result.get("errorCode").toString());
			paymentResult.setRefundAmount(result.get("refundAmount").toString());
			paymentResult.setRefundDatetime(result.get("refundDatetime").toString());
			paymentResult.setRefundResult(result.get("refundResult").toString());
			paymentResult.setReturnDatetime(result.get("returnDatetime").toString());
			// signMsg为服务器端返回的签名值。
			paymentResult.setSignMsg(result.get("signMsg").toString());
			paymentResult.setKey(key);
			// 验证签名：返回true代表验签成功；否则验签失败。
			boolean verifyResult = paymentResult.verify();
			
			if(verifyResult){
				
			} else {
				
			}
			String resultMsg = "";
			if (verifyResult) {
				System.out.println("验签成功！商户更新订单状态。");
				resultMsg = "订单支付成功，验签成功！商户更新订单状态。";
				allinpay = (AllinpayProperties)Function.RegisterBean(AllinpayProperties.class, result);
			} else {
				System.out.println("验签失败！");
				resultMsg = "订单支付成功，验签失败！";
			}
		}
		
		return allinpay;
	}

	@Override
	public String batchQuery(String beginDateTime, String endDateTime,
			String pageNo) {
		
		String merchantId = props.getString("Allinpay.MerId");
//		beginDateTime = beginDateTime;
//		endDateTime = endDateTime;
//		pageNo = pageNo;
		String signType = "1";
		String version = "v1.6";
		String key = "1234567890";

		String signSrc="version="+version+"&merchantId="+merchantId+"&beginDateTime="+beginDateTime +"&endDateTime="+ endDateTime+"&pageNo="+pageNo +"&signType="+signType +"&key="+key;
		String signMsg=SecurityUtil.MD5Encode(signSrc);
		
		String resultMsg = "";
		String viewMsg="";
		try{
			
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("merchantId", merchantId));
			qparams.add(new BasicNameValuePair("version", version));
			qparams.add(new BasicNameValuePair("signType", signType));
			qparams.add(new BasicNameValuePair("pageNo",pageNo));
			qparams.add(new BasicNameValuePair("beginDateTime",beginDateTime));
			qparams.add(new BasicNameValuePair("endDateTime",endDateTime));
			qparams.add(new BasicNameValuePair("signMsg", signMsg));

			resultMsg = clientService.getResponeResult(
					"ceshi.allinpay.com" + "/" + "mchtoq/index.do", qparams);
			
			
			System.out.println("批量查询响应的原始报文："+resultMsg);
			String fileAsString = ""; // 签名信息前的对账文件内容
			String fileSignMsg = ""; // 文件签名信息
			boolean isVerified = false; // 验证签名结果			
			String ENCODING = "UTF-8";			
			byte[] data = Base64.decode(resultMsg);
			String tempStr = new String(data, ENCODING);
			System.out.println(tempStr);
			if(tempStr.indexOf("ERRORCODE=")<0){
				BufferedReader fileReader = new BufferedReader(new StringReader(tempStr));
				// 读取交易结果
				String lines;
				StringBuffer fileBuf = new StringBuffer(); // 签名信息前的字符串				String lines;
				while ((lines = fileReader.readLine()) != null) {
					if (lines.length() > 0) {
						// 按行读，每行都有换行符
						fileBuf.append(lines + "\r\n");
					} else {
						// 文件中读到空行，则读取下一行为签名信息
						fileSignMsg = fileReader.readLine();
					}
				}
				fileReader.close();
				fileAsString = fileBuf.toString();
				System.out.println("File: \n" + fileAsString);
				System.out.println("Sign: \n" + fileSignMsg);
				// 验证签名：先对文件内容计算MD5摘要，再将MD5摘要作为明文进行验证签名
				String fileMd5 = SecurityUtil.MD5Encode(fileAsString);
				String certPath="";
				
				URL keyFile = getClass().getResource("/allinpay/TLCert-test.cer");
				if (keyFile != null) {
					certPath = keyFile.getPath();
				}
				
				isVerified = SecurityUtil.verifyByRSA(certPath, fileMd5.getBytes(), Base64.decode(fileSignMsg));
				if (isVerified) {
					// 验证签名通过，解析交易明细，开始对账
					System.out.println("验证签名通过");
					viewMsg=fileAsString+fileSignMsg;
				} else {
					// 验证签名不通过，丢弃对账文件
					System.out.println("验证签名不通过");
	
				}
			}else{
				viewMsg=tempStr;
			}
		}catch(Exception e){
		 	e.printStackTrace();
		}
		
		
		return viewMsg;
	}
	
}
