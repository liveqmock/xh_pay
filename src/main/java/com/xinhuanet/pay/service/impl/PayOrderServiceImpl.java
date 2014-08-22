package com.xinhuanet.pay.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.cache.CacheName;
import com.xinhuanet.pay.cache.redis.RedisCacheMan;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.dao.PayOrderDao;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.util.OrderUtil;

@Component
public class PayOrderServiceImpl implements PayOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PayAppOrderServiceImpl.class);
    
	private @Autowired PayOrderDao dao;
	
    private @Autowired RedisCacheMan cache;

	@Override
	public List<Order> getOrderList(String uid,PageRollModel model) {
		return dao.getOrderList(uid,model);
	}

	@Override
	public int getOrderCount(String uid) {
		return dao.getOrderCount(uid);
	}

	@Override
	public String getOrderIdNumber() {
		String newOrders = "";
		synchronized (PayOrderServiceImpl.class) {
			// 生成新的订单号
			newOrders = OrderUtil.createOrderNumber();
		}
		return newOrders;
	}

	@Override
	public int addOrder(Order order) {
		Object object = cache.setObject(CacheName.ORDER, order.getId(), order, 3 * 60);
		if(object == null){
			logger.error("loginName："+order.getLoginName()+",网银订单添加缓存失败,缓存key:"+CacheName.ORDER + order.getId());
		}
		return dao.addOrder(order);
	}

	@Override
	public Order getOrderById(String orderId) {
		Order order = (Order)cache.getObject(CacheName.ORDER, orderId, 3 * 60);
		return order;
	}

	@Override
	public List<Order> getOrderByOldOrdIdList(String oldOrdId) {
		return dao.getOrderByOldOrdIdList(oldOrdId);
	}

	@Override
	public int succeedOrder(Order order) {
		order.setChangeTime(new Date());
		order.setPayStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
		int i = dao.updateOrderStatus(order);
		if(i == 1){
			Object object = cache.setObject(CacheName.ORDER, order.getId(), order, 3 * 60);
			if(object == null){
				logger.error("loginName："+order.getLoginName()+",网银订单更新缓存失败,缓存key:"+CacheName.ORDER + order.getId());
			}
		}
		return i;
	}

	@Override
	public int failOrder(Order order) {
		order.setChangeTime(new Date());
		order.setPayStatus(OrderStatus.ORDER_STATUS_FAILED);// 将订单状态改为失败
		int i = dao.updateOrderStatus(order);
		if(i == 1){
			Object object = cache.setObject(CacheName.ORDER, order.getId(), order, 3 * 60);
			if(object == null){
				logger.error("loginName："+order.getLoginName()+",网银订单更新缓存失败,缓存key:"+CacheName.ORDER + order.getId());
			}
		}
		return i;
	}

}
