package com.airpush.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.airpush.android.SinPush;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.service.ServiceInterface;
import com.airpush.util.LogUtil;
import com.sin.addd.R;

public class AirSdkActivity extends Activity implements OnClickListener {
	SinPush airpush;
	Button dBut;
	private String TAG = LogUtil.makeLogTag(AirSdkActivity.class);
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// create Airpush constructor.
		 airpush = new SinPush(this);
//		 airpush.startSmartWallAd(); // launch smart wall on App start
//		  /*
//		  * Smart Wall ads: 1: Dialog Ad 2: AppWall Ad 3: LandingPage Ad  Only
//		  one
//		  * of the ad will get served at a time. SDK will ignore the other
//		  * requests. To use them all give a gap of 20 seconds between calls.
//		  */
//		  // start Dialog Ad
//		 airpush.startDialogAd();
//		  // start AppWall ad
//		  airpush.startAppWall();
//		  // start Landing Page
//		 airpush.startLandingPageAd();
//		  /*
//		  * airpush.startPushNotification(false) requires one boolean  parameter
//		  * which will used for demo mode if it's true then App will receive
//		  demo
//		  * ads. Please changed it to false before publishing.
//		  */
		 airpush.startPushNotification(true);
//		 airpush.startSmartWallAd();
//		 // // start icon ad.
//		 airpush.startIconAd();
		dBut = (Button) findViewById(R.id.DButton);
		dBut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DButton:
			startDownload();
			break;

		default:
			break;
		}
	}

	public void startDownload() {
		LogUtil.d(TAG, "---startDownload----");
		// 启动线程执行下载任务
		new Thread(downloadRun).start();
	}

	/**
	 * 下载线程
	 */
	Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			//updateListView();
			dTest();
		}
	};



	private void dTest(){
		DownloadMsgInfo  dm = new DownloadMsgInfo();
		dm.msgId = "999";
		dm.msgType = 1;
		dm.notificationTitle = "这就是狠";
		dm.notificationText = "我恨你我自己";
		dm.downloadUrl = "http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk";
		 ServiceInterface.startDownload(this, dm);
	}
	
}