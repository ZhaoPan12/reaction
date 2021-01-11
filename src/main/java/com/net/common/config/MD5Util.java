package com.net.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	private MD5Util() {
	}

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String CalculateFileMD5(String filepath) {
		try {
			return MD5Util.getHash(filepath, "MD5");
		} catch (Exception ex) {
			return "";
		}

	}
	

	public static String getHash(String fileName, String hashType) throws IOException, NoSuchAlgorithmException {

		File f = new File(fileName);
//        System.out.println(" -------------------------------------------------------------------------------");
//        System.out.println("|当前文件名称:" + f.getName());
//        System.out.println("|当前文件大小:" + (f.length() / 1024 / 1024) + "MB");
//        System.out.println("|当前文件路径[绝对]:" + f.getAbsolutePath());
//        System.out.println("|当前文件路径[---]:" + f.getCanonicalPath());
//        System.out.println(" -------------------------------------------------------------------------------");

		InputStream ins = new FileInputStream(f);

		byte[] buffer = new byte[8192];
		MessageDigest md5 = MessageDigest.getInstance(hashType);

		int len;
		while ((len = ins.read(buffer)) != -1) {
			md5.update(buffer, 0, len);
		}

		ins.close();
//        也可以用apache自带的计算MD5方法
//        return DigestUtils.md5Hex(md5.digest());
//        自己写的转计算MD5方法
		return toHexString(md5.digest());
	}

	public static String getHash2(String fileName) {
		File f = new File(fileName);
		return String.valueOf(f.lastModified());
	}

	protected static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/*
	 * 获取MessageDigest支持几种加密算法
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String[] getCryptolmpls(String serviceType) {
        
		Set result = new HashSet();
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			Set keys = providers[i].keySet();
			for (Iterator it = keys.iterator(); it.hasNext();) {
				String key = it.next().toString();
				key = key.split(" ")[0];

				if (key.startsWith(serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 1));
				} else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 11));
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

}
