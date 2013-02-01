package com.airpush.util;

import java.security.MessageDigest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CheckSumUntil {

	private static final String TAG = "CheckSumUntil";

	public static byte[] createChecksum(File file) {

		InputStream fis = null;
		MessageDigest complete = null;
		try {
			fis = new FileInputStream(file);

			byte[] buffer = new byte[1024];
			complete = MessageDigest.getInstance("MD5");
			int numRead;

			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);

		} catch (Exception e) {
			LogUtil.d(TAG, "Get Check sum error", e);
			return null;
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
				LogUtil.d(TAG, "Get Check sum error", ex);
				return null;
			}
		}
		return complete.digest();
	}

	public static String getMD5Checksum(File filename) {
		byte[] b = createChecksum(filename);
		String result = "";
		if (null != b && b.length > 0) {
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16)
						.substring(1);
			}
		}
		return result;
	}

	public static boolean checkMd5(String md5, File file) {
		LogUtil.d(TAG, "md5 from server side: " + md5);
		if (null == md5 || "".equals(md5)) { // 不需要MD5
			LogUtil.d(TAG, "the md5 from server is: " + md5 + " do not need check MD5 code, return true");
			return true;
		}
		if (!file.exists() || file.length() == 0)
			return false;
		// caculate md5 to compare
		String md5Str = CheckSumUntil.getMD5Checksum(file);
		LogUtil.d(TAG, "md5 in the cliet file: " + md5Str);
		if (null != md5Str && !"".equals(md5Str) && md5Str.equals(md5)) {
			LogUtil.d(TAG, "Check MD5 code successful");
			return true;
		} else {
			LogUtil.d(TAG, "Check MD5 code failed");
			return false;
		}

	}

}
