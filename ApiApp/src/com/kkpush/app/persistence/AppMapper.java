package com.kkpush.app.persistence;


import java.util.List;
import java.util.Map;

import com.kkpush.app.domain.App;

public interface AppMapper { 
	void insertApp(App app);
	void updateApp(App app);
	void deleteApp(Map<String,Object> param);
	void deleteIosCertificate(Map<String,Object> param);
	App findApp(Map<String,Object> param);
	List<App> findAppList(Map<String,Object> param);
	List<App> findAllApp(Map<String,Object> param);
	List<App> findAppNameList(Map<String,Object> param);
	int getCount(Map<String,Object> param);
	int getAllAppCount(Map<String,Object> param);
	int getAppID(Map<String,Object> param); 
	int isExistsAppPackage(Map<String,Object> param );
	int isExistsApp(Map<String,Object> param );
	List<Map<String, Object>> resultAppUser(Map<String,Object> param );
	List<Map<String, Object>> resultAppDaliyStats(Map<String,Object> param );
}
