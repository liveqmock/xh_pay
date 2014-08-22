package com.xinhuanet.pay.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chinapnr.SecureLink;

import com.xinhuanet.pay.gateway.ChinaPnr;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.ChinapnrProperties;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.ChinaPnrService;
import com.xinhuanet.pay.service.HttpClient4Service;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;

@Component
public class ChinaPnrServiceImpl extends ThridPartyServiceImpl<ChinapnrProperties> implements ChinaPnrService {
    private static final Logger logger = LoggerFactory.getLogger(ChinaPnrServiceImpl.class);
	/**
	 * 订单服务
	 */
	private @Autowired
	PayOrderService payOrderService;
	
	/**
	 * HttpClient服务
	 */
	protected @Autowired HttpClient4Service clientService;
	
	/**
	 * 读取配置文件服务
	 */
	protected @Autowired PropertiesConfiguration props;

	@Override
	public void thridOrdFailedNotify(Order order, AppOrder appOrder) {
		super.thridOrdFailedNotify(order, appOrder);
	}

	@Override
	public void thridOrdSucceedNotify(Order order, AppOrder appOrder) {
		super.thridOrdSucceedNotify(order, appOrder);
	}

	@Override
	public ChinapnrProperties queryStatus(String ordId) {
		// 设置汇付天下查询订单明细所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setVersion(props.getString("ChinaPnr.QueryStatus.version"));// 版本
		chinapnr.setCmdId(props.getString("ChinaPnr.QueryStatus.CmdId"));// 消息类型
		chinapnr.setMerId(props.getString("ChinaPnr.MerId"));// 商户号
		chinapnr.setOrdId(ordId);// 订单号
		try {
			// 签名
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId();

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			if (ret != 0) {//签名失败后返回
				logger.info("查询订单状态：签名错误 ret=" + ret + " MerData:["+ MerData +"]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setErrMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return new ChinapnrProperties();
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("Version", chinapnr.getVersion()));
			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
			qparams.add(new BasicNameValuePair("ChkValue", chinapnr.getChkValue()));
			
			String returnValue = clientService.getResponeResult(props.getString("ChinaPnr.uri")+ "/" +props.getString("ChinaPnr.path"),qparams);
			
			if(!StringUtils.isBlank(returnValue)){//解析返回结果
				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
				chinapnr = (ChinapnrProperties) Function.RegisterBean(
						ChinapnrProperties.class, map);
			} else{//请求异常后返回
				chinapnr = new ChinapnrProperties();
				chinapnr.setProcStat(ChinaPnr.PROCSTAT_ERROR);
				chinapnr.setErrMsg(ChinaPnr.getProcStatName(chinapnr.getProcStat()));
				return chinapnr;
			}
		} catch (Exception e) {
			logger.error("查询订单状态：签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-101);//私钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		
		
		// 验签
		try {
			String MerKeyFile = getClass().getResource(props.getString("ChinaPnr.PgPubk")).getPath();
			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
					+ chinapnr.getProcStat() + chinapnr.getErrMsg().trim(); // 参数顺序不能错

			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr.getChkValue());

			if (ret != 0) { //验证签名失败后返回
				logger.info("签名验证失败[" + MerData + "]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return new ChinapnrProperties();
			} else {
				if (chinapnr.getRespCode().equals("000000")) {
					logger.info("查询订单"+ordId+"状态："+chinapnr.getErrMsg()+"，交易成功");
				} else {
					// 交易失败
					// 根据订单号 进行相应业务操作
					// 在些插入代码
					logger.info("查询订单"+ordId+"状态：交易失败");
				}
			}
		} catch (Exception e) {
			logger.error("查询订单状态：验证签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-112);//公钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		return chinapnr;
	}

	@Override
	public ChinapnrProperties queryOrder(String ordId) {

		// 设置汇付天下查询订单明细所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setVersion(props.getString("ChinaPnr.QueryOrder.version"));// 版本
		chinapnr.setCmdId(props.getString("ChinaPnr.QueryOrder.CmdId"));// 消息类型
		chinapnr.setMerId(props.getString("ChinaPnr.MerId"));// 商户号
		chinapnr.setOrdId(ordId);// 订单号
		try {
			// 签名
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId() + "";

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			if (ret != 0) {
				logger.info("查询订单明细：签名错误 ret=" + ret + " MerData:["+ MerData +"]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setErrMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("Version", chinapnr.getVersion()));
			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
			qparams.add(new BasicNameValuePair("UsrId", ""));
			qparams.add(new BasicNameValuePair("ChkValue", chinapnr.getChkValue()));
			
			String returnValue = clientService.getResponeResult(props.getString("ChinaPnr.uri")+ "/" +props.getString("ChinaPnr.path"),qparams);
			
			if (!StringUtils.isBlank(returnValue)) {// 解析返回结果
				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
				chinapnr = (ChinapnrProperties) Function.RegisterBean(
						ChinapnrProperties.class, map);
			} else {// 请求异常后返回
				chinapnr = new ChinapnrProperties();
				chinapnr.setProcStat(ChinaPnr.PROCSTAT_ERROR);
				chinapnr.setErrMsg(ChinaPnr.getProcStatName(chinapnr
						.getProcStat()));
				return chinapnr;
			}
			
		} catch (Exception e) {
			logger.error("查询订单明细：签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-101);//私钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		// 验签
		try {
			// 验签
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.PgPubk")).getPath();
			//该段调试错误使用
//			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
//					+ chinapnr.getTrxId() + chinapnr.getOrdAmt()
//					+ chinapnr.getCurCode() + chinapnr.getPid()
//					+ chinapnr.getOrdId() + chinapnr.getDivDetails()
//					+ chinapnr.getMerPriv() + chinapnr.getRefCnt()
//					+ chinapnr.getRefAmt() + chinapnr.getErrMsg().trim(); // 参数顺序不能错
			
			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
			+ StringUtils.stripToEmpty(chinapnr.getTrxId()) + StringUtils.stripToEmpty(chinapnr.getOrdAmt())
			+ StringUtils.stripToEmpty(chinapnr.getCurCode()) + StringUtils.stripToEmpty(chinapnr.getPid())
			+ StringUtils.stripToEmpty(chinapnr.getOrdId()) + StringUtils.stripToEmpty(chinapnr.getDivDetails())
			+ StringUtils.stripToEmpty(chinapnr.getMerPriv()) + StringUtils.stripToEmpty(chinapnr.getRefCnt())
			+ StringUtils.stripToEmpty(chinapnr.getRefAmt()) + StringUtils.stripToEmpty(chinapnr.getErrMsg()); // 参数顺序不能错

			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr
					.getChkValue());

			if (ret != 0) {
				logger.info("查询订单明细：签名验证错误 ret="+ret+" MerData[" + MerData + "]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			} else {
				if (chinapnr.getRespCode().equals("000000")) {
					logger.info("查询订单"+ordId+"明细："+chinapnr.getErrMsg()+"，交易成功");
				} else {
					// 交易失败
					// 根据订单号 进行相应业务操作
					// 在些插入代码
					logger.info("查询订单"+ordId+"明细：交易失败");
				}
			}
		} catch (Exception e) {
			logger.error("查询订单明细：验证签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-112);//公钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		return chinapnr;
	}

	@Override
	public ChinapnrProperties paymentConfirm(String ordId) {

		// 设置汇付天下订单结算所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setVersion(props.getString("ChinaPnr.PaymentConfirm.version"));// 版本
		chinapnr.setCmdId(props.getString("ChinaPnr.PaymentConfirm.CmdId"));// 消息类型
		chinapnr.setMerId(props.getString("ChinaPnr.MerId"));// 商户号
		chinapnr.setOrdId(ordId);// 订单号
		try {
			// 签名
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId() + "";

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			if (ret != 0) {
				logger.info("订单结算：签名错误 ret=" + ret + " MerData:["+ MerData +"]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setErrMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("Version", chinapnr.getVersion()));
			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
			qparams.add(new BasicNameValuePair("ChkValue", chinapnr.getChkValue()));
			
			String returnValue = clientService.getResponeResult(props.getString("ChinaPnr.uri")+ "/" +props.getString("ChinaPnr.path"),qparams);
			
			if (!StringUtils.isBlank(returnValue)) {// 解析返回结果
				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
				chinapnr = (ChinapnrProperties) Function.RegisterBean(
						ChinapnrProperties.class, map);
			} else {// 请求异常后返回
				chinapnr = new ChinapnrProperties();
				chinapnr.setProcStat(ChinaPnr.PROCSTAT_ERROR);
				chinapnr.setErrMsg(ChinaPnr.getProcStatName(chinapnr
						.getProcStat()));
				return chinapnr;
			}
			
		} catch (Exception e) {
			logger.error("订单结算：签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-101);//私钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		// 验签
		try {
			// 验签
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.PgPubk")).getPath();
			
			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
			+ StringUtils.stripToEmpty(chinapnr.getErrMsg()); // 参数顺序不能错

			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr
					.getChkValue());

			if (ret != 0) {
				logger.info("订单结算：签名验证错误 ret="+ret+" MerData[" + MerData + "]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			} else {
				if (chinapnr.getRespCode().equals("000000")) {
					logger.info("订单结算"+ordId+"："+chinapnr.getRespCode()+" "+chinapnr.getErrMsg()+"，交易成功");
				} else {
					// 交易失败
					// 根据订单号 进行相应业务操作
					// 在些插入代码
					logger.info("订单结算"+ordId+"："+chinapnr.getRespCode()+" "+chinapnr.getErrMsg()+"，交易失败");
				}
			}
		} catch (Exception e) {
			logger.error("订单结算：验证签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-112);//公钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		return chinapnr;
	}

	@Override
	public ChinapnrProperties refundOrder(String oldOrdId) {
		
		Order order = payOrderService.getOrderById(oldOrdId); // 原订单信息
		
		String ordId = payOrderService.getOrderIdNumber(); //新生成订单号
		
		logger.info("原订单号:"+oldOrdId + "; 退款订单号:"+ordId + " 金额:" + order.getMoney());
		
		// 设置汇付天下查询订单明细所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setVersion(props.getString("ChinaPnr.Refund.version"));// 版本
		chinapnr.setCmdId(props.getString("ChinaPnr.Refund.CmdId"));// 消息类型
		chinapnr.setMerId(props.getString("ChinaPnr.MerId"));// 商户号
		chinapnr.setRefAmt(Function.formtDecimalPoint2(order.getMoney()));//退款金额
		chinapnr.setOrdId(ordId);// 订单号
		chinapnr.setOldOrdId(oldOrdId);//原订单号
		chinapnr.setBgRetUrl("");//后台返回交易退款信息的url，后台返回时包含OrdId和OldOrdId
		
		try {
			// 签名
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getRefAmt() 
					+ chinapnr.getOrdId() + chinapnr.getOldOrdId();

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			if (ret != 0) {
				logger.info("订单退款：签名错误 ret=" + ret + " MerData:["+ MerData +"]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setErrMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("Version", chinapnr.getVersion()));
			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
			qparams.add(new BasicNameValuePair("RefAmt", chinapnr.getRefAmt()));
			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
			qparams.add(new BasicNameValuePair("OldOrdId", chinapnr.getOldOrdId()));
			qparams.add(new BasicNameValuePair("BgRetUrl", ""));
			qparams.add(new BasicNameValuePair("ChkValue", chinapnr.getChkValue()));
			
			String returnValue = clientService.getResponeResult(props.getString("ChinaPnr.uri")+ "/" +props.getString("ChinaPnr.path"),qparams);
			
			if (!StringUtils.isBlank(returnValue)) {// 解析返回结果
				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
				chinapnr = (ChinapnrProperties) Function.RegisterBean(
						ChinapnrProperties.class, map);
			} else {// 请求异常后返回
				chinapnr = new ChinapnrProperties();
				chinapnr.setProcStat(ChinaPnr.PROCSTAT_ERROR);
				chinapnr.setErrMsg(ChinaPnr.getProcStatName(chinapnr.getProcStat()));
				return chinapnr;
			}
			
		} catch (Exception e) {
			logger.error("订单退款：签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-101);//私钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		// 验签
		try {
			// 验签
			String MerKeyFile = getClass().getResource(props.getString("ChinaPnr.PgPubk")).getPath();
			
			String MerData = chinapnr.getCmdId() + StringUtils.stripToEmpty(chinapnr.getOrdId())
			+ StringUtils.stripToEmpty(chinapnr.getOldOrdId()) + chinapnr.getRespCode()
			+ StringUtils.stripToEmpty(chinapnr.getErrMsg()); // 参数顺序不能错

			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr
					.getChkValue());

			if (ret != 0) {
				logger.info("订单退款：签名验证错误 ret="+ret+" MerData[" + MerData + "]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return chinapnr;
			} else {
				if (chinapnr.getRespCode().equals("000000")) {
					logger.info("订单退款"+ordId+"："+chinapnr.getErrMsg()+"，交易成功");
				} else {
					// 交易失败
					// 根据订单号 进行相应业务操作
					// 在些插入代码
					logger.info("订单退款"+ordId+"：交易失败");
				}
			}
		} catch (Exception e) {
			logger.error("订单退款：验证签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-112);//公钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		return chinapnr;
	}

	@Override
	public ChinapnrProperties queryRefundStatus(String ordId) {
		// 设置汇付天下查询订单退款状态所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setVersion(props.getString("ChinaPnr.QueryRefundStatus.version"));// 版本
		chinapnr.setCmdId(props.getString("ChinaPnr.QueryRefundStatus.CmdId"));// 消息类型
		chinapnr.setMerId(props.getString("ChinaPnr.MerId"));// 商户号
		chinapnr.setOrdId(ordId);// 订单号
		try {
			// 签名
			String MerKeyFile = getClass().getResource(
					props.getString("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId();

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			if (ret != 0) {//签名失败后返回
				logger.info("查询订单退款状态：签名错误 ret=" + ret + " MerData:["+ MerData +"]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setErrMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return new ChinapnrProperties();
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("Version", chinapnr.getVersion()));
			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
			qparams.add(new BasicNameValuePair("ChkValue", chinapnr.getChkValue()));
			
			String returnValue = clientService.getResponeResult(props.getString("ChinaPnr.uri")+ "/" +props.getString("ChinaPnr.path"),qparams);
			
			if(!StringUtils.isBlank(returnValue)){//解析返回结果
				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
				chinapnr = (ChinapnrProperties) Function.RegisterBean(
						ChinapnrProperties.class, map);
			} else{//请求异常后返回
				chinapnr = new ChinapnrProperties();
				chinapnr.setProcStat(ChinaPnr.PROCSTAT_ERROR);
				chinapnr.setErrMsg(ChinaPnr.getProcStatName(chinapnr.getProcStat()));
				return chinapnr;
			}
		} catch (Exception e) {
			logger.error("查询订单退款状态：签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-101);//私钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		
		
		// 验签
		try {
			String MerKeyFile = getClass().getResource(props.getString("ChinaPnr.PgPubk")).getPath();
			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
					+ chinapnr.getProcStat() + chinapnr.getErrMsg().trim(); // 参数顺序不能错

			SecureLink sl = new SecureLink();
			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr.getChkValue());

			if (ret != 0) { //验证签名失败后返回
				logger.info("查询订单退款签名验证失败[" + MerData + "]");
				chinapnr = new ChinapnrProperties();
				chinapnr.setSignRet(ret);
				chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
				return new ChinapnrProperties();
			} else {
				if (chinapnr.getRespCode().equals("000000")) {
					logger.info("查询订单退款"+ordId+"状态："+chinapnr.getErrMsg()+"，交易成功");
				} else {
					// 交易失败
					// 根据订单号 进行相应业务操作
					// 在些插入代码
					logger.info("查询订单退款"+ordId+"状态：交易失败");
				}
			}
		} catch (Exception e) {
			logger.error("查询订单退款状态：验证签名验证异常",e);
			chinapnr = new ChinapnrProperties();
			chinapnr.setSignRet(-112);//公钥文件异常
			chinapnr.setSignMsg(ChinaPnr.getRetName(chinapnr.getSignRet()));
			return chinapnr;
		}
		return chinapnr;
	}
}
