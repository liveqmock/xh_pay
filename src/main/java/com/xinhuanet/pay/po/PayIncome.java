package com.xinhuanet.pay.po;

import java.util.Date;

/**
 * 汇总信息po
 * @author wangwei
 *
 */
public class PayIncome {
	public String id;//id
	public int appId;//应用id
	public double money;//交易金额
	public String payflatform;//支付平台
	public Date payTime;//支付时间
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getPayflatform() {
		return payflatform;
	}
	public void setPayflatform(String payflatform) {
		this.payflatform = payflatform;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

}
