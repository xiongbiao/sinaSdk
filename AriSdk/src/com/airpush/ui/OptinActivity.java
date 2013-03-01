package com.airpush.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airpush.android.AsyncTaskCompleteListener;
import com.airpush.android.DialogAd;
import com.airpush.android.HttpPostDataTask;
import com.airpush.android.IConstants;
import com.airpush.android.SinPush;
import com.airpush.data.ConfigUtil;
import com.airpush.data.SetPreferences;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public class OptinActivity extends Activity {
	private static String TAG = LogUtil.makeLogTag(OptinActivity.class);
	private static final String OPT_IN_TEXT = "<html><body style='background:#C4C4C4;font-family:Arial;font-size:11pt;line-height:18px'><p align='justify'>Thank you for downloading this free, ad-supported application! Please read carefully. This application is ad-supported and our advertising partner, Airpush, Inc., may place ads within applications and in your device's notification tray and home screen.  Airpush collects certain information in accordance with the permissions you just granted through the prior screen.  When you click on advertisements delivered by Airpush, you will typically be directed to a third party's web page and we may pass certain of your information to the third parties operating or hosting these pages, including your email address, phone number and a list of the apps on your device.</p><p align='justify'>  For more information on how Airpush collects, uses and shares your information, and to learn about your information choices, please visit the <a href='http://m.airpush.com/privacypolicy'><i>Airpush Privacy Policy</i> </a>. If you do not wish to receive ads delivered by Airpush in the future, you may visit the <a href='http://m.airpush.com/optout'><i>Airpush opt-out page</i></a> or delete this app.</p></body></html>";
	private static final String TITLE = "最新推荐";
	private static String event = "optOut";
	private static WebView webView;
	private OptinDialog dialog;
	private String adType;
	private Intent intent;
	AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
		public void onTaskComplete(String result) {
			LogUtil.i(TAG,  OptinActivity.event + " data sent: " + result);
			OptinActivity.this.finish();
		}

		public void lauchNewHttpTask() {
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			values.add(new BasicNameValuePair("event", OptinActivity.event));
			values.add(new BasicNameValuePair("imei", ConfigUtil.getImei()));
			values.add(new BasicNameValuePair("appId", ConfigUtil.getAppID()));
			LogUtil.i(TAG,  OptinActivity.event + " Data: " + values);
			HttpPostDataTask httpPostTask = new HttpPostDataTask(OptinActivity.this, values, IConstants.URL.URL_OPT_IN, this);
			httpPostTask.execute(new Void[0]);
		}

	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		try {
			this.intent = getIntent();
			if ((this.intent != null)
					&& ((this.intent.getStringExtra("adtype")
							.equalsIgnoreCase("DAU"))
							|| (this.intent.getStringExtra("adtype")
									.equalsIgnoreCase("DCM")) || (this.intent
							.getStringExtra("adtype").equalsIgnoreCase("DCC")))) {
				this.adType = this.intent.getStringExtra("adtype");
				new DialogAd(this.intent, this);
			}
			return;
		} catch (Exception localException) {
			if (SetPreferences.isShowOptinDialog(getApplicationContext())) {
				this.dialog = new OptinDialog(this);
				this.dialog.show();
			}
		}
	}

	protected void onUserLeaveHint() {
		try {
			if ((this.adType != null)
					&& ((this.adType.equalsIgnoreCase("DAU"))
							|| (this.adType.equalsIgnoreCase("DCM")) || (this.adType
							.equalsIgnoreCase("DCC")))) {
				DialogAd.getDialog().dismiss();
				finish();
			}
		} catch (Exception e) {
			finish();
		}

		super.onUserLeaveHint();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((this.adType != null)
					&& ((this.adType.equalsIgnoreCase("DAU"))
							|| (this.adType.equalsIgnoreCase("DCM")) || (this.adType
							.equalsIgnoreCase("DCC"))) && (keyCode == 4)
					&& (event.getAction() == 0)) {
				return false;
			}
			if ((keyCode == 4) && (event.getAction() == 0)) {
				if (this.dialog != null)
					this.dialog.dismiss();
				if (webView != null)
					webView.destroy();
				finish();
			}
		} catch (Exception localException) {
		}
		return true;
	}

	private class MyWebViewClient extends WebViewClient {
		Context mContext;

		private MyWebViewClient(Context context) {
			mContext = context;
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				Intent intent = new Intent("android.intent.action.VIEW",Uri.parse(url));
				mContext.startActivity(intent);
			} catch (Exception localException) {
			}
			return true;
		}
	}

	public class OptinDialog extends AlertDialog {
		Context context;

		protected OptinDialog(Context context) {
			super(context);
			this.context = context;
			showOptinDialog();
		}

		private void showOptinDialog() {
			LogUtil.i(TAG,  "Display Privacy & Terms");
			try {
				setTitle(TITLE);
				setIcon(0);
				 
				int[] colors = { Color.parseColor("#A5A5A5"),
						Color.parseColor("#9C9C9C"),
						Color.parseColor("#929493") };
				GradientDrawable drawable = new GradientDrawable(
						GradientDrawable.Orientation.TOP_BOTTOM, colors);

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						-1, -1);

				LinearLayout linearLayout = new LinearLayout(this.context);
				linearLayout.setLayoutParams(layoutParams);

				linearLayout.setOrientation(1);
				float scale = this.context.getResources().getDisplayMetrics().density;

				LinearLayout buttonLayout = new LinearLayout(this.context);
				buttonLayout.setGravity(17);

				buttonLayout.setBackgroundDrawable(drawable);
				LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
						-1, (int) (scale * 60.0F), 2.0F);

				buttonLayoutParams.topMargin = (int) (-(60.0F * scale));
				buttonLayoutParams.gravity = 80;
				buttonLayout.setOrientation(0);
				buttonLayout.setLayoutParams(buttonLayoutParams);

				TextView closeText = new TextView(this.context);
				closeText.setGravity(17);
				LinearLayout.LayoutParams btparParams = new LinearLayout.LayoutParams(
						-1, -2, 2.0F);

				btparParams.gravity = 17;

				closeText.setLayoutParams(btparParams);
				closeText.setTextColor(-16777216);

				closeText.setTextAppearance(this.context, 16843271);

				SpannableString content = new SpannableString("关闭");
				content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
				closeText.setText(content);
				closeText.setId(-2);

				buttonLayout.addView(closeText);

				Button continueButton = new Button(this.context);

				continueButton.setId(-1);
				continueButton.setLayoutParams(new LinearLayout.LayoutParams(
						-1, -2, 2.0F));
				continueButton.setText("Ok");

				buttonLayout.addView(continueButton);

				buttonLayout.setBackgroundColor(-3355444);

				LinearLayout layout = new LinearLayout(this.context);
				LinearLayout.LayoutParams webLayoutParams = new LinearLayout.LayoutParams(
						-1, -1);
				webLayoutParams.bottomMargin = (int) (scale * 60.0F);
				layout.setLayoutParams(webLayoutParams);

				OptinActivity.webView = new WebView(this.context);
				OptinActivity.webView.loadData(OPT_IN_TEXT, "text/html","utf-8");
				OptinActivity.webView.setWebChromeClient(new WebChromeClient());
				OptinActivity.webView
						.setWebViewClient(new OptinActivity.MyWebViewClient(
								OptinActivity.this));
				OptinActivity.webView.setScrollBarStyle(33554432);
				layout.addView(OptinActivity.webView);

				linearLayout.addView(layout);
				linearLayout.addView(buttonLayout);

				setView(linearLayout);

				setCancelable(true);

				closeText.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						try {
							if (AndroidUtil.checkInternetConnection(OptinActivity.this)) {
								OptinActivity.event = "optOut";
								OptinActivity.OptinDialog.this.dismiss();
								OptinActivity.this.asyncTaskCompleteListener.lauchNewHttpTask();
								SinPush.startNewAdThread(false);
							} else {
								OptinActivity.OptinDialog.this.dismiss();
								SinPush.startNewAdThread(false);
								OptinActivity.this.finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				continueButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View arg0) {
						try {
							OptinActivity.OptinDialog.this.dismiss();

							if (AndroidUtil.checkInternetConnection(OptinActivity.OptinDialog.this.context)) {
								OptinActivity.event = "optIn";

								OptinActivity.this.asyncTaskCompleteListener
										.lauchNewHttpTask();
								SinPush.startNewAdThread(true);
							} else {
								SinPush.startNewAdThread(true);

								OptinActivity.this.finish();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
				OptinActivity.this.finish();
			}
		}
	}
}
