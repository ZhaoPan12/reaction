package com.net.common.config;



/**
 * @date: 2018/10/26 16:42
 * @author: YINLELE
 * @description: 关于微信小程序的配置参数
 */
public class WxSPConfig {
    
    /*微信公众号的ID  wxa7c2b431b1b32e45，wx13dfc5ea6f387b3a*/
    public static final String APP_ID_PUBLIC = "wxa7c2b431b1b32e45";
 
    /*微信公众号的SECRT  d998b4ce9b3ea7abe819b3c326bf9c96 ， 34352b999d12fa92fa9b350593bd46be*/
    public static final String SECRET_PUBLIC = "d998b4ce9b3ea7abe819b3c326bf9c96";
 
    /*商户号1594672581 ， 1514086041*/
    public static final String MCH_ID = "1594672581";
 
    /*商户平台的秘钥  nanjingsuzhihang20170830zyygzxcv，tzsmk86881988tzsmk86881988tzsmk8*/
    public  static final String MCH_KEY="nanjingsuzhihang20170830zyygzxcv";
 
    /*小程序的ID*/
    public static final String APP_ID_SMALL_PROGRAM = "wxf90169b4c7976eee";
 
    /*小程序的appsecret*/
    public static final String SECRET_SMALL_PROGRAM = "45a2901d8d079fbd700966078698dbcc";
 
    /*获取微信小程序的access_token接口*/
    public static final String ACCESS_TOKEN_SMALL_PROGRAM = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
 
    /*获取微信网页的access_token*/
    public static final String ACCEAA_Token_WEB = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
 
    /*获取微信用户的信息*/
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
 
    /*登录凭证校验。通过 wx.login() 接口获得临时登录凭证 code 后传到开发者服务器调用此接口完成登录流程*/
    public static final String CODE2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
 
    /*统一下单 URL*/
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
 
 
}
