package com.xinhuanet.pay.util;

import java.security.MessageDigest;

public class Md5Util {
	public static String MD5Encode(String sourceString) {
		return MD5Encode(sourceString,null);
	}
	public static final String byte2hexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
	
	public static String MD5Encode(String sourceString,String charSet) {
		String resultString = null;
		try {
			resultString = new String(sourceString);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if(charSet==null){
				resultString = byte2hexString(md.digest(resultString.getBytes()));
			}else{
				resultString = byte2hexString(md.digest(resultString.getBytes(charSet)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	
	public static void main(String[] args) throws Exception {
//		System.out.println(MD5Encode("8888"));
//		System.out.println(MD5Encode("hello"));
//		System.out.println(MD5Encode("123456"));
//		System.out.println(MD5Encode("111111"));
	}
}
