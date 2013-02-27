package com.airpush.android;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.Adler32;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.airpush.data.MsgInfo;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

public class NotificationHelper {
	private static final String TAG = "NotificationHelper";
    public static final int TYPE_AD_SHOW = 0;
    public static final int TYPE_AD_DOWNLOADED = 1;
    private static Queue<Integer> mNotifactionIdQueue = new LinkedList<Integer>();
    
    public static void cancelNotification(Context context, int notifiId) {
    	LogUtil.d(TAG, "action:cleanNotification - notificationId:" + notifiId);
    	if (null == context) context = SinPush.getmContext();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(notifiId);
    }
    
    public static void cancelNotification(Context context, String adId, int type) {
    	LogUtil.d(TAG, "action:cleanNotification - adId:" + adId);
    	if (null == context) context = SinPush.getmContext();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifiId = getNofiticationID(adId, type);
        nm.cancel(notifiId);
    }
    
    public static void cancelAllNotification(Context context, String adId) {
    	LogUtil.d(TAG, "action:cleanAllNotification - adId:" + adId);
    	if (null == context) context = SinPush.getmContext();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifiId = getNofiticationID(adId, TYPE_AD_SHOW);
        nm.cancel(notifiId);
        notifiId = getNofiticationID(adId, TYPE_AD_DOWNLOADED);
        nm.cancel(notifiId);
    }
    
    public static Notification getDefaultNotification(Context context, 
    		int notifyId, Intent intent, MsgInfo entity, boolean needOriginalPakcageIcon) {
    	return getDefaultNotification(context, notifyId, intent, entity, needOriginalPakcageIcon, false);
    }
//    
//    public static Notification getBrocastNotification(Context context, 
//            int notifyId, Intent intent, MsgInfo entity, boolean needOriginalPakcageIcon) {
//        return getDefaultNotification(context, notifyId, intent, entity, needOriginalPakcageIcon, true);
//    }
//    
    public static Notification getDefaultNotification(Context context, 
    		int notifyId, Intent intent, MsgInfo entity, 
    		boolean needOriginalPakcageIcon, boolean needBroadcast) {
    	
        int appIconId = -1;
        if (needOriginalPakcageIcon) {
            LogUtil.d(TAG, "Need to use original app icon.");
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_GIDS);
                appIconId = packageInfo.applicationInfo.icon;
            } catch (NameNotFoundException e) {
                LogUtil.d(TAG, "", e);
            }
        }
        
        if (appIconId < 0) {
            appIconId = getNotifiIcon(entity.notificationIconId);
        }
        
    	final Notification notification = new Notification();
        notification.when = System.currentTimeMillis();
        notification.icon = appIconId;
        notification.tickerText = entity.notificationTitle;
        notification.flags = getNotifiFlag(entity.notificationRemoveMode);
        notification.defaults = getNotificationRingmode(entity.ringMode);
        
        PendingIntent pendingIntent = null;
        if (needBroadcast) {
        	pendingIntent = PendingIntent.getBroadcast(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
        	pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if(!StringUtils.isEmpty(entity._fullImagePath)){
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(entity._fullImagePath);
                if(null != bitmap){
                    HashMap<String, Integer> resIdMap = NotificationHelper.getResourceId(NotificationHelper.RES_TYPE_ID, new String[] { "state_bar_image_view" });
                    Integer imageId = resIdMap.get("state_bar_image_view");
                    resIdMap = NotificationHelper.getResourceId(NotificationHelper.RES_TYPE_LAYOUT, new String[] { "ad_image_state_bar_layout" });
                    Integer layoutId = resIdMap.get("ad_image_state_bar_layout");
                    if(imageId != null && layoutId != null && imageId > 0 && layoutId > 0){
                        RemoteViews imageRemoteView = new RemoteViews(context.getPackageName(), layoutId);
                        imageRemoteView.setImageViewBitmap(imageId, bitmap);
                        notification.contentView = imageRemoteView;
                        notification.contentIntent = pendingIntent;
                    }else{
                        LogUtil.w(TAG, "get res id failed");
                        return null;
                    }
                }else{
                    LogUtil.w(TAG, "get status bar photo error");
                    return null;
                }
            } catch (Exception e) {
                LogUtil.w(TAG, "init status bar photo error", e);
                return null;
            }
        }else{
            notification.setLatestEventInfo(context, entity.notificationTitle, entity.notificationText, pendingIntent);
        }
        return notification;
    }
