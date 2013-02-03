package com.kkpush.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 文件操作
 * @author xiongbiao
 *
 */
public class FileHelp {
	private static Logger logger = LoggerFactory.getLogger(FileHelp.class);

	/***
	 * 从服务端获得文件来保存
	 * 
	 * @param pamer
	 * @param request
	 * @return
	 */
	public static String saveFile(Map<String, Object> pamer,
			HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// // 获得文件：
		logger.debug("---------------seve file begin -------------");
		String filePath = (String) pamer.get("filePath");
		String r_file = (String) pamer.get("r_file");//apn证书
		String newFileName = (String) pamer.get("newFileName");
		String cPath = SystemConfig.getProperty("file_path", "");
		if (cPath == null) {
			cPath = request.getSession().getServletContext().getRealPath("/devfile/");
		}
		MultipartFile file = multipartRequest.getFile(r_file);
		if (file == null || file.getSize() == 0) {
			return null;
		}
		String targetDirectory = cPath + filePath + "/";
		try {
			logger.debug("filePath : " + targetDirectory);
			File filenew = new File(targetDirectory);
			if (filenew.exists()) {
				logger.debug(" File exists  ");
			} else {
				logger.debug("The file does not exist, create ...");
				if (filenew.mkdirs()) {
					logger.debug("File is successfully created ！");
				} else {
					logger.debug("File creation failed ！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		String filename = "";
		if (file != null && file.getSize() > 0) {
			try {
				logger.debug("1----------" + targetDirectory);
				filename = saveFileToServer(file, newFileName, targetDirectory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.debug("---------------end file begin -------------");
		return filename;
	}

	/***
	 * 保存文件
	 * 
	 * @param multifile
	 * @param newFileName
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String saveFileToServer(MultipartFile multifile,
			String newFileName, String path) throws IOException {
		String filename = multifile.getOriginalFilename();
		String infoextName = filename.substring(filename.lastIndexOf("."));
		String infofileName = filename;
		if (newFileName != null && !newFileName.equals("")) {
			infofileName = newFileName + infoextName;
		}
		// 创建目录
		File dir = new File(path);
		if (!dir.exists()) {
			logger.debug("~~~~~~~~~File does not exist-----");
			dir.mkdirs();
		}
		// 读取文件流并保持在指定路径
		InputStream inputStream = multifile.getInputStream();
		OutputStream outputStream = new FileOutputStream(path + "/" + infofileName);
		byte[] buffer = multifile.getBytes();
//		int bytesum = 0;
		int byteread = 0;
		while ((byteread = inputStream.read(buffer)) != -1) {
//			bytesum += byteread;
			outputStream.write(buffer, 0, byteread);
			outputStream.flush();
		}
		outputStream.close();
		inputStream.close();
		logger.debug("file name： " + infofileName);
		return infofileName;
	}

	/**
	 * 拷贝文件夹
	 * @param src
	 * @param des
	 */
	public static void copy(String src, String des) {
		File file1 = new File(src);
		File[] fs = file1.listFiles();
		File file2 = new File(des);
		if (!file2.exists()) {
			file2.mkdirs();
		}
		 for (File f : fs) {
				if (f.isFile()) {
					fileCopy(f.getPath(), des + "/" + f.getName()); // 调用文件拷贝
				} else if (f.isDirectory()) {
					copy(f.getPath(), des + "/" + f.getName());
				}
		 }
	}

	/**
	 * 文件拷贝
	 */
	private static void fileCopy(String src, String des) {
		try {
			File srcFile = new File(src);
			File destFile = new File(des);
			FileUtils.copyFile(srcFile, destFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
    	boolean flag ;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
    	boolean flag ;
        flag = false;
        File   file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *@param sPath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public    boolean DeleteFolder(String sPath) {
    	boolean flag ;
        flag = false;
        File  file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
}
