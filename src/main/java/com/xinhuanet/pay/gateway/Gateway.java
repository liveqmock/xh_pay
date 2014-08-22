package com.xinhuanet.pay.gateway;

import java.io.Serializable;
import java.util.Map;
/**
 * 网关相关的枚举管理类
 * @author duanwc
 *
 */
public enum Gateway implements Serializable  {
	/**
	 * 适配网关的对象
	 */
	adapterGateeay();
	/**
	 * 初始化第三方平台配置
	 */
	private Gateway() {
	}
	/**
	 * 第三方支付平台支持网关配置
	 */
	private Map<String,Object> gatewayConfigMap;
	/**
	 * 银行卡名称
	 */
	private String bankCardName;
	/**
	 * 第三方支付平台网关
	 */
	private String thirdGateway;
	/**
	 * 银行网关
	 */
	private String gateway;
	
	public Map<String, Object> getGatewayConfigMap() {
		return gatewayConfigMap;
	}
	public void setGatewayConfigMap(Map<String, Object> gatewayConfigMap) {
		this.gatewayConfigMap = gatewayConfigMap;
	}
	public String getBankCardName() {
		return bankCardName;
	}
	public void setBankCardName(String bankCardName) {
		this.bankCardName = bankCardName;
	}
	public String getThirdGateway() {
		return thirdGateway;
	}
	public void setThirdGateway(String thirdGateway) {
		this.thirdGateway = thirdGateway;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
}