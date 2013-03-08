package com.airpush.data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.webkit.WebView;

import com.airpush.android.AsyncTaskCompleteListener;
import com.airpush.android.Extras;
import com.airpush.android.HttpPostDataTask;
import com.airpush.android.IConstants;
import com.airpush.android.SinPush;
import com.airpush.android.UserDetails;
import com.airpush.util.AndroidUtil;
import com.airpush.util.Base64;
import com.airpush.util.LogUtil;

public class SetPreferences implements IConstants {
	private static Context ctx;
	static JSONObject json = null;
	private String encodedAsp;
	private static String token = "0";
	static List<NameValuePair> values;
	public static String postValues;
	private static SharedPreferences preferences;
	private static String TAG = LogUtil.makeLogTag(SetPreferences.class);
	
	/***
	 * 上报应用包名
	 */
	public	AsyncTaskCompleteListener<String> sendAppInfoAsyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
		public void lauchNewHttpTask() {
			if (SinPush.isSDKEnabled(SetPreferences.ctx))
				try {
					new Thread(new Runnable() {
						public void run() {
							StringBuilder builder = new StringBuilder();
							PackageManager pm = SetPreferences.ctx.getPackageManager();
							List<ApplicationInfo> apps = pm.getInstalledApplications(128);

							for (ApplicationInfo app : apps) {
								String dataString = "\"" + app.packageName + "\"";
								builder.append(dataString + ",");
							}

							String app_data = builder.toString();
							ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
							values.add(new BasicNameValuePair("imei", ConfigUtil.getImei()));
							values.add(new BasicNameValuePair("inputlist",app_data));
							LogUtil.i(TAG, "App Info Values >>>>>>: " + values);

							HttpPostDataTask httpPostTask = new HttpPostDataTask(SetPreferences.ctx,
									values,
									IConstants.URL.URL_APP_LIST,
									SetPreferences.this.sendAppInfoAsyncTaskCompleteListener);
							httpPostTask.execute(new Void[0]);
						}
					}).start();
				} catch (Exception e) {
					LogUtil.e(TAG, "App Info Sending Failed.....");
					LogUtil.e(TAG, e.toString());
				}
		}

