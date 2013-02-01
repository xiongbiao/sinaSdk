package com.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import cn.waps.AppConnect;

import com.google.android.MsgFactory;

public class AdemoActivity extends Activity implements View.OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		MsgFactory.adinit(this);
		MsgFactory.getAdInit(this);
//		MsgFactory.adWooboo(this,Gravity.CENTER);
	}

	public void onClick(View v) {
		if (v instanceof Button) {
			int id = ((Button) v).getId();
			switch (id) {
			case R.id.OffersButton:
				AppConnect.getInstance(this).showOffers(this);
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// KuguoAdsManager.getInstance().recycle(this);
		AppConnect.getInstance(this).finalize();
	}

	 
}