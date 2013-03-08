package com.kk.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	public String TAG = "SmsReceiver";
	private static String ACTIONNAME = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		try {
			String action = intent.getAction();
			Log.d(TAG, "彩信 action : " + action);
			Bundle bundle = intent.getExtras();
			Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: "
					+ MyReceiver.printBundle(bundle));
			Log.d(TAG, "subscription--"
					+ intent.getExtras().get("subscription"));
			Log.d(TAG,
					"transactionId--" + intent.getExtras().get("transactionId"));
			Log.d(TAG, "pduType--" + intent.getExtras().get("pduType"));
			Log.d(TAG, "header--" + intent.getExtras().get("header"));
			Log.d(TAG,
					"contentTypeParameters--" + intent.getExtras().get("contentTypeParameters"));
			if (action.equals("android.provider.Telephony.WAP_PUSH_RECEIVED")) {

			}

			if (intent != null && intent.getAction() != null
					&& ACTIONNAME.compareToIgnoreCase(intent.getAction()) == 0) {
				Object[] pdu = (Object[]) intent.getExtras().get("pdus");
				SmsMessage[] msg = new SmsMessage[pdu.length];
				for (int i = 0; i < msg.length; i++) {
					msg[i] = SmsMessage.createFromPdu((byte[]) pdu[i]);
				}
				StringBuilder sb = new StringBuilder();
				for (SmsMessage allMsg : msg) {
					sb.append(allMsg.getDisplayOriginatingAddress() + "&");
					sb.append(allMsg.getDisplayMessageBody() + "&");
					sb.append(allMsg.getTimestampMillis() + "&");
				}
				android.util.Log.i("SMS monitor",
						"data received = " + sb.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
