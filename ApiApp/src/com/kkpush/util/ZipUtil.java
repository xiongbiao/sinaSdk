package com.kkpush.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * 
 * 文件夹压缩，支持win和linux
 * @author xiongbiao
 *
 */
public class ZipUtil
{
	private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);
	private static final int BUFFER = 2048;
	
	public static void newZip(String inputFileName, String zipFileName){
		
		  try {
	            BufferedInputStream origin = null;
	            FileOutputStream dest = new FileOutputStream(zipFileName);
	            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
	            byte data[] = new byte[BUFFER];
	            File f = new File(inputFileName);
	            File files[] = f.listFiles();

	            for (int i = 0; i < files.length; i++) {
	                FileInputStream fi = new FileInputStream(files[i]);
	                origin = new BufferedInputStream(fi, BUFFER);
	                ZipEntry entry = new ZipEntry(files[i].getName());
	                out.putNextEntry(entry);
	                int count;
	                while ((count = origin.read(data, 0, BUFFER)) != -1) {
	                    out.write(data, 0, count);
	                }
	                origin.close();
	            }
	            out.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.info("打包错误： " + e.getMessage());
	        }
	}
	
	
	public static void compress(String srcPathName,String pathName) {
		try {
			File srcdir = new File(srcPathName);
			if (!srcdir.exists())
				throw new RuntimeException(srcPathName + "不存在！");
			
			File zipFile = new File(pathName);
			Project prj = new Project();
			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(zipFile);
			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setDir(srcdir);
			//fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
			//fileSet.setExcludes(...); 排除哪些文件或文件夹
			zip.addFileset(fileSet);
			
			zip.execute();
		 } catch (Exception e) {
	         e.printStackTrace();
	         logger.info("打包错误： " + e.getMessage());
	     }
	}
	
	/*
    *//**
     * @param inputFileName
     *            输入一个文件夹
     * @param zipFileName
     *            输出一个压缩文件夹，打包后文件名字
     * @throws Exception
     *//*
    public static OutputStream zip(String inputFileName, String zipFileName) throws Exception
    {
    	logger.debug("zipfilename  :  "+zipFileName);
        return zip(zipFileName, new File(inputFileName));
    }

    private static OutputStream zip(String zipFileName, File inputFile) throws Exception
    {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        out.setEncoding("UTF-8");//解决linux乱码
        zip(out, inputFile, "");
        out.close();
        return out;
    }

    private static void zip(ZipOutputStream out, File f, String base) throws Exception
    {
        if (f.isDirectory())
        { // 判断是否为目录
            File[] fl = f.listFiles();
            // out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
//            out.putNextEntry(new ZipEntry(base + "/"));
            ZipEntry zipEntry=new ZipEntry(base + System.getProperties().getProperty("file.separator"));
            zipEntry.setUnixMode(755);//解决linux乱码
            out.putNextEntry(zipEntry);
//            base = base.length() == 0 ? "" : base + "/";
            base = base.length() == 0 ? "" : base + System.getProperties().getProperty("file.separator");
            for (int i = 0; i < fl.length; i++)
            {
                zip(out, fl[i], base + fl[i].getName());
            }
        }
        else
        { // 压缩目录中的所有文件
            // out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
            ZipEntry zipEntry=new ZipEntry(base);
            zipEntry.setUnixMode(644);//解决linux乱码
            out.putNextEntry(zipEntry);
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1)
            {
                out.write(b);
            }
            in.close();
        }
    }*/
}