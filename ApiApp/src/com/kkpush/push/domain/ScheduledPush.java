package com.kkpush.push.domain;

import java.io.Serializable;
import com.kkpush.util.HttpUrls;

public class ScheduledPush extends Push implements Serializable, Comparable<ScheduledPush> {
	private static final long serialVersionUID = -48509669299284028L;

	private String username;
	
	private String password;
	
	private String verificationCode;
	
	private String sendDescription;
	private String apiMasterSecret;
	
	public String getApiMasterSecret() {
		return apiMasterSecret;
	}

	public void setApiMasterSecret(String apiMasterSecret) {
		this.apiMasterSecret = apiMasterSecret;
	}

	private String callback = HttpUrls.PUSH_CALLBACK;
	
	private int retrySendCount;
	
	public int getRetrySendCount() {
		return retrySendCount;
	}

	public void setRetrySendCount(int retrySendCount) {
		this.retrySendCount = retrySendCount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getSendDescription() {
		return sendDescription;
	}

	public void setSendDescription(String sendDescription) {
		this.sendDescription = sendDescription;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	@Override
	public boolean equals(Object obj) {
		return	null != obj
				? (((ScheduledPush)obj).getMsgId() == this.getMsgId())
				: false;
	}

	@Override
	public int hashCode() {
		return this.getMsgId();
	}

	@Override
	public int compareTo(ScheduledPush scheduledPush) {
		return this.getScheduledTime().compareTo(scheduledPush.getScheduledTime());
	}
	
}
