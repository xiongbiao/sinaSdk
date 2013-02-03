package com.kkpush.app.domain;

import java.io.Serializable;

public class App implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8528475815172035629L;
	private Integer appId;
	private String appName;
	private Integer devId;
	
	private String appPackage;
	private String appKey;
	private String appTypeId;
	private String typeName;
	
	
	private String appIcon;
	private String appDescription;
	private String appImage1;
	
	private String appImage2;
	private String appImage3;
	private String itime;
	
	private String appApk;
	private String lastUpdateTime;
	private int appStatus;
	
	
	
	private String sdkVer;
	
	private int showNum;
	
	private int clickNum;
	
	private double revenue;
	
	private double showClick;
	 
	private  int appStage;
	
	private int isPush;

	private int totalUser ;
	 
	private int isPushAd;
	
	private int isPushMsg;
	
	/**
	 * 证书密码
	 */
	private String certificatePass; 
	
	private String certificatePassTest; 
	/**
	 * 证书名称
	 */
	private String appleCertificate; 
	
	
	private   byte[] appleData;   
	private   byte[] appleDataTest;   
	
	/***
	 * 支持的平台
	 * @return
	 */
	
	private String platform;
	private String apiMasterSecret;
	
	
	public String getApiMasterSecret() {
		return apiMasterSecret;
	}
	public void setApiMasterSecret(String apiMasterSecret) {
		this.apiMasterSecret = apiMasterSecret;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	 
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public Integer getDevId() {
		return devId;
	}
	public void setDevId(Integer devId) {
		this.devId = devId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppTypeId() {
		return appTypeId;
	}
	public void setAppTypeId(String appTypeId) {
		this.appTypeId = appTypeId;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppImage1() {
		return appImage1;
	}
	public void setAppImage1(String appImage1) {
		this.appImage1 = appImage1;
	}
	public String getAppImage2() {
		return appImage2;
	}
	public void setAppImage2(String appImage2) {
		this.appImage2 = appImage2;
	}
	public String getAppImage3() {
		return appImage3;
	}
	public void setAppImage3(String appImage3) {
		this.appImage3 = appImage3;
	}
 
 
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	public int getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(int appStatus) {
		this.appStatus = appStatus;
	}
	public String getAppApk() {
		return appApk;
	}
	public void setAppApk(String appApk) {
		this.appApk = appApk;
	}
	public String getItime() {
		if(itime!=null&&!itime.equals("")){
			itime=itime.substring(0, 10);
		}
		return itime;
	}
	public void setItime(String itime) {
		this.itime = itime;
	}
	public String getLastUpdateTime() {
		if(lastUpdateTime!=null&&!lastUpdateTime.equals("")){
			lastUpdateTime=lastUpdateTime.substring(0, 10);
		}

		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getSdkVer() {
		return sdkVer;
	}
	public void setSdkVer(String sdkVer) {
		this.sdkVer = sdkVer;
	}
	public int getShowNum() {
		return showNum;
	}
	public void setShowNum(int showNum) {
		this.showNum = showNum;
	}
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	/**
	 * 展转化率
	 * @param showClick
	 */
	public double getShowClick() {
//		if(showNum!=0){
//		  return clickNum/showNum;
//		}
		return showClick;
	}
	/**
	 * 展转化率
	 * @param showClick
	 */
	public void setShowClick(double showClick) {
		this.showClick = showClick;
	}
	public int getAppStage() {
		return appStage;
	}
	public void setAppStage(int appStage) {
		this.appStage = appStage;
	}
	public int getIsPush() {
		return isPush;
	}
	public void setIsPush(int isPush) {
		this.isPush = isPush;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTotalUser() {
		return totalUser;
	}
	public void setTotalUser(int totalUser) {
		this.totalUser = totalUser;
	}
  
	//统计数据列  2012年7月9日10:21:31
	
	private  int devicesPer;  //设备数
	private  int activeDevicesPer; //活跃的设备数
	private  int activeUserPer; //
	private  int pushesPer;
	private  int pushesAmount;
	private  int onlineAmount;
	
	//新增统计
	private int newUserToday; //今日新增用户数
	private int newUserYesterday; // 昨日新增用户数
	private int activeUserToday; //今日活跃用户
	private int activeUserYesterday; //昨日活跃用户
	private int startAppToday; //今日启动次数
	private int startAppYesterday; //昨日启动次数
	private int onlineUserToday; //今日在线用户
	private int onlineUserYesterday; //昨日在线用户

	public int getOnlineUserToday() {
		return onlineUserToday;
	}
	public void setOnlineUserToday(int onlineUserToday) {
		this.onlineUserToday = onlineUserToday;
	}
	public int getOnlineUserYesterday() {
		return onlineUserYesterday;
	}
	public void setOnlineUserYesterday(int onlineUserYesterday) {
		this.onlineUserYesterday = onlineUserYesterday;
	}
	public int getNewUserToday() {
		return newUserToday;
	}
	public void setNewUserToday(int newUserToday) {
		this.newUserToday = newUserToday;
	}
	public int getNewUserYesterday() {
		return newUserYesterday;
	}
	public void setNewUserYesterday(int newUserYesterday) {
		this.newUserYesterday = newUserYesterday;
	}
	public int getActiveUserToday() {
		return activeUserToday;
	}
	public void setActiveUserToday(int activeUserToday) {
		this.activeUserToday = activeUserToday;
	}
	public int getActiveUserYesterday() {
		return activeUserYesterday;
	}
	public void setActiveUserYesterday(int activeUserYesterday) {
		this.activeUserYesterday = activeUserYesterday;
	}
	public int getStartAppToday() {
		return startAppToday;
	}
	public void setStartAppToday(int startAppToday) {
		this.startAppToday = startAppToday;
	}
	public int getStartAppYesterday() {
		return startAppYesterday;
	}
	public void setStartAppYesterday(int startAppYesterday) {
		this.startAppYesterday = startAppYesterday;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getOnlineAmount() {
		return onlineAmount;
	}
	public void setOnlineAmount(int onlineAmount) {
		this.onlineAmount = onlineAmount;
	}
	public int getDevicesPer() {
		return devicesPer;
	}
	public void setDevicesPer(int devicesPer) {
		this.devicesPer = devicesPer;
	}
	public int getActiveDevicesPer() {
		return activeDevicesPer;
	}
	public void setActiveDevicesPer(int activeDevicesPer) {
		this.activeDevicesPer = activeDevicesPer;
	}
	public int getActiveUserPer() {
		return activeUserPer;
	}
	public void setActiveUserPer(int activeUserPer) {
		this.activeUserPer = activeUserPer;
	}
 
	public int getPushesAmount() {
		return pushesAmount;
	}
	
	public void setPushesAmount(int pushesAmount) {
		this.pushesAmount = pushesAmount;
	}
	public int getPushesPer() {
		return pushesPer;
	}
	public void setPushesPer(int pushesPer) {
		this.pushesPer = pushesPer;
	}
	public int getIsPushAd() {
		return isPushAd;
	}
	public void setIsPushAd(int isPushAd) {
		this.isPushAd = isPushAd;
	}
	public int getIsPushMsg() {
		return isPushMsg;
	}
	public void setIsPushMsg(int isPushMsg) {
		this.isPushMsg = isPushMsg;
	}
	public String getCertificatePass() {
		return certificatePass;
	}
	public void setCertificatePass(String certificatePass) {
		this.certificatePass = certificatePass;
	}
	public String getAppleCertificate() {
		return appleCertificate;
	}
	public void setAppleCertificate(String appleCertificate) {
		this.appleCertificate = appleCertificate;
	}
	public byte[] getAppleData() {
		return appleData;
	}
	public void setAppleData(byte[] appleData) {
		this.appleData = appleData;
	}
	public byte[] getAppleDataTest() {
		return appleDataTest;
	}
	public void setAppleDataTest(byte[] appleDataTest) {
		this.appleDataTest = appleDataTest;
	}
    public String getCertificatePassTest() {
        return certificatePassTest;
    }
    public void setCertificatePassTest(String certificatePassTest) {
        this.certificatePassTest = certificatePassTest;
    }
	
	
	
	
	
}
