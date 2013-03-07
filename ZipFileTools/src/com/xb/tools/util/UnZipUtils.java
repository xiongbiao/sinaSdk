package com.xb.tools.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;


/**
 * 
 * 文件夹压缩，支持win和linux
 * 
 * @author xiongbiao
 * 
 */
public class UnZipUtils {
	private static final int BUFFER = 2048;
	public static final String EXT = ".zip";
	
	/**
	 * 文件 解压缩
	 * 
	 * @param srcPath
	 *            源文件路径
	 * 
	 * @throws Exception
	 */
	public static void decompress(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		decompress(srcFile);
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void decompress(File srcFile) throws Exception {
		String basePath = srcFile.getParent();
		decompress(srcFile, basePath);
	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void decompress(File srcFile, File destFile) throws Exception {

		CheckedInputStream cis = new CheckedInputStream(new FileInputStream(
				srcFile), new CRC32());

//		ZipInputStream zis = new ZipInputStream(cis);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
		decompress(destFile, zis);

		zis.close();

	}

	/**
	 * 解压缩
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void decompress(File srcFile, String destPath)
			throws Exception {
		decompress(srcFile, new File(destPath));

	}

	/**
	 * 文件 解压缩
	 * 
	 * @param srcPath
	 *            源文件路径
	 * @param destPath
	 *            目标文件路径
	 * @throws Exception
	 */
	public static void decompress(String srcPath, String destPath)
			throws Exception {

		File srcFile = new File(srcPath);
		decompress(srcFile, destPath);
	}

	/**
	 * 文件 解压缩
	 * 
	 * @param destFile
	 *            目标文件
	 * @param zis
	 *            ZipInputStream
	 * @throws Exception
	 */
	private static void decompress(File destFile, ZipInputStream zis)
			throws Exception {

		
		ZipEntry entry = null;
		while ((entry = zis.getNextEntry()) != null) {

			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();

			File dirFile = new File(dir);

			// 文件检查
			fileProber(dirFile);

 			if (entry.isDirectory()) {
 				dirFile.mkdirs();
 			} else {
 				decompressFile(dirFile, zis);
 			}

 			zis.closeEntry();
 		}
 	}

	/**
	 * 文件探针
	 * 
	 * 
	 * 当父目录不存在时，创建目录！
	 * 
	 * 
	 * @param dirFile
	 */
	private static void fileProber(File dirFile) {

		File parentFile = dirFile.getParentFile();
		if (!parentFile.exists()) {

			// 递归寻找上级目录
			fileProber(parentFile);

			parentFile.mkdir();
		}

	}

	/**
	 * 文件解压缩
	 * 
	 * @param destFile
	 *            目标文件
	 * @param zis
	 *            ZipInputStream
	 * @throws Exception
	 */
	private static void decompressFile(File destFile, ZipInputStream zis)
			throws Exception {

		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(destFile));

		int count;
		byte data[] = new byte[BUFFER];
		while ((count = zis.read(data, 0, BUFFER)) != -1) {
			bos.write(data, 0, count);
		}

		bos.close();
	}

	
	public static boolean unzip(String unZipFile, String saveFilePath) {
		boolean succeed = true;
		ZipInputStream zin = null;
		ZipEntry entry;
		try {
			// zip file path
			File olddirec = new File(unZipFile);
			zin = new ZipInputStream(new FileInputStream(unZipFile));
			// iterate ZipEntry in zip
			while ((entry = zin.getNextEntry()) != null) {
				// if folder,create it
				if (entry.isDirectory()) {
					File directory = new File(olddirec.getParent(), entry
							.getName());
					if (!directory.exists()) {
						if (!directory.mkdirs()) {
							System.out.println("Create foler in "
									+ directory.getAbsoluteFile() + " failed");
						}
					}
					zin.closeEntry();
				}
				// if file,unzip it
				if (!entry.isDirectory()) {
					File myFile = new File(saveFilePath);
					FileOutputStream fout = new FileOutputStream(myFile);
					DataOutputStream dout = new DataOutputStream(fout);
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = zin.read(b)) != -1) {
						dout.write(b, 0, len);
					}
					dout.close();
					fout.close();
					zin.closeEntry();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			succeed = false;
			System.out.println(e);
		} finally {
			if (null != zin) {
				try {
					zin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (succeed)
			System.out.println("File unzipped successfully!");
		else
			System.out.println("File unzipped with failure!");
		return succeed;
	}
	
}