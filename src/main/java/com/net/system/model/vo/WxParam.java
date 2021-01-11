package com.net.system.model.vo;




import javax.servlet.http.HttpServletRequest;

import com.net.common.config.WxSPConfig;
import com.net.common.util.IPUtils;
import com.net.system.model.Order;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;
 
/**
 * @date: 2018/11/28 16:45
 * @author: YINLELE
 * @description: 微信请求参数
 */
public class WxParam {
    /*微信分配的小程序ID*/
    private String appid;
 
    /*微信支付分配的商户号*/
    private String mch_id;
 
    /*随机字符串，长度要求在32位以内*/
    private String nonce_str;
 
    /*签名*/
    private String sign;
 
    /*商品描述*/
    private String body;
 
    /*商户订单号*/
    private String out_trade_no;
 
    /*标价金额 默认分*/
    private Integer total_fee;
 
    /*终端IP*/
    private String spbill_create_ip;
 
    /*通知地址*/
    private String notify_url;
 
    /*交易类型*/
    private String trade_type;
 
    /*用户的openid*/
    private String openid;
 
    public String getAppid() {
        return appid;
    }
 
    public void setAppid(String appid) {
        this.appid = appid;
    }
 
    public String getMch_id() {
        return mch_id;
    }
 
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
 
    public String getNonce_str() {
        return nonce_str;
    }
 
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }
 
    public String getSign() {
        return sign;
    }
 
    public void setSign(String sign) {
        this.sign = sign;
    }
 
    public String getBody() {
        return body;
    }
 
    public void setBody(String body) {
        this.body = body;
    }
 
    public String getOut_trade_no() {
        return out_trade_no;
    }
 
    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }
 
    public Integer getTotal_fee() {
        return total_fee;
    }
 
    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }
 
    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }
 
    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }
 
    public String getNotify_url() {
        return notify_url;
    }
 
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
 
    public String getTrade_type() {
        return trade_type;
    }
 
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
 
    public String getOpenid() {
        return openid;
    }
 
    public void setOpenid(String openid) {
        this.openid = openid;
    }
 
    /*组装微信支付请求的参数*/
    public void setWxParam(WxParam wxParam,String serialNumber,double actual,
                           HttpServletRequest request,
                           String  notify_url,String openid)
            throws UnsupportedEncodingException {
        wxParam.setAppid(WxSPConfig.APP_ID_PUBLIC);
        wxParam.setMch_id(WxSPConfig.MCH_ID);
        wxParam.setSpbill_create_ip(IPUtils.getIpAddress(request));
        wxParam.setTrade_type("JSAPI");
        wxParam.setNonce_str(UUID.randomUUID().toString().replaceAll("-", ""));
        wxParam.setOut_trade_no(serialNumber);
        wxParam.setBody("泰州市民卡-商品支付");
        wxParam.setOpenid(openid);
        wxParam.setTotal_fee(Integer.valueOf(String.valueOf(Math.round(actual* 100))));
        wxParam.setNotify_url(notify_url);
        wxParam.setOpenid(openid);
    }
}
