package com.android.go.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlHelp extends SQLiteOpenHelper {

	private static String dbName = "test.db";
	private static int version = 1;
	private static SqlHelp msqlHelp;
	private static String M_TABLE = "mytest";
	private static String TABLE_ID = "_id";
	public static final String UP_LIST_MID = "m_id";
	public static final String UP_LIST_NAME = "m_name";
	public static final String UP_LIST_MAINID = "m_call";

	public static final String[] UP_LIST_COM_ITEM = { TABLE_ID, UP_LIST_MID,
			UP_LIST_NAME, UP_LIST_MAINID, };

	public SqlHelp(Context context) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table mytest(_id integer primary key autoincrement,m_id text,m_name text,m_call text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table mytest");
		onCreate(db);
	}

	public void insertData(Context context, String name) {
		SQLiteDatabase db = getSQLiteDBHelper(context).getWritableDatabase();
		Cursor c = db.query(M_TABLE, UP_LIST_COM_ITEM, UP_LIST_NAME + "=?",
				new String[] { name }, null, null, null);
		if (c == null || c.getCount() <= 0) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(UP_LIST_MID, "900");
			contentValues.put(UP_LIST_NAME, name);
			contentValues.put(UP_LIST_MAINID, "wowo");
			db.insert(M_TABLE, null, contentValues);
			Log.d(this.getClass().getName(), "--------->>>>>>>>>>>>数据不存在");
		} else {
			// c.moveToFirst();
			// ContentValues contentValues = new ContentValues();
			// contentValues.put(UP_LIST_AD_ID, ad_id);
			// contentValues.put(UP_LIST_MAINID, main_id);
			// db.update(TABLE_UP_LIST, contentValues, UP_LIST_APPID + "=?", new
			// String[]{app_id});
			Log.d(this.getClass().getName(), "--------->>>>>>>>>>>>数据存在");
		}
		if (c != null)
			c.close();
	}

	public void selectAll(Context context) {
		SQLiteDatabase db = getSQLiteDBHelper(context).getWritableDatabase();
		Cursor c = db.query(M_TABLE, UP_LIST_COM_ITEM, null, null, null, null,
				null);
		if (c != null && c.getCount() > 0) {
			while (c.moveToNext()) {
				Log.d(this.getClass().getName(), "--------->>>>>>>>>>>> " +c.getString(2));
				// your calculation goes here
			}
		}

		if (c != null)
			c.close();
	}

	private SQLiteOpenHelper getSQLiteDBHelper(Context context) {
		if (null == msqlHelp) {
			msqlHelp = new SqlHelp(context);
		}
		return msqlHelp;
	}

}
