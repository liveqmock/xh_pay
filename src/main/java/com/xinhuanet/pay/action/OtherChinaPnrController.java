package com.xinhuanet.pay.action;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import chinapnr.SecureLink;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.gateway.ChinaPnr;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.ChinapnrProperties;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.PayIncomeService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.DESUtil;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;
import com.xinhuanet.pay.util.UUID;

/**
 * Handles requests for the application welcome page.
 */
@Controller
public class OtherChinaPnrController extends BaseController {
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
	 * 汇总服务
	 */
	private @Autowired PayIncomeService piService;

	

//	/**
//	 * 发送订单到ChinaPnr的钱管家
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/send.do")
//	public ModelAndView sendChinaPnrOrder(HttpServletRequest request,HttpServletResponse response) {
//		ModelAndView mav = new ModelAndView();
//		HttpSession session = request.getSession();
//
//		double money = Double.parseDouble(request.getParameter("money"));
//		String orderId = request.getParameter("orderId");
//		String gateId = request.getParameter("gateId");
//
//		/**
//		 * 验证订单是否重复提交，重复提交跳转到提示页面
//		 */
//		String stoken = (String) session.getAttribute("_submitOrderId");
//		if (stoken == null || !stoken.equals(orderId)) {
//			session.setAttribute("_submitOrderId", orderId);
//		} else {
//			mav.setViewName("pay/repeatorder");
//			return mav;
//		}
//		/**
//		 * 商户私有数据项加密并赋值到汇付天下
//		 */
//		Account account = this.getAccount(request,response, false);
//		String merPriv = account.getUid() + "," + account.getLoginName() + ","
//				+ account.getUserName();
//		try {
//			merPriv = DESUtil.encrypt(merPriv);
//		} catch (Exception e) {
//			logger.info("数据加密错误");
//			e.printStackTrace();
//		}
//
//		PayOrder order = new PayOrder();
//		order.setMoney(money);
//		order.setOrderId(orderId);
//		order.setGateId(gateId);
//		// 设置汇付天下在线支付所需属性
//		ChinapnrProperties chinapnr = new ChinapnrProperties();
//		chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
//		chinapnr.setCmdId(this.getProperty("ChinaPnr.Buy.CmdId"));// 消息类型
//		chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
//		chinapnr.setOrdId(order.getOrderId());// 订单号
//		chinapnr.setOrdAmt(Function.formtDecimalPoint2(order.getMoney()));// 订单金额
//		chinapnr.setCurCode(this.getProperty("ChinaPnr.CurCode"));// 币种
//		chinapnr.setPid("");// 商品编号
//		chinapnr.setRetUrl(this.getProperty("ChinaPnr.Buy.RetUrl"));// 页面返回地址
//		chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.Buy.BgRetUrl"));// 后台返回地址
//		chinapnr.setMerPriv(merPriv);// 商户私有数据项
//		chinapnr.setGateId(order.getGateId());// 网关号
//		chinapnr.setUsrMp("");// 用户手机号
//		chinapnr.setDivDetails("");// 分账明细
//		chinapnr.setPayUsrId("");// 付款人用户号
//		// 签名
//		String MerKeyFile = getClass().getResource(
//				this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//																// 请将MerPrK510010.key改为你的私钥文件名称
//		String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
//				+ chinapnr.getMerId() + chinapnr.getOrdId()
//				+ chinapnr.getOrdAmt() + chinapnr.getCurCode()
//				+ chinapnr.getPid() + chinapnr.getRetUrl()
//				+ chinapnr.getMerPriv() + chinapnr.getGateId()
//				+ chinapnr.getUsrMp() + chinapnr.getDivDetails()
//				+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();
//
//		SecureLink sl = new SecureLink();
//		int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);
//
//		ChinaPnr pnr = new ChinaPnr();
//		pnr.setGateId(order.getGateId());
//		if (ret != 0) {
//			logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
//		}
//		String chkValue = sl.getChkValue();
//		chinapnr.setChkValue(chkValue);// 数字签名
//
//		mav.addObject("bank", pnr.getGateBank());
//		mav.addObject("order", order);
//		mav.addObject("chinapnr", chinapnr);
//		mav.setViewName("pay/chinapnr/sendorder");
//		return mav;
//	}


//	/**
//	 * 汇付天下交易返回页面
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/pay/chinapnr/return.do")
//	public ModelAndView chinaPnrReturn(HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView();
//		String CmdId = request.getParameter("CmdId"); // 消息类型
//		String MerId = request.getParameter("MerId"); // 商户号
//		String RespCode = request.getParameter("RespCode"); // 应答返回码
//		String TrxId = request.getParameter("TrxId"); // 钱管家交易唯一标识
//		String OrdAmt = request.getParameter("OrdAmt"); // 金额
//		String CurCode = request.getParameter("CurCode"); // 币种
//		String Pid = request.getParameter("Pid"); // 商品编号
//		String OrdId = request.getParameter("OrdId"); // 订单号
//		String MerPriv = request.getParameter("MerPriv"); // 商户私有域
//		String RetType = request.getParameter("RetType"); // 返回类型
//		String DivDetails = request.getParameter("DivDetails"); // 分账明细
//		String GateId = request.getParameter("GateId"); // 银行ID
//		String ChkValue = request.getParameter("ChkValue"); // 签名信息
//
//		// 设置汇付天下在线支付所需属性
//		ChinapnrProperties chinapnr = new ChinapnrProperties();
//		chinapnr.setCmdId(CmdId);// 消息类型
//		chinapnr.setMerId(MerId);// 商户号
//		chinapnr.setRespCode(RespCode);// /应答返回码
//		chinapnr.setTrxId(TrxId);// 钱管家交易唯一标识
//		chinapnr.setOrdAmt(OrdAmt);// 订单金额
//		chinapnr.setCurCode(CurCode);// 币种
//		chinapnr.setPid(Pid);// 商品编号
//		chinapnr.setOrdId(OrdId);// 订单号
//		chinapnr.setMerPriv(MerPriv);// 商户私有数据项
//		chinapnr.setRetType(RetType);// 返回类型
//		chinapnr.setDivDetails(DivDetails);// 分账明细
//		chinapnr.setGateId(GateId);// 网关号
//		chinapnr.setChkValue(ChkValue);// 数字签名
//		System.out.println(chinapnr);
//
//		String message = "";// 提示信息
//		boolean bStatus = true;// 是否成功
//		Account account = new Account();// 账户信息
//		try {
//			String[] decryptMerPriv = DESUtil.decrypt(MerPriv).split(",");
//			account.setUid(decryptMerPriv[0]);
//			account.setLoginName(decryptMerPriv[1]);
//			account.setUserName(decryptMerPriv[2]);
//			System.out.println("2345678902345678904567890");
//			String uid = account.getUid();
//			// 验签
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.PgPubk")).getPath();
//			String MerData = CmdId + MerId + RespCode + TrxId + OrdAmt
//					+ CurCode + Pid + OrdId + MerPriv + RetType + DivDetails
//					+ GateId; // 参数顺序不能错
//			SecureLink sl = new SecureLink();
//			int ret = sl.VeriSignMsg(MerKeyFile, MerData, ChkValue);
//			System.out.println("-------------" + ret);
//			if (ret != 0) {
//				bStatus = false;
//				message = "交易失败";
//				logger.info("签名验证失败[" + MerData + "]");
//			} else {
//				// TODO 因为系统没有在公网上部署，所以，汇付后台请求不到，以下数据库操作业务应在后台通知处理
//				PayOrder order = new PayOrder();
//				// 查询当前订单本地状态
//				order = payOrderService.getOrder(OrdId);// 查询当前订单的信息
//				if (RespCode.equals("000000")) {
//					System.out.println("if----------------");
//					// 交易成功
//					// 根据订单号 进行相应业务操作
//					message = "交易成功";
//					logger.info("交易成功");
//
//					// 验证是否需要修改数据库，需要更改则更新订单状态和用户余额
//					if (ChinaPnr.isOperate(order.getPayStatus(), true)) {
//						// 更新订单
//						order.setUid(uid);
//						order.setOrderId(OrdId);
//						order.setPayStatus(OrderStatus.ORDER_STATUS_SUCCEES);
//						payOrderService.updateOrder(order);
//						System.out
//								.println("6666666666666666666666666666666666666");
//						// 更新用户余额
//						accountService.addAccountCash(uid, Double
//								.parseDouble(OrdAmt), OrdId);
//						System.out
//								.println("77777777777777777777777777777777777777");
//					}
//				} else {
//					System.out.println("else----------------");
//					// 交易失败
//					// 根据订单号 进行相应业务操作
//					// 验证是否需要修改数据库,需要更改则更新订单状态
//					if (ChinaPnr.isOperate(order.getPayStatus(), false)) {
//						// 更新订单
//						order.setUid(uid);
//						order.setOrderId(OrdId);
//						order.setPayStatus(OrderStatus.ORDER_STATUS_FAILED);
//						payOrderService.updateOrder(order);
//					}
//					bStatus = false;
//					message = "交易失败";
//					logger.info("交易失败");
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			message = "签名验证异常";
//			logger.info("签名验证异常");
//			bStatus = false;
//		}
//
//		request.setAttribute("account", account);
//		request.setAttribute("chinapnr", chinapnr);
//		request.setAttribute("message", message);
//		request.setAttribute("bStatus", bStatus);
//		mav.setViewName("/pay/chinapnr/chinapnrreturn");
//		return mav;
//	}

//	/**
//	 * 汇付天下后台通知页面
//	 * 
//	 * @return
//	 */
//	@RequestMapping(value = "/pay/chinapnr/notify.do")
//	public String chinaPnrNotify(HttpServletRequest request) {
//		String CmdId = request.getParameter("CmdId"); // 消息类型
//		String MerId = request.getParameter("MerId"); // 商户号
//		String RespCode = request.getParameter("RespCode"); // 应答返回码
//		String TrxId = request.getParameter("TrxId"); // 钱管家交易唯一标识
//		String OrdAmt = request.getParameter("OrdAmt"); // 金额
//		String CurCode = request.getParameter("CurCode"); // 币种
//		String Pid = request.getParameter("Pid"); // 商品编号
//		String OrdId = request.getParameter("OrdId"); // 订单号
//		String MerPriv = request.getParameter("MerPriv"); // 商户私有域
//		String RetType = request.getParameter("RetType"); // 返回类型
//		String DivDetails = request.getParameter("DivDetails"); // 分账明细
//		String GateId = request.getParameter("GateId"); // 银行ID
//		String ChkValue = request.getParameter("ChkValue"); // 签名信息
//
//		// 设置汇付天下在线支付所需属性
//		ChinapnrProperties chinapnr = new ChinapnrProperties();
//		chinapnr.setCmdId(CmdId);// 消息类型
//		chinapnr.setMerId(MerId);// 商户号
//		chinapnr.setRespCode(RespCode);// /应答返回码
//		chinapnr.setTrxId(TrxId);// 钱管家交易唯一标识
//		chinapnr.setOrdAmt(OrdAmt);// 订单金额
//		chinapnr.setCurCode(CurCode);// 币种
//		chinapnr.setPid(Pid);// 商品编号
//		chinapnr.setOrdId(OrdId);// 订单号
//		chinapnr.setMerPriv(MerPriv);// 商户私有数据项
//		chinapnr.setRetType(RetType);// 返回类型
//		chinapnr.setDivDetails(DivDetails);// 分账明细
//		chinapnr.setGateId(GateId);// 网关号
//		chinapnr.setChkValue(ChkValue);// 数字签名
//		try {
//			// 验签
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.PgPubk")).getPath();
//			String MerData = CmdId + MerId + RespCode + TrxId + OrdAmt
//					+ CurCode + Pid + OrdId + MerPriv + RetType + DivDetails
//					+ GateId; // 参数顺序不能错
//			SecureLink sl = new SecureLink();
//			int ret = sl.VeriSignMsg(MerKeyFile, MerData, ChkValue);
//
//			if (ret != 0) {
//				System.out.println("签名验证失败[" + MerData + "]");
//			} else {
//				if (RespCode.equals("000000")) {
//					// 交易成功
//					// 根据订单号 进行相应业务操作
//					// 在些插入代码
//					logger.info("后台通知：交易成功");
//				} else {
//					// 交易失败
//					// 根据订单号 进行相应业务操作
//					// 在些插入代码
//					logger.info("交易失败");
//				}
//			}
//		} catch (Exception e) {
//			logger.info("签名验证异常");
//		}
//
//		request.setAttribute("chinapnr", chinapnr);
//		request.setAttribute("recvOrdid", "RECV_ORD_ID_" + OrdId);
//
//		return "pay/chinapnr/chinapnrnotify";
//	}
	
	
	

//	/**
//	 * 汇付天下查询订单详细
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/queryorder.do")
//	public ModelAndView queryOrder(HttpServletRequest request,HttpServletResponse response) {
//		ModelAndView mav = new ModelAndView();
//
//		String ordId = request.getParameter("orderId");
//
//		this.getAccount(request,response, false);
//
//		// 设置汇付天下查询订单详细所需属性
//		ChinapnrProperties chinapnr = new ChinapnrProperties();
//		chinapnr.setVersion(this.getProperty("ChinaPnr.QueryOrder.version"));// 版本
//		chinapnr.setCmdId(this.getProperty("ChinaPnr.QueryOrder.CmdId"));// 消息类型
//		chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
//		chinapnr.setOrdId(ordId);// 订单号
//		// 创建HttpClient实例
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			// 签名
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//																	// 请将MerPrK510010.key改为你的私钥文件名称
//			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
//					+ chinapnr.getMerId() + chinapnr.getOrdId() + "";
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);
//
//			if (ret != 0) {
//				logger.info("查询订单详细：签名错误 ret=" + ret);
//			}
//			String chkValue = sl.getChkValue();
//			chinapnr.setChkValue(chkValue);// 数字签名
//
//			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
//			qparams
//					.add(new BasicNameValuePair("Version", chinapnr
//							.getVersion()));
//			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
//			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
//			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
//			qparams.add(new BasicNameValuePair("UsrId", ""));
//			qparams.add(new BasicNameValuePair("ChkValue", chinapnr
//					.getChkValue()));
//
//			URI uri = URIUtils.createURI("http", this
//					.getProperty("ChinaPnr.uri"), -1, this
//					.getProperty("ChinaPnr.path"), URLEncodedUtils.format(
//					qparams, "GBK"), null);
//
//			// 代理的设置
//			HttpHost proxy = new HttpHost(this
//					.getProperty("http.proxy.address"), this
//					.getPropertyInt("http.proxy.port"));
//			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
//					proxy);
//			// 设置超时时间
//			httpclient.getParams().setIntParameter(
//					CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//
//			HttpGet httpget = new HttpGet(uri);
//			// System.out.println(httpget.getURI());
//
//			HttpResponse res = httpclient.execute(httpget);
//			HttpEntity entity = res.getEntity();
//			if (entity != null) {
//				InputStream instream = entity.getContent();
//				int l;
//				byte[] tmp = new byte[2048];
//				while ((l = instream.read(tmp)) != -1) {
//
//				}
//
//				String returnValue = new String(tmp);
//
//				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
//				chinapnr = (ChinapnrProperties) Function.RegisterBean(
//						ChinapnrProperties.class, map);
//
//				// System.out.println(chinapnr.toString());
//				System.out.println(new String(tmp));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			// 关闭连接
//			httpclient.getConnectionManager().shutdown();
//		}
//		// 验签
//		try {
//			// 验签
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.PgPubk")).getPath();
//			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
//					+ chinapnr.getTrxId() + chinapnr.getOrdAmt()
//					+ chinapnr.getCurCode() + chinapnr.getPid()
//					+ chinapnr.getOrdId() + chinapnr.getDivDetails()
//					+ chinapnr.getMerPriv() + chinapnr.getRefCnt()
//					+ chinapnr.getRefAmt() + chinapnr.getErrMsg().trim(); // 参数顺序不能错
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr
//					.getChkValue());
//
//			if (ret != 0) {
//				logger.info("签名验证失败[" + MerData + "]");
//			} else {
//				if (chinapnr.getRespCode().equals("000000")) {
//					logger.info("交易成功");
//					mav.addObject("chinapnr", chinapnr);
//				} else {
//					// 交易失败
//					// 根据订单号 进行相应业务操作
//					// 在些插入代码
//					logger.info("交易失败");
//				}
//			}
//		} catch (Exception e) {
//			logger.info("签名验证异常");
//		}
//
//		mav.setViewName("pay/chinapnr/queryorder");
//		return mav;
//	}

