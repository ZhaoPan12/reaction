package com.net.system.controller.product;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.net.common.util.HttpClientUtil;
import com.net.system.mapper.product.KeyMapper;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class SignUtil {
    private static final String ENCODING = "UTF-8";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    //私钥
    private static final String privateKey ="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDH8b6vkBG8p1wvWC1Gx4HUT5EUzcehNc2BbqJKKhJ3sDjuGa7Nl72y2pJPnCf0EmZCo98fzK/fsy9V3Al6Ot1t/aGY4jiLiUvTBJL4NXZbYJIIFRWkXOPOduXCtLZchVJTCxWEY5IwVfsbKUUlgWpaJXN90r69JdRSClc1Q7ct5vu9/bTYCyszuFOiw0gBxp5paPvgpOIitWhjoFTYf/loIOOVsHWeHawSrNJwPxHj/Mc8ftZdqVCVQYLnnRza4FpL0L2hdfwA+IG26uu7VROx/VkHY07u5lr5lj9XjWLypJO+3JP3lETRcn54ka9INwjmQ+p45sSJSokQy4IGeWlBAgMBAAECggEAb+lle8q9wsRCGtXV8cn1Zypp4CUV2avNrOaJu800rP1bwBlr9/M44ITqY9jZnAk4Z5BlcN0Wi7U1h28zdtC/47KU4oescp2UbTYgNQFnj9nLou56QJclgwiVrZeMgti0rXqEvqckyh02AYvmAgofxvkzXXe5/xDPeGQFT/Do9K1YnVOc08eN1EsKwGs8RITLnI3DidNIKAYRAbWu53LJSqCppdnCrYdm5ob5Kk+fcMRTqkkKBXLPJCMZav2J5OKri0UwQWJgJQ/+DBjpp17wB1dt0oxRrbRQ/UOgT4T8gx8KDH3qC93aZB4i5ocGTFiWmG5I1Cm183oHc+ahBtAUqQKBgQD1V0XgTSRtuHBE4JjqyrYANQAZCuQ84BGZ41FNIcvRuwHB1SdM3lqUuOJbUhcqPFT9c/q5FxgiYp5ZdXWQBaUlXUslMxTampKaA50vIXJqFR1M6cnAk+oU0GXQFJ4Swf45ShMTIIg80v5D1DWcU1H5lFwcNYphxsdXBnmaCwG/lwKBgQDQoZAFgsLio2dxAtwnXhyF6KMeC/yfUbLjSTLRL8adsYvqwTlNi/pjlrext7bOfHxOgPMTpDCzNYLPki5T04nXjfFWyJy4N8rBowk/XqiIUk1tLaZ6uwdzDVbOvSBVMzwyNIaSZoUZcQCDBcWMIPB8FgGlkrF4w1J57GhmUXe45wKBgQCUoU2EyFc0XVMudv5M54eA8prfkPiaZIhlORBs8PeAVJGI8u//IomvpZ4EAuRjwu8eKOAQt4v4cNRLj9wr5y+YRj+bBqowkQoVYfEct1+QIsCAcvW1xCZAe8viVks5Q+sWm+iYYtIGPzCouOvNi7CSya1PraPq/jiPYS3UA60IewKBgQCMKy/28955pG04GULOVEV3llAZPNZyN9KnL6OrtZyMkB/ADiewuQ314OnzxclVO3/MwogNty9dB+OmD8F+Yk5m+x+dPdVFU36u0BnDcJZsWFdateP26zdbDig3aAuUEoMr++w56hUcfbZpUPRjbDIGPEbI9iC0UQ5VigauT6fPGwKBgQD0aOL+NnOQTb5sqXji3uaDTJXT67knutUNXRdzFuqF21DJujvviWkVHz2s/bV5iNKJIymDN1msoxcKA2a82HVRAWQ4oTbIXD1NVdbrUfnxB6KhBNjipl2nT4BAg5kx0qDPQunzRJogMD0tnC0z2rDN97lmWjO1E95w45/zfWkA0g==";
    //公钥
    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx/G+r5ARvKdcL1gtRseB1E+RFM3HoTXNgW6iSioSd7A47hmuzZe9stqST5wn9BJmQqPfH8yv37MvVdwJejrdbf2hmOI4i4lL0wSS+DV2W2CSCBUVpFzjznblwrS2XIVSUwsVhGOSMFX7GylFJYFqWiVzfdK+vSXUUgpXNUO3Leb7vf202AsrM7hTosNIAcaeaWj74KTiIrVoY6BU2H/5aCDjlbB1nh2sEqzScD8R4/zHPH7WXalQlUGC550c2uBaS9C9oXX8APiBturru1UTsf1ZB2NO7uZa+ZY/V41i8qSTvtyT95RE0XJ+eJGvSDcI5kPqeObEiUqJEMuCBnlpQQIDAQAB";
    // AES密匙
    private static final String KEY = "sendinfotomtest1";
    // AES偏移量
    private static final String OFFSET = "1234567ab2345678";
    //算法
    private static final String ALGORITHM = "AES";
    // 默认的加密算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    /**
     * SHA256WithRSA签名
     * @param data
     * @return
     */
    public static String sign256(String data,String privateKeys) {
        String result = "";
        try {
            byte[] keyBytes;
            //解码
            keyBytes = Base64.decodeBase64(privateKeys.getBytes());
            //生成PKCS8格式对象
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            //加密算法类型
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            //生成加密算法实例
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            //进行加密
            signature.initSign(privateKey);
            signature.update(data.getBytes(ENCODING));
            
            result = new String(Base64.encodeBase64(signature.sign()));
            
        }catch (Exception e){
           
        }
            return result;
    }

	public static boolean verifySign(String plain_text, String sign,String publicKeys) {
		boolean SignedSuccess=false;
		try {
			  byte[] keyBytes;
	            //解码
	            keyBytes = Base64.decodeBase64(publicKeys.getBytes());
	            //生成PKCS8格式对象
	            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
	            //加密算法类型
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
	            //生成加密算法实例
	            Signature  verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
				verifySign.initVerify(publicKey);
				verifySign.update(plain_text.getBytes());
				byte[] signByte = Base64.decodeBase64(sign);
				SignedSuccess = verifySign.verify(signByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SignedSuccess;
	}
    /**
     *
     * 生成密钥
     *
     * @param seed 种子
     *
     * @return 密钥对象
     * @throws Exception
     *
     */

    public static Map<String, Key> initKey(String seed) throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom();
        // 如果指定seed，那么secureRandom结果是一样的，所以生成的公私钥也永远不会变
//		secureRandom.setSeed(seed.getBytes());
        // Modulus size must range from 512 to 1024 and be a multiple of 64
        keygen.initialize(1024, secureRandom);
        KeyPair keys = keygen.genKeyPair();
        PrivateKey privateKey = keys.getPrivate();
        PublicKey publicKey = keys.getPublic();
        Map<String, Key> map = new HashMap<String, Key>(2);
        Key si = (Key) privateKey;
        Key gong = (Key) publicKey;
        String pubKey = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String priKey = new String(Base64.encodeBase64(privateKey.getEncoded()));
        System.out.println("pubKey-----"+pubKey);
        System.out.println("priKey-----"+priKey);
        return map;
    }



    /**
     *
     * 取得私钥
     *
     * @param keyMap
     *
     * @return
     * @throws Exception
     *
     */
    public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
        Key key = (Key) keyMap.get("PRIVATE_KEY");
        return new String(Base64.encodeBase64(key.getEncoded())); // base64加密私钥
    }


    /**
     * AES加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(ENCODING));
        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
    }

    /**
     * AES解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes("ASCII"), ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(OFFSET.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] buffer = new BASE64Decoder().decodeBuffer(data);
        byte[] encrypted = cipher.doFinal(buffer);
        return new String(encrypted, ENCODING);//此处使用BASE64做转码。
    }

    public static void main(String[] args) throws Exception {
    	/**
        try {
            Map<String,Object> map=new LinkedHashMap<String, Object>();
            map.put("idCard", "9Hwu3MC/6k940CsjLHeJpmI3gEAzcqHP6xLc0gd9kHk=");
            map.put("idType", "00");
            map.put("channelId", "test");
            StringBuffer str=new StringBuffer();
            for (String key : map.keySet()) {
            	str.append(key+"="+map.get(key)+"&");
             }
            String syrs=str.substring(0, str.length()-1);
            map.put("sign", sign256(syrs));
            System.out.println("签名串："+syrs);
            System.out.println("签名后："+sign256(syrs));
            System.out.println("验签后："+verifySign(syrs,sign256(syrs)));
        //    System.out.println("签名后："+sign256("channelId=test&idCard=9Hwu3MC/6k940CsjLHeJpmI3gEAzcqHP6xLc0gd9kHk=&idType=00"));
       //   String param=HttpClientUtil.doPostJson("http://t3.htaonet.com/right/doQueryRightInfos", JSONObject.toJSONString(map));
       //   System.out.println(param);
        }catch (Exception e){

        }
        */
    	System.out.println(initKey("test"));
    }

}









