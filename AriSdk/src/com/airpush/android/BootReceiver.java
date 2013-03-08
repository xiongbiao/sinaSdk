package com.airpush.android;

import com.airpush.data.MsgInfo;
import com.airpush.data.SetPreferences;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class BootReceiver extends BroadcastReceiver {
	private static String TAG = LogUtil.makeLogTag(BootReceiver.class);
	public void onReceive(Context arg0, Intent intent) {
		try {
		 String action = intent.getAction();
		  /**
		   * 
		   */
		 
		 
		 if("android.intent.action.msg".equals(action)){
			 LogUtil.d(TAG, "action : "+action);
			 Bundle b = intent.getExtras();
				MsgInfo msginfo = (MsgInfo)b.getSerializable("msgInfo");
				if(msginfo!=null){
					LogUtil.d(TAG, "msg info not null type: "+msginfo.msgType);
					if ((msginfo.msgType==1)) {
//						postAdValues(intent);
						LogUtil.d(TAG, "msg type  web  url : " + ((DownloadMsgInfo)msginfo).wUrl);
						new HandleClicks(arg0,msginfo).displayUrl();
					}
				}
		 }
		 final Context arg = arg0;
			if (!SetPreferences.getDataSharedPrefrences(arg0))
				return;
			new Handler().postDelayed(new Runnable() {
				public void run() {
					new PushNotification(arg).startAirpush();
					LogUtil.i(TAG, "Airpush SDK started form BootReciver.");
				}
			}, 4000L);
		} catch (Exception e) {
			LogUtil.e(TAG, "Error occoured while starting BootReciver. "+ e.getMessage());
		}
	}

}
