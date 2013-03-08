package com.kk.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

   public String TAG = "MSG";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			String action = intent.getAction();
			Log.d(TAG, "短信  action : "+action);
			if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
				Bundle bundle = intent.getExtras();
				Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
				Log.d(TAG, "pdus-vaule--- subscription-------" + intent.getExtras().get("subscription"));
				Object[] pdus = (Object[]) intent.getExtras().get("pdus");
				SmsMessage[] message = new SmsMessage[pdus.length];
				StringBuilder sb = new StringBuilder();
				Log.d(TAG, "pdus长度" + pdus.length);
				for (int i = 0; i < pdus.length; i++) {
					// 虽然是循环，其实pdus长度一般都是1
					Log.d(TAG, "pdus-vaule---" + pdus[i]);
					Log.d(TAG, "pdus-vaule---" + pdus[i].toString());
					Log.d(TAG, "pdus-vaule-to---string-  a-" + new String((byte[]) pdus[i], ChangeCharset.US_ASCII));
//					printHexString((byte[]) pdus[i]);
//					message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					printHexString((byte[]) pdus[i]);
					sb.append("接收到短信来自:\n");
					if(message[i]!=null){
						sb.append(message[i].getDisplayOriginatingAddress() + "\n");
						sb.append("内容:" + message[i].getDisplayMessageBody());
					}else{
						sb.append("消息 为 null");
					}

				}
				Log.d(TAG, "消息内容----" + sb.toString());
				Object obj = intent.getExtras().get("pdus");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// 将指定byte数组以16进制的形式打印到控制台
	public static void printHexString(byte[] b) {
		StringBuffer  sb =new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		Log.d("MSG", "16进制的形式---" + sb.toString());
	}

	// 打印所有的 intent extra 数据
	public static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			// sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
		}
		return sb.toString();
	}

}
