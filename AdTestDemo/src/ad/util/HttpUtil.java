package ad.util;

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

public class HttpUtil {

	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String D_CHARSET = "UTF-8";
 
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
	
	
	public static HttpURLConnection doPost(String form,String toUrl){
		  URL url = null;
		    HttpURLConnection httpurlconnection = null;
		    try
		    {
		      url = new URL(toUrl);
		      httpurlconnection = (HttpURLConnection) url.openConnection();
		      httpurlconnection.setDoOutput(true);
		      httpurlconnection.setRequestMethod("POST");
//		      String username="username=02000001";
		      httpurlconnection.getOutputStream().write(toUrl.getBytes());
		      httpurlconnection.getOutputStream().flush();
//		      httpurlconnection.getOutputStream().close();
//		      int code = httpurlconnection.getResponseCode();
//		      System.out.println("code   " + code);

		    }
		    catch(Exception e)
		    {
		      e.printStackTrace();
		    }
		    finally
		    {
//		      if(httpurlconnection!=null)
//		        httpurlconnection.disconnect();
		    }
		  return   httpurlconnection ;
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
}
