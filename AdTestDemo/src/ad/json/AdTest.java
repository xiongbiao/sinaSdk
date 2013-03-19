package ad.json;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import ad.model.HttpRespons;
import ad.util.HttpRequester;
import ad.util.HttpUrls;
import ad.util.HttpUtil;

public class AdTest {
	
	public static void main(String[] args) {
		sendMsg();
		
//		newSendMsh();
	}

	private static void newSendMsh() {
		// TODO Auto-generated method stub
		try {
			HttpRequester request = new HttpRequester();
			Map<String, String> params = new HashMap<String, String>();
			params.put("uid", "25337844");
			params.put("msgtype", "0");
			params.put("message", " com.uapush.android.example \r\n uapush@uapush.com \r\n {show_type:1,ad_id:194,m_content:{ad_t:0,full_screen:1,sys_view:{allow_cancel:true,image_url_list:['http://121.9.211.85:81/pushhtml/1000/ad_1.PNG','http://121.9.211.85:81/pushhtml/1000/ad_2.png']},n_title:'Show标题',n_content:'Show内容',ad_content:{e_res:1,e_show:0,e_url:'http://www.kktalk.cn/'}}}");
			HttpRespons hr = request.sendPost("http://localhost:8080/ad/weibo");
			System.out.println(hr.getUrlString());
			System.out.println(hr.getProtocol());
			System.out.println(hr.getHost());
			System.out.println(hr.getPort());
			System.out.println(hr.getContentEncoding());
			System.out.println(hr.getMethod());
			System.out.println(hr.getContent());
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendMsg() {
		// TODO Auto-generated method stub
		
		try {
			String from = "" ;
			StringBuffer pasb = new StringBuffer();
			pasb.append("uid=" + 25337844);
			pasb.append("&msgtype=" + 0);
			pasb.append("&message=" + "com.uapush.android.example \r uapush@uapush.com \r");
			pasb.append("{show_type:1,ad_id:194,m_content:{ad_t:0,full_screen:1,sys_view:{allow_cancel:true,image_url_list:['http://121.9.211.85:81/pushhtml/1000/ad_1.PNG','http://121.9.211.85:81/pushhtml/1000/ad_2.png']},n_title:'Show标题',n_content:'Show内容',ad_content:{e_res:1,e_show:0,e_url:'http://www.kktalk.cn/'}}}");
//			pasb.append("{ \"ad_id\":\"9999129\", \"m_content\":{ \"icon\":1, \"n_title\":\"APK广告\", \"n_content\":\"APK广告\", \"n_flag\":0, \"icon_url\":\"\", \"ad_content\":{ \"auto_n\":0, \"apk_auto_install\":1, \"auto_m\":0, \"apk_n\":\"com.lehe.food\", \"apk_show_finished_noti\":1, \"apk_show\":1, \"apk_econ\":{ \"a_eres\":[ \"1-1.jpg\" ], \"a_eurl\":\"http://121.9.211.85:81/pushhtml/1000/5.html\", \"a_res\":0, \"a_score\":1 }, \"apk_url\":\"http://121.9.211.85:81/pushhtml/1000/10.apk\", \"apk_u\":0, \"auto_r\":0 }, \"ad_t\":1, \"full_screen\":1, \"ring_m\":3 }, \"show_type\":1 }");
			System.out.println(pasb.toString());
			// 在 创建 用户时 获得 token
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(pasb.toString(),HttpUrls.TEST_MSG_URL, "POST", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String msg_json = HttpUtil.inputStreamString(in);
				System.out.println(msg_json);
				
//				JSONObject json = JSONObject.fromObject(msg_json);
//				JSONObject tjson = json.getJSONObject("rsp_info");
//				System.out.println("web接口返回-token-" + tjson.getString("token"));
//				token = tjson.getString("token");
			} else if (conn.getResponseCode() == 403) {

			}
		} catch (Exception e) {
          e.printStackTrace();
		}
	}
}
