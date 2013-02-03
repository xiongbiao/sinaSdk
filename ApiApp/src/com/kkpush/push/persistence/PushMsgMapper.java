package com.kkpush.push.persistence;

import java.util.List;
import java.util.Map;
import com.kkpush.push.domain.Push;
import com.kkpush.push.domain.ScheduledPush;
import com.kkpush.push.domain.SimplePush;

public interface PushMsgMapper {
	void insertPushMsg(ScheduledPush scheduledPush);
	
	SimplePush getPushMsg(Map<String,Object> param);
	
	void insertPushMsgTask(Push push);
	
	int deletePush(Map<String, Object> param);
	
	void upatePushStatus(Map<String, Object> param);
	
	List<ScheduledPush> getScheduledPushList();
	
	int getPushMsgMaxId();
	
	List<SimplePush> getPushMsgList(Map<String, Object> param);
	
	int checkScheduledPushCancelled(int msgId);
}
