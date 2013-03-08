package com.airpush.service;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.airpush.util.LogUtil;

public class StatusCode {
	
	public static final int ERRCODE_OK 				= 0;
	// buffer size is 1.2k. If exceed this size, will be give up
	public static final int ERRCODE_SENDBUFF 		= -1001;
	public static final int ERRCODE_CONN 			= -1000;
	public static final int ERRCODE_SEND 			= -998;
	public static final int ERRCODE_RECV 			= -997;
	public static final int ERRCODE_CLOSE 			= -996;
	public static final int ERRCODE_TIMEOUT 		= -994;
	public static final int ERRCODE_INVALIDSOCK 	= -993;
	
	private static final HashMap<Integer, String> errorMap = new HashMap<Integer, String>();
	static {
		errorMap.put(ERRCODE_OK, "OK");
		errorMap.put(ERRCODE_SENDBUFF, "Exceed buffer size");
		errorMap.put(ERRCODE_CONN, "Connection failed");
		errorMap.put(ERRCODE_SEND, "Sending failed or timeout");
		errorMap.put(ERRCODE_RECV, "Receiving failed or timeout");
		errorMap.put(ERRCODE_CLOSE, "Connection is closed");
		errorMap.put(ERRCODE_TIMEOUT, "Response timeout");
		errorMap.put(ERRCODE_INVALIDSOCK, "Invalid socket");
	}
	
	public static String getErrorDesc(int code) {
		if (!errorMap.containsKey(code)) {
			LogUtil.d("StatusCode", "Unknown error code - " + code);
			return "";
		}
		return errorMap.get(code);
	}
	
	
    public static final int RESULT_TYPE_AD_JSON_LOAD_SUC = 995;
    public static final int RESULT_TYPE_AD_JSON_LOAD_FAIL = 996;
    public static final int RESULT_TYPE_APK_FOUND_NOT_INSTAL = 997;
    public static final int RESULT_TYPE_APK_FOUND_INSTAL = 998;
    public static final int RESULT_TYPE_TARGET = 999;	// received AD
    public static final int RESULT_TYPE_CLICK = 1000;
    public static final int RESULT_TYPE_DOWN_SUCCEED = 1001;
    public static final int REPORT_APP_INSTALL_SUCCEED = 1002;
    public static final int RESULT_TYPE_AUTO_DOWN_SUCCEED = 1003;
    public static final int RESULT_TYPE_VIDEO_DOWN_SUCEED = 1004;
    public static final int RESULT_TYPE_VIDEO_CLICK = 1005;
    public static final int RESULT_TYPE_BTN_CANCEL = 1006;
    public static final int RESULT_TYPE_BTN_OK = 1007;
    public static final int RESULT_TYPE_VIDEO_CLICK_CLOSE = 1008;

    public static final int RESULT_TYPE_DOWN_FAIL = 1011;
    //下载失败后，状态栏显示让用户重新点击下载，用户点击后上报
    public static final int RESULT_TYPE_DOWN_AGAIN = 1012;
    
	// --------------------- 0.0.8 new added
    // 已经存在，大小相同，不再重新下载
    public static final int RESULT_TYPE_DOWN_EXIST = 1013;
    
    // failed to load webview page
    public static final int RESULT_TYPE_RESOURCE_REQUIRED_PRELOAD_FAILED = 1014;
    
    // failed to load image
    public static final int RESULT_TYPE_IMAGE_LOAD_FAIL= 1020;
    public static final int RESULT_TYPE_HTML_LOAD_FAIL= 1021;
    public static final int RESULT_TYPE_APK_LOAD_FAIL= 1022;
    // 下载完成后，通知栏上提示安装，用户点击了
    public static final int RESULT_TYPE_NOTIFICATION_INSTALL_CLICKED = 1015;
    
    // 某个资源发生了非预期的无效的参数，需要开发人员去定位
    public static final int RESULT_TPPE_INVALID_PARAM = 1100;
    
    //在WEBVIEW中用户点击拨打电话/发送邮件/发送短信/打开主页 等用户行为
    public static final int RESULT_TYPE_CLICK_CONTENT = 1016;
    
    public static final int RESULT_TYPE_CLICK_CALL = 1017;
    
    public static final int RESULT_TYPE_NOTIFACTION_SHOW = 1018;
    
    public static final int RESULT_TYPE_CLCIK_APPLIST = 1019;

