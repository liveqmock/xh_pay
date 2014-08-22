package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;


public class PayDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 系统ID
	 */
	private String id;
	/**
	 * 用户ID
	 */
	private String uid;
	/**
	 * 用户登录名称
	 */
	private String loginName;
	/**
	 * 商品ID
	 */
	private String pid;
	/**
	 * 商品名称
	 */
	private String pname;
	/**
	 * 类型，0-收入，1-支出
	 */
	private int type;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 金额
	 */
	private double money;
	/**
	 * 操作前金额
	 */
	private double beforeMoney;
	/**
	 * 操作后金额
	 */
	private double afterMoney;
	/**
	 * 交易类型(0-充值、1-即时交易、2-保证金)
	 */
	private int orderType;
	/**
	 * 状态（预留）
	 */
	private int status;
	/**
	 * 来自哪个应用
	 */
	private int appId;
	/**
	 * 来自哪个应用中文名称
	 */
	private String AppName;
	/**
	 * 定制时间
	 */
	private Date addTime;
	/**
	 * IP地址
	 */
	private String ipAddress;
	/**
	 * 备注信息
	 */
	private String ext;
	/**
	 * 币种
	 */
	private String curType;
	/**
	 * 币种名称
	 */
	private String curName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getBeforeMoney() {
		return beforeMoney;
	}
	public void setBeforeMoney(double beforeMoney) {
		this.beforeMoney = beforeMoney;
	}
	public double getAfterMoney() {
		return afterMoney;
	}
	public void setAfterMoney(double afterMoney) {
		this.afterMoney = afterMoney;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return AppName;
	}
	public void setAppName(String appName) {
		AppName = appName;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getCurType() {
		return curType;
	}
	public void setCurType(String curType) {
		this.curType = curType;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	@Override
	public String toString() {
		return "PayDetail [id=" + id + ", uid=" + uid + ", loginName="
				+ loginName + ", pid=" + pid + ", pname=" + pname + ", type="
				+ type + ", orderId=" + orderId + ", money=" + money
				+ ", beforeMoney=" + beforeMoney + ", afterMoney=" + afterMoney
				+ ", orderType=" + orderType + ", status=" + status
				+ ", appId=" + appId + ", AppName=" + AppName + ", addTime="
				+ addTime + ", ipAddress=" + ipAddress + ", ext=" + ext
				+ ", curType=" + curType + ", curName=" + curName + "]";
	}
}
