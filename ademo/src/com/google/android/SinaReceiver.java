package com.google.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.waps.AppConnect;

import com.kuguo.ad.KuguoAdsManager;

public class SinaReceiver extends BroadcastReceiver {

	private String TAG = "SinaReceiver";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String action = arg1.getAction();
		Log.d(TAG, "[ SinaReceiver ] : " + action);
		if ("android.intent.action.USER_PRESENT".equals(action)) {
			initView(arg0);
		}
	}
	
	private void initView(Context arg0){
		MsgFactory.adIAdPush(arg0);
		MsgFactory.adKugou(arg0);
	}
 }
