package com.xinhuanet.pay.common;

public class RefundChangeState {
	/**
	 * 0- 默认值，通用
	 */
	public static final int DEFAULT = 0;
	
	//---------------订单表订单支付完成后，后续变更状态 start------------------
	/**
	 * 1- 订单状态，退款申请
	 */
	public static final int REFUND_APPLY = 1;
	/**
	 * 2- 订单状态，撤销退款申请
	 */
	public static final int REFUND_UNDO = 2;
	/**
	 * 3- 订单状态，拒绝退款
	 */
	public static final int REFUND_REFUSE = 3;
	/**
	 * 4- 订单状态，同意退款
	 */
	public static final int REFUND_AGREE = 4;
	/**
	 * 5- 订单状态，处理完成
	 */
	public static final int REFUND_COMPLETE = 5;
	
	
	
	//------------------角色定义--------------------------
	/**
	 * 0- 用户
	 */
	public static final int ROLE_USER = 0;
	/**
	 * 1- 应用管理员
	 */
	public static final int ROLE_APP_MANAGER = 1;
	/**
	 * 2- 财务人员
	 */
	public static final int ROLE_FINANCIAL = 2;
	
	
	//------------------退款发起定义--------------------------
	/**
	 * 0- 主动发起退款
	 */
	public static final int APPLY_ACTIVE = 0;
	/**
	 * 1- 被动发起退款，由管理进行退款操作
	 */
	public static final int APPLY_PASSIVE = 1;
	
	/**
	 * 转换代码到名称
	 * @param status
	 * @return
	 */
	public static String convertStatus(int status){
		String statusName = "";
		if(REFUND_APPLY == status){
			statusName = "退款申请";
		}else if(REFUND_UNDO == status){
			statusName = "撤销退款申请";
		}else if(REFUND_REFUSE == status){
			statusName = "拒绝退款";
		}else if(REFUND_AGREE == status){
			statusName = "同意退款";
		}else if(REFUND_COMPLETE == status){
			statusName = "处理完成";
		}else{
			statusName = "没有改状态";
		}
		return statusName;
	}
	
	/**
	 * 步骤代码转名称
	 * @param step
	 * @return
	 */
	public static String convertStep(int step){
		String stepName = "";
		if(ROLE_USER == step){
			stepName = "用户申请";
		}else if(ROLE_APP_MANAGER == step){
			stepName = "应用管理员审核";
		}else if(ROLE_FINANCIAL == step){
			stepName = "财务管理员审核";
		}else {
			stepName = "没有改步骤";
		}
		return stepName;
	}
}
