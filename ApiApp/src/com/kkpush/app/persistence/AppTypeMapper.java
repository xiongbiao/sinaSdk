package com.kkpush.app.persistence;

import java.util.List;
import java.util.Map;

import com.kkpush.app.domain.AppType;

public interface AppTypeMapper {
	List<AppType> getAppTypeList(Map<String,Object> param);
}
