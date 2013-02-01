package com.airpush.ui;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.Toast;

import com.airpush.data.DownloadMsgInfo;
import com.airpush.data.MsgInfo;
import com.airpush.service.ServiceInterface;
import com.airpush.util.LogUtil;

public class SinaWebCallBack implements JavaScriptCallBack{

	private String TAG = LogUtil.makeLogTag(SinaWebCallBack.class);
//	private Context mContext;
	private final WeakReference<Activity> mActivity;
    private final MsgInfo mMsgEntity;
	
	public SinaWebCallBack(Context context,MsgInfo entity) {
        this.mActivity = new WeakReference<Activity>((Activity) context);
        this.mMsgEntity = entity;
	}
	
	
	public void Tel(String num){
		LogUtil.i(TAG, "Tel : " +num);
	}
	
	public void Sms(String conent,String num){
		LogUtil.i(TAG, "Sms : " +num + " conent : " + conent);
	}
	
	public void download(String actionId, String downladUrl, String adType) {
    	LogUtil.v(TAG, "adType from web: " + adType);
    	download(actionId, downladUrl);
    }
    
    public void click(String actionId, String shouldClose, String shouldCancelNotification) {
    	if (null != mActivity.get()) {
    		LogUtil.d(TAG, "SinaWeb callback:click - actionId:" + actionId
    				+ ", shouldClose:" + shouldClose
    				+ ", shouldCancelNotification:" + shouldCancelNotification);
    		userClick(actionId);
    		
    		boolean needClose = false;
    		boolean needCancel = false;
    		try {
    			needClose = Boolean.parseBoolean(shouldClose);
                needCancel = Boolean.parseBoolean(shouldCancelNotification);
    		} catch (Exception e) {
    		}
    		if (needCancel) {
//            	NotificationHelper.cancelNotification(mActivity.get(), mMsgEntity.adId, 
//            			NotificationHelper.TYPE_AD_SHOW);
    		}
    		if (needClose) {
                mActivity.get().finish();
    		}
    	}
    }
	
    public void download(String actionId, String downladUrl) {
    	LogUtil.v(TAG, "actionId from web: " + actionId);
    	if (null == mActivity.get()) return;
    	
		userClick(actionId);
		
		download(downladUrl);
		
//    	NotificationHelper.cancelNotification(mActivity.get(), mMsgEntity.adId, NotificationHelper.TYPE_AD_SHOW);
        mActivity.get().finish();
    }
    
    private void userClick(String param) {
//		int actionId = StatusCode.RESULT_TPPE_INVALID_PARAM;
//		try {
//			actionId = Integer.parseInt(param);
//		} catch (Exception e) {
//			LogUtil.e(TAG, "Invalid actionId from UAWeb - " + param);
//		}
//		ServiceInterface.reportAdResult(mMsgEntity.adId, actionId, mActivity.get());
    }
   
   
   public void download(String downladUrl) {
	   LogUtil.i(TAG, "downladUrl : " +downladUrl);
   	if (null == mActivity.get()) return;
   	
		LogUtil.d(TAG, "SinaWeb callback:download - " + downladUrl);
		Context context = mActivity.get();
		
		if (mMsgEntity.msgType == 1) {
			DownloadMsgInfo downloadEntity = (DownloadMsgInfo) mMsgEntity;
			if (TextUtils.isEmpty(downloadEntity.downloadUrl)) {
				downloadEntity.downloadUrl = downladUrl; 
			}
			//开始下载
			ServiceInterface.startDownload(context, downloadEntity);
//			ServiceInterface.startDownload(context, downloadEntity);
			
//   		if (!TextUtils.isEmpty(apkEntity._apkSavedPath)) {
//   			AndroidUtil.installPackage(context, apkEntity._apkSavedPath);
//           	NotificationHelper.cancelNotification(context, apkEntity.adId, 
//           			NotificationHelper.TYPE_AD_SHOW);
//           	NotificationHelper.cancelNotification(context, apkEntity.adId, 
//           			NotificationHelper.TYPE_AD_DOWNLOADED);
//   			return;
//   		}
   		
		}// else if (entity.isAdTypeVideo()) {
//			VideoAdEntity videoEntity = (VideoAdEntity) entity;
//			if (TextUtils.isEmpty(videoEntity.videoPlayUrl)) {
//				videoEntity.videoPlayUrl = downladUrl; 
//			}
//   		if (!TextUtils.isEmpty(videoEntity._videoSavedPath)) {
//               context.startActivity(AndroidUtil.getIntentForStartPushActivity(context, entity, false));
//   			return;
//   		}
//   		
		else {
			LogUtil.w(TAG, "Invalid adType for download - " + mMsgEntity.msgType);
			return;
		}
//		ServiceInterface.executeDownload(context, entity);
   }
   
   public void Paly(String url){
		LogUtil.i(TAG, "Paly : " +url);
		try {
			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(url);
			mp.start();
		} catch (Exception e) {
          e.printStackTrace();
		}
	
  }
	
   public void close() {
	   LogUtil.d(TAG, "SinaWeb callback:close");
   	if (null != mActivity.get()) {
   		LogUtil.d(TAG, "SinaWeb callback:close but activity not  null");
   		mActivity.get().finish();
   	}
   }
   
   public void showToast(String toast) {
   	if (null != mActivity.get()) {
   		LogUtil.d(TAG, "SinaWeb callback:showToast - " + toast);
   		Toast.makeText(mActivity.get(), toast, Toast.LENGTH_SHORT).show();
   	}
   }
}
