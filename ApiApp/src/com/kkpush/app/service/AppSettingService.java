package com.kkpush.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.app.domain.AppSetting;
import com.kkpush.app.persistence.AppSettingMapper;

@Service
@Transactional
public class AppSettingService {

	@Autowired
	private AppSettingMapper appSettingMapper;

	public void insertAppSetting(AppSetting appSetting) {
		try {
			appSettingMapper.insertAppSetting(appSetting); 
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	public void deleteAppSetting(Map<String,Object> param) {
		try {
			appSettingMapper.deleteAppSetting(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<AppSetting> findAppSetting(Map<String,Object> param) {
		try {
			return appSettingMapper.findAppSetting(param);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
}
