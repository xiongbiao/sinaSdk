package com.kk.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MyJavascript {
    private Context mContext;
	
	public MyJavascript(Context context ) {
		this.mContext = context;
	}


	/*
	 * 拨打电话方法
	 */
	public void call(final String phone) {
		Log.d("MSG", "phone : " + phone);
//		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phone));
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("tel:" + phone));
		mContext.startActivity(browserIntent);
		
		 
	}

	/*
	 * 由java程序生成数据传到网页中显示
	 */
	private String generateData() {
		try {
			// 构造一个json对象
			JSONObject obj1 = new JSONObject();
			obj1.put("id", 12);
			obj1.put("name", "tom");
			obj1.put("phone", "66666666");

			JSONObject obj2 = new JSONObject();
			obj2.put("id", 13);
			obj2.put("name", "jerry");
			obj2.put("phone", "88888888");

			// 将构造好的2个json对象加入到json数组中
			JSONArray arr = new JSONArray();
			arr.put(obj1);
			arr.put(obj2);
			return arr.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
