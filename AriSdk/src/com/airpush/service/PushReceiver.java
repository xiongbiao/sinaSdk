package com.airpush.service;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.airpush.android.IConstants;
import com.airpush.android.NotificationHelper;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.data.MsgInfo;
import com.airpush.db.DbHelper;
import com.airpush.util.LogUtil;

 
public class PushReceiver  extends BroadcastReceiver {
	
	public static MsgInfo failedDowndLoadAD = null;// save the failedDownLoadAd
    private final static String TAG = "PushReceiver";
    private final static int MSG_SHOW_APPLIST_VIEW = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        final Context finalContext = context;
        String theAction = intent.getAction();
        LogUtil.d(TAG, "onReceive - " + theAction);
//        if (!SinPush.init(context.getApplicationContext()))
//            return;
//
//        if (theAction == null)
//            return;
//
//        if (theAction.equals(Intent.ACTION_BOOT_COMPLETED)) {
//            ServerConfigUtil.getServerConfig(finalContext);
//            SinDirectoryUtils.deleteADData(context);
//            if(PushService.ismRestartRtc()) {
//	            Intent intent1 = new Intent(context, PushService.class);
//	            Bundle bundle = new Bundle();
//	            bundle.putString("rtc", "rtc");
//	            intent1.putExtras(bundle);
//	            context.startService(intent1);
//            } else {
//            	Logger.i(TAG, "RESTART_RTC is false, can not start");
//            }
//
//        } else
        if (theAction.equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getDataString().replace("package:", "");
            String obj = DbHelper.delUpTableForAppId(context, packageName);
//            ServiceInterface.reportOperation(context, StatusCode.buildPackageAddedReportContent(packageName));

            if (!TextUtils.isEmpty(obj)) {
                String[] objs = obj.split(",");
                String adId = objs[0];
//                ServiceInterface.reportAdResult(adId, StatusCode.REPORT_APP_INSTALL_SUCCEED, context);

                String mainActivity = "";
                if (objs.length >= 2)
                    mainActivity = objs[1];

//                if (!mainActivity.equals(ApkAdEntity.MAIN_ACTIVITY_CONST_NOT_AUTO_RUN)) {
//                    if (!AndroidUtil.startNewAPK(context, packageName, mainActivity)) {
//                        LogUtil.d(TAG, "Failed to install.");
//                        ServiceInterface.reportAdResult(adId, StatusCode.RESULT_TPPE_INVALID_PARAM, context);
//                    }
//                }

                NotificationHelper.cancelAllNotification(context, adId);
            }

        } 
//            else if (theAction.equals(Intent.ACTION_PACKAGE_REMOVED)) {
//            String packageName = intent.getDataString().replace("package:", "");
//            ServiceInterface.reportOperation(context, StatusCode.buildPackageRemovedReportContent(packageName));
//
//        } else if (theAction.equals(Intent.ACTION_USER_PRESENT)) {
//            if (PushService.mStartOnUserPresent && !PushService.INTERNAL_USE) {
//                Intent intent1 = new Intent(context, PushService.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("rtc", "rtc");
//                intent1.putExtras(bundle);
//                context.startService(intent1);
//                return;
//            }
//
//        } 
            else if (theAction.equals(IConstants.PushReceiver.INTENT_NOTIFICATION_INSTALL_CLICKED)) {
            MsgInfo entity = (MsgInfo) intent.getSerializableExtra(IConstants.PARAM_BODY);
            if (null == entity || !entity.isMsgTypeDownloadAndUpdate())
                return;

//            ServiceInterface.reportAdResult(entity.msgId, StatusCode.RESULT_TYPE_NOTIFICATION_INSTALL_CLICKED, context);

            DownloadMsgInfo apkEntity = (DownloadMsgInfo) entity;
            Intent install = new Intent();
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.setAction(android.content.Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(apkEntity._apkSavedPath)), IConstants.APK_MIME);
            context.startActivity(install);

        }
