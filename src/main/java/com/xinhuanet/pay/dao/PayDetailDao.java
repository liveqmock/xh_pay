package com.xinhuanet.pay.dao;

import java.util.List;

import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.po.PayDetail;
import com.xinhuanet.pay.po.PayDetailAndRefundApply;

public interface PayDetailDao {
	
	/**
	 * 新增一条消费记录
	 * @param pg 消费记录对象
	 */
	public void addDetail(PayDetail pg);
	/**
	 * 获取消费记录
	 * @param uid 用户ID
	 * @param model 分页模型
	 * @return 消费记录列表
	 */
	public List<PayDetail> getDetailList(String uid,PageRollModel model);
	/**
	 * 获取用户的所有消费记录总数
	 * @param uid
	 * @return 消费记录总条数
	 */
	public int getDetailCount(String uid);
	
	/**
	 * 获取明细
	 * @param id
	 * @return
	 */
	public PayDetail get(int id);
	/**
	 * 获取订单明细与退款明细信息
	 * @param uid
	 * @param pageModel
	 * @return
	 */
	public List<PayDetailAndRefundApply> getDetailListAndRefundApply(String uid, PageRollModel pageModel);
}
