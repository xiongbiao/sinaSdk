package com.kkpush.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/** 
 * 可删除
 */
public class PushHelp {
	private static Logger logger = LoggerFactory.getLogger(PushHelp.class);

	/******************************
	 * @return
	 * @param flieName
	 *            生成文件的名称
	 * @param templateName
	 *            模板名称
	 * @param tempPath
	 *            模板路径
	 * @param htmlPath
	 *            生成页面的路径
	 * @param pv
	 *            页面对象
	 * @return
	 ******************************/
	public static String CreateHtml(String flieName, String templateName,
			String tempPath, String htmlPath, Map<String, Object> pv) {
		String filePath = "";
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(tempPath));
			// 设置读取模板文件的目录
			// 到时选择模板在这里设置
			Template t = cfg.getTemplate(templateName); // 读取文件名为Test.ftl的模板
			Map<String, Object> root = pv; // 存储数据
			// root.put("appname", pv.get("appname").toString());
			// root.put("appInfo", pv.getAppInfo());
			// root.put("apptype", pv.getApptype());
			// root.put("appTime", pv.getAppTime());
			// root.put("download", pv.getDownload());
			// root.put("size", pv.getSize());
			// root.put("updateIntroduction", pv.getUpdateIntroduction());
			// root.put("author", pv.getAuthor());
			// root.put("imgInfo", pv.getImagUrl());
			// root.put("version", pv.getVersion());
			// root.put("apkurl", pv.getApkUrl());
			// root.put("imgicon", pv.getImagicon());
			File f = new File(htmlPath);
			if (f.exists()) {
				logger.debug("文件存在");
			} else {
				logger.debug("文件不存在，正在创建...");
				if (f.mkdirs()) {
					logger.debug("文件创建成功！");
				} else {
					logger.debug("文件创建失败！");
				}

			}