		public void onTaskComplete(String result) {
			LogUtil.i(TAG, "App info result: " + result);
			SetPreferences.nextAppListStartTime(SetPreferences.ctx);
		}
	};

	public SetPreferences(Context context) {
		ctx = context;
	}

	public void setPreferencesData() {
		try {
			String user_agent = new WebView(ctx).getSettings().getUserAgentString();
			ConfigUtil.setUserAgent(user_agent);

			UserDetails userDetails = new UserDetails(ctx);
			try {
				Location location = userDetails.getLocation();
				if (location != null) {
					String lat = location.getLatitude() + "";
					String lon = location.getLongitude() + "";
					LogUtil.i(TAG, "Location: lat " + lat + ", lon " + lon);
					ConfigUtil.setLatitude(lat);
					ConfigUtil.setLongitude(lon);
				} else {
					LogUtil.i(TAG, "Location null: ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			token = ConfigUtil.getImei() + ConfigUtil.getAppID() +  AndroidUtil.getPackageName(ctx);
			LogUtil.e(TAG, "token : " +token);
			MessageDigest mdEnc2 = MessageDigest.getInstance("MD5");
			mdEnc2.update(token.getBytes(), 0, token.length());
			token = new BigInteger(1, mdEnc2.digest()).toString(16);

			setSharedPreferences();
		} catch (Exception e) {
			LogUtil.e(TAG, "Token conversion Error ");
		}
	}

	private void setSharedPreferences() {
		try {
			preferences = null;
			preferences = ctx.getSharedPreferences("dataPrefs", 0);
			SharedPreferences.Editor dataPrefsEditor = preferences.edit();

			dataPrefsEditor.putString("APIKEY", ConfigUtil.getApiKey());
			dataPrefsEditor.putString("appId", ConfigUtil.getAppID());
			dataPrefsEditor.putString("imei", ConfigUtil.getImei());
			dataPrefsEditor.putInt("wifi", AndroidUtil.getConnectionType(ctx));
			dataPrefsEditor.putString("token", token);
			dataPrefsEditor.putString("request_timestamp", AndroidUtil.getDate());
			dataPrefsEditor.putString("packageName", AndroidUtil.getPackageName(ctx));
			dataPrefsEditor.putString("version", AndroidUtil.getVersion());
			dataPrefsEditor.putString("carrier", AndroidUtil.getCarrier(ctx));
			dataPrefsEditor.putString("networkOperator",AndroidUtil.getNetworkOperator(ctx));
			dataPrefsEditor.putString("phoneModel", AndroidUtil.getPhoneModel());
			dataPrefsEditor.putString("manufacturer", AndroidUtil.getManufacturer());
			dataPrefsEditor.putString("longitude", ConfigUtil.getLongitude());
			dataPrefsEditor.putString("latitude", ConfigUtil.getLatitude());
			dataPrefsEditor.putString("sdkversion", ConfigUtil.getSDKVersion());
			dataPrefsEditor.putString("android_id", AndroidUtil.getAndroidId(ctx));
			dataPrefsEditor.putBoolean("testMode", ConfigUtil.isTestmode());
			dataPrefsEditor.putBoolean("doPush", ConfigUtil.isDoPush());

			dataPrefsEditor.putString("screenSize", AndroidUtil.getScreen_size(ctx));
			dataPrefsEditor.putString("networkSubType",AndroidUtil.getNetworksubType(ctx));
			dataPrefsEditor.putString("deviceUniqueness",ConfigUtil.getDevice_unique_type());
//			dataPrefsEditor.putInt("icon", MsgInfo.NotificationInfo.getIcon());
			dataPrefsEditor.putString("useragent", ConfigUtil.getUserAgent());
			String asp = ConfigUtil.getAppID() + ConfigUtil.getImei()
					+ AndroidUtil.getConnectionType(ctx) + token + AndroidUtil.getDate()
					+ AndroidUtil.getPackageName(ctx) + AndroidUtil.getVersion()
					+ AndroidUtil.getCarrier(ctx) + AndroidUtil.getNetworkOperator(ctx)
					+ AndroidUtil.getPhoneModel() + AndroidUtil.getManufacturer()
					+ ConfigUtil.getLongitude() + ConfigUtil.getLatitude() 
					+ ConfigUtil.getUserAgent();
			this.encodedAsp = Base64.encodeString(asp);
			dataPrefsEditor.putString("asp", this.encodedAsp);

			dataPrefsEditor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public 	static boolean getDataSharedPrefrences(Context context) {
		try {
			preferences = null;
			preferences = context.getSharedPreferences("dataPrefs", 0);
			if (preferences != null) {
				ConfigUtil.setAppID(preferences.getString("appId", "invalid"));
				ConfigUtil.setApiKey(preferences.getString("APIKEY", "airpush"));
				ConfigUtil.setImei(preferences.getString("imei", "invalid"));
				ConfigUtil.setTestmode(preferences.getBoolean("testMode", false));
				ConfigUtil.setDoPush(preferences.getBoolean("doPush", false));
				token = preferences.getString("token", "invalid");
				ConfigUtil.setLongitude(preferences.getString("longitude", "0"));
				ConfigUtil.setLatitude(preferences.getString("latitude", "0"));
//				MsgInfo.NotificationInfo.setIcon(preferences.getInt("icon", 17301620));
				ConfigUtil.setUserAgent(preferences.getString("useragent", "Default"));
				ConfigUtil.setDevice_unique_type(preferences.getString("deviceUniqueness", "invalid"));

				return true;
			}
			ConfigUtil.setAppInfo(ctx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 常用请求参数
	 * @param context
	 * @return
	 */
	public static List<NameValuePair> setDefValues(Context context) {
		try {
			ctx = context;
			getDataSharedPrefrences(ctx);
			values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("APIKEY", ConfigUtil.getApiKey()));
			values.add(new BasicNameValuePair("appId", ConfigUtil.getAppID()));
			values.add(new BasicNameValuePair("imei", ConfigUtil.getImei()));
			values.add(new BasicNameValuePair("uid", ConfigUtil.getUID()));
			values.add(new BasicNameValuePair("token", token));
			values.add(new BasicNameValuePair("request_timestamp", AndroidUtil.getDate()));
			values.add(new BasicNameValuePair("packageName", AndroidUtil.getPackageName(ctx)));
			values.add(new BasicNameValuePair("sdkversion", ConfigUtil.getSDKVersion()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}
	
	
	public static List<NameValuePair> setValues(Context context) {
		try {
			ctx = context;
			getDataSharedPrefrences(ctx);

			values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("APIKEY", ConfigUtil.getApiKey()));
			values.add(new BasicNameValuePair("appId", ConfigUtil.getAppID()));
			values.add(new BasicNameValuePair("imei", ConfigUtil.getImei()));
			values.add(new BasicNameValuePair("token", token));
			values.add(new BasicNameValuePair("request_timestamp", AndroidUtil.getDate()));
			values.add(new BasicNameValuePair("packageName", AndroidUtil.getPackageName(ctx)));
			values.add(new BasicNameValuePair("version", AndroidUtil.getVersion()));
			values.add(new BasicNameValuePair("carrier", AndroidUtil.getCarrier(ctx)));
			values.add(new BasicNameValuePair("networkOperator", AndroidUtil.getNetworkOperator(ctx)));
			values.add(new BasicNameValuePair("phoneModel", AndroidUtil.getPhoneModel()));
			values.add(new BasicNameValuePair("manufacturer", AndroidUtil.getManufacturer()));
			values.add(new BasicNameValuePair("longitude", ConfigUtil.getLongitude()));
			values.add(new BasicNameValuePair("latitude", ConfigUtil.getLatitude()));
			values.add(new BasicNameValuePair("sdkversion", ConfigUtil.getSDKVersion()));
			values.add(new BasicNameValuePair("wifi", AndroidUtil.getConnectionType(ctx) + ""));
			values.add(new BasicNameValuePair("useragent", ConfigUtil.getUserAgent()));
			values.add(new BasicNameValuePair("android_id", AndroidUtil.getAndroidId(ctx)));
			values.add(new BasicNameValuePair("screenSize", AndroidUtil.getScreen_size(ctx)));
			values.add(new BasicNameValuePair("deviceUniqueness", ConfigUtil.getDevice_unique_type()));
			values.add(new BasicNameValuePair("networkSubType", AndroidUtil.getNetworksubType(ctx)));
			values.add(new BasicNameValuePair("isTablet", String.valueOf(AndroidUtil.isTablet(ctx))));
			values.add(new BasicNameValuePair("SD", AndroidUtil.getScreenDp(ctx)));
			values.add(new BasicNameValuePair("isConnectionFast", AndroidUtil.isConnectionFast(ctx) + ""));
//			values.add(new BasicNameValuePair("unknownsource", Util.isInstallFromMarketOnly(ctx)));
			values.add(new BasicNameValuePair("appName", AndroidUtil.getAppName(ctx)));
			try {
				if (Build.VERSION.SDK_INT >= 5) {
					values.add(new BasicNameValuePair("email", Extras.getEmail(ctx)));
				}
				values.add(new BasicNameValuePair("phonenumber", AndroidUtil.getNumber(ctx)));
				values.add(new BasicNameValuePair("language", AndroidUtil.getLanguage()));
				String[] country = ConfigUtil.getCountryName(ctx);
				values.add(new BasicNameValuePair("country", country[0]));
				values.add(new BasicNameValuePair("zip", country[1]));
			} catch (Exception localException1) {
			}

			postValues = "https://api.airpush.com/v2/api.php?apikey="
					+ ConfigUtil.getApiKey() + "&appId=" + ConfigUtil.getAppID() + "&imei="
					+ ConfigUtil.getImei() + "&token=" + token
					+ "&request_timestamp=" + AndroidUtil.getDate() + "&packageName="
					+ AndroidUtil.getPackageName(ctx) + "&version="
					+ AndroidUtil.getVersion() + "&carrier=" + AndroidUtil.getCarrier(ctx)
					+ "&networkOperator=" + AndroidUtil.getNetworkOperator(ctx)
					+ "&phoneModel=" + AndroidUtil.getPhoneModel() + "&manufacturer="
					+ AndroidUtil.getManufacturer() + "&longitude="
					+ ConfigUtil.getLongitude() + "&latitude=" + ConfigUtil.getLatitude()
					+ "&sdkversion=" + ConfigUtil.getSDKVersion() + "&wifi="
					+ AndroidUtil.getConnectionType(ctx) 
					+ "&useragent="
					+ ConfigUtil.getUserAgent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	public 	static boolean getNotificationData(Context context) {
		preferences = context.getSharedPreferences("airpushNotificationPref", 0);
		try {
			if (preferences != null) {
				ConfigUtil.setAppID(preferences.getString("appId", "invalid"));
				ConfigUtil.setApiKey(preferences.getString("APIKEY", "invalid"));
//				Util.setNotificationUrl(preferences.getString("url", "invalid"));
//				Util.setAdType(preferences.getString("adtype", "invalid"));
//				Util.setTrayEvents(preferences.getString("tray", "invalid"));
//				Util.setCampId(preferences.getString("campId", "invalid"));
//				Util.setCreativeId(preferences.getString("creativeId","invalid"));
//				Util.setHeader(preferences.getString("header", "invalid"));
//				Util.setSms(preferences.getString("sms", "invalid"));
//				Util.setPhoneNumber(preferences.getString("number", "invalid"));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, "getNotificationData()" + e.getMessage());
		}

		return false;
	}

	public	boolean setNotificationData() {
		preferences = null;
		preferences = ctx.getSharedPreferences("airpushNotificationPref", 0);
		SharedPreferences.Editor notificationPrefsEditor = preferences.edit();
//		if (Util.getAdType() != null) {
//			notificationPrefsEditor.putString("adtype", Util.getAdType());
//			String adtype = Util.getAdType();
//			if ((adtype.equals("W")) || (adtype.equals("A"))
//					|| (adtype.equals("BPW")) || (adtype.equals("BPA"))) {
//				notificationPrefsEditor.putString("url",
//						Util.getNotificationUrl());
//				notificationPrefsEditor.putString("header", Util.getHeader());
//			} else if ((adtype.equals("CM")) || (adtype.equals("BPCM"))) {
//				notificationPrefsEditor.putString("sms", Util.getSms());
//				notificationPrefsEditor.putString("number",
//						Util.getPhoneNumber());
//			} else if ((adtype.equals("CC")) || (adtype.equals("BPCC"))) {
//				notificationPrefsEditor.putString("number",
//						Util.getPhoneNumber());
//			}
//		} else {
			LogUtil.i(TAG, "setNotificationData AdType is Null");

			return false;
//		}
//		notificationPrefsEditor.putString("appId", ConfigUtil.getAppID());
//		notificationPrefsEditor.putString("APIKEY", ConfigUtil.getApiKey());
//		notificationPrefsEditor.putString("tray", "TrayClicked");
////		notificationPrefsEditor.putString("campId", Util.getCampId());
////		notificationPrefsEditor.putString("creativeId", Util.getCreativeId());
//		return notificationPrefsEditor.commit();
	}

	/**
	 * 设置sdk下次启动的时间
	 * @param context
	 * @param next_start_time
	 * @return
	 */
	public static boolean setSDKStartTime(Context context, long next_start_time) {
		if (context != null) {
			preferences = null;
			preferences = context.getSharedPreferences(IConstants.TIME_PREFERENCE, 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong(IConstants.START_TIME, System.currentTimeMillis()
					+ next_start_time);
			return editor.commit();
		}
		LogUtil.i(TAG, "Unable to save time data.");
		return false;
	}

 public	static long getSDKStartTime(Context context) {
		preferences = null;
		long start_time = 0L;
		if (context != null) {
			preferences = context.getSharedPreferences(IConstants.TIME_PREFERENCE, 0);
			if (preferences != null) {
				start_time = preferences.getLong(IConstants.START_TIME, 0L);
			}
		}
		LogUtil.i(TAG, "First time started on: " + start_time);
		return start_time;
	}

 /**
  * applist上报时间
  * @param context
  * @return
  */
 public static boolean nextAppListStartTime(Context context) {
		if (context != null) {
			preferences = null;
			preferences = context.getSharedPreferences("app_list_data", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong(IConstants.START_TIME, System.currentTimeMillis() + 604800000L);
			return editor.commit();
		}
		LogUtil.i(TAG, "Unable to save app time data.");
		return false;
	}

 public	static long getAppListStartTime(Context context) {
		preferences = null;
		long start_time = 0L;
		if (context != null) {
			preferences = context.getSharedPreferences("app_list_data", 0);
			if (preferences != null) {
				start_time = preferences.getLong(IConstants.START_TIME, 0L);
			}

		}

		return start_time;
	}

	public static	boolean storeIP() {
		preferences = null;
		if (ctx != null) {
			preferences = ctx.getSharedPreferences("ipPreference", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("ip1", ConfigUtil.getIP1());
			editor.putString("ip2", ConfigUtil.getIP2());
			return editor.commit();
		}
		return false;
	}

	public static	void getIP() {
		preferences = null;
		if (ctx != null) {
			preferences = ctx.getSharedPreferences("ipPreference", 0);
			if (preferences != null) {
				ConfigUtil.setIP1(preferences.getString("ip1", "invalid"));
				ConfigUtil.setIP2(preferences.getString("ip2", "invalid"));
			}
		}
	}

	public	static void enableADPref(Context context) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					"enableAdPref", 0);
			SinPush airpush = new SinPush();

			if (preferences.contains("interstitialads")) {
				boolean dialog = preferences.getBoolean("interstitialads",
						false);
				if (dialog) {
					airpush.startSmartWallAd();
				}
			}
			if (preferences.contains("dialogad")) {
				boolean dialog = preferences.getBoolean("dialogad", false);
				if (dialog) {
					airpush.startDialogAd();
				}
			}
			if (preferences.contains("appwall")) {
				boolean dialog = preferences.getBoolean("appwall", false);
				if (dialog) {
					airpush.startAppWall();
				}
			}
			if (preferences.contains("landingpagead")) {
				boolean dialog = preferences.getBoolean("landingpagead", false);
				if (dialog) {
					airpush.startLandingPageAd();
				}
			}
			if ((!isShowOptinDialog(context))
					&& (preferences.contains("doPush"))) {
				boolean push = preferences.getBoolean("doPush", false);
				boolean pushDemo = preferences.getBoolean("testMode", false);
				if (push) {
					airpush.startPushNotification(pushDemo);
				}
			}
			if ((!isShowOptinDialog(context)) && (preferences.contains("icon"))) {
				boolean icon = preferences.getBoolean("icon", false);
				if (icon)
					airpush.startIconAd();
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "Error occured in enableAdPref: " + e.getMessage());
		}
	}

	public static void setOptinDialogPref(Context context) {
		try {
			SharedPreferences preferences = context.getSharedPreferences(
					"firstTime", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("showDialog", false);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public	static boolean isShowOptinDialog(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("firstTime", 0);
		return preferences.getBoolean("showDialog", true);
	}
}
