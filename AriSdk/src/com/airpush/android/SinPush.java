package com.airpush.android;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.airpush.data.ConfigUtil;
import com.airpush.data.MsgInfo;
import com.airpush.data.SetPreferences;
import com.airpush.ui.OptinActivity;
import com.airpush.ui.SmartWallActivity;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

public class SinPush extends SDKIntializer {
	static final String TAG = LogUtil.makeLogTag(SinPush.class);
	/**
	 *   
	 *   1  接入
	 *   2  获取
	 *   3 每隔一段时间
	 *   
	 */
	private static Context mContext;

	private boolean isDialogClosed = false;
	
	public static Context getmContext() {
		return mContext;
	}
	
	Runnable optinRunnable = new Runnable() {
		public void run() {
			try {
				Intent intent = new Intent(SinPush.mContext, OptinActivity.class);
				intent.addFlags(268435456);
				intent.addFlags(67108864);
				SinPush.mContext.startActivity(intent);
			} catch (ActivityNotFoundException e) {
				LogUtil.e(TAG, "Required OptinActivity not declared in Manifest, Please add.");
				e.printStackTrace();
			} catch (Exception e) {
				LogUtil.e(TAG, "Error in Optin runnable: " + e.getMessage());
				e.printStackTrace();
			}
		}
	};

	Runnable userInfoRunnable = new Runnable() {
		public void run() {
			SinPush.this.sendUserInfo();
		}
	};

	public SinPush(Context context) {
		if (context == null) {
			LogUtil.e(TAG, "Context must not be null.");
			return;
		}
		mContext = context;
		this.isDialogClosed = false;
//		Util.setContext(mContext);
		//配置检查、
		if ((!ConfigUtil.getDataFromManifest(mContext)) || (!AndroidUtil.checkRequiredPermission(mContext))) {
			return;
		}
       /**
        * 注册用户
        */
//		BugSenseHandler.setup(mContext, "bcdf67df", ConfigUtil.getAppID());
		
		
		if (!new UserDetails(mContext).setImeiInMd5())
			return;
		new SetPreferences(mContext).setPreferencesData();
		SetPreferences.getDataSharedPrefrences(mContext);

		SharedPreferences SDKPrefs = context.getSharedPreferences("sdkPrefs", 0);
		if ((SDKPrefs == null) || (!SDKPrefs.contains("SDKEnabled"))) {
			enableSDK(context, true);
		}

		if (SetPreferences.isShowOptinDialog(mContext))
			new Handler().post(this.optinRunnable);
		else
			new Handler().postDelayed(this.userInfoRunnable, 5000L);
	}

