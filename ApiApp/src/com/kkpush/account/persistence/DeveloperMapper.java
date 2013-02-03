package com.kkpush.account.persistence;

import java.util.List;
import java.util.Map;

import com.kkpush.account.domain.Developer;


public interface DeveloperMapper {

	void insertDeveloper(Developer developer);
	void updateDeveloper(Developer developer);
	void updateActivate(Map<String,Object> param); 
	List<Map<String, Object>> getDevAppCount(Map<String,Object> param); 
	Developer isExists(Developer aeveloper);
	Developer isExistsR(Developer aeveloper);
	int isExistsName(Map<String,Object> param );
	int isExistsEmail(Map<String,Object> param );
	int isExistsPass(Map<String,Object> param );
	void updatePass(Map<String,Object> param );
	void updateEmail(Map<String,Object> param );
	void updateTxPass(Map<String,Object> param );
	int Activate(Map<String,Object> param);
	String getTempPass(Map<String,Object> param);
	Developer getDev(Map<String,Object> param);
	void updateTempPass(Map<String,Object> param);
	
	void updateUserToken(Developer aeveloper);
	//validate email
	void updateEmailCode(Map<String, Object> param);
	void updateEmailFlag(Map<String, Object> param);
	Developer getAllDevById(Map<String, Object> params);
	void updateDevBase(Map<String, Object> params);//修改用户基本信息

	
	//新增，收集从第三方站过来的用户注册
	void insertThirdSiteRegister(Map<String, Object> param);
}
