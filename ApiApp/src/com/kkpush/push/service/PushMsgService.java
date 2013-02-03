package com.kkpush.push.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kkpush.push.domain.ScheduledPush;
import com.kkpush.push.domain.SimplePush;
import com.kkpush.push.persistence.PushMsgMapper;

@Service
@Transactional
public class PushMsgService{
	@Autowired
	public PushMsgMapper pushMsgMapper;

	/*
	 * 获取定时
	 */
	@Transactional(readOnly = true)
	public List<ScheduledPush> getScheduledPushList() {
		return pushMsgMapper.getScheduledPushList();
	}
	
	public void insertPushMsg(ScheduledPush scheduledPush) {
		pushMsgMapper.insertPushMsg(scheduledPush);
	}
	
	public void insertPushMsgTask(ScheduledPush scheduledPush) {
		pushMsgMapper.insertPushMsgTask(scheduledPush);
	}
	
	/*
	 * 修改push的状态
	 */
	public void upatePushStatus(Map<String,Object> param) {
		pushMsgMapper.upatePushStatus(param);
	}
	
	/*
	 * 删除推送历史消息
	 * @param param
	 */
	public int deletePush(Map<String, Object> param) {
		return pushMsgMapper.deletePush(param);
	}
	
	@Transactional(readOnly = true)
	public SimplePush getPushMsg(Map<String,Object> param) {
		return pushMsgMapper.getPushMsg(param);
	}
	
	/*
	 * 获取最大的消息Id
	 */
	@Transactional(readOnly = true)
	public int getPushMsgMaxId() {
		return pushMsgMapper.getPushMsgMaxId();
	}
	
	/*
	 * 获取消息历史记录
	 */
	@Transactional(readOnly = true)
	public List<SimplePush> getPushMsgList(Map<String, Object> param) {
		return pushMsgMapper.getPushMsgList(param);
	}
	
	/*
	 * 获取消息状态
	 */
	@Transactional(readOnly = true)
	public boolean checkScheduledPushCancelled(int msgId) {
		return pushMsgMapper.checkScheduledPushCancelled(msgId) == 1;
	}
}
