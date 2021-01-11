package com.net.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	 /**
     * 对字符串进行MD5加密(小写+字母)
     * @param str 要进行加密的字符串
     * @return 返回MD5加密后的字符串
     */
    public static String getMD5(String str) {
        String md5Str = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            md5Str = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
         
        return md5Str;
    }
     
    /**
     * 对字符串进行MD5加密(大写+数字)
     * @param s 要进行加密的字符串
     * @return 返回MD5加密后的字符串
     */
    public static String MD5(String s) {
        String md5Str = null;
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            md5Str = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return md5Str;
    }
    
    public static void main(String[] args) {
    	System.out.println(MD5("appName=10011&appTime=2019070216180207031dec76d8-4cfd-4f73-b9e2-749c2d1224c3"));
    }
     
}
