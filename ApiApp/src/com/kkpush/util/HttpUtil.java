package com.kkpush.util;

/**
 * http 请求常用类
 * @author Administrator
 *
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.account.controller.DeveloperController;

public class HttpUtil {

	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String D_CHARSET = "UTF-8";
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 *  创建服务端 交互 并 返回响应
	 * @param form 参数 格式 "?xx=x1&bb=b1"
	 * @param toUrl 访问 地址
	 * @param method  访问方式  默认POST
	 * @param charset  编码方式 默认UTF-8
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getHttpURLConnection(String form, String toUrl,String method, String charset) throws Exception {
		URL url = new URL(toUrl);
		
		
		charset =  charset.equals("") ? D_CHARSET : charset;
		byte[] data = form.getBytes(charset);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		conn.setRequestMethod(method.equals("") ? REQUEST_METHOD_POST : REQUEST_METHOD_GET);
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", charset);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(data);
		outStream.flush();
		return conn;
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamString(InputStream in) throws IOException{
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(in, D_CHARSET);
		int read = 0;
	
		char[] buff = new char[1024];
		int len;
		while((len = reader.read(buff)) > 0){
			sb.append(buff, 0, len);
		}
		return sb.toString();
	}
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamToString(InputStream in) throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader breader = new BufferedReader(new InputStreamReader(in, D_CHARSET));
		String str = breader.readLine();
		while (str != null) {
			sb.append(str);
			str = breader.readLine();
		}
		return sb.toString();
	}
	

	public static String[] doGet(String url) {
		String[] result = new String[] { "0", null };
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			result[0] = String.valueOf(statusCode);
			logger.info("URL:" + url + "   statusCode:" + statusCode);
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String buffer;
			while ((buffer = reader.readLine()) != null) {
				result[1] = buffer;
			//	logger.info(buffer);
			}
			get.abort();
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return result;
	}
	
	public static void main(String[] args) {
		String[] aStrings = HttpUtil.doGet("http://5566ua.com/0b983561201d468d88a5f2e5d364f.txt");
		System.out.println(">>>>>>>>>>>>>>>>"+aStrings[0]);
		System.out.println(aStrings[1]);
		
	}

}
