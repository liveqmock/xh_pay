package com.xinhuanet.pay.gateway;

public class ThirdPartyGateway {
	/**
	 * 汇付天下
	 */
	public static final String chinapnr = "chinapnr";
	public static final String chinapnr_name = "汇付天下";
	
	/**
	 * 通联支付
	 */
	public static final String allinpay = "allinpay";
	public static final String allinpay_name = "通联支付";
	
	/**
	 * 支付宝
	 */
	public static final String alipay = "alipay";
	public static final String alipay_name = "支付宝";
	
	/**
	 * 财付通
	 */
	public static final String  tenpay = "tenpay";
	public static final String tenpay_name = "财付通";
	
	/**
	 * 通过支付平台对第三方网关的定义，获取网关名称
	 * @param gateway 第三方网关的定义 @see ThirdPartyGateway
	 * @return 第三方支付平台的名称
	 */
	public static String getName(String gateway){
		String gatewayName = null;
		if(chinapnr.equals(gateway)){
			gatewayName = chinapnr_name;
		} else if(allinpay.equals(gateway)){
			gatewayName = allinpay_name;
		} else if(alipay.equals(gateway)){
			gatewayName = alipay_name;
		}else if(tenpay.equals(gateway)){
			gatewayName = tenpay_name;
		}
		return gatewayName;
	}
}
