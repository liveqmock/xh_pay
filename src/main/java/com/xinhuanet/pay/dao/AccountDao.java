package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.Account;

public interface AccountDao {
	/**
	 * 查询当前用户帐户信息，记录条数小于1条和大于1条，返回null
	 * @param uid
	 * @return Account帐户实例
	 */
	public Account getAccount(String uid);
	/**
	 * 查询当前用户帐户信息，记录条数小于1条和大于1条，返回null
	 * @param uid 用户ID
	 * @param loginName 用户登录名称
	 * @return Account帐户实例
	 */
	public Account getAccount(String uid,String loginName);
	
	/**
	 * 更新用户保证金
	 * @param uid 用户ID
	 * @param money 充值金额
	 * @param lastOrderId 最后一次充值订单号
	 * @return
	 */
	public int addAccountDeposit(String uid,double money,String lastOrderId);
	
	/**
	 * 更新用户现金余额
	 * @param uid 用户ID
	 * @param money 充值金额
	 * @param lastOrderId 最后一次充值订单号
	 * @return
	 */
	public int addAcountCash(String uid,double money,String lastOrderId);
	
	/**
	 * 扣除账户金额
	 * @param uid 用户ID
	 * @param money 支付金额
	 * @return
	 */
	public int subAccountAmt(String uid,double money);
	/**
	 * 初始化一个用户
	 * @param uid
	 * @param loginName
	 * @return 更新记录条数，1表示成功，其它则表示失败
	 */
	public int initializeAccount(String uid,String loginName);
	
	/**
	 * 更新一个用户的邮箱认证和邮箱认证状态
	 * @param uid
	 * @param loginName
	 * @param emailID 用户邮箱号
	 * @return 更新记录条数，1表示成功，其它则表示失败
	 */
	public int updAccountEmail(String uid,String loginName,String emailID);
	
	/**
	 * 根据邮箱账号查看是否已被使用  0表示未使用  其他数字表示已已经被使用
	 * @param emailID
	 * @return
	 */
	public int queryAccountByEmail(String emailID);
}
