package com.kkpush.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkpush.account.domain.Developer;
import com.kkpush.app.domain.App;
import com.kkpush.app.domain.AppSetting;
import com.kkpush.app.service.AppService;
import com.kkpush.app.service.AppSettingService;

@Controller
@RequestMapping("/app")
public class AppSettingController {
	@Autowired
	AppSettingService appSettingService;
	@Autowired
	AppService appService;
	private static Logger logger = LoggerFactory.getLogger(AppSettingController.class);
	/**
	 * 修改应用广告设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/saveSetting", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveSetting(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		String push_interval = request.getParameter("pushInterval");
		String max_per_day = request.getParameter("maxPerDay");
		String isPush = request.getParameter("isPush");
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		try {
			logger.debug(appId+" : "+push_interval+"  :  " +max_per_day + "  :  " + isPush);
			if (!"".equals(devId)) {
				// 编辑应用设置信息
				if (!"".equals(appId)) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("appId", appId);
				  	appSettingService.deleteAppSetting(param);
				  	List<AppSetting> list = new ArrayList<AppSetting>();
				  	//push_interval表示推送间隔，单位分钟
				  	AppSetting pushIntervalSetting = new AppSetting();
				  	pushIntervalSetting.setAppId(Integer.valueOf(appId));
				  	pushIntervalSetting.setKey("push_interval");
				  	pushIntervalSetting.setValue(push_interval);
				  	list.add(pushIntervalSetting);
				  	pushIntervalSetting = null;
				  	//max_per_day每天每用户最大推送量 
				  	AppSetting maxPerDaySetting = new AppSetting();
				  	maxPerDaySetting.setAppId(Integer.valueOf(appId));
				  	maxPerDaySetting.setKey("max_per_day");
				  	maxPerDaySetting.setValue(max_per_day);
				  	list.add(maxPerDaySetting);
				  	maxPerDaySetting = null;
				  	AppSetting isPushSetting = new AppSetting();
				  	isPushSetting.setAppId(Integer.valueOf(appId));
				  	isPushSetting.setKey("isPush");
				  	isPushSetting.setValue(isPush);				  	
				  	list.add(isPushSetting);
				  	isPushSetting = null;
				  	for(AppSetting appSetting: list){
				  		appSettingService.insertAppSetting(appSetting);
				  	}
					responseMap.put("method", "saveSetting");
					responseMap.put("success", "true");
					responseMap.put("info", "设置成功！");
				} else { 
					responseMap.put("method", "saveSetting");
					responseMap.put("success", "false");
					responseMap.put("info", "请选择应用！");
				}
			} else {
				responseMap.put("method", "save");
				responseMap.put("success", "false");
				responseMap.put("info", "重新登录");
				
				response.sendRedirect("/UaPush/login.jsp");
				return responseMap;
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}

	/**
	 * 修改所有的应用的广告设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/saveAllSetting", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> saveAllSetting(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		//String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
		String push_interval = request.getParameter("pushInterval");
		String max_per_day = request.getParameter("maxPerDay");
		String isPush = request.getParameter("isPush");
		Developer dev=(Developer)request.getSession().getAttribute("user");
		String devId = dev==null?"":dev.getDevId().toString();
		try {
			logger.debug(push_interval+"  :  " +max_per_day + "  :  " + isPush);
			
			if (!"".equals(devId)) {
				Map<String, Object> devParam = new HashMap<String, Object>();
				logger.debug("-in-param - : " + devId);
				devParam.put("devId", devId);
				List<App> appList = appService.getAppList(devParam);
				for(App app: appList){
					// 编辑应用设置信息
					String appId = app.getAppId()+"";
					if (!"".equals(appId)) {
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("appId", appId);
					  	appSettingService.deleteAppSetting(param);
					  	List<AppSetting> list = new ArrayList<AppSetting>();
					  	//push_interval表示推送间隔，单位分钟
					  	AppSetting pushIntervalSetting = new AppSetting();
					  	pushIntervalSetting.setAppId(Integer.valueOf(appId));
					  	pushIntervalSetting.setKey("push_interval");
					  	pushIntervalSetting.setValue(push_interval);
					  	list.add(pushIntervalSetting);
					  	pushIntervalSetting = null;
					  	//max_per_day每天每用户最大推送量 
					  	AppSetting maxPerDaySetting = new AppSetting();
					  	maxPerDaySetting.setAppId(Integer.valueOf(appId));
					  	maxPerDaySetting.setKey("max_per_day");
					  	maxPerDaySetting.setValue(max_per_day);
					  	list.add(maxPerDaySetting);
					  	maxPerDaySetting = null;
					  	AppSetting isPushSetting = new AppSetting();
					  	isPushSetting.setAppId(Integer.valueOf(appId));
					  	isPushSetting.setKey("isPush");
					  	isPushSetting.setValue(isPush);				  	
					  	list.add(isPushSetting);
					  	isPushSetting = null;
					  	for(AppSetting appSetting: list){
					  		appSettingService.insertAppSetting(appSetting);
					  	}
					}
					responseMap.put("method", "saveSetting");
					responseMap.put("success", "true");
					responseMap.put("info", "操作成功！");
				}
			} else {
				responseMap.put("method", "save");
				responseMap.put("success", "false");
				responseMap.put("info", "重新登录"); 
				response.sendRedirect("/UaPush/login.jsp");
				return responseMap;
			}
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}
	@RequestMapping(value = "/getAppSetting", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getAppSetting(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String appId = request.getParameter("appId") != null ? request.getParameter("appId") : "";
			if(!appId.equals("")){ 
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("appId", appId);
				responseMap.put("method", "getAppSetting");
				responseMap.put("success", "true");
				responseMap.put("info", appSettingService.findAppSetting(param));
			 } 
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}
}
