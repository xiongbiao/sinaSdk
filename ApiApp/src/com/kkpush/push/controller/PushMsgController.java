package com.kkpush.push.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkpush.account.domain.Developer;
import com.kkpush.push.domain.ScheduledPush;
import com.kkpush.push.domain.SimplePush;
import com.kkpush.push.service.PushMsgService;
import com.kkpush.systembridge.PushManager;
import com.kkpush.systembridge.SessionBridge;
import com.kkpush.util.Def;
import com.kkpush.util.NumberUtils;
import com.kkpush.util.SpringContextUtils;
import com.kkpush.util.StringUtils;
import com.kkpush.web.controller.PublicController;

@Controller
@RequestMapping("/pushMsg")
public class PushMsgController extends PublicController{
	@Autowired
	public  PushMsgService pushMsgService;

//	@Autowired
//	public PushManager pushManager;

	private static Logger logger = LoggerFactory.getLogger(PushMsgController.class);

	/*
	 * push 消息的回调函数
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.POST)
	public void callback(HttpServletRequest request, HttpServletResponse response) {
		logger.info("callback --------- begin -------");
		Map<String, Object> map = requestToMap(request);
		String result = map.get("push_result") == null ? null : map.get("push_result") + "";
		
		if (result != null && !"".equals(result)) {
			logger.info("callback json : " + result);
			JSONObject json = JSONObject.fromObject(result);
			int sendno = json.getInt("sendno");
			int errcode = json.getInt("errcode");
			String errmsg = json.getString("errmsg") ;
			int total_user = json.getInt("total_user");
			int send_cnt = json.getInt("send_cnt");
			// 如果成功 把 消息的状态改变
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("msgId", sendno);
			param.put("opt", 4);
			param.put("errCode", errcode);
			param.put("errMsg", errmsg);
			param.put("totalUser", total_user);
			param.put("sendCnt", send_cnt);
			pushMsgService.upatePushStatus(param);
		} else {
			logger.info("err callback result is null");
		}
		logger.info("callback --------- end -------");
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public Object sendMsg(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = SessionBridge.getInstance().lookup(request);
		if (null == dev) {
			responseMap.put("success", "false");
			responseMap.put("info", "长时间未操作页面，请刷新后再试！");
			return responseMap;
		}

		Map<String, Object> reqMap = requestToMap(request);
		Object pushType = reqMap.get("pushType");
		Object receiverType = reqMap.get("recipientType");
		Object receiverValue = reqMap.get("sendRecipients");
		Object appId = reqMap.get("appId");
		Object title = reqMap.get("adTitle");
		Object content = reqMap.get("adContent");
		Object msgType = reqMap.get("msgType"); //1：通知，0：消息
		Object builderId = reqMap.get("builderId"); //通知栏样式编号
		Object timeToLive = reqMap.get("timetolive"); //离线消息保存时间

		if ( StringUtils.isEmpty(content) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(pushType) || (!NumberUtils.isNumberic(receiverType)) || (!NumberUtils.isNumberic(msgType)) ) {
			responseMap.put("success", "false");
			responseMap.put("info", "缺少参数");

			return responseMap;
		}

		boolean isScheduled = "1".equals(pushType.toString());
		ScheduledPush scheduledPush = new ScheduledPush();
		int msgId = 0;//pushManager.getPushMsgMaxId();
		scheduledPush.setMsgId(msgId);
		scheduledPush.setReceiverType(Integer.valueOf(receiverType.toString()));
		//设置离线消息时间
		if(!StringUtils.isEmpty(timeToLive) && Integer.parseInt(timeToLive.toString()) != -1){
			scheduledPush.setTimeToLive(Integer.parseInt(timeToLive.toString()));
		}else{
			scheduledPush.setTimeToLive(86400);
		}

		if (scheduledPush.getReceiverType() != 0) {
			scheduledPush.setReceiverValue(String.valueOf(receiverValue));
		} else {
			scheduledPush.setReceiverType(4);
		}
		if (!isScheduled) {
			scheduledPush.setScheduledTime(new Date());
		} else {
			Object startTime = reqMap.get("beginTime");
			try {
				Date scheduleTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(startTime));
				scheduledPush.setScheduledTime(scheduleTime);
			} catch (ParseException pe) {
				responseMap.put("success", "fasle");
				responseMap.put("info", "解析定时时间出错！需按照 yyyy-MM-dd HH:mm:ss 格式");

				return responseMap;
			}
		}
		String platform = "";
		if (null != reqMap.get("android")) {
			platform += "android";
		}
		if (null != reqMap.get("ios")) {
			if (platform.length() > 0) {
				platform += ",";
			}
			platform += "ios";
		}
		if (platform.length() == 0) {
			responseMap.put("success", "fasle");
			responseMap.put("info", "请至少选择一个手机平台！");

			return responseMap;
		}
		Object extrasCount = reqMap.get("custom_count");
		if (null != extrasCount && NumberUtils.isNumberic(extrasCount)) {
			int count = Integer.valueOf(String.valueOf(extrasCount));
			JSONObject jsonObj = new JSONObject();
			for (int i = 1; i <= count; i++) {
				if (null != reqMap.get("custom_key" + i) && !"".equals( reqMap.get("custom_key" + i))) {
					jsonObj.put(SpringContextUtils.urlEncode(reqMap.get("custom_key" + i)), SpringContextUtils.urlEncode(reqMap.get("custom_value" + i)));
				}
			}
			//IOS 扩展字段
			JSONObject iosExt = new JSONObject();
			Object badge = reqMap.get("badge");
			Object sound = reqMap.get("sound");
			
			if (StringUtils.isNotEmpty(sound)) {
				iosExt.put("sound", sound);
			}
			if (NumberUtils.isNumberic(badge)) {
				try {
					iosExt.put("badge", Integer.parseInt(badge.toString()));
				} catch (Exception e) {
					responseMap.put("success", "fasle");
					responseMap.put("info", "badge不是有效的整数");

					return responseMap;
				}
			}
			if (iosExt.size() > 0 && platform.indexOf("ios") >= 0) {
				jsonObj.put("ios", iosExt);
			}
			scheduledPush.setExtras(jsonObj.toString());

		}
		scheduledPush.setAppId(Integer.parseInt(String.valueOf(appId)));
		scheduledPush.setPlatform(platform);
		scheduledPush.setMsgType(Integer.parseInt(String.valueOf(msgType)));
		scheduledPush.setDevId(dev.getDevId());
		scheduledPush.setCreateUser(dev.getEmail());
		scheduledPush.setLastUpdateUser(dev.getEmail());
		scheduledPush.setUsername(dev.getDevName());
		scheduledPush.setTitle(String.valueOf(title));
		scheduledPush.setContent(String.valueOf(content));
		if (NumberUtils.isNumberic(builderId)) {
			scheduledPush.setBuilderId(Integer.parseInt(String.valueOf(builderId)));
		}
		scheduledPush.setScheduled(isScheduled);

		pushMsgService.insertPushMsg(scheduledPush);
		if (isScheduled) {
			//定时任务加入发送队列
			//pushManager.addScheduledPushTask(scheduledPush);

			responseMap.put("adId", msgId);
			responseMap.put("success", "true");
			responseMap.put("info", "成功");
		} else {
			//立即发送
			int errorCode = 0;// pushManager.sendPushMessage(scheduledPush);
			if (errorCode == 0) {
				responseMap.put("adId", msgId);
				responseMap.put("success", "true");
				responseMap.put("info", "成功"); 
			} else {
				responseMap.put("success", "false");
				responseMap.put("info",  Def.getErrStr(errorCode));
			}
		}

		return responseMap;
	}

	@RequestMapping(value = "/getPushList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getPushSendList(@RequestParam int appId, @RequestParam int msgType, @RequestParam boolean isScheduled,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = SessionBridge.getInstance().lookup(request);
		int pageIndex = NumberUtils.getIntValueFromRequest(request, "pageIndex");
		if (pageIndex == 0) pageIndex = 1;
		try {
			if (null != dev) {
				int devId = dev.getDevId();
				int pageSize = 50;
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				param.put("msgType", msgType);
				param.put("appId", appId);
				param.put("pageSize", pageSize);
				param.put("startIndex", (pageIndex-1)*50);
				param.put("isScheduled", isScheduled);

				List<SimplePush> list = pushMsgService.getPushMsgList(param);
				responseMap.put("rows", list);
			} else {
				logger.debug("没有开发信息 ");
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			responseMap.put("info", "查询出错");
			return responseMap;
		}
	}

	/**
	 * 删除一条消息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> deletePush(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = SessionBridge.getInstance().lookup(request);
		try {
			if (null != dev) {
				String targets = request.getParameter("msgids");
				if (null != targets) {
					int devId = Integer.valueOf(dev.getDevId());
					String[] msgIds = targets.split(",");
					int[] msgIdArr = new int[msgIds.length];
					int j = 0;
					boolean hasError = false;
					for (int i=0,size=msgIds.length; i<size; i++) {
						String _msgId = msgIds[i].trim();
						if (NumberUtils.isNumberic(_msgId)) {
							int msgId = Integer.parseInt(_msgId);
							if (msgId > 0) {
								msgIdArr[j] = msgId;
								j++;
							} else {
								hasError = true;
							}
						}
					}
					if (!hasError) {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("devId", devId);
						param.put("msgIds", msgIdArr);
						pushMsgService.deletePush(param);

						ScheduledPush scheduledPush = null;
						for (int msgId : msgIdArr) {
							scheduledPush = new ScheduledPush();
							scheduledPush.setDevId(devId);
							scheduledPush.setMsgId(msgId);
							//pushManager.removeScheduledPushTask(scheduledPush);
						}

						responseMap.put("success", "true");
						responseMap.put("info", "删除成功！");
					} else {
						responseMap.put("success", "false");
						responseMap.put("info", "不合法的参数！");
					}
				}
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("success", "false");
			responseMap.put("info", "删除失败");
			return responseMap;
		}
	}

	/**
	 * 获得一条消息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getMsg", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getMsg(@RequestParam int msgid, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = SessionBridge.getInstance().lookup(request);
		try {
			if (null != dev) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("msgId", msgid);
				param.put("devId", dev.getDevId());
				responseMap.put("method", "getMsg");
				responseMap.put("success", "true");
				responseMap.put("info", pushMsgService.getPushMsg(param));
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("method", "getMsg");
			responseMap.put("success", "false");
			responseMap.put("info", "获得消息详情错误");
			return null;
		}
	}
}
