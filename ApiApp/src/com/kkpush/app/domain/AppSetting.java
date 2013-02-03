package com.kkpush.app.domain;

import java.io.Serializable;

public class AppSetting implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8528479815172035629L;
	
	private Integer id;
	private Integer appId;
	private String key; 
	private String value;
	private String description;
	
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
