package com.sin180.android.sendmsg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public class FileUtil {

	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
	private static SimpleDateFormat myLogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
	private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/msg/";// 日志文件在sdcard中的路径
	private static String MYLOGFILEName = "_Log.log";// 本类输出的日志文件名称
	public static final String TAG = "FileUtil ";

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	public static void writeLogtoFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
		Date nowtime = new Date();
		String needWriteFiel = logfile.format(nowtime);
		String needWriteMessage =":.:"+ myLogSdf.format(nowtime) + "&@" + mylogtype + "&@" + tag + "&@" + text +"&@" ;
		needWriteMessage = StringUtil.encode(needWriteMessage);
		File file = new File(MYLOG_PATH_SDCARD_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			File fileLog = new File(MYLOG_PATH_SDCARD_DIR + needWriteFiel
					+ MYLOGFILEName);
			if (!fileLog.exists()) {
				if (!fileLog.createNewFile()) {
					Log.d(TAG, "创建文件");
				}
				Log.d(TAG, "文件不存在");
			} else {
				Log.d(TAG, "文件存在");
			}
			FileWriter filerWriter = new FileWriter(fileLog, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本
	 * @param fileName
	 * @return
	 */
	public static String readFile(String fileName) {
		
		fileName = MYLOG_PATH_SDCARD_DIR+fileName;
		String output = "";
		File file = new File(fileName);
		if (file.exists()) {
			if (file.isFile()) {
				try {
					BufferedReader input = new BufferedReader(new FileReader(file));
					StringBuffer buffer = new StringBuffer();
					String text;
					while ((text = input.readLine()) != null)
						buffer.append(text);
					output = buffer.toString();
				} catch (IOException ioException) {
					Log.d(TAG, "File Error!");
				}
			} else if (file.isDirectory()) {
				String[] dir = file.list();
				output += "Directory contents:/n";

				for (int i = 0; i < dir.length; i++) {
					output += dir[i];
				}
			}
		} else {
			Log.d(TAG, "Does not exist!");
		}
		return output;
	}
	
	/**
	 * hd
	 * @param path
	 * @return
	 */
	public static  ArrayList<String> getFileListName(String path){
		if(path==null){
			path = MYLOG_PATH_SDCARD_DIR;
		}
		  ArrayList<String> fileList = new ArrayList<String>();
		  File f =new  File(path);
		  if(f.isDirectory()){
			  File[] fitems = f.listFiles();
			  for(int i =0 ;i<fitems.length;i++){
				  if(fitems[i].isFile()){
					  String fileName = fitems[i].getName();
					  fileName = fileName.substring(0, fileName.lastIndexOf("_"));
					  System.out.println(fileName);
					  fileList.add(fileName);
				  }
			  }
		  }
		  return fileList;
	  }
}
