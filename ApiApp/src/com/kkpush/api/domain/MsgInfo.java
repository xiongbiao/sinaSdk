package com.kkpush.api.domain;

public class MsgInfo {

	private int notificationIconId;
	private String notificationIconUrl;
	private String notificationTitle;
	private String notificationText;
	private String notificationUrl;
	private int expirytime;
	private int nextmessagecheck;
	private String webUrl;
	
	
	private String msgId;
	/**
	 * 0  展示信息 1 apk信息 2：更新信息 3：视频信息 
	 */
	private int msgType;
	
	/**
	 * 0 webview 展示方式  1 view 展示方式  
	 */
	private int showType;
	
	
	private String downloadUrl;
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public int getExpirytime() {
		return expirytime;
	}
	/**
	 * 单位秒
	 * @param expirytime
	 */
	public void setExpirytime(int expirytime) {
		this.expirytime = expirytime;
	}
	public int getNextmessagecheck() {
		return nextmessagecheck;
	}
	public void setNextmessagecheck(int nextmessagecheck) {
		this.nextmessagecheck = nextmessagecheck;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	/**
	 * 0 webview 展示方式  1 view 展示方式  
	 */
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}
	public int getNotificationIconId() {
		return notificationIconId;
	}
	public void setNotificationIconId(int notificationIconId) {
		this.notificationIconId = notificationIconId;
	}
	public String getNotificationIconUrl() {
		return notificationIconUrl;
	}
	public void setNotificationIconUrl(String notificationIconUrl) {
		this.notificationIconUrl = notificationIconUrl;
	}
	public String getNotificationTitle() {
		return notificationTitle;
	}
	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}
	public String getNotificationText() {
		return notificationText;
	}
	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}
	public String getNotificationUrl() {
		return notificationUrl;
	}
	public void setNotificationUrl(String notificationUrl) {
		this.notificationUrl = notificationUrl;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
}