	public	SinPush() {
		try {
			this.isDialogClosed = true;
			if (!SetPreferences.isShowOptinDialog(mContext))
				sendUserInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	boolean isSDKUser(Context context) {
		try {
			SharedPreferences SDKPrefs = context.getSharedPreferences("sdkUserPrefs", 0);
			if ((SDKPrefs != null) && (!SDKPrefs.equals(null))&& (SDKPrefs.contains("userId")))
			{
				String uid = SDKPrefs.getString("userId", "");
				if(!StringUtils.isEmpty(uid)){
					LogUtil.i(TAG, "login  uid : " + uid);
					ConfigUtil.setUID(uid);
					return  true;
				}else{
					LogUtil.i(TAG, "not  uid   ");
				}
			}	
			
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return false;
	}
	
	 static void setSDKUser(Context context, String userJson) {
		try {
			SharedPreferences SDKPrefs = context.getSharedPreferences("sdkUserPrefs", 0);
			SharedPreferences.Editor SDKPrefsEditor = SDKPrefs.edit();
			SDKPrefsEditor.putString("userId", userJson);
			SDKPrefsEditor.commit();
			LogUtil.i(TAG, "SDK add user id : " + userJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	void sendUserInfo() {
		if (isSDKEnabled(mContext)){
		  final	boolean isUsers =  isSDKUser( mContext);
			try {
				AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
					public void lauchNewHttpTask() {
						List<NameValuePair> values ;
						if(isUsers){
							values = SetPreferences.setDefValues(SinPush.mContext);
							values.add(new BasicNameValuePair("model", "user"));
							values.add(new BasicNameValuePair("action", "login"));
						}else{
						    values = SetPreferences.setValues(SinPush.mContext);
						    values.add(new BasicNameValuePair("model", "user"));
						    values.add(new BasicNameValuePair("action", "reg"));
						}
						LogUtil.ip(TAG, "UserInfo Values >>>>>>: " + values);
						
						HttpPostDataTask httpPostTask = new HttpPostDataTask( SinPush.mContext, values,IConstants.URL.URL_API_MESSAGE, this);
						httpPostTask.execute(new Void[0]);
					}

					public void onTaskComplete(String result) {
						LogUtil.i(TAG, "User Info Sent.");
						LogUtil.ir(TAG, "sendUserInfo >>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + result);
						try {
							JSONObject json = new JSONObject(result);
							if(json.optBoolean("saveUser")){
								SinPush.setSDKUser(mContext,json.optString("uid",""));
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							LogUtil.ir(TAG, "get user  >>>>>>>>>>>>>>>>>>>>>>>>>>>>> e : " + e.getMessage());
							e.printStackTrace();
						}
						
						long startTime = SetPreferences.getAppListStartTime(SinPush.mContext);
						if ((startTime == 0L) || (startTime < System.currentTimeMillis())) {
							if (AndroidUtil.checkInternetConnection(SinPush.mContext))
								new SetPreferences(SinPush.mContext).sendAppInfoAsyncTaskCompleteListener.lauchNewHttpTask();
						}
					}
				};
				asyncTaskCompleteListener.lauchNewHttpTask();
			} catch (Exception e) {
				LogUtil.e(TAG, "User Info Sending Failed.....");
				LogUtil.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
	}

	public void startPushNotification(boolean testMode) {
		try {
			if (SetPreferences.isShowOptinDialog(mContext)) {
				SharedPreferences preferences = mContext.getSharedPreferences("enableAdPref", 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("doPush", true);
				editor.putBoolean("testMode", testMode);
				editor.commit();
				return;
			}
			boolean permissionReceiveBootCompleted = mContext
					.checkCallingOrSelfPermission("android.permission.RECEIVE_BOOT_COMPLETED") == 0;
			if (!permissionReceiveBootCompleted)
				LogUtil.e(TAG, "Required permission android.permission.RECEIVE_BOOT_COMPLETED not added in manifest, Please add.");
			if ((AndroidUtil.checkRequiredPermission(mContext))
					&& (ConfigUtil.getDataFromManifest(mContext))) {
				ConfigUtil.setTestmode(testMode);
				ConfigUtil.setDoPush(true);
				PushNotification pushNotification = new PushNotification(
						mContext);
				pushNotification.startAirpush();
			}
		} catch (Exception e) {
			LogUtil.e(TAG,"Error in Start Push Notification: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void startIconAd() {
		try {
			if (SetPreferences.isShowOptinDialog(mContext)) {
				SharedPreferences preferences = mContext.getSharedPreferences(
						"enableAdPref", 0);
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean("icon", true);
				editor.commit();
				return;
			}
			LogUtil.i(TAG, "Push IconSearch....true");
			if ((AndroidUtil.checkRequiredPermission(mContext))
					&& (ConfigUtil.getDataFromManifest(mContext))) {
				if (!new UserDetails(mContext).setImeiInMd5())
					return;
				new SetPreferences(mContext).setPreferencesData();
				SetPreferences.getDataSharedPrefrences(mContext);
				if (mContext.checkCallingOrSelfPermission("com.android.launcher.permission.INSTALL_SHORTCUT") == 0)
					new IconAds(mContext);
				else
					LogUtil.i(TAG, "Installing shortcut permission not found in Manifest, please add.");
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "Error in StartIconAd: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void startSmartWallAd() {
		if ((!this.isDialogClosed) && (SetPreferences.isShowOptinDialog(mContext))) {
			SharedPreferences preferences = mContext.getSharedPreferences("enableAdPref", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("interstitialads", true);
			editor.commit();
			return;
		}
		if ((mContext != null) && (isSDKEnabled(mContext))) {
//			Util.setContext(mContext);
			if ((!ConfigUtil.getDataFromManifest(mContext)) || (!AndroidUtil.checkRequiredPermission(mContext))) {
				return;
			}
			if (!new UserDetails(mContext).setImeiInMd5())
				return;
			new SetPreferences(mContext).setPreferencesData();
			SetPreferences.getDataSharedPrefrences(mContext);

			AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
				public void onTaskComplete(String result) {
					LogUtil.i(TAG, "Interstitial JSON: " + result);
					try {
						if (result == null)
							return;
						JSONObject jsonObject = new JSONObject(result);
						String adtype = jsonObject.isNull("adtype") ? ""
								: jsonObject.getString("adtype");
						if ((!adtype.equals(""))&& (adtype.equalsIgnoreCase("AW"))) {
							SinPush.this.parseAppWallJson(result);
						} else {
							if (!adtype.equals("")) {
								if ((adtype.equalsIgnoreCase("DAU")) || (adtype.equalsIgnoreCase("DCC")) || (adtype.equalsIgnoreCase("DCM"))) {
									SinPush.this.parseDialogAdJson(result);
									return;
								}
							}
							if (!adtype.equals("")) {
								if (adtype.equalsIgnoreCase("FP"))
									SinPush.this.parseLandingPageAdJson(result);
							}
						}
					} catch (JSONException e) {
						LogUtil.e(TAG,"Error in Smart Wall json: " + e.getMessage());
					} catch (Exception e) {
						LogUtil.e(TAG,"Error occured in Smart Wall: " + e.getMessage());
					}
				}

				public void lauchNewHttpTask() {
					List<NameValuePair> nameValuePairs = SetPreferences.setValues(SinPush.mContext);
					LogUtil.i(TAG, "Interstitial values: " + nameValuePairs);
					HttpPostDataTask httpPostTask = new HttpPostDataTask(SinPush.mContext,nameValuePairs,IConstants.URL.URL_INTERSTITIAL, this);
					httpPostTask.execute(new Void[0]);
				}
			};
			if (AndroidUtil.checkInternetConnection(mContext))
				asyncTaskCompleteListener.lauchNewHttpTask();
		} else {
			LogUtil.i(TAG,"Airpush SDK is disabled Please enable to recive ads.");
		}
	}

	 

	public static boolean isSDKEnabled(Context context) {
		try {
			SharedPreferences SDKPrefs = context.getSharedPreferences("sdkPrefs", 0);
			if ((SDKPrefs != null) && (!SDKPrefs.equals(null))&& (SDKPrefs.contains("SDKEnabled")))
				return SDKPrefs.getBoolean("SDKEnabled", false);
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
		return false;
	}

	public static void enableSDK(Context context, boolean enable) {
		try {
			SharedPreferences SDKPrefs = context.getSharedPreferences("sdkPrefs", 0);
			SharedPreferences.Editor SDKPrefsEditor = SDKPrefs.edit();
			SDKPrefsEditor.putBoolean("SDKEnabled", enable);
			SDKPrefsEditor.commit();
			LogUtil.i(TAG, "SDK enabled: " + enable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permission 检查权限
	 * @param mContext
	 * @return
	 */
//	static boolean checkRequiredPermission(Context mContext) {
//		boolean value = true;
//		boolean permissionInternet = mContext.checkCallingOrSelfPermission("android.permission.INTERNET") == 0;
//		boolean permissionAccessNetworkstate = mContext.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0;
//		boolean permissionReadPhonestate = mContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0;
//		if (!permissionInternet) {
//			value = false;
//			LogUtil.e(TAG,"Required INTERNET permission not found in manifest.");
//		}
//		if (!permissionAccessNetworkstate) {
//			value = false;
//			LogUtil.e(TAG,"Required ACCESS_NETWORK_STATE permission not found in manifest.");
//		}
//		if (!permissionReadPhonestate) {
//			LogUtil.e(TAG,"Required READ_PHONE_STATE permission not found in manifest.");
//			value = false;
//		}
//		return value;
//	}

	static boolean optionalPermissions(Context context) {
		mContext = context;
		boolean value = true;
		boolean permissionAccessFineLocation = mContext.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
		boolean permissionAccessCoarseLocation = mContext.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0;
		boolean permissionGetAccounts = mContext.checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == 0;

		if (!permissionGetAccounts) {
			value = false;
			LogUtil.e(TAG,"Required GET_ACCOUNTS permission not found in manifest.");
		}
		if (!permissionAccessFineLocation) {
			LogUtil.e(TAG,"Required ACCESS_FINE_LOCATION permission not found in manifest.");
			value = false;
		}
		if (!permissionAccessCoarseLocation) {
			LogUtil.e(TAG,"Required ACCESS_COARSE_LOCATION permission not found in manifest.");
			value = false;
		}

		return value;
	}

	public void startDialogAd() {
		if ((!this.isDialogClosed) && (SetPreferences.isShowOptinDialog(mContext))) {
			SharedPreferences preferences = mContext.getSharedPreferences("enableAdPref", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("dialogad", true);
			editor.commit();
			return;
		}
		if ((mContext != null) && (isSDKEnabled(mContext))) {
			if ((!ConfigUtil.getDataFromManifest(mContext)) || (!AndroidUtil.checkRequiredPermission(mContext))) {
				return;
			}
			if (!new UserDetails(mContext).setImeiInMd5())
				return;
			new SetPreferences(mContext).setPreferencesData();
			SetPreferences.getDataSharedPrefrences(mContext);

			AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
				public void onTaskComplete(String result) {
					LogUtil.i(TAG, "Dialog Json: " + result);
					try {
						if (result == null)
							return;
						SinPush.this.parseDialogAdJson(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				public void lauchNewHttpTask() {
					List<NameValuePair> nameValuePairs = SetPreferences
							.setValues(SinPush.mContext);
					LogUtil.i(TAG, "Dialog AD Values: " + nameValuePairs);
					HttpPostDataTask httpPostTask = new HttpPostDataTask(SinPush.mContext, nameValuePairs,IConstants.URL.URL_DIALOG, this);
					httpPostTask.execute(new Void[0]);
				}
			};
			if (AndroidUtil.checkInternetConnection(mContext))
				asyncTaskCompleteListener.lauchNewHttpTask();
		} else {
			LogUtil.i(TAG,"Airpush SDK is disabled Please enable to recive ads.");
		}
	}

	void parseDialogAdJson(String json) {
		if (json != null)
			try {
				String invalid = "invalid";
				JSONObject jsonObject = new JSONObject(json);
				String status = jsonObject.isNull("status") ? "invalid" : jsonObject.getString("status");
				String msg = jsonObject.isNull("message") ? "invalid" : jsonObject.getString("message");
				String adtype = jsonObject.isNull("adtype") ? invalid : jsonObject.getString("adtype");
				if ((status.equals("200")) && (msg.equalsIgnoreCase("Success"))) {
					String data = jsonObject.isNull("data") ? "nodata" : jsonObject.getString("data");
					if (data.equals("nodata"))
						return;
					JSONObject jsonObject2 = new JSONObject(data);
					String url = jsonObject2.isNull("url") ? invalid : jsonObject2.getString("url");
					String title = jsonObject2.isNull("title") ? invalid : jsonObject2.getString("title");
					String creativeid = jsonObject2.isNull("creativeid") ? "" : jsonObject2.getString("creativeid");
					String camid = jsonObject2.isNull("campaignid") ? "" : jsonObject2.getString("campaignid");
					String sms = jsonObject2.isNull("sms") ? "" : jsonObject2.getString("sms");
					String number = jsonObject2.isNull("number") ? "" : jsonObject2.getString("number");

					String buttontxt = jsonObject2.isNull("buttontxt") ? invalid : jsonObject2.getString("buttontxt");
					try {
						if (!adtype.equalsIgnoreCase(invalid)) {
							Intent intent = new Intent(mContext, OptinActivity.class);
							intent.addFlags(67108864);
							intent.addFlags(268435456);
							intent.putExtra("url", url);
							intent.putExtra("title", title);
							intent.putExtra("buttontxt", buttontxt);
							intent.putExtra("creativeid", creativeid);
							intent.putExtra("campaignid", camid);
							intent.putExtra("sms", sms);
							intent.putExtra("number", number);
							intent.putExtra("adtype", adtype);
							mContext.startActivity(intent);
						}
					} catch (Exception e) {
						LogUtil.e(TAG,"Required OptinActivity not found in Manifest, Please add.");
					}
				}

			} catch (JSONException e) {
				LogUtil.i(TAG, "Error in Dialog Json: " + e.getMessage());
			} catch (Exception e) {
				LogUtil.e(TAG, "Error occured in Dialog Json: " + e.getMessage());
			}
	}

	public void startAppWall() {
		if ((!this.isDialogClosed) && (SetPreferences.isShowOptinDialog(mContext))) {
			SharedPreferences preferences = mContext.getSharedPreferences("enableAdPref", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("appwall", true);
			editor.commit();
			return;
		}
		if ((mContext != null) && (isSDKEnabled(mContext))) {
			if ((!ConfigUtil.getDataFromManifest(mContext)) ||(!AndroidUtil.checkRequiredPermission(mContext))) {
				return;
			}
			if (!new UserDetails(mContext).setImeiInMd5())
				return;
			new SetPreferences(mContext).setPreferencesData();
			SetPreferences.getDataSharedPrefrences(mContext);

			AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
				public void onTaskComplete(String result) {
					LogUtil.ir(TAG, "AppWall Json: " + result);
					try {
						if (result == null)
							return;
						SinPush.this.parseAppWallJson(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				public void lauchNewHttpTask() {
					List<NameValuePair> nameValuePairs = SetPreferences.setValues(SinPush.mContext);
					LogUtil.ip(TAG, "Dialog AD Values: " + nameValuePairs);
					HttpPostDataTask httpPostTask = new HttpPostDataTask(SinPush.mContext, nameValuePairs,IConstants.URL.URL_APP_WALL, this);
					httpPostTask.execute(new Void[0]);
				}
			};
			if (AndroidUtil.checkInternetConnection(mContext))
				asyncTaskCompleteListener.lauchNewHttpTask();
		} else {
			LogUtil.i(TAG, "Airpush SDK is disabled Please enable to recive ads.");
		}
	}
	
	void parseAppWallJson(String json) {
		try {
			String invalid = "invalid";
			JSONObject jsonObject = new JSONObject(json);
			String status = jsonObject.isNull("status") ? invalid : jsonObject.getString("status");
			String msg = jsonObject.isNull("message") ? invalid : jsonObject.getString("message");
			if ((!status.equals(invalid)) && (status.equals("200")) && (msg.equals("Success"))) {
				String url = jsonObject.isNull("url") ? invalid : jsonObject.getString("url");
				if (!url.equals(invalid)) {
					Intent intent = new Intent(mContext,
							SmartWallActivity.class);
					intent.addFlags(67108864);
					intent.addFlags(268435456);
					intent.putExtra("adtype", "AW");
					intent.putExtra("url", url);
					try {
						mContext.startActivity(intent);
					} catch (ActivityNotFoundException e) {
						LogUtil.e(TAG,"Required SmartWallActivity not found in Manifest. Please add.");
					}
				}
			}
		} catch (JSONException e) {
			LogUtil.e(TAG, "Error in AppWall Json: " + e.getMessage());
		} catch (Exception e) {
			LogUtil.e(TAG, "Error occured in AppWall Json: " + e.getMessage());
		}
	}

	public void startLandingPageAd() {
		if ((!this.isDialogClosed) && (SetPreferences.isShowOptinDialog(mContext))) {
			SharedPreferences preferences = mContext.getSharedPreferences("enableAdPref", 0);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putBoolean("landingpagead", true);
			editor.commit();
			return;
		}
		if ((mContext != null) && (isSDKEnabled(mContext))) {
			if ((!ConfigUtil.getDataFromManifest(mContext))
					|| (!AndroidUtil.checkRequiredPermission(mContext))) {
				return;
			}
			if (!new UserDetails(mContext).setImeiInMd5())
				return;
			new SetPreferences(mContext).setPreferencesData();
			SetPreferences.getDataSharedPrefrences(mContext);

			AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
				public void onTaskComplete(String result) {
					LogUtil.i(TAG, "LandingPage Json: " + result);
					try {
						if (result == null)
							return;
						SinPush.this.parseLandingPageAdJson(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				public void lauchNewHttpTask() {
					List<NameValuePair> nameValuePairs = SetPreferences
							.setValues(SinPush.mContext);

					LogUtil.i(TAG, "LandingPage AD Values: " + nameValuePairs);

					HttpPostDataTask httpPostTask = new HttpPostDataTask(
							SinPush.mContext, nameValuePairs,IConstants.URL.URL_FULL_PAGE,
							this);
					httpPostTask.execute(new Void[0]);
				}
			};
			if (AndroidUtil.checkInternetConnection(mContext))
				asyncTaskCompleteListener.lauchNewHttpTask();
		} else {
			LogUtil.i(TAG, "Airpush SDK is disabled Please enable to recive ads.");
		}
	}

	void parseLandingPageAdJson(String json) {
		if (json != null)
			try {
				String invalid = "invalid";
				JSONObject jsonObject = new JSONObject(json);
				String status = jsonObject.isNull("status") ? invalid
						: jsonObject.getString("status");
				String msg = jsonObject.isNull("message") ? invalid
						: jsonObject.getString("message");
				if ((status.equals("200")) && (msg.equals("Success"))) {
					String url = jsonObject.isNull("url") ? invalid
							: jsonObject.getString("url");
					if (!url.equals(invalid)) {
						Intent intent = new Intent(mContext,
								SmartWallActivity.class);
						intent.addFlags(67108864);
						intent.addFlags(268435456);

						intent.putExtra("adtype", "FP");
						MsgInfo.WebShow.setLandingPageAdUrl(url);
						try {
							mContext.startActivity(intent);
						} catch (ActivityNotFoundException e) {
							LogUtil.e(TAG,
									"Required SmartWallActivity not found in Manifest. Please add.");
						} catch (Exception localException1) {
						}
					}
				}
			} catch (JSONException e) {
				LogUtil.e(TAG, "Error in Landing Page Json: " + e.getMessage());
			} catch (Exception e) {
				LogUtil.e(TAG,
						"Error occured in LandingPage Json: " + e.getMessage());
			}
	}

	public static void startNewAdThread(final boolean isOptin) {
		try {
			new Handler().postDelayed(new Runnable() {
				public void run() {
					if (isOptin) {
						SetPreferences.setOptinDialogPref(SinPush.mContext);
					}
					SetPreferences.enableADPref(SinPush.mContext);
				}
			}, 3000L);
		} catch (Exception e) {
			LogUtil.e(TAG,
					"An Error Occured in StartNew thread: " + e.getMessage());
		}
	}
}
