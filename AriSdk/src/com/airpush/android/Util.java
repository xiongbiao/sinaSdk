package com.airpush.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public class Util {
//	private static String imei;
//	private static String apiKey;
//	private static String appID;
//	private static boolean testmode;
//	private static boolean doPush;
//	private static String longitude = "0";
//	private static String latitude = "0";
//	private static int icon;
//	private static Context context;
//	private static String jsonstr;
//	private static String campId;
//	private static String creativeId;
//	private static String user_agent;
//	private static String device_unique_type;
//	private static String notification_title;
//	private static String notification_text;
//	private static String phoneNumber;
//	private static String adType = "";
//	private static String trayEvents;
//	private static String header;
//	private static String notificationUrl;
//	private static String sms;
//	private static String adImageUrl;
//	private static String delivery_time;
	private static long expiry_time;
//	private static String landingPageAdUrl;
//	private static long lastLocationTime = 0L;
//	private static String IP1;
//	private static String IP2;

	private static String TAG = LogUtil.makeLogTag(Util.class);

//	static {
//		imei = "0";
//		apiKey = "airpush";
//		appID = "0";
//		testmode = false;
//		doPush = false;
//	}

//	Util(Context tcontext) {
//		context = tcontext;
//	}

	 

//	static boolean isTablet(Context context) {
//		boolean isTablet = false;
//		try {
//			DisplayMetrics dm = context.getResources().getDisplayMetrics();
//			float screenWidth = dm.widthPixels / dm.xdpi;
//			float screenHeight = dm.heightPixels / dm.ydpi;
//			double size = Math.sqrt(Math.pow(screenWidth, 2.0D)+ Math.pow(screenHeight, 2.0D));
//			isTablet = size >= 6.0D;
//		} catch (NullPointerException e) {
//			LogUtil.e(TAG, e.getMessage());
//		} catch (Exception e) {
//			LogUtil.e(TAG, e.getMessage());
//		} catch (Throwable t) {
//			LogUtil.e(TAG, t.getMessage());
//		}
//		return isTablet;
//	}

//	static Context getContext() {
//		return context;
//	}
//
//	static void setContext(Context tcontext) {
//		context = tcontext;
//	}

//	public static String getImei() {
//		return imei;
//	}
//
//	static void setImei(String timei) {
//		imei = timei;
//	}
//
//	static String getApiKey() {
//		return apiKey;
//	}
//
//	static void setApiKey(String tapiKey) {
//		apiKey = tapiKey;
//	}
//
//	public	static String getAppID() {
//		return appID;
//	}
//
//	static void setAppID(String tappID) {
//		appID = tappID;
//	}
//
//	static boolean isTestmode() {
//		return testmode;
//	}
//
//	static void setTestmode(boolean ttestmode) {
//		testmode = ttestmode;
//	}
//
//	static boolean isDoPush() {
//		return doPush;
//	}
//
//	static void setDoPush(boolean tdoPush) {
//		doPush = tdoPush;
//	}

//	static void setUser_agent(String tuser_agent) {
//		user_agent = tuser_agent;
//	}
//
//	static String getUser_agent() {
//		return user_agent;
//	}

//	static String getLatitude() {
//		return latitude;
//	}
//
//	static void setLatitude(String tlatitude) {
//		latitude = tlatitude;
//	}
//
//	static String getLongitude() {
//		return longitude;
//	}
//
//	static void setLongitude(String tlongitude) {
//		longitude = tlongitude;
//	}

//	static void setLastLocationTime(long tlastLocationTime) {
//		lastLocationTime = tlastLocationTime;
//	}
//
//	static long getLastLocationTime() {
//		return lastLocationTime;
//	}

//	static String getDate() {
//		try {
//			String format = "yyyy-MM-dd HH:mm:ss";
//			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
//			String time = dateFormat.format(new Date()) + "_"
//					+ dateFormat.getTimeZone().getDisplayName() + "_"
//					+ dateFormat.getTimeZone().getID() + "_"
//					+ dateFormat.getTimeZone().getDisplayName(false, 0);
//			return time;
//		} catch (Exception e) {
//		}
//		return "00";
//	}

//	static String getPhoneModel() {
//		return Build.MODEL;
//	}
//
//	static String getVersion() {
//		return Build.VERSION.SDK_INT + "";
//	}
//
//	static String getAndroidId(Context context) {
//		if (context == null)
//			return "";
//		return Settings.Secure.getString(context.getContentResolver(),"android_id");
//	}

//	static void setIcon(int ticon) {
//		icon = ticon;
//	}
//
//	static int getIcon() {
//		return icon;
//	}



	 

  
//	static String getJsonstr() {
//		return jsonstr;
//	}
//
//	static void setJsonstr(Context ctx) {
//		final String urlString = IConstants.URL.URL_GET_APP_INFO + AndroidUtil.getPackageName(ctx);
//		try {
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						URL url = new URL(urlString);
//						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//						connection.setRequestMethod("GET");
//						connection.setConnectTimeout(2000);
//						connection.connect();
//						if (connection.getResponseCode() == 200) {
//							StringBuffer sb = new StringBuffer();
//							BufferedReader reader = new BufferedReader(
//									new InputStreamReader(connection
//											.getInputStream()));
//							String line;
//							while ((line = reader.readLine()) != null) {
//								sb.append(line);
//							}
//							Util.jsonstr = sb.toString();
//						}
//
//						connection.disconnect();
//					} catch (MalformedURLException localMalformedURLException) {
//					} catch (IOException localIOException) {
//					} catch (Exception localException) {
//					}
//				}
//			}).start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	static String getAppIdFromJSON() {
//		try {
//			JSONObject json = new JSONObject(getJsonstr());
//			return json.getString("appid");
//		} catch (JSONException e) {
//		}
//		return "";
//	}
//
//	static String getApiKeyFromJSON() {
//		try {
//			JSONObject json = new JSONObject(getJsonstr());
//			return json.getString("authkey");
//		} catch (JSONException e) {
//		}
//		return "invalid key";
//	}
//
//	static void setAppInfo(Context ctx) {
//		setJsonstr(ctx);
//		setAppID(getAppIdFromJSON());
//		setApiKey(getApiKeyFromJSON());
//	}

//public	static String getCampId() {
//		return campId;
//	}
//
//public	static void setCampId(String tcampId) {
//		campId = tcampId;
//	}
//
//public	static String getCreativeId() {
//		return creativeId;
//	}
//
//public	static void setCreativeId(String tcreativeId) {
//		creativeId = tcreativeId;
//	}
//
//	static String getPhoneNumber() {
//		return phoneNumber;
//	}
//
//	public	static void setPhoneNumber(String tphoneNumber) {
//		phoneNumber = tphoneNumber;
//	}
//
//	public static String getAdType() {
//		return adType;
//	}
//
//	public	static void setAdType(String tadType) {
//		adType = tadType;
//	}
//
//	static String getTrayEvents() {
//		return trayEvents;
//	}
//
//	public	static void setTrayEvents(String ttrayEvents) {
//		trayEvents = ttrayEvents;
//	}
//
//	static String getHeader() {
//		return header;
//	}
//
//	public static void setHeader(String theader) {
//		header = theader;
//	}
//
//	static String getNotificationUrl() {
//		return notificationUrl;
//	}
//
//	public 	static void setNotificationUrl(String tnotificationUrl) {
//		notificationUrl = tnotificationUrl;
//	}
//
//	static String getNotification_title() {
//		return notification_title;
//	}
//
//	static void setNotification_title(String tnotification_title) {
//		notification_title = tnotification_title;
//	}
//
//	static String getNotification_text() {
//		return notification_text;
//	}
//
//	static void setNotification_text(String tnotification_text) {
//		notification_text = tnotification_text;
//	}
//
//	static String getAdImageUrl() {
//		return adImageUrl;
//	}
//
//	static void setAdImageUrl(String tadImageUrl) {
//		adImageUrl = tadImageUrl;
//	}
//
//	static String getDelivery_time() {
//		return delivery_time;
//	}
//
//	static void setDelivery_time(String tdelivery_time) {
//		delivery_time = tdelivery_time;
//	}
//
	static long getExpiry_time() {
		return expiry_time;
	}

	static void setExpiry_time(long texpiry_time) {
		expiry_time = texpiry_time;
	}
//
//	static String getSms() {
//		return sms;
//	}
//
//	public static void setSms(String tsms) {
//		sms = tsms;
//	}

//	static long getMessageIntervalTime() {
//		if (testmode) {
//			return 120000L;
//		}
//		return 14400000L;
//	}

//	static String getDevice_unique_type() {
//		return device_unique_type;
//	}
//
//	static void setDevice_unique_type(String tdevice_unique_type) {
//		device_unique_type = tdevice_unique_type;
//	}
//
//	static String getScreen_size(Context context) {
//		String size = "";
//		if (context != null) {
//			Display display = ((WindowManager) context
//					.getSystemService("window")).getDefaultDisplay();
//			size = display.getWidth() + "_" + display.getHeight();
//		}
//		return size;
//	}

//	static String getIP1() {
//		return IP1;
//	}
//
//	static void setIP1(String iP1) {
//		IP1 = iP1;
//	}
//
//	static String getIP2() {
//		return IP2;
//	}
//
//	static void setIP2(String iP2) {
//		IP2 = iP2;
//	}

//	public static String getLandingPageAdUrl() {
//		return landingPageAdUrl;
//	}
//
//	static void setLandingPageAdUrl(String fullPageAdUrl) {
//		landingPageAdUrl = fullPageAdUrl;
//	}

//	static String[] getCountryName(Context context) {
//		String[] country = { "", "" };
//		try {
//			Geocoder geocoder = new Geocoder(context);
//			if ((latitude == null) || (latitude.equals("invalid")) || (longitude == null) || (longitude.equals("invalid")))
//				return country;
//			List<Address> addresses = geocoder.getFromLocation(
//					Double.parseDouble(latitude),
//					Double.parseDouble(longitude), 1);
//			if (!addresses.isEmpty()) {
//				country[0] = ((Address) addresses.get(0)).getCountryName();
//				country[1] = ((Address) addresses.get(0)).getPostalCode();
//				LogUtil.i(TAG, "Postal Code: " + country[1] + " Country Code: "
//						+ ((Address) addresses.get(0)).getCountryCode());
//			}
//		} catch (IOException localIOException) {
//		} catch (Exception localException) {
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		return country;
//	}

//	static String getLanguage() {
//		Locale locale = Locale.getDefault();
//		return locale.getDisplayLanguage();
//	}
//
//	static String getNumber(Context context) {
//		String number = "";
//		if (context != null) {
//			TelephonyManager manager = (TelephonyManager) context
//					.getSystemService("phone");
//			if (manager != null)
//				number = manager.getLine1Number();
//		}
//		return number;
//	}
//
//	static String getScreenDp(Context context) {
//		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//		float density = metrics.density;
//		return density + "";
//	}

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

	// static void printDebugLog(String message) {
	// boolean isLogable = false;
	// }
	//
	// static void printLog(String message) {
	// Log.d("System.out", " " + message);
	// }
}
