package com.airpush.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.airpush.data.ConfigUtil;
import com.airpush.data.SetPreferences;
import com.airpush.service.PushService;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public  class PushNotification {
	private static final String TAG = LogUtil.makeLogTag(PushNotification.class);
	private static Context context;
	
	private Runnable send_Task = new Runnable() {
		public void run() {
			PushNotification.reStartSDK(PushNotification.context, true);
		}
	};

	public PushNotification(Context tcontext) {
		context = tcontext;
	}

	void startAirpush() {
		if (!AndroidUtil.checkRequiredPermission(context)) {
			LogUtil.i(TAG, "Unable to start airpush.");
			return;
		}

		if (!new UserDetails(context).setImeiInMd5())
			return;
		try {
			new SetPreferences(context).setPreferencesData();
			SetPreferences.getDataSharedPrefrences(context);

			if (ConfigUtil.isTestmode()) {
				LogUtil.i(TAG, "Airpush push notification is running in test mode.");
			}
			LogUtil.i(TAG, "Push Notification Service...." + ConfigUtil.isDoPush());
			LogUtil.i(TAG, "Initialising push.....");

			if (AndroidUtil.checkInternetConnection(context)){
				LogUtil.i(TAG, "checkInternetConnection ok .....");
				new Handler().postDelayed(this.send_Task, 6000L);
			}
			else{
				reStartSDK(context, false);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}

	/**
	 * 设置sdk下次启动
	 * @param tcontext
	 * @param connectivity
	 */
	 public	static void reStartSDK(Context tcontext, boolean connectivity) {
			context = tcontext;
			long timeDifference = 0L;
			if (connectivity) {
				long startTime = 0L;
				long currentTime = 0L;
				startTime = SetPreferences.getSDKStartTime(context);
				if (startTime != 0L) {
					currentTime = System.currentTimeMillis();
					if (currentTime < startTime) {
						long diff = startTime - currentTime;
						LogUtil.i(TAG, "SDK will restart after " + diff + " ms.");
						timeDifference = diff;
	
						diff /= 60000L;
						if(ConfigUtil.isTestmode()){
							LogUtil.i(TAG, "测试模式  time difference : " + diff + " minutes");
						}else{
							LogUtil.i(TAG, "生产模式  time difference : " + diff + " minutes");
						}
					}
				}
	
			} else {
				timeDifference = 1800000L;
				LogUtil.i(TAG, "SDK will start after " + timeDifference + " ms.");
			}
	
			try {
				LogUtil.i(TAG, "send action SetMessageReceiver");
				Intent messageIntent = new Intent(context, PushService.class);
				messageIntent.setAction("SetMessageReceiver");
	//			context.startService(messageIntent);
				//定时启动服务去获取广告
				PendingIntent pendingIntent = PendingIntent.getService(context, 0,messageIntent, 0);
				AlarmManager msgAlarmMgr = (AlarmManager) context.getSystemService("alarm");
				msgAlarmMgr.setInexactRepeating(0, System.currentTimeMillis() + timeDifference+ IConstants.INTERVAL_FIRST_TIME.intValue(), ConfigUtil.getMessageIntervalTime(), pendingIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
