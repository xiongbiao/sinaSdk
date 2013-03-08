package com.sin180.android.sendmsg;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	Button mButLogin;
	EditText loginNameEdit;		//帐号输入
	EditText passwrodEdit;      //密码输入
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
	}

	private void init() {
		mButLogin = (Button) findViewById(R.id.login_in);
		mButLogin.setOnClickListener(this);
		loginNameEdit =  (EditText)findViewById(R.id.login_name);
	    passwrodEdit =  (EditText)findViewById(R.id.login_password);
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
			}else if(username.equals("1832080506") ){
				startActivity(new Intent(this,ContactsActivity.class));
			}else{
				Toast.makeText(this, "密码错误^_^", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
