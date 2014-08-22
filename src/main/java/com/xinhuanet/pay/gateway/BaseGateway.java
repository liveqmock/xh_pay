package com.xinhuanet.pay.gateway;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xinhuanet.pay.common.Base;

public abstract class BaseGateway<T> extends Base {
	/**
	 * 已注册的第三方平台网关对象
	 */
	protected static ConcurrentHashMap<String,BaseGateway<?>> baseGatewayMap = new ConcurrentHashMap<String, BaseGateway<?>>();
	/**
	 * 第三方支付平台网关对象初始化
	 * @param t 第三方支付平台网关对象
	 */
	protected abstract void init(T t);
	/**
	 * 注册第三方支付平台网关对象
	 * @param key 第三方网关名称，如：chinapnr,allinpay,alipay.....
	 * @param t 第三方网关对象
	 */
	protected void attach(String key, T t){
		baseGatewayMap.put(key, (BaseGateway<?>)t);
	}
	/**
	 * 获取第三方平台所有支持的银行网关属性集合
	 * @return 第三方平台所有支持的银行网关属性集合，该对象结果为一个不可修改的map视图
	 */
	public abstract Map<String,Object> getConfigMap();
	/**
	 * 银行网关配置对象
	 * @param gateway
	 * @param cardName
	 * @return
	 */
	public BankGatewayEntry getBankValue(String gateway,String cardName){
		BankGatewayEntry bankValue = new BankGatewayEntry();
		bankValue.setKey(gateway);
		bankValue.setValue(cardName);
		return bankValue;
	}
}
