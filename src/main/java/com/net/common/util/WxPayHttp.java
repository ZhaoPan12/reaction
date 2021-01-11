package com.net.common.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
 
/**
 * @date: 2018/11/28 17:06
 * @author: YINLELE
 * @description: 小程序支付的工具类
 */
public class WxPayHttp {
    public  static final Logger logger= LoggerFactory.getLogger(WxPayHttp.class);
 
    /**
     * 请求微信下单的接口
     *
     * @param urlRequest
     * @param xmlRequest
     *            void
     */
    public static String doPostPayUnifiedOrder(String urlRequest, String xmlRequest) {
        String xmlResponse = HttpUtil.doSSLPost(urlRequest, xmlRequest);
        return xmlResponse;
    }
 
    /**
     * 返回给微信异步通知的信息
     * SUCCESS/FAIL
     * SUCCESS表示商户接收通知成功并校验成功
     *
     * 返回信息，如非空，为错误原因：
     * 签名失败
     * 参数格式校验错误
     * */
    public static void responseXmlSuccess(HttpServletResponse response) throws Exception {
        Map<String,String> map =new HashMap<String,String>();
        map.put("return_code","SUCCESS");
        map.put("return_msg","OK");
        String xml = XmlUtil.createRequestXml(map);
        logger.info("微信异步回调结束====> "+xml);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = response.getWriter();
        writer.write(xml);
        writer.flush();
    }
}

