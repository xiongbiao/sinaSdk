package com.kkpush.util;

public class StringUtils {
	
	public static boolean isEmpty(Object obj){
		return obj == null ? true : String.valueOf(obj).trim().length() == 0;
	}
	
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
	
}