//        	else if (theAction.startsWith(SinConstants.PushReceiver.INTENT_NOTIFICATION_OPENED_PROXY)) {
//            String pushId = intent.getStringExtra(SinInterface.EXTRA_PUSH_ID);
//            if (!StringUtils.isEmpty(pushId)) {
//                ServiceInterface.reportAdResult(pushId, StatusCode.RESULT_TYPE_CLICK, context);
//            }
//            
//            //客户端没有定义ACTION_NOTIFICATION_OPENED 直接打开住界面
//            if (!AndroidUtil.hasReceiverIntentFilter(context, SinInterface.ACTION_NOTIFICATION_OPENED, true)) {
//            	Logger.dd(TAG, "No ACTION_NOTIFICATION_OPENED defined in manifest, open the default main activity");
//            	AndroidUtil.startMainActivity(finalContext);
//            	
//            } else {
//                //向应用发送广播
//	            final Intent opened = new Intent(SinInterface.ACTION_NOTIFICATION_OPENED);
//	            Bundle bundle = intent.getExtras();
//	            opened.putExtras(bundle);
//	           
//	            String toAppPackage = intent.getStringExtra(cn.sin.android.SinConstants.PushService.PARAM_APP);
//	    		if (StringUtils.isEmpty(toAppPackage)) toAppPackage =  context.getPackageName();
//	    		
//	            opened.addCategory(toAppPackage);
//	            Logger.i(TAG, "Send broadcast to app: " + toAppPackage);
//	            context.sendBroadcast(opened, toAppPackage + SinConstants.PUSH_MESSAGE_PERMISSION_POSTFIX);
//            }
//
//        } else if (theAction.equalsIgnoreCase(SinConstants.PushReceiver.SYSTEM_FULLSCREEN_ACTION)) {
//            Set<String> strs = intent.getCategories();
//
//            if (strs == null || strs.size() == 0) {
//                Logger.e(TAG, "getCategories fail !!");
//                return;
//            }
//
//            for (String str : strs) {
//                Logger.i(TAG, "Categories:" + str);
//                if (str.equals(SinConstants.PushReceiver.SYSTEM_FULLSCREEN_CATEGORY)) {
//                    final MsgInfo adEntity = (MsgInfo) intent.getSerializableExtra(SinConstants.PARAM_BODY);
//
//                    if (adEntity == null) {
//                        Logger.e(TAG, "get AdEntity fail !!");
//                        return;
//                    }
//                    
//                    if(adEntity.notificationRemoveMode == MsgInfo.REMOVE_MODE_MUST_CLICK){
//                        adEntity.notificationRemoveMode = MsgInfo.REMOVE_MODE_AUTO;
//                        adEntity.ringMode = MsgInfo.RING_MODE_SLEEP_DEFAULT;
//                        NotificationHelper.showNotification(finalContext, adEntity);
//                    }
//                    
//                    ArrayList<String> bgFilePaths = adEntity._systemViewImageFilePathList;
//
//                    if (bgFilePaths == null || bgFilePaths.size() == 0) {
//                        Logger.e(TAG, "bgFilePaths have no data !!");
//                        return;
//                    }
//
//                    boolean isDownload = false;
//                    if (adEntity.adType == MsgInfo.AD_TYPE_APK || adEntity.adType == MsgInfo.AD_TYPE_VIDEO) {
//                        isDownload = true;
//                    }
//
//                    try {
//                        SystemFullScreenView systemFullScreenView = new SystemFullScreenView(context, bgFilePaths,
//                                adEntity.allowCancel, isDownload);
//                        systemFullScreenView.setOnSystemViewClickListener(new OnSystemViewClickListener() {
//                            @Override
//                            public void onItemClick(DialogInterface v, int position, boolean isDownload) {
//                                if (!isDownload) {
//                                    Logger.i(TAG, "启动activity");
//                                    Intent intent = AndroidUtil.getIntentForStartPushActivity(finalContext, adEntity,
//                                            false);
//                                    finalContext.startActivity(intent);
//                                    v.dismiss();
//                                }
//                            }
//
//                            @Override
//                            public void onCloseButtonClick(View v, int position) {
//                                NotificationHelper.cancelNotification(finalContext, adEntity.adId,
//                                        NotificationHelper.TYPE_AD_SHOW);
//                            }
//
//                            @Override
//                            public void onBottomButtonClick(View v, int position) {
//                                ServiceInterface.executeDownload(finalContext, adEntity);
//                                NotificationHelper.cancelNotification(finalContext, adEntity.adId,
//                                        NotificationHelper.TYPE_AD_SHOW);
//                            }
//                        });
//                        systemFullScreenView.show();
//                    } catch (Exception e) {
//                        Logger.e(TAG, "init system view failed！", e);
//                    }
//
//                    break;
//                }
//            }
//        } else if (theAction.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)) {
//            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            if (null == info)
//                return;
//
//            Logger.v(TAG, "Connection state changed to - " + info.toString());
//            boolean disConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//            if (disConnected) {
//                Logger.v(TAG, "No any network is connected");
//                DownloadControl.isNetworkAvailable = false;
//
//            } else {
//                if (NetworkInfo.State.CONNECTED == info.getState()) {
//                	
//                	ServiceInterface.sendUdpPackageFromDB(context);//上报缓存在数据库里面的UDP包
//                	
//                    Logger.v(TAG, "Network is connected.");
//                    DownloadControl.isNetworkAvailable = true;
//                    if (DownloadService.hasDownladTask()) {
//                        DownloadService.startDownloadTasks(context);
//                    }
//                    ServiceInterface.reStartRtc(finalContext);
//                    //下载APPLIST 
//                    if (failedDowndLoadAD != null) {
//                    	NotificationHelper.showN(finalContext, failedDowndLoadAD);
//                    }
//
//                } else if (NetworkInfo.State.DISCONNECTED == info.getState()) {
//                    Logger.v(TAG, "Network is disconnected.");
//                    DownloadControl.isNetworkAvailable = false;
//
//                } else {
//                    Logger.v(TAG, "other network state - " + info.getState() + ". Do nothing.");
//                }
//            }
//
//        } else if(theAction.equalsIgnoreCase(SinConstants.PushReceiver.SHOW_FLOAT_VIEW_ACTION)){
//            final MsgInfo adEntity = (MsgInfo)intent.getSerializableExtra(SinConstants.PARAM_BODY);
//            if(null == adEntity){
//                Logger.e(TAG, "The entity is null");
//                return;
//            }
//            if (adEntity.newShowType == MsgInfo.NEW_SHOW_TYPE_FLOAT_VIEW_APPLIST) {
//                new AppListHelp(context, adEntity) {
//					@Override
//					public void onDownloadSuccess(String fileName, MsgInfo entity, boolean isNewDownload) {
//						failedDowndLoadAD = null;
//						if (isNewDownload) {//不是最新的下载    需要解压文件插入数据库
//							boolean flag = this.DownloadSuccess(fileName);
//							if (!flag) {
//								Logger.e(TAG, "zipFile or insertDB failed, give up");
//								return;
//							}
//						}
//						List<MsgInfo> ads = getAds();
//						if (ads.size() == 0){
//							Logger.e(TAG, "The App-list do not have any AD, give up");
//							return;
//						} else {
//							adEntity.ads = ads;
//							Message msg = new Message();
//							msg.what = MSG_SHOW_APPLIST_VIEW;
//							msg.obj = adEntity;
//							mHandler.sendMessage(msg);
//						}
//					}
//					@Override
//					public void onDownloadFailed(String fileName, MsgInfo entity) {
//						 Logger.e(TAG, "Download failed" + fileName + "  give up this float view...");
//						 failedDowndLoadAD = entity;
//						 ServiceInterface.reportAdResult(adEntity.adId, StatusCode.RESULT_TYPE_DOWN_FAIL, finalContext);
//						 
//						 //report failed download info ,since 1.2.4
//						  String apkurl =  "";
//		                    try {
//			                    ApkAdEntity apk  = (ApkAdEntity)adEntity;
//			                    apkurl = apk.apkDownloadUrl;
//		                    } catch (Exception e) {
//								
//							}
//		                    ServiceInterface.reportAdActionResult(adEntity.adId, StatusCode.RESULT_TYPE_APK_LOAD_FAIL, AndroidUtil.getDownloadFailedClientInfo(finalContext, apkurl), finalContext);
//						 return;
//					}
//				};
//            } else if (adEntity.newShowType == MsgInfo.NEW_SHOW_TYPE_FLOAT_VIEW_GIFAD){
//            	showADGifView(context, adEntity);
//            } else if (adEntity.newShowType == MsgInfo.NEW_SHOW_TYPE_FLOAT_VIEW_PHYSICS_COLLISIO) {
//            	if (null == FloatViewHelper.myFloatView) FloatViewHelper.initFloatView(context);
//            	
//            	try {
//            		FloatViewHelper.myFloatView.setNewAd();
//            		FloatViewHelper.myFloatView.stopThread();
//            		FloatViewHelper.myFloatView.setFloatViewListener(new FloatViewListener() {
//						
//						@Override
//						public void onPress() {
//							// TODO Auto-generated method stub
//							FloatViewHelper.myFloatView.startThread();
//						}
//					});
//				
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }else {
//            	showFloatView(context, adEntity);
//            }
//        }
        
    }
}
