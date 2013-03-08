package com.kk.msg;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class NotificationUtil {

	private Context mContext;
	
	public NotificationUtil(){}
	
	public NotificationUtil(Context context){
		mContext = context;
	}

	//自定义显示的通知 ，创建RemoteView对象
	public void showCustomizeNotification() {

		CharSequence title = "i am new";
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		Notification noti = new Notification(icon, title, when + 10000);
		noti.flags = Notification.FLAG_INSISTENT;
		
		// 1、创建一个自定义的消息布局 view.xml
		// 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
		RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),R.layout.notification);
		remoteView.setImageViewResource(R.id.image, R.drawable.ic_launcher);
		remoteView.setTextViewText(R.id.text , "通知类型为：自定义View");
		noti.contentView = remoteView;
		// 3、为Notification的contentIntent字段定义一个Intent(注意，使用自定义View不需要setLatestEventInfo()方法)
        Log.v("MSG","defaults : "+ noti.defaults);
		//这儿点击后简单启动Settings模块
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,new Intent("android.settings.SETTINGS"), 0);
		noti.contentIntent = contentIntent;
	
		NotificationManager mnotiManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mnotiManager.notify(0, noti);

	}

	// 默认显示的的Notification
	public void showDefaultNotification() {
	    // 定义Notication的各种属性
	     CharSequence title = "i am new";
//	    int icon = R.drawable.ic_launcher;
//	    long when = System.currentTimeMillis();
//	    Notification noti = new Notification(icon, title, when + 10000);
//	    noti.flags = Notification.FLAG_INSISTENT;

	    // 创建一个通知
	    Notification mNotification = new Notification();

	    // 设置属性值
	    mNotification.icon = R.drawable.ic_launcher;
	    mNotification.tickerText = "NotificationTest";
	    mNotification.when = System.currentTimeMillis(); // 立即发生此通知

	    // 带参数的构造函数,属性值如上
	    // Notification mNotification = = new Notification(R.drawable.icon,"NotificationTest", System.currentTimeMillis()));

	    // 添加声音效果
	    mNotification.defaults |= Notification.DEFAULT_SOUND;

	    // 添加震动,后来得知需要添加震动权限 : Virbate Permission
	    mNotification.defaults |= Notification.DEFAULT_VIBRATE ; 

	    //添加状态标志 
	    //FLAG_AUTO_CANCEL          该通知能被状态栏的清除按钮给清除掉
	    //FLAG_NO_CLEAR                 该通知能被状态栏的清除按钮给清除掉
	    //FLAG_ONGOING_EVENT      通知放置在正在运行
	    //FLAG_INSISTENT                通知的音乐效果一直播放
	    mNotification.flags = Notification.FLAG_AUTO_CANCEL ;

	    //将该通知显示为默认View
	    PendingIntent contentIntent = PendingIntent.getActivity (mContext, 0,new Intent("android.settings.SETTINGS"), 0);
	    mNotification.setLatestEventInfo(mContext, "通知类型：默认View", "一般般哟。。。。",contentIntent);
	    
	    // 设置setLatestEventInfo方法,如果不设置会App报错异常
	    NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    //注册此通知 
	    // 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
	    mNotificationManager.notify(2, mNotification);

	}
	
    private void removeNotification()
	{
	    NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	    // 取消的只是当前Context的Notification
	    mNotificationManager.cancel(2);
	}
	
}
