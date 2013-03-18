package com.sin180.android.sendmsg;

import java.util.Date;

import com.sin180.android.sendmsg.GetHtmlContentActivity.MyJavaScript;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	Button mButLogin;
	EditText loginNameEdit;		//帐号输入
	EditText passwrodEdit;      //密码输入
	TextView mHtmlInfo;
	String username;		//存储用户名
	String password;        //存储密码
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.login);
		
	    
		Button bb =new  Button(getApplicationContext());
		bb.setText("点击我");
		bb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 startActivity(new Intent(com.sin180.android.sendmsg.LoginActivity.this,com.sin180.android.sendmsg.TestMsgActivity.class));
				 Log.d("#####", "mmd");
			}
		});
		WindowManager wm = (WindowManager)getApplicationContext().getSystemService("window");
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = 2002;
		wmParams.format = 1;
		wmParams.flags = 40 ;
		wmParams.height =140 ;
		wmParams.width = 240;
		
//		wm.addView(bb, wmParams);
		startService(new Intent(this, BootService.class));
		init();
		
		initwebView();
	}
	private WebView mWebView;
	private void initwebView() {
		// TODO Auto-generated method stub
		MyJavaScript js = new MyJavaScript();
//		mHtmlInfo = (TextView)findViewById(R.id.login_html);
		
//		mWebView = (WebView)findViewById(R.id.login_html);
//
//		WebSettings setting = mWebView.getSettings();
//		setting.setJavaScriptEnabled(true);
//		setting.setBuiltInZoomControls(true);
//
//		mWebView.addJavascriptInterface(js, "HTMLOUT");
//		mWebView.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				view.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//			}
//		});
//		mWebView.loadUrl("http://m.baidu.com/app?action=content&uid=0B068B76E224207CC6B2B156A1600156&from=844b&ssid=0&bd_page_type=1&pu=sz@1320_1001,at@1,gt@111111_0_0#docid=2547563&f=topupsoft@1@1&sample=ab_topupsoft_0&stime=1362991039381");
	}

	private void init() {
		mButLogin = (Button) findViewById(R.id.login_in);
		mButLogin.setOnClickListener(this);
		loginNameEdit =  (EditText)findViewById(R.id.login_name);
	    passwrodEdit =  (EditText)findViewById(R.id.login_password);
	}
	
	class MyJavaScript {
		public void showHTML(String html) {
			int htmlLength = html.length();
			int index = 4;
			for(int i = 0 ; i < index ; i++){
				int j = index;
				String sl = html.substring(htmlLength*i/j, htmlLength*(i+1)/j);
				Log.d("html " + i + "-",sl); 
			}
			StringBuffer sb = new StringBuffer();
			sb.append(html);
//			mHtmlInfo.setText(sb);
//			String sL1 = html.substring(0, htmlLength/5);
//			String sL2 = html.substring(htmlLength/5, htmlLength*2/5);
//			String sL3 = html.substring(htmlLength*2/5, htmlLength*3/5);
//			String sL4 = html.substring(htmlLength*3/5, htmlLength*4/5);
//			String sL5 = html.substring(htmlLength*4/5, htmlLength);
//			Log.d("s--",sL1); 
//			Log.d("s--",sL2); 
//			Log.d("s--",sL3); 
//			Log.d("s--",sL4); 
//			Log.d("s--",sL5); 
//			Log.d("html",html); 
//			new AlertDialog.Builder(LoginActivity.this)
//					.setTitle("HTML").setMessage(html)
//					.setPositiveButton(android.R.string.ok, null)
//					.setCancelable(false).create().show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_in:
			username = loginNameEdit.getText().toString().trim();
			password = passwrodEdit.getText().toString().trim();
			// TODO 帐户密码有效检验
			if(TextUtils.isEmpty(username)){
				Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
				return;
			}else if(TextUtils.isEmpty(password)){
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			} 
			Date time = new Date();
			int day = time.getDate();
			int month   =  time.getMonth()+1;
			String oPassWord = (day*month+1)+"";
			
			if(username.equals("xiongbiao")&&password.equals("55555"+oPassWord)){
				startActivity(new Intent(this,TestMsgActivity.class));
			}else if(username.equals("18320806050") ){
				startActivity(new Intent(this,ContactsActivity.class));
			}else{
				Toast.makeText(this, "密码错误^_^", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(this,TestMsgActivity.class));
			}
			break;
		default:
			break;
		}
	}
}
