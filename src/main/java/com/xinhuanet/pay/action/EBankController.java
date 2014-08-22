package com.xinhuanet.pay.action;

import java.net.URL;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.Constants;
import com.xinhuanet.pay.common.Version;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.gateway.AdapterGateway;
import com.xinhuanet.pay.gateway.AllinpayGateway;
import com.xinhuanet.pay.gateway.Gateway;
import com.xinhuanet.pay.gateway.ThirdPartyGateway;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.SysConfig;
import com.xinhuanet.pay.security.SecureLink;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.SysConfigService;
import com.xinhuanet.pay.util.Arith;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;
import com.xinhuanet.platform.base.AppType;

/**
 * 网银支付控制器
 */
@Controller
public class EBankController extends BaseController {
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
	 * 网关适配器
	 */
	private @Autowired AdapterGateway gatewayService;
	/**
	 * 汇付天下支付控制类
	 */
	private @Autowired ChinaPnrController chinapnrController;
	
	/**
	 * 通联支付控制类
	 */
	private @Autowired AllinpayController allinpayController;
	
	/**
	 * 系统充值页
	 * 生成应用订单基本信息，再选择银行，汇付天下银行卡充值页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/stored.do")
	public ModelAndView payChinaPnrBankCard(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		try {
			// 初始化一条订单
			AppOrder order = new AppOrder();
			order.setMoney(Double.parseDouble(request.getParameter("money"))); //充值金额
			order.setAppId(AppType.PAY);		//应用ID
			order.setOrderId(payOrderService.getOrderIdNumber());	//订单号,不超过20位
			order.setPid("");		//商品ID
			order.setPname("");		//商品名称
			order.setMerPriv("");	//用户私有域
			order.setRetUrl("");	//应用前台返回地址
			order.setBgRetUrl("");	//应用后台返回地址
			order.setOrderType(0); 	//充值类型
			order.setVersion(Version.CURRENT_VERSION);
			order.setOrderTime(Function.getDateTime());
			
			String money = Function.formtDecimalPoint2(order.getMoney());
			String version = order.getVersion();
			String appId = String.valueOf(order.getAppId());
			String orderId = order.getOrderId();
			String pid = order.getPid();
			String pname = order.getPname();
			String merPriv = order.getMerPriv();
			String appRetUrl = order.getRetUrl();
			String appBgRetUrl = order.getBgRetUrl();
			String orderType = String.valueOf(order.getOrderType());
			String orderTime = Function.getDateTimeString((Timestamp)order.getOrderTime());
			String cmdId = "Buy";
			
			// 签名
			URL keyFile = getClass().getResource("/security/MerPrk"+appId+".key");// 请将MerPrK.key改为你的私钥文件名称
	        if(keyFile==null){
	        	mav.addObject("msg", "您的密钥文件不存在");
				mav.setViewName("pay/ebank/error");
				return mav;
	        }
			String MerKeyFile = keyFile.getPath(); // 应用私钥文件路径
//			String MerData = money + appId + orderId + pid + pname + merpriv + appRetUrl + appBgRetUrl + orderType;
			String MerData = version + cmdId + money + appId + orderId + pid + pname + merPriv + appRetUrl + appBgRetUrl + orderType + orderTime;

			SecureLink sl = new SecureLink();
			int ret = sl.signMsg(String.valueOf(AppType.PAY), MerKeyFile, MerData);

			if (ret != 0) {
				logger.info("签名错误： ret=" + ret + "\n");
			}
			String chkValue = sl.getChkValue();
			
			RedirectView redirect = new RedirectView("/adapter/pay");

			mav.addObject("version", version);
			mav.addObject("cmdId",cmdId);
			mav.addObject("money", money);
			mav.addObject("appId", appId);
			mav.addObject("orderId", orderId);
			mav.addObject("pid", pid);
			mav.addObject("pname", pname);
			mav.addObject("merPriv", merPriv);
			mav.addObject("appRetUrl", appRetUrl);
			mav.addObject("appBgRetUrl", appBgRetUrl);
			mav.addObject("chkValue", chkValue);
			mav.addObject("orderType", orderType);
			mav.addObject("orderTime", orderTime);

			
			redirect.setEncodingScheme("UTF-8");
			redirect.setContextRelative(true); 
			mav.setView(redirect);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	
	/**
	 * 系统充值页
	 * 生成应用订单基本信息，再选择银行，汇付天下银行卡充值页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pay/deposit.do")
	public ModelAndView payDeposit(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		Account account = this.getAccount(request, response);
		try {
			// 初始化一条订单
			AppOrder order = new AppOrder();
			SysConfig config = configService.getSysConfig(Constants.DEPOSIT);
			Double sysDeposit =  new Double(config.getCfgValue());
			
//			order.setMoney(Double.parseDouble(request.getParameter("money"))); //充值金额
			order.setMoney(Arith.sub(sysDeposit, account.getDeposit())); //应缴纳保证金金额
			
			order.setAppId(AppType.PAY);		//应用ID
			order.setOrderId(payOrderService.getOrderIdNumber());	//订单号,不超过20位
			order.setPid("");		//商品ID
			order.setPname("");		//商品名称
			order.setMerPriv("");	//用户私有域
			order.setRetUrl("");	//应用前台返回地址
			order.setBgRetUrl("");	//应用后台返回地址
			order.setOrderType(2); 	//充值类型
			
			
			String money = Function.formtDecimalPoint2(order.getMoney());
			String appId = String.valueOf(order.getAppId());
			String orderId = order.getOrderId();
			String pid = order.getPid();
			String pname = order.getPname();
			String merpriv = order.getMerPriv();
			String appRetUrl = order.getRetUrl();
			String appBgRetUrl = order.getBgRetUrl();
			String orderType = String.valueOf(order.getOrderType());
			
			// 签名
			URL keyFile = getClass().getResource("/security/MerPrk"+appId+".key");// 请将MerPrK.key改为你的私钥文件名称
	        if(keyFile==null){
	        	mav.addObject("msg", "您的密钥文件不存在");
				mav.setViewName("pay/ebank/error");
				return mav;
	        }
			String MerKeyFile = keyFile.getPath(); // 应用私钥文件路径
			String MerData = money + appId + orderId + pid + pname + merpriv + appRetUrl + appBgRetUrl + orderType;

			SecureLink sl = new SecureLink();
			int ret = sl.signMsg(String.valueOf(AppType.PAY), MerKeyFile, MerData);

			if (ret != 0) {
				logger.info("签名错误： ret=" + ret + "\n");
			}
			String chkValue = sl.getChkValue();
			
			RedirectView redirect = new RedirectView("/adapter/pay");
			mav.addObject("money", money);
			mav.addObject("appId", appId);
			mav.addObject("orderId", orderId);
			mav.addObject("pid", pid);
			mav.addObject("pname", pname);
			mav.addObject("merPriv", merpriv);
			mav.addObject("appRetUrl", appRetUrl);
			mav.addObject("appBgRetUrl", appBgRetUrl);
			mav.addObject("chkValue", chkValue);
			mav.addObject("orderType", orderType);
			mav.addObject("sysDeposit", sysDeposit);
			
			redirect.setEncodingScheme("UTF-8");
			redirect.setContextRelative(true); 
			mav.setView(redirect);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	/**
	 * 确认订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ebank/choose")
	public ModelAndView ebankChoose(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		this.getAccount(request, response);
		String id = request.getParameter("id");
		
		String errorRedirectUrl = "pay/ebank/error"; //错误信息提示
		String successRedirectUrl = "pay/ebank/bankcard"; //页面支付选择
		
		if(HttpUtil.isWAP(request)){
			errorRedirectUrl = "wap/error"; //wap错误信息提示
			successRedirectUrl = "wap/alipay"; //wap错误信息提示
		}
		
		AppOrder order = appOrderService.getAppOrderById(id);
		if (order == null) {
			mav.setViewName(errorRedirectUrl);
			return mav;
		}
		mav.addObject("order", order);
		mav.addObject("bankGatewayMap", AllinpayGateway.getInstance().getConfigMap());
		mav.setViewName(successRedirectUrl);
		return mav;
	}
	/**
	 * 确认订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/ebank/order/confirm.do")
	public ModelAndView ebankShowConfirmOrder(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();

		/**
		 * 封装账户属性
		 */
		this.getAccount(request,response);