//	/**
//	 * 汇付天下查询订单被支付状态
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/querystatus.do")
//	public ModelAndView queryStatus(HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView();
//
//		// TODO session 失效后，重登录
//
//		String ordId = request.getParameter("orderId");
//
//		// 设置汇付天下查询订单详细所需属性
//		ChinapnrProperties chinapnr = new ChinapnrProperties();
//		chinapnr.setVersion(this.getProperty("ChinaPnr.QueryStatus.version"));// 版本
//		chinapnr.setCmdId(this.getProperty("ChinaPnr.QueryStatus.CmdId"));// 消息类型
//		chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
//		chinapnr.setOrdId(ordId);// 订单号
//		// 创建HttpClient实例
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			// 签名
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//																	// 请将MerPrK510010.key改为你的私钥文件名称
//			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
//					+ chinapnr.getMerId() + chinapnr.getOrdId();
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);
//
//			if (ret != 0) {
//				System.out.println("签名错误 ret=" + ret);
//			}
//			String chkValue = sl.getChkValue();
//			chinapnr.setChkValue(chkValue);// 数字签名
//
//			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
//			qparams.add(new BasicNameValuePair("ChkValue", chinapnr
//					.getChkValue()));
//			qparams
//					.add(new BasicNameValuePair("Version", chinapnr
//							.getVersion()));
//			qparams.add(new BasicNameValuePair("CmdId", chinapnr.getCmdId()));
//			qparams.add(new BasicNameValuePair("MerId", chinapnr.getMerId()));
//			qparams.add(new BasicNameValuePair("OrdId", chinapnr.getOrdId()));
//
//			URI uri = URIUtils.createURI("http", this
//					.getProperty("ChinaPnr.uri"), -1, this
//					.getProperty("ChinaPnr.path"), URLEncodedUtils.format(
//					qparams, "GBK"), null);
//
//			// 代理的设置
//			HttpHost proxy = new HttpHost(this
//					.getProperty("http.proxy.address"), this
//					.getPropertyInt("http.proxy.port"));
//			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
//					proxy);
//
//			HttpGet httpget = new HttpGet(uri);
//			// System.out.println(httpget.getURI());
//
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				InputStream instream = entity.getContent();
//				int l;
//				byte[] tmp = new byte[2048];
//				while ((l = instream.read(tmp)) != -1) {
//
//				}
//
//				String returnValue = new String(tmp);
//
//				Map<String, String> map = ChinaPnr.wrapperMap(returnValue);
//				chinapnr = (ChinapnrProperties) Function.RegisterBean(
//						ChinapnrProperties.class, map);
//
//				// System.out.println(chinapnr.toString());
//				System.out.println(new String(tmp));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			// 关闭连接
//			httpclient.getConnectionManager().shutdown();
//		}
//		// 验签
//		try {
//			// 验签
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.PgPubk")).getPath();
//			String MerData = chinapnr.getCmdId() + chinapnr.getRespCode()
//					+ chinapnr.getProcStat() + chinapnr.getErrMsg().trim(); // 参数顺序不能错
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.VeriSignMsg(MerKeyFile, MerData, chinapnr
//					.getChkValue());
//
//			if (ret != 0) {
//				System.out.println("签名验证失败[" + MerData + "]");
//			} else {
//				if (chinapnr.getRespCode().equals("000000")) {
//					System.out.println("交易成功");
//
//					ChinaPnr pnr = new ChinaPnr();
//					pnr.setProcStat(chinapnr.getProcStat());
//					mav.addObject("pnr", pnr);
//					mav.addObject("chinapnr", chinapnr);
//				} else {
//					// 交易失败
//					// 根据订单号 进行相应业务操作
//					// 在些插入代码
//					System.out.println("交易失败");
//				}
//			}
//		} catch (Exception e) {
//			System.out.println("签名验证异常");
//		}
//
//		mav.setViewName("pay/chinapnr/querystatus");
//		return mav;
//	}

//	/**
//	 * 银行卡充值产生订单，回显订单号和金额
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/bank.do")
//	public ModelAndView order(HttpServletRequest request,HttpServletResponse response) {
//		ModelAndView mav = new ModelAndView();
//		try {
//			String money = request.getParameter("money");
//			String bank = request.getParameter("bank");
//			
//			String id = request.getParameter("id");
//			AppOrder appOrder = payOrderService.getAppOrderByid(id);
//
//			String uid = this.getUserId(request,response);
//			String loginName = this.getLoginName(request,response);
//
//			// 查询充值前金额
//			Account account = accountService.getAccount(uid);
//			// 初始化一条订单
//			PayOrder order = new PayOrder();
//			String uuid = UUID.create();
//			order.setId(uuid);
//			order.setUid(uid);
//			order.setLoginName(loginName);
//			order.setMoney(new Double(appOrder.getMoney()));
//			order.setBeforeMoney(account.getMoney());
//			order.setBank(bank);
////			order.setPayType(OrderStatus.ORDER_TYPE_CHINAPNR);
//			order.setIpAddress(HttpUtil.getIpAddr(request));
//			order.setOrderId(payOrderService.getOrderIdNumber());
//			payOrderService.addOrder(order);
//
////			mav.setView(new RedirectView(request.getContextPath()
////					+ "/order/chinapnr/show.do?id=" + uuid));
//			mav.setView(new RedirectView(request.getContextPath()
//					+ "/order/chinapnr/show.do?id=" + uuid));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mav;
//	}
	
	
	
	//
//		/**
//		 * 银行卡充值产生订单，回显订单号和金额
//		 * 
//		 * @param request
//		 * @return
//		 */
//		@RequestMapping(value = "/order/chinapnr/bank.do")
//		public ModelAndView order(HttpServletRequest request) {
//			ModelAndView mav = new ModelAndView();
//			try {
//				String money = request.getParameter("money");
//				String bank = request.getParameter("bank");
//	
//				String uid = this.getUserId(request);
//				String loginName = this.getLoginName(request);
//	
//				// 查询充值前金额
//				PayAccount account = payAccountService.getAccount(uid);
//				// 初始化一条订单
//				PayOrder order = new PayOrder();
//				String uuid = UUID.create();
//				order.setId(uuid);
//				order.setUid(uid);
//				order.setLoginName(loginName);
//				order.setMoney(new Double(money));
//				order.setBeforeMoney(account.getMoney());
//				order.setBank(bank);
//				//order.setPayType(OrderStatus.ORDER_TYPE_CHINAPNR);
//				order.setIpAddress(HttpUtil.getIpAddr(request));
//				order.setOrderId(payOrderService.getOrderIdNumber());
//				payOrderService.addOrder(order);
//	
//				mav.setView(new RedirectView(request.getContextPath()
//						+ "/order/chinapnr/show.do?id=" + uuid));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return mav;
//		}

//		/**
//		 * 确认订单
//		 * 
//		 * @param request
//		 * @return
//		 */
//		@RequestMapping(value = "/order/chinapnr/show.do")
//		public ModelAndView showConfirmChinaPnrOrder(HttpServletRequest request) {
//			ModelAndView mav = new ModelAndView();
	//
//			/**
//			 * 封装账户属性
//			 */
//			this.getPayAccount(request);
	//
//			String id = request.getParameter("id");
//			PayOrder order = payOrderService.getOrderById(id);
//			if (order == null) {
//				mav.setViewName("pay/chinapnr/failed");
//				return mav;
//			}
//			ChinaPnr pnr = new ChinaPnr();
//			pnr.setGateId(order.getGateId());
//			mav.addObject("bank", pnr.getGateBank());
//			mav.addObject("order", order);
//			mav.setViewName("pay/chinapnr/confirmorder");
//			return mav;
//		}
	
	
	
//	/**
//	 * 展现订单信息 再跳转至三方平台
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value="/order/chinapnr/appordersend.do")
//	public ModelAndView showOrder(HttpServletRequest request){
//		ModelAndView mav = new ModelAndView();
//		//展现订单
//		String id = request.getParameter("id");
//		String gateId = request.getParameter("bank");
//		AppOrder ao = payOrderService.getAppOrderByid(id);
//		
//		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
//		String rd = Function.getFixLenthString(6);// 6位随机数
//		Order od = new Order();
//		//接着做订单order
//		od.setId(nt+rd);
//		od.setUid(ao.getUid());
//		od.setLoginName(ao.getLoginName());
//		od.setMoney(ao.getMoney());
//		od.setAppId(ao.getAppId());
//		od.setAppOrderid(ao.getOrderId());
//		od.setPayStatus(0);//新订单默认为初始状态
//		od.setPayType(OrderStatus.ORDER_TYPE_CHINAPNR);
//		od.setGateId(gateId);
//		od.setIpAddress(HttpUtil.getIpAddr(request));
//		od.setPid(ao.getPid());
//		od.setMerpriv(id);
//		//http://test.chinapnr.com/gar/RecvMerchant.do
//		int i = payOrderService.addOrder(od);
//		if(i>0){
//			// 设置汇付天下在线支付所需属性
//			ChinapnrProperties chinapnr = new ChinapnrProperties();
//			chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
//			chinapnr.setCmdId(this.getProperty("ChinaPnr.Buy.CmdId"));// 消息类型
//			chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
//			chinapnr.setOrdId(od.getId());// 订单号
//			chinapnr.setOrdAmt(Function.formtDecimalPoint2(od.getMoney()));// 订单金额
//			chinapnr.setCurCode(this.getProperty("ChinaPnr.CurCode"));// 币种
//			chinapnr.setPid(od.getPid());// 商品编号
//			chinapnr.setRetUrl(this.getProperty("ChinaPnr.PCPay.RetUrl"));// 页面返回地址
//			chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.PCPay.BgRetUrl"));// 后台返回地址
//			chinapnr.setMerPriv(od.getMerpriv());// 商户私有数据项
//			chinapnr.setGateId(od.getGateId());// 网关号
//			chinapnr.setUsrMp("");// 用户手机号
//			chinapnr.setDivDetails("");// 分账明细
//			chinapnr.setPayUsrId(od.getUid());// 付款人用户号
//			// 签名
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//																	// 请将MerPrK510010.key改为你的私钥文件名称
//			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
//					+ chinapnr.getMerId() + chinapnr.getOrdId()
//					+ chinapnr.getOrdAmt() + chinapnr.getCurCode()
//					+ chinapnr.getPid() + chinapnr.getRetUrl()
//					+ chinapnr.getMerPriv() + chinapnr.getGateId()
//					+ chinapnr.getUsrMp() + chinapnr.getDivDetails()
//					+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);
//
//			ChinaPnr pnr = new ChinaPnr();
//			pnr.setGateId(od.getGateId());
//			if (ret != 0) {
//				logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
//			}
//			String chkValue = sl.getChkValue();
//			chinapnr.setChkValue(chkValue);// 数字签名
//
//			mav.addObject("bank", pnr.getGateBank());
//			mav.addObject("order", od);
//			mav.addObject("chinapnr", chinapnr);
//			mav.setViewName("pay/chinapnr/sendorder");
//		}else{
//			mav.addObject("msg", "生成支付订单失败！");
//			mav.setViewName("pay/ebank/error");
//		}
//		return mav;
//	}

