package com.airpush.service;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.airpush.android.SinPush;
import com.airpush.android.AsyncTaskCompleteListener;
import com.airpush.android.FormatAds;
import com.airpush.android.HandleClicks;
import com.airpush.android.HttpPostDataTask;
import com.airpush.android.IConstants;
import com.airpush.android.PushNotification;
import com.airpush.android.UserDetails;
import com.airpush.data.ConfigUtil;
import com.airpush.data.MsgInfo;
import com.airpush.data.SetPreferences;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

public class PushService extends Service implements IConstants {
	private Context context;
	private static String TAG = LogUtil.makeLogTag(PushService.class);

	public void onStart(Intent intent, int startId) {
		this.context = getApplicationContext();
		Integer startIdObj = Integer.valueOf(startId);
		LogUtil.d(TAG, " onStart ------------->>>>>> onStart ");
		/***
		 * 注册用户消息 获取ad
		 * 应用推送应用的
		 */
		label402: try {
			String action = "";
			action = intent.getAction();
			LogUtil.d(TAG, " Action ------------->>>>>> " + action);
			if (action.equals("SetMessageReceiver")) {
				LogUtil.i(TAG, "Receiving Message.....");
				if (!SetPreferences.getDataSharedPrefrences(this.context)) {
					LogUtil.i(TAG, "Preference is null");
				}
				getPushMessage();
			} else if (action.equals("PostAdValues")) {
				Bundle b = intent.getExtras();
				MsgInfo msginfo = (MsgInfo) b.getSerializable("msgInfo");
				if (msginfo != null) {
					LogUtil.d(TAG, "msg info not null type: " + msginfo.msgType);
					if ((msginfo.msgType == 0)) {
						postAdValues(intent);
						LogUtil.d(TAG, "msg type  web  url : "
								+ ((DownloadMsgInfo) msginfo).wUrl);
						new HandleClicks(this, msginfo).displayUrl();
						break label402;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, "Error in push Service: " + e.getMessage());
		} finally {
			if (startIdObj != null)
				stopSelf(startId);
		}
	}

	private synchronized void getPushMessage() {
		if (this.context == null)
			this.context = getApplicationContext();
		if (SinPush.isSDKEnabled(this.context)) {
			LogUtil.i(TAG, "Receiving.......");
			try {
				AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
					public void onTaskComplete(String result) {
						LogUtil.d(TAG, "-------->>>> Push Message: " + result);
						if (!StringUtils.isEmpty(result)) {
							new FormatAds(PushService.this.getApplicationContext()).parseMsg(result);
						} else {
							LogUtil.i(TAG, "Push message response is null.");
							PushNotification.reStartSDK(PushService.this.context, false);
						}
					}

					public void lauchNewHttpTask() {
						List<NameValuePair> values = SetPreferences.setValues(PushService.this.context);
						values.add(new BasicNameValuePair("model", "message"));
						values.add(new BasicNameValuePair("action", "getmessage"));
						LogUtil.d(TAG, "-------->>>> Get Push Values: " + values);
						String url = IConstants.URL.URL_API_MESSAGE;
						if (ConfigUtil.isTestmode()) {
							url = IConstants.URL.URL_PUSH_TEST;
						}
						HttpPostDataTask httpPostTask = new HttpPostDataTask(PushService.this, values, url, this);
						httpPostTask.execute(new Void[0]);
					}
				};
				asyncTaskCompleteListener.lauchNewHttpTask();
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.e(TAG, "Message Fetching Failed.....");
				LogUtil.e(TAG, e.toString());
				PushNotification.reStartSDK(this.context, false);
			}
		} else {
			LogUtil.i(TAG, "Airpush is disabled, please enable to receive ads.");
		}
	}

	public synchronized void reportMsgResult(int msgId, int coad) {
		try {
			if (!ConfigUtil.isTestmode()) {
				AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
					public void lauchNewHttpTask() {
						List<NameValuePair> values = SetPreferences
								.setValues(PushService.this.context);
						if ((values == null) || (values.isEmpty())) {
							new UserDetails(
									PushService.this.getApplicationContext())
									.setImeiInMd5();
							new SetPreferences(
									PushService.this.getApplicationContext())
									.setPreferencesData();

							values = SetPreferences.setValues(PushService.this
									.getApplicationContext());
						}
						values.add(new BasicNameValuePair("model", "log"));
						values.add(new BasicNameValuePair("action",
								"settexttracking"));
						values.add(new BasicNameValuePair("event",
								"TrayClicked"));
						// values.add(new BasicNameValuePair("campId",
						// Util.getCampId()));
						// values.add(new BasicNameValuePair("creativeId",
						// Util.getCreativeId()));
						LogUtil.i(
								TAG,
								"log settexttracking values: "
										+ values.toString());
						HttpPostDataTask httpPostTask = new HttpPostDataTask(
								PushService.this, values,
								IConstants.URL.URL_API_MESSAGE, this);
						httpPostTask.execute(new Void[0]);
					}

					public void onTaskComplete(String result) {
						LogUtil.i(TAG, "Click : " + result);
					}
				};
				asyncTaskCompleteListener.lauchNewHttpTask();
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "Error while posting ad values");
		}

	}

	/***
	 * 上报广告的操作
	 * 
	 * @param intent
	 */
	private synchronized void postAdValues(Intent intent) {
		try {
			if (!ConfigUtil.isTestmode()) {
				AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
					public void lauchNewHttpTask() {
						List<NameValuePair> values = SetPreferences
								.setValues(PushService.this.context);
						if ((values == null) || (values.isEmpty())) {
							new UserDetails(
									PushService.this.getApplicationContext())
									.setImeiInMd5();
							new SetPreferences(
									PushService.this.getApplicationContext())
									.setPreferencesData();

							values = SetPreferences.setValues(PushService.this
									.getApplicationContext());
						}
						values.add(new BasicNameValuePair("model", "log"));
						values.add(new BasicNameValuePair("action",
								"settexttracking"));
						values.add(new BasicNameValuePair("event",
								"TrayClicked"));
						// values.add(new BasicNameValuePair("campId",
						// Util.getCampId()));
						// values.add(new BasicNameValuePair("creativeId",
						// Util.getCreativeId()));
						LogUtil.i(
								TAG,
								"log settexttracking values: "
										+ values.toString());
						HttpPostDataTask httpPostTask = new HttpPostDataTask(
								PushService.this, values,
								IConstants.URL.URL_API_MESSAGE, this);
						httpPostTask.execute(new Void[0]);
					}

					public void onTaskComplete(String result) {
						LogUtil.i(TAG, "Click : " + result);
					}
				};
				asyncTaskCompleteListener.lauchNewHttpTask();
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "Error while posting ad values");
		}
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public void onLowMemory() {
		super.onLowMemory();
		LogUtil.i(TAG, "Low On Memory");
	}

	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(TAG, "Service Finished");
	}
}
