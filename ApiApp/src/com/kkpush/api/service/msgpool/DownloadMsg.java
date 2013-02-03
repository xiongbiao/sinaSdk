package com.kkpush.api.service.msgpool;

import java.util.HashMap;
import java.util.Map;

import com.kkpush.api.domain.MsgInfo;

public class DownloadMsg extends MsgFactory{

	
	
	public DownloadMsg(MsgInfo msgInfo) {
		super(msgInfo);
	}

//	public String getJsonMsg() {
//		
//		return null;
//	}

	@Override
	public Map<String, Object> getMap() {
		super.getMsgToMap();
		msgMap.put(KEY_SHOW_TYPE, msgInfo.getShowType());
		Map<String, Object> m_content  = new HashMap<String, Object>();
		m_content.put(KEY_WEB_URL, "http://192.168.0.104:8080/test.html");
		m_content.put(KEY_DOWNLOAD_URL, "http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk");
		msgMap.put(KEY_M_CONTENT, m_content);
		return msgMap;
	}

}
