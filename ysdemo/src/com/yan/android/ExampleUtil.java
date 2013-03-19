package com.yan.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class ExampleUtil {
//    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
//    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
//    public static final String PREFS_START_TIME = "PREFS_START_TIME";
//    public static final String PREFS_END_TIME = "PREFS_END_TIME";
      public static final String KEY_APP_KEY = "AppKey";
      private static String TAG = LogUtil.makeLogTag(ExampleUtil.class);
    
    // 校验Tag Alias 只能是数字和英文字母
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo( context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (NameNotFoundException e) {

        }
        return appKey;
    }
    
    // 取得版本号
    public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}
    /**
     * 是否存在網絡
     * @param context
     * @return
     */
    public static boolean checkInternetConnection(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if ((cm.getActiveNetworkInfo() != null)&& (cm.getActiveNetworkInfo().isAvailable())&& (cm.getActiveNetworkInfo().isConnected())) {
				return true;
			}
		  	LogUtil.e(TAG, "Internet Connection not found.");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    /**
     * 取IMEI
     * @param context
     * @return
     */
    public static String geiIMEI(Context context){
    	TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	String imei=telephonyManager.getDeviceId();
    	return imei;
    }
}
