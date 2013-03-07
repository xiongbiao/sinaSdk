package com.jpush.tools.util;

import java.util.*;
import java.io.*;

public class TxtReplace {

	public List<String> getAllFiles(String path, String extendName) {
		List<String> af = new ArrayList<String>();
		File file = new File(path);
		if (file.isFile()) {
			if (file.getAbsolutePath().endsWith(extendName)) {
				af.add(file.getAbsolutePath());
			}
			return af;
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				af.addAll(getAllFiles(f.getAbsolutePath(), extendName));
			}
		}
		return af;
	}

	public void txtReplace(String path, String extendName, String oldStr,
			String newStr) {
		List<String> list = new TxtReplace().getAllFiles(path, extendName);
		for (String s : list) {
			File file = new File(s);
			if (file.canRead() && file.canWrite()) {
				// System.out.println(s);
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					
					StringBuffer buffer = new StringBuffer();  
	                String text;  
	                         
                    while((text = br.readLine()) != null)  {
                    	buffer.append(text +"\n");  
                    }
					
					String outTxt = buffer.toString();
					outTxt=outTxt.replaceAll(oldStr, newStr);
					while (outTxt.endsWith("\n")) {
						outTxt = outTxt.substring(0, outTxt.length() - 1);
					}
					br.close();
					BufferedWriter bw = new BufferedWriter(new FileWriter(file,false));
					bw.write(outTxt);
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("发生IO错误...文件:" + s);
				}
			}

		}
	}

	public static void main(String[] args) {
		System.out.println("Start!....");
		new TxtReplace().txtReplace("C:/Users/xiongbiao/Desktop/test/test", "java", "import cn.jpush.android.api.JPushInterface;", "import cn.jpush.android.api.JPushInterface;\nimport  \\${packagename!''}.R;");
		System.out.println("Successed!....");
	}

}
