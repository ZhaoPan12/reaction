package com.net.system.controller.report;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WxServic {
	
	private static  final String TOKEN=""; 

	/**
	 * 验证签名
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp, String nonce, String signature) {
		String[] strs=new String[] {TOKEN,timestamp,nonce};
		Arrays.sort(strs);
		String str=strs[0]+strs[1]+strs[2];
		String mysig=sha1(str);
		// TODO Auto-generated method stub
		return false;
	}

	private static String sha1(String str) {
		try {
			MessageDigest md=MessageDigest.getInstance("sha1");
			//加密
			byte[] diges=md.digest(str.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuffer sb=new StringBuffer();
			//处理加密结果
			for (byte b : diges) {
				sb.append(chars[(b>>4)&15]);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
