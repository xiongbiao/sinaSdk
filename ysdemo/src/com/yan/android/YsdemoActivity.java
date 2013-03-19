package com.yan.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezjoynetwork.marbleblast.R;
import com.uapush.android.api.UAInterface;

public class YsdemoActivity extends Activity implements OnClickListener {
	private TextView pointsTextView;
	private TextView SDKVersionView;
	private Button mBut;
	private EditText msgEdit;		 

	final Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		 UAInterface.setDebugMode(true);
		 UAInterface.setTestMode(true);
		 UAInterface.initPush(this);
		 initView();
	}
	

	private void initView() {
		// TODO Auto-generated method stub

		Button loginButton = (Button) findViewById(R.id.LoginButton);
		Button imagButton = (Button) findViewById(R.id.ImageButton);
		Button suspensionAdButton = (Button) findViewById(R.id.SuspensionButton);
		Button viewAdButton = (Button) findViewById(R.id.ViewButton);
		Button videoAdButton = (Button) findViewById(R.id.VideoAdButton);
		Button fullAdButton = (Button) findViewById(R.id.FullAdButton);
		Button promptBoxAdButton = (Button) findViewById(R.id.promptBoxBtn);
		
		loginButton.setOnClickListener(this);
		imagButton.setOnClickListener(this);
		suspensionAdButton.setOnClickListener(this);
		viewAdButton.setOnClickListener(this);
		promptBoxAdButton.setOnClickListener(this);
		videoAdButton.setOnClickListener(this);
		fullAdButton.setOnClickListener(this);

		pointsTextView = (TextView) findViewById(R.id.PointsTextView);
		SDKVersionView = (TextView) findViewById(R.id.SDKVersionView);
		SDKVersionView.setText("版本号 : "+ ExampleUtil.GetVersion(this));
		
		mBut = (Button) findViewById(R.id.SendButton);
		mBut.setOnClickListener(this);
		msgEdit =  (EditText)findViewById(R.id.msg_info);
	}



	public void onClick(View v) {
		if (v instanceof Button) {
			int id = ((Button) v).getId();
			//無網絡則提示用戶
			if(ExampleUtil.checkInternetConnection(this)){
				if(id==R.id.LoginButton)
					Toast.makeText(this, "SDK初始化中 请稍后……", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "广告稍后就到！", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "网络出现异常，请检查你的网络。", Toast.LENGTH_SHORT).show();
				return ;
			}
			switch (id) {
			case R.id.LoginButton:
				//注册或者登录
				 UAInterface.initPush(getApplicationContext());
				break;
			case R.id.SendButton:
				//自定义json发送
				myJson();
				break;
			case R.id.ImageButton:
				//图片广告
				MsgUtil.sendNotificationImage(getApplicationContext());
				break;
			case R.id.FullAdButton:
				//全屏广告
				MsgUtil.sendFullMsg(getApplicationContext());
				break;
			case R.id.SuspensionButton:
				//悬浮广告
				MsgUtil.sendSuspensionMsg(getApplicationContext());
				break;
			case R.id.ViewButton:
				//view广告
				MsgUtil.sendViewMsg(getApplicationContext());
				break;
			case R.id.VideoAdButton:
				//视频广告
				MsgUtil.sendVideo(getApplicationContext());
				break;
			case R.id.promptBoxBtn:
				//提示框广告
				MsgUtil.sendPromptBoxMsg(getApplicationContext());
				break;
			}
		}
	}

	private void myJson(){
		String msgInfo = msgEdit.getText().toString().trim();
		// TODO  有效检验
		if(TextUtils.isEmpty(msgInfo)){
			Toast.makeText(YsdemoActivity.this, "消息内容为空", Toast.LENGTH_SHORT).show();
			return;
		}
		MsgUtil.sendMyJson(getApplicationContext(), msgInfo);
	}
	 
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

 
}