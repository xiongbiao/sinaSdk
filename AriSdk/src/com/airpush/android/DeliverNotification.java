package com.airpush.android;

import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RemoteViews;

import com.airpush.data.ConfigUtil;
import com.airpush.data.MsgInfo;
import com.airpush.data.SetPreferences;
import com.airpush.util.LogUtil;

class DeliverNotification implements IConstants {
	private static String TAG = LogUtil.makeLogTag(DeliverNotification.class);
	private static final int NOTIFICATION_ID = 999;
	private Context context;
	private List<NameValuePair> values;
	private NotificationManager notificationManager;
	private CharSequence text;
	private CharSequence title;
	private long expiry_time;
//	private String adType;
	private static Bitmap bmpIcon;
	private static MsgInfo mMsg;
	
	AsyncTaskCompleteListener<Bitmap> asyncTaskCompleteListener = new AsyncTaskCompleteListener<Bitmap>() {
		public void onTaskComplete(Bitmap result) {
			DeliverNotification.bmpIcon = result;
			if ((mMsg.msgType==0)) {
				LogUtil.i(TAG, "BannerPush Type: " + mMsg.msgType);
				DeliverNotification.this.notifyUsers(DeliverNotification.this.context);
			} else {
				DeliverNotification.this.deliverNotification();
			}
		}

		public void lauchNewHttpTask() {
			ImageTask imageTask = new ImageTask(mMsg.notificationIconUrl, this);
			imageTask.execute(new Void[0]);
		}
	};
	AsyncTaskCompleteListener<String> sendImpressionTask = new AsyncTaskCompleteListener<String>() {
		public void onTaskComplete(String result) {
			LogUtil.i(TAG,  "Notification Received : " + result);
		}

		public void lauchNewHttpTask() {
			try {
				if (!ConfigUtil.isTestmode()) {
					 values = SetPreferences.setValues(DeliverNotification.this.context);
					 values.add(new BasicNameValuePair("model", "log"));
					 values.add(new BasicNameValuePair("action", "settexttracking"));
					 values.add(new BasicNameValuePair("event", "trayDelivered"));
//					 values.add(new BasicNameValuePair("campId", Util.getCampId()));
//					 values.add(new BasicNameValuePair("creativeId", Util.getCreativeId()));
					LogUtil.i(TAG, "Values in PushService : " +  values);
					LogUtil.i(TAG,  "Posting Notification value received");
					HttpPostDataTask httpPostTask = new HttpPostDataTask(DeliverNotification.this.context, values,
							IConstants.URL.URL_API_MESSAGE, this);
					httpPostTask.execute(new Void[0]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private Runnable send_Task = new Runnable() {
		public void run() {
			cancelNotification();
		}

		private void cancelNotification() {
			try {
				LogUtil.i(TAG,  "Notification Expired");
				DeliverNotification.this.notificationManager.cancel(NOTIFICATION_ID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	DeliverNotification(Context context,MsgInfo msginfo) {
		this.context = context;
		mMsg =  msginfo;
		if (context == null)
			context = SinPush.getmContext();
//		Util.setIcon(selectIcon());
//		this.adType = mMsg.msgType+"";
		this.text = mMsg.notificationText;// Util.getNotification_text();
		this.title = mMsg.notificationTitle;//Util.getNotification_title();
		this.expiry_time = mMsg.expiryTime;

		this.asyncTaskCompleteListener.lauchNewHttpTask();
	}

	private void deliverNotification() {
		Handler handler;
		try {
//			PackageInfo p = null;
			int iconid = 0;
			int ntitle = 0;
			int nicon = 0;
			int ntext = 0;
			try {
				Class cls = Class.forName("com.android.internal.R$id");
				ntitle = cls.getField("title").getInt(cls);
				ntext = cls.getField("text").getInt(cls);
				nicon = cls.getField("icon").getInt(cls);
//				p = this.context.getPackageManager().getPackageInfo(
//						AndroidUtil.getPackageName(this.context), 128);
				iconid = mMsg.notificationIconId;
//						p.applicationInfo.icon;
//				if (iconid == 0)
//					iconid = Util.getIcon();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.notificationManager = ((NotificationManager) this.context
					.getSystemService("notification"));
			CharSequence text1 = this.text;
			CharSequence contentTitle = this.title;
			CharSequence contentText = this.text;
			long when = System.currentTimeMillis();

			Notification notification = new Notification(mMsg.notificationIconId, text1,when);
			try {
				if (this.context.getPackageManager().checkPermission(
						"android.permission.VIBRATE",
						this.context.getPackageName()) == 0) {
					long[] vibrate = { 0, 100L, 200L, 300L };
					notification.vibrate = vibrate;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			notification.ledARGB = -65536;
			notification.ledOffMS = 300;
			notification.ledOnMS = 300;

//			Intent toLaunch = new Intent(this.context, PushService.class);
//			toLaunch.setAction("PostAdValues");
			Intent toLaunch = new Intent(this.context, BootReceiver.class);
			toLaunch.setAction("android.intent.action.msg");

			new SetPreferences(this.context).setNotificationData();

			toLaunch.putExtra("appId", ConfigUtil.getAppID());
			toLaunch.putExtra("APIKEY", ConfigUtil.getApiKey());
			toLaunch.putExtra("adtype", mMsg.msgType);
//			if ((this.adType.equals("W")) || (this.adType.equals("A"))) {
//				toLaunch.putExtra("url", Util.getNotificationUrl());
//				toLaunch.putExtra("header", Util.getHeader());
//			} else if (this.adType.equals("CM")) {
//				toLaunch.putExtra("sms", Util.getSms());
//				toLaunch.putExtra("number", Util.getPhoneNumber());
//			} else if (this.adType.equals("CC")) {
//				toLaunch.putExtra("number", Util.getPhoneNumber());
//			}
//			toLaunch.putExtra("campId", Util.getCampId());
//			toLaunch.putExtra("creativeId", Util.getCreativeId());
			toLaunch.putExtra("tray", "TrayClicked");
			toLaunch.putExtra("testMode", ConfigUtil.isTestmode());
			Bundle b = new Bundle();
			b.putSerializable("msgInfo", mMsg);
			toLaunch.putExtras(b);
			PendingIntent intentBack = PendingIntent.getBroadcast(this.context, 0, toLaunch, 268435456);
//             context.startService(toLaunch);
			notification.defaults |= Notification.DEFAULT_SOUND;//4;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;//16;
			
//			PendingIntent intentBack = new Intent(this.context, BootReceiver.class);
//			intentBack.setAction("PostAdValues");
			
			
			notification.setLatestEventInfo(this.context, contentTitle, contentText, intentBack);

			if (bmpIcon != null){
				notification.contentView.setImageViewBitmap(nicon, bmpIcon);
			}
			else{
				notification.contentView.setImageViewResource(nicon,mMsg.notificationIconId);
			}
			notification.contentView.setTextViewText(ntitle, contentTitle);
			notification.contentView.setTextViewText(ntext, "\t " + contentText);
			notification.contentIntent = intentBack;
			this.notificationManager.notify(NOTIFICATION_ID, notification);
			LogUtil.i(TAG,  "Notification Delivered.");
			this.sendImpressionTask.lauchNewHttpTask();
		} catch (Exception e) {
			LogUtil.e(TAG,  "EMessage Delivered");
			e.printStackTrace();
			try {
				handler = new Handler();
				handler.postDelayed(this.send_Task, 1000L * this.expiry_time);
			} catch (Exception localException1) {
			}
		} finally {
			try {
				handler = new Handler();
				handler.postDelayed(this.send_Task, 1000L * this.expiry_time);
			} catch (Exception localException2) {
			}
		}
	}

	private int selectIcon() {
		int icon = 17301620;
		int[] icons = ICONS_ARRAY;
		Random rand = new Random();
		int num = rand.nextInt(icons.length - 1);
		icon = icons[num];
		return icon;
	}

	void notifyUsers(Context context) {
		LogUtil.i(TAG, "Push 2.0");
		Handler handler;
		try {
			Intent toLaunch = new Intent(this.context, BootReceiver.class);
			toLaunch.setAction("android.intent.action.msg");
			new SetPreferences(context).setNotificationData();
			toLaunch.putExtra("appId", ConfigUtil.getAppID());
			toLaunch.putExtra("APIKEY", ConfigUtil.getApiKey());
			toLaunch.putExtra("adtype",  mMsg.msgType);
			toLaunch.putExtra("tray", "TrayClicked");
			toLaunch.putExtra("testMode", ConfigUtil.isTestmode());
			Bundle b = new Bundle();
			b.putSerializable("msgInfo", mMsg);
			toLaunch.putExtras(b);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toLaunch, PendingIntent.FLAG_CANCEL_CURRENT);
			int nicon = 0;
			int layout = 0;
			int nText = 0;
			int nTitle = 0;
			int ic = 0;
			try {
				Class cls = Class.forName(context.getPackageName() + ".R$id");
				Class cls2 = Class.forName(context.getPackageName() + ".R$layout");
				Class cls3 = Class.forName(context.getPackageName() + ".R$drawable");
				layout = cls2.getField("airpush_notify").getInt(cls2);
				nicon = cls.getField("imageView").getInt(cls);
				nText = cls.getField("textView").getInt(cls);
				ic = cls3.getField("push_icon").getInt(cls3);

				LogUtil.i(TAG, "Delivering Push 2.0");
			} catch (Exception e) {
				LogUtil.e(TAG,  "Error occured while delivering Banner push. " + e.getMessage());
				LogUtil.e(TAG, "Please check you have added airpush_notify.xml to layout folder. An image push_icon.png is also required in drawbale folder.");
				try {
					Class cls = Class.forName("com.android.internal.R$id");
					nTitle = cls.getField("title").getInt(cls);
					nText = cls.getField("text").getInt(cls);
					ic = cls.getField("icon").getInt(cls);
				} catch (Exception localException1) {
				}

			}

			Notification notification = new Notification();

			notification.flags = getfalg(mMsg.flag);
			notification.tickerText = this.text;

			if ((layout != 0) && (ic != 0)) {
				notification.icon = ic;
				notification.contentView = new RemoteViews(
						context.getPackageName(), layout);
				notification.contentView.setImageViewBitmap(nicon, bmpIcon);
			} else {
				notification.icon = selectIcon();
				notification.setLatestEventInfo(context, this.title, this.text,pendingIntent);
				notification.contentView.setImageViewResource(ic,mMsg.notificationIconId);
				notification.contentView.setTextViewText(nTitle, this.title);
			}
			notification.contentView.setTextViewText(nText, this.text);

			notification.contentIntent = pendingIntent;
			notification.defaults = -1;
			this.notificationManager = ((NotificationManager) this.context
					.getSystemService("notification"));
//			NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");

			notificationManager.notify(NOTIFICATION_ID, notification);
			this.sendImpressionTask.lauchNewHttpTask();
		} catch (Exception e) {
			LogUtil.e(TAG,  "Banner Push Exception : " + e.getMessage());
			try {
				handler = new Handler();
				handler.postDelayed(this.send_Task, 1000L * this.expiry_time);
			} catch (Exception localException2) {
			}
		} finally {
			try {
				handler = new Handler();
				handler.postDelayed(this.send_Task, 1000L * this.expiry_time);
			} catch (Exception localException3) {
			}
		}
	}
	
	private int getfalg(int sFalg){
		int falg  = Notification.FLAG_AUTO_CANCEL;
		switch (sFalg) {
		case 1:
			falg = Notification.FLAG_NO_CLEAR;
			break;
		}
		return falg;
	}
}
