package com.xinhuanet.pay.util;

import com.xinhuanet.pay.security.SecureLink;

import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: 李野
 * Date: 13-5-17
 * Time: 上午11:12
 */
public class KenGenTest {

//    @Test
//    public void testSign() throws Exception {
////        String pri = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJrNZiWSRpcSpKuz5Pt9iuQdDg8VDdsORDqOJ/7+fgSPlH7exo5NlAQXloNhpLoN5mb0qmwMVbxUjRLZOVk9Je3FjxXDFINxnG33agWk4qH5ZSmgr02GHFbq4qwzBYUc0zkwVcphQzeJym1QpD9eWTpgXMR/4MmyoM6LWfQ65G7/AgMBAAECgYBv8fNZXjOAEnJU3M3NdFcZaU1+aKpRbDq44Y5Y328AG1i2eqG5zK4qW2hz8AuwpFamw23unujtPXObWs0XFJo0+wI6vPi5xnAjXXUkDc94Ymr1pQcrbHAjXRxGjBLa1O17dF4ckLnpOG1bW9Yx0hJc+KJKYnFsA90GPuwpN6ffgQJBAOHBnCmOe+F4u07vCEpZcLQ/Z5lCsAU41FVEro1tXUdpgNe2KhFgp30t2SsD8kWLSgf7Aj/nrP36X/d+dj+0MEECQQCvimYWf7M/eMe7a9c35HD7qsdGKPJKLYek+dj6hxC4oDyfVVfcV2D+ZZSRN+63zP+s1GiVhnygYKcnlxA4iM8/AkEA3ls6GtxSPAQUSWy6N7Dch0ykF5pNkPcmfE3Ht1jutTW/R7gOu0r97RfJfMrIR6Mn6bBbyOHBy+5ds4GFqr0pgQJBAKCNP8QSNjq2WE/WWXjYanH0B+DmsHwh2m+MyOSSxjtm6yt73ik8jgz/kXukoaTgTXSgrasc3z28SQYPx5k9n/cCQQCHyU9Xy3ujFtUPsqfll+dekzc0HDElB2x2Yyj6K4TDlN2RCZD2lpl5Ebz5ocYwSn4MFqD7vNZbpQyGQHtBYr3G";
////        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCazWYlkkaXEqSrs+T7fYrkHQ4PFQ3bDkQ6jif+/n4Ej5R+3saOTZQEF5aDYaS6DeZm9KpsDFW8VI0S2TlZPSXtxY8VwxSDcZxt92oFpOKh+WUpoK9NhhxW6uKsMwWFHNM5MFXKYUM3icptUKQ/Xlk6YFzEf+DJsqDOi1n0OuRu/wIDAQAB";
//        String data = "我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人我是中国人";
//        SecureLink link = new SecureLink();
//        int i = link.signMsg("140", getClass().getResource("/security/MerPrk140.key").getPath(), data);
//        System.out.println(i);
//        System.out.println(link.getChkValue());
//        i =  link.veriSignMsg(getClass().getResource("/security/PgPubk140.key").getPath(), data, link.getChkValue());
//        System.out.println(i);
//    }
}
