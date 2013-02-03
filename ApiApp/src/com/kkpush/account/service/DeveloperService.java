package com.kkpush.account.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.account.domain.Developer;
import com.kkpush.account.persistence.DeveloperMapper;

@Service
@Transactional
public class DeveloperService {
	@Autowired
	DeveloperMapper developerMapper;

	private static Logger logger = LoggerFactory
	.getLogger(DeveloperService.class);

	public void insertDeveloper(Developer developer) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("email", developer.getEmail());

		if (isExistsEmail(param)) {
			throw new Exception("邮件已经被注册");

		}
		param = new HashMap<String, Object>();
		param.put("devName", developer.getDevName());
		if (isExistsName(param)) {
			throw new Exception("用户名已经被注册");
		}
		// 新增开发者
		developerMapper.insertDeveloper(developer);
	}

	public void updateDeveloper(Developer developer) {
		logger.debug(developer.getContact());
		developerMapper.updateDeveloper(developer);
	}

	public Developer getDev(Map<String, Object> param) {
		return developerMapper.getDev(param);
	}

	public void updateTempPass(Map<String, Object> param) {
		developerMapper.updateTempPass(param);
	}
	public boolean isExistsName(Map<String, Object> param) {
		if (developerMapper.isExistsName(param) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isExistsEmail(Map<String, Object> param) {
		if (developerMapper.isExistsEmail(param) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public  Map<String, Object> getDevAppCount(Map<String,Object> param){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> listM = developerMapper.getDevAppCount(param); 

		if(listM != null && listM.size( )> 0){
			for(int i=0;i<listM.size();i++){
				Map<String, Object> mitem =	listM.get(i);
				if(mitem != null){
					Object generate_app_count = mitem.get("GENERATE_APP_COUNT");
					if (generate_app_count instanceof Number) {
						result.put("generate_app_count", generate_app_count);
					}else{
						result.put("generate_app_count", 0);
					}
					Object test_app_count = mitem.get("TEST_APP_COUNT");
					if (test_app_count instanceof Number) {
						result.put("test_app_count", test_app_count);
					}else{
						result.put("test_app_count", 0);
					}
				}else{
					result.put("test_app_count", 0);
					result.put("generate_app_count", 0);
				}
			}
		}
		return result;
	}

	/**
	 * 登录
	 * 
	 * @param aeveloper
	 */
	public Developer isExists(Developer developer) {
		return developerMapper.isExists(developer);
	}
	
	public Developer getDeveloper(int developerId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("devId", developerId);
		return getAllDevById(p);
	}

	/**
	 * 找回密码---登录
	 * 
	 * @param aeveloper
	 */
	public Developer isExistsR(Developer developer) {
		return developerMapper.isExistsR(developer);
	}


	public String  getTempPass(Map<String, Object> param) throws Exception{
		try {
			return developerMapper.getTempPass(param) ;
		} catch (Exception e) {
			throw new Exception ("查询临时密码错误");
		}
	}



	public boolean  updateUserToken(Developer dev){
		try {
			developerMapper.updateUserToken(dev);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("修改 登录token失败");
			return false;
		}
	}


	/**
	 * 查询激活码存在不
	 * 
	 * @param param
	 * @return
	 */
	public int Activate(Map<String, Object> param) {
		try {
			return developerMapper.Activate(param);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 激活账号
	 * 
	 * @param param
	 * @return
	 */
	public void updateActivate(Map<String, Object> param) {
		developerMapper.updateActivate(param);
	}

	/***************************************************************************
	 * 经验原密码
	 * 
	 * @param param
	 * @return
	 */
	public boolean isExistsPass(Map<String, Object> param) {
		if (developerMapper.isExistsPass(param) > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updatePass(Map<String, Object> param) {
		developerMapper.updatePass(param);
	}

	/***
	 * 修改邮箱
	 * @param param
	 */
	public void updateEmail(Map<String, Object> param) {
		developerMapper.updateEmail(param);
	}

	public void updateTxPass(Map<String, Object> param) {
		developerMapper.updateTxPass(param);
	}

	public void updateEmailCode(Map<String, Object> param){
		developerMapper.updateEmailCode(param);
	}

	public void updateEmailFlag(Map<String, Object> param){
		developerMapper.updateEmailFlag(param);
	}

	public Developer getAllDevById(Map<String, Object> params){
		return developerMapper.getAllDevById(params);
	}
	//修改用户基本信息
	public void updateDevBase(Map<String, Object> params){
		developerMapper.updateDevBase(params);
	}

	//收集第三方网站过来注册
	public void insertThirdSiteRegister(Map<String, Object> param) {
		developerMapper.insertThirdSiteRegister(param);
	}

}
