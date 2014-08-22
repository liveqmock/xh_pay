package com.xinhuanet.pay.security;


/**
 * User: 李野
 * @version 1.0
 */
public class SecureLink {

    private String chkValue = "";
    private PrivateKey privateKey = new PrivateKey();
    private final String encode = "UTF-8";

    public String getChkValue() {
        return chkValue;
    }


    public int signMsg(String appId,String appKeyFile, String msgData){
        boolean bool = this.privateKey.buildKey(appId,appKeyFile);
        if (!bool){
            System.out.println("build key error!");
            return -101;
        }
        try {
            // 产生签名
            chkValue = RSAUtils.sign(msgData.getBytes(encode), privateKey.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return -102;
        }

        return 0;
    }

    public int veriSignMsg(String appKeyFile,String msgData,String chkValue){
        boolean bool = this.privateKey.buildKey("888888", appKeyFile);
        if (!bool){
            System.out.println("build key error!");
            return -112;
        }
        // 验证签名
        boolean status = false;
        try {
        	byte[] data = msgData.getBytes(encode);
            status = RSAUtils.verify(data, privateKey.getKey(), chkValue);
        } catch (Exception e) {
            e.printStackTrace();
            return -113;
        }

        return status ? 0 : -301;
    }
}
