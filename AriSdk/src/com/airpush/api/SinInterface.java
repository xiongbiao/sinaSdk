package com.airpush.api;

import android.content.Context;

import com.airpush.util.LogUtil;

public class SinInterface {

	private static String TAG = LogUtil.makeLogTag(SinInterface.class);
	private static Context mContext;

	public static Context getmContext() {
		return mContext;
	}

	public static void init(Context context) {
		LogUtil.d(TAG, "action:init");
		if (context == null) {
			LogUtil.e(TAG, "Context must not be null.");
			return;
		}

		mContext = context.getApplicationContext();

		// 配置检查、
		// 注册用户

	}

	/**
	 * 设置测试模式
	 * 
	 * @param isTest
	 */
	public static void setMode(boolean isTest) {

	}

	/**
	 * 开启推送
	 * @param openPush
	 */
	public static void setOpenPush(boolean openPush) {
        
	}
	
	public static void getOpen(){
		 
	}
}