//	/**
//	 * 展现订单信息 再跳转至三方平台
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value="/order/chinapnr/appordersend.do")
//	public ModelAndView showOrder(HttpServletRequest request){
//		ModelAndView mav = new ModelAndView();
//		//展现订单
//		String id = request.getParameter("id");
//		String gateId = request.getParameter("bank");
//		AppOrder ao = payOrderService.getAppOrderByid(id);
//		
//		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
//		String rd = Function.getFixLenthString(6);// 6位随机数
//		Order od = new Order();
//		//接着做订单order
//		od.setId(nt+rd);
//		od.setUid(ao.getUid());
//		od.setLoginName(ao.getLoginName());
//		od.setMoney(ao.getMoney());
//		od.setAppId(ao.getAppId());
//		od.setAppOrderid(ao.getOrderId());
//		od.setPayStatus(0);//新订单默认为初始状态
//		od.setPayType(OrderStatus.ORDER_TYPE_CHINAPNR);
//		od.setGateId(gateId);
//		od.setIpAddress(HttpUtil.getIpAddr(request));
//		od.setPid(ao.getPid());
//		od.setMerpriv(id);
//		//http://test.chinapnr.com/gar/RecvMerchant.do
//		int i = payOrderService.addOrder(od);
//		if(i>0){
//			// 设置汇付天下在线支付所需属性
//			ChinapnrProperties chinapnr = new ChinapnrProperties();
//			chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
//			chinapnr.setCmdId(this.getProperty("ChinaPnr.Buy.CmdId"));// 消息类型
//			chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
//			chinapnr.setOrdId(od.getId());// 订单号
//			chinapnr.setOrdAmt(Function.formtDecimalPoint2(od.getMoney()));// 订单金额
//			chinapnr.setCurCode(this.getProperty("ChinaPnr.CurCode"));// 币种
//			chinapnr.setPid(od.getPid());// 商品编号
//			chinapnr.setRetUrl(this.getProperty("ChinaPnr.PCPay.RetUrl"));// 页面返回地址
//			chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.PCPay.BgRetUrl"));// 后台返回地址
//			chinapnr.setMerPriv(od.getMerpriv());// 商户私有数据项
//			chinapnr.setGateId(od.getGateId());// 网关号
//			chinapnr.setUsrMp("");// 用户手机号
//			chinapnr.setDivDetails("");// 分账明细
//			chinapnr.setPayUsrId(od.getUid());// 付款人用户号
//			// 签名
//			String MerKeyFile = getClass().getResource(
//					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//																	// 请将MerPrK510010.key改为你的私钥文件名称
//			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
//					+ chinapnr.getMerId() + chinapnr.getOrdId()
//					+ chinapnr.getOrdAmt() + chinapnr.getCurCode()
//					+ chinapnr.getPid() + chinapnr.getRetUrl()
//					+ chinapnr.getMerPriv() + chinapnr.getGateId()
//					+ chinapnr.getUsrMp() + chinapnr.getDivDetails()
//					+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();
//
//			SecureLink sl = new SecureLink();
//			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);
//
//			ChinaPnr pnr = new ChinaPnr();
//			pnr.setGateId(od.getGateId());
//			if (ret != 0) {
//				logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
//			}
//			String chkValue = sl.getChkValue();
//			chinapnr.setChkValue(chkValue);// 数字签名
//
//			mav.addObject("bank", pnr.getGateBank());
//			mav.addObject("order", od);
//			mav.addObject("chinapnr", chinapnr);
//			mav.setViewName("pay/chinapnr/sendorder");
//		}else{
//			mav.addObject("msg", "生成支付订单失败！");
//			mav.setViewName("pay/ebank/error");
//		}
//		return mav;
//	}
}