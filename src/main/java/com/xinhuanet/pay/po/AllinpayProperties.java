package com.xinhuanet.pay.po;

import java.io.Serializable;

public class AllinpayProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 字符集	2	可为空	默认填1，固定选择值：1、2、3；1代表UTF-8；2代表GBK；3代表GB2312；
	 */
	private Integer inputCharset = -1;
	/**
	 * 付款客户的取货url地址	100	可为空	客户的取货地址，pickupUrl和receiveUrl两个参数不能同时为空，但建议两个地址都填写
	 */
	private String pickupUrl;
	/**
	 * 服务器接受支付结果的后台地址	256	可为空	通知商户网站支付结果的url地址，pickupUrl和receiveUrl两个参数不能同时为空，但建议两个地址都填写
	 */
	private String receiveUrl;
	/**
	 * 网关接收支付请求接口版本	10	不可空	"固定选择值：v1.0、v2.0；注意为小写字母，无特殊需求请一律填写v1.0
	 * 该字段决定payType、issuerId是否参与签名"
	 */
	private String version;
	/**
	 * 网关页面显示语言种类	2	可为空	固定值：1；1代表简体中文、2代表繁体中文、3代表英文
	 */
	private Integer language = -1;
	/**
	 * 签名类型	2	不可空	默认填1，固定选择值：0、1；0表示客户用MD5验签，1表示客户用证书验签
	 */
	private Integer signType;
	/**
	 * 商户号	30	不可空	数字串，商户在通联申请开户的商户号
	 */
	private String merchantId;
	/**
	 * 付款人姓名	32	可为空	英文或中文字符，当payType为3、issuerId为telpshx“直连模式”时，该值不可空，为办理银行卡时的所使用的姓名
	 */
	private String payerName;
	/**
	 * 付款人邮件联系方式	50	可为空	字符串
	 */
	private String payerEmail;
	/**
	 * 付款人电话联系方式	16	可为空	数字串，当payType为3、issuerId不为空“直连模式”时，该值不可空，为付款人支付时所使用的手机号码
	 */
	private String payerTelephone;
	/**
	 * 付款人证件号	22	可为空	数字串，目前只有电话支付直连模式才必填,并使用公钥加密(PKCS1)后进行Base64编码
	 */
	private String payerIDCard;
	/**
	 * 合作伙伴的商户号	30	可为空	用于商户与第三方合作伙伴拓展支付业务，Partner merchantId
	 */
	private String pid;
	/**
	 * 商户订单号	50	不可空	字符串，只允许使用字母、数字、- 、_,并以字母或数字开头；每商户提交的订单号，必须在当天的该商户所有交易中唯一
	 */
	private String orderNo;
	/**
	 * 商户订单金额	10	不可空	"整型数字，金额与币种有关
	 * 如果是人民币，则单位是分，即10元提交时金额应为1000
	 * 如果是美元，单位是美分，即10美元提交时金额为1000"
	 */
	private Long orderAmount = new Long(-1L);
	/**
	 * 订单金额币种类型	2	可为空	"默认填0-人民币
	 * 0和156代表人民币、840代表美元、344代表港币等"
	 */
	private String orderCurrency;
	/**
	 * 商户订单提交时间	14	不可空	日期格式：yyyyMMDDhhmmss，例如：20121116020101
	 */
	private String orderDatetime;
	/**
	 * 订单过期时间	14	可为空	用于订单提交和支付分离的业务或是可以通过通联会员系统对已提交订单发起再支付的业务。数字类型，单位为分钟。如不填写或填0或填非法值，则默认该订单60分钟后过期，最大值过期时间为9999分。
	 */
	private String orderExpireDatetime;
	/**
	 * 商品名称	256	可为空	英文或中文字符串
	 */
	private String productName;
	/**
	 * 商品价格	20	可为空	整型数字
	 */
	private Long productPrice = new Long(-1L);
	/**
	 * 商品数量	8	可为空	整型数字，默认传1
	 */
	private Integer productNum = -1;
	/**
	 * 商品代码	20	可为空	字母、数字或 - 、_ 的组合；用于使用产品数据中心的产品数据，或用于市场活动的优惠
	 */
	private String productId;
	/**
	 * 商品描述	400	可为空	英文或中文字符串
	 */
	private String productDesc;
	/**
	 * 扩展字段1	128	可为空	英文或中文字符串，支付完成后，按照原样返回给商户
	 */
	private String ext1;
	/**
	 * 扩展字段2	128	可为空	英文或中文字符串，支付完成后，按照原样返回给商户
	 */
	private String ext2;
	/**
	 * 支付方式	2	不可空	"固定选择值：0、1、3、4、10、11、12
	 * 0代表组合支付方式
	 * 1代表个人网银支付
	 * 3代表电话支付
	 * 4代表企业网银支付
	 * 10代表wap支付
	 * 11代表信用卡支付
	 * 12快捷付
	 * 非直连模式，设置为0；直连模式，值为非0的固定选择值
	 * 该字段在version =v2.0 时不参与签名，version=v1.0时参与签名
	 */
	private Integer payType = -1;
	/**
	 * 发卡方代码	8	可为空	"银行或预付卡发卡机构代码，用于指定支付使用的付款方机构。
	 * 该字段在version =v2.0 时不参与签名，version=v1.0时参与签名
	 * payType为0时，issuerId必须为空——显示该商户支持的所有支付类型和各支付类型下支持的全部发卡机构
	 * payType为非0时，若issuerId为空——显示该商户所填payType支付类型下的全部发卡机构
	 * payType为非0时，若issuerId不为空——直接跳转到该商户所填payType下指定的发卡机构网银页面，支持发卡机构详见《机构代码》sheet页
	 */
	private String issuerId;
	/**
	 * 付款人支付卡号	19	可为空	数字串，目前电话支付及交行B2B直连模式才必填,并使用公钥加密(PKCS1)后进行Base64编码
	 */
	private String pan;
	/**
	 * 贸易类型	2	可为空	"固定选择值：GOODS或SERVICES
	 * 当币种为人民币时选填
	 * 当币种为非人民币时必填，GOODS表示实物类型，SERVICES表示服务类型"
	 */
	private String tradeNature;
	/**
	 * 签名字符串	1024	不可空	以上所有非空参数按上述顺序与密钥key组合，经加密后生成该值。
	 */
	private String signMsg;
	//===============================request/response========================
	/**
	 * MD5 密钥
	 */
	private String key;
	/**
	 * 商户提交查询的时间	14	不可空	此时间不能与系统当前时间相差15分钟
	 */
	private String queryDateTime;
	/**
	 * 退款金额	10	不可空	"整型数字，金额与币种有关，以十分之一厘为单位
	 * 如果是人民币，即10元返回时金额应为100000
	 * 如果是美元，即10美元返回时金额应为1000
	 * 单位请商户自行转化
	 */
	private Long refundAmount = new Long(-1L);
	/**
	 * 业务扩展字段	1024	可为空	请参考《接口技术规范文档3.9节介绍》
	 */
	private String extTL;
	/**
	 * 通联订单号	50	不可空	字符串，通联订单号
	 */
	private String paymentOrderId;
	/**
	 * 支付完成时间	14	不可空	日期格式：yyyyMMDDhhmmss，例如：20121116020101
	 */
	private String payDatetime;
	/**
	 * 查询订单的开始时间	14	不可空	与endDateTime必须为同一天，日期填写格式：yyyymmddhh，例如：2013011600，目前只支持对当天订单进行查询
	 */
	private String beginDateTime;
	/**
	 * 查询订单的结束时间	14	不可空	与beginDateTime必须为同一天，日期填写格式：yyyymmddhh，例如：2013011623，查询时间范围为当天的00:00:00~23:59:59
	 */
	private String endDateTime;
	/**
	 * 查询页码	2	不可空	从1开始，必须为数字
	 */
	private String pageNo;
	/**
	 * 订单实际支付金额	10	不可空	"整型数字，实际支付金额，用户实际支付币种为人民币；
	 * 以分为单位，例如10元返回时应为1000分"
	 */
	private String payAmount;
	/**
	 * 处理结果	2	不可空
	 * 1：支付成功
	 * 0：未付款
	 * 仅在支付成功时通知商户。
	 * 商户可以通过查询接口查询订单状态。
	 */
	private String payResult;
	/**
	 * 错误代码	10	可空	失败时返回的错误代码，可以为空。
	 */
	private String errorCode;
	/**
	 * 结果返回时间	14	不可空	系统返回支付结果的时间，日期格式：yyyyMMDDhhmmss
	 */
	private String returnDatetime;
	/**
	 * 退款受理时间	14	不可空	数字串、退款申请受理的时间 日期格式：yyyyMMDDhhmmss 如20121116143030
	 */
	private String refundDatetime;
	/**
	 * 退款结果	10	不可空	成功：20  其他为失败
	 */
	private String refundResult;
	
	public Integer getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(Integer inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getPickupUrl() {
		return pickupUrl;
	}

	public void setPickupUrl(String pickupUrl) {
		this.pickupUrl = pickupUrl;
	}

	public String getReceiveUrl() {
		return receiveUrl;
	}

	public void setReceiveUrl(String receiveUrl) {
		this.receiveUrl = receiveUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(int language) {
		this.language = language;
	}

	public Integer getSignType() {
		return signType;
	}

	public void setSignType(Integer signType) {
		this.signType = signType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerEmail() {
		return payerEmail;
	}

	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}

	public String getPayerTelephone() {
		return payerTelephone;
	}

	public void setPayerTelephone(String payerTelephone) {
		this.payerTelephone = payerTelephone;
	}

	public String getPayerIDCard() {
		return payerIDCard;
	}

	public void setPayerIDCard(String payerIDCard) {
		this.payerIDCard = payerIDCard;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getOrderDatetime() {
		return orderDatetime;
	}

	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}

	public String getOrderExpireDatetime() {
		return orderExpireDatetime;
	}

	public void setOrderExpireDatetime(String orderExpireDatetime) {
		this.orderExpireDatetime = orderExpireDatetime;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Long productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductNum() {
		return productNum;
	}

	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getTradeNature() {
		return tradeNature;
	}

	public void setTradeNature(String tradeNature) {
		this.tradeNature = tradeNature;
	}

	public String getSignMsg() {
		return signMsg;
	}

	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getQueryDateTime() {
		return queryDateTime;
	}

	public void setQueryDateTime(String queryDateTime) {
		this.queryDateTime = queryDateTime;
	}

	public Long getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Long refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getExtTL() {
		return extTL;
	}

	public void setExtTL(String extTL) {
		this.extTL = extTL;
	}

	public String getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(String paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

	public String getPayDatetime() {
		return payDatetime;
	}

	public void setPayDatetime(String payDatetime) {
		this.payDatetime = payDatetime;
	}

	public String getBeginDateTime() {
		return beginDateTime;
	}

	public void setBeginDateTime(String beginDateTime) {
		this.beginDateTime = beginDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getReturnDatetime() {
		return returnDatetime;
	}

	public void setReturnDatetime(String returnDatetime) {
		this.returnDatetime = returnDatetime;
	}

	public String getRefundDatetime() {
		return refundDatetime;
	}

	public void setRefundDatetime(String refundDatetime) {
		this.refundDatetime = refundDatetime;
	}

	public String getRefundResult() {
		return refundResult;
	}

	public void setRefundResult(String refundResult) {
		this.refundResult = refundResult;
	}

	@Override
	public String toString() {
		return "AllinpayProperties [inputCharset=" + inputCharset
				+ ", pickupUrl=" + pickupUrl + ", receiveUrl=" + receiveUrl
				+ ", version=" + version + ", language=" + language
				+ ", signType=" + signType + ", merchantId=" + merchantId
				+ ", payerName=" + payerName + ", payerEmail=" + payerEmail
				+ ", payerTelephone=" + payerTelephone + ", payerIDCard="
				+ payerIDCard + ", pid=" + pid + ", orderNo=" + orderNo
				+ ", orderAmount=" + orderAmount + ", orderCurrency="
				+ orderCurrency + ", orderDatetime=" + orderDatetime
				+ ", orderExpireDatetime=" + orderExpireDatetime
				+ ", productName=" + productName + ", productPrice="
				+ productPrice + ", productNum=" + productNum + ", productId="
				+ productId + ", productDesc=" + productDesc + ", ext1=" + ext1
				+ ", ext2=" + ext2 + ", payType=" + payType + ", issuerId="
				+ issuerId + ", pan=" + pan + ", tradeNature=" + tradeNature
				+ ", signMsg=" + signMsg + ", key=" + key + ", queryDateTime="
				+ queryDateTime + ", refundAmount=" + refundAmount + ", extTL="
				+ extTL + ", paymentOrderId=" + paymentOrderId
				+ ", payDatetime=" + payDatetime + ", beginDateTime="
				+ beginDateTime + ", endDateTime=" + endDateTime + ", pageNo="
				+ pageNo + ", payAmount=" + payAmount + ", payResult="
				+ payResult + ", errorCode=" + errorCode + ", returnDatetime="
				+ returnDatetime + ", refundDatetime=" + refundDatetime
				+ ", refundResult=" + refundResult + "]";
	}
	
}