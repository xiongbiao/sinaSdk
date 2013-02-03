package com.kkpush.account.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 开发者
 * 
 * @author xiongbiao
 * 
 */

public class Developer implements Serializable {

	public Developer(){}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1810570489131042517L;
	private Integer devId;
	private String devName;
	private String contact;
	private String password;
	private String email;
	private String qq;
	private String mobilePhone;
	private String otherContact;
	private String regTime;
	private String lastUpdateTime;
	private String authToken;
	private int isEnabled;
	private int isActivation;
	private String activationCode;
	private int isCustomDiscount;
	private double clickRate;
	private double showRate;
	private String secondPassword;
	private String tempPasswordTime;
	private String companyName;
	private String loginToken;
	private String emailCode;
	private int emailFlag;



		
	public Developer(Integer devId, String devName, String contact,
			String email, String qq, String mobilePhone,
			String otherContact, String regTime, String lastUpdateTime,
			String authToken, int isEnabled, int isActivation,
			String activationCode, int isCustomDiscount, double clickRate,
			double showRate, 
			String companyName, String loginToken, String emailCode,
			int emailFlag, String tokenTime) {
		super();
		this.devId = devId;
		this.devName = devName;
		this.contact = contact;
		this.email = email;
		this.qq = qq;
		this.mobilePhone = mobilePhone;
		this.otherContact = otherContact;
		this.regTime = regTime;
		this.lastUpdateTime = lastUpdateTime;
		this.authToken = authToken;
		this.isEnabled = isEnabled;
		this.isActivation = isActivation;
		this.activationCode = activationCode;
		this.isCustomDiscount = isCustomDiscount;
		this.clickRate = clickRate;
		this.showRate = showRate;
	//	this.secondPassword = secondPassword;
		//this.tempPasswordTime = tempPasswordTime;
		this.companyName = companyName;
		this.loginToken = loginToken;
		this.emailCode = emailCode;
		this.emailFlag = emailFlag;
	
		this.tokenTime = tokenTime;
	}


	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}


	public int getEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(int emailFlag) {
		this.emailFlag = emailFlag;
	}






	private String tokenTime;

	public String getTokenTime() {
		return tokenTime;
	}

	public void setTokenTime(String tokenTime) {
		this.tokenTime = tokenTime;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}



	public String getCompanyName() {
		if(companyName ==null || "null".equals(companyName)){
			companyName="";
		}
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getDevId() {
		return devId;
	}

	public void setDevId(Integer devId) {
		this.devId = devId;
	}



	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}



	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getIsActivation() {
		return isActivation;
	}

	public void setIsActivation(int isActivation) {
		this.isActivation = isActivation;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public int getIsCustomDiscount() {
		return isCustomDiscount;
	}

	public void setIsCustomDiscount(int isCustomDiscount) {
		this.isCustomDiscount = isCustomDiscount;
	}

	public double getClickRate() {
		return clickRate;
	}

	public void setClickRate(double clickRate) {
		this.clickRate = clickRate;
	}

	public double getShowRate() {
		return showRate;
	}

	public void setShowRate(double showRate) {
		this.showRate = showRate;
	}

	public String getSecondPassword() {
		return secondPassword;
	}

	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getRegTime() {
		if(regTime!=null&&!"".equals(regTime)){
			regTime = regTime.substring(0, regTime.lastIndexOf(".")==-1?regTime.length():regTime.lastIndexOf("."));
		}
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

	public String getTempPasswordTime() {
		return tempPasswordTime;
	}

	public void setTempPasswordTime(String tempPasswordTime) {
		this.tempPasswordTime = tempPasswordTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Override
	public String toString() {
		return "Developer [devId=" + devId + ", devName=" + devName
				+ ", contact=" + contact + ", password=" + password
				+ ", email=" + email + ", qq=" + qq + ", mobilePhone="
				+ mobilePhone + ", otherContact=" + otherContact + ", regTime="
				+ regTime + ", lastUpdateTime=" + lastUpdateTime
				+ ", authToken=" + authToken + ", isEnabled=" + isEnabled
				+ ", isActivation=" + isActivation + ", activationCode="
				+ activationCode + ", isCustomDiscount=" + isCustomDiscount
				+ ", clickRate=" + clickRate + ", showRate=" + showRate
				+ ", secondPassword=" + secondPassword + ", tempPasswordTime="
				+ tempPasswordTime + ", companyName=" + companyName
				+ ", loginToken=" + loginToken + ", emailCode=" + emailCode
				+ ", emailFlag=" + emailFlag +"tokenTime="
				+ tokenTime + "]";
	}

	
}
