package com.kkpush.systembridge;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.HashMap;
import com.kkpush.util.MD5;
import com.kkpush.util.StringUtils;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.kkpush.util.WebInterface;
import javax.annotation.PostConstruct;
import com.kkpush.push.domain.ScheduledPush;
import com.kkpush.push.service.PushMsgService;
import com.kkpush.push.service.factory.PushFactory;
import com.kkpush.scheduling.AbstractTimerTask;
import com.kkpush.app.domain.App;
import com.kkpush.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;


/*
 * @creater shidong 
 */
public class PushManager {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private PushMsgService pushMsgService;

	@Autowired
	private AppService appService;

	//存放portal最大的发送消息ID
	private static int PUSH_MSG_MAX_ID = 0;

	//存放定时消息，由于ArrayList的随机访问效率高，此程序访问大于插入操作，故选择ArrayList。
	private List<ScheduledPush> scheduledPushList = Collections.synchronizedList(new ArrayList<ScheduledPush>());

	/*
	 * 由spring启动后自动调用该方法
	 */  
	@SuppressWarnings("unused")
	@PostConstruct
	private void startup() {
//		loadPushMsgMaxId(); 
//		loadScheduledPushTask();
//
//		new Timer().schedule(new ScheduledPushTask(), 3000, 5000);
		logger.info("Scheduled Push Task Started, period=5s");
	}

	/*
	 * 程序启动执行一次
	 * 从数据库加载portal的最大的消息ID
	 */
	private synchronized void loadPushMsgMaxId() {
		PUSH_MSG_MAX_ID = pushMsgService.getPushMsgMaxId();
		logger.info("PUSH_MSG_MAX_ID = " + PUSH_MSG_MAX_ID);
	}

	/*
	 * 程序启动执行一次
	 * 加载未处理的定时任务到内存，并启动task
	 */
	private synchronized void loadScheduledPushTask() {
		synchronized (scheduledPushList) {
			scheduledPushList = pushMsgService.getScheduledPushList();
			Collections.sort(scheduledPushList);
		}
		logger.info(String.format("LoadScheduledPushTask size(%s)", scheduledPushList.size()));
	}

	/*
	 * 获取消息ID
	 */
	public synchronized int getPushMsgMaxId() {
		return ++PUSH_MSG_MAX_ID;
	}

	/*
	 * 定时任务加入缓存
	 */
	public void addScheduledPushTask(ScheduledPush scheduledPush) {
		synchronized (scheduledPushList) {
			scheduledPushList.add(scheduledPush);
			Collections.sort(scheduledPushList);
		}
		logger.info(String.format("add new scheduled push(%s | %s | %s)", scheduledPush.getMsgId(), scheduledPush.getScheduledTime(), scheduledPushList.size()));
	}

	/*
	 * 删除定时任务,这里存在并发的效率问题，稍后使用ThreadLocal优化
	 */
	public void removeScheduledPushTask(ScheduledPush delScheduledPush) {
		Iterator<ScheduledPush> iter = scheduledPushList.iterator();
		ScheduledPush scheduledPush = null;
		while(iter.hasNext()) {
			scheduledPush = iter.next();
			if (scheduledPush.getDevId() == delScheduledPush.getDevId()) {
				if (scheduledPush.getMsgId() == delScheduledPush.getMsgId()) {
					iter.remove();
					break;
				}
			}
		}
	}

	/*
	 * Timer处理消息队列
	 */
	private class ScheduledPushTask extends AbstractTimerTask {
		private boolean canRun = true;

