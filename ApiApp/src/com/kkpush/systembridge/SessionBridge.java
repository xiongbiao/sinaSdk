package com.kkpush.systembridge;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.kkpush.account.domain.Developer;
import com.kkpush.util.CommonConstants;

public class SessionBridge {
	private static SessionBridge bridge;
	private SessionBridge(){}

	public static SessionBridge getInstance() {
		if (null == bridge) {
			bridge = new SessionBridge();
		}
		return bridge;
	}

	/*
	 * 从request信息中获取用户。
	 */
	public Developer lookup(HttpServletRequest request) {
		Developer dev = null;
		HttpSession session = request.getSession();
		if (null != session) {
			Object user = session.getAttribute("user");
			if (null != user) {
				dev = (Developer) user;
			}
		}
		return dev;
	}

	/*
	 * 从新设置session
	 */
	public void setDeveloper(HttpServletRequest request,Developer dev) {
		HttpSession session = request.getSession();
		session.setAttribute(CommonConstants.SESSION_USER,dev);
	}

	public Map<String, Object> isSessionOut(HttpServletRequest request){
		Developer developer = lookup(request);
		Map<String, Object> responseMap = new HashMap<String, Object>();;
		if(developer == null)
		{
			responseMap.put("success", false);
			responseMap.put("info","用户信息已过期，请刷新页面后再操作~！");
		}else{
			responseMap.put("user", developer);
		}
		return responseMap;
	}

}
