package com.net.common.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesEncryptUtil {

    //使用AES-128-CBC加密模式，key需要为16位,key和iv可以相同！
	//@Value("${aesEncrypt.key}")
	private static final String KEY="dufy20170329java";
	
	//@Value("${aesEncrypt.iv}")
   	private static final String IV="dufy20170329java";

   	
   	private static AesEncryptUtil aesEncryptUtil=null;
   	
	private AesEncryptUtil() {
		 
	}
	
	public static AesEncryptUtil getAesEncryptUtil() {
		if (aesEncryptUtil == null) {
			aesEncryptUtil = new AesEncryptUtil();
		}
		return aesEncryptUtil;
	}
    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public  String encrypt(String data, String key, String iv) throws Exception {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public  String desEncrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public  String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public  String desEncrypt(String data) throws Exception {
        return desEncrypt(data, KEY, IV);
    }



 // 解密
    public String decrypt(String sSrc) throws Exception {
	    try {
		    byte[] raw = KEY.getBytes("ASCII");
		    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
		    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		    byte[] encrypted1 = new  Base64().decode(sSrc);//先用base64解密
		    byte[] original = cipher.doFinal(encrypted1);
		    String originalString = new String(original,"utf-8");
		    	return originalString;
		  } catch (Exception ex) {
			  return null;
	    }
    }
    
    
    public static void main(String[] args) throws Exception {
    	AesEncryptUtil s=new AesEncryptUtil();
    //	String str="/2b4E1JWY9q114EB+1bORXpiv9yu2ge7U3wZxQ7hW0yhn9+kN85lN6NwcLkYEtJNRGetDhecxPpgsM1y3l++1pe12b6tGwLDMhTfvyJUOtKgW+q/5Y2y5Qr9SGU8MjIIRdypiePzcyNCq6ach5yl/a+TnarR5c0Xy5WAbt+P27+uAKyNwOexwXP6ehnlHBN8kv0ZoK/TuAVi2GU1vuLuKFIfBMcoAFH2Didf0pclwANzi8c9hbT5F41qCNMnkze+38h5FBG/knktacvdl6W+zC5SuHV5WXVcIpdnqTVxxGlaixwcyLUzLowxH00UNhYELUFSxEbF7OAutOL6LSpcNE1R57z5TFsvGFeU8FbNuVpGbLY//e36zSKfKVzc+xB84Ty5alpEazs+adiko76BgltJcrdUu3QSk5WecbtzzImnM/NxuIQflb06vVCHqoayM3en9rUPxJHjOKdReD9XX4It0w4AnJLDLQdGpIF4n57aQzOHRVGx5rAVgy24jVA+/hfA78L8br02Z7tQQRIOwAtps2O51RFtfA8f/0Rf0YQforjuUXkYdVDwu0Ow1R/YJNE2VLs5IkeBuhG2EQw/lF3GEDWV+i6pUzXuj/EIOfHAdCxIAFG3jNSGo+reOH5itBDmfirGwMmFvpmSBqKVrWq7s4VzhUlGtqY8h3D91zKwjDcrZ26M+/9D44IkbK7Fd8NUYJEFdiT4tXucMc5SFeUexBDukjx8RnV3fkmcZc98rkECcH4e1fjdQLVmP3I5Gthv5fgv71XssC4RxSwZD7qOVuPVGxa4uouMdTbcVItlMQZY3SszQ3BKC540cggv25ifBTxPCMn0p1oe8i3rwIvjqVUr4qfbQ/2vxGcobIQ=";
        String str="tOXCUrRy5cOIq4tYS1o2wrs3QAsHAMrUHel4Sr3C2ND1picSda/ob75itHrH0fFgcHVbZhj8IuXaDg4QV56iXnQoDoNcs5O2tSsVyrRik+AQo/+NyCrw67eSy0VohhZKMf0X4wiPwVxDJ2bpbAVrSIwuOYAkSqILrAxmh3n4lbv5bST3/2uV1gNosQJLfv+K8CRhGMdAdd+wRz7wRri3B75ubH3bea0rmQ2tScP11m6+nJ9ZSiR1EPI6X1GmsGdWafNKsN/HpOj5xg4rbDbtKNcykkgfWQJvCoOwu6e48egjKIkqwh9xn6PyudgrqGvD63h/G7kvO1fpyH60eLbC0X2EobUeIe4nDZrzfATs3MX33xxWJQ1lDeBYbydVIQXSCT4u4lPLZEWX0JucsBeWnQrWkijVyYu/xaibyeOXJXa0P7uQp+Zmph3uZaMOtAF5izpRJ/W+GXvVM5uOoiyXcIBFw9dIJ1IUaAUtudZdaEvvShtD/XaY+TdM+zfP9wVKVRY8PVa7OH4PPZmB34ev5MHCNGZgD2IDwazuefXvOJLBU/t4YIYonrQEzLDdbuhYDXILJKUDg5FkxoGKIYz1DvsgXYvBA8e5A/0iZv8ZYkwsunWKZ/g79nooUDvKKa9LH7sYgCRojjMqgWQjrsL3XS48QZr0w88JicjtzigmQJEUGV3V18LVjRrjTHa3stE1MZS5EkDft4+yxIcpfUw+/wOjrGzljxBhSabSMi3HT//d3abtg/pHzFLBjsJVnDNLja/a0/wU9JLZ1PDytw5VULmAlCEuX9JK4n/UZX54LZ25I4+bNbgJheiMOpzvtVIXwax5DdPk12VxJdDTaq8G1mcCGYMcg0QbFczXinsAOG5WBTfiJo7hZMuEZ2ACL1OEC1B3IwpE8c6KkW8CRnbhfYy28+0qgN/IMZe+TqUPeBFx3rDg+k+WhON411pxBwRWDRao5Mq5lHJGHU5H+tcDH7VxNlAKHrCzsfdvjLZNT3yhyJ6sBk+ro1lUffvO2+DumShLoM58BKkPAVVjm/Cjaqb5CI6xAYAeSIoHyWX1DrMcnQd8/EwCslI+lF/OX2BgM0Y1XeJaJxzoRxffHC2XRpXTwoZpKQjfZFOwREl0ZcpSczjH/58Iy0+UvvcAxUIK1WXSycbyxrPVVAH92/3hOr/6syCajUoKh1jWa1okIQljqOkQ9FMt6lItPlBGHiw/cwQ5+5pteYIS+Jo/yEVYZNHgR6HST1Q6Sqz99/6x3393GldhpIoBElizrUpcNVGzlBwXIpaTvuWNPQ8zzvdxwjmzDQ9gzpZsr7szO3cHhvPw5G6nzjgPikdzUKRun8h7P5BEsXy7oMa+CKWLW1F7u7K8qxICIzFVM4wZomEPV+c+ZW8we5K8VxFixwGFp5txNNPkWmkgcuV7FadYc6EG9oc4pCbmyQNZfHaGrn4WNIOQ6Jv0qHUDJ1PMWeyrTAOQLE/0NcxNSOEpCGFZmEv2GHEccCOEU09t0/VIn3Es3dg3kKXvLC/H+a9jD4MoRxjeaf8frT8MZde3qvLS11p9wOAJa7MgrhW+l8PT3y8MJbjgDKQtZziIfu/z/2gCWscRIaVR3/TiVLONGAgQY+RbaurQh+5M9pLG8OAAQ/+xAl7bMRuLSLDjuSAXTEvWTI3FzmMFcPTe5uEq/lQW8xxhKlQj6Zj2jsNY8R63hE45fX50K9roIfdIfnl9eYPnIvH4BTpC2geGy76ZfVzpoO8VEx8RDJ6/F7HkOmEOjOLBcmxXj4JDsyrssmtnTK1VqKOprcCiXML1aGpuQmFOcXFZMzrnFyUQ/yEpy1N89XFcgq5PWRrhibjZYpmgtJ8Mq0jG1qUD2BepzwYVfwde/80xFLKKoBa0g34rX8Sbier0euPXDz3zcaLuqw39COSXxtEb/gRTxKewjzMDFu1dsbtuAA==";
        System.out.println(s.desEncrypt(str));
    	Map<String,Object> map=new HashMap<String, Object>();
    //	map=transStringToMap(s.desEncrypt(str));
    //	System.out.println(map);
    }

  /** 
     * 方法名称:transStringToMap 
     * 传入参数:mapString 形如 username'chenziwen^password'1234 
     * 返回值:Map 
    */  
    public static Map transStringToMap(String mapString){  
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(mapString, map.getClass());
      return map;  
    }  

    public static boolean isJson(String json) {
    	JsonParser jsonParser = new JsonParser();
    	JsonElement jsonElement = jsonParser.parse(json);
    	return jsonElement.isJsonObject();
    }
}