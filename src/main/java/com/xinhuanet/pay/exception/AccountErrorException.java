package com.xinhuanet.pay.exception;

public class AccountErrorException extends Exception {

	private static final long serialVersionUID = -2355024441848818958L;

	public AccountErrorException(String msg) {
		super(msg);
	}
	
	public AccountErrorException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
