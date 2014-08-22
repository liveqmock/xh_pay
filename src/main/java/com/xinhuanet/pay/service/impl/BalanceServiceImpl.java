package com.xinhuanet.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.po.Account;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.service.BalanceService;
import com.xinhuanet.pay.service.AccountService;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.pay.service.PayDetailService;
import com.xinhuanet.pay.util.Arith;
import com.xinhuanet.platform.base.AppType;

@Component
public class BalanceServiceImpl implements BalanceService {
    private static final Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);
    
	/**
	 * 账户服务
	 */
	private @Autowired
	AccountService accountService;
	
	/**
	 * app订单服务
	 */
	private @Autowired PayAppOrderService appOrderService;

	/**
	 * 收支明细
	 */
	private @Autowired PayDetailService detailService;

	@Override
	public Boolean balancePaying(AppOrder appOrder) {
		if(appOrder.getStatus() == 1){ //如果订单状态成功
			return true;
		} else if(appOrder.getStatus() == 2){//如果订单状态失败
			return false;
		}
		/**
		 * 当订单为新增加时再去进行操作相关数据
		 */
		int i = accountService.subAccountAmt(appOrder.getUid(),appOrder.getMoney());//扣除账户金额
		if (i > 0) {// 扣款成功
			// 更新应用订单表
			appOrderService.succeedAppOrder(appOrder, appOrder.getAppId() + appOrder.getOrderId());
			logger.info("应用订单信息更新成功！新增订单：" + appOrder);

			// TODO 操作 用户收支明细
			Account account = accountService.getAccount(appOrder.getUid());
			
			PayDetail detail = new PayDetail();
			detail.setUid(appOrder.getUid());
			detail.setLoginName(appOrder.getLoginName());
			detail.setAppId(appOrder.getAppId());
			detail.setAppName(AppType.getAppName(appOrder.getAppId()));
			detail.setMoney(appOrder.getMoney());
			detail.setPid(appOrder.getPid());
			detail.setPname(appOrder.getPname());
			detail.setOrderId(appOrder.getAppId() + appOrder.getId());
			detail.setOrderType(appOrder.getOrderType());

			// TODO 明细内增加交易类型
			if (appOrder.getOrderType() == 1) {// 即时交易
				
				detail.setType(0);
				detail.setOrderType(0);
				detail.setBeforeMoney(account.getMoney());
				detail.setAfterMoney(Arith.add(appOrder.getMoney(), account.getMoney()));//设置余额
				detailService.addDetail(detail);
				
				detail.setType(1);
				detail.setOrderType(1);
				detail.setBeforeMoney(Arith.add(appOrder.getMoney(), account.getMoney()));
				detail.setAfterMoney(account.getMoney());//扣除金额
				detailService.addDetail(detail);
				
				
//				detail.setType(0);
//				detail.setBeforeMoney(account.getMoney());
//				detail.setAfterMoney(account.getMoney());//设置余额
//				detailService.addDetail(detail);
			} else if (appOrder.getOrderType() == 2) {// 保证金交易
				
				detail.setType(0);
				detail.setOrderType(0);
				detail.setBeforeMoney(account.getMoney());
				detail.setAfterMoney(Arith.add(appOrder.getMoney(), account.getMoney()));//设置余额
				detailService.addDetail(detail);
				
				detail.setType(1);
				detail.setOrderType(2);
				detail.setPname("缴纳保证金");
				detail.setBeforeMoney(Arith.add(appOrder.getMoney(), account.getMoney()));
				detail.setAfterMoney(account.getMoney());//扣除金额
				detailService.addDetail(detail);
				
				
//				detail.setType(1);
//				detail.setBeforeMoney(account.getMoney());
//				detail.setAfterMoney(account.getMoney());//设置余额
//				detailService.addDetail(detail);
			}
			return true;
		} else {
			appOrderService.failAppOrder(appOrder, appOrder.getAppId() + appOrder.getId());
			return false;
		}
	}
}
