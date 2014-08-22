package com.xinhuanet.pay.dao;

import com.xinhuanet.pay.po.AppOrder;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-6-3
 * Time: 下午4:11
 */
public interface PayAppOrderDao {

    /**
     * 获取需要后台通知的订单
     * @return 订单列表
     */
    public List<AppOrder> getQuartzList();

    /**
     * 更新quartz状态
     * @param id
     * @param status
     * @return
     */
    public boolean updateQuartzStatus(String id, int status);

	
	/**
	 * 汇付响应成功后支付订单更新成功后 更新应用订单信息。
	 * @param aod
	 * @return
	 */
	public int updateAppOrderStatus(AppOrder aod);
	
	/**
	 * 生成一条应用订单
	 * @param AppOrder 初始化的应用订单对象
	 */
	public int insertAppOrder(AppOrder order);
	
	/**
	 * 根据id获取应用订单对象
	 * @param id
	 * @return
	 */
	public AppOrder getAppOrderById(String id);
	/**
	 * 根据appOrderId获取应用订单对象
	 * @param appOrderId
	 * @return
	 */
	public AppOrder getAppOrder(String appOrderId,int appId);
	
	/**
	 * 根据trxId(交易号)获取应用订单对象
	 * @param trxId
	 * @return
	 */
	public AppOrder getAppOrder(String trxId);
	
	/**
	 * 更新交易记录状态
	 * @param aod 应用订单属性，需应用订单ID 和 应用ID ，状态和时间
	 * @return
	 */
	public int updateTradeStatus(AppOrder aod);
	/**
	 * 更新应用订单时间
	 * @param aod 应用订单属性，需应用订单ID 和 应用ID及时间
	 * @return
	 */
	public int updateChangeTime(AppOrder aod);
}
