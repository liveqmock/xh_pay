package com.xinhuanet.pay.po;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-15
 * Time: 下午3:40
 */
public class KeyGen {
    private String id;
    private String publicKey;
    private String privateKey;
    private Date addTime;
    private Date updateTime;
    private Integer status;  //0启用，1停用

    public KeyGen(){}

    public KeyGen(String id,String publicKey,String privateKey){
        this.id = id;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "KeyGen [id=" + id + ", publicKey=" + publicKey
				+ ", privateKey=" + privateKey + ", addTime=" + addTime
				+ ", updateTime=" + updateTime + ", status=" + status + "]";
	}
    
}
