package com.xinhuanet.pay.exception;

public class LoginUserNotFoundException extends Exception {
	
	/**
	 * 用户未登录异常
	 */
	private static final long serialVersionUID = 1L;

	public LoginUserNotFoundException(String msg) {
		super(msg);
	}
	
	public LoginUserNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