	private static final HashMap<Integer, String> reportCodeMap = new HashMap<Integer, String>();
	static {
		reportCodeMap.put(RESULT_TYPE_AD_JSON_LOAD_SUC, "Message JSON parsing succeed");
		reportCodeMap.put(RESULT_TYPE_AD_JSON_LOAD_FAIL, "Message JSON parsing failed");
		reportCodeMap.put(RESULT_TYPE_APK_FOUND_NOT_INSTAL, "APK already installed, give up");
		reportCodeMap.put(RESULT_TYPE_APK_FOUND_INSTAL, "APK already installed, still process");
		
		reportCodeMap.put(RESULT_TYPE_TARGET, "Begin to parse AD");
		reportCodeMap.put(RESULT_TYPE_CLICK, "User clicked and opened the AD");
		reportCodeMap.put(RESULT_TYPE_DOWN_SUCCEED, "APK download succeed");
		reportCodeMap.put(REPORT_APP_INSTALL_SUCCEED, "APK install succeed");
		reportCodeMap.put(RESULT_TYPE_AUTO_DOWN_SUCCEED, "APK silence download succeed");
		
		reportCodeMap.put(RESULT_TYPE_VIDEO_DOWN_SUCEED, "Video silence downlaod succeed");
		reportCodeMap.put(RESULT_TYPE_VIDEO_CLICK, "User clicked video and jumped to url AD (browser)");
		reportCodeMap.put(RESULT_TYPE_VIDEO_CLICK_CLOSE, "Video is force closed by user");
		
		reportCodeMap.put(RESULT_TYPE_BTN_OK, "User clicked 'OK'");
		reportCodeMap.put(RESULT_TYPE_BTN_CANCEL, "User clicked 'Cancel'");
		reportCodeMap.put(RESULT_TYPE_DOWN_FAIL, "Download failed");
		reportCodeMap.put(RESULT_TYPE_DOWN_AGAIN, "User clicked to download again");

		reportCodeMap.put(RESULT_TYPE_DOWN_EXIST, "The file already exist and same size. Don't download again.");
		reportCodeMap.put(RESULT_TPPE_INVALID_PARAM, "Invalid param or unexpected result.");
		
		reportCodeMap.put(RESULT_TYPE_RESOURCE_REQUIRED_PRELOAD_FAILED, "Failed to preload required resource");
		reportCodeMap.put(RESULT_TYPE_NOTIFICATION_INSTALL_CLICKED, "User clicked install alert on status bar after downloading finished.");
		reportCodeMap.put(RESULT_TYPE_CLICK_CONTENT, "User clicked the webview's url");
		reportCodeMap.put(RESULT_TYPE_CLICK_CALL, "User clicked call action");
		reportCodeMap.put(RESULT_TYPE_NOTIFACTION_SHOW, "The AD show in the status bar");
		reportCodeMap.put(RESULT_TYPE_CLCIK_APPLIST, "Click applist and show the AD");
		reportCodeMap.put(RESULT_TYPE_IMAGE_LOAD_FAIL, "Down image failed");
		reportCodeMap.put(RESULT_TYPE_HTML_LOAD_FAIL, "Down html failed");
		reportCodeMap.put(RESULT_TYPE_APK_LOAD_FAIL, "Down apk failed");
	}
	
	public static String getReportDesc(int code) {
		if (!reportCodeMap.containsKey(code)) {
			LogUtil.d("StatusCode", "Unknown report code - " + code);
			return "";
		}
		return reportCodeMap.get(code);
	}

	// ------------------------ report content
	
	public static JSONObject buildPackageAddedReportContent(String packageName) {
		JSONObject pkg = new JSONObject();
		if (TextUtils.isEmpty(packageName)) return pkg;
		
		try {
			JSONObject o = new JSONObject();
			o.put("action", "add");
			o.put("appid", packageName);
			
			pkg.put("package", o);
		} catch (JSONException e) {
		}
		return pkg;
	}
	
	public static JSONObject buildPackageRemovedReportContent(String packageName) {
		JSONObject pkg = new JSONObject();
		if (TextUtils.isEmpty(packageName)) return pkg;
		
		try {
			JSONObject o = new JSONObject();
			o.put("action", "rmv");
			o.put("appid", packageName);
			
			pkg.put("package", o);
		} catch (JSONException e) {
		}
		return pkg;
	}
	
	private static long _lastUserPresentTime = 0;
	public static JSONObject buildUserActiveReportContent(boolean isActive) {
		int useTime = 0;
		if (isActive) {
			_lastUserPresentTime = System.currentTimeMillis();
		} else {
			if (_lastUserPresentTime != 0) {
				useTime = (int) ((System.currentTimeMillis() - _lastUserPresentTime) / 1000L);
			}
		}
		
		JSONObject userActive = new JSONObject();
		try {
			JSONObject o = new JSONObject();
			o.put("active", isActive ? 1 : 0);
			o.put("timestamp", System.currentTimeMillis());
			if (!isActive) {
				o.put("use_time", useTime);
			}
			
			userActive.put("user_active", o);
		} catch (JSONException e) {
		}
		return userActive;
	}
	
	public static String buildUnexpectedErrorContent(String packageName, String errorDescription) {
		return buildUnexpectedErrorContent(packageName, errorDescription, null, null);
	}
	
	public static String buildUnexpectedErrorContent(String packageName, 
			String errorDescription, String adId, String senderId) {
		if (TextUtils.isEmpty(packageName)) return "";
		
		JSONObject o = new JSONObject();
		try {
			o.put("error", errorDescription);
			o.put("appid", packageName);
			if (null != adId) {
				o.put("ad_id", adId);
			}
			if (null != senderId) {
				o.put("sender", senderId);
			}
		} catch (JSONException e) {
		}
		return o.toString();
	}
	
	
}


