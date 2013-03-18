package com.sin180.android.sendmsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GetHtmlContentActivity extends Activity {

	private WebView mWebView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		MyJavaScript js = new MyJavaScript();

		mWebView = new WebView(this);

		WebSettings setting = mWebView.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setBuiltInZoomControls(true);

		mWebView.addJavascriptInterface(js, "HTMLOUT");
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			}
		});
		mWebView.loadUrl("http://m.baidu.com/app?action=content&uid=0B068B76E224207CC6B2B156A1600156&from=844b&ssid=0&bd_page_type=1&pu=sz@1320_1001,at@1,gt@111111_0_0#docid=2262602&f=topupsoft@1@14&sample=ab_topupsoft_0&stime=1362985746427");
	}

	class MyJavaScript {
		public void showHTML(String html) {
			Log.d("html",html);
			new AlertDialog.Builder(GetHtmlContentActivity.this)
					.setTitle("HTML").setMessage(html)
					.setPositiveButton(android.R.string.ok, null)
					.setCancelable(false).create().show();
		}
	}
}