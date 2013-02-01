package com.google.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.waps.AppConnect;

import com.kuguo.ad.KuguoAdsManager;

public class MsgActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

//		KuguoAdsManager.getInstance().showKuguoSprite(this,
//				KuguoAdsManager.STYLE_KUZAI);
//		/**
//		 * 加入精品推荐的酷悬，传入1表示显示酷悬
//		 */
//		KuguoAdsManager.getInstance().showKuguoSprite(this,KuguoAdsManager.STYLE_KUXUAN);
//		initView();
		initAd();
	}

	private void initAd() {
		// TODO Auto-generated method stub
		AppConnect.getInstance(this).showOffers(this);
//		//显示推荐列表（软件）
//		AppConnect.getInstance(this).showAppOffers(this);
//		AppConnect.getInstance(this).showOffers(this);
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		KuguoAdsManager.getInstance().recycle(this);
		AppConnect.getInstance(this).finalize();
	}
}
