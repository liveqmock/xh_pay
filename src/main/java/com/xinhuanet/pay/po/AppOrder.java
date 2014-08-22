package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2013-5-31 
 * @author wangwei
 *
 */
public class AppOrder implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 支付系统订单号
     */
    private String id;
    /**
     * 应用订单号
     */
    private String orderId;
    /**
     * 交易号，对应订单表(Pay_Orders)的订单号，成功后进行更新
     */
    private String trxId;
    /**
     * 用户ID
     */
    private String uid;
    /**
     * 用户登录名称
     */
    private String loginName;
    /**
     * 应用ID
     */
    private int appId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 商品ID
     */
    private String pid;
    /**
     * 商品名称
     */
    private String pname;
    /**
     * 应用私有域
     */
    private String merPriv;
    /**
     * 金额
     */
    private double money;
    /**
     * 后台通知地址
     */
    private String bgRetUrl;
    /**
     * 前台通知地址
     */
    private String retUrl;
    /**
     * 订单状态，0-新增，1-成功，2-失败
     */
    private int status;
    /**
     * 重复通知状态 0-新增 1-成功 2-失败
     */
    private int quartzStatus;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 创建时间
     */
    private Date addTime;
    /**
     * 备注信息
     */
    private String ext;
	/**
	 * 订单类型(0-充值、1-即时交易、2-保证金)
	 */
	private int orderType;
	/**
	 * 交易类型 (0-支付、1-退款)
	 */
	private Integer transType;
	/**
	 * 订单变更时间
	 */
	private Date changeTime;
	
	/**
	 * 订单支付完成后交易状态，后续状态：如退款等
	 */
	private int tradeStatus;
	/**
	 * 版本号，目前为10
	 */
	private String version;
	/**
	 * 应用订单提交时间
	 */
	private Date orderTime;
	/**
	 * 订单处理状态 //TODO 未实现
	 */
	private Integer procStatus;
	/**
	 * 返回类型：1-页面返回方式，2-后台数据流返回方式
	 */
	private Integer retType;
	/**
	 * 处理结果原因
	 */
	private String message;
	/**
	 * 签名
	 */
	private String checkValue;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
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
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getBgRetUrl() {
		return bgRetUrl;
	}
	public void setBgRetUrl(String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}
	public String getRetUrl() {
		return retUrl;
	}
	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getQuartzStatus() {
		return quartzStatus;
	}
	public void setQuartzStatus(int quartzStatus) {
		this.quartzStatus = quartzStatus;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getTrxId() {
		return trxId;
	}
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getProcStatus() {
		return procStatus;
	}
	public void setProcStatus(Integer procStatus) {
		this.procStatus = procStatus;
	}
	public Integer getRetType() {
		return retType;
	}
	public void setRetType(Integer retType) {
		this.retType = retType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCheckValue() {
		return checkValue;
	}
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
	public int getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	@Override
	public String toString() {
		return "AppOrder [id=" + id + ", orderId=" + orderId + ", trxId="
				+ trxId + ", uid=" + uid + ", loginName=" + loginName
				+ ", appId=" + appId + ", pid=" + pid + ", pname=" + pname
				+ ", merPriv=" + merPriv + ", money=" + money + ", bgRetUrl="
				+ bgRetUrl + ", retUrl=" + retUrl + ", status=" + status
				+ ", quartzStatus=" + quartzStatus + ", ipAddress=" + ipAddress
				+ ", addTime=" + addTime + ", ext=" + ext + ", orderType="
				+ orderType + ", transType=" + transType + ", version="
				+ version + ", orderTime=" + orderTime + ", procStatus="
				+ procStatus + ", retType=" + retType + ", message=" + message
				+ ", checkValue=" + checkValue + "]";
	}
}
