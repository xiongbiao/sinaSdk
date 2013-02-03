package com.kkpush.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.app.domain.App;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 创建 apk的example
 * @author xiongbiao
 *
 */
public class SdkExampleUtil {
	private static Logger logger = LoggerFactory.getLogger(SdkExampleUtil.class);

	/**
	 * 通过模板文件创建 新的文件
	 * @param flieName
	 * @param appId
	 * @param AppKey
	 * @param templateName
	 * @param PackageName
	 * @param tPath
	 * @return
	 */
	private static String CreateFile(String path,String flieName, String appId, String AppKey, String templateName, String PackageName, String tPath) {
		String filePath = "";
		Configuration cfg = new Configuration();
		try {
			cfg.setDirectoryForTemplateLoading(new File(tPath + "/sdktemplate"));
			Template t = cfg.getTemplate(templateName); // 读取文件名为Test.ftl的模板
			t.setEncoding("UTF-8");
			Map<String, String> root = new HashMap<String, String>(); // 存储数据
			root.put("appkey", AppKey);
			root.put("packagename", PackageName);
			File f = new File(tPath + "/sdkexample/" + appId+"/"+path);
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
			Writer out = new OutputStreamWriter(new FileOutputStream(tPath + "/sdkexample/" + appId + "/"+ path + flieName), "UTF-8"); // 输出流
			t.process(root, out);
		
			logger.debug("Create  successfully!");
		} catch (TemplateException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public static void main(String[] args) throws Exception {

//		App app = new App();
//		app.setAppId(2121);
//		app.setAppKey("e34cc4fbbe19937f601437ae");
//		app.setAppPackage("com.sin.baba");
//	    //创建项目 
//		String webPath = "H:/workspace/push_dev/push-developer/portal/WebRoot";
//		CreateExample(app,webPath,false);
		
		String path = "H:/log4";
		new FileHelp().DeleteFolder(path);
		
	}
	
	
	/**
	 * 创建测试应用
	 * isUpdate 是否更新
	 * @throws Exception
	 * 
	 */
	public static String  CreateExample(App app , String webPath,boolean isUpdate) throws Exception{
		logger.info(String.format("begin Create AppID-%s-example……!", app.getAppId()));
		 //项目目录 
		String appId = app.getAppId()+"";
		//zip包路径
		String zipPath = "/sdkexample/"+appId+"/JPushExample("+appId+").zip";
		logger.info("项目目录 ： " + webPath);
		File file = new File(webPath+zipPath);
		
		if (file.exists()) {
			if (!isUpdate) {
				logger.info(String.format(" Create AppID-%s-example……!   appzip exists not update", app.getAppId()));
				return zipPath;
			}
			else {
				String delePath = webPath+"/sdkexample/"+appId;
				new FileHelp().DeleteFolder(delePath);
			}
		}
		 
		//拷贝母版文件夹
		FileHelp.copy(webPath+"/sdkmastermask", webPath+"/sdkexample/"+appId );

		//生成AndroidManifest文件 等java文件
		CreateFiles(app,webPath);
 		
		//打包项目
		ZipUtil.compress(webPath+"/sdkexample/"+appId+"/example", webPath+zipPath);
		
		//删除打包前文件
        
		logger.info(String.format("end Create AppID-%s-example……!", app.getAppId()));
		return zipPath;
	}
	
	private static void CreateFiles(App app ,String webPath){
		  
		String appId = app.getAppId()+"";
		String appkey = app.getAppKey();
		String packagename = app.getAppPackage();
		String demoPackageName = SystemConfig.getProperty("demo.package.name");
		
		CreateFile("example/","AndroidManifest.xml", appId, appkey,  "AndroidManifest.ftl", packagename, webPath);
		CreateFile("example/"+demoPackageName,"PushSetActivity.java", appId, appkey,  "PushSetActivity.ftl", packagename, webPath);
		CreateFile("example/"+demoPackageName,"MainActivity.java", appId, appkey,  "MainActivity.ftl", packagename, webPath);
		CreateFile("example/"+demoPackageName,"SettingActivity.java", appId, appkey,  "SettingActivity.ftl", packagename, webPath);
	}

}