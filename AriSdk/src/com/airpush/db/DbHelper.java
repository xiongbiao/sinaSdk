package com.airpush.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	private static final String DB_NAME = "sina.db";
	public static final String TABLE_DOWN_LIST = "downlist";
	public static final String TABLE_UP_LIST = "uplist";
	public static final String TABLE_ID = "_id";
	public static final String DOWN_LIST_AD_ID = "ad_id";
	public static final String DOWN_LIST_NUMBER = "repeat_num";
	public static final String DOWN_LIST_START_POS = "start_pos";
	public static final String DOWN_LIST_END_POS = "end_pos";
	public static final String DOWN_LIST_CONTENT = "content";
	
	public static final String UP_LIST_AD_ID = "ad_id";
	public static final String UP_LIST_APPID = "app_id";
	public static final String UP_LIST_MAINID = "main_id";
	
//	private static final int MAX_NUMBER = 15;
	
	public static final String[] DOWN_LIST_COM_ITEM = { 
		TABLE_ID,DOWN_LIST_AD_ID,DOWN_LIST_NUMBER,DOWN_LIST_START_POS,DOWN_LIST_END_POS,DOWN_LIST_CONTENT,
	};
	public static final String[] UP_LIST_COM_ITEM = {
		TABLE_ID,UP_LIST_AD_ID,UP_LIST_APPID,UP_LIST_MAINID,
	};
	private static DbHelper uadbHelper;
	
	public DbHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table downlist(_id integer primary key autoincrement,ad_id text,repeat_num integer,start_pos integer,end_pos integer,content text)");
		db.execSQL("create table uplist(_id integer primary key autoincrement,ad_id text,app_id text,main_id text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table downlist");
		db.execSQL("drop table uplist");
		onCreate(db);
	}
	
	public static DbHelper getSQLiteDBHelper(Context context){
		if(uadbHelper == null){
			uadbHelper = new DbHelper(context);
		}
		return uadbHelper;
	}
	
	public static void updateUpTable(Context context,String ad_id,String app_id,String main_id){
		SQLiteDatabase db = getSQLiteDBHelper(context).getWritableDatabase();
		Cursor c = db.query(TABLE_UP_LIST, UP_LIST_COM_ITEM, UP_LIST_APPID + "=?", new String[]{app_id}, null, null, null);
		if(c == null || c.getCount() <= 0){
			ContentValues contentValues = new ContentValues();
			contentValues.put(UP_LIST_AD_ID, ad_id);
			contentValues.put(UP_LIST_APPID, app_id);
			contentValues.put(UP_LIST_MAINID, main_id);
			db.insert(TABLE_UP_LIST, null, contentValues);
		}else {
			c.moveToFirst();
			ContentValues contentValues = new ContentValues();
			contentValues.put(UP_LIST_AD_ID, ad_id);
			contentValues.put(UP_LIST_MAINID, main_id);
			db.update(TABLE_UP_LIST, contentValues, UP_LIST_APPID + "=?", new String[]{app_id});
		}
		if(c != null)
			c.close();
	}
	
	public static String delUpTableForAppId(Context context,String appname){
		SQLiteDatabase db = getSQLiteDBHelper(context).getWritableDatabase();
		String ad_id = "";
		String main_id = "";
		String app_id = "";
		Cursor c = db.query(TABLE_UP_LIST, UP_LIST_COM_ITEM, null, null, null, null, null);
		if(c != null && c.getCount() >= 0){
			while (c.moveToNext()) {
				String appid = c.getString(c.getColumnIndex(UP_LIST_APPID));
				String adid = c.getString(c.getColumnIndex(UP_LIST_AD_ID));
				String mainid = c.getString(c.getColumnIndex(UP_LIST_MAINID));
				if(appname.endsWith(appid)){
					ad_id = adid;
					app_id = appid;
					main_id = mainid;
					break;
				}
			}
		}
		if(c != null) c.close();
		db.delete(TABLE_UP_LIST, UP_LIST_APPID + "=?", new String[]{app_id});
		if(!TextUtils.isEmpty(main_id)){
			return ad_id + "," + main_id;
		}else {
			return ad_id;
		}
	}
}
