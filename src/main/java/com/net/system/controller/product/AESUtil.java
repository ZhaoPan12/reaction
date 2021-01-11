package com.net.system.controller.product;

import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.sun.istack.internal.NotNull;

public class AESUtil {
	
	
	private static final String CHARSET_NAME = "UTF-8";
	private static final String AES_NAME = "AES";
	// 加密模式
	public static final String ALGORITHM = "AES/CBC/PKCS7Padding";
	// 密钥
	public static final String KEY = "sendinfotomtest1";
	// 偏移量
	public static final String IV = "1234567ab2345678";
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static AESUtil aESUtil=null;
	
	public AESUtil getAESUtil() {
		if(aESUtil==null) {
			return new AESUtil();
		}else {
			return aESUtil;
		}
	}
	/**
	 * 加密
	 *
	 * @param content
//	 *
	 * @return
	 */
	public String encrypt(@NotNull String content) {
		byte[] result = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(CHARSET_NAME), AES_NAME);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
			result = cipher.doFinal(content.getBytes(CHARSET_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.encodeBase64String(result);
	}

	/**
	 * 解密
	 *
	 * @param content
	 *
	 * @return
	 */
	public String decrypt(@NotNull String content) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(CHARSET_NAME), AES_NAME);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
			return new String(cipher.doFinal(Base64.decodeBase64(content)), CHARSET_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	public static void main(String[] args) {
		AESUtil aes = new AESUtil();
		String contents = "DYEeaaWf6uNSRBSmvYe6UQU00WZpbppDptIU3LIz1FM=";
		String decrypt = aes.decrypt(contents);
		System.out.println("加密后:" + decrypt);
	}
}
