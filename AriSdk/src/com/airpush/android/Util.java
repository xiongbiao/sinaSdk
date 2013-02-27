package com.airpush.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

import com.airpush.util.LogUtil;

public class Util {

	private static String TAG = LogUtil.makeLogTag(Util.class);

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

	static String getAppName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo ai;
			try {
				ai = pm.getApplicationInfo(context.getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				ai = null;
			}
			String applicationName = (String) (ai != null ? pm
					.getApplicationLabel(ai) : "(unknown)");
			return applicationName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	static String isInstallFromMarketOnly(Context context) {
		return Settings.Secure.getString(context.getContentResolver(),
				"install_non_market_apps");
	}

}
