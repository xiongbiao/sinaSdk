package com.kkpush.user.domain;

import java.io.Serializable;

/**
 * 开发者
 * 
 * @author xiongbiao
 * 
 */

public class UserInfo implements Serializable {

	public UserInfo() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1810570489131042517L;
	private int uId;
	private int appId;

	private String androidId;
	private String version;
	private String useragent;
	private String email;
	private String sdkversion;

	private String networkOperator;
	private String regTime;
	private String lastUpdateTime;
	private String isConnectionFast;

	private String phoneModel;
	private String imei;
	
	public int getuId() {
		return uId;
	}
	public void setuId(int uId) {
		this.uId = uId;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUseragent() {
		return useragent;
	}
	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSdkversion() {
		return sdkversion;
	}
	public void setSdkversion(String sdkversion) {
		this.sdkversion = sdkversion;
	}
	public String getNetworkOperator() {
		return networkOperator;
	}
	public void setNetworkOperator(String networkOperator) {
		this.networkOperator = networkOperator;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getIsConnectionFast() {
		return isConnectionFast;
	}
	public void setIsConnectionFast(String isConnectionFast) {
		this.isConnectionFast = isConnectionFast;
	}
	public String getPhoneModel() {
		return phoneModel;
	}
	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

}
