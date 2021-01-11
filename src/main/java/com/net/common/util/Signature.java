package com.net.common.util;



import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.net.common.config.MD5Util;

import com.net.common.config.WxSPConfig;


import java.util.*;
 
/**
 *      * 签名算法
 *      * 
 *      * @param o 要参与签名的数据对象
 *      * @return 签名
 *      * @throws IllegalAccessException
 *      
 */
public class Signature {
 
    private static Logger logger = LoggerFactory.getLogger(Signature.class);
 
    /**
     * 检验微信返回的数据并校验签名是否一致
     *
     * @param xmlResponse
     * @return Boolean
     */
    public static Boolean validateResponseXmlToMap(String xmlResponse) {
        Boolean  flag=false;
        if (StringUtils.isEmpty(xmlResponse)) {
            throw new RuntimeException("传入请求微信支付返回的数据为空");
        }
        // 处理微信返回的数据进行验证
        Map<String, String> xmlToMap = XmlUtil.xmlToMap(xmlResponse);
        logger.info("请求微信支付,返回的数据转换成Map" + xmlToMap);
        // 1.判断返回值是否成功 return_code 此值返回成功并不代表成功
        String return_code = xmlToMap.get("return_code");
        String return_msg = xmlToMap.get("return_msg");
        logger.info("微信支付,返回的通信标识" + return_code);
        logger.info("微信支付,返回的信息" + return_msg);
        if (StringUtils.isNotEmpty(return_code) && return_code.equals("SUCCESS")) {
            // 需要判断此值
            String result_code = xmlToMap.get("result_code");
            logger.info("微信支付,返回的成功标识" + result_code);
            if (StringUtils.isNotEmpty(result_code) && result_code.equals("SUCCESS")) {
                // 校验返回的sign是否一致
                validateSign(xmlToMap, WxSPConfig.MCH_KEY);
                flag=true;
                return flag;
            }
        }
        return flag;
    }
 
    /**
     * 校验微信支付的sign
     *
     * @param parameterMap
     * @param key          void
     */
    public static boolean validateSign(Map<String, String> parameterMap, String key) {
        String sign = parameterMap.get("sign");
        logger.info("微信支付,传入的sign进行校验" + sign);
        if (StringUtils.isEmpty(sign)) {
            logger.info("微信支付,sign参数为空!");
            return false;
        }
        String md5Hex = getSign(parameterMap, key);
        if (!md5Hex.equals(sign.toUpperCase())) {
            logger.info("微信支付,签名错误");
            return false;
        }
        return true;
    }
 
 
 
    /**
     * 获取sign值
     *
     * @param parameterMap
     * @param key
     * @return String
     */
    public static String getSign(Map<String, String> parameterMap, String key) {
        // 将Map转换为TreeMap
        Set<Map.Entry<String, String>> parameterMapSet = parameterMap.entrySet();
        Iterator<Map.Entry<String, String>> hashMapIterator = parameterMapSet.iterator();
 
        Map<String, String> treeMap = new TreeMap<String, String>();
        while (hashMapIterator.hasNext()) {
            Map.Entry<String, String> param = hashMapIterator.next();
            if (!"sign".equals(param.getKey())) {
                treeMap.put(param.getKey(), param.getValue());
            }
        }
        // 拼接字符串
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> treeMapSet = treeMap.entrySet();
        Iterator<Map.Entry<String, String>> treeMapIterator = treeMapSet.iterator();
        while (treeMapIterator.hasNext()) {
            Map.Entry<String, String> param = treeMapIterator.next();
            // 校验空值
            if (StringUtils.isEmpty(param.getValue())) {
                if (treeMapIterator.hasNext()) {
                } else {
                    sb.replace(sb.toString().length() - 1, sb.toString().length(), "");
                }
                continue;
            }
            sb.append(param.getKey());
            sb.append("=");
            sb.append(param.getValue());
            if (treeMapIterator.hasNext()) {
                sb.append("&");
            }
        }
        if (StringUtils.isEmpty(sb.toString())) {
            throw new RuntimeException("传入的参数为空");
        }
        // 拼接key
        sb.append("&key=").append(key);
        logger.info("微信支付,检验的拼接的字符串=" + sb.toString());
        String md5Hex = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        return md5Hex;
    }
    
    

    /** 
     * @author 
     * @date 2016-4-22 
     * @Description：sign签名 
     * @param characterEncoding 
     *      编码格式 
     * @param packageParams
     *      请求参数 
     * @return 
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) { 
        StringBuffer sb = new StringBuffer(); 
        Set es = packageParams.entrySet(); 
        Iterator it = es.iterator(); 
        while (it.hasNext()) { 
            Map.Entry entry = (Map.Entry) it.next(); 
            String k = (String) entry.getKey(); 
            String v = (String) entry.getValue(); 
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) { 
                sb.append(k + "=" + v + "&"); 
            } 
        } 
        sb.append("key=" + API_KEY); 
        String sign =MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase(); 
        return sign; 
    }
 
 
 
}
