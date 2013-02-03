package com.kkpush.api.domain;

import java.io.Serializable;

/**
 * 
 * @author zengzhiwu
 *  api 回调地址实体Bean
 */
public class ApiCallBack implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private int id;
	private int devId;
	private String devName;
	private String type;
	private String baseUrl;
	private String callbackUrl;
	private String createDate;
	private String apiSecretKey; //认证key
	private int apiWebsiteValidate; //认证状态
	private String backUrl;
	
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDevId() {
		return devId;
	}
	public void setDevId(int devId) {
		this.devId = devId;
	}
	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getApiSecretKey() {
		return apiSecretKey;
	}
	public void setApiSecretKey(String apiSecretKey) {
		this.apiSecretKey = apiSecretKey;
	}
	public int getApiWebsiteValidate() {
		return apiWebsiteValidate;
	}
	public void setApiWebsiteValidate(int apiWebsiteValidate) {
		this.apiWebsiteValidate = apiWebsiteValidate;
	}
	@Override
	public String toString() {
		return "ApiCallBack [id=" + id + ", devId=" + devId + ", devName="
				+ devName + ", type=" + type + ", baseUrl=" + baseUrl
				+ ", callbackUrl=" + callbackUrl + ", createDate=" + createDate
				+ ", apiSecretKey=" + apiSecretKey + ", apiWebsiteValidate="
				+ apiWebsiteValidate + ", getId()=" + getId() + ", getDevId()="
				+ getDevId() + ", getDevName()=" + getDevName()
				+ ", getType()=" + getType() + ", getBaseUrl()=" + getBaseUrl()
				+ ", getCallbackUrl()=" + getCallbackUrl()
				+ ", getCreateDate()=" + getCreateDate()
				+ ", getApiSecretKey()=" + getApiSecretKey()
				+ ", getApiWebsiteValidate()=" + getApiWebsiteValidate()+"]";
	}
	
}
