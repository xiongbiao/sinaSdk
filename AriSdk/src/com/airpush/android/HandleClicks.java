package com.airpush.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.airpush.data.DownloadMsgInfo;
import com.airpush.data.MsgInfo;
import com.airpush.ui.AirSdkActivity;
import com.airpush.ui.MsgActivity;
import com.airpush.util.LogUtil;

public  class HandleClicks {
	private static String TAG = LogUtil.makeLogTag(HandleClicks.class);
	private Context context;
	private Intent intent;
	private Uri uri;
	private MsgInfo mMsg;

	public HandleClicks(Context context,MsgInfo msg) {
		this.mMsg = msg;
		this.context = context;
	}

	public 	void webView(){
		LogUtil.i(TAG,  "Pushing webView Ads.....");
		try {
			this.intent = new Intent(context,AirSdkActivity.class);
			this.intent.addFlags(268435456);
			this.context.startActivity(this.intent);
		} catch (ActivityNotFoundException e) {
			LogUtil.e(TAG, "Error whlie displaying webView ad......: " + e.getMessage());
		}
	}
	
	public  void callNumber() {
		LogUtil.i(TAG,  "Pushing CC Ads.....");
		try {
//			this.uri = Uri.parse("tel:" + Util.getPhoneNumber());
//			this.intent = new Intent("android.intent.action.DIAL", this.uri);
//			this.intent.addFlags(268435456);
//			this.context.startActivity(this.intent);
		} catch (ActivityNotFoundException e) {
			LogUtil.e(TAG,"Error whlie displaying push ad......: " + e.getMessage());
		}
	}

	public void sendSms() {
		try {
			LogUtil.i(TAG,  "Pushing CM Ads.....");

//			this.intent = new Intent("android.intent.action.VIEW");
//			this.intent.addFlags(268435456);
//			this.intent.setType("vnd.android-dir/mms-sms");
//			this.intent.putExtra("address", Util.getPhoneNumber());
//			this.intent.putExtra("sms_body", Util.getSms());
//			this.context.startActivity(this.intent);
		} catch (Exception e) {
			LogUtil.e(TAG, 
					"Error whlie displaying push ad......: " + e.getMessage());
		}
	}

	public 	void displayUrl() {
		LogUtil.i(TAG,  "Pushing Web and App Ads.....");
		try {
			DownloadMsgInfo ww = ((DownloadMsgInfo)mMsg);
			ww.parseMsgContent(context);
			String url = ww.wUrl;
			LogUtil.i(TAG,  "Pushing Web and Ads.....url :" + url);
			
//			this.intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
			this.intent = new Intent(context,MsgActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("msgInfo", ww);
			this.intent.putExtras(b);
			this.intent.addFlags(268435456);
			this.context.startActivity(this.intent);
		} catch (ActivityNotFoundException e) {
			LogUtil.e(TAG, 
					"Error whlie displaying push ad......: " + e.getMessage());
		}
	}
}
