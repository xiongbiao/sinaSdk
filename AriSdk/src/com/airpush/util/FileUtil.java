package com.airpush.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import android.content.Context;
import android.text.TextUtils;



public class FileUtil {
	private static final String TAG = "FileUtil";

    public static void deepDeleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                	deepDeleteFile(f.getAbsolutePath());
                    f.delete();
                }
            }
            file.delete();
        }
    }
    
	public static boolean createHtmlFile(String filePath, String content, Context context) {
		if (null == context) throw new IllegalArgumentException("NULL context");
		LogUtil.v(TAG, "action:createHtmlFile - filePath:" + filePath + ", content:" + content);
		if (!AndroidUtil.isSdcardExist()) {
			LogUtil.d(TAG, "SDCard is not valid. Give up.");
			return false;
		}
		
	    if (!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(content)) {
	    	try {
		        File file = new File(filePath);
		        if (!file.exists()) {
		            file.createNewFile();
		        }
		        FileOutputStream fos = null;
		        try {
		            fos = new FileOutputStream(file);
		            fos.write(content.getBytes("UTF-8"));
		            fos.flush();
		        } finally {
		            if (fos != null) {
		                fos.close();
		            }
		        }
		        
		        return true;
	    	} catch (IOException e) {
	    		LogUtil.d(TAG, "", e);
	    	}
	    }
	    return false;
	}

	public static boolean createImgFile(String filePath, byte[] buffer, Context context) throws IOException {
	    SinDirectoryUtils.createPath(context);
	    if (!TextUtils.isEmpty(filePath) && buffer.length > 0 && AndroidUtil.isSdcardExist()) {
	        File file = new File(filePath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	        FileOutputStream fos = null;
	        try {
	            fos = new FileOutputStream(file);
	            fos.write(buffer);
	            fos.flush();
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }
	        return true;
	    }
	    return false;
	}

//	/**
//	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
//	 */
//	@SuppressWarnings("rawtypes")
//	public static int upZipFile(File zipFile, String folderPath) {
//		// public static void upZipFile() throws Exception{
//		ZipFile zfile = null;
//		OutputStream os = null;
//		InputStream is = null;
//		try {
//			zfile = new ZipFile(zipFile);
//			Enumeration zList = zfile.entries();
//			ZipEntry ze = null;
//			byte[] buf = new byte[1024];
//			while (zList.hasMoreElements()) {
//				ze = (ZipEntry) zList.nextElement();
//				if (ze.isDirectory()) {
//					LogUtil.i(TAG, "ze.getName() = " + ze.getName());
//					String dirstr = folderPath + ze.getName();
//					// dirstr.trim();
//					dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
//					Log.d("upZipFile", "str = " + dirstr);
//					File f = new File(dirstr);
//					f.mkdir();
//					continue;
//				}
//				LogUtil.i(TAG, "ze.getName() = " + ze.getName());
//				os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
//				is = new BufferedInputStream(zfile.getInputStream(ze));
//				int readLen = 0;
//				while ((readLen = is.read(buf, 0, 1024)) != -1) {
//					os.write(buf, 0, readLen);
//				}
//			}
//		} catch (Exception e) {
//			LogUtil.e(TAG , "", e);
//			return -1;
//		} finally {
//			try {
//				if (is != null) is.close();
//				if (os != null) os.close();
//				if (zfile != null) zfile.close();
//			} catch (IOException e) {
//				LogUtil.e(TAG , "", e);
//			}
//		}
//		return 0;
//	}
//    
//	 /**
//     * 给定根目录，返回一个相对路径所对应的实际文件名.
//     * @param baseDir 指定根目录
//     * @param absFileName 相对路径名，来自于ZipEntry中的name
//     * @return java.io.File 实际的文件
// */
//	public static File getRealFileName(String baseDir, String absFileName) {
//		String[] dirs = absFileName.split("/");
//		File ret = new File(baseDir);
//		String substr = null;
//		if (dirs.length > 1) {
//			for (int i = 0; i < dirs.length - 1; i++) {
//				substr = dirs[i];
//				try {
//					substr = new String(substr.getBytes("8859_1"), "GB2312");
//				} catch (UnsupportedEncodingException e) {
//					LogUtil.e(TAG , "", e);
//				}
//				ret = new File(ret, substr);
//			}
//			LogUtil.i(TAG, "ret = " + ret);
//			if (!ret.exists()) ret.mkdirs();
//			substr = dirs[dirs.length - 1];
//			try {
//				// substr.trim();
//				substr = new String(substr.getBytes("8859_1"), "GB2312");
//				LogUtil.i(TAG, "substr = " + substr);
//			} catch (UnsupportedEncodingException e) {
//				LogUtil.e(TAG , "", e);
//			}
//
//			ret = new File(ret, substr);
//			Log.d("upZipFile", "2ret = " + ret);
//			return ret;
//		}
//		return ret;
//	}
}
