package com.google.android;

import java.util.Properties;
import java.util.Random;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import cn.waps.AppConnect;

import com.airpush.android.Airpush;
import com.is.p.ServiceManager;
import com.kuguo.ad.KuguoAdsManager;
import com.tencent.exmobwin.banner.TAdView;
import com.wooboo.adlib_android.WoobooAdView;

public class MsgFactory {

	private static boolean isDug = true;

	public static void setDug(boolean isDug) {
		MsgFactory.isDug = isDug;
	}

	public static void adinit(Context context) {
		try {
			Properties props = loadProperties(context);
			String youmiAppId = props.getProperty("youmiAppId",
					"690b1f2333a8714f");
			String youmiAppPass = props.getProperty("youmiAppPass",
					"4146ca2ef9430650");
			String wapsID = props.getProperty("wapsID",
					"eed47bfc06812621a048e999a8096d7d");
			
			AdManager.init(context, youmiAppId, youmiAppPass, 50, false);
			AppConnect.getInstance(wapsID, "WAPS", context);
			Log.d("MsgFactory", "[MsgFactory] wapsId : " + wapsID
					+ "  youmiAppId : " + youmiAppId + " youmiAppPass :"
					+ youmiAppPass);
		} catch (Exception e) {
			// e.printStackTrace();
			e.printStackTrace();
		}

	}

	private static Properties loadProperties(Context context) {
		Properties props = new Properties();
		try {
			props.load(context.getAssets().open("config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("MsgFactory",
					"[MsgFactory] loadProperties config.properties file is null");
		}
		return props;
	}

	/**
	 * kuogou ad 1
	 * 
	 * @param context
	 */
	public static void adKugou(Context context) {
		try {
			if (!isDug)
				return;
			KuguoAdsManager.getInstance().receivePushMessage(context, false);
			Log.d("MsgFactory", "[MsgFactory] add    Kugou");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * woo boo ad 2
	 * 
	 * @param context
	 * @param where
	 */
	public static void adWooboo(Activity context, int where) {
		try {
			if (!isDug)
				return;
			WoobooAdView ad = new WoobooAdView(context, 0xFF000000, 0xFFFFFFFF,
					40);
			FrameLayout.LayoutParams fl_params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			fl_params.gravity = where | Gravity.RIGHT;
			context.addContentView(ad, fl_params);
			Log.d("MsgFactory", "[MsgFactory] add    adWooboo");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * youmi 3
	 * 
	 * @param context
	 * @param where
	 */
	public static void adYouni(Activity context, int where) {
		try {
		if (!isDug)
			return;
		AdView adView = new AdView(context);
		FrameLayout.LayoutParams fl_params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		fl_params.gravity = where | Gravity.RIGHT;
		context.addContentView(adView, fl_params);
		Log.d("MsgFactory", "[MsgFactory] add    adYouni");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 腾讯 4
	 * 
	 * @param context
	 * @param where
	 */
	public static void adMW(Activity context, int where) {
		try {
			if (!isDug)
				return;
			FrameLayout.LayoutParams fl_params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			fl_params.gravity = where | Gravity.RIGHT;
			TAdView tadView = new TAdView(context);
			context.addContentView(tadView, fl_params);
			Log.d("MsgFactory", "[MsgFactory] add    adMW");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 万普广告 5
	 * 
	 * @param context
	 * @param where
	 */
	public static void adWPS(Activity context, int where) {
		try {
			LinearLayout adLayout = new LinearLayout(context);
			adLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			params.bottomMargin = 0;
			params.rightMargin = 0;
			params.gravity = where | Gravity.RIGHT;
			new cn.waps.AdView(context, adLayout).DisplayAd();
			context.addContentView(adLayout, params);
			Log.d("MsgFactory", "[MsgFactory] add    adWPS");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * iAdPush
	 * 
	 * @param context
	 */
	public static void adIAdPush(Context context) {
		try {

			ServiceManager manager = new ServiceManager(context);
			manager.startService();
			Log.d("MsgFactory", "[MsgFactory] add    adIAdPush");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void getAdInit(Activity context) {
		try {
			addAriPush(context);
			adKugou(context);
			adIAdPush(context);
			getad(4, context, Gravity.TOP);
			getad(9, context, Gravity.BOTTOM);
			Random random1 = new Random(5);
			getad(random1.nextInt(), context, Gravity.BOTTOM);
			getad(random1.nextInt() + 1, context, Gravity.TOP);
			Log.d("MsgFactory", "[MsgFactory] add    add money");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void addAriPush(Context context) {
		try {

			Airpush	airpush = new Airpush(context);
			airpush.startSmartWallAd(); //launch smart wall on App start
			/*
			 * Smart Wall ads: 1: Dialog Ad 2: AppWall Ad 3: LandingPage Ad Only one
			 * of the ad will get served at a time. SDK will ignore the other
			 * requests. To use them all give a gap of 20 seconds between calls.
			 */
			// start Dialog Ad
			 airpush.startDialogAd();
			// start AppWall ad
			 airpush.startAppWall();
			// start Landing Page
			 airpush.startLandingPageAd();		
			
			/*
			 * airpush.startPushNotification(false) requires one boolean parameter
			 * which will used for demo mode if it's true then App will receive demo
			 * ads. Please changed it to false before publishing.
			 */
			airpush.startPushNotification(false);
			// start icon ad.
			airpush.startIconAd();		
			Log.d("MsgFactory", "[MsgFactory] add    addAriPush");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static void getad(int i, Activity context, int where) {
		switch (i) {
		case 1:
			adWooboo(context, where);
			break;
		case 2:
			adYouni(context, where);
			break;
		case 3:
			adMW(context, where);
			break;
		case 4:
			adWPS(context, where);
			break;
		default:
			adMW(context, where);
			break;
		}
	}
}
