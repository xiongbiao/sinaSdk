package com.airpush.api;

import java.util.StringTokenizer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.airpush.android.IConstants;
import com.airpush.data.ConfigUtil;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

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

		//配置检查、
		//注册用户
		
	}
	
 
}
