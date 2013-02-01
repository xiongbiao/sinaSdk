package com.airpush.data;

import org.json.JSONObject;

import android.content.Context;

public class DownloadMsgInfo extends MsgInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1930438678487167454L;

	public String wUrl = "";
	public static final String KEY_WEB_URL = "w_url";

	public static final String KEY_DOWNLOAD_URL = "d_url";
	public String downloadUrl;

	public String dMd5;
	public static final String KEY_APK_MD5 = "d_md5";
	
	public String _apkSavedPath;	
	public boolean _isApkPreloadSucceed;	
	
	public String apkPackageName;
	public String apkMainActivity;
	public static final String KEY_APK_MAIN_ACTIVITY = "main_a";
	public static final String KEY_APK_PACKAGE_NAME = "package_n";
	
	
	@Override
	public void process(Context context) {}

	@Override
	public boolean parseMsgContent(Context congtext) {
		try {
			JSONObject thirdJsonObject = new JSONObject(mContent);
			this.wUrl = thirdJsonObject.optString(KEY_WEB_URL, "");
			this.downloadUrl = thirdJsonObject.optString(KEY_DOWNLOAD_URL, "");
			this.apkPackageName = thirdJsonObject.optString(KEY_APK_PACKAGE_NAME, "");
			this.apkMainActivity = thirdJsonObject.optString(KEY_APK_MAIN_ACTIVITY, "");
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

}
