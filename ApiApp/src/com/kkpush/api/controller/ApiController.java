package com.kkpush.api.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkpush.api.domain.MsgInfo;
import com.kkpush.api.service.msgpool.DownloadMsg;
import com.kkpush.user.service.UserService;
import com.kkpush.util.StrUtil;
import com.kkpush.web.controller.PublicController;
 
@Controller
@RequestMapping("/v2")
public class ApiController extends PublicController{
	private static final Log logger = LogFactory.getLog(ApiController.class);
	@Autowired
	UserService userService;
	
//	@Autowired
//	private DownloadMsg callBackMapper;
	
	private final static String VALIDATE_CONTENT = "Welcome xbSDK";
	
	@RequestMapping(value = "/api", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> api(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> requestParams = requestToMap(request);
		String mStr = getMapToString(requestParams);
		System.out.println("params string -------->>>>"+mStr);
		
		String action = requestParams.get("action")+"";
		String model = requestParams.get("model")+"";
		
		System.out.println("model-->>>"+model + "---action----->>>" +action );

		Map<String, Object> responseMap = new HashMap<String, Object>();
    	  
		if(requestParams != null){
			 if(!StrUtil.isEmpty(model)){
	             if("user".equals(model)){
	            	 if("reg".equals(action)){
	            		 try {
							
//		            		 UserInfo user = (UserInfo)BeanToMapUti.convertMap(UserInfo.class, requestParams);
//		            		 System.out.println("appid :  ------------->>> "+user.getAppId());
//		            		 userService.insertUser(user);

		            		 responseMap.put("uid", "8899900");
		            		 responseMap.put("saveUser", true);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
	            		 return responseMap;
	            	 }else if("login".equals(action)){
	            		 //验证
	            		 try {
	            			 String appId = requestParams.get("appId")+"";
	            			 String imei = requestParams.get("imei")+"";
	            			 String packageName = requestParams.get("packageName")+"";
	            			 String rtoken = requestParams.get("token")+"";
		            		 String token = imei + appId + packageName;
		            		   System.out.println("--------token--begin------>>>>>>>>>>>"+token);
		            		 MessageDigest mdEnc2 = MessageDigest.getInstance("MD5");
			         		 mdEnc2.update(token.getBytes(), 0, token.length());
			         		 token = new BigInteger(1, mdEnc2.digest()).toString(16);
			                    System.out.println("--------token-------->>>>>>>>>>>"+token);
			                    System.out.println("--------rtoken-------->>>>>>>>>>>"+rtoken);
			         		 if(rtoken.equals(token)){
			         			 responseMap.put("uid", "8899900");
			         			 responseMap.put("saveUser", false);
			         		 }else{
			         			 responseMap.put("uid", "111100");
			         			 responseMap.put("saveUser", true);
			         		 }
		         		 
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		
	            		 return responseMap;
	            	 }
	              }else if("message".equals(model)){
                       String type = "0";
	                   getMsg(responseMap,type);  
	                   JSONObject json = new JSONObject();
	                    json.putAll(getMSG());
	                    System.out.println("---------------->>>>>>>>>>>"+json.toString());
	                    
							return getMSG();
					
	              }
			 }
		}
		
		return null;
	}
	
	private Map<String, Object>   getMSG(){
		MsgInfo ms = new MsgInfo();
		ms.setMsgId("231");
		ms.setMsgType(1);
		ms.setExpirytime(80);
		ms.setNextmessagecheck(120);
		ms.setNotificationIconId(12);
		ms.setNotificationIconUrl("http://www.airpush.com/images/adsthumbnail/48.png");
		ms.setNotificationText("通知内容");
		ms.setNotificationTitle("通知标题");
		ms.setShowType(0);
		ms.setWebUrl("http://192.168.0.104:8080/login.jsp");
		ms.setDownloadUrl("http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk");
		DownloadMsg dm = new DownloadMsg(ms);
		return dm.getMap();
	}
	

	private void getMsg(Map<String, Object> responseMap,String type) {
		if("w".equals(type)){
			  responseMap.put("adimage", "http://www.airpush.com/images/adsthumbnail/48.png");
	    	  responseMap.put("title", "应用列表");
	    	  responseMap.put("text", "获取应用列表");
	    	  responseMap.put("textColor", "#FCDFFF");
	    	  responseMap.put("number", "");
	    	  responseMap.put("sms", "");
	    	  responseMap.put("countrycode", "");
	    	  responseMap.put("creativeid", "32132");
	    	  responseMap.put("adtype", "W");
	    	  responseMap.put("current_time", "2013-01-11 09:04:33");
	    	  responseMap.put("delivery_time", "2013-01-11 09:04:33");
	    	  responseMap.put("nextmessagecheck", 120);
	    	  responseMap.put("expirytime", 1);
	    	  responseMap.put("superad", 1);
		  }else if("BPW".equals(type)){
			  responseMap.put("title", "更多就爱");
			  responseMap.put("url", "http://api.airpush.com/redirect.php?market=http://partners.handango.com/smartphone/home.jsp?siteId=2897&apcd=eyJhcHBpZCI6Ijk1ODczIiwid2lmaSI6IjEiLCJ2ZXJzaW9uIjoiNS4wIiwicHVzaF9ndWlkIjoiMWNmNDE2YzhlZjAxNTQ5M2Y2MjU3NTM4OWFmNTY0MWIiLCJjb3VudHJ5Ijo0MSwiY2FycmllciI6NTI3MSwiZGV2aWNlIjoiOTgxMyIsInBob25lTW9kZWwiOjEwMDQ0MSwibWFudWZhY3R1cmVyIjozMjg5OCwiY2l0eSI6MCwic3RhdGUiOjEwMDIxMCwiaW1laSI6ImE3ZTFjYzk1NDIwZGRlMDk5MThiYmUwMmNhZTQzODI2In0=");
			  responseMap.put("number", "");
			  responseMap.put("sms", "");
			  responseMap.put("countrycode", "");
			  responseMap.put("creativeid", "155597");
			  responseMap.put("campaignid", "61035");
			  responseMap.put("expirytime", 86399);
			  responseMap.put("adtype", "BPW");
			  responseMap.put("ip1", "http://api1.airpush.com");
			  responseMap.put("ip2", "http://api2.airpush.com");
			  responseMap.put("adimage", "http://s3.amazonaws.com/creative-adtype-images/155597-320.png");
			  responseMap.put("text", "更多就爱");
			  responseMap.put("current_time", "2013-01-11 09:04:33");
	    	  responseMap.put("delivery_time", "2013-01-11 09:04:33");
			  responseMap.put("nextmessagecheck", 0);
			  responseMap.put("header", "Browser");
			  
			  
		  }else if("0".equals(type)){
			  responseMap.put("m_id", "9999");
			  responseMap.put("m_t", 1);
			  responseMap.put(KEY_NOTI_ICON_INTERNAL_ID, "1");
			  responseMap.put(KEY_NOTI_ICON_URL, "http://s3.amazonaws.com/creative-adtype-images/155597-320.png");
			  responseMap.put(KEY_NOTI_TITLE, "通知栏的标题");
			  responseMap.put(KEY_NOTI_CONTENT, "通知的信息");
			  responseMap.put(KEY_NOTI_REMOVE_MODE, "61035");
			  responseMap.put("nextmessagecheck", 120);
			  Map<String, Object> m_content  = new HashMap<String, Object>();
			  responseMap.put("expirytime", 80);
			  m_content.put(KEY_WEB_URL, "http://192.168.0.104:8080/test.html");
			  m_content.put(KEY_DOWNLOAD_URL, "http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk");
			  responseMap.put(KEY_M_CONTENT, m_content);
			  
		  }
		
		
	}
	
	public static final String KEY_DOWNLOAD_URL = "d_url";
	public static final String KEY_M_CONTENT = "m_c";
	public static final String KEY_WEB_URL = "w_url";
	public static final String KEY_NOTI_ICON_INTERNAL_ID = "n_i";
	public static final String KEY_NOTI_ICON_URL = "n_iocn_url";
	public static final String KEY_NOTI_TITLE = "n_t";
	public static final String KEY_NOTI_CONTENT = "n_c";
	public static final String KEY_NOTI_REMOVE_MODE = "n_f";
	 

	@RequestMapping(value = "/api_music", method = RequestMethod.POST)
	public @ResponseBody String apiMusic(HttpServletRequest request,HttpServletResponse response){
		JSONArray ja =new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("id", 50033);
		jo.put("name", "熊彪");
		jo.put("url", "http://a.138z.com/wuer199390/hongchengkezhang.mp3");
		jo.put("image", "http://b.hiphotos.baidu.com/ting/pic/item/d043ad4bd11373f0cf95fb5aa40f4bfbfaed0448.jpg");
		jo.put("rating", 1.2);
		jo.put("artist_name", "周星驰");
		ja.add(jo);
		
		 jo = new JSONObject();
		jo.put("id", 50023);
		jo.put("name", "想你的夜");
		jo.put("url", "http://nonie.1ting.com:9092/zzzzzmp3/2009gjuly/23/23_guanzhe/02.wma");
		jo.put("image", "http://www.xingkuad.com/upfile/news/09101624030.jpg");
		jo.put("rating", 1.2);
		jo.put("artist_name", "关喆");
		ja.add(jo);
		String s = "" ; 
		try {
			s = new String (ja.toString().getBytes(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
		
	}
}
