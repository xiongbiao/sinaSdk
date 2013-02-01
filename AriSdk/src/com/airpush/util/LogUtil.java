package com.airpush.util;

import android.util.Log;

public class LogUtil {

	private static String TAG = "XB-SDK";
	public static boolean isDug = true;
	
	private static String POSTSTR = "请求参数--->>>";
	private static String Response_STR = "返回结果--->>>";

	public static void ip(String tag, String msg){
		if (isDug)
			Log.i(TAG, "[ " + tag + " ] " + POSTSTR + msg);
	}

	public static void ir(String tag, String msg){
		if (isDug)
			Log.i(TAG, "[ " + tag + " ] " + Response_STR + msg);
	}

	
	public static void i(String tag, String msg) {
		if (isDug)
			Log.i(TAG, "[ " + tag + " ] " + msg);
	}
	
	public static void e(String tag, String msg) {
		if (isDug)
			Log.e(TAG, "[ " + tag + " ] " + msg);
	}
	
	public static void d(String tag, String msg) {
		if (isDug)
			Log.d(TAG, "[ " + tag + " ] " + msg);
	}
	
	public static void d(String tag, String msg,Throwable tr) {
		if (isDug)
			Log.d(TAG, "[ " + tag + " ] " + msg,tr);
	}
	
	public static void v(String tag, String msg) {
		if (isDug)
			Log.d(TAG, "[ " + tag + " ] " + msg);
	}
	
	public static void v(String tag, String msg,Throwable tr) {
		if (isDug)
			Log.v(TAG, "[ " + tag + " ] " + msg,tr);
	}
	
	public static void w(String tag, String msg) {
		if (isDug)
			Log.w(TAG, "[ " + tag + " ] " + msg);
	}
	
	public static void w(String tag, String msg,Throwable tr) {
		if (isDug)
			Log.w(TAG, "[ " + tag + " ] " + msg,tr);
	}
	
	public static void e(String tag, String msg,Throwable tr) {
		if (isDug)
			Log.e(TAG, "[ " + tag + " ] " + msg,tr);
	}
	
	public static String makeLogTag(Class cls) {
        return  cls.getSimpleName();
    }
}
