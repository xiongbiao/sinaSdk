package ad.json;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ad.model.Mailinfo;
import ad.util.FileUtil;
import ad.util.SendMail;
import ad.util.SmsSendExceptionHandler;

public class XlsxUtil {
//	private static Logger logger = LoggerFactory.getLogger(XlsxUtil.class);
	/**
	 * @param args
	 */
	// sendEmail("email111.xlsx");
//	public static void main(String[] args) {
//		sendEmail("email123.xlsx");
//	}
	
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

	private static void getADJson() {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SS");
		TimeZone t = sdf.getTimeZone();
		t.setRawOffset(0);
		sdf.setTimeZone(t);
		Long startTime = System.currentTimeMillis();

		// sql

		String fileName = "F:\\我的文档\\学生缴费代码.xlsx";
		// // 检测代码
		// try {
		// PoiReadExcel er = new PoiReadExcel();
		// // 读取excel2007
		// er.testPoiExcel2007(fileName);
		// } catch (Exception ex) {
		// Logger.getLogger(FastexcelReadExcel.class.getName()).log(Level.SEVERE,
		// null, ex);
		// }
		Long endTime = System.currentTimeMillis();
		System.out.println("用时：" + sdf.format(new Date(endTime - startTime)));
		try {
			String file = "C:\\Users\\xiongbiao\\Desktop\\123.xlsx";
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一个sheet
			XSSFSheet sheet = xwb.getSheetAt(0);
			// 定义 row、cell
			XSSFRow row;
			XSSFCell cell;
			String output;
			int rowNum;
			int cellNum;
			// 循环输出表格中的内容
			output = "";
			rowNum = sheet.getLastRowNum();
			for (int i = 0; i <= rowNum; i++) {
				row = sheet.getRow(i);

				// System.out.println( "广告 id： " + (int)
				// row.getCell(1).getNumericCellValue() );
				// System.out.println( "广告 apkName： " +
				// row.getCell(10).getRichStringCellValue() );
				// System.out.println( "广告 name： " +
				// row.getCell(18).getRichStringCellValue() );
				// System.out.println( "广告 info： " +
				// row.getCell(6).getRichStringCellValue() );
				// System.out.println( "广告 ad_title： " +
				// row.getCell(5).getRichStringCellValue() );
				// System.out.println( "广告 apk_n： " +
				// row.getCell(11).getRichStringCellValue() );
				// System.out.println( "广告 a_ver： " +
				// row.getCell(20).getNumericCellValue() );
				// System.out.println( "广告 a_size： " +
				// row.getCell(21).getNumericCellValue() );
				/**
				 * 
				 * select ad_id
				 * ,ad_url,apk_title,apk_info,ad_title,ad_content,apk_name
				 * ,apk_version,apk_size from t_advertising where ad_id =633 or
				 * ad_id =776 or ad_id =972 or ad_id=858 or ad_id=949
				 * 
				 * 
				 */

				String madid = row.getCell(1).getNumericCellValue() + "";
				String mapkName = row.getCell(2).getRichStringCellValue() + "";
				String mname = row.getCell(3).getRichStringCellValue() + "";
				String minfo = row.getCell(4).getRichStringCellValue() + "";
				String mad_title = row.getCell(5).getRichStringCellValue() + "";
				String madContnet = row.getCell(6).getRichStringCellValue()
						+ "";
				String mapkN = row.getCell(7).getRichStringCellValue() + "";
				String maVer = row.getCell(8).getNumericCellValue() + "";
				String maSize = row.getCell(9).getNumericCellValue() + "";
				Ad ad = new Ad(madid.substring(0, madid.length() - 2),
						mapkName, mname, minfo, mad_title, madContnet, mapkN,
						maVer, maSize);
				// json.getJsonCaixin(ad);
				json.getJson008(ad);

				// cellNum = row.getPhysicalNumberOfCells();
				// System.out.println("cellNum : "+ cellNum);
				// for(int j = 0;j<cellNum;j++){
				// System.out.println("j : "+ j);
				// if(row.getCell(j)!=null){
				// System.out.println( "广告 :" +
				// row.getCell(j).getRichStringCellValue());
				// }
				// }
				//
				//
				// cell = row.getCell(8);
				// String s = cell.getStringCellValue();
				// byte[] b = s.getBytes("utf-8");
				// int length = b.length;
				// //
				// System.out.println(row.getCell(0).getNumericCellValue()+"  :  "+length);
				// if(length>1024){
				// System.out.println( "广告id： " +(int)
				// row.getCell(0).getNumericCellValue()+"  json大小:  "+length);
				// }else{
				// System.out.println( "广告id： " +(int)
				// row.getCell(0).getNumericCellValue()+"  json大小:  "+length);
				// }

				// double adid = cell.getNumericCellValue();
				//
				// for (int j = 0; j < cellNum; j++)
				// {
				// cell = row.getCell(j);
				// output = output + cell.getNumericCellValue() + "   ";
				// }
				// output =output + s +";";
			}

			// // System.out.println(output);
			// String uuid = UUID.randomUUID().toString().substring(0, 24) ;
			//
			// System.out.println(uuid);
			// uuid = uuid .replace("-", new Random().nextInt(10)+"");
			// System.out.println(uuid);
			// System.err.println( uuid.getBytes("utf-8").length);

			System.out.println("用时：" + sdf.format(new Date(endTime - startTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void sendEmail(String fileName,String bodyMail,String subjectMail)  throws Exception{
		try {
			if(fileName==null || "".equals(fileName))
				throw new Exception("数据文件为null");
			if(subjectMail==null || "".equals(subjectMail))
				throw new Exception("邮件主题为null或者空");
			if(bodyMail==null || "".equals(bodyMail))
				throw new Exception("邮件内容为null或者空");
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
				int count =20;
			    Random rnd = new Random();
			    XSSFRow row ;
				@Override
				public void run()  {
					System.out.println("------发邮件-------");
					FileUtil.writeLogtoFile("[  --发邮件-- ]", false);
					for (int i = 0; i <= rowNum; i++) {
						synchronized (this) {
							int sleepTime = 1000*30*rnd.nextInt(4);
							if (count > 0) {
								System.out.println( "邮件NO : "+ i);
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
								count = 20;
								sleepTime = 1000*60*10;
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
			e.printStackTrace();
			throw e;
		}
	}
	
	
	private static void sendEmail(String fileName) {

		try {
			String file = "C:\\Users\\xiongbiao\\Desktop\\" + fileName;
			XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
			// 读取第一个sheet
			final XSSFSheet sheet = xwb.getSheetAt(0);
			// 定义 row、cell
		   
			XSSFCell cell;
			String output;
			final int rowNum;
			final int cellNum;
			// 循环输出表格中的内容
			output = "";
			rowNum = sheet.getLastRowNum();
			
			new Thread() {
				int count =20;
			    Random rnd = new Random();
			    XSSFRow row ;
				@Override
				public void run() {
					System.out.println("-------------");
					for (int i = 0; i <= rowNum; i++) {
						synchronized (this) {
							int sleepTime = 1000*30*rnd.nextInt(4);
							if (count > 0) {
								System.out.println( "邮件NO : "+ i);
								 //发邮件
								row = sheet.getRow(i);
								String email = row.getCell(0).getStringCellValue() + "";
								System.out.println(email);
								try {
									Mailinfo mailInfo = new Mailinfo();
									String toMail = email;
									mailInfo.setToMail(toMail);
									String content = getMailBody();
									mailInfo.setMailbody(content);
									mailInfo.setSubject("极光推送 SDK版本更新通知：JPush SDK 1.2.8");
									SendMail.fromMail(mailInfo);
								} catch (Exception e) {
									e.printStackTrace();
								}

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
			}.start();
			
			
//			for (int i = 0; i <= rowNum; i++) {
//				row = sheet.getRow(i);
//				String email = row.getCell(0).getStringCellValue() + "";
//				System.out.println(email);
//				try {
//					Mailinfo mailInfo = new Mailinfo();
//					String toMail = email;
//					mailInfo.setToMail(toMail);
//					String content = getMailBody();
//					mailInfo.setMailbody(content);
//					mailInfo.setSubject("极光推送 SDK版本更新通知：JPush SDK 1.2.7");
//					SendMail.fromMail(mailInfo);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String getMailBody() {
		String str = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<span></span>各位开发者，大家好！<br><br>由于您注册了极光推送开发者，所以给您发送这个邮件，向您知会&nbsp;JPush&nbsp;SDK&nbsp;的更新。<br>新版本&nbsp;SDK&nbsp;1.2.8&nbsp;请访问极光推送官方网站下载：<a target=\"_blank\" href=\"http://jpush.cn/home/download-android.jsp\">http://jpush.cn/home/download-android.jsp</a><br><br>升级提示：建议升级。<br><br>Change&nbsp;Log:<br>1、新增功能：默认保留最近5条通知，同时提供API供开发者设置，请参考文<br>档：<a target=\"_blank\" href=\"http://docs.jpush.cn/pages/viewpage.action?pageId=2621610\">http://docs.jpush.cn/pages/viewpage.action?pageId=2621610</a><br>2、优化：&nbsp;加强检查&nbsp;AppKey&nbsp;有效性<br>3、优化：&nbsp;Example项目<br><br><br>--&nbsp;极光推送团队&nbsp;<br>");
		return sb.toString();
	}
}
