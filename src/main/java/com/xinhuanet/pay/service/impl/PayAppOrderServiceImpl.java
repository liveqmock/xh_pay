package com.xinhuanet.pay.service.impl;

import com.xinhuanet.pay.cache.CacheName;
import com.xinhuanet.pay.cache.redis.RedisCacheMan;
import com.xinhuanet.pay.common.OrderStatus;
import com.xinhuanet.pay.common.PageRollModel;
import com.xinhuanet.pay.dao.PayAppOrderDao;
import com.xinhuanet.pay.po.AppOrder;
import com.xinhuanet.pay.po.Order;
import com.xinhuanet.pay.security.SecureLink;
import com.xinhuanet.pay.service.HttpClient4Service;
import com.xinhuanet.pay.service.PayAppOrderService;
import com.xinhuanet.platform.base.AppType;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-6-3
 * Time: 下午4:10
 */
@Service
public class PayAppOrderServiceImpl implements PayAppOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PayAppOrderServiceImpl.class);

    private
    @Autowired
    PayAppOrderDao dao;

    private
    @Autowired
    HttpClient4Service clientService;
    
    private @Autowired RedisCacheMan cache;

    @Override
    public List<AppOrder> getQuartzList() {
        return dao.getQuartzList();
    }

    @Override
    public boolean updateQuartzStatus(String id, int status) {
        return dao.updateQuartzStatus(id, status);
    }

    @Override
    public void backgroundNotice(AppOrder appOrder) {
//        logger.info("后台通知订单号：" + order.getId());
        if(StringUtils.isEmpty(appOrder.getBgRetUrl())){
        	return;
        }
        String cmdId = "Buy";
        String orderId = appOrder.getOrderId();            							//订单号
        String trxId = appOrder.getTrxId();											//支付平台唯一标识
        String appId = StringUtils.trimToEmpty(String.valueOf(appOrder.getAppId()));	//应用ID
        String pid = appOrder.getPid();												//商品ID
        String pname = appOrder.getPname();											//商品名称
        String merPriv = appOrder.getMerPriv();										//用户私有域
        String money = StringUtils.trimToEmpty(String.valueOf(appOrder.getMoney()));	//金额
        String status = StringUtils.trimToEmpty(String.valueOf(appOrder.getStatus()));	//支付状态，1表示成功，其它为失败
        String retType = "2";

        String merKeyFile = getClass().getResource("/security/MerPrk" + AppType.PAY + ".key").getPath();

        String merData = cmdId + orderId + trxId + appId + pid + pname + merPriv + money + status + retType;    //参数顺序不能错
        SecureLink secureLink = new SecureLink();

        int ret = secureLink.signMsg(AppType.PAY + "", merKeyFile, merData);
        if (ret != 0) {
            logger.info("产生签名失败[" + merData + "]");
        } else {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            try {
                params.add(new BasicNameValuePair("cmdId", cmdId));
                params.add(new BasicNameValuePair("orderId", orderId));
                params.add(new BasicNameValuePair("trxId", trxId));
                params.add(new BasicNameValuePair("appId", appId));
                params.add(new BasicNameValuePair("pid", pid));
                params.add(new BasicNameValuePair("pname", pname));
                params.add(new BasicNameValuePair("merPriv", merPriv));
                params.add(new BasicNameValuePair("money", money));
                params.add(new BasicNameValuePair("status", status));
                params.add(new BasicNameValuePair("retType", retType));
                params.add(new BasicNameValuePair("chkValue", secureLink.getChkValue()));
                String result = clientService.getResponeResult(appOrder.getBgRetUrl(), params);
                System.out.println(result);
                boolean flag = parseResponse(appOrder.getOrderId(), result);
                System.out.println(flag);
                if (flag) {
                    logger.info("后台通知订单号：" + appOrder.getOrderId() + "成功");
                    updateQuartzStatus(appOrder.getId(), 1);
                } else {
                    logger.info("后台通知订单号：" + appOrder.getOrderId() + "失败");
                    updateQuartzStatus(appOrder.getId(), 2);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        logger.info("后台通知订单号：" + appOrder.getOrderId() + "结束");
    }

    @Override
    public boolean parseResponse(String id, String result) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(result)) {
            return false;
        }

        Pattern pattern = Pattern.compile("RECV_ORD_ID_" + id);
        Matcher matcher = pattern.matcher(result);
        return matcher.matches();
    }
    
	@Override
	public int succeedAppOrder(AppOrder appOrder,String orderId) {
		appOrder.setChangeTime(new Date());
		appOrder.setStatus(OrderStatus.ORDER_STATUS_SUCCEES);// 将订单状态改为成功
		appOrder.setTrxId(orderId);//更新应用订单交易号
		int i =  dao.updateAppOrderStatus(appOrder);
		if (i == 1){
			Object object = cache.setObject(CacheName.APPORDER, appOrder.getId(), appOrder, 3 * 60);
			if(object == null){
				logger.error("loginName："+appOrder.getLoginName()+",应用订单添加缓存失败,缓存key:"+CacheName.APPORDER + appOrder.getId());
			}
		}
		return i;
	}

	@Override
	public int failAppOrder(AppOrder appOrder,String orderId) {
		appOrder.setChangeTime(new Date());
		appOrder.setStatus(OrderStatus.ORDER_STATUS_FAILED);// 将订单状态改为失败
		appOrder.setTrxId(orderId);//更新应用订单交易号
		int i = dao.updateAppOrderStatus(appOrder);
		if (i == 1){
			Object object = cache.setObject(CacheName.APPORDER, appOrder.getId(), appOrder, 3 * 60);
			if(object == null){
				logger.error("loginName："+appOrder.getLoginName()+",应用订单添加缓存失败,缓存key:"+CacheName.APPORDER + appOrder.getId());
			}
		}
		return i;
	}

	@Override
	public AppOrder getAppOrderById(String id) {
		AppOrder appOrder = (AppOrder)cache.getObject(CacheName.APPORDER, id, 3 * 60);
		return appOrder;
	}

	@Override
	public AppOrder getAppOrder(String appOrderId,int appId) {
		return dao.getAppOrder(appOrderId, appId);
	}
	
	@Override
	public AppOrder getAppOrder(String trxId) {
		return dao.getAppOrder(trxId);
	}

	@Override
	public int insertAppOrder(AppOrder order) {
		Object object = cache.setObject(CacheName.APPORDER, order.getId(), order, 3 * 60);
		if(object == null){
			logger.error("loginName："+order.getLoginName()+",应用订单添加缓存失败,缓存key:"+CacheName.APPORDER + order.getId());
		}
		return dao.insertAppOrder(order);
	}

	@Override
	public List<Order> getTradingRecordList(String uid, PageRollModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTradingRecordCount(String uid) {
		// TODO Auto-generated method stub
		return 0;
	}
}