//
//    public static void showNotification(final Context context, final MsgInfo entity) {
//        if(Thread.currentThread().getId() == PushService.mainThreadId){
//            LogUtil.i(TAG, "start new thread");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    showN(context, entity);
//                }
//            }).start();
//        }else{
//            showN(context, entity);
//        }
//    }
//    
//    public static void showN(Context context, MsgInfo adEntity){
//        if(null == downloadSystemFullScreenViewRes(context, adEntity)){
//            return;
//        }
//        if (adEntity.newShowType >= 0) {
//            if(null == downloadFloatViewImage(context, adEntity)){
//                return;
//            }
//            Intent intent = new Intent();
//            intent.setAction(SinConstants.PushReceiver.SHOW_FLOAT_VIEW_ACTION);
//            intent.putExtra(SinConstants.PARAM_BODY, adEntity);
//            context.sendBroadcast(intent);
//            return;
//        }
//        if(null == downloadFullStateRes(context, adEntity)){
//            return;
//        }
//        showNotification(context, adEntity, false, false);
//    }
//    
//    private static MsgInfo downloadFullStateRes(final Context context, final MsgInfo entity){
//        if (!StringUtils.isEmpty(entity.notificationCustomizedLayoutImageUrl) && !entity._isHadNCLIUDownloadSuccess) {
//            LogUtil.i(TAG, "download notifactin photo");
//            byte[] imageByte = HttpHelper.httpGet(entity.notificationCustomizedLayoutImageUrl, 5, 5000, 4);
//            if (imageByte != null) {
//                try {
//                    int index = entity.notificationCustomizedLayoutImageUrl.lastIndexOf("/");
//                    String fileName = entity.notificationCustomizedLayoutImageUrl.substring(index + 1);
//                    String filePath = SinDirectoryUtils.getStorageDir(context, entity.adId) + fileName;
//                    if (FileUtil.createImgFile(filePath, imageByte, context)) {
//                        entity._fullImagePath = filePath;
//                        entity._isHadNCLIUDownloadSuccess = true;
//                    } else {
//                        LogUtil.e(TAG, "download status icon success, but save to local have some error");
//                        return null;
//                    }
//                    LogUtil.v(TAG, "Succeed to load image - " + filePath);
//                } catch (Exception e) {
//                    LogUtil.d(TAG, "create img file error", e);
//                    return null;
//                }
//            } else {
//                LogUtil.w(TAG, "download status icon failed，imageUrl: " + entity.notificationCustomizedLayoutImageUrl);
//                return null;
//            }
//        }
//        return entity;
//    }
//    
//    private static MsgInfo downloadFloatViewImage(final Context context, final MsgInfo entity){
//        if (!StringUtils.isEmpty(entity.viewImgUrl)) {
//            LogUtil.i(TAG, "begin to download float photo");
//            byte[] imageByte = HttpHelper.httpGet(entity.viewImgUrl, 5, 5000, 4);
//            if (imageByte != null) {
//                try {
//                    int index = entity.viewImgUrl.lastIndexOf("/");
//                    String fileName = entity.viewImgUrl.substring(index + 1);
//                    String filePath = SinDirectoryUtils.getStorageDir(context, entity.adId) + fileName;
//                    if (FileUtil.createImgFile(filePath, imageByte, context)) {
//                        entity._viewImgPath = filePath;
//                    } else {
//                        LogUtil.e(TAG, "download float photo success,but falied to save it to local");
//                        return null;
//                    }
//                    LogUtil.v(TAG, "Succeed to load image - " + filePath);
//                } catch (Exception e) {
//                    LogUtil.d(TAG, "create img file error", e);
//                    return null;
//                }
//            } else {
//                LogUtil.w(TAG, "dowanload float photo failed，imageUrl: " + entity._viewImgPath);
//                return null;
//            }
//        }
//        return entity;
//    }
//    
//    private static MsgInfo downloadSystemFullScreenViewRes(final Context context, final MsgInfo entity){
//        if (!StringUtils.isEmpty(entity.systemFullScreenView) && !entity._isSFSVHadDownloadSuccess) {
//        	MsgInfo adEntity = MsgInfo.preLoadSystemViewResources(context, entity);
//            if (adEntity == null) {
//                LogUtil.e(TAG, " preload fullscreen res failed!!");
//         
//                return null;
//            }
//            adEntity._isSFSVHadDownloadSuccess = true;
//        }
//        return entity;
//    }
//    
//    public static void showNotification(final Context context, final MsgInfo entity, 
//    		final boolean needOriginalPakcageIcon, final boolean isUpdateVersion) {
//    	LogUtil.v(TAG, "action:showNotification");
//        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        final int notifiId = getNofiticationID(entity.adId, TYPE_AD_SHOW);
//        
//        if (entity.isDeveloperMessage && entity.notificationOnly) {
//        	deliveryDeveloperPushNotification(context, notifiId, entity);
//        	return;
//        }
//        
//        Notification notification = null;
//        Intent intent = null;
//        
//        if(entity._systemViewImageFilePathList != null && entity._systemViewImageFilePathList.size() > 0){
//            intent = new Intent();
//            intent.putExtra(SinConstants.PARAM_BODY, entity);
//            intent.setAction(SinConstants.PushReceiver.SYSTEM_FULLSCREEN_ACTION);
//            intent.addCategory(SinConstants.PushReceiver.SYSTEM_FULLSCREEN_CATEGORY);
//            notification = getBrocastNotification(context, notifiId, intent, entity, needOriginalPakcageIcon);
//        }else{
//            intent = AndroidUtil.getIntentForStartPushActivity(context, entity, isUpdateVersion);
//            notification = getDefaultNotification(context, notifiId, intent, entity, needOriginalPakcageIcon); 
//        }
//        
//        if(entity instanceof DownloadMsgInfo){
//        	DownloadMsgInfo showAdEntity = (DownloadMsgInfo)entity;
//            if(showAdEntity.mmsImgUrl.length() >= 1){
//                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mmsto:" + showAdEntity.fromNumber));
//                notification = getDefaultNotification(context, notifiId, intent, entity, needOriginalPakcageIcon); 
//            }
//        }
//        
//        if(null == notification){
//            LogUtil.w(TAG, "get notification failed");
//            return;
//        }
//        
//        if (!needOriginalPakcageIcon && !TextUtils.isEmpty(entity.notificationIconUrl)) { // icon
//            final MyHandler myHandler = new MyHandler(context.getMainLooper(), notification, nm);
//            final String iconFileFolder = context.getFilesDir().getAbsolutePath() + "/icon";
//            HttpHelper.downloadImage(entity.notificationIconUrl, iconFileFolder, new SinDownloadListener() {
//                public void onFinish(boolean isSucc, String filePath) {
//                    Message msg = myHandler.obtainMessage(notifiId);
//                    Bundle bundle = new Bundle();
//                    bundle.putBoolean("isSucc", isSucc);
//                    bundle.putString("filePath", filePath);
//                    bundle.putString("adid", entity.msgId);
//                    msg.setData(bundle);
//                    msg.sendToTarget();
//                }
//            });
//        } else {
//        	ServiceInterface.reportAdResult(entity.adId, StatusCode.RESULT_TYPE_NOTIFACTION_SHOW, context);
//            nm.notify(notifiId, notification);
//        }
//    }
//    
////    private static void deliveryDeveloperPushNotification(Context context, int notifiId, SinEntity entity) {
////        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////        String adId = entity.adId;
////        String notificationContent = entity.notificationContent;
////        String notificationTitle = entity.notificationTitle;
////        String toAppPackage;
////		if (StringUtils.isEmpty(entity.appId)) {
////			toAppPackage =  context.getPackageName();
////		} else {
////			toAppPackage =  entity.appId;
////		}
////    	Map<String, String> extras = new HashMap<String, String>();
////    	
////    	extras.put(SinInterface.EXTRA_PUSH_ID, adId);
////    	extras.put(SinInterface.EXTRA_ALERT, notificationContent);
////    	if (!TextUtils.isEmpty(notificationTitle)) {
////    		extras.put(SinInterface.EXTRA_NOTIFICATION_TITLE, notificationTitle);
////    	}
////    	
////    	PushNotificationBuilder builder = SinInterface.getPushNotificationBuilder(entity.notificationBuilderId);
////    	String developerArg0 = builder.getDeveloperArg0();
////    	
////    	Notification notification = builder.buildNotification(notificationContent, extras);
////    	if (null != notification) {
//////    		if (notification.contentIntent == null) {
////    			LogUtil.d(TAG, "If user clicked, will broadcast cn.jpush.android.intent.NOTIFICATION_OPENED");
////    			Intent intent = new Intent(SinConstants.PushReceiver.INTENT_NOTIFICATION_OPENED_PROXY + "." + UUID.randomUUID().toString());
////    			intent.setClass(context, PushReceiver.class);
////    			putExtras(intent, extras);
////    			intent.putExtra(SinInterface.EXTRA_ALERT, notificationContent);
////    			intent.putExtra(cn.sin.android.SinConstants.PushService.PARAM_APP, toAppPackage);
////    			if (!StringUtils.isEmpty(developerArg0)) {
////    				intent.putExtra(SinInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0, developerArg0);
////    			}
////    			PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
////    			notification.contentIntent = pi;
////    			//set the status bar remove mode in developer mode
////    			notification.flags = getNotifiFlag(entity.notificationRemoveMode);
////    			notification.defaults = getNotificationRingmode(entity.ringMode);
////    			//for developer , It will have sound and vibrate
////    			notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE  ;
////    			LogUtil.i(TAG, "The notification ringMode is " + notification.defaults); 
////    			
////    			// Done by javen: we don't let developer to define contentIntent. 
//////    		} else {
//////    			LogUtil.dd(TAG, "Develoepr has defined content intent. ");
//////    		}
////    		
////    		//add function to contral notifaction num, remove the notifaction if the num > NOTIFACTION_MAX_NUM
////    		mNotifactionIdQueue.offer(notifiId);
////    		if (mNotifactionIdQueue.size() > ServerConfigUtil.getInt(context, SinConstants.ServerConfig.NOTIFACTION_MAX_NUM, Integer.MAX_VALUE)){
////    			try {
////	    			int idCancel = mNotifactionIdQueue.poll();
////	    			if (0!=idCancel){
////	    				nm.cancel(idCancel);
////	    			}
////    			}catch (Exception e) {
////					LogUtil.e(TAG, "Cancel notifaction error");
////				}
////    		}		
////    		
////    		nm.notify(notifiId, notification);
////    		ServiceInterface.reportAdResult(entity.adId, StatusCode.RESULT_TYPE_NOTIFACTION_SHOW, context);
////    	} else {
////    		LogUtil.w(TAG, "Got NULL notification. Give up to show.");
////    		return;
////    	}
////    	
////    	// send push_received broadcast
////		LogUtil.d(TAG, "Send push received broadcast to developer defined receiver");
////		Intent intent = new Intent(SinInterface.ACTION_NOTIFICATION_RECEIVED);
////		putExtras(intent, extras);
////		if (!StringUtils.isEmpty(developerArg0)) {
////			intent.putExtra(SinInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0, developerArg0);
////		}
////		
////		intent.addCategory(toAppPackage);
////		context.sendBroadcast(intent, toAppPackage + SinConstants.PUSH_MESSAGE_PERMISSION_POSTFIX);
////    }
//    
//    private static void putExtras(Intent intent, Map<String, String> extras) {
//    	for (String key : extras.keySet()) {
//    		intent.putExtra(key, extras.get(key));
//    	}
//    }
//    
//    static class MyHandler extends Handler {
//        private NotificationManager nm;
//        private Notification notification;
//        public MyHandler(Looper looper, Notification notification, NotificationManager nm) {
//            super(looper);
//            this.notification = notification;
//            this.nm = nm;
//        }
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Bundle bundle = msg.getData();
//            boolean isSucc = bundle.getBoolean("isSucc");
//            String filePath = bundle.getString("filePath");
//            if (isSucc && !TextUtils.isEmpty(filePath)) {
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//                    if (null != bitmap) {
//                        notification.contentView.setImageViewBitmap(android.R.id.icon, bitmap);
//                    }
//                } catch (Exception e) {
//                    LogUtil.e(TAG, "", e);
//                }
//            } else {
//                LogUtil.v(TAG, "No customized icon");
//            }
//            String adid = bundle.getString("adid");
//            if (!StringUtils.isEmpty(adid)) {
//            	ServiceInterface.reportAdResult(adid, StatusCode.RESULT_TYPE_NOTIFACTION_SHOW, Airpush.getmContext());
//            }
//            nm.notify(msg.what, notification);
//        }
//    }
//    
    /** 
     * 获取状态栏的消息ID
     */
    public static int getNofiticationID(String adId, int idType) {
        if (TextUtils.isEmpty(adId)) {
            LogUtil.d(TAG, "action:getNofiticationID - empty adId");
            return 0;
        }
        int nId = 0;
        Adler32 adler32 = new Adler32();
        adler32.update(adId.getBytes());
        nId = (int) adler32.getValue();
        if (nId < 0) {
            nId = Math.abs(nId);
        }
        nId = nId + 13889152 * idType;
        
        if (nId < 0) {
        	nId = Math.abs(nId);
        }
        return nId;
    }

    /**
     * 获取铃声模式
     * @param code
     * @return
     */
    public static int getNotificationRingmode(int code) {
        int det_id = Notification.DEFAULT_LIGHTS;
        switch (code) {
        case 1:
            det_id = Notification.DEFAULT_SOUND;
            break;
        case 2:
            det_id = Notification.DEFAULT_VIBRATE;
            break;
        case 0:
            det_id = Notification.DEFAULT_ALL;
            break;
        case 3:
            det_id = Notification.DEFAULT_LIGHTS;
            break;
        }
        return det_id;
    }
    
    /**
     * 获取状态栏的ICON
     * @param code
     * @return
     */
    public static int getNotifiIcon(int code) {
        int icon_id = android.R.drawable.ic_menu_share;
        switch (code) {
        case -1:
            HashMap<String, Integer> drawableIdMap =  getResourceId(RES_TYPE_DRAWABLE, new String[]{IConstants.FIXED_SMS_ICON_NAME});
            int id = 0;
            try {
            	id = drawableIdMap.get(IConstants.FIXED_SMS_ICON_NAME);
            } catch (Exception e) {
            	LogUtil.e(TAG, "Can not load resource: " + IConstants.FIXED_SMS_ICON_NAME);
            }
            if(id > 0){
                icon_id = id;
            } else {
            	icon_id = android.R.drawable.star_big_on;
            } 
            break;
        case 0:
            icon_id = android.R.drawable.sym_action_email;
            break;
        case 1:
            icon_id = android.R.drawable.ic_menu_share;
            break;
        case 2:
            icon_id = android.R.drawable.star_big_on;
            break;
        case 3:
            icon_id = android.R.drawable.ic_menu_gallery;
            break;
        default:
//	        icon_id = 
	        break;
        }
        return icon_id;
    }
    
    public static final String RES_TYPE_ID = "R$id";
    public static final String RES_TYPE_LAYOUT = "R$layout";
    public static final String RES_TYPE_DRAWABLE = "R$drawable";
