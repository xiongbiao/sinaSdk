package com.kkpush.api.service.msgpool;

import java.util.HashMap;
import java.util.Map;

import com.kkpush.api.domain.MsgInfo;
import com.kkpush.api.domain.NotificationInfo;

public abstract class MsgFactory {

	protected static final String KEY_MSG_ID = "m_id";
	protected static final String KEY_MSG_TYPE = "m_t";
	protected static final String KEY_DOWNLOAD_URL = "d_url";
	protected static final String KEY_M_CONTENT = "m_c";
	protected static final String KEY_WEB_URL = "w_url";
	protected static final String KEY_NOTI_ICON_INTERNAL_ID = "n_i";
	protected static final String KEY_NOTI_ICON_URL = "n_iocn_url";
	protected static final String KEY_NOTI_TITLE = "n_t";
	protected static final String KEY_NOTI_CONTENT = "n_c";
	protected static final String KEY_NOTI_REMOVE_MODE = "n_f";
	protected static final String KEY_SHOW_TYPE = "show_t";
	/**
	 * 信息的有效时间
	 */
	public static final String KEY_EXPIRY_TIME = "expirytime";
	/**
	 * 下一个条信息的时间
	 */
	public static final String KEY_NEXT_MESSAGE_CHECK = "nextmessagecheck";
	
	protected String msgId;
//	public NotificationInfo notificationInfo;
	protected static MsgInfo msgInfo;
	
	protected static Map<String, Object> msgMap;
	
	public MsgFactory(MsgInfo sgInfo){
		msgInfo = sgInfo;
		System.out.println("-------------------MsgFactory----------------->>>>>>>>>>>>>>>>>");
		msgMap.put(KEY_MSG_ID, msgInfo.getMsgId());
		msgMap.put(KEY_MSG_TYPE, msgInfo.getMsgType());
		
	}
	
	static{
		System.out.println("------------------------------------>>>>>>>>>>>>>>>>>");
		msgMap = new HashMap<String, Object>();
		msgMap.put(KEY_MSG_ID, "0");
		msgMap.put(KEY_MSG_TYPE, 0);
	}
	
	public void getMsgToMap(){
		msgMap.put(KEY_NOTI_ICON_INTERNAL_ID, msgInfo.getNotificationIconId());
		msgMap.put(KEY_NOTI_ICON_URL, msgInfo.getNotificationIconUrl());
		msgMap.put(KEY_NOTI_TITLE, msgInfo.getNotificationTitle());
		msgMap.put(KEY_NOTI_CONTENT, msgInfo.getNotificationText());
		msgMap.put(KEY_NOTI_REMOVE_MODE, "61035");
		msgMap.put(KEY_NEXT_MESSAGE_CHECK, msgInfo.getNextmessagecheck());
		msgMap.put(KEY_EXPIRY_TIME, msgInfo.getExpirytime());
	}
	

//	public abstract String getJsonMsg();
	
	public abstract Map<String, Object> getMap();
	
}
