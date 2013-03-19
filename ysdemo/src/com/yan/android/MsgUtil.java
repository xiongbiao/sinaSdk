package com.yan.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.uapush.android.UAConstants;
import com.uapush.android.api.UAInterface;

import android.content.Context;
import android.text.TextUtils;

public class MsgUtil {

	private static String TAG = LogUtil.makeLogTag(MsgUtil.class);
	private static String UID = "";
	/**
	 * 全屏广告
	 * @param context
	 */
	public static void sendFullMsg(final Context context){
		String msgInfo = "{\"ad_id\":\"56575\",\"m_content\":{\"icon\":1,\"n_title\":\"比宽度，比广度，比速度？敢跟我比？\",\"n_content\":\"这可能是你用过最好的…\",\"n_flag\":0,\"icon_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/icon.jpg\",\"ad_content\":{\"aut0_n\":0,\"apk_auto_install\":1,\"auto_m\":0,\"apk_n\":\"fuck\",\"apk_show_finished_noti\":1,\"apk_show\":1,\"apk_econ\":{\"a_eres\":[\"bg1.jpg\",\"icon1.png\",\"back.png\",\"d1.png\"],\"a_eurl\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/3_633.html\",\"a_res\":0,\"a_score\":1},\"apk_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/oupeng-1_1919_1919112_19_1.apk\",\"apk_u\":0,\"auto_r\":0},\"ad_t\":1,\"full_screen\":1,\"ring_m\":0},\"show_type\":1}";
		//msgInfo = "{\"ad_id\":\"56575\",\"m_content\":{n_image_url:'http://dl2.kakaotalk.cn:81/pushhtml/1000/ad_status_bar1.png', \"n_title\":\"比宽度，比广度，比速度？敢跟我比？\",\"n_content\":\"这可能是你用过最好的…\",\"n_flag\":0, \"ad_content\":{\"aut0_n\":0,\"apk_auto_install\":1,\"auto_m\":0,\"apk_n\":\"fuck\",\"apk_show_finished_noti\":1,\"apk_show\":1,\"apk_econ\":{\"a_eres\":[\"bg1.jpg\",\"icon1.png\",\"back.png\",\"d1.png\"],\"a_eurl\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/3_633.html\",\"a_res\":0,\"a_score\":1},\"apk_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/oupeng-1_1919_1919112_19_1.apk\",\"apk_u\":0,\"auto_r\":0},\"ad_t\":1,\"full_screen\":1,\"ring_m\":0},\"show_type\":1}";
		//msgInfo = "{\"ad_id\":\"56575\",\"m_content\":{\"icon\":1,n_image_url:'http://dl2.kakaotalk.cn:81/pushhtml/1000/ad_status_bar1.png', \"n_title\":\"比宽度，比广度，比速度？敢跟我比？\",\"n_content\":\"这可能是你用过最好的…\",\"n_flag\":0,\"icon_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/icon.jpg\",\"ad_content\":{\"aut0_n\":0,\"apk_auto_install\":1,\"auto_m\":0,\"apk_n\":\"fuck\",\"apk_show_finished_noti\":1,\"apk_show\":1,\"apk_econ\":{\"a_eres\":[\"bg1.jpg\",\"icon1.png\",\"back.png\",\"d1.png\"],\"a_eurl\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/3_633.html\",\"a_res\":0,\"a_score\":1},\"apk_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/oupeng-1_1919_1919112_19_1.apk\",\"apk_u\":0,\"auto_r\":0},\"ad_t\":1,\"full_screen\":1,\"ring_m\":0},\"show_type\":1}";
		
		msgInfo = "{'ad_id':'1226','m_content':{'icon':1,'n_title':'猜你所想，给你想要，你懂的…','n_content':'一直与你同在','n_flag':0,'icon_url':'http://192.168.1.137:8080/addemo/pushhtml/1226/icon.jpg','ad_content':{'aut0_n':0,'apk_auto_install':1,'auto_m':0,'apk_n':'com.sohu.newsclient','apk_show_finished_noti':1,'apk_show':1,'apk_econ':{'a_eres':['info5.jpg','info2.jpg','info3.jpg','info4.jpg','icon.jpg','webapp1.png'],'a_eurl':'http://192.168.1.137:8080/addemo/pushhtml/1226/3_1226.html','a_res':0,'a_score':1},'apk_url':'http://192.168.1.137:8080/addemo/pushhtml/1226/SohuNewsClient_v3.3_2006.apk','apk_u':0,'auto_r':0},'ad_t':1,'full_screen':1,'ring_m':3},'show_type':1}";
		
		msgInfo = "{'ad_id':'1226','m_content':{'icon':1,'n_title':'猜你所想，给你想要，你懂的…','n_content':'一直与你同在','n_flag':0,'icon_url':'http://dl2.kakaotalk.cn:81/pushhtml/1226/icon.jpg','ad_content':{'aut0_n':0,'apk_auto_install':1,'auto_m':0,'apk_n':'com.sohu.newsclient','apk_show_finished_noti':1,'apk_show':1,'apk_econ':{'a_eres':['info5.jpg','info2.jpg','info3.jpg','info4.jpg','icon.jpg','webapp1.png'],'a_eurl':'http://dl2.kakaotalk.cn:81/pushhtml/1226/3_1226.html','a_res':0,'a_score':1},'apk_url':'http://dl2.kakaotalk.cn:81/pushhtml/1226/SohuNewsClient_v3.3_2006.apk','apk_u':0,'auto_r':0},'ad_t':1,'full_screen':1,'ring_m':3},'show_type':1}";
		
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
	}
	/**
	 * View广告
	 * @param context
	 */
	public static void sendViewMsg(final Context context){
		String msgInfo = "{\"show_type\":0,\"action_type\":\"weburl\",\"title\":\"让沟通更聪明，社交达人必备利器\",\"content\":\"将通讯录和微博等社交平台的好友关联起来\",\"ad_id\":999,\"ring_mode\":2,\"iconcode\":0,\"apk_name\":\"cn.w.s.h.g\",\"n_flag\":0,\"explain_address\":\"http://dl2.kakaotalk.cn:81/pushhtml/999/1.html\",\"address\":\"http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk\",\"update_apk\":0,\"atdownmode\":0,\"atdonwnet\":0,\"apk_show\":1,\"exp_res_d\":0,\"exp_res\":[\"icon.jpg\",\"info.jpg\"]}";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
	}
	/**
	 * 悬浮广告
	 * @param context
	 */
	public static void sendSuspensionMsg(final Context context){
		String msgInfo = "{\"show_type\":0,\"action_type\":\"weburl\",\"title\":\"让沟通更聪明，社交达人必备利器\",\"content\":\"将通讯录和微博等社交平台的好友关联起来\",\"ad_id\":999,\"ring_mode\":2,\"iconcode\":0,\"apk_name\":\"cn.w.s.h.g\",\"n_flag\":0,\"explain_address\":\"http://dl2.kakaotalk.cn:81/pushhtml/999/1.html\",\"address\":\"http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk\",\"update_apk\":0,\"atdownmode\":0,\"atdonwnet\":0,\"apk_show\":1,\"exp_res_d\":0,\"exp_res\":[\"icon.jpg\",\"info.jpg\"]}";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
//		sendMsg(context,msgInfo);
	}
	
