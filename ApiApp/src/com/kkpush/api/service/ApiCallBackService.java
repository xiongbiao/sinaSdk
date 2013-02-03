package com.kkpush.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.api.domain.ApiCallBack;
import com.kkpush.api.persistence.ApiCallBackMapper;
@Service
@Transactional
public class ApiCallBackService {

	@Autowired
	public ApiCallBackMapper callBackMapper;

	public	ApiCallBack selectApiCallBackByCond(Map<String, Object> params){
		ApiCallBack apiCallBack =  callBackMapper.selectApiCallBackByCond(params);
		return apiCallBack == null ? new ApiCallBack() : apiCallBack;
	}

	public	void updateApiCallBack(Map<String, Object> params){
		callBackMapper.updateApiCallBack(params);
	}
	public 	void insertApiCallBack(ApiCallBack apiCallBack){
		callBackMapper.insertApiCallBack(apiCallBack);
	}
	
	public List<ApiCallBack> selectApiCallBackList(Map<String,Object> params){
		return callBackMapper.selectApiCallBackList(params);
	}
	
	public ApiCallBack selectApiCallBackOne(Map<String, Object> params){
		return callBackMapper.selectApiCallBackOne(params);
	}
}
