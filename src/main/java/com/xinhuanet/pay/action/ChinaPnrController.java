package com.xinhuanet.pay.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import chinapnr.SecureLink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.BankGatewayEntry;
import com.xinhuanet.pay.gateway.ChinaPnr;
import com.xinhuanet.pay.gateway.ChinapnrGateway;
import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.ChinapnrProperties;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.ChinaPnrService;
import com.xinhuanet.pay.service.HttpClient4Service;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;

/**
 * 第三方支付平台，汇付天下支付控制器
 * @author duanwc
 */
@Controller
public class ChinaPnrController extends BaseController {
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
	 * 汇付天下业务相关服务
	 */
	private @Autowired ChinaPnrService pnrService;
	/**
	 * HttpClient服务
	 */
	protected @Autowired HttpClient4Service clientService;
	
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
		Map<String,Object> config = ChinapnrGateway.getInstance().getConfigMap();
		
		//获取用户信息
		Account account = accountService.getAccount(this.getUserId(request, response));
		AppOrder ao = appOrderService.getAppOrderById(id);
		
		ChinaPnr pnr = new ChinaPnr();
		
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
		od.setPayType(ThirdPartyGateway.chinapnr);
		od.setGateId(((BankGatewayEntry)config.get(bank)).getKey());
		od.setGateName(((BankGatewayEntry)config.get(bank)).getValue());
		od.setIpAddress(HttpUtil.getIpAddr(request));
		od.setPid(ao.getPid());
		od.setMerpriv(id);
		od.setBeforeMoney(account.getMoney());
		od.setOrderType(ao.getOrderType());
		od.setPayTime(Function.getDateTime());
		//http://test.chinapnr.com/gar/RecvMerchant.do
		int i = payOrderService.addOrder(od);
		if(i>0){
			// 设置汇付天下在线支付所需属性
			ChinapnrProperties chinapnr = new ChinapnrProperties();
			chinapnr.setVersion(this.getProperty("ChinaPnr.Buy.version"));// 版本
			chinapnr.setCmdId(this.getProperty("ChinaPnr.Buy.CmdId"));// 消息类型
			chinapnr.setMerId(this.getProperty("ChinaPnr.MerId"));// 商户号
			chinapnr.setOrdId(StringUtils.trimToEmpty(od.getId()));// 订单号
			chinapnr.setOrdAmt(Function.formtDecimalPoint2(od.getMoney()));// 订单金额
			chinapnr.setCurCode(this.getProperty("ChinaPnr.CurCode"));// 币种
			chinapnr.setPid(od.getPid());// 商品编号
			chinapnr.setRetUrl(this.getProperty("ChinaPnr.PCPay.RetUrl"));// 页面返回地址
			chinapnr.setBgRetUrl(this.getProperty("ChinaPnr.PCPay.BgRetUrl"));// 后台返回地址
			chinapnr.setMerPriv(od.getMerpriv());// 商户私有数据项
			chinapnr.setGateId(od.getGateId());// 网关号
			chinapnr.setUsrMp("");// 用户手机号
			chinapnr.setDivDetails("");// 分账明细
			chinapnr.setPayUsrId(od.getUid());// 付款人用户号
			// 签名
			String MerKeyFile = getClass().getResource(
					this.getProperty("ChinaPnr.MerPrK")).getPath(); // 商户私钥文件路径
																	// 请将MerPrK510010.key改为你的私钥文件名称
			String MerData = chinapnr.getVersion() + chinapnr.getCmdId()
					+ chinapnr.getMerId() + chinapnr.getOrdId()
					+ chinapnr.getOrdAmt() + chinapnr.getCurCode()
					+ StringUtils.trimToEmpty(chinapnr.getPid()) + chinapnr.getRetUrl()
					+ chinapnr.getMerPriv() + chinapnr.getGateId()
					+ chinapnr.getUsrMp() + chinapnr.getDivDetails()
					+ chinapnr.getPayUsrId() + chinapnr.getBgRetUrl();

			SecureLink sl = new SecureLink();
			int ret = sl.SignMsg(chinapnr.getMerId(), MerKeyFile, MerData);


			if (ret != 0) {
				logger.info("签名错误： ret=" + ret + "\n" + pnr.getRetName(ret));
			}
			String chkValue = sl.getChkValue();
			chinapnr.setChkValue(chkValue);// 数字签名
			
			
			//把请求参数打包成数组
			Map<String, String> sPara = new HashMap<String, String>();
			sPara.put("Version", chinapnr.getVersion());
			sPara.put("CmdId", chinapnr.getCmdId());
			sPara.put("MerId", chinapnr.getMerId());
			sPara.put("OrdId", chinapnr.getOrdId());
			sPara.put("OrdAmt", chinapnr.getOrdAmt());
			sPara.put("CurCode", chinapnr.getCurCode());
			sPara.put("Pid", chinapnr.getPid());
			sPara.put("RetUrl", chinapnr.getRetUrl());
			sPara.put("MerPriv", chinapnr.getMerPriv());
			sPara.put("GateId", chinapnr.getGateId());
			sPara.put("PayUsrId", chinapnr.getPayUsrId());
			sPara.put("BgRetUrl", chinapnr.getBgRetUrl());
			sPara.put("ChkValue", chinapnr.getChkValue());
			List<String> keys = new ArrayList<String>(sPara.keySet());
			
//	        String action = "http://test.chinapnr.com/gar/RecvMerchant.do";	//form action 地址
	        String action = props.getString("ChinaPnr.form.action");	//form action 地址
	        String describe = "正在为您转向汇付天下，请稍候...";	//跳转时描述
	        
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
	 * pc支付时汇付回调前台url地址
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/order/chinapnr/pcorderpayreturn.do")
	public ModelAndView pcorderPayReturn(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		System.out.println("汇付已经返回……前台通知页面");
		
		String message = "";// 提示信息
		boolean bStatus = true;// 是否成功
		ChinapnrProperties chinapnr = this.getPnrBuyReturnData(request);
		if (chinapnr == null) {
			bStatus = false;
			message = "交易失败";
			
			mav.addObject("msg", message);
			mav.setViewName("pay/ebank/error");
			return mav;
		} else {
			boolean transFlag = "000000".equals(chinapnr.getRespCode());
			updateAppOrderAndOrder(chinapnr);
			if (transFlag){
				// 页面获取信息再跳转到应用通知地址中
				mav.setView(new RedirectView(request.getContextPath()
						+ "/pay/ebank/complete.do?OrdId="+chinapnr.getOrdId()));
			} else {
				// 交易失败
				bStatus = false;
				message = "交易失败";
				logger.info("交易失败");
				mav.addObject("msg", message);
				mav.setViewName("pay/ebank/error");
			}
			
			return mav;
		}		
	}
	
	/**
	 * 支付动作完成后接收汇付天下返回的数据，并返回ChinapnrProperties对象
	 * 函数内包含了验证签名功能
	 * @param request
	 * @return 如果验证签名成功返回ChinapnrProperties对象，否则返回null
	 */
	private ChinapnrProperties getPnrBuyReturnData(HttpServletRequest request){
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
		String DivDetails = request.getParameter("DivDetails"); // 分账明细
		String GateId = request.getParameter("GateId"); // 银行ID
		String ChkValue = request.getParameter("ChkValue"); // 签名信息

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
		chinapnr.setDivDetails(DivDetails);// 分账明细
		chinapnr.setGateId(GateId);// 网关号
		chinapnr.setChkValue(ChkValue);// 数字签名


		// 验签
		String MerKeyFile = getClass().getResource(
				this.getProperty("ChinaPnr.PgPubk")).getPath();
		String MerData = chinapnr.getCmdId() + chinapnr.getMerId() + chinapnr.getRespCode() + chinapnr.getTrxId() + chinapnr.getOrdAmt() + chinapnr.getCurCode()
				+ chinapnr.getPid() + chinapnr.getOrdId() + chinapnr.getMerPriv() + chinapnr.getRetType() + chinapnr.getDivDetails() + chinapnr.getGateId(); // 参数顺序不能错
		SecureLink sl = new SecureLink();
		int ret = sl.VeriSignMsg(MerKeyFile, MerData, ChkValue);
		if (ret != 0) {
			logger.info("签名验证失败[" + MerData + "]");
			return null;
		}
		return chinapnr;
	}
	
	/**
	 * pc支付时汇付回调后台url地址
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/order/chinapnr/pcorderpaybgreturn.do")
	public String pcorderPayBgreturn(HttpServletRequest request){
		logger.info("汇付后台通知处理>>>>>start");
		String resultFlag = "RECV_ORD_ID_";
		ChinapnrProperties chinapnr = getPnrBuyReturnData(request);
		if (chinapnr != null) {
			updateAppOrderAndOrder(chinapnr);
			resultFlag += chinapnr.getOrdId();
		}
		logger.info("汇付后台通知处理>>>>>end");
		return resultFlag;
	}
	
	/**
	 * 更新订单操作(同一笔订单的更新操作保持同步)
	 * @param chinapnr
	 */
	private void updateAppOrderAndOrder(ChinapnrProperties chinapnr){
		
		String lock = chinapnr.getOrdId().intern();
		synchronized (lock) {
			Order order = payOrderService.getOrderById(chinapnr.getOrdId());
			AppOrder appOrder = appOrderService.getAppOrderById(chinapnr.getMerPriv());
			order.setTrxId(chinapnr.getTrxId()); //第三方平台交易号
			order.setMoney(Double.parseDouble(chinapnr.getOrdAmt()));
			boolean transFlag = "000000".equals(chinapnr.getRespCode());//true 表示交易成功
			
			if (EBank.isOperate(order.getPayStatus(), transFlag)) {
				if (transFlag){
					pnrService.thridOrdSucceedNotify(order,appOrder);
					logger.info("汇付天下通知(订单号:"+chinapnr.getOrdId()+")交易成功,后台更新订单成功");
				} else {
					pnrService.thridOrdFailedNotify(order, appOrder);
					logger.info("汇付天下通知(订单号:"+chinapnr.getOrdId()+")交易失败,后台更新订单成功");
				}
			}
		}
		
	}

	/**
	 * 汇付天下退款
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@ResponseBody
	@RequestMapping(value="/order/chinapnr/refundOrder.do")
	public String refundOrder(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		String oldOrdId = request.getParameter("oldOrdId");
		ChinapnrProperties chinapnr = pnrService.refundOrder(oldOrdId);
		JSONObject json = (JSONObject) JSON.toJSON(chinapnr);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 汇付天下查询退款订单状态
	 * 目前仅用于测试，应用于补单情况
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/chinapnr/queryRefundStatus.do")
	public String queryRefundStatus(HttpServletRequest request,HttpServletResponse response) {
		String ordId = request.getParameter("orderId");
//		String ordId = "20140102110802222234";
		ChinapnrProperties chinapnr = pnrService.queryRefundStatus(ordId);
		JSONObject json = (JSONObject) JSON.toJSON(chinapnr);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 汇付天下查询订单详细
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/chinapnr/queryOrder.do")
	public String queryOrder(HttpServletRequest request,HttpServletResponse response) {
		String ordId = request.getParameter("orderId");
		ChinapnrProperties chinapnr = pnrService.queryOrder(ordId);
		JSONObject json = (JSONObject) JSON.toJSON(chinapnr);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 汇付天下查询订单被支付状态
	 * 目前仅用于测试，应用于补单情况
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/chinapnr/queryStatus.do")
	public String queryStatus(HttpServletRequest request,HttpServletResponse response) {
		String ordId = request.getParameter("orderId");
//		String ordId = "20140102110802222234";
		ChinapnrProperties chinapnr = pnrService.queryStatus(ordId);
		JSONObject json = (JSONObject) JSON.toJSON(chinapnr);
		return JSON.toJSONString(json);
	}
	
	/**
	 * 汇付天下订单结算
	 * 目前仅用于测试，应用于补单情况
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/chinapnr/paymentConfirm.do")
	public String paymentConfirm(HttpServletRequest request,HttpServletResponse response) {
		String ordId = request.getParameter("orderId");
//		String ordId = "20140102110802222234";
		ChinapnrProperties chinapnr = pnrService.paymentConfirm(ordId);
		JSONObject json = (JSONObject) JSON.toJSON(chinapnr);
		return JSON.toJSONString(json);
	}
}