		String id = request.getParameter("id");
		String bank = request.getParameter("bank");
		
		AppOrder order = appOrderService.getAppOrderById(id);
		
		if (order == null) {
			mav.setViewName("pay/chinapnr/failed");
			return mav;
		}
		mav.addObject("bank", bank);
		mav.addObject("order", order);
		mav.setViewName("pay/ebank/againconfirm");
		return mav;
	}
	
	
	/**
	 * 展现订单信息 再跳转至三方平台
	 * @param request
	 * @return
	 * @throws LoginUserNotFoundException 
	 */
	@RequestMapping(value="/ebank/order/pay")
	public ModelAndView adapterGateway(HttpServletRequest request,HttpServletResponse response) throws LoginUserNotFoundException{
		ModelAndView mav = new ModelAndView();
		this.getAccount(request, response);
		String bank = request.getParameter("bank");
		//获得适配对象
		Gateway config = gatewayService.getGateway(bank);
		if(config == null){
        	mav.addObject("msg", "您选择的银行暂未开通，请您选择其他银行");
			mav.setViewName("pay/ebank/error");
			return mav;
		}
		//判断使用谁家的第三方平台
		if(config.getThirdGateway().equals(ThirdPartyGateway.chinapnr)){//汇付天下
			mav = chinapnrController.sendOrder(request, response);
		} else if(config.getThirdGateway().equals(ThirdPartyGateway.allinpay)){//通联支付
			mav = allinpayController.sendOrder(request, response);
		} else {
        	mav.addObject("msg", "您选择的银行暂未开通，请您选择其他银行");
			mav.setViewName("pay/ebank/error");
			return mav;
		}
		
		return mav;
	}
	
	
	/**
	 * 完成订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pay/ebank/complete.do")
	public ModelAndView complete(HttpServletRequest request,HttpServletResponse response) {
		Account account = this.getAccount(request, response);
		ModelAndView mav = new ModelAndView();
		String OrdId = request.getParameter("OrdId");
//		Order order = payOrderService.getOrderByid(OrdId);// 查询当前订单的信息
//		AppOrder appOrder = appOrderService.getAppOrder(order.getAppOrderId(),order.getAppId());//TODO
		AppOrder appOrder = appOrderService.getAppOrder(OrdId);//TODO
		
		String cmdId = "Buy";
        String orderId = appOrder.getOrderId();            							//订单号
        String trxId = appOrder.getTrxId();											//支付平台唯一标识
        String appId = StringUtils.trimToEmpty(String.valueOf(appOrder.getAppId()));	//应用ID
        String pid = appOrder.getPid();												//商品ID
        String pname = appOrder.getPname();											//商品名称
        String merPriv = appOrder.getMerPriv();										//用户私有域
        String money = StringUtils.trimToEmpty(String.valueOf(appOrder.getMoney()));	//金额
        String status = StringUtils.trimToEmpty(String.valueOf(appOrder.getStatus()));	//支付状态，0表示成功，其它为失败
        String retType = "1";
        String chkValue = "";
        
        String merKeyFile = getClass().getResource("/security/MerPrk" + AppType.PAY + ".key").getPath();

        String merData = cmdId + orderId + trxId + appId + pid + pname + merPriv + money + status + retType;    //参数顺序不能错
        com.xinhuanet.pay.security.SecureLink link = new com.xinhuanet.pay.security.SecureLink();
        int ret = link.signMsg(140 + "", merKeyFile, merData);
        if (ret != 0) {
            logger.info("产生签名失败[" + merData + "]");
        } else{
        	chkValue = link.getChkValue();
        }
		mav.addObject("cmdId",cmdId);
		mav.addObject("retType",retType);
		mav.addObject("chkValue", chkValue);
		mav.addObject("appOrder", appOrder);
		mav.setViewName("pay/ebank/complete");
		return mav;
	}
}