		@Override
		public void run() {
			if (canRun) {
				try {
					logger.debug("队列集合大小 = " + scheduledPushList.size());
					Iterator<ScheduledPush> iter = scheduledPushList.iterator();
					ScheduledPush scheduledPush = null;
					while(iter.hasNext()) {
						scheduledPush = iter.next();
						try {
							if (new Date().compareTo(scheduledPush.getScheduledTime()) >= 0) {
								logger.info(String.format("开始处理队列消息(%s)，消息时间(%s)", scheduledPush.getMsgId(), scheduledPush.getScheduledTime()));
								int errorCode = sendPushMessage(scheduledPush);
								if (errorCode >= 0 || scheduledPush.getRetrySendCount() >= 3) {
									iter.remove();
									if (scheduledPush.getRetrySendCount() >= 3) {
										logger.info("定时推送尝试3次仍然失败，放弃推送 | " + scheduledPush.getMsgId());
									}
								} else {
									if (errorCode == -1) {
										scheduledPush.setRetrySendCount(scheduledPush.getRetrySendCount() + 1);
									}
								}
								logger.info(String.format("队列消息(%s)处理完毕，结果(%s)，剩余未处理消息数量(%s)", scheduledPush.getMsgId(), errorCode, scheduledPushList.size()));
							} else {
								//这里break，是由于队列按时间升序了，起到优化作用。
								break;
							}
						} catch (Exception e) {
							iter.remove();
							logger.error(String.format("Scheduled push(%s) exception,task already been removed.", scheduledPush.getMsgId()));
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					logger.error(String.format("Scheduled push task got exception(%s), canRun=%s", e.getMessage(), canRun));
					e.printStackTrace();
				}
			} else {
				logger.info("Process push delayed, canRun = " + canRun);
			}
		}

	}

	/*
	 * 发送通知or消息
	 */
	public int sendPushMessage(ScheduledPush scheduledPush) {
		if (scheduledPush.isScheduled()) {
			if (pushMsgService.checkScheduledPushCancelled(scheduledPush.getMsgId())) {
				//用户取消了定时发送
				logger.info("Scheduled Push task have been cancelled | " + scheduledPush.getMsgId());
				return 9;
			}
		}
		int errorCode = -1;
		Map<String, Object> pushParam = new HashMap<String, Object>();
		
		if (StringUtils.isEmpty(scheduledPush.getAppKey())) {
			App app = appService.getApp(scheduledPush.getDevId(), scheduledPush.getAppId());
			scheduledPush.setAppKey(app.getAppKey());
			scheduledPush.setApiMasterSecret(app.getApiMasterSecret());
		}
		PushFactory.buildScheduledPush(scheduledPush);

		int sendNo = scheduledPush.getMsgId();
		int receiverType = scheduledPush.getReceiverType();
		String receiverValue = scheduledPush.getReceiverValue();

		pushParam.put("sendno", sendNo);
		pushParam.put("app_key", scheduledPush.getAppKey());
		pushParam.put("receiver_type", receiverType);
		pushParam.put("receiver_value",  receiverValue);
		String input =  String.valueOf(sendNo) + receiverType + receiverValue +scheduledPush.getApiMasterSecret();
		String verificationCode = MD5.MD5Encode(input).toUpperCase();
		pushParam.put("verification_code", verificationCode);
		pushParam.put("msg_type", scheduledPush.getMsgType() == 0? 2 : scheduledPush.getMsgType());
		pushParam.put("msg_content", scheduledPush.getMsgContent());
		pushParam.put("time_to_live", scheduledPush.getTimeToLive());
		//pushParam.put("send_description", "Portal Scheduled Push");
		pushParam.put("platform", scheduledPush.getPlatform());
		pushParam.put("callbackv2_urlv2", scheduledPush.getCallback());

		try {
			errorCode = WebInterface.pushMsg(pushParam);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("msgId", sendNo);
			if (!scheduledPush.isScheduled()) {
				//定时推送考虑到失败问题，不更新
				param.put("opt", 4);
				param.put("errCode", errorCode);
			} else {
				if ((errorCode != 0) && (scheduledPush.getRetrySendCount() < 3)) {
					param.put("retrySendCount", 1);
				} else {
					param.put("opt", 4);
					param.put("errCode", errorCode);
				}
			}
			pushMsgService.upatePushStatus(param);
		} catch (Exception e) {
			logger.error("Push Error："+e.getMessage());
		}
		return errorCode;
	}

}
