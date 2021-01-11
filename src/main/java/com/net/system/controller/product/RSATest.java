package com.net.system.controller.product;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jcajce.provider.digest.MD5;

import com.alibaba.fastjson.JSONObject;
import com.net.common.util.HttpUtil;
import com.net.common.util.MD5Util;

public class RSATest {

	private static final String KEY_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 1024;//设置长度
	//公钥
	private static final String PUBLIC_KEY_NAME = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZNY3wg3CxxK9rGzLU0Zz3b0S8fiq7AQ3oeASio7MLOKV54/GdOSwkBmVqe3XBeh9V9Duo509FBUM/22fzN5tx5w4K9nclUBmy1XSxOH0z4r9DNePRX2IhdM68dOCtjAAV6tjgj03v+H342Lv6WI9G1ZLwowov6WFXsAJgkViiHwIDAQAB";
	//私钥
	private static final String PRIVATE_KEY_NAME = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJk1jfCDcLHEr2sbMtTRnPdvRLx+KrsBDeh4BKKjsws4pXnj8Z05LCQGZWp7dcF6H1X0O6jnT0UFQz/bZ/M3m3HnDgr2dyVQGbLVdLE4fTPiv0M149FfYiF0zrx04K2MABXq2OCPTe/4ffjYu/pYj0bVkvCjCi/pYVewAmCRWKIfAgMBAAECgYAamVOlOAyusEkMPVKb5DBuPOEgIH9mrA4wF/YmDmmdPx5rhQmDlFN3hazByeFtz7f1AkwBTPdUMk+0nHD1pufTnfINrFUwvLXiYVgTB0OBtk7eyVy83D8WRAY71ZbM+QGQXyaH7frJh4ceeFPJIeErybfcP7eOJ+yTtqOE0GxxMQJBAPpUF6QtXQVcDT1KzY9jNMb7k5PCXEUc6iykdXleUuBHnDBpj8rl/kQ66iseE5K5rE0zdTE1OlFNW5yUEYUcpb0CQQCcritWLwdiCU+SzGrxW6xb9wzpSPYu1g582Q6aQHZ8DfFdZr3J4TpTXT8zH5ngd5AeLtbB48RU7EIzwe3w5D8LAkEAv3qngsu4Ras97U5UooOOEU7KedHH5o3RikhaG3ZNCoV8MYPtm5QFxIcUK9sbubYQ2BSuZe+DJkF3+/M+24UoOQJAST3x7DvXy7Bmjk0YwPMFw4fddUAyNPrDkwE0nk78FEnARtU8ax7qUQYKATAIkrHRoQpsQf7jTH+VAvTo8e8AuQJAFPnAlEqWUzh31SzYV3KV+4aaZEolpcx3FZB+KzQVLvW3eQrJiLy4jceAwM+ee0C9YClLl2s3JDaZKXpwKE3PPA==";
	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
	public static final String ENCODE_ALGORITHM = "SHA-256";
	
	/**
	 * 生成公、私钥
	 * 根据需要返回String或byte[]类型
	 * @return
	 */
	private static Map<String, String> createRSAKeys(){
		Map<String, String> keyPairMap = new HashMap<String, String>();
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			
			PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            /*Map<String, byte[]> byteMap = new HashMap<String, byte[]>();
            byteMap.put(PUBLIC_KEY_NAME, publicKey.getEncoded());
            byteMap.put(PRIVATE_KEY_NAME, privateKey.getEncoded());*/
			
			//获取公、私钥值
			String publicKeyValue = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			String privateKeyValue = Base64.getEncoder().encodeToString(privateKey.getEncoded());
			
			//存入
			keyPairMap.put(PUBLIC_KEY_NAME, publicKeyValue);
			keyPairMap.put(PRIVATE_KEY_NAME, privateKeyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return keyPairMap;
	}
	
	/**
	 * 解码PublicKey
	 * @param key
	 * @return
	 */
	public static PublicKey getPublicKey(String key) {
	    try {
	        byte[] byteKey = Base64.getDecoder().decode(key);
	        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
	        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	        return keyFactory.generatePublic(x509EncodedKeySpec);
	 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	/**
	 * 解码PrivateKey
	 * @param key
	 * @return
	 */
	public static PrivateKey getPrivateKey(String key) {
	    try {
	        byte[] byteKey = Base64.getDecoder().decode(key);
	        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
	        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	        
	        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}
	
	/**
	 * 签名
	 * @param key	私钥
	 * @param requestData	请求参数
	 * @return
	 */
	public static String sign(String key, String requestData){
		String signature = null;
		byte[] signed = null;
		try {
			PrivateKey privateKey = getPrivateKey(key);
	        
			Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
			Sign.initSign(privateKey);
			Sign.update(requestData.getBytes());
			signed = Sign.sign();
	        
			signature = Base64.getEncoder().encodeToString(signed);
			System.out.println("===签名结果："+signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return signature;
	}
	
	/**
	 * 验签
	 * @param key	公钥
	 * @param requestData	请求参数
	 * @param signature	签名
	 * @return
	 */
	public static boolean verifySign(String key, String requestData, String signature){
		boolean verifySignSuccess = false;
		try {
			PublicKey publicKey = getPublicKey(key);
			
			Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
			verifySign.initVerify(publicKey);
			verifySign.update(requestData.getBytes());
			
			verifySignSuccess = verifySign.verify(Base64.getDecoder().decode(signature));
			System.out.println("===验签结果："+verifySignSuccess);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return verifySignSuccess;
	}
	
	public static void main(String[] args) {
		
	}


}
