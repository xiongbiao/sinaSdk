package com.airpush.data;

import java.io.Serializable;
import java.util.Random;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.airpush.android.FormatAds;
import com.airpush.android.IConstants;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public abstract class MsgInfo  implements Serializable {

	private static String TAG = LogUtil.makeLogTag(FormatAds.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5448948565781401030L;
	//-------------------------one begin--------
	public static final String KEY_MSG_ID = "m_id";
	public static final String KEY_MSG_TYPE = "m_t";
	public static final String KEY_MSG_SHOW_TYPE = "s_t";
	
	public static final int MSG_TYPE_SHOW = 0;
    public static final int MSG_TYPE_APK = 1;
    public static final int MSG_TYPE_VIDEO = 2;
    public static final int MSG_TYPE_UPDATE = 3;
	
	public  String msgId = "";
	/**
	 * 0  下载信息或则更新信息
	 * 1  展示页面
	 * 2  列表信息
	 * 3 悬乎信息 
	 */
	public  int msgType ;
	public  long nextMessageCheckValue;
    /** 0 web展示方式
     *  1 view 展示方式
     *  
     */
	public  String showType = "0";
	
	
	//--------------------config-----------
	public int flag = 0;
	public long expiryTime;
	//-------------------------one end--------
	
	//--------------------notification--con-----------------
	public static final String KEY_NOTI_ICON_INTERNAL_ID = "n_i";
	public static final String KEY_NOTI_ICON_URL = "n_iocn_url";
	public static final String KEY_NOTI_TITLE = "n_t";
	public static final String KEY_NOTI_CONTENT = "n_c";
	public static final String KEY_NOTI_REMOVE_MODE = "n_f";
	
	public   int notificationIconId;
	public   String notificationIconUrl;
	public   String notificationTitle;
	public   String notificationText;
	public   String notificationUrl;
	
	public static final String KEY_M_CONTENT = "m_c";
	public String mContent ;
	
	// 0 : 手动清除模式(默认)，1 : 自动清除模式
	public int notificationRemoveMode;
	public static final int REMOVE_MODE_MANUAL = 0;
	public static final int REMOVE_MODE_AUTO = 1;
	public static final int REMOVE_MODE_MUST_CLICK = 2;
	
	// ------ 下载时需要用到的状态信息
	public boolean _isEverDownloadFailed = false;
	public boolean _isDownloadInterrupted = false;
	public boolean _isDownloadFinisehd = false;
	
	
	public int ringMode = 0;
	public String _fullImagePath;
	// 是否是下载 或则是更新消息
	public boolean  isMsgTypeDownloadAndUpdate(){
		return msgType == MSG_TYPE_UPDATE || msgType == MSG_TYPE_APK;
	}

	
	public boolean isGoBroswer = false;
	public int _downloadRetryTimes = DOWNLOAD_RETRY_TIMES_NOT_SET;
	public static final int DOWNLOAD_RETRY_TIMES_NOT_SET = -1;
	
	
	 public String getDownloadUrl() {
	    	if (isMsgTypeDownloadAndUpdate()) {
	    		return ((DownloadMsgInfo) this).downloadUrl;
	    	} 
//	    	else if (isAdTypeVideo()) {
//	    		return ((VideoAdEntity) this).videoPlayUrl;
//	    	}
	    	else {
	    		return "";
	    	}
	    }
	 
		public String getMd5() {
			String md5 = "";
			if (isMsgTypeDownloadAndUpdate()) {
				DownloadMsgInfo apkEntity = (DownloadMsgInfo) this;
				String s = apkEntity.dMd5;
				if (null != s && !"".equals(s)) {
					md5 = s.trim();
				} else {
					md5 = null;
				}

			}
//			else if (isAdTypeVideo()) {
//				VideoAdEntity videoEntity = (VideoAdEntity) this;
//				String s = videoEntity.videoMd5;
//				if (null != s && !"".equals(s)) {
//					md5 = s.trim();
//				} else {
//					md5 = null;
//				}
//			}
			return md5;
		}
		
		   public String getDownloadedSavedPath() {
		    	if (isMsgTypeDownloadAndUpdate()) {
		    		return ((DownloadMsgInfo) this)._apkSavedPath;
		    	} 
//		    	else if (isAdTypeVideo()) {
//		    		return ((VideoAdEntity) this)._videoSavedPath;
//		    	}
		    	else {
		    		return "";
		    	}
		    }

	
	public boolean parse(Context context, JSONObject secondJsonObject) {
		try {
			
			this.msgId = secondJsonObject.optString(KEY_MSG_ID, "-1");
			this.msgType = secondJsonObject.optInt(KEY_MSG_TYPE, -1);
			//---------config
			this.expiryTime = Long.valueOf(secondJsonObject.isNull("expirytime") ? Long .parseLong("86400000") : secondJsonObject.getLong("expirytime"));
			
			this.nextMessageCheckValue = getNextMessageCheckTime(secondJsonObject);
			this.notificationRemoveMode = secondJsonObject.optInt(KEY_NOTI_REMOVE_MODE, REMOVE_MODE_MANUAL);
			this.notificationIconId = secondJsonObject.optInt(KEY_NOTI_ICON_INTERNAL_ID, 1);
			this.notificationIconUrl = secondJsonObject.optString(KEY_NOTI_ICON_URL, "");
			this.notificationTitle = secondJsonObject.optString(KEY_NOTI_TITLE, "");
			this.notificationText = secondJsonObject.optString(KEY_NOTI_CONTENT, "");
			
			PackageInfo p = context.getPackageManager().getPackageInfo(AndroidUtil.getPackageName(context), 128);
			if(this.notificationIconId==0){
				this.notificationIconId = p.applicationInfo.icon;
			}else{
				this.notificationIconId = selectIcon();
			}
			mContent = secondJsonObject.getString(KEY_M_CONTENT);
			
 		}  catch (Exception e) {
			LogUtil.e(TAG, "Epush parse: " + e.getMessage());
		}
		 
		return false;
	}
	
	private int selectIcon() {
		int icon = 17301620;
		int[] icons = IConstants.ICONS_ARRAY;
		Random rand = new Random();
		int num = rand.nextInt(icons.length - 1);
		icon = icons[num];
		return icon;
	}
	
	public abstract void process(final Context context);
	protected abstract boolean parseMsgContent(Context congtext);
	
	private long getNextMessageCheckTime(JSONObject json) {
		Long nextMsgCheckTime = Long.valueOf(Long.parseLong("300") * 1000L);
		try {
			nextMsgCheckTime = Long.valueOf(Long.parseLong(json.get("nextmessagecheck").toString()) * 1000L);
		} catch (Exception e) {
			e.printStackTrace();
			return 14400000L;
		}
		return nextMsgCheckTime.longValue();
	}
	
	public static class MsgConfig{
		
	}
	
	public static class ConnInfo{
		
	}
	
	/**
	 * 页面展示消息
	 */
	public static class WebShow{
		private static String landingPageAdUrl;

		public static String getLandingPageAdUrl() {
			return landingPageAdUrl;
		}

		public static void setLandingPageAdUrl(String landingPageAdUrl) {
			WebShow.landingPageAdUrl = landingPageAdUrl;
		}
		
	}
	private static String jsonstr;
	private static String trayEvents;
	private static String header;
	private static String adImageUrl;
	private static String delivery_time;
//	private static long expiry_time;
	private static String landingPageAdUrl;
	private static long lastLocationTime = 0L;
	
	//-----------------------------
	
	
	
}