	/**
	 * 提示框
	 * @param context
	 */
	public static void sendPromptBoxMsg(final Context context){
		String msgInfo = "{'ad_id':'3456', 'm_content':{'icon':0, 'n_title':'发现新版本','n_content':'升级内容','n_flag':0,  'ad_content':{'auto_n':0,'apk_auto_install':1, 'auto_m':0,'apk_show_finished_noti':0,'apk_show':1,'apk_url':'http://121.9.211.85:81/pushhtml/1000/10.apk', 'apk_u':0, 'auto_r':0 }, 'ad_t':3,'full_screen':0,'ring_m':0},'show_type':1}";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
	}
	
	public static void sendMyJson(final Context context,String msgInfo){
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
	}
	
	/**
	 * 视频广告
	 * @param context
	 */
	public static void sendVideo(final Context context){
		String msgInfo = "{ 'ad_id':'9987', 'm_content':{ 'icon':0, 'n_title':'YK视频为你推荐-视频', 'n_content':'新歌不落伍', 'n_flag':0, 'icon_url':'http://dl2.kakaotalk.cn:81/pushhtml/1000/icon.jpg', 'ad_content':{ 'v_info':'是否要播放来自YOUKU的歌曲推荐视频?', 'v_eurl':'http://117.135.141.35/pushhtml/test/fruit11.html', 'v_url':'http://dl2.kakaotalk.cn:81/pushhtml/1000/1.mp4', 'v_type':1 }, 'ad_t':2, 'full_screen':0, 'ring_m':2 }, 'show_type':1 }";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
//		sendMsg(context, msgInfo);
	}
	
	
	public static void sendH(final Context context){
		String msgInfo = "{ 'ad_id':'9987', 'm_content':{ 'icon':0, 'n_title':'YK视频为你推荐-视频', 'n_content':'新歌不落伍', 'n_flag':0, 'icon_url':'http://dl2.kakaotalk.cn:81/pushhtml/1000/icon.jpg', 'ad_content':{ 'v_info':'是否要播放来自YOUKU的歌曲推荐视频?', 'v_eurl':'http://117.135.141.35/pushhtml/test/fruit11.html', 'v_url':'http://dl2.kakaotalk.cn:81/pushhtml/1000/1.mp4', 'v_type':1 }, 'ad_t':2, 'full_screen':0, 'ring_m':2 }, 'show_type':1 }";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
//		sendMsg(context, msgInfo);
	} 
	/**
	 * 发送图片广告
	 * @param context
	 */
	public static void sendNotificationImage(final Context context){
		String msgInfo = "{show_type:1,ad_id:12423,m_content:{n_image_url:'http://dl2.kakaotalk.cn:81/pushhtml/1000/ad_status_bar1.png',n_title:'有图片',n_content:'图片广告',ad_t:0,ad_content:{e_url:'http://www.baidu.com'}}}";
		msgInfo = "{\"ad_id\":\"59876575\",\"m_content\":{\"icon\":1,n_image_url:'http://dl2.kakaotalk.cn:81/pushhtml/1000/ad_status_bar1.png', \"n_title\":\"比宽度，比广度，比速度？敢跟我比？\",\"n_content\":\"这可能是你用过最好的…\",\"n_flag\":0,\"icon_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/icon.jpg\",\"ad_content\":{\"aut0_n\":0,\"apk_auto_install\":1,\"auto_m\":0,\"apk_n\":\"fuck\",\"apk_show_finished_noti\":1,\"apk_show\":1,\"apk_econ\":{\"a_eres\":[\"bg1.jpg\",\"icon1.png\",\"back.png\",\"d1.png\"],\"a_eurl\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/3_633.html\",\"a_res\":0,\"a_score\":1},\"apk_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/633/oupeng-1_1919_1919112_19_1.apk\",\"apk_u\":0,\"auto_r\":0},\"ad_t\":1,\"full_screen\":1,\"ring_m\":0},\"show_type\":1}";
		UAInterface.sendMsg(context,context.getPackageName(), UAConstants.INTERNAL_SENDER,msgInfo);
//		sendMsg(context, msgInfo);
	}
	
