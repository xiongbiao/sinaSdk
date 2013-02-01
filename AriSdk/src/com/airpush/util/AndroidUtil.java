package com.airpush.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.airpush.android.IConstants;
import com.airpush.data.ConfigUtil;
import com.airpush.data.MsgInfo;

public class AndroidUtil {
	private static String TAG = LogUtil.makeLogTag(AndroidUtil.class);
	private static final int NETWORK_TYPE_EHRPD = 14;
	private static final int NETWORK_TYPE_EVDO_B = 12;
	private static final int NETWORK_TYPE_HSPAP = 15;
	private static final int NETWORK_TYPE_IDEN = 11;
	private static final int NETWORK_TYPE_LTE = 13;
	private static final int NETWORK_TYPE_HSDPA = 8;
	private static final int NETWORK_TYPE_HSPA = 10;
	private static final int NETWORK_TYPE_HSUPA = 9;

	public static boolean isSdcardExist() {
        boolean ret = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!ret) {
        	LogUtil.d(TAG, "SDCard is not mounted");
        }
        return ret;
    }
	
	public static boolean isConnectionFast(Context context) {
		try {
			if (context == null)
				return false;
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if ((ni == null) || (!ni.isConnected())) {
				return false;
			}
			int type = ni.getType();
			if (type == 1) {
				LogUtil.i(TAG, "CONNECTED VIA WIFI");
				return true;
			}

			if (type == 0) {
				int subType = ni.getSubtype();
				switch (subType) {
				case 7:
					return false;
				case 4:
					return false;
				case 2:
					return false;
				case 5:
					return true;
				case 6:
					return true;
				case 1:
					return false;
				case NETWORK_TYPE_HSDPA:
					return true;
				case NETWORK_TYPE_HSPA:
					return true;
				case NETWORK_TYPE_HSUPA:
					return true;
				case 3:
					return true;
				case NETWORK_TYPE_EHRPD:
					return true;
				case NETWORK_TYPE_EVDO_B:
					return true;
				case NETWORK_TYPE_HSPAP:
					return true;
				case NETWORK_TYPE_IDEN:
					return false;
				case NETWORK_TYPE_LTE:
					return true;
				case 0:
					return false;
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	 public static boolean hasPermission(Context context, String thePermission) {
	        if (null == context || TextUtils.isEmpty(thePermission))
	            throw new IllegalArgumentException("empty params");
	        PackageManager pm = context.getPackageManager();
	        if (pm.checkPermission(thePermission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
	            return true;
	        }
	        return false;
	    }

	public static String getNetworksubType(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService("connectivity");
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if ((ni != null) && (ni.isConnected())
					&& (!ni.getTypeName().equals("WIFI"))) {
				return ni.getSubtypeName();
			}
		}
		return "";
	}
	
    public static Intent getIntentForStartPushActivity(Context context, MsgInfo entity, boolean isUpdateVersion) {
        Intent intent = new Intent();
//        intent.putExtra(IConstants.PushActivity.IS_UPDATE_VERSION, isUpdateVersion);
        intent.putExtra(IConstants.PARAM_BODY, entity);
//        intent.setAction(IConstants.PushActivity.ACTION_UA);
        intent.addCategory(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
	
	public static String getURlToFileName(String url){
		
		String filename = url.substring(url.lastIndexOf("/"), url.length());
		return filename;
	}
	
	public static int getConnectionType(Context ctx) {
		if (ctx == null)
			return 0;
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService("connectivity");
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if ((ni != null) && (ni.isConnected())
				&& (ni.getTypeName().equals("WIFI"))) {
			return 1;
		}
		return 0;
	}
	
	public static String getManufacturer() {
		return Build.MANUFACTURER;
	}
	
	
	public static String getPackageName(Context context) {
		try {
			return context.getPackageName();
		} catch (Exception e) {
		}
		return "";
	}

	public static String getCarrier(Context context) {
		if (context == null)
			return "";
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService("phone");
		if ((manager != null) && (manager.getSimState() == 5)) {
			return manager.getSimOperatorName();
		}
		return "";
	}

	public static String getNetworkOperator(Context context) {
		if (context == null) {
			return "";
		}
		TelephonyManager manager = (TelephonyManager) context.getSystemService("phone");
		if ((manager != null) && (manager.getPhoneType() == 1)) {
			return manager.getNetworkOperatorName();
		}
		return "";
	}
	
	public static String getDate() {
		try {
			String format = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			String time = dateFormat.format(new Date()) + "_"
					+ dateFormat.getTimeZone().getDisplayName() + "_"
					+ dateFormat.getTimeZone().getID() + "_"
					+ dateFormat.getTimeZone().getDisplayName(false, 0);
			return time;
		} catch (Exception e) {
		}
		return "00";
	}
	
	public static String getVersion() {
		return Build.VERSION.SDK_INT + "";
	}
	
	public static String getPhoneModel() {
		return Build.MODEL;
	}
	
	public static String getAndroidId(Context context) {
		if (context == null)
			return "";
		return Settings.Secure.getString(context.getContentResolver(),"android_id");
	}
	
	public static String getScreen_size(Context context) {
		String size = "";
		if (context != null) {
			Display display = ((WindowManager) context
					.getSystemService("window")).getDefaultDisplay();
			size = display.getWidth() + "_" + display.getHeight();
		}
		return size;
	}
	
	public static boolean isTablet(Context context) {
		boolean isTablet = false;
		try {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			float screenWidth = dm.widthPixels / dm.xdpi;
			float screenHeight = dm.heightPixels / dm.ydpi;
			double size = Math.sqrt(Math.pow(screenWidth, 2.0D)+ Math.pow(screenHeight, 2.0D));
			isTablet = size >= 6.0D;
		} catch (NullPointerException e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		} catch (Throwable t) {
			LogUtil.e(TAG, t.getMessage());
		}
		return isTablet;
	}
	
	public static String getNumber(Context context) {
		String number = "";
		if (context != null) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService("phone");
			if (manager != null)
				number = manager.getLine1Number();
		}
		return number;
	}
	
	public static String getLanguage() {
		Locale locale = Locale.getDefault();
		return locale.getDisplayLanguage();
	}


	public static String getScreenDp(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float density = metrics.density;
		return density + "";
	}
	
	public static String getAppName(Context context) {
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
	
	public static String getDownloadFailedClientInfo(Context context, String failedUrl ) {
        String androidSdkVersion = android.os.Build.VERSION.RELEASE + ","
                + Integer.toString(android.os.Build.VERSION.SDK_INT);
        String model = android.os.Build.MODEL;
        String device = android.os.Build.DEVICE;

        String appKey = StringUtils.isEmpty(ConfigUtil.getApiKey()) ? " " : ConfigUtil.getApiKey();
        String network =  getNetworkType(context);
        JSONObject json =  new JSONObject();
       
        try {
			json.put("androidSdkVersion", androidSdkVersion);
			json.put("model", model);
			json.put("device", device);
			json.put("appKey", appKey);
			json.put("network", network);
			json.put("url", failedUrl);
		} catch (JSONException e) {
			
		}
		return json.toString();
    }
	
	 public static String getNetworkType(Context context) {
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo();
	        String type = info.getTypeName();
	        String subtype = info.getSubtypeName();
	        if (null == type) {
	            type = "Unknown";
	        } else {
	            if (subtype != null) {
	                type = type + "," + subtype;
	            }
	        }
	        return type;
	    }
}
