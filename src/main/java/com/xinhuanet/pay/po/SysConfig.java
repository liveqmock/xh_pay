package com.xinhuanet.pay.po;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ronny
 * Date: 13-11-20
 * Time: 上午9:33
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cfgKey;
    private String cfgValue;
    private String cfgDesc;       //配置项描述
    private Date updateTime;//更新时间
    private String ip;          //操作人
    private String userName;   //操作人
    private Integer status;   //状态0表示有效，1表示无效

    public String getCfgKey() {
        return cfgKey;
    }

    public void setCfgKey(String cfgKey) {
        this.cfgKey = cfgKey;
    }

    public String getCfgValue() {
        return cfgValue;
    }

    public void setCfgValue(String cfgValue) {
        this.cfgValue = cfgValue;
    }

    public String getCfgDesc() {
        return cfgDesc;
    }

    public void setCfgDesc(String cfgDesc) {
        this.cfgDesc = cfgDesc;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