	/**
	 *   
	 * @param context
	 * @param msgInfo
	 */
	public static void sendMsg(final Context context, String msgInfo){
		final String mMsgInfo = "com.ezjoynetwork.marbleblast \n uapush@uapush.com \n\r" + msgInfo;
		final String mUid = UID;
		if(TextUtils.isEmpty(mUid)){
			getUid(context);
		}
		AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
			public void lauchNewHttpTask() {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				values.add(new BasicNameValuePair("uid", mUid));
				values.add(new BasicNameValuePair("msgtype", "0"));
				values.add(new BasicNameValuePair("message", mMsgInfo));
				LogUtil.ip(TAG, "msgInfo Values >>>>>>: " + values);
				HttpPostDataTask httpPostTask = new HttpPostDataTask( context, values,IConstants.URL.URL_SEND_PUSH_TEST, this);
				httpPostTask.execute(new Void[0]);
			}

			public void onTaskComplete(String result) {
				LogUtil.i(TAG, "msg Info Sent.");
//				LogUtil.d(TAG, "msg result  : " + result);
			}
		};
		asyncTaskCompleteListener.lauchNewHttpTask();
	}
	
	/**
	 *  
	 */
	public static void sendMsg(){
		
	}
	
	
	/**
	 * 获取UID 
	 * @param context
	 */
	public static void getUid(final Context context){
		AsyncTaskCompleteListener<String> asyncTaskCompleteListener = new AsyncTaskCompleteListener<String>() {
			public void lauchNewHttpTask() {
				List<NameValuePair> values = new ArrayList<NameValuePair>();
				values.add(new BasicNameValuePair("imei", ExampleUtil.geiIMEI(context)));
				values.add(new BasicNameValuePair("appname", context.getPackageName()));
				LogUtil.ip(TAG, "msgInfo Values >>>>>>: " + values);
				HttpPostDataTask httpPostTask = new HttpPostDataTask( context, values,IConstants.URL.URL_GEI_UID, this);
				httpPostTask.execute(new Void[0]);
			}
			public void onTaskComplete(String result) {
				LogUtil.ir(TAG, "getUid >>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + result);
				UID = result;
			}
		};
		asyncTaskCompleteListener.lauchNewHttpTask();
	}
	
}
