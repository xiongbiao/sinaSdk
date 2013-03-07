package com.jpush.tools.util;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jpush.tools.model.Mailinfo;

public class XlsxUtil {
	/**
	 * @param args
	 */
	// sendEmail("email111.xlsx");
	public static void main(String[] args) {
//		sendEmail("email123.xlsx");
	}
	
/*	private static void threadSendMail(){
		Thread mThread = new Thread() {
			int count =20;
		    Random rnd = new Random();
			@Override
			public void run() {
				System.out.println("-------------");
				for (int i = 0; i <= 110; ++i) {
					synchronized (this) {
						int sleepTime = 1000*30*rnd.nextInt(4);
						if (count > 0) {
							System.out.println( "邮件NO : "+ i);
							 //发邮件
							count--;
						}else{
							count = 20;
							sleepTime = 1000*60*10;
						}
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		mThread.start();
	}*/


	
	public static void sendEmail(String fileName,String bodyMail,String subjectMail)  throws Exception{
		try {
			if(fileName==null || "".equals(fileName))
				throw new Exception("数据文件为null");
			if(subjectMail==null || "".equals(subjectMail))
				throw new Exception("邮件主题为null或者空");
			if(bodyMail==null || "".equals(bodyMail))
				throw new Exception("邮件内容为null或者空");
			FileUtil.writeErLogtoFile("filename : ----------- "+fileName,true);
			
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(fileName));
			// 读取第一个sheet
			final XSSFSheet sheet = xwb.getSheetAt(0);
			// 定义 row、cell
			XSSFCell cell;
			String output;
			final int rowNum;
			final int cellNum;
			final String  mBodyMail = bodyMail;
			final String  mSubjectMail = subjectMail;
			// 循环输出表格中的内容
			output = "";
			rowNum = sheet.getLastRowNum();
			
			Thread msgThread =	new Thread() {
				
			    Random rnd = new Random();
			    XSSFRow row ;
				@Override
				public void run()  {
					int count = Integer.valueOf(SystemConfig.getProperty("mail.count", "20"));
					FileUtil.writeErLogtoFile("------发邮件--参数-----count : " + count,true );
					FileUtil.writeLogtoFile("[  --发邮件-- ]", false);
					for (int i = 0; i <= rowNum; i++) {
						synchronized (this) {
							int rndTime = Integer.valueOf(SystemConfig.getProperty("mail.rnd.time", "30"));
							int sleepTime = 1000*rndTime*rnd.nextInt(4);
							if (count > 0) {
								FileUtil.writeErLogtoFile( "邮件NO : "+ i,true);
								 //发邮件
								row = sheet.getRow(i);
								String email = row.getCell(0).getStringCellValue() + "";
								System.out.println(email);
								try {
									Mailinfo mailInfo = new Mailinfo();
									String toMail = email;
									mailInfo.setToMail(toMail);
									mailInfo.setMailbody(mBodyMail);
									mailInfo.setSubject(mSubjectMail);
									SendMail.fromMail(mailInfo);
								} catch (Exception e) {
									e.printStackTrace();
									FileUtil.writeLogtoFile("[ "+e.getMessage()+" ]", false);
								}
								count--;
							}else{
								count = Integer.valueOf(SystemConfig.getProperty("mail.count", "20"));
								int stopTime = Integer.valueOf(SystemConfig.getProperty("mail.stop.time", "5"));
								FileUtil.writeErLogtoFile("------ -参数----  stopTime : "+ stopTime,true);
								sleepTime = 1000*60*stopTime;
							}
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
								FileUtil.writeLogtoFile("[ "+e.getMessage()+" ]", false);
							}
						}
					}
				}
			};
			
			
			SmsSendExceptionHandler smsHandler=new SmsSendExceptionHandler(); 
			msgThread.setUncaughtExceptionHandler(smsHandler);  
			msgThread.start();
			
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		}
	}
	
	
