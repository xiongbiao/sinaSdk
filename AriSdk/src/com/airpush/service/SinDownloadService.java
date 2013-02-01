package com.airpush.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.airpush.android.IConstants;
import com.airpush.android.NotificationHelper;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.data.MsgInfo;
import com.airpush.db.DbHelper;
import com.airpush.service.DownloadControl.SinDownloadListener;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public class SinDownloadService extends IntentService {


	private MsgInfo msgEntity;
	public static ConcurrentLinkedQueue<MsgInfo> mDownladTasks = new ConcurrentLinkedQueue<MsgInfo>();
	private ToastHandler mToastHandler;
	private NotificationManager mNotificationManager;
    private static String TAG = LogUtil.makeLogTag(SinDownloadService.class); 	
    private static Bundle mDownloadInfos;
    private Notification notification;
    private RemoteViews remoteViews;
    private Integer titleViewId = 0;
    private Integer progressViewId = 0;
    private Integer imageViewId = 0;
    private Integer textProgressId = 0;
    private Integer notificationLayoutId = 0;
    
    private static final String ID_TITLE = "title";
    private static final String ID_PROGRESS_BAR = "progress_bar";
    private static final String ID_ICON = "icon";
    private static final String ID_TEXT_PROGRESS = "text_progress";
    private static final String LAYOUT_FIELD_NAME = "notification_layout";
    
	public SinDownloadService(String name) {
		super(name);
	}

	public SinDownloadService() {
		super("SinDownloadService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		LogUtil.v(TAG, "SinDownloadService:onCreate");
		mToastHandler = new ToastHandler(getApplicationContext());
		
		 mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        if (mDownloadInfos == null) {
	            mDownloadInfos = new Bundle();
	        }
	        
	        try {
	            if(notificationLayoutId == 0){
	                HashMap<String, Integer> idMap =  NotificationHelper.getResourceId(NotificationHelper.RES_TYPE_LAYOUT, new String[]{LAYOUT_FIELD_NAME});
	                if(idMap.size() > 0){
	                    notificationLayoutId = idMap.get(LAYOUT_FIELD_NAME);
	                }
	                idMap =  NotificationHelper.getResourceId(NotificationHelper.RES_TYPE_ID, new String[]{ID_TITLE, ID_PROGRESS_BAR, ID_ICON, ID_TEXT_PROGRESS});
	                if(idMap.size() > 0){
	                    titleViewId = idMap.get(ID_TITLE);
	                    progressViewId = idMap.get(ID_PROGRESS_BAR);
	                    imageViewId = idMap.get(ID_ICON);
	                    textProgressId = idMap.get(ID_TEXT_PROGRESS);
	                }
	            }
	        } catch (Exception e) {
	            LogUtil.w(TAG, "Init progress bar error", e);
	        }
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		LogUtil.v(TAG, "SinDownloadService:onHandleIntent begin");
		if ((Object) intent.getSerializableExtra(IConstants.PARAM_BODY) instanceof MsgInfo) {
			msgEntity = (MsgInfo) intent.getSerializableExtra(IConstants.PARAM_BODY);
		}
		if (msgEntity == null)
			return;
		// 检查sdcard 存在与否
		 if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	            LogUtil.w(TAG, "SDCard can not work");
	            mToastHandler.sendEmptyMessage(0);
	            return;
	     }
		 
		// 下载失败上报
		if (msgEntity._isEverDownloadFailed) {
		//	            ServiceInterface.reportAdResult(msgEntity.msgId, StatusCode.RESULT_TYPE_DOWN_AGAIN, this);
		}
		//添加到集合里面
		if(!mDownladTasks.contains(msgEntity)){
			mDownladTasks.offer(msgEntity);
        }
	        
		final int notifiId = 11;// NotificationHelper.getNofiticationID(adEntity.msgId, NotificationHelper.TYPE_AD_DOWNLOADED);
        
        // 是否静默下载
        final boolean isSilent = isSilent(msgEntity, notifiId);
		
		 new DownloadControl(this, msgEntity, mDownloadInfos, new SinDownloadListener() {
	            
	            public void onDownloading(long downloadLenth, long totalLength) {
	                int percent = (int) (((float) downloadLenth / (float) totalLength) * 100);
	               LogUtil.d(TAG, "pecent:" + percent + ", downloaded:" + downloadLenth + ", total:" + totalLength);
	                if (!isSilent) {
	                    downloadingNotification(msgEntity, notifiId, downloadLenth, totalLength);
	                }
	            }
	            
	            public void onDownloadSucceed(String fileSaveTotalPath, boolean existed) {
	                msgEntity._isDownloadFinisehd = true;
	                mDownladTasks.remove(msgEntity);
	                
	                int reportCode = StatusCode.RESULT_TYPE_DOWN_SUCCEED;
	                if (msgEntity.isMsgTypeDownloadAndUpdate()) {
	                	DownloadMsgInfo downloadEntity = (DownloadMsgInfo) msgEntity;
	                    downloadEntity._apkSavedPath = fileSaveTotalPath;
	                    downloadEntity._isApkPreloadSucceed = false;
//	                    if (ProtocolHelper.isNeedSlienceDownload(apkEntity.needSilenceDownload,  apkEntity.silenceDownloadNetworkOption, DownloadService1.this)) {
//	                        reportCode = StatusCode.RESULT_TYPE_AUTO_DOWN_SUCCEED;
//	                        apkEntity._isApkPreloadSucceed = true;
//	                    }

	                } 
//	                else if (msgEntity.isAdTypeVideo()) {
//	                    VideomsgEntity videoEntity = (VideomsgEntity) msgEntity;
//	                    videoEntity._videoSavedPath = fileSaveTotalPath;
//	                    reportCode = StatusCode.RESULT_TYPE_VIDEO_DOWN_SUCEED;
//	                }

	                if (existed) {
	                    reportCode = StatusCode.RESULT_TYPE_DOWN_EXIST;
	                }

//	                ServiceInterface.reportAdResult(msgEntity.adId, reportCode, DownloadService.this);
	                mHandler.sendEmptyMessageDelayed(notifiId, 500);
	            	msgEntity._isDownloadInterrupted = true;
	                downloadSucceed(msgEntity);
	            }
	            
	            public void onDownloadFailed(int failType) {
	            	mNotificationManager.cancel(notifiId);
	                
	                if (DownloadControl.isRealFailed(failType)) {
	                	msgEntity._isEverDownloadFailed = true;
//	                    ServiceInterface.reportAdResult(msgEntity.adId, StatusCode.RESULT_TYPE_DOWN_FAIL, DownloadService.this);
	                    
	               	 //report failed download info ,since 1.2.4
	                    String apkurl =  "";
	                    try {
	                    	DownloadMsgInfo apk  = (DownloadMsgInfo)msgEntity;
		                    apkurl = apk.downloadUrl;
	                    } catch (Exception e) {
							
						}
	                    ServiceInterface.reportAdActionResult(msgEntity.msgId, StatusCode.RESULT_TYPE_APK_LOAD_FAIL, AndroidUtil.getDownloadFailedClientInfo(SinDownloadService.this, apkurl), SinDownloadService.this);
	                }
	                
	            	msgEntity._isDownloadInterrupted = true;
	                downFailNotification(notifiId, msgEntity, failType);
	            }
	        }, 3000);
		 
			LogUtil.v(TAG, "SinDownloadService:onHandleIntent end");

	}

	 private void downloadSucceed(MsgInfo entity) {
	        String filePath = entity.getDownloadedSavedPath();
	        if ((entity.isMsgTypeDownloadAndUpdate()) && !TextUtils.isEmpty(filePath)) {
	        	DownloadMsgInfo apkEntity = (DownloadMsgInfo) entity;
	            
	            String mainActivity = "";
//	            if (apkEntity.needAutoRunup) {
	                mainActivity = apkEntity.apkMainActivity;
//	            } else {
//	            	mainActivity = ApkAdEntity.MAIN_ACTIVITY_CONST_NOT_AUTO_RUN;
//	            }
	            
	            PackageInfo info = null;
	            try {
	                info = getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_CONFIGURATIONS);
	            } catch (Exception e) {
	                LogUtil.w(TAG, "analyze package error !", e);
	            }
	            String appName = info != null ? info.packageName : "";
	            if (TextUtils.isEmpty(appName) && !TextUtils.isEmpty(apkEntity.apkPackageName)) {
	                appName = apkEntity.apkPackageName;
	            }
	            
	            DbHelper.updateUpTable(this, entity.msgId, appName, mainActivity);
	            
//	            if (ProtocolHelper.isNeedSlienceDownload(apkEntity.needSilenceDownload, apkEntity.silenceDownloadNetworkOption, getApplicationContext())) {
//	                downloadEndNotification(entity, true);
//	            } else {
//	            	if (apkEntity.needShowFinishedNotification) {
	            		downloadEndNotification(entity, false);
//	            	}
//	            	if (apkEntity.needAutoInstall) {
//	                    AndroidUtil.installPackage(getApplicationContext(), filePath);
//	            	}
//	            	if (!apkEntity.needShowFinishedNotification && !apkEntity.needAutoInstall) {
//	            		LogUtil.d(TAG, "NOTE: no action for this downloaded apk");
//	            	}
//	            }
	            
	        } 
//	        else if (entity.isAdTypeVideo() && !TextUtils.isEmpty(filePath)) {
//	            downloadEndNotification(entity, false);
//	            
//	        }
	        else {
	        	LogUtil.d(TAG, "No end notification. is filePath empty ? - " + filePath);
	        }
	    }
	  private boolean isSilent(MsgInfo msgEntity, int notifiId) {
	        boolean isApkSilent = false;
	        if (msgEntity.isMsgTypeDownloadAndUpdate()) {
	        	DownloadMsgInfo dEntity = (DownloadMsgInfo) msgEntity;
//	            if (!ProtocolHelper.isNeedSlienceDownload(apkEntity.needSilenceDownload, apkEntity.silenceDownloadNetworkOption, this)) {
	                downloadingNotification(msgEntity, notifiId, 0, 0);
//	            } else {
//	                isApkSilent = true;
//	            }
	        }
	        return isApkSilent ;
	 }
	  
	   private void downloadingNotification(MsgInfo entity, int notifiId, long downloadLenth, long totalLength) {
	        
	        if(null == notification){
	            notification = new Notification();
	            notification.icon = android.R.drawable.stat_sys_download;
	            notification.when = System.currentTimeMillis();
	            notification.flags = Notification.FLAG_ONGOING_EVENT;
	            notification.defaults = Notification.DEFAULT_LIGHTS;

	            Intent intent = new Intent();
	            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notifiId, intent,
	                    PendingIntent.FLAG_UPDATE_CURRENT);
	            
	            notification.contentIntent = pendingIntent;
	        }
	        
	        String title = entity.notificationTitle;
	        String content = "下载中... ";
	        
	        int percent = (int) (((float) downloadLenth / (float) totalLength) * 100f);
	        
	        if (totalLength > 0) {
	        	content +=  percent + "%";
	        }
	        
	        if(notificationLayoutId != null && notificationLayoutId > 0){
	            if(remoteViews == null){
	                remoteViews = new RemoteViews(getPackageName(), notificationLayoutId);
	                remoteViews.setTextViewText(titleViewId, title);
	                remoteViews.setImageViewResource(imageViewId, msgEntity.notificationIconId);
	            }
	            remoteViews.setTextViewText(textProgressId, percent + "%");
	            remoteViews.setProgressBar(progressViewId, 100, percent, false);
	            notification.contentView = remoteViews;
	        }else{
	            Intent intent = new Intent();
	            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), notifiId, intent,
	                    PendingIntent.FLAG_UPDATE_CURRENT);
	            notification.setLatestEventInfo(this, title, content, pendingIntent);
	        }
	        
	        mNotificationManager.notify(notifiId, notification);
	    }
 
	
	private class ToastHandler extends Handler {
		private Context context;

		public ToastHandler(Context context) {
			super(context.getMainLooper());
			this.context = context;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Toast.makeText(context, "SDCard can not work", Toast.LENGTH_LONG).show();
		}
	}
	
	
	 private void downloadEndNotification(MsgInfo entity, boolean isSilenceDownloadedApk) {
	        Intent intent = null;
	        boolean needOriginalPakcageIcon = entity.isMsgTypeDownloadAndUpdate();
	        boolean needBroadcast = false;
	        
	        if (entity.isMsgTypeDownloadAndUpdate() && !isSilenceDownloadedApk) {
	        	// Will install APK
	        	needBroadcast = true;
	        	
	        	intent = new Intent();
	    	    intent.putExtra(IConstants.PARAM_BODY, entity);
	        	intent.setClass(getApplicationContext(), PushReceiver.class);
	        	intent.setAction(IConstants.PushReceiver.INTENT_NOTIFICATION_INSTALL_CLICKED);
	        	
	            if (entity.isMsgTypeDownloadAndUpdate()) {
	            	entity.notificationText = IConstants.NOTIFICATION_CONTENT_UPDATE_INSTALL;
	            } else {
	            	entity.notificationText = IConstants.NOTIFICATION_CONTENT_APK_INSTALL;
	            }
	            
	        } else {
	        	// AD show
	            intent = AndroidUtil.getIntentForStartPushActivity(getApplicationContext(), entity, false);
	        }
	        
	        int notifyId = NotificationHelper.getNofiticationID(entity.msgId, NotificationHelper.TYPE_AD_SHOW);
	        Notification notification = NotificationHelper.getDefaultNotification(getApplicationContext(), notifyId, intent, entity, needOriginalPakcageIcon, needBroadcast);
	        if(null != notification){
	            mNotificationManager.notify(notifyId, notification);
	        }else{
	            LogUtil.e(TAG, "get notification failed");
	        }
	    }
	    
        /**
         * 下载失败	 
         * @param notifiId
         * @param entity
         * @param failType
         */
	    private void downFailNotification(int notifiId, MsgInfo entity, int failType) {
	    	if (DownloadControl.FAIL_TYPE_NOALERT_AUTO_CONTINUE == failType) return;
	    	
	        String content = null;
	        int flags = Notification.DEFAULT_LIGHTS;
	        if (DownloadControl.FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL == failType) {
	            content = "下载失败。请稍后点击重新下载！";
	        } else if (DownloadControl.FAIL_TYPE_ALERT_CLICK_CONTINUE_404 == failType) {
	        	content = "下载资源失效。请稍后点击重新下载！";
	        } else if (DownloadControl.FAIL_TYPE_ALERT_AUTO_CONTINUE == failType) {
	        	content = "当前网络不可用。稍后会继续下载！";
	        	flags = Notification.FLAG_ONGOING_EVENT;
	        } else {
	        	return;
	        }
	        
	        String title = msgEntity.notificationTitle;
	        Intent intent = new Intent();
	        if (DownloadControl.isRealFailed(failType)) {
	        	intent.setClass(getApplicationContext(), SinDownloadService.class);
	        	entity._downloadRetryTimes = MsgInfo.DOWNLOAD_RETRY_TIMES_NOT_SET;
	            intent.putExtra(IConstants.PARAM_BODY, entity);
	        }
	        
	        Notification notification = new Notification();
	        notification.icon = android.R.drawable.stat_sys_download_done;
	        notification.when = System.currentTimeMillis();
	        notification.flags = flags;
	        
	        PendingIntent pendingIntent = PendingIntent.getService(this, notifiId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        
	        notification.setLatestEventInfo(getApplicationContext(), title, content, pendingIntent);
	        mNotificationManager.notify(notifiId, notification);
	    }
	    
	    public static boolean hasDownladTask() {
	    	return (mDownladTasks.size() > 0);
	    }
	    
		public static void startDownloadTasks(Context context) {
	        int size = mDownladTasks.size();
	    	LogUtil.d(TAG,"Execute old download task - size:" + size);
	    	
	    	MsgInfo entity = null;
	    	ArrayList<MsgInfo> taskList = new ArrayList<MsgInfo>();
	        while ((entity = mDownladTasks.poll()) != null) {
	            if (entity._isDownloadInterrupted) {
	            	LogUtil.d(TAG, "Starting to download - adId:" + entity.msgId);
//	            	ServiceInterface.executeDownload(context, entity);
	            } else {
	            	LogUtil.v(TAG, "Downloading is still there.");
	            	taskList.add(entity);
	            }
	        }
	        
	        for (MsgInfo task : taskList) {
	        	mDownladTasks.offer(task);
	        }
	    }
		
	    
	    private Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            super.handleMessage(msg);
	            NotificationHelper.cancelNotification(SinDownloadService.this, msg.what);
	        }
	    };
}
