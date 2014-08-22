package com.xinhuanet.pay.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xinhuanet.pay.common.RefundChangeState;
import com.xinhuanet.pay.exception.AccountUnmatchErrorException;
import com.xinhuanet.pay.exception.OrderException;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.po.RefundApply;
import com.xinhuanet.pay.po.RefundStep;
import com.xinhuanet.pay.service.PayOrderService;
import com.xinhuanet.pay.service.RefundApplyService;
import com.xinhuanet.pay.service.RefundOrderService;
import com.xinhuanet.pay.service.RefundStepService;

@Component
public class RefundOrderServiceImpl implements RefundOrderService {
	@Autowired
	public RefundApplyService refundService;
	
	@Autowired
	public PayOrderService orderService;
	
	@Autowired
	private RefundStepService refundStepService;


	@Override
	public int add(String uid,RefundApply refApply) throws AccountUnmatchErrorException,OrderException {
		Order order = orderService.getOrderById(refApply.getOrderId());
		System.out.println(order);
		if(order == null){
			throw new OrderException("订单" + refApply.getOrderId() + "不存在");
		}
		
		if(!uid.equals(order.getUid())){
			throw new AccountUnmatchErrorException("当前用户与订单用户信息不匹配");
		}
        
		// 原订单需要时支付类型
		if ("0".equals(order.getTransType())){
			throw new AccountUnmatchErrorException("原订单支付类型异常");
		}
		
		// 原订单支付成功
		if ("1".equals(order.getPayStatus())){
			throw new AccountUnmatchErrorException("原支付订单未成功");
		}
		
		// TODO 原订单没有退货
		
		
		refApply.setOrderId(order.getId());//当前退款的原订单ID
        refApply.setUid(order.getUid());//用户ID
		refApply.setLoginName(order.getLoginName());//用户登录名称
		refApply.setTrxId(order.getTrxId());//交易号，来源于第三方交易平台
		refApply.setAppId(order.getAppId()); //应用ID
		refApply.setMoney(order.getMoney());//充值金额
		refApply.setPayType(order.getPayType());//第三方网关（支付宝-alipay、通联支付-allinpay、汇付天下-chinapnr）
		refApply.setStatus(RefundChangeState.REFUND_APPLY);//流程状态，1-退款申请,2-拒绝退款,3-同意退款,4-处理完成
		refApply.setStep(RefundChangeState.ROLE_USER);//处理完成状态,0-用户处理完成，1-应用管理员处理完成，2-财务处理完成
		refApply.setApply(RefundChangeState.APPLY_ACTIVE);//主动申请或被动申请(主动-0，被动-1)
		refApply.setHandleTime(new Date());//申请时间
		
		int i = refundService.add(refApply);
		return i;
	}

	@Override
	public int add(RefundApply refundApply, RefundStep refundStep) {
		int i = refundService.add(refundApply);
		int j = refundStepService.add(refundStep);
		return i+j>1?1: 0;
	}

	@Override
	public int update(RefundApply refundApply, RefundStep refundStep) {
		int i = refundService.update(refundApply);
		int j = refundStepService.add(refundStep);
		return i+j>1?1: 0;
	}
	
}