package com.kkpush.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kkpush.account.domain.Developer;
import com.kkpush.web.controller.PublicController;

/**
 * 接口数据的请求
 * 
 * @author xb
 * 
 */
public class WebInterface {
	private static Logger logger = LoggerFactory.getLogger(WebInterface.class);
	private static String token = TokenUtil.generateToken(SystemConfig.getProperty("toke.uid"), SystemConfig.getProperty("toke.pass"));
	/**
	 * 在 创建 用户时 获得webservice token
	 * 
	 * @param senderid
	 * @return
	 */
	public static String getToken(String senderid, String app_url) {
		String token = "";
		try {
			String from = "sender_id=" + senderid + "&callback_url=" + app_url;
			// String from = "SenderID=" + senderid;
			// 在 创建 用户时 获得 token
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.AUTH_TOKEN_URL, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				logger.info(msg_json);
				JSONObject json = JSONObject.fromObject(msg_json);
				JSONObject tjson = json.getJSONObject("rsp_info");
				logger.info("web接口返回-token-" + tjson.getString("token"));
				token = tjson.getString("token");
			} else if (conn.getResponseCode() == 403) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	/***************************************************************************
	 * 推送普通消息 如果 是富文本 这是内容传送的msgURL
	 * 
	 * 
	 * @param app_url应用回调的url
	 * @param registration_id
	 *            终端注册id
	 * @param collapse_key
	 * @param auth_token
	 *            开发者验证
	 * @param message
	 *            内容
	 * @return 消息id
	 */
	public static String pushMsg(String pushType, String app_url,String registration_id, String collapse_key, String auth_token,String message, String delay_while_idle) {
		boolean b = true;
		String msgid = "";
		try {
			String from = "registration_id=" + registration_id
			+ "&collapse_key=" + collapse_key + "&auth_token="
			+ auth_token + "&message=" + message + "&delay_while_idle="
			+ delay_while_idle;
			logger.info(from);
			String pushUrl = "";
			// 批量推送
			if (pushType.equals("") || pushType.equals("0")) {
				pushUrl = HttpUrls.PUSH_BATH_MSG_URL;
			}
			// 单独推送
			else {
				pushUrl = HttpUrls.PUSH_MSG_URL;
			}
			if (b == true) {
				// 在 推送消息时 获得 消息id
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,pushUrl, "", "ufd-8"); 
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				if (conn.getResponseCode() == 200) {

					JSONObject json = JSONObject.fromObject(msg_json);
					String code = json.getString("rsp_code");
					if (code.equals("200")) {
						JSONObject jid = json.getJSONObject("rsp_info");
						msgid = jid.getString("id");
					} else if (code.equals("503")) {
						logger.info("表示服务器没有响应，此时可以等待一段时间后重新尝试");
					}

					// msgid = json.getString("id");

				}
				// 现在返回500消息
				else {
					logger.info("访问webservice 500 --错误--");
				}
			}
			// else if()
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("访问webservice--错误--");
		}
		return msgid;
	}

	//	public static String getMapToString(Map<String, Object> param) {
	//		if(param == null){
	//			return "";
	//		}
	//		StringBuffer strBuffer = new StringBuffer();
	//		String retStr = "";
	//		for (Map.Entry<String, Object> entry : param.entrySet()) {
	//			strBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
	//		}
	//		retStr = strBuffer.toString();
	//		if (retStr.equals("")) {
	//			return "";
	//		} else {
	//			return retStr.substring(0, retStr.length() - 1);
	//		}
	//	}

