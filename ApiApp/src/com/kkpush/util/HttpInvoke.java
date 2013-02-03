package com.kkpush.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by JiaCheng User: smallbeetle Date: 2011-3-21 Time: 15:47:22 To
 * change this template use File | Settings | File Templates.
 */
public class HttpInvoke {
	private static Logger logger = LoggerFactory.getLogger(HttpInvoke.class);

	public static String[] invokeDoPost(String url, List<NameValuePair> qparams, Header... header) {
		String[] result = new String[] { "0", null };
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			if (header != null && header.length > 0)
				post.setHeader(header[0]);
			post.setEntity(new UrlEncodedFormEntity(qparams, "UTF-8"));
			HttpResponse response2 = client.execute(post);

			int statusCode = response2.getStatusLine().getStatusCode();
			result[0] = String.valueOf(statusCode);
			logger.info("URL:" + url + "   statusCode:" + statusCode);
			if (statusCode == 200) {
				HttpEntity entity2 = response2.getEntity();
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(entity2.getContent()));
				String buffer2;
				StringBuffer buffer = new StringBuffer();
				while ((buffer2 = reader2.readLine()) != null) {
					buffer.append(buffer2);
				}
				logger.debug(buffer.toString());
				result[1] = buffer.toString();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return result;
	}
	public static String[] invokeDoGet(String url) {
		String[] result = new String[] { "0", null };
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			result[0] = String.valueOf(statusCode);
			logger.info("URL:" + url + "   statusCode:" + statusCode);
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String buffer;
			while ((buffer = reader.readLine()) != null) {
				result[1] = buffer;
				logger.info(buffer);
			}
			get.abort();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			
//			File fDir = new File("/2");
//			String[] fNames = fDir.list();
//			
//			for(int i=0;i<fNames.length;i++){
//				File   f=new   File( "/2/"+fNames[i]);
//				long   time=f.lastModified();
//				System.out.println(time);
//				Calendar   cal=Calendar.getInstance();
//				cal.setTimeInMillis(time);
//				 long nowTime = System.currentTimeMillis();
//				 System.out.println((nowTime-time)>86400000);
//				System.out.println(cal.getTime().toLocaleString()); 
//				
//			}
//			
//			File   f=new   File( "/123.txt");
//			long   time=f.lastModified();
//			System.out.println(1000*3600*24);
//			Calendar   cal=Calendar.getInstance();
//			cal.setTimeInMillis(time);
//			System.out.println(cal.getTime().toLocaleString()); 
			
//			String token=TokenUtil.generateToken("admin","admin!@#");
//			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
//			qparams.add(new BasicNameValuePair("jid", "admin@kk6.cc"));
//			invokeDoPost("http://froyo.kkliaotian.com/newuser", qparams);
			
//			String appKey = "admincom.uapush.suppor34FG10" ;
//			System.err.println( MD5.MD5Encode(appKey));
//			appKey = MD5.MD5Encode(appKey).substring(2, 26);
//			System.err.println(appKey);
//			System.err.println(appKey.length());
			
			// when I want you when
//			//633 广告
//			JSONObject jo=new JSONObject();
//			jo.put("show_type", 0);
//			jo.put("action_type", "weburl");
//			jo.put("address", "http://dl2.kakaotalk.cn:81/pushhtml/771/maikujishi.apk");
//			jo.put("title","麦库-永不丢失的记事本");
//			jo.put("content", "随时随地记录，永久云存储");
//			jo.put("ad_id", 771);
//			jo.put("apk_name", "com.snda.inote");
//			
//			jo.put("update_apk", 0);
//			jo.put("ring_mode", 3);
//			jo.put("atdownmode", 0);
//			jo.put("atdownnet", 0);
//			jo.put("iconcode", 2);
//			jo.put("apk_show", 0);
//			JSONObject joo=new JSONObject();
//			joo.put("a_title", "麦库记事");
//			joo.put("a_icon_url", "http://dl2.kakaotalk.cn:81/pushhtml/771/icon.jpg");
//			joo.put("a_ver", "1.5.0");
//			joo.put("a_type", "实用工具");
//			joo.put("a_size", "1.57 MB");
//			joo.put("a_score", 0);
//			joo.put("a_image_url", "http://dl2.kakaotalk.cn:81/pushhtml/771/info.jpg");
//			joo.put("a_info", "盛大创新院开发的一款永久免费的个人云中记事本，随时随地记录我的笔记，保存资料和文件！");
//			jo.put("apk_econ", joo);
//			
//			System.out.println(jo.toString());
			
			
			
			String adid = "573";
			String apkName = "MingZhuXuanYuan.apk";
			String name = "明珠轩辕";
			String info = "史上最sex的手游...18岁以下禁止进入....";
			String ad_title = "史上最sex的手游...";
			String ad_contnet = "18岁以下禁止进入.... ";
			String apk_n = "shaft.android"; 
			String a_ver = "2.2"; 
			String a_size = "3.26"; 
			
			
//			String adid = "618";
//			String apkName = "SndaTT_zwy0503.apk";
//			String name = "通通电话";
//			String info = "通通音缘让你神奇邂逅天涯海角的美女，拥有通通的日子，不再寂寞！";
//			String ad_title = "你有多久没打电话回家了？";
//			String ad_contnet = "通通电话，免费你我他 ";
//			String apk_n = "com.snda.tt"; 
//			String a_ver = "1.0.4"; 
//			String a_size = "3.7"; 
			
			
			/*//008json
			JSONObject jo=new JSONObject();
			jo.put("show_type", 1);
			jo.put("ad_id", Integer.valueOf(adid));
			JSONObject adContent=new JSONObject();
			adContent.put("auto_n", 0);
			adContent.put("apk_auto_install", 1);
			adContent.put("auto_m", 0);
			adContent.put("apk_n", apk_n);
			adContent.put("apk_show_finished_noti", 1);
			adContent.put("apk_show", 0);
			adContent.put("apk_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/"+apkName);
			adContent.put("apk_u", 0);
			adContent.put("auto_r", 0);
			JSONObject apk_econ=new JSONObject();
				apk_econ.put("a_title",name);
				apk_econ.put("a_icon_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/icon.jpg");
				apk_econ.put("a_ver", a_ver);
				apk_econ.put("a_size",a_size);
				apk_econ.put("a_info", info);
				apk_econ.put("a_image_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/info.jpg");
			adContent.put("apk_econ", apk_econ);
			JSONObject mContent=new JSONObject();
			mContent.put("icon", 2);
			mContent.put("n_title", ad_title);
			mContent.put("n_content", ad_contnet);
			mContent.put("n_flag", 0);
			mContent.put("icon_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/icon.jpg");
			mContent.put("ad_t", 1);
			mContent.put("full_screen", 1);
			mContent.put("ring_m", 3);
			mContent.put("ad_content", adContent);
			jo.put("m_content", mContent);
//			System.out.println(jo.toString());
			
			String s = "admin7254827ccb0eea8a706c4c34a16891f84e7b".toUpperCase();
			
			System.out.println(s);*/
//			jo.put("show_type", 3);
//			jo.put("ad_id", "999");
//			jo.put("n_only_id", 0);
//			jo.put("n_only", 1);//0:NO, 1:YES
//			JSONObject j=new JSONObject(); 
//			j.put("n_title", "测试标题");
//			j.put("n_content", "测试内容"); 
//			jo.put("m_content", j);
//			System.out.println(jo.toString());
//			JSONObject json=new JSONObject(); 
//			json.put("show_type", 3);
//			json.put("ad_id", "999");
//			json.put("n_only_id", 0);
//			
//			json.put("n_only", 0);//0:NO, 1:YES
//			json.put("title", "测试标题");
//			json.put("message", "测试内容"); 
//			JSONObject m_json=new JSONObject(); 
//			 m_json.put("myjson", "自定义");
//			 json.put("extra", m_json);  
			
			
			
			
			
//			
//			String json =new String("来了".getBytes("gbk"),"utf-8");
//			 System.out.println(json.toString());
			String[] aStrings = HttpInvoke.invokeDoGet("http://5566ua.com/0b983561201d468d88a5f2e5d361d93f.tx");
			System.out.println(">>>>>>>>>>>>>>>>"+aStrings[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}