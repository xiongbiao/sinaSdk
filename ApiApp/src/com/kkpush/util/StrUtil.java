package com.kkpush.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
	
	public static boolean isValidHttp(String s) {
		if (isEmpty(s)) return false;
		Pattern p = Pattern.compile("^(http|ftp|file|https)://.*");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	public static boolean isValidCN(String s) {
		if (isEmpty(s)) return false;
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	 public static boolean isEmpty(String s) {
	        if (null == s) return true;
	        if (s.length() == 0) return true;
	        if (s.trim().length() == 0) return true;
	        if ("null".equals(s)) return true;
	        return false;
	 }
	 
	 public static void main(String[] args) throws Exception {
		 boolean b = isValidHttp("htts://baidu.com");
	 }
}
