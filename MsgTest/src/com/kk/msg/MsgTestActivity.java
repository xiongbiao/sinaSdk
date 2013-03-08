package com.kk.msg;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony.Sms.Intents;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kk.msg.ui.AdR;
import com.kk.msg.ui.MFullView;
import com.kk.msg.ui.PlayView;

public class MsgTestActivity extends Activity implements OnClickListener {

	private Button receiverSend;
	private Button tel;
	private Button telCall;
	private Button mSmsM;
	private EditText addressee;
	private EditText smsContent;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		receiverSend = (Button) findViewById(R.id.receiverSend);
		tel = (Button) findViewById(R.id.tel);
		mSmsM = (Button) findViewById(R.id.sms_m);
		telCall = (Button) findViewById(R.id.tel_call);
		addressee = (EditText) findViewById(R.id.addressee);
		smsContent = (EditText) findViewById(R.id.sms_content);
		receiverSend.setOnClickListener(this);
		tel.setOnClickListener(this);
		telCall.setOnClickListener(this);
		mSmsM.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.receiverSend:
			sendMsg();
			break;
		case R.id.tel:
			tel();
			break;
		case R.id.tel_call:
			// telCall();
			String phone = addressee.getText().toString();
			call(phone);
			break;
		case R.id.sms_m:
			//发送广播
//			Intent intent1 = new Intent();
//			intent1.setAction(Intents.WAP_PUSH_RECEIVED_ACTION);
//			intent1.putExtra("count", 99990);
//			sendBroadcast(intent1); 
//			  MultimediaSms.createFakeSms(MsgTestActivity.this);
//			  
			  
			  
//			MultimediaSms.send(this, "137222222", "ssss", "wwws", "", "");
//			MMmakePdu();
//			ss();
			notifi();
			break;
		default:
			break;
		}
	}
	
	
	private void notifi(){
//		NotificationUtil n = new  NotificationUtil(this);
//		n.showCustomizeNotification();
		
		AdR ad = new AdR();
		//Drawable bg = new Drawable();
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
	    BitmapDrawable bg = new BitmapDrawable(b);
		ad.setBg(bg);
//		MFullView fv = new MFullView(this, ad);
//		fv.setBackgroundDrawable(bg);
//		setContentView(fv);
		
		PlayView v = new PlayView(this);
		addContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
	}
	
	
	private void ss(){
		try {
		File audiofile = new File("sdcard//camera.h264");                                        
		byte     fileContent[] = new byte[(int) audiofile.length()];                                            
		InputStream input = new FileInputStream(audiofile);    

		int data = input.read();                                        
		while(data != -1) {                                           
		    data = input.read(fileContent);                                     
		}
		input.close();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, "12388777766");
		sendIntent.setType("audio/*");
		sendIntent.putExtra(Intent.EXTRA_STREAM, fileContent);
		startActivity(sendIntent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	private void telCall() {
		String phone = addressee.getText().toString();
		// Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("tel:"
		// + phone));
		// startActivity(browserIntent);

		Intent intent = new Intent();
		String action = Intent.ACTION_VIEW;
		Uri data = Uri.parse("tel:" + phone);
		intent.setAction(action);
		intent.setData(data);
		startActivity(intent);
	}

	private void call(String number) {
		Log.d("MSG", "call - number: " + number);
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("call",
					String.class);
			dial.invoke(iTelephony, "8888,999");
			// dial.invoke(iTelephony, number);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void tel() {
		
		Intent addShortCut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		String title =  getString(R.string.app_name);//名称
		Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);//图标
		//点击快捷方式后操作Intent,快捷方式建立后，再次启动该程序  
        Intent intent = new Intent( this, MsgTestActivity.class);  
        //设置快捷方式的标题  
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);  
        //设置快捷方式的图标  
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);  
        //设置快捷方式对应的Intent  
        addShortCut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);  
      //发送广播添加快捷方式  
        sendBroadcast(addShortCut);  
//		Intent intent = new Intent(this, TelActivity.class);
//		startActivity(intent);
	}

	private void sendMsg() {
		String addresseeStr = addressee.getText().toString();
		String smsContentStr = smsContent.getText().toString();
		Log.d("MSG", "addresseeStr : " + addresseeStr + "  smsContentStr: " + smsContentStr);
		SmsUtil.createFakeSms(this, addresseeStr, smsContentStr);
	}

}