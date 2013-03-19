package ad.json;

import net.sf.json.JSONArray;

public class Ad {
	String adid;
	String apkName;
	String name;
	String info;
	String ad_title;
	String ad_contnet;
	String apk_n;
	String a_ver;
	String a_size;
	
	String ApkUrl;
	String ApkPackageName;
	String notificationIconUrl;
	String notificationContnet;
	String notificationTitle;
	JSONArray adResource;
    String adShowUrl;
	 

	public String getAdShowUrl() {
		return adShowUrl;
	}

	public void setAdShowUrl(String adShowUrl) {
		this.adShowUrl = adShowUrl;
	}

	public String getApkUrl() {
		return ApkUrl;
	}

	public void setApkUrl(String apkUrl) {
		ApkUrl = apkUrl;
	}

	public String getApkPackageName() {
		return ApkPackageName;
	}

	public void setApkPackageName(String apkPackageName) {
		ApkPackageName = apkPackageName;
	}

	public String getNotificationIconUrl() {
		return notificationIconUrl;
	}

	public void setNotificationIconUrl(String notificationIconUrl) {
		this.notificationIconUrl = notificationIconUrl;
	}

	public String getNotificationContnet() {
		return notificationContnet;
	}

	public void setNotificationContnet(String notificationContnet) {
		this.notificationContnet = notificationContnet;
	}

	public String getNotificationTitle() {
		return notificationTitle;
	}

	public void setNotificationTitle(String notificationTitle) {
		this.notificationTitle = notificationTitle;
	}

	public JSONArray getAdResource() {
		return adResource;
	}

	public void setAdResource(JSONArray adResource) {
		this.adResource = adResource;
	}

	public Ad() {
	}

	public Ad(String madid, String mapkName, String mname, String minfo,
			String mad_title, String madContnet, String mapkN, String maVer,
			String maSize) {
		 adid = madid; 
		  apkName=mapkName;
		  name=mname;
		  info=minfo;
		  ad_title=mad_title;
		  ad_contnet=madContnet;
		  apk_n = mapkN ;
		  a_ver = maVer;
		  a_size =maSize;
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAd_title() {
		return ad_title;
	}

	public void setAd_title(String ad_title) {
		this.ad_title = ad_title;
	}

	public String getAd_contnet() {
		return ad_contnet;
	}

	public void setAd_contnet(String ad_contnet) {
		this.ad_contnet = ad_contnet;
	}

	public String getApk_n() {
		return apk_n;
	}

	public void setApk_n(String apk_n) {
		this.apk_n = apk_n;
	}

	public String getA_ver() {
		return a_ver;
	}

	public void setA_ver(String a_ver) {
		this.a_ver = a_ver;
	}

	public String getA_size() {
		return a_size;
	}

	public void setA_size(String a_size) {
		this.a_size = a_size;
	}
}
