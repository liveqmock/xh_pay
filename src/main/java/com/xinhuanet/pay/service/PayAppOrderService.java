package com.xinhuanet.pay.service;

import java.util.List;

import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-6-3
 * Time: 下午4:10
 */
public interface PayAppOrderService {
	
	/**
	 * 根据id获取应用订单对象
	 * @param id
	 * @return
	 */
	public AppOrder getAppOrderById(String id);
	/**
	 * 根据appOrderId和appId获取应用订单对象
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
	 * 支付平台返回成功通知，更新应用订单
	 * @param od
	 * @return
	 */
	public int succeedAppOrder(AppOrder appOrder,String orderId);
	/**
	 * 支付平台返回失败通知，更新应用订单
	 * @param aod
	 * @return
	 */
	public int failAppOrder(AppOrder appOrder,String orderId);
	
	/**
	 * 生成一条新应用订单信息
	 * @param order
	 * @return
	 */
	public int insertAppOrder(AppOrder order);

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
     * 后台通知
     * @param payAppOrder
     */
    public void backgroundNotice(AppOrder payAppOrder);

    /**
     * 解析后台通知结果，验证是否通知成功
     * @param id
     * @param result
     * @return
     */
    public boolean parseResponse(String id, String result);
    
	/**
	 * 获取用户的所有的交易记录（应用订单）列表
	 * @param uid 用户ID
	 * @param model 分页模型
	 * @return
	 */
	public List<Order> getTradingRecordList(String uid,PageRollModel model);
	/**
	 * 获取用户的所有的交易记录（应用订单）列表总数
	 * @param uid
	 * @return
	 */
	public int getTradingRecordCount(String uid);
}
