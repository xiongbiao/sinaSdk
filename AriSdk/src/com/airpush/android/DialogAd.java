package com.airpush.android;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.airpush.data.SetPreferences;
import com.airpush.util.LogUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

public class DialogAd implements DialogInterface.OnClickListener {
	private Activity activity;
	private String title;
	private String buttontxt;
	private String url;
	private String creativeId;
	private String campid;
	private String adtype;
	private String sms;
	private String number;
	private static AlertDialog dialog;
	private String event = "0";
	private static String TAG = LogUtil.makeLogTag(DialogAd.class);
	Runnable runnable = new Runnable() {
		public void run() {
			if (DialogAd.this.event.equalsIgnoreCase("1")) {
				DialogAd.this.asyncTaskCompleteListener.lauchNewHttpTask();
				DialogAd.this.handleClicks();
			} else {
				DialogAd.this.asyncTaskCompleteListener.lauchNewHttpTask();
				DialogAd.this.activity.finish();
			}
		}
	};

	AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
		public void onTaskComplete(String result) {
			LogUtil.i(TAG,  "Dialog Click: " + result);
		}

		public void lauchNewHttpTask() {
			List<NameValuePair> values = SetPreferences.setValues(DialogAd.this.activity);
			values.add(new BasicNameValuePair("creativeid",
					DialogAd.this.creativeId));
			values.add(new BasicNameValuePair("campaignid", DialogAd.this.campid));
			values.add(new BasicNameValuePair("event", DialogAd.this.event));
			LogUtil.i(TAG,  "Dialog event values : " + values);
			HttpPostDataTask httpPostTask = new HttpPostDataTask(
					DialogAd.this.activity, values,IConstants.URL.URL_DIALOG_CLICK, this);
			httpPostTask.execute(new Void[0]);
		}
	};

	public DialogAd(Intent intent, Activity activity) {
		try {
			this.activity = activity;
			this.title = intent.getStringExtra("title");
			this.buttontxt = intent.getStringExtra("buttontxt");
			this.url = intent.getStringExtra("url");
			this.creativeId = intent.getStringExtra("creativeid");
			this.campid = intent.getStringExtra("campaignid");
			this.adtype = intent.getStringExtra("adtype");
			this.sms = intent.getStringExtra("sms");
			this.number = intent.getStringExtra("number");

			dialog = showDialog();
		} catch (Exception e) {
			LogUtil.e(TAG, "Error occured in DialogAd: " + e.getMessage());
		}
	}

	protected AlertDialog showDialog() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

			if ((this.title != null) && (!this.title.equalsIgnoreCase("")))
				builder.setMessage(this.title);
			else {
				builder.setMessage("Click for new offers");
			}
			builder.setPositiveButton("No Thanks.", this);

			if ((this.buttontxt != null)
					&& (!this.buttontxt.equalsIgnoreCase("")))
				builder.setNegativeButton(this.buttontxt, this);
			else {
				builder.setNegativeButton("Yes!", this);
			}
			builder.setCancelable(false);
			builder.create();
			return builder.show();
		} catch (Exception e) {
			LogUtil.e(TAG, "Error : " + e.toString());
		}
		return null;
	}

	public void onClick(DialogInterface dialog, int which) {
		try {
			switch (which) {
			case -2:

				this.event = "1";

				dialog.dismiss();

				new Handler().post(this.runnable);

				break;
			case -1:

				this.event = "0";

				dialog.dismiss();

				new Handler().post(this.runnable);
			}
		} catch (Exception localException) {
		}
	}

	void handleClicks() {
		try {
			if (this.adtype.equalsIgnoreCase("DAU")) {
				LogUtil.i(TAG,  "Pushing dialog DAU Ads.....");
				try {
					Intent intent = new Intent("android.intent.action.VIEW",
							Uri.parse(this.url));
					intent.addFlags(268435456);
					this.activity.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (this.adtype.equalsIgnoreCase("DCC")) {
				LogUtil.i(TAG,  "Pushing dialog CC Ads.....");
				try {
					Uri uri = Uri.parse("tel:" + this.number);
					Intent intent = new Intent("android.intent.action.DIAL",
							uri);
					intent.addFlags(268435456);
					this.activity.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				} catch (Exception localException1) {
				}
			} else if (this.adtype.equalsIgnoreCase("DCM")) {
				try {
					LogUtil.i(TAG,  "Pushing dialog CM Ads.....");

					Intent intent = new Intent("android.intent.action.VIEW");
					intent.addFlags(268435456);
					intent.setType("vnd.android-dir/mms-sms");
					intent.putExtra("address", this.number);
					intent.putExtra("sms_body", this.sms);
					this.activity.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				LogUtil.i(TAG, "Invalid ad type for dialog ad." + this.adtype);
			}
		} finally {
			try {
				dialog.dismiss();
				this.activity.finish();
			} catch (Exception localException2) {
			}
		}
	}

	public static AlertDialog getDialog() {
		return dialog;
	}
}
