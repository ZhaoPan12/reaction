package com.net.common.util;
import org.apache.commons.codec.binary.Hex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.net.system.model.vo.AccountType;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

/**
 * @author:
 * @desc: 3DES 加密/解密
 */
@Slf4j
public class Des3Utils {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
       /**
		Map<String,Object> m1 = new LinkedHashMap<String,Object>();
		m1.put("reqcode", "ACCLEDGER_CONF_QUERY");
		m1.put("oper_id", "admin");
		m1=sing(m1);
		m1.put("key", "reqcode,oper_id");
		System.out.println("签名结果："+m1);
		System.out.println("加密结果："+encryption("123456","8800100200000001671"));
		String param=HttpUtil.doPostJson("http://58.222.216.146:10003/json/JSONServlet", JSONObject.toJSONString(m1));
		**/
		Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("reqcode", AccountTypeBasic.RECHARGE);
		map.put("cert_no", "321202199309140029");
		map.put("oper_id", AccountTypeBasic.OPER_ID);
		map.put("amt", "1");
		map.put("paySource", "2");
		map.put("acc_kind", "45");
		map=sing(map);
		map.put("key", "reqcode,cert_no,oper_id,amt,paySource,acc_kind");
		System.out.println("签名结果："+map);
		String param=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
		System.out.println(param);
		JSONObject json=JSONObject.parseObject(param);
		
		
    }
    
    
    public static boolean isJson(String content){
	    	if(content.isEmpty()) {
	    		return false;
	    	}
            if(content.substring(0,1).equals("{")) {
            	return  true;
            }else {
            	 return  false;
            }
    }
    
    /**
         * 签名
     * @param map
     * @return
     */
    public static Map<String, Object> sing(Map<String,Object> map) {
    	String keys="";
    	String values="";
    	for(String key : map.keySet()) {
    		keys+=key+",";
			if(isJson(map.get(key).toString())) {
				
			}else {
				values+=map.get(key);
			}
		}
    	map.put("key", keys.substring(0, keys.length()-1));
    	map.put("sign", sha1(md5(values+"868888")));
		return map;
    }
  
    public static String encryption(String data,String key) {
    	String result="";
    	try {
	    	StringBuffer str=new StringBuffer();
	    	//待加密的数据
	    	StringBuffer strData=new StringBuffer();
	    	//加密的key
	    	StringBuffer strKey=new StringBuffer();
	    	Integer dataLength=data.length();
	    	if(dataLength<10) {
	    		str.append("0"+dataLength);
	    	}else {
	    		str.append(dataLength);
	    	}
	    		str.append(data);
	    	for (int i = str.length(); i < 32; i++) {
	    		str.append("0");
			}
	    	strData.append(StringUtil.strTo16(str.toString()));
	    	
	    	strKey.append(md5(key));
	    	strKey.append(md5(key).substring(0,16));
	    	SecretKeySpec keySpec=new SecretKeySpec(strToByte(strKey.toString()),"DESede");
	        Cipher cipher=Cipher.getInstance("DESede");
	        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
	        byte[] byteNi=cipher.doFinal(strToByte(strData.toString()));
	        result=byteTostr(byteNi);
    	}catch(Exception e) {
    		
    	}
		return result;
    }
    private static byte[] strToByte(String data) {
    	byte[] buf=new byte[data.length()/2];
    	for (int i = 0; i < data.length(); i+=2) {
			int n=Integer.parseInt(data.substring(i,i+2),16);
			buf[i/2]=(byte)(n & 0xFF);
		}
		return buf;
    }
    
    private static String byteTostr(byte[] buffer) {
    	String data="";
    	for (int i = 0; i < buffer.length; i++) {
			String hex=Integer.toHexString(buffer[i] & 0xFF);
			if(hex.length()==1) {
				hex='0'+hex;
			}
			data+=hex.toUpperCase();
		}
    	return data;
    }
    
    
    /**
         * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public static String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString().toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
         * 将数据进行 MD5 加密，并以16进制字符串格式输出
     * @param data
     * @return
     */
    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Hex.encodeHexString(md.digest(data.getBytes(StandardCharsets.UTF_8))).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    
  

    
}
