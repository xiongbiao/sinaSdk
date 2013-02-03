package com.kkpush.app.persistence;


import java.util.List;
import java.util.Map;

import com.kkpush.app.domain.AppSetting;

public interface AppSettingMapper {

	void insertAppSetting(AppSetting appSetting);
	void deleteAppSetting(Map<String,Object> param);
	List<AppSetting> findAppSetting(Map<String,Object> param);
}
