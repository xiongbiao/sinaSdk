package com.airpush.android;

import com.airpush.util.LogUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;

public abstract class Extras {
	private static String TAG = LogUtil.makeLogTag(Extras.class);
	public static String getEmail(Context context) {
		String email = "";
		try {
			if ((Build.VERSION.SDK_INT >= 5) && (context.checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == 0)) {
				Account[] accounts = AccountManager.get(context)
						.getAccountsByType("com.google");
				email = accounts[0].name;
			}
		} catch (Exception e1) {
			LogUtil.e(TAG,  "No email account found.");
		}
		return email;
	}
}