//	private static void sendEmail(String fileName) {
//
//		try {
//			String file =  fileName;
//			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
//			// 读取第一个sheet
//			final XSSFSheet sheet = xwb.getSheetAt(0);
//			// 定义 row、cell
//		   
//			XSSFCell cell;
//			String output;
//			final int rowNum;
//			final int cellNum;
//			// 循环输出表格中的内容
//			output = "";
//			rowNum = sheet.getLastRowNum();
//			
//			new Thread() {
//				int count =20;
//			    Random rnd = new Random();
//			    XSSFRow row ;
//				@Override
//				public void run() {
//					System.out.println("-------------");
//					for (int i = 0; i <= rowNum; i++) {
//						synchronized (this) {
//							int sleepTime = 1000*30*rnd.nextInt(4);
//							if (count > 0) {
//								System.out.println( "邮件NO : "+ i);
//								 //发邮件
//								row = sheet.getRow(i);
//								String email = row.getCell(0).getStringCellValue() + "";
//								System.out.println(email);
//								try {
//									Mailinfo mailInfo = new Mailinfo();
//									String toMail = email;
//									mailInfo.setToMail(toMail);
//									String content = getMailBody();
//									mailInfo.setMailbody(content);
//									mailInfo.setSubject("极光推送 SDK版本更新通知：JPush SDK 1.2.9");
//									SendMail.fromMail(mailInfo);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//
//								count--;
//							}else{
//								count = 20;
//								sleepTime = 1000*60*10;
//							}
//							try {
//								Thread.sleep(sleepTime);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}.start();
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

//	private static String getMailBody() {
//		StringBuffer sb = new StringBuffer();
////		sb.append("<span></span>各位开发者，大家好！<br><br>由于您注册了极光推送开发者，所以给您发送这个邮件，向您知会&nbsp;JPush&nbsp;SDK&nbsp;的更新。<br>新版本&nbsp;SDK&nbsp;1.2.8&nbsp;请访问极光推送官方网站下载：<a target=\"_blank\" href=\"http://jpush.cn/home/download-android.jsp\">http://jpush.cn/home/download-android.jsp</a><br><br>升级提示：建议升级。<br><br>Change&nbsp;Log:<br>1、新增功能：默认保留最近5条通知，同时提供API供开发者设置，请参考文<br>档：<a target=\"_blank\" href=\"http://docs.jpush.cn/pages/viewpage.action?pageId=2621610\">http://docs.jpush.cn/pages/viewpage.action?pageId=2621610</a><br>2、优化：&nbsp;加强检查&nbsp;AppKey&nbsp;有效性<br>3、优化：&nbsp;Example项目<br><br><br>--&nbsp;极光推送团队&nbsp;<br>");
//		sb.append("<div>各位开发者，大家好！<br><br>由于您注册了极光推送开发者，所以给您发送这个邮件，向您知会 JPush SDK 的更新。 <br><br><br>JPush Android SDK r1.2.9 新版本发布！<br><br>请访问极光推送官方网站下载：<br>http://jpush.cn/home/download-android.jsp<br><br>升级提示：建议升级。<br><br>Change Log：<br>1、新增功能：tags, alias 可设置为中文<br>2、新增功能：支持通知带参数推送<br><br>老版本升级步骤：<br>1、替换项目里的的 libs/jpush-sdk-release.jar 与 libs/armeabi/libpushprotocol.so<br>2、删除AndroidManifest.xml 里的一行：<br>receiver --&gt; cn.jpush.android.service.PushReceiver --&gt; intent-filter --&gt; <br>&lt;action android:name=\"android.intent.action.BOOT_COMPLETED\" /&gt;<br><br>或者看以下段落，以下划线的方式表示删除项：<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;receiver<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:name=\"cn.jpush.android.service.PushReceiver\"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:enabled=\"true\" &gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;intent-filter&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;action android:name=\"android.intent.action.BOOT_COMPLETED\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;action android:name=\"android.intent.action.USER_PRESENT\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;action android:name=\"android.net.conn.CONNECTIVITY_CHANGE\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;/intent-filter&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;intent-filter&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;action android:name=\"android.intent.action.PACKAGE_ADDED\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;action android:name=\"android.intent.action.PACKAGE_REMOVED\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;data android:scheme=\"package\" /&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;/intent-filter&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;/receiver&gt;<br><br><br>如遇到问题，请到极光推送官方网站使用你方便的方式咨询。<br><br><br>-- 极光推送团队<br></div>");
//		return sb.toString();
//	}
}
