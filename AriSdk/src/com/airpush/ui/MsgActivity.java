package com.airpush.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;

import com.airpush.android.IConstants;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.data.MsgInfo;
import com.airpush.util.LogUtil;

public class MsgActivity extends Activity {

	WebView webView;
	MsgInfo msgInfo;
	private String TAG = LogUtil.makeLogTag(MsgActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub


getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();

		if (b != null) {
			msgInfo = (MsgInfo) b.getSerializable("msgInfo");
			if (null != msgInfo) {
				switch (msgInfo.msgType) {
				case 1:
					showWebView();
					break;
				default:
					LogUtil.e(TAG, "msginfo type is  null");
					break;
				}
			} else {
				LogUtil.i(TAG, "msginfo is  null");
				return;
			}
		}

	}
	private void showWebView() {
		DownloadMsgInfo wmsginfo = (DownloadMsgInfo)msgInfo; 
		String url = wmsginfo.wUrl;
		webView = new WebView(this);
		addContentView(webView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new SinaWebCallBack(this,msgInfo),IConstants.MyWebView.PARAM_JS_MODULE);
		webView.setWebViewClient(new SinaWebViewClient(msgInfo) {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		// musicWebView.addJavascriptInterface(new Object() {
		// public void clickOnAndroid() {
		// }
		// }, "demo");
		webView.loadUrl(url);
	}

}
