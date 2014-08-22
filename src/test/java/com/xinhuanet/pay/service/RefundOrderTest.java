package com.xinhuanet.pay.service;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import com.xinhuanet.pay.exception.AccountUnmatchErrorException;
import com.xinhuanet.pay.exception.OrderException;
import com.xinhuanet.pay.po.RefundApply;


/**
 * Created with IntelliJ IDEA.
 * Date: 13-5-17
 * Time: 上午11:12
 */
public class RefundOrderTest {

    @Test
    public void addTest()  {
        String[] args = new String[]{"src/main/webapp/WEB-INF/config/app-config.xml", "src/main/webapp/WEB-INF/config/mvc-config.xml","src/main/webapp/WEB-INF/config/redis.xml"};
        ApplicationContext context = new FileSystemXmlApplicationContext(args);
        RefundOrderService service = context.getBean(RefundOrderService.class);
        RefundApply refApply =new RefundApply();
       
        refApply.setUid("2902353");//用户ID
		refApply.setLoginName("duanwc");//用户登录名称
		refApply.setOrderId("20140311052232957319");//当前退款的原订单ID
        refApply.setReason("无条件退款");
        int i = 0;
		try {
			i = service.add(refApply.getUid(),refApply);
		} catch (AccountUnmatchErrorException e) {
			e.printStackTrace();
		} catch (OrderException e) {
			e.printStackTrace();
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
			System.out.println("已经申请退款");
		}
        assertEquals(1,i);
    }
}
