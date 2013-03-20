package com.android.go.home;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GohomeActivity extends Activity implements OnClickListener {
	private Button sendMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}

	/**
	 * 数据库测试
	 */
	private void sqlTest() {
		SqlHelp s = new SqlHelp(getApplicationContext());
		s.insertData(getApplicationContext(), new Date().toLocaleString());
		s.selectAll(getApplicationContext());
	}

	/**
	 * 发送通话请求 1 和谁通话 2 是否同意通话
	 * 
	 * 
	 */

	public void askCall() {

	}

	/**
	 * view
	 */

	private void initView() {
		sendMsg = (Button) findViewById(R.id.sendmsg);
		sendMsg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendmsg:
			takle();
			break;

		default:
			break;
		}

	}

	private void takle() {
       new AudioSend().start(); 
	}

}