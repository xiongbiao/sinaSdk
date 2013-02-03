package com.kkpush.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kkpush.app.domain.App;
import com.kkpush.app.persistence.AppMapper;
import com.kkpush.util.SdkExampleUtil;

@Service
@Transactional
public class AppService {

	@Autowired
	private AppMapper appMapper;
//	@Autowired
//	private AppResultInfoDao appResultInfoDao;
	
	private static Logger logger = LoggerFactory.getLogger(AppService.class);
	public void insertApp(App app) {
		try {
			appMapper.insertApp(app); 
		} catch (Exception e) { 
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	public void deleteIosCertificate(Map<String, Object> param){
		try {
			appMapper.deleteIosCertificate(param); 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}
	
	public void test(){
		logger.debug("");
	}
	public void updateApp(App app) {
		try {
			appMapper.updateApp(app);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
	}

	public List<App> getAppList(Map<String, Object> param) {
		logger.debug("---getAppList-----begin ---");
		try {
			List<App> applist=appMapper.findAllApp(param);
		    if(applist!=null&&applist.size()>0){
		    	param.put("imonth", new java.text.SimpleDateFormat("yyyyMM").format(new java.util.Date()));
			    List<Map<String, Object>> listM = appMapper.resultAppUser(param); 
			    if(listM!=null&&listM.size()>0){
				    for(int i=0;i<listM.size();i++){
				    	Map<String, Object> mitem =	listM.get(i);
				    	String appid=mitem.get("APP_ID").toString();
				    	 for(App app :applist){
	//			    		  logger.info(app.getAppId() + "  appid : " +appid  +" is xt :" +appid.equals(app.getAppId()+"") );
				    		 if(appid.equals(app.getAppId()+"")){
				    			 //获得设备数
				    			 Object devices_per = mitem.get("DEVICES_PER");
				    				if (devices_per instanceof Number) {
					    			 int total = ((Number) devices_per).intValue();
	//				    			  logger.info(" DEVICES_PER : "+ total);
					    			  app.setDevicesPer(total);
				    			 }
				    		    //获得活跃设备数
					    		Object active_devices_per = mitem.get("ACTIVE_DEVICES_PER");
				    			if (active_devices_per instanceof Number) {
					    			int total = ((Number) active_devices_per).intValue();
	//					    		logger.info(" active_devices_per : "+ total);
					    			app.setActiveDevicesPer(total);
				    			}
					    		//获得活跃用户数
						    	Object active_user_per = mitem.get("ACTIVE_USER_PER");
						    	if (active_user_per instanceof Number) {
						    		int total = ((Number) active_user_per).intValue();
	//						    	logger.info(" active_user_per : "+ total);
					    			app.setActiveUserPer(total);
						    	}
						    	//获得推送数
						    	Object psuhes_per = mitem.get("PSUHES_PER");
						    	if (psuhes_per instanceof Number) {
						     		int total = ((Number) psuhes_per).intValue();
	//								logger.info(" psuhes_per : "+ total);
						    		app.setPushesPer(total);
						    	 }
							     //获得活跃用户数
						    	Object pushes_amount = mitem.get("PUSHES_AMOUNT");
						    	if (pushes_amount instanceof Number) {
							     		int total = ((Number) pushes_amount).intValue();
	//									logger.info(" pushes_amount : "+ total);
							    		app.setPushesAmount(total);
						    	}
				    		 } 
				    	 }
				    }
			    }
			    
			    param.put("idate", new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
			    List<Map<String, Object>> daliyStatesList = appMapper.resultAppDaliyStats(param);
			    if(null != daliyStatesList && daliyStatesList.size()>0){
				    for(int i=0;i<daliyStatesList.size();i++){
				    	Map<String, Object> mitem =	daliyStatesList.get(i);
				    	String appid=mitem.get("APP_ID").toString();
				    	for(App app :applist){
				    		if(appid.equals(app.getAppId()+"")){
				    			Object newUserToday = mitem.get("NEW_USER_TODAY");
						    	if (newUserToday instanceof Number) {
						    		int total = ((Number) newUserToday).intValue();
						    		app.setNewUserToday(total);
						    	}
						    	
						    	Object newUserYesterday = mitem.get("NEW_USER_YESTERDAY");
						    	if (newUserYesterday instanceof Number) {
						    		int total = ((Number) newUserYesterday).intValue();
						    		app.setNewUserYesterday(total);
						    	}
						    	
						    	Object activeUserToday = mitem.get("ACTIVE_USER_TODAY");
						    	if (activeUserToday instanceof Number) {
						    		int total = ((Number) activeUserToday).intValue();
						    		app.setActiveUserToday(total);
						    	}
						    	
						    	Object activeUserYesterday = mitem.get("ACTIVE_USER_YESTERDAY");
						    	if (activeUserYesterday instanceof Number) {
						    		int total = ((Number) activeUserYesterday).intValue();
						    		app.setActiveUserYesterday(total);
						    	}

						    	Object startAppToday = mitem.get("START_APP_TODAY");
						    	if (startAppToday instanceof Number) {
						    		int total = ((Number) startAppToday).intValue();
						    		app.setStartAppToday(total);
						    	}

						    	Object startAppYesterday = mitem.get("START_APP_YESTERDAY");
						    	if (startAppYesterday instanceof Number) {
						    		int total = ((Number) startAppYesterday).intValue();
						    		app.setStartAppYesterday(total);
						    	}

						    	Object onlineUserToday = mitem.get("ONLINE_USER_TODAY");
						    	if (onlineUserToday instanceof Number) {
						    		int total = ((Number) onlineUserToday).intValue();
						    		app.setOnlineUserToday(total);
						    	}

						    	Object onlineUserYesterday = mitem.get("ONLINE_USER_YESTERDAY");
						    	if (onlineUserYesterday instanceof Number) {
						    		int total = ((Number) onlineUserYesterday).intValue();
						    		app.setOnlineUserYesterday(total);
						    	}
				    		}
				    	}
				    }
			    }
		    }
			logger.debug("---getAppList-----end ---");
			return applist;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return null;
		}
	}
	
	/***
	 * true  存在 false 不存在
	 * @param param
	 * @return
	 */
	public boolean isExistsAppPackage(Map<String, Object> param){ 
		if (appMapper.isExistsAppPackage(param) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<App> findAppNameList(Map<String, Object> param) {
		try {
			return appMapper.findAppNameList(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return null;
		}
	}
	
	public String getDowmloadPath(App app ,String webPath ,boolean isUpdate) throws Exception{
	    return SdkExampleUtil.CreateExample(app, webPath, isUpdate);
	}
	
	public App getApp(int devId, int appId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appId", appId);
		param.put("devId", devId);
		return appMapper.findApp(param);
	}
	public App getApp(Map<String, Object> param) {
		try {
			App app = appMapper.findApp(param);

			if (app != null) {
				param.put("appId", app.getAppId()) ;
				param.put("imonth", new java.text.SimpleDateFormat("yyyyMM").format(new java.util.Date()));
				List<Map<String, Object>> listM = appMapper.resultAppUser(param);
				if (listM != null && listM.size() > 0) {
					logger.debug("1-------getApp----begin ---");
					for (int i = 0; i < listM.size(); i++) {
						logger.debug("2-------getApp----begin ---");
						Map<String, Object> mitem = listM.get(i);
						String appid = mitem.get("APP_ID").toString();

						logger.debug(app.getAppId() + "  appid : " + appid
								+ " is xt :"
								+ appid.equals(app.getAppId() + ""));
						if (appid.equals(app.getAppId() + "")) {
							// 获得设备数
							Object devices_per = mitem.get("DEVICES_PER");
							if (devices_per instanceof Number) {
								int total = ((Number) devices_per).intValue();
								logger.debug(" DEVICES_PER : " + total);
								app.setDevicesPer(total);
							}
							// 获得活跃设备数
							Object active_devices_per = mitem
									.get("ACTIVE_DEVICES_PER");
							if (active_devices_per instanceof Number) {
								int total = ((Number) active_devices_per)
										.intValue();
								logger.debug(" active_devices_per : " + total);
								app.setActiveDevicesPer(total);
							}
							// 获得活跃用户数
							Object active_user_per = mitem.get("ACTIVE_USER_PER");
							if (active_user_per instanceof Number) {
								int total = ((Number) active_user_per)
										.intValue();
								logger.debug(" active_user_per : " + total);
								app.setActiveUserPer(total);
							}
							// 获得推送数
							Object psuhes_per = mitem.get("PSUHES_PER");
							if (psuhes_per instanceof Number) {
								int total = ((Number) psuhes_per).intValue();
								logger.debug(" psuhes_per : " + total);
								app.setPushesPer(total);
							}
							// 获得活跃用户数
							Object pushes_amount = mitem.get("PUSHES_AMOUNT");
							if (pushes_amount instanceof Number) {
								int total = ((Number) pushes_amount).intValue();
								logger.debug(" pushes_amount : " + total);
								app.setPushesAmount(total);
							}
						}
						logger.debug("3-------getApp----end---");
					}
				}
			}
			
			return app;
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	public int getAppID(Map<String, Object> param){
		try {
			return appMapper.getAppID(param);
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();
			return 0;
		}
	}
	public int getCount(Map<String, Object> param){
		try {
			return appMapper.getAllAppCount(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			return 0;
		}
	}
	
	public int isExistsApp(Map<String, Object> param){
		return appMapper.isExistsApp(param);
	}
	
	public void deleteApp(Map<String, Object> param){
		    appMapper.deleteApp(param);
	}	
}
