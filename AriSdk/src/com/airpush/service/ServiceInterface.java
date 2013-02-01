package com.airpush.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airpush.android.IConstants;
import com.airpush.data.MsgInfo;
import com.airpush.util.LogUtil;

public class  ServiceInterface {
	
	private static String TAG = LogUtil.makeLogTag(ServiceInterface.class);
	
	
	public static void startDownload(Context context, MsgInfo msginfo) {
		LogUtil.v(TAG, "action:executeDownload");
	    Intent intent = new Intent(context, SinDownloadService.class);
	    intent.putExtra(IConstants.PARAM_BODY, msginfo);
	    context.startService(intent);
	}
	
	/**
	 * 上报
	 * @param msgId
	 * @param resultCode
	 * @param jsonContent
	 * @param context
	 */
	public static void reportAdActionResult(String msgId, int resultCode, String jsonContent, Context context) {
//		StringBuffer logMsg  = new StringBuffer();
//		logMsg.append("action:reportAdActionResult - adId: " + msgId + ", code: " + resultCode + "-" + StatusCode.getReportDesc(resultCode));
//		if (!StringUtils.isEmpty(jsonContent)) logMsg.append(" report content: " + jsonContent);
//		LogUtil.d(TAG, logMsg.toString());
//		if (null == context) throw new IllegalArgumentException("NULL context");
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put(SinConstants.PushService.PARAM_ADID, msgId);
//			jsonObject.put(SinConstants.PushService.PARAM_RESULT, resultCode);
//			if (!StringUtils.isEmpty(jsonContent)) jsonObject.put(SinReportInterface.DATA, jsonContent);
//		} catch (JSONException e) {
//		}
//
//		reportInfo(context, jsonObject, UDP_PREFIX_REPORT);
	}

}
