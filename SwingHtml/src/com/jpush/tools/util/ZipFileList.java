package com.jpush.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.jpush.tools.util.zip.ZipEntry;
import com.jpush.tools.util.zip.ZipInputStream;

public class ZipFileList {
	public static void main(String[] args) {
		extZipFileList("C:/Users/xiongbiao/Desktop/Jpush-Android-sdk.zip",
				"C:/Users/xiongbiao/Desktop/test/");
		try {
//			ZipUtil.unzip("C:/Users/xiongbiao/Desktop/Jpush-Android-sdk.zip",
//					"C:/Users/xiongbiao/Desktop/test/");
//			CompresszZipFile.ReadZip ("C:/Users/xiongbiao/Desktop/Jpush-Android-sdk.zip",
//					"C:/Users/xiongbiao/Desktop/test/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void extZipFileList(String zipFileName, String extPlace) {
		try {

			ZipInputStream in = new ZipInputStream(new FileInputStream(
					zipFileName));

			ZipEntry entry = null;

			while ((entry = in.getNextEntry()) != null) {

				String entryName = entry.getName();

				if (entry.isDirectory()) {
					File file = new File(extPlace + entryName);
					file.mkdirs();
					System.out.println("创建文件夹:" + entryName);
				} else {

					FileOutputStream os = new FileOutputStream(extPlace
							+ entryName);

					// Transfer bytes from the ZIP file to the output file
					byte[] buf = new byte[1024];

					int len;
					while ((len = in.read(buf)) > 0) {
						os.write(buf, 0, len);
					}
					os.close();
					in.closeEntry();

				}
			}

		} catch (IOException e) {
		}
		System.out.println("解压文件成功");
	}
}
