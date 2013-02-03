package com.kkpush.api.persistence;

import java.util.List;
import java.util.Map;

import com.kkpush.api.domain.ApiCallBack;

public interface ApiCallBackMapper {
	
	ApiCallBack selectApiCallBackByCond(Map<String, Object> params);
	void updateApiCallBack(Map<String, Object> params);
	void insertApiCallBack(ApiCallBack apiCallBack);
	List<ApiCallBack> selectApiCallBackList(Map<String,Object> params);
	ApiCallBack selectApiCallBackOne(Map<String, Object> params);
}