	public static boolean pushMsgJob(Map<String, Object> param)throws Exception {
		boolean result = false;
		try {
			String from = PublicController.getMapToString(param);

			String pushUrl = HttpUrls.PUSH_M_LIST_URL;
			// 在 推送消息时 获得 消息id
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, pushUrl, "", "utf-8"); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				logger.info("定时推送成功" + msg_json);
			} else if (conn.getResponseCode() == 500) {
				throw new Exception("定时推送错误" + conn.getResponseCode());
			} 
			// 现在返回500消息
			else {
				throw new Exception("定时推送错误" +conn.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 登录
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Developer PushLogin(Map<String, Object> param) {
		Developer result = null;
		try {
			String from = PublicController.getMapToString(param);

			String pushUrl = HttpUrls.PUSH_LOGIN;
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, pushUrl, "", "utf-8"); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);

				if (msg_json == null || msg_json.equals("")) return null;
				JSONObject json = JSONObject.fromObject(msg_json);
				if (null == json || json.isEmpty()) return null;		//为空不做处理
				boolean success = json.getBoolean("success");

				if (success) {
					result  = new  Developer() ;
					JSONObject dev = json.getJSONObject("dev");
					result = (Developer) JSONObject.toBean(dev,Developer.class);
					//result.setQQ(dev.getString("qq")!=null&&!"null".equals(dev.getString("qq"))?dev.getString("qq"):"");
				}
			}  else {
				logger.debug("PushLogin  code : " + conn.getResponseCode() );
			}
		} catch (Exception e) {
			logger.debug("PushLogin  code : " + e.getMessage());
			e.printStackTrace();
		}
		logger.debug("result : " + result );
		return result;
	}


	/**
	 * 推送消息
	 */
	public static int pushMsg(Map<String, Object> param) throws Exception {
		int code = -1;
		HttpURLConnection conn = null;
		try {
			String postParams = PublicController.getMapToString(param);
			String pushUrl = HttpUrls.PUSH_M_URL;
			logger.info(postParams);
			conn = HttpUtil.getHttpURLConnection(postParams, pushUrl, "", "utf-8"); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				if (null != in) {
					in.close();
				}
				logger.info("推送接口返回：" + msg_json);
				if (!"".equals(msg_json)) {
					code = JSONObject.fromObject(msg_json).getInt("errcode");
				}
			} else {
				logger.warn("推送接口错误: return code=" + conn.getResponseCode());
			}
		} catch (Exception e) {
			logger.error("推送接口异常:" + e.getMessage());
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
		}
		return code;
	}

	/**
	 * 添加应用
	 * 调接口 
	 * @param param
	 * @return
	 */
	public static int newApp(Map<String, Object> param) throws Exception {
		
		boolean b = true;
		try {
			int appNewId = 0;
			param.put("token", token);
			param.put("source", 2);
			String from = PublicController.getMapToString(param);
			logger.info(from);
			String pushUrl = HttpUrls.NEW_APP_URL;
			logger.info("post url :" + pushUrl );
			if (b == true) {
				// 在 推送消息时 获得 消息id
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,pushUrl, "", "utf-8"); 
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				if (conn.getResponseCode() == 200) {
					logger.info(" new app code 200 return json  :  " + msg_json);
					JSONObject json = JSONObject.fromObject(msg_json);
					boolean success = json.getBoolean("success");
					if (success) {
						JSONObject jid = json.getJSONObject("result_detail");
						appNewId = jid.getInt("app_id");
					} else {
						int code = json.getInt("error_code");
						if( code > 0 ){
							appNewId = -code;
						} 
					}
				}// 现在返回500消息
				else {
					appNewId = -conn.getResponseCode();
					throw new CustomException("");
				}
			}
			return appNewId;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw new Exception("新增应用出错 请联系管理员 ");
		}
	}

	/**
	 * 删除应用
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static JSONObject deleteApp(Map<String, Object> param) throws Exception {
		boolean b = true;
		try {
			param.put("token", token);
			String from = PublicController.getMapToString(param);
			logger.info(from);
			String pushUrl = HttpUrls.DEL_APP_URL;
			logger.info("post url :" + pushUrl );
			JSONObject json = null;
			if (b == true) {
				// 在 推送消息时 获得 消息id
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,pushUrl, "", "utf-8"); 
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				if (conn.getResponseCode() == 200) {
					logger.info(" new app code 200 return json  :  " + msg_json);
					json = JSONObject.fromObject(msg_json);
				}// 现在返回500消息
				else {
					throw new CustomException("conn response code 500");
				}
			}
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw new Exception("删除应用出错 请联系管理员 !");
		}
	}

	public static boolean isExistsAppPackage(Map<String, Object> param) throws Exception {
		boolean b = true;
		boolean result = true;
		try {
			param.put("token", token);
			String from =  PublicController.getMapToString(param); 
			logger.info(from);
			String pushUrl = HttpUrls.SEARCH_APP_URL;
			logger.info("post url :" + pushUrl );
			if (b == true) {
				// 在 推送消息时 获得 消息id
				HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,pushUrl, "", "utf-8");
				logger.info(" 接口 code : " + conn.getResponseCode());
				if (conn.getResponseCode() == 200) {
					InputStream in = conn.getInputStream();
					String msg_json = HttpUtil.inputStreamString(in);
					logger.info("return  :  " + msg_json);
					JSONObject json = JSONObject.fromObject(msg_json);
					boolean success = json.getBoolean("success");
					if (success) {
						result = json.getBoolean("isExistApp");
					} else {
						logger.info("表示服务器没有响应，此时可以等待一段时间后重新尝试");
					}
				}
				// 现在返回500消息
				else{
					throw new CustomException("接口不存在");
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw new Exception("查询应用接口出错");
		}
	}

	/**
	 * 证书验证
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static boolean iosCertificateValidation(Map<String, Object> param ) throws Exception ,IosCVException {
		boolean result = false;
		try {
			String from =  PublicController.getMapToString(param); 
			logger.debug("iosCertificateValidation  param : "+from);
			String pushUrl = HttpUrls.IOS_C_V;
			logger.debug("post url :" + pushUrl );
			// 在 推送消息时 获得 消息id
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,pushUrl, "GET", "utf-8");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				logger.info("iosCertificateValidation return data :  " + msg_json);
				if(msg_json!=null&&!msg_json.equals("")){
					JSONObject json = JSONObject.fromObject(msg_json);
					int errno = json.getInt("errno");
					if(errno==0){
						result = true;
					}else if(errno == 1){
						result =false;
					}else{
						result =false;
					}
				}
			}
			// 现在返回500消息
			else{
				result =false;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw new Exception("证书验证接口出错");
		}
	}


	/**
	 *  登录jpush 官网
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static boolean loginJpush(Map<String, Object> param) throws Exception, IosCVException {
		boolean result = false;
		try {
			String from = PublicController.getMapToString(param);
			logger.info("iosCertificateValidation  param : " + from);
			String pushUrl = "http://localhost:48000/loginservlet";
			logger.info("post url :" + pushUrl);
			// 在 推送消息时 获得 消息id
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					pushUrl, "", "utf-8");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				logger.info("return data :  " + msg_json);

			}
			// 现在返回500消息
			else {
				result = false;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			throw new Exception("登录官网失败");
		}
	}


	public static void main(String[] args) {
		try {
			// 测试调用
			// Map<String, Object> param = new HashMap<String, Object>();
			// param.put("nameCn", "测试");
			// param.put("appid", "com.android.ts");
			// param.put("sendId", "196610102.com");
			// newApp(param);

			String appKey = UUID.randomUUID().toString();
			// appKey = UUID.randomUUID().toString().substring(0,
			// 24).replace("-", new Random().nextInt(10)+"");
			appKey = MD5.MD5Encode(appKey);
			System.out.println(appKey.substring(0, 24).length());
			// 
			// Map<String, Object> param = new HashMap<String, Object>();
			// // search_bg
			// param.put("appPackage", "com.uapush.android.example");
			// param.put("devId", 12);
			// String appid = "com.uapush.android.example";

			// boolean isAppid = appid != null && !"".equals(appid)
			// && !"null".equals(appid);
			// if (isAppid) {
			// param.put("appid", appid);
			// }
			// boolean result = WebInterface.isExistsAppPackage(param);
			// lbs 消息

			//-------------
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("username", "admin");
			param.put("sendno", 1232);
			param.put("appkeys", "e16dbf5658ac1ea8b6d46e96");
			param.put("receiver_type", "1");
			param.put("receiver_value", "004999010640000");
			String input = "admin" + 1232 + "1" + "004999010640000"+ MD5.MD5Encode("12345");
			String verificationCode = MD5.MD5Encode(input);
			param.put("verification_code", verificationCode);
			param.put("msg_type", "3");
			param.put("msg_content", "{\"n_title\":\"test\", \"n_content\":\"test msg\"}");
			param.put("send_description", "test");
			param.put("platform", "test");
			param.put("callback_url","http://117.135.160.60:8090/cgi-bin/reportresulttest.py");
			String result = PublicController.getMapToString(param);
			pushMsg(param);
			System.out.println(result);

			//			Map<String, Object> param = new HashMap<String, Object>();
			//			param.put("ctype", 2);
			//			param.put("appkey", "5d98bf0962543272590e31dc");
			//			WebInterface.iosCertificateValidation(param);
			///==============
			// 检验应用的包名存在否
			// 新增 应用 获得应用id 在自己系统添加
			// 如果是开发者的 这是 就在预推送表和正式表添加
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
