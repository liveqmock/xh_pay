package com.xinhuanet.pay.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import chinapnr.SecureLink;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.ChinaPnr;
import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.ChinapnrProperties;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.po.PayIncome;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayIncomeService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;
import com.xinhuanet.pay.util.UUID;

/**
 * 快捷支付
 * 
 * @author wangwei
 * 
 */
@Controller
public class FastPayController extends BaseController {
	
	
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
	 * 汇总服务
	 */
	private @Autowired PayIncomeService piService;
	
	@RequestMapping(value = "/order/chinapnr/openorder.do")
	public ModelAndView openOrder(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("pay/chinapnr/fastpay");
		return mav;
	}
	
	/**
	 * 先获取应用订单信息、再处理应用信息、最后跳转至选择银行界面。
	 * @param request
	 * @return
	 */
	@RequestMapping(value="order/chinapnr/selectbank.do")
	public ModelAndView selectBank(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String money = request.getParameter("money");

		String appId = request.getParameter("appId");
		String appOrderid = request.getParameter("appOrderid");//应用本身订单号
		String pid = request.getParameter("pid");
		String pname = request.getParameter("pname");
		String merpriv = request.getParameter("merpriv");
		String ext = request.getParameter("ext");
		String appReturl = request.getParameter("appReturl");
		String appBgReturl = request.getParameter("appBgReturl");
		String uid;

		try {
			uid = this.getUserId(request,response);
			String loginName = this.getLoginName(request,response);
			String stoken = (String) session.getAttribute("_submitAppOrderId");
			if (stoken == null || !stoken.equals(appOrderid)) {
				session.setAttribute("_submitAppOrderId", appOrderid);
			} else {
				mav.addObject("msg", "订单重复，请勿重复提交！");
				mav.setViewName("pay/ebank/error");
				return mav;
			}
			// 初始化一条订单
			AppOrder ao = new AppOrder();
			String uuid = UUID.create();
			ao.setId(uuid);//该笔交易相对支付系统的流水号
			ao.setUid(uid);
			ao.setLoginName(loginName);
			ao.setMoney(Double.parseDouble(money));
			ao.setRetUrl(appReturl);//应用前台返回地址
			ao.setBgRetUrl(appBgReturl);//应用后台返回地址
			ao.setIpAddress(HttpUtil.getIpAddr(request));
			ao.setAppId(Integer.parseInt(appId));
			ao.setOrderId(appOrderid);
			ao.setStatus(0);// 初始状态为0
			ao.setAddTime(new Date());// 支付时间默认为系统当前时间（需要添加个创建时间字段）
			ao.setPid(pid);
			ao.setPname(pname);
			ao.setMerPriv(merpriv);
			ao.setExt(ext);
			//存储应用订单信息
			int rs = appOrderService.insertAppOrder(ao);
			if(rs>0){
				mav.addObject("appId", appId);
				mav.addObject("money", money);
				mav.addObject("pid", pid);
				mav.addObject("pname", pname);
				mav.addObject("id", ao.getId());
				mav.setViewName("/pay/chinapnr/showphoneapporder");
			}else{
				mav.addObject("msg", "生成订单失败！");
				mav.setViewName("pay/ebank/error");
			}
		} catch (LoginUserNotFoundException e) {
			logger.info("用户身份验证失败：未登陆");
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * C：创建订单
	 * S：发送订单
	 * 创建并发送订单至汇付平台
	 * @param request
	 * @return
	 */
	@RequestMapping(value="order/chinapnr/csfastorderchinapnr.do")
	public ModelAndView CSFastOrderChinapnr(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//展现订单
		String id = request.getParameter("id");//应用订单相对支付系统流水号
		String gateId = request.getParameter("bank");
		AppOrder ao = appOrderService.getAppOrderById(id);
		
		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
		String rd = Function.getFixLenthString(6);// 6位随机数
		Order od = new Order();
		//接着做订单order
		od.setId(nt+rd);//支付系统本身订单号
		System.out.println("当前提交给汇付的支付订单号："+od.getId());
		od.setUid(ao.getUid());
		od.setLoginName(ao.getLoginName());
		od.setMoney(ao.getMoney());
		od.setAppId(ao.getAppId());
		od.setAppOrderId(ao.getOrderId());
		od.setPayStatus(0);//新订单默认为初始状态
		od.setPayType(ThirdPartyGateway.chinapnr);
		od.setGateId(gateId);
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		//http://test.chinapnr.com/gar/RecvMerchant.do
		int i = payOrderService.addOrder(od);
		if(i>0){
			// 设置汇付天下在线支付所需属性
			ChinapnrProperties chinapnr = new ChinapnrProperties();
			chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
			chinapnr.setCmdId(this.getProperty("ChinaPnr.fastPay.CmdId"));// 消息类型
			chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
			chinapnr.setOrdId(od.getId());// 订单号
			chinapnr.setOrdAmt(Function.formtDecimalPoint2(od.getMoney()));// 订单金额
			chinapnr.setCurCode(this.getProperty("ChinaPnr.CurCode"));// 币种
			chinapnr.setPid(od.getPid());// 商品编号
			chinapnr.setRetUrl(this.getProperty("ChinaPnr.fastPay.RetUrl"));// 页面返回地址
			chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.fastPay.BgRetUrl"));// 后台返回地址
			chinapnr.setMerPriv(od.getMerpriv());// 商户私有数据项
			chinapnr.setGateId(od.getGateId());// 网关号
			chinapnr.setPayUsrId(od.getUid());// 付款人用户号
			// 签名
			String MerKeyFile = getClass().getResource(
					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId()
					+ chinapnr.getOrdAmt() + chinapnr.getCurCode()
					+ chinapnr.getPid() + chinapnr.getRetUrl()
					+ chinapnr.getMerPriv() + chinapnr.getGateId()
					+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			ChinaPnr pnr = new ChinaPnr();
//			pnr.setGateId(od.getGateId());
			if (ret != 0) {
				logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名

//			mav.addObject("bank", pnr.getGateBank());
			mav.addObject("order", od);
			mav.addObject("chinapnr", chinapnr);
			mav.setViewName("pay/chinapnr/phonefastpaysendorder");
		}else{
			mav.addObject("msg", "生成支付订单失败！");
			mav.setViewName("pay/ebank/error");
		}
		return mav;
	}
	
	/**
	 * 汇付处理完成后返回前台通知地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/chinapnr/phonefastpayreturn.do")	
	public ModelAndView phoneFastpayReturn(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
System.out.println("汇付ok了，继续做本系统订单处理");
		String CmdId = request.getParameter("CmdId"); // 消息类型
		String MerId = request.getParameter("MerId"); // 商户号
		String RespCode = request.getParameter("RespCode"); // 应答返回码
		String TrxId = request.getParameter("TrxId"); // 钱管家交易唯一标识
		String OrdAmt = request.getParameter("OrdAmt"); // 金额
		String CurCode = request.getParameter("CurCode"); // 币种
		String Pid = request.getParameter("Pid"); // 商品编号
		String OrdId = request.getParameter("OrdId"); // 订单号
		String MerPriv = request.getParameter("MerPriv"); // 商户私有域
		String RetType = request.getParameter("RetType"); // 返回类型
		String GateId = request.getParameter("GateId"); // 银行ID
		String ChkValue = request.getParameter("ChkValue"); // 签名信息
System.out.println(ChkValue);
		// 设置汇付天下在线支付所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setCmdId(CmdId);// 消息类型
		chinapnr.setMerId(MerId);// 商户号
		chinapnr.setRespCode(RespCode);// /应答返回码
		chinapnr.setTrxId(TrxId);// 钱管家交易唯一标识
		chinapnr.setOrdAmt(OrdAmt);// 订单金额
		chinapnr.setCurCode(CurCode);// 币种
		chinapnr.setPid(Pid);// 商品编号
		chinapnr.setOrdId(OrdId);// 订单号
		chinapnr.setMerPriv(MerPriv);// 商户私有数据项
		chinapnr.setRetType(RetType);// 返回类型
		chinapnr.setGateId(GateId);// 网关号
		chinapnr.setChkValue(ChkValue);// 数字签名
		System.out.println(chinapnr);

		String message = "";// 提示信息
		boolean bStatus = true;// 是否成功

System.out.println(TrxId + "     &&&&&&&&&&&");
System.out.println(CmdId + "     ^^^^^^^^^^^^^^^^");
		// 验签
		String MerKeyFile = getClass().getResource(
				this.getProperty("ChinaPnr.PgPubk")).getPath();
		String MerData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode
				+ Pid + OrdId + MerPriv + RetType  + GateId; // 参数顺序不能错
		SecureLink sl = new SecureLink();
		int ret = sl.VeriSignMsg(MerKeyFile, MerData, ChkValue);
System.out.println("-------------" + ret);
		if (ret != 0) {
			bStatus = false;
			message = "交易失败";
			logger.info("签名验证失败[" + MerData + "]");
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
			return mav;
		} else {
			// 因为系统没有在公网上部署，所以，汇付后台请求不到，以下数据库操作业务应在后台通知处理
			Order order = new Order();
			// 查询当前订单本地状态
			if (RespCode.equals("000000")) {
				order = payOrderService.getOrderById(OrdId);// 查询当前订单的信息
				// 交易成功
				// 根据订单号 进行相应业务操作
				message = "交易成功";
				logger.info("交易成功");
				// 验证是否需要修改数据库，需要更改则更新订单状态和用户余额
				if (EBank.isOperate(order.getPayStatus(), true)) {
					PayIncome pi = new PayIncome();
System.out.println("汇总表id：        "+order.getId());
					pi.setId(order.getId());
					pi.setAppId(order.getAppId());
					pi.setMoney(order.getMoney());
					pi.setPayflatform(order.getPayType());
					pi.setPayTime(new Date());
					// 更新汇总表数据
					piService.addIncome(pi);
					logger.info("汇总表插入数据已更新！");
					order.setPayTime(new Date());
					order.setPayStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
					// TODO 更新用户余额 先加再减
					// TODO 操作 用户收支明细

					// 更新订单表
					payOrderService.succeedOrder(order);
					logger.info("订单信息更新成功！新增订单：" + order);
					AppOrder appOrder = new AppOrder();
					appOrder = appOrderService.getAppOrderById(MerPriv);
					appOrder.setStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
					// 更新应用订单表
//					appOrderService.succeedAppOrder(appOrder);
					logger.info("应用订单信息更新成功！新增订单：" + appOrder);
					// 封装信息到页面
					mav.addObject("apporder", appOrder);
					mav.addObject("chinapnr", chinapnr);
					mav.addObject("order", order);
					mav.addObject("message", message);
					mav.addObject("bStatus", bStatus);
					// 页面获取信息再跳转到应用通知地址中
					mav.setViewName("pay/chinapnr/payend");
				}
			} else {
System.out.println("else----------------");
				// 交易失败
				// 根据订单号 进行相应业务操作
				// 验证是否需要修改数据库,需要更改则更新订单状态
				if (EBank.isOperate(order.getPayStatus(), false)) {

				}
				String errMsg = request.getParameter("ErrMsg");
				bStatus = false;
				message = "交易失败"+errMsg;
				logger.info("交易失败"+errMsg);
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}
			return mav;
		}
	}
	
	/**
	 * 汇付处理完成后返回后台通知地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/chinapnr/phonefastpaybgreturn")	
	public ModelAndView phoneFastpayBgReturn(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		
		return mav;
	}
	
//	/**
//	 * 向汇付发送手机快捷支付订单信息
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/fastpaysend.do")
//	public ModelAndView sendFastPayOrderPnr(HttpServletRequest request,HttpServletResponse response) {
//		ModelAndView mav = new ModelAndView();
//		HttpSession session = request.getSession();
//		System.out.println("AAAAAAAAAAAAAAAAAAA");
//		String money = request.getParameter("money");
//		String orderId = request.getParameter("orderId");
//		String gateId = "T1";// 网关号
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
//
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
//		PayOrder order = new PayOrder();
////		order.setMoney();
//		order.setOrderId(orderId);
//		order.setGateId(gateId);
//		ChinapnrProperties pnrParams = new ChinapnrProperties();
//		pnrParams.setVersion(this.getProperty("ChinaPnr.Buy.version"));
//		pnrParams.setCmdId(this.getProperty("ChinaPnr.fastPay.CmdId"));
//		pnrParams.setMerId(this.getProperty("ChinaPnr.MerId"));
//		pnrParams.setCurCode(this.getProperty("ChinaPnr.CurCode"));
//		pnrParams.setRetUrl(this.getProperty("ChinaPnr.Buy.RetUrl"));// 页面返回地址
//		pnrParams.setBgRetUrl(this.getProperty("ChinaPnr.Buy.BgRetUrl"));// 后台返回地址
//		pnrParams.setOrdId(order.getOrderId());
//		pnrParams.setOrdAmt(Function.formtDecimalPoint2(Double.parseDouble(money)));
//
//		// 签名
//		String MerKeyFile = getClass().getResource(
//				this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//		// 请将MerPrK510010.key改为你的私钥文件名称
//		String MerData = pnrParams.getVersion() + pnrParams.getCmdId()
//				+ pnrParams.getMerId() + pnrParams.getOrdId()
//				+ pnrParams.getOrdAmt() + pnrParams.getCurCode()
//				+ pnrParams.getRetUrl() + pnrParams.getBgRetUrl();
//
//		SecureLink sl = new SecureLink();
//		int ret = sl.SignMsg(pnrParams.getMerId(), MerKeyFile, MerData);
//
//		ChinaPnr pnr = new ChinaPnr();
//		pnr.setGateId(pnrParams.getGateId());
//		if (ret != 0) {
//			logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
//		}
//		String chkValue = sl.getChkValue();
//		pnrParams.setChkValue(chkValue);// 数字签名
//		
//		mav.addObject("bank",pnr.getGateBank());
//		mav.addObject("order", order);
//		mav.addObject("chinapnr", pnrParams);
//		mav.setViewName("pay/chinapnr/fastpaysendorder");
//		
//		return mav;
//	}
	
	//****************PC快捷支付***********************//
	/**
	 * pc快捷支付选择银行
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/chinapnr/pcselectbank.do")
	public ModelAndView pcFastPaySelectBank(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String money = request.getParameter("money");

		String appId = request.getParameter("appId");
		String appOrderId = request.getParameter("orderId");//应用本身订单号
		String pid = request.getParameter("pid");
		String pname = request.getParameter("pname");
		String merpriv = request.getParameter("merpriv");
		String ext = request.getParameter("ext");
		String appReturl = request.getParameter("appReturl");
		String appBgReturl = request.getParameter("appBgReturl");
		String uid;

		try {
			uid = this.getUserId(request,response);
			String loginName = this.getLoginName(request,response);
			String stoken = (String) session.getAttribute("_submitAppOrderId");
			if (stoken == null || !stoken.equals(appOrderId)) {
				session.setAttribute("_submitAppOrderId", appOrderId);
			} else {
				mav.addObject("msg", "订单重复，请勿重复提交！");
				mav.setViewName("pay/ebank/error");
				return mav;
			}
			// 初始化一条订单
			AppOrder ao = new AppOrder();
			String uuid = UUID.create();
			ao.setId(uuid);//该笔交易相对支付系统的流水号
			ao.setUid(uid);
			ao.setLoginName(loginName);
			ao.setMoney(Double.parseDouble(money));
			ao.setRetUrl(appReturl);//应用前台返回地址
			ao.setBgRetUrl(appBgReturl);//应用后台返回地址
			ao.setIpAddress(HttpUtil.getIpAddr(request));
			ao.setAppId(Integer.parseInt(appId));
			ao.setOrderId(appOrderId);
			ao.setStatus(0);// 初始状态为0
			ao.setAddTime(new Date());// 支付时间默认为系统当前时间（需要添加个创建时间字段）
			ao.setPid(pid);
			ao.setPname(pname);
			ao.setMerPriv(merpriv);
			ao.setExt(ext);
			//存储应用订单信息
			int rs = appOrderService.insertAppOrder(ao);
			if(rs>0){
				mav.addObject("appId", appId);
				mav.addObject("money", money);
				mav.addObject("pid", pid);
				mav.addObject("pname", pname);
				mav.addObject("id", ao.getId());
				mav.setViewName("/pay/chinapnr/showpcapporder");
			}else{
				mav.addObject("msg", "生成订单失败！");
				mav.setViewName("pay/ebank/error");
			}
		} catch (LoginUserNotFoundException e) {
			logger.info("用户身份验证失败：未登陆");
			e.printStackTrace();
		}
		return mav;
	}
	
	
	/**
	 * PC快捷支付，生产订单并向汇付发送订单请求。
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/chinapnr/pcfastorderchinapnr")
	public ModelAndView pcFastOrderChinapnr(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//展现订单
		String id = request.getParameter("id");//应用订单相对支付系统流水号
		String gateId = request.getParameter("bank");
		AppOrder ao = appOrderService.getAppOrderById(id);
		
		String nt = Function.getDateString(new Date(), "yyyyMMddhhmmss");// 时间值
		String rd = Function.getFixLenthString(6);// 6位随机数
		Order od = new Order();
		//接着做订单order
		od.setId(nt+rd);//支付系统本身订单号
System.out.println("当前提交给汇付的支付订单号："+od.getId());
		od.setUid(ao.getUid());
		od.setLoginName(ao.getLoginName());
		od.setMoney(ao.getMoney());
		od.setAppId(ao.getAppId());
		od.setAppOrderId(ao.getOrderId());
		od.setPayStatus(0);//新订单默认为初始状态
		od.setPayType(ThirdPartyGateway.chinapnr);
		od.setGateId(gateId);
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		//http://test.chinapnr.com/gar/RecvMerchant.do
		int i = payOrderService.addOrder(od);
		if(i>0){
			// 设置汇付天下在线支付所需属性
			ChinapnrProperties chinapnr = new ChinapnrProperties();
			chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
			chinapnr.setCmdId(this.getProperty("ChinaPnr.pcfastPay.CmdId"));// 消息类型
			chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
			chinapnr.setOrdId(od.getId());// 订单号
			chinapnr.setOrdAmt(Function.formtDecimalPoint2(od.getMoney()));// 订单金额
			chinapnr.setPid(od.getPid());// 商品编号
			chinapnr.setMerPriv(od.getMerpriv());// 商户私有数据项
//			chinapnr.setGateId(od.getGateId());
			chinapnr.setRetUrl(this.getProperty("ChinaPnr.pcfastPay.RetUrl"));// 页面返回地址
			chinapnr.setPayUsrId(od.getUid());// 付款人用户号
			chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.pcfastPay.BgRetUrl"));// 后台返回地址
			chinapnr.setHPVersion(this.getProperty("ChinaPnr.pcfastPay.HPVersion"));
			
			// 签名
			String MerKeyFile = getClass().getResource(
					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId()
					+ chinapnr.getOrdAmt() + chinapnr.getPid() 
					+ chinapnr.getRetUrl()	+ chinapnr.getMerPriv() 				
					+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();
			
System.out.println("参与签名的字段值:"+MerData);
			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);

			ChinaPnr pnr = new ChinaPnr();
//			pnr.setGateId(od.getGateId());
			if (ret != 0) {
				logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
			}
			String chkValue = sl.getChkValue();
System.out.println("签名信息："+chkValue);
			chinapnr.setChkValue(chkValue);// 数字签名

//			mav.addObject("bank", pnr.getGateBank());
			mav.addObject("order", od);
			mav.addObject("chinapnr", chinapnr);
			mav.setViewName("pay/chinapnr/pcfastpaysendorder");
		}else{
			mav.addObject("msg", "生成支付订单失败！");
			mav.setViewName("pay/ebank/error");
		}
		return mav;
	}
	
	@RequestMapping(value="/order/chinapnr/pcfastpayreturn.do")
	public ModelAndView pcFastpayReturn(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
System.out.println("汇付ok了，继续做本系统订单处理");
		String CmdId = request.getParameter("CmdId"); // 消息类型
		String MerId = request.getParameter("MerId"); // 商户号
		String RespCode = request.getParameter("RespCode"); // 应答返回码
		String TrxId = request.getParameter("TrxId"); // 钱管家交易唯一标识
		String OrdAmt = request.getParameter("OrdAmt"); // 金额
		String CurCode = request.getParameter("CurCode"); // 币种
		String Pid = request.getParameter("Pid"); // 商品编号
		String OrdId = request.getParameter("OrdId"); // 订单号
		String MerPriv = request.getParameter("MerPriv"); // 商户私有域
		String RetType = request.getParameter("RetType"); // 返回类型
		String GateId = request.getParameter("GateId"); // 银行ID
		String ChkValue = request.getParameter("ChkValue"); // 签名信息
System.out.println(ChkValue);
		// 设置汇付天下在线支付所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setCmdId(CmdId);// 消息类型
		chinapnr.setMerId(MerId);// 商户号
		chinapnr.setRespCode(RespCode);// /应答返回码
		chinapnr.setTrxId(TrxId);// 钱管家交易唯一标识
		chinapnr.setOrdAmt(OrdAmt);// 订单金额
		chinapnr.setCurCode(CurCode);// 币种
		chinapnr.setPid(Pid);// 商品编号
		chinapnr.setOrdId(OrdId);// 订单号
		chinapnr.setMerPriv(MerPriv);// 商户私有数据项
		chinapnr.setRetType(RetType);// 返回类型
		chinapnr.setGateId(GateId);// 网关号
		chinapnr.setChkValue(ChkValue);// 数字签名
System.out.println(chinapnr);

		String message = "";// 提示信息
		boolean bStatus = true;// 是否成功

System.out.println(TrxId + "     &&&&&&&&&&&");
System.out.println(CmdId + "     ^^^^^^^^^^^^^^^^");
		// 验签
		String MerKeyFile = getClass().getResource(
				this.getProperty("ChinaPnr.PgPubk")).getPath();
		String MerData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode
				+ Pid + OrdId  + MerPriv + RetType  + GateId; // 参数顺序不能错
		SecureLink sl = new SecureLink();
		int ret = sl.VeriSignMsg(MerKeyFile, MerData, ChkValue);
System.out.println("-------------" + ret);
		if (ret != 0) {
			bStatus = false;
			message = "交易失败";
			logger.info("签名验证失败[" + MerData + "]");
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
			return mav;
		} else {
			// 因为系统没有在公网上部署，所以，汇付后台请求不到，以下数据库操作业务应在后台通知处理
			Order order = new Order();
			// 查询当前订单本地状态
			if (RespCode.equals("000000")) {
				order = payOrderService.getOrderById(OrdId);// 查询当前订单的信息
				// 交易成功
				// 根据订单号 进行相应业务操作
				message = "交易成功";
				logger.info("交易成功");
				// 验证是否需要修改数据库，需要更改则更新订单状态和用户余额
				if (EBank.isOperate(order.getPayStatus(), true)) {
					PayIncome pi = new PayIncome();
System.out.println("汇总表id：        "+order.getId());
					pi.setId(order.getId());
					pi.setAppId(order.getAppId());
					pi.setMoney(order.getMoney());
					pi.setPayflatform(order.getPayType());
					pi.setPayTime(new Date());
					// 更新汇总表数据
					piService.addIncome(pi);
					logger.info("汇总表插入数据已更新！");
					order.setPayTime(new Date());
					order.setPayStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
					// TODO 更新用户余额 先加再减
					// TODO 操作 用户收支明细

					// 更新订单表
					payOrderService.succeedOrder(order);
					logger.info("订单信息更新成功！新增订单：" + order);
					AppOrder appOrder = new AppOrder();
					appOrder = appOrderService.getAppOrderById(MerPriv);
					appOrder.setStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
					// 更新应用订单表
//					appOrderService.succeedAppOrder(appOrder);
					logger.info("应用订单信息更新成功！新增订单：" + appOrder);
					// 封装信息到页面
					mav.addObject("apporder", appOrder);
					mav.addObject("chinapnr", chinapnr);
					mav.addObject("order", order);
					mav.addObject("message", message);
					mav.addObject("bStatus", bStatus);
					// 页面获取信息再跳转到应用通知地址中
					mav.setViewName("pay/chinapnr/payend");
				}
			} else {
System.out.println("else----------------");
				// 交易失败
				// 根据订单号 进行相应业务操作
				// 验证是否需要修改数据库,需要更改则更新订单状态
				if (EBank.isOperate(order.getPayStatus(), false)) {

				}
				String errMsg = request.getParameter("ErrMsg");
				bStatus = false;
				message = "交易失败"+errMsg;
				logger.info("交易失败"+errMsg);
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}
			return mav;
		}
	}
	
	
	
	//**************************PC快捷支付结束*********************************//
//	/**
//	 * 向汇付发送手机快捷支付订单信息
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/order/chinapnr/pcfastpaysend.do")
//	public ModelAndView sendpcFastPayOrderPnr(HttpServletRequest request,HttpServletResponse response) {
//		ModelAndView mav = new ModelAndView();
//		HttpSession session = request.getSession();
//		System.out.println("BBBBBBBBBBBBBBBB");
//		String money = request.getParameter("money");
//		String orderId = request.getParameter("orderId");
//		String gateId = "T1";// 网关号
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
//
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
//		PayOrder order = new PayOrder();
////		order.setMoney();
//		order.setOrderId(orderId);
//		order.setGateId(gateId);
//		ChinapnrProperties pnrParams = new ChinapnrProperties();
//		pnrParams.setVersion(this.getProperty("ChinaPnr.Buy.version"));
//		pnrParams.setCmdId(this.getProperty("ChinaPnr.pcfastPay.CmdId"));
//		pnrParams.setMerId(this.getProperty("ChinaPnr.MerId"));
//		pnrParams.setOrdId(order.getOrderId());
//		pnrParams.setOrdAmt(Function.formtDecimalPoint2(Double.parseDouble(money)));
//		pnrParams.setCurCode(this.getProperty("ChinaPnr.CurCode"));
//		pnrParams.setHPVersion(this.getProperty("ChinaPnr.pcfastPay.HPVersion"));//支付版本号（不参与签名加密）
//		pnrParams.setRetUrl(this.getProperty("ChinaPnr.Buy.RetUrl"));// 页面返回地址
//		pnrParams.setPayUsrId(account.getUid());//用户id
//		pnrParams.setHPVersion(this.getProperty("ChinaPnr.pcfastPay.HPVersion"));//支付版本
//		pnrParams.setBgRetUrl(this.getProperty("ChinaPnr.Buy.BgRetUrl"));// 后台返回地址
//		pnrParams.setCancelURL(this.getProperty("ChinaPnr.Buy.CancelURL"));//用户撤销支付后返回的地址
//		pnrParams.setCancelPriv(this.getProperty("ChinaPnr.Buy.CancelURL")+"?orderId="+order.getOrderId());//撤销私有域
//		// 签名
//		String MerKeyFile = getClass().getResource(
//				this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
//		// 请将MerPrK510010.key改为你的私钥文件名称
//		String MerData = pnrParams.getVersion() + pnrParams.getCmdId()
//				+ pnrParams.getMerId() + pnrParams.getOrdId()
//				+ pnrParams.getOrdAmt() + pnrParams.getCurCode()
//				+ pnrParams.getRetUrl() + pnrParams.getPayUsrId()
//				+ pnrParams.getBgRetUrl();
//
//		SecureLink sl = new SecureLink();
//		int ret = sl.SignMsg(pnrParams.getMerId(), MerKeyFile, MerData);
//
//		ChinaPnr pnr = new ChinaPnr();
//		pnr.setGateId(pnrParams.getGateId());
//		if (ret != 0) {
//			logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
//		}
//		String chkValue = sl.getChkValue();
//		pnrParams.setChkValue(chkValue);// 数字签名
//		
//		mav.addObject("bank",pnr.getGateBank());
//		mav.addObject("order", order);
//		mav.addObject("chinapnr", pnrParams);
//		mav.setViewName("pay/chinapnr/pcfastpaysendorder");
//		
//		return mav;
//	}
	
	/**
	 * 汇付天下后台通知页面
	 * @return
	 */
	@RequestMapping(value = "/pay/chinapnr/fastpayreturn.do")
	public String fastPayReturn(HttpServletRequest request){
	    String 	CmdId = request.getParameter("CmdId");			    //消息类型
	    String 	MerId = request.getParameter("MerId");	 			//商户号
	    String 	RespCode = request.getParameter("RespCode"); 		//应答返回码
	    String 	TrxId = request.getParameter("TrxId"); 				//钱管家交易唯一标识
	    String 	OrdAmt = request.getParameter("OrdAmt");  			//金额
	    String 	CurCode = request.getParameter("CurCode"); 			//币种
	    String 	Pid = request.getParameter("Pid");					//商品编号
	    String 	OrdId = request.getParameter("OrdId"); 				//订单号
	    String 	MerPriv = request.getParameter("MerPriv");			//商户私有域
	    String 	RetType = request.getParameter("RetType");			//返回类型
	    String 	DivDetails = request.getParameter("DivDetails"); 	//分账明细
	    String 	GateId = request.getParameter("GateId");  			//银行ID
	    String 	ChkValue = request.getParameter("ChkValue"); 		//签名信息 
		

		//设置汇付天下在线支付所需属性
		ChinapnrProperties chinapnr = new ChinapnrProperties();
		chinapnr.setCmdId(CmdId);//消息类型
		chinapnr.setMerId(MerId);//商户号
		chinapnr.setRespCode(RespCode);///应答返回码
		chinapnr.setTrxId(TrxId);//钱管家交易唯一标识
		chinapnr.setOrdAmt(OrdAmt);//订单金额
		chinapnr.setCurCode(CurCode);//币种
		chinapnr.setPid(Pid);//商品编号
		chinapnr.setOrdId(OrdId);//订单号
		chinapnr.setMerPriv(MerPriv);//商户私有数据项
		chinapnr.setRetType(RetType);//返回类型
		chinapnr.setDivDetails(DivDetails);//分账明细
		chinapnr.setGateId(GateId);//网关号
		chinapnr.setChkValue(ChkValue);//数字签名
		try{
			//验签
			String 	MerKeyFile	= getClass().getResource(this.getProperty("ChinaPnr.PgPubk")).getPath();
			String	MerData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid + OrdId + MerPriv + RetType + DivDetails + GateId;  	//参数顺序不能错
			SecureLink sl = new SecureLink( ) ;
			int ret = sl.VeriSignMsg(MerKeyFile , MerData, ChkValue) ;
				
			if (ret != 0) {
				System.out.println("签名验证失败["+MerData+"]");
			}else{
				if(RespCode.equals("000000")){
					//交易成功
					//根据订单号 进行相应业务操作
					//在些插入代码
					logger.info("后台通知：交易成功");
				}else{
					//交易失败
					//根据订单号 进行相应业务操作
					//在些插入代码
					logger.info("交易失败");
				}
			}
		}catch(Exception e){
			logger.info("签名验证异常");
		}
		
		request.setAttribute("chinapnr", chinapnr);
		request.setAttribute("recvOrdid", "RECV_ORD_ID_"+OrdId);
		
		return "pay/chinapnr/chinapnrnotify";
	}
}
