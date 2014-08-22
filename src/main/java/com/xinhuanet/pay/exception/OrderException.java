package com.xinhuanet.pay.exception;

public class OrderException extends Exception {

	private static final long serialVersionUID = -2355024441848818958L;

	public OrderException(String msg) {
		super(msg);
	}
	
	public OrderException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
