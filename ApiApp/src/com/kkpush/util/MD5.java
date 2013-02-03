/**
 * 
 */
package com.kkpush.util;

import java.security.MessageDigest;

/**
 * @author xiongbiao
 * 
 */
public class MD5 {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultString;
	}
	 // 测试主函数
	 public static void main(String args[]) {
	  String s = new String("12345");
	  System.out.println("原始：" + s);
	  System.out.println("MD5后：" + MD5Encode(s));
	  System.out.println("MD5后再加密：" + KL(MD5Encode(s)));
	  System.out.println("解密为MD5后的：" + JM(KL(MD5Encode(s))));
	 }
	 
	 
	 

	 // 可逆的加密算法
	 public static String KL(String inStr) {
	  // String s = new String(inStr);
	  char[] a = inStr.toCharArray();
	  for (int i = 0; i < a.length; i++) {
	   a[i] = (char) (a[i] ^ 't');
	  }
	  String s = new String(a);
	  return s;
	 }

	 // 加密后解密
	 public static String JM(String inStr) {
	  char[] a = inStr.toCharArray();
	  for (int i = 0; i < a.length; i++) {
	   a[i] = (char) (a[i] ^ 't');
	  }
	  String k = new String(a);
	  return k;
	 }
	 
}
