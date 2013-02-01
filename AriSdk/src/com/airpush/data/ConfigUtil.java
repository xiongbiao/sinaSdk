package com.airpush.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import com.airpush.android.IConstants;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

/**
 * 用户配置参数
 * @author xiong
 *
 */
public class ConfigUtil {
	private static final String TAG = LogUtil.makeLogTag(ConfigUtil.class);
	private static String imei;
	private static String apiKey;
	private static String appID;
	private static boolean testmode;
	private static boolean doPush;
	private static String longitude = "0";
	private static String latitude = "0";
	private static long lastLocationTime = 0L;
	private static String IP1;
	private static String IP2;
	private static String jsonstr;
	private static String UserAgent;
	private static String Device_unique_type;
	private static String UID;
	
	
	static {
		imei = "0";
		apiKey = "airpush";
		appID = "0";
		testmode = false;
		doPush = false;
	}
	
	
	public static String getUID() {
		return UID;
	}
	public static void setUID(String uID) {
		UID = uID;
	}
	public static String getDevice_unique_type() {
		return Device_unique_type;
	}
	public static void setDevice_unique_type(String device_unique_type) {
		Device_unique_type = device_unique_type;
	}
	public static String getUserAgent() {
		return UserAgent;
	}
	public static void setUserAgent(String userAgent) {
		UserAgent = userAgent;
	}
	public static long getLastLocationTime() {
		return lastLocationTime;
	}
	public static void setLastLocationTime(long lastLocationTime) {
		ConfigUtil.lastLocationTime = lastLocationTime;
	}
	public static String getIP1() {
		return IP1;
	}
	public static void setIP1(String iP1) {
		IP1 = iP1;
	}
	public static String getIP2() {
		return IP2;
	}
	public static void setIP2(String iP2) {
		IP2 = iP2;
	}
	public static boolean isTestmode() {
		return testmode;
	}
	public static void setTestmode(boolean testmode) {
		ConfigUtil.testmode = testmode;
	}
	public static boolean isDoPush() {
		return doPush;
	}
	public static void setDoPush(boolean doPush) {
		ConfigUtil.doPush = doPush;
	}
	public static String getLongitude() {
		return longitude;
	}
	public static void setLongitude(String longitude) {
		ConfigUtil.longitude = longitude;
	}
	public static String getLatitude() {
		return latitude;
	}
	public static void setLatitude(String latitude) {
		ConfigUtil.latitude = latitude;
	}
	public static String getImei() {
		return imei;
	}
	public static void setImei(String imei) {
		ConfigUtil.imei = imei;
	}
	public static String getApiKey() {
		return apiKey;
	}
	public static void setApiKey(String apiKey) {
		ConfigUtil.apiKey = apiKey;
	}
	public static String getAppID() {
		return appID;
	}
	public static void setAppID(String appID) {
		ConfigUtil.appID = appID;
	}
	
	
	public static boolean getDataFromManifest(Context context) {
		try {
			ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
			Bundle bundle = applicationInfo.metaData;
			String appid = bundle.get(IConstants.APPID_MANIFEST).toString();
			if (!StringUtils.isEmpty(appid)) {
				setAppID(appid);
			}
			String apikey = "";
			try {
				apikey = bundle.get(IConstants.APIKEY_MANIFEST).toString();
				if (!StringUtils.isEmpty(apikey)){
					StringTokenizer stringTokenizer = new StringTokenizer(apikey, "*");
					stringTokenizer.nextToken();
					apikey = stringTokenizer.nextToken();
					setApiKey(apikey);
				} else {
					setApiKey("airpush");
				}
			} catch (Exception e) {
				LogUtil.e(TAG, "Problem with fetching apiKey.");
				setApiKey("airpush");
			}
			LogUtil.i(TAG, "AppId: " + appid + " ApiKey=" + apikey);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			LogUtil.e(TAG, "AppId or ApiKey not found in Manifest. Please add.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public	static long getMessageIntervalTime() {
		if (testmode) {
			return 120000L;
		}
		return 14400000L;
	}
	
	public static void setAppInfo(Context ctx) {
		setJsonstr(ctx);
		setAppID(getAppIdFromJSON());
		setApiKey(getApiKeyFromJSON());
	}
	
	public static String getAppIdFromJSON() {
		try {
			JSONObject json = new JSONObject(getJsonstr());
			return json.getString("appid");
		} catch (JSONException e) {
		}
		return "";
	}

	public static String getApiKeyFromJSON() {
		try {
			JSONObject json = new JSONObject(getJsonstr());
			return json.getString("authkey");
		} catch (JSONException e) {
		}
		return "invalid key";
	}
	
	static String getJsonstr() {
		return jsonstr;
	}	
	
	static void setJsonstr(Context ctx) {
		final String urlString = IConstants.URL.URL_GET_APP_INFO + AndroidUtil.getPackageName(ctx);
		try {
			new Thread(new Runnable() {
				public void run() {
					try {
						URL url = new URL(urlString);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.setConnectTimeout(2000);
						connection.connect();
						if (connection.getResponseCode() == 200) {
							StringBuffer sb = new StringBuffer();
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(connection
											.getInputStream()));
							String line;
							while ((line = reader.readLine()) != null) {
								sb.append(line);
							}
							ConfigUtil.jsonstr = sb.toString();
						}
						connection.disconnect();
					} catch (MalformedURLException localMalformedURLException) {
					} catch (IOException localIOException) {
					} catch (Exception localException) {
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getSDKVersion() {
		return "5.0";
	}
	
	public static String getVersion() {
		return Build.VERSION.SDK_INT + "";
	}
	
	public static String[] getCountryName(Context context) {
		String[] country = { "", "" };
		try {
			Geocoder geocoder = new Geocoder(context);
			if ((latitude == null) || (latitude.equals("invalid")) || (longitude == null) || (longitude.equals("invalid")))
				return country;
			List<Address> addresses = geocoder.getFromLocation(
					Double.parseDouble(latitude),
					Double.parseDouble(longitude), 1);
			if (!addresses.isEmpty()) {
				country[0] = ((Address) addresses.get(0)).getCountryName();
				country[1] = ((Address) addresses.get(0)).getPostalCode();
				LogUtil.i(TAG, "Postal Code: " + country[1] + " Country Code: "
						+ ((Address) addresses.get(0)).getCountryCode());
			}
		} catch (IOException localIOException) {
		} catch (Exception localException) {
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return country;
	}
}
