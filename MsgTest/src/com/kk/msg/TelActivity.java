package com.kk.msg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TelActivity extends Activity{

	private WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		initView();
	}
	
	private void initView(){
		 mWebView = (WebView) findViewById(R.id.wv);
		 mWebView.getSettings().setJavaScriptEnabled(true);
		 mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// TODO Auto-generated method stub
					mWebView.setVisibility(View.GONE);
					super.onReceivedError(view, errorCode, description, failingUrl);
				}
				@Override
				public void onPageFinished(WebView view, String url) {
					// pd.dismiss();
						mWebView.setVisibility(View.VISIBLE);
				}
			});  
		 mWebView.addJavascriptInterface(new MyJavascript(this), "myjavascript");
		 String url = "http://www.baidu.com";
		 url = "file:///android_asset/6.html";
		 mWebView.loadUrl(url); 
		 mWebView.reload();
	}
}
