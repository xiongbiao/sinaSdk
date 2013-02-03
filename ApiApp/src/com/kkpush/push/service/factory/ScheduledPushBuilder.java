package com.kkpush.push.service.factory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import net.sf.json.JSONObject;
import com.kkpush.push.domain.ScheduledPush;
import com.kkpush.util.Constants;
import com.kkpush.util.StringUtils;

public class ScheduledPushBuilder {
	
	public void build(ScheduledPush scheduledPush) {
		JSONObject jsonObj = new JSONObject();

		//通知
		if (Constants.PUSH.AD.MSG_D == scheduledPush.getMsgType()) {
			jsonObj.put("n_builder_id", scheduledPush.getBuilderId());
			if(!StringUtils.isEmpty(scheduledPush.getTitle())) {
				jsonObj.put("n_title", encodeString(scheduledPush.getTitle()));
			}
			jsonObj.put("n_content", encodeString(scheduledPush.getContent()));
			jsonObj.put("n_extras", scheduledPush.getExtras());
		}
		//自定义消息
		else if (Constants.PUSH.AD.MSG_MY == scheduledPush.getMsgType()) {
			jsonObj.put("message", encodeString(scheduledPush.getContent()));
			if (!StringUtils.isEmpty(scheduledPush.getTitle()) ) {
				jsonObj.put("title", encodeString(scheduledPush.getTitle()));
			}
			if (!StringUtils.isEmpty(scheduledPush.getExtras()) ) {
				jsonObj.put("extras", scheduledPush.getExtras());
			}
		}
		
		scheduledPush.setMsgContent(jsonObj.toString());
	}
	
	private static String encodeString(String param){
		String encodeParam = "";
		try {
			encodeParam = URLEncoder.encode(param,"utf-8");
		} catch (UnsupportedEncodingException e) {
		
			e.printStackTrace();
		}
		return encodeParam;
	}
	
	
}
