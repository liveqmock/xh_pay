package com.xinhuanet.pay.gateway;
/**
 * 银行网关属性Entry
 * @author duanwc
 *
 */
public class BankGatewayEntry {
	
	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "BankGatewayProp [key=" + key + ", value=" + value + "]";
	}
	
}
