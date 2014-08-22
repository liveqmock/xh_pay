package com.xinhuanet.pay.service.impl;

import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.gateway.EBank;
import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.po.PayIncome;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.HttpClient4Service;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayDetailService;
import com.xinhuanet.pay.service.PayIncomeService;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.ThridPartyService;
import com.xinhuanet.pay.util.Arith;
import com.xinhuanet.platform.base.AppType;

@Component
public class ThridPartyServiceImpl<T> implements ThridPartyService<T> {
    private static final Logger logger = LoggerFactory.getLogger(ThridPartyServiceImpl.class);
    
	/**
	 * 账户服务
	 */
	private @Autowired
	AccountService accountService;
	/**
	 * 订单服务
	 */
	private @Autowired
	PayOrderService payOrderService;
	
	/**
	 * app订单服务
	 */
	private @Autowired PayAppOrderService appOrderService;
	
	/**
	 * 汇总服务
	 */
	private @Autowired PayIncomeService piService;
	/**
	 * 收支明细
	 */
	private @Autowired PayDetailService detailService;
	/**
	 * 读取配置文件服务
	 */
	protected @Autowired PropertiesConfiguration props;
	/**
	 * HttpClient服务
	 */
	protected @Autowired HttpClient4Service clientService;

	@Override
	public void thridOrdFailedNotify(Order order, AppOrder appOrder) {
		// 根据订单号 进行相应业务操作
		// 验证是否需要修改数据库,需要更改则更新订单状态
		if (EBank.isOperate(order.getPayStatus(), false)) {
			// 更新订单表
			order.setTrxId(order.getTrxId());//设置第三方平台的交易号
			payOrderService.failOrder(order);
			// 更新应用订单表
			appOrderService.failAppOrder(appOrder,order.getId());
		}
	}

	@Override
	public void thridOrdSucceedNotify(Order order, AppOrder appOrder) {
		PayIncome pi = new PayIncome();
		pi.setId(order.getId());
		pi.setAppId(order.getAppId());
		pi.setMoney(order.getMoney());
		pi.setPayflatform(order.getPayType());
		pi.setPayTime(new Date());
		// 更新汇总表数据
		piService.addIncome(pi);
		logger.info("汇总表插入数据已更新！");
		

		// 更新订单表
		order.setTrxId(order.getTrxId());//设置第三方平台的交易号
		payOrderService.succeedOrder(order);
		logger.info("订单信息更新成功！新增订单：" + order);

		// 更新应用订单表
		appOrderService.succeedAppOrder(appOrder,order.getId());
		logger.info("应用订单信息更新成功！新增订单：" + appOrder);
		
		// TODO 更新用户余额 先加再减
		// 更新用户余额
		if(order.getOrderType() == 0){ //账户充值
			accountService.addAccountCash(order.getUid(), order.getMoney(), order.getId());
			logger.info("订单id:"+order.getId()+"  账户充值:"+order.getMoney());
		} else if(order.getOrderType() == 2){ //缴纳保证金
			accountService.addAccountDeposit(order.getUid(), order.getMoney(), order.getId());
			logger.info("订单id:"+order.getId()+"  缴纳保证金:"+order.getMoney());
		}
		
		// TODO 操作 用户收支明细
		Account account = accountService.getAccount(order.getUid());
		PayDetail detail = new PayDetail();
		
		detail.setUid(order.getUid());
		detail.setLoginName(order.getLoginName());
		detail.setAppId(order.getAppId());
		detail.setAppName(AppType.getAppName(order.getAppId()));
		detail.setMoney(order.getMoney());
		detail.setPid(order.getPid());
		detail.setPname(appOrder.getPname());
		detail.setOrderId(order.getId());
		detail.setOrderType(order.getOrderType());
		
		// TODO  明细内增加交易类型
		if(order.getOrderType() == 0){ //充值
			detail.setType(0);
			detail.setBeforeMoney(account.getMoney());
			detail.setAfterMoney(Arith.add(order.getMoney(), account.getMoney()));//
			detailService.addDetail(detail);
		} else if(order.getOrderType() == 1){//即时交易
			detail.setType(0);
			detail.setOrderType(0);
			detail.setBeforeMoney(account.getMoney());
			detail.setAfterMoney(Arith.add(order.getMoney(), account.getMoney()));//设置余额
			detailService.addDetail(detail);
			
			detail.setType(1);
			detail.setOrderType(1);
			detail.setBeforeMoney(Arith.add(order.getMoney(), account.getMoney()));
			detail.setAfterMoney(account.getMoney());//扣除金额
			detailService.addDetail(detail);
			
//			detail.setType(0);
//			detail.setBeforeMoney(account.getMoney());
//			detail.setAfterMoney(account.getMoney());//设置余额
//			detailService.addDetail(detail);
			
		} else if(order.getOrderType() == 2){//保证金交易
			detail.setType(0);
			detail.setOrderType(0);
			detail.setBeforeMoney(account.getMoney());
			detail.setAfterMoney(Arith.add(order.getMoney(), account.getMoney()));//设置余额
			detailService.addDetail(detail);
			
			detail.setType(1);
			detail.setOrderType(2);
			detail.setPname("缴纳保证金");
			detail.setBeforeMoney(Arith.add(order.getMoney(), account.getMoney()));
			detail.setAfterMoney(account.getMoney());//扣除金额
			detailService.addDetail(detail);
			
//			detail.setType(1);
//			detail.setBeforeMoney(account.getMoney());
//			detail.setAfterMoney(account.getMoney());//设置余额
//			detailService.addDetail(detail);
		}
	}

	@Override
	public T queryOrder(String ordId) {
		// TODO 不同的第三方平台，需对应实现
		return null;
	}

	@Override
	public T refundOrder(String oldOrdId) {
		// TODO 不同的第三方平台，需对应实现
		return null;
	}
	
}