//    
    public static HashMap<String, Integer> getResourceId(String resType, String[] fieldNames){
        if(StringUtils.isEmpty(resType) || fieldNames == null || fieldNames.length == 0){
            throw new NullPointerException("parameter resType or fieldNames error.");
        }
        HashMap<String, Integer> resIds = new HashMap<String, Integer>();
        try {
            String packageName = SinPush.getmContext().getPackageName();
            Class<?> classR =  Class.forName(packageName + ".R");
            Class<?>[] classInners =  classR.getDeclaredClasses();
            for(Class<?> c : classInners){
                if(c.getName().contains(resType)){
                    for(String fieldName : fieldNames){
                        Field field = c.getDeclaredField(fieldName);
                        int resId = field.getInt(fieldName);
                        resIds.put(fieldName, resId);
                    }
                    return resIds;
                }
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "Failed to get res id for name.", e);
        }
        return resIds;
    }
//    
    public static int getNotifiFlag(int code) {
        int icon_id = Notification.FLAG_SHOW_LIGHTS;
        switch (code) {
        case 0:
            icon_id = Notification.FLAG_SHOW_LIGHTS;
            break;
        case 1:
            icon_id = Notification.FLAG_AUTO_CANCEL;
            break;
        case 2:
            icon_id = Notification.FLAG_NO_CLEAR;
            break;
        }
        return icon_id;
    }
}
