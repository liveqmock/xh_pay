package com.xinhuanet.pay.action;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.news.xhsso.sessionutils.XSession;
import cn.news.xhsso.sessionutils.XSessionUtils;

import com.xinhuanet.pay.common.BaseController;
import com.xinhuanet.pay.common.Version;
import com.xinhuanet.pay.exception.LoginUserNotFoundException;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.KeyGen;
import com.xinhuanet.pay.service.KeyGenService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.util.Function;
import com.xinhuanet.pay.util.HttpUtil;
import com.xinhuanet.pay.util.UUID;

/**
 * @author duanwc
 * 用于支付适配器，为用户选择适合的支付方式
 */
@Controller
public class AdapterController extends BaseController{
	
	/**
	 * app订单服务
	 */
	private @Autowired PayAppOrderService appOrderService;
	
	private @Autowired KeyGenService keyGenService;
	
	/**
	 * 查询账户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/adapter/pay")
	public ModelAndView accountCharge(HttpServletRequest request,HttpServletResponse response) {
		
		String errorRedirectUrl = "pay/ebank/error"; //wap错误信息提示
		String successRedirectUrl = "wap/alipay"; //wap页面支付选择
		
		if(HttpUtil.isWAP(request)){
			errorRedirectUrl = "wap/error"; //wap错误信息提示
		}
		
		//获取账户信息
		Account account = this.getAccount(request,response);
		ModelAndView mav = new ModelAndView();
		String version = request.getParameter("version");
		String cmdId = request.getParameter("cmdId");	//消息类型
		String money = request.getParameter("money");
		String appId = request.getParameter("appId");
		String appOrderId = request.getParameter("orderId");	//应用订单号
		String pid = request.getParameter("pid");
		String pname = request.getParameter("pname");
		String merPriv = request.getParameter("merPriv");
		String appRetUrl = request.getParameter("appRetUrl");
		String appBgRetUrl = request.getParameter("appBgRetUrl");
		String orderType = request.getParameter("orderType");
		String orderTime = request.getParameter("orderTime");
		String chkValue = request.getParameter("chkValue");
		
		String checkMessage = checkParam(version, cmdId, money, appId, appOrderId, pid, pname, merPriv, appRetUrl,
				appBgRetUrl, orderType, orderTime, chkValue);
		if (!"success".equals(checkMessage)) {
			mav.addObject("msg", checkMessage);
			mav.setViewName(errorRedirectUrl);
			logger.info("提交应用订单,参数验证错误:"+"xhId:" + account.getUid() + ",loginName:"
					+ account.getLoginName() + ",应用ID:" + appId + ",订单号:"
					+ appOrderId);
			return mav;
		}
		
		com.xinhuanet.pay.security.SecureLink link = new com.xinhuanet.pay.security.SecureLink();
        String MerData = version + cmdId + money + appId + appOrderId + pid + pname + merPriv + appRetUrl + appBgRetUrl + orderType + orderTime;
        System.out.println(MerData);
        URL keyFile = getClass().getResource("/security/PgPubk"+appId+".key");
        System.out.println("keyFile:"+keyFile);
        
        if(keyFile==null){
        	mav.addObject("msg", "不存在的应用");
			mav.setViewName(errorRedirectUrl);
			return mav;
        }
        
        KeyGen keyGen = keyGenService.getKey(appId);
        if(keyGen.getStatus() == 1){
        	mav.addObject("msg", "暂时不支持该应用，请联系管理员");
			mav.setViewName(errorRedirectUrl);
			return mav;
        }
        
        int i =  link.veriSignMsg(keyFile.getPath(), MerData, chkValue);
        if(i!=0){
        	mav.addObject("msg", "签名异常，验证失败");
			mav.setViewName(errorRedirectUrl);
			return mav;
        }
        
        String uid;
        String loginName;
		try {
			XSession session=XSessionUtils.getSession(request, response);
			
			uid = this.getUserId(request,response);
			loginName = this.getLoginName(request,response);
			
			String stoken = (String) session.getAttribute(appId + "_submitAppOrderId");
			if (stoken == null || !stoken.equals(appOrderId)) {
				session.setAttribute(appId + "_submitAppOrderId", appOrderId);
			} else {
				mav.addObject("msg", "订单重复，请勿重复提交！");
				mav.setViewName(errorRedirectUrl);
				return mav;
			}
//			由第三方应用发送请求
//			获取订单信息。生成应用订单基本信息，再选择银行
			// 初始化一条订单
			AppOrder order = new AppOrder();
			String uuid = UUID.create();
			order.setId(uuid);// 组成订单号
			order.setUid(uid);
			order.setLoginName(loginName);
			order.setMoney(Double.parseDouble(money));
			order.setRetUrl(appRetUrl);//应用前台返回地址
			order.setBgRetUrl(appBgRetUrl);//应用后台返回地址
			order.setIpAddress(HttpUtil.getIpAddr(request));
			order.setVersion(version);//版本号
			order.setAppId(Integer.parseInt(appId));
			order.setOrderId(appOrderId);
			order.setStatus(0);// 初始状态为0
			order.setAddTime(Function.getDateTime());// 支付时间默认为系统当前时间（需要添加个创建时间字段）
			order.setPid(pid);
			order.setPname(pname);
			order.setMerPriv(merPriv);
			order.setOrderType(Integer.parseInt(orderType));
			order.setOrderTime(Function.getDateTime(orderTime));
			int rs = appOrderService.insertAppOrder(order);
			
			if(rs>0){
				mav.addObject("id", uuid);
				mav.setView(new RedirectView(request.getContextPath()
						+ "/ebank/choose"));
			}else{
				mav.addObject("msg", "由于交易产生异常订单处理失败，请您稍后重试！");
				mav.setViewName(errorRedirectUrl);
				return mav;
			}
		} catch (LoginUserNotFoundException e) {
			e.printStackTrace();
		}
		logger.info("xhId:" + account.getUid() + ",loginName:"
				+ account.getLoginName() + ",提交应用订单,应用ID:" + appId + ",订单号:"
				+ appOrderId);
		return mav;
	}
	/**
	 * 验证提交参数是否符合要求
	 * @param version 版本号
	 * @param cmdId 消息类型
	 * @param money 金额
	 * @param appId 应用ID
	 * @param appOrderId 订单号
	 * @param pid 商品ID
	 * @param pname 商品名称
	 * @param merPriv 私有域
	 * @param appRetUrl 前台响应地址
	 * @param appBgRetUrl 后台通知地址
	 * @param orderType 订单类型
	 * @param orderTime 订单提交时间
	 * @param chkValue 签名
	 * @return 如不符合要求，则返回相应的错误提示信息；全通过返回success
	 */
	private static String checkParam(String version, String cmdId, String money, String appId,
			String appOrderId, String pid, String pname, String merPriv,
			String appRetUrl, String appBgRetUrl, String orderType, String orderTime, 
			String chkValue){
		String res = "success";
		if (!Function.isNumber2Decimal(money)) {// 金额验证
			res = "金额必须保留两位小数，整数时用0补齐，money:" + money + ",不符合规范";
		} else if (StringUtils.isBlank(appId)) {
			res = "应用ID为必须项，appId:" + appId + ",不符合规范";
		} else if (StringUtils.isBlank(appOrderId)) {
			res = "订单号为必须项，orderId:" + appOrderId + ",不符合规范";
		} else if (!Function.maxLength(appOrderId, 20)) {
			res = "订单号长度不大于20，orderId:" + appOrderId + ",不符合规范";
		} else if (!Version.CURRENT_VERSION.equals(version)) {
			res = "固定版本号"+Version.CURRENT_VERSION+"，version:" + version + ",不符合规范";
		} else if (StringUtils.isBlank(cmdId)) {
			res = "消息类型为必须项，cmdId:" + cmdId + ",不符合规范";
		} else if (StringUtils.isBlank(orderTime)) {
			res = "订单提交时间必须项，orderTime:" + orderTime + ",不符合规范";
		} else if (!Function.isValidDate(orderTime)) {
			res = "订单提交时间格式不正确，orderTime:" + orderTime + ",不符合规范";
		}
//		else if (StringUtils.isBlank(appRetUrl)) {
//			res = "前台响应url地址为必须项，appRetUrl:" + appRetUrl + ",不符合规范";
//		} else if (StringUtils.isBlank(appBgRetUrl)) {
//			res = "后台通知url地址为必须项，appBgRetUrl:" + appBgRetUrl + ",不符合规范";
//		}
		else if (!NumberUtils.isDigits(orderType)) {
			res = "订单类型必须为正整数，orderType:" + orderType + ",不符合规范";
		} else if (StringUtils.isBlank(chkValue)) {
			res = "签名为必须项，chkValue:" + chkValue + ",不符合规范";
		} 
		return res;
	}
	
//	public static void main(String args[]){
//		String sDt = "2012-05-06 15:32:15";
//		System.out.println(Function.isValidDate(sDt));
//		
//		String sDt1 = "ss";
//		System.out.println(Function.isValidDate(sDt1));
//	}
}