			Writer out = new OutputStreamWriter(new FileOutputStream(htmlPath + "/" + flieName), "utf-8"); // 输出流
			t.process(root, out);
			// 动态加载root中的数据到 創建.html。数据在模板中定义好了。
			logger.debug("Create successfully!");
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
	/***************************************************************** 
	 * 获得文件名称
	 * 
	 * @return
	 */
	public static String getHtmlName() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return formatter.format(currentTime) + ".html";
	}

	public static String getFileName() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return formatter.format(currentTime);
	}
	public static String getJson(String templateName, String tempPath,
			Map<String, Object> root) {

		String jsonStr = "";
		Configuration cfg = new Configuration();
		try {

			cfg.setDirectoryForTemplateLoading(new File(tempPath));
			// 设置读取模板文件的目录
			// 到时选择模板在这里设置
			Template t = cfg.getTemplate(templateName); // 读取文件名为Test.ftl的模板

			// 最关键在这里，不使用与文件相关的Writer
			StringWriter stringWriter = new StringWriter();

			t.process(root, stringWriter);
			// 这里打印的就是通过模板处理后得到的字符串内容
			logger.debug("stringWriter: " + stringWriter.toString());
			jsonStr = stringWriter.toString();
			// 动态加载root中的数据到 創建.html。数据在模板中定义好了。
			logger.debug("Create successfully!");
		} catch (TemplateException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	public static void main(String[] args) throws Exception {

		// root.put("appname", pv.get("appname").toString());
		// root.put("appInfo", pv.getAppInfo());
		// root.put("apptype", pv.getApptype());
		// root.put("appTime", pv.getAppTime());
		// root.put("download", pv.getDownload());
		// root.put("size", pv.getSize());
		// root.put("updateIntroduction", pv.getUpdateIntroduction());
		// root.put("author", pv.getAuthor());
		// root.put("imgInfo", pv.getImagUrl());
		// root.put("version", pv.getVersion());
		// root.put("apkurl", pv.getApkUrl());
		// root.put("imgicon", pv.getImagicon());

		// Map<String, Object> root = new HashMap<String, Object>();
		// root.put("appname", "kkpush ");
		// root.put("appInfo",
		// "kk是一款手机通信软件，支持通过手机网络发送语音短信、视频、图片和文字，可以单聊及群聊，还能根据地理 ");
		// root.put("apptype", "kkpush ");
		// root.put("appTime", "kkpush ");
		// root.put("size", "2.0 ");
		// root.put("author", "kkpush ");
		// root.put("version", "kkpush ");
		// root.put("apkurl", "kkpush ");
		// root.put("imgicon", "kkpush ");

		// pv.setAuthor("*****公司");
		// pv.setAppTime("2011-11-11");
		// pv.setApptype("通讯.聊天");
		// pv.setDownload("123");
		// pv.setSize("2.0");
		// pv.setAppInfo("kk是一款手机通信软件，支持通过手机网络发送语音短信、视频、图片和文字，可以单聊及群聊，还能根据地理位置找到附近的人，带给朋友们全新的移动沟通体验。支持ios、Android、塞班等多种平台手机。");
		// pv.setUpdateIntroduction("2012-1-4 更新内容: kk 3.4.3 版本更新如下：
		// 摇一摇增加显示历史摇到的人。 部分体验优化，聊天背景增加纯色背景，体验更流畅。 修改了一些bug，使用更流畅，收发消息更及时。");
		//
		// Push push =new Push();
		//	
		// push.setAdId(87);
		//	
		// String p=PushHelp.getJson(
		// "json.ftl","G:/jj",ObjAnalysis.ConvertObjToMap(push));//
		/*
		 * String p= PushHelp.CreateHtml("0.html", "temphtml.ftl","",
		 * "G:/jj",root);// String
		 * str="http://dl2.kakaotalk.cn:81/pushhtml/321/SohuNewsClient_v2.1_2102.apk";
		 * logger.debug(str.substring(0,4)); if(str.substring(0,
		 * 4).equals("http")){
		 * 
		 * }else{
		 *  }
		 * 
		 * Date currentTime = new Date( ); SimpleDateFormat formatter = new
		 * SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); String
		 * t=formatter.format(currentTime); logger.debug(t);
		 */

		// update t_advertising_pool_h set ad_price = 1.2 where ad_id=204;
		String[] adid = "204;323;422;423;440;505;518;573;574;578;587;588;591;592;596;602;605;614;617;618;620;622;627;633;634;636;638;642;645;647;650;653;654;655;663;664;665;666;667;668;669;670;687;707;708;709;710;711;712;714;715;716;717;718;719;720;721;722;723;724;725;726;727;728;729;730;731;732;733;747;748;749;750;"
				.split(";");
		String[] price = "1.2;1.4;0.8;1.2;1.5;1.3;1.5;1.5;1.5;1.2;1.6;1.0;1.0;1.5;1.0;1.6;1.5;1.0;1.1;1.2;1.3;1.3;1.4;1.7;1.3;1.4;1.1;1.0;1.1;1.5;1.0;0.8;3.6;1.5;1.3;1.0;0.8;1.2;1.0;1.1;0.8;1.0;1.5;0.8;1.0;1.4;1.0;1.2;3.8;1.6;1.2;1.2;1.0;1.5;1.1;0.8;0.8;1.1;1.0;1.2;1.0;1.2;1.6;1.2;1.5;1.2;1.0;1.1;1.2;0.8;0.8;1.1;1.8;"
				.split(";");

	     String hh ="--------heh---";
	     System.out.println(hh);
		  
//		for (int i = 0; i < adid.length; i++) {
//			System.out.println("update t_advertising  set   ad_price = "
//					+ price[i] + " where ad_id=" + adid[i] + ";");
//		}
	     
	     /******
	      *  写个生成 
	      *     
	      * instanceof
	      * 
	      *
	      **
	      *
	      ***/
	       
	     // 
	}
}
