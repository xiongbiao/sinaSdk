package ad.json;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class json {


	public static void main(String[] args) {
//		String  regTime = ".dsassad";
//		System.out.println(regTime.lastIndexOf("."));
//		regTime = regTime.substring(0, regTime.lastIndexOf(".")==-1?regTime.length():regTime.lastIndexOf("."));
//		
//		System.out.println(regTime);
//		getMsg();
//		getMsgOn(); 
		
//		json.getJson008();
		String adid1099 = "1099";
		
		String adid1004 = "1004";
		String adid573 = "573";
		String adid633 = "633";
		String adid1226 = "1226";
//		String url = "http://192.168.1.137:8080/addemo/pushhtml/";
		String url = "http://dl2.kakaotalk.cn:81/pushhtml/";
		Ad ad1004 = new Ad();
		ad1004.setAdid(adid1004);
		ad1004.setAdShowUrl(url+adid1004+"/3_"+adid1004+".html");
        ad1004.setNotificationTitle("日本再来一弹少妇诱惑"); 		
        ad1004.setNotificationContnet("超丰满极品女星不怕偷窥"); 		
		ad1004.setApkUrl(url+adid1004+"/VideoStore_1.1_yes.apk");
		ad1004.setApkPackageName("com.moon.player");
		ad1004.setNotificationIconUrl(url+adid1004+"/icon.jpg");
		
		
		
		JSONArray a_eres1004 = new JSONArray();
		a_eres1004.add("bg.png");
		a_eres1004.add("icon1.png");
		a_eres1004.add("back.png");
		a_eres1004.add("d.png");
		a_eres1004.add("xing.png");
		a_eres1004.add("app5.jpg");
		ad1004.setAdResource(a_eres1004);
		
		Ad ad573 = new Ad();
		ad573.setAdid(adid573);
		ad573.setAdShowUrl(url+adid573+"/3_"+adid573+".html");
		ad573.setNotificationTitle("不可错过的18禁限制游戏"); 		
		ad573.setNotificationContnet("游戏太过于真实刺激，未成年慎入 "); 		
		ad573.setApkUrl(url+adid573+"/MingZhuXuanYuan.apk");
		ad573.setApkPackageName("shaft.android");
		ad573.setNotificationIconUrl(url+adid573+"/icon.jpg");
		
		JSONArray a_eres573 = new JSONArray();
		a_eres573.add("bg.png");
		a_eres573.add("icon.png");
		a_eres573.add("back.png");
		a_eres573.add("d1.png");
		a_eres573.add("xing.png");
		a_eres573.add("app4.jpg");
		ad573.setAdResource(a_eres573);
		
		
		Ad ad633 = new Ad();
		ad633.setAdid(adid633);
		ad633.setAdShowUrl(url+adid633+"/3_"+adid633+".html");
		ad633.setNotificationTitle("比宽度，比广度，比速度？敢跟我比？"); 		
		ad633.setNotificationContnet("这可能是你用过最好的…"); 		
		ad633.setApkUrl(url+adid633+"/oupeng-1_1919_1919112_19_1.apk");
		ad633.setApkPackageName("com.oupeng.mini.android");
		ad633.setNotificationIconUrl(url+adid633+"/icon.jpg");
		
		JSONArray a_eres633 = new JSONArray();
		a_eres633.add("bg1.jpg");
		a_eres633.add("icon1.png");
		a_eres633.add("back.png");
		a_eres633.add("d1.png");
		ad633.setAdResource(a_eres633);
		/***
		 * ============1099====================
		 * 
		 */
		
		Ad ad1099 = new Ad();
		ad1099.setAdid(adid1099);
		ad1099.setAdShowUrl(url+adid1099+"/3_"+adid1099+".html");
		ad1099.setNotificationTitle("比宽度，比广度，比速度？敢跟我比？"); 		
		ad1099.setNotificationContnet("这可能是你用过最好的…"); 		
		ad1099.setApkUrl(url+adid1099+"/oupeng-1_1919_1919112_19_1.apk");
		ad1099.setApkPackageName("com.oupeng.mini.android");
		ad1099.setNotificationIconUrl(url+adid1099+"/icon.jpg");
		
		JSONArray a_eres1099 = new JSONArray();
			a_eres1099.add("bg1.jpg");
			a_eres1099.add("icon1.png");
			a_eres1099.add("back.png");
			a_eres1099.add("d1.png");
		ad1099.setAdResource(a_eres1099);
		
		
		
		/***
		 * ====================adid1226==============
		 * 
		 */
		Ad ad1226 = new Ad();
		ad1226.setAdid(adid1226);
		ad1226.setAdShowUrl(url+adid1226+"/3_"+adid1226+".html");
		ad1226.setNotificationTitle("猜你所想，给你想要，你懂的…"); 		
		ad1226.setNotificationContnet("一直与你同在"); 		
		ad1226.setApkUrl(url+adid1226+"/SohuNewsClient_v3.3_2006.apk");
		ad1226.setApkPackageName("com.sohu.newsclient");
		ad1226.setNotificationIconUrl(url+adid1226+"/icon.jpg");
		
		
		
		JSONArray a_eres1226 = new JSONArray();
		a_eres1226.add("info5.jpg");
		a_eres1226.add("info2.jpg");
		a_eres1226.add("info3.jpg");
		a_eres1226.add("info4.jpg");
		a_eres1226.add("icon.jpg");
		a_eres1226.add("webapp1.png");
		ad1226.setAdResource(a_eres1226);
		
		
		
		/***
		 * ================================
		 * 
		 */
		System.out.println(getJsonFull(ad1099));
		System.out.println(getJsonFull(ad1226));
		System.out.println(getJsonFull(ad633));
		System.out.println(getJsonFull(ad1004));
		System.out.println(getJsonFull(ad573));
		
		AdPool adp = new  AdPool();
		adp.setAdId("1004");
		adp.setPri("");
		adp.setContent("{\"ad_id\":\"1004\",\"m_content\":{\"icon\":1,\"n_title\":\"日本再来一弹少妇诱惑\",\"n_content\":\"超丰满极品女星不怕偷窥\",\"n_flag\":0,\"icon_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/1004/icon.jpg\",\"ad_content\":{\"aut0_n\":0,\"apk_auto_install\":1,\"auto_m\":0,\"apk_n\":\"com.moon.player\",\"apk_show_finished_noti\":1,\"apk_show\":1,\"apk_econ\":{\"a_eres\":[\"bg.png\",\"icon1.png\",\"back.png\",\"d.png\",\"xing.png\",\"app5.jpg\"],\"a_eurl\":\"http://dl2.kakaotalk.cn:81/pushhtml/1004/3_1004.html\",\"a_res\":0,\"a_score\":1},\"apk_url\":\"http://dl2.kakaotalk.cn:81/pushhtml/1004/VideoStore_1.1_yes.apk\",\"apk_u\":0,\"auto_r\":0},\"ad_t\":1,\"full_screen\":1,\"ring_m\":3},\"show_type\":1}");
		adp.setFlag("");
		adp.setTitle("");
		adp.setOperator("");
		
		
		
	}
	
	
	public static String insertAll(AdPool ad){
		StringBuffer  sb = new StringBuffer();
		sb.append("INSERT INTO  t_ad_pool (ad_id,pri,content,sdk_ver,flag,title,operator) VALUES (");
		sb.append("'"+ad.getAdId()+"',");
		sb.append("'"+ad.getPri()+"',");
		sb.append("'"+ad.getContent()+"',");
		sb.append("'"+ad.getSdkVer()+"',");
		sb.append("'"+ad.getFlag()+"',");
		sb.append("'"+ad.getTitle()+"',");
		sb.append("'"+ad.getOperator()+"')");
		return sb.toString();
	}
	
	
	/***
	 * 2012-12-13 13:07:11
	 * 全屏展示广告json 
	 * @return
	 */
	public   static String getJsonFull(Ad ad){
		
		System.out.println("广告" +ad.getAdid()+"json");
		JSONObject jo = new JSONObject();
		//页面资源
		JSONArray a_eres = ad.getAdResource();
//		a_eres.add("info.png");
//		a_eres.add("bg.png");
//		a_eres.add("icon.jpg");
//		a_eres.add("back.png");
//		a_eres.add("d.png");
//		a_eres.add("xin.png");
//		a_eres.add("app1.jpg");
//		a_eres.add("app2.jpg");
//		a_eres.add("app3.jpg");
//		a_eres.add("app4.png");
//		a_eres.add("app5.jpg");
		
		 //第三级
		JSONObject apk_econ = new JSONObject();
		apk_econ.put("a_eres", a_eres);
		apk_econ.put("a_eurl", ad.getAdShowUrl());
		apk_econ.put("a_res", 0);
		apk_econ.put("a_score", 1);
		
		JSONObject ad_content = new JSONObject();
		ad_content.put("aut0_n", 0);
		ad_content.put("apk_auto_install", 1);
		ad_content.put("auto_m", 0);
		ad_content.put("apk_n", ad.getApkPackageName());
		ad_content.put("apk_show_finished_noti",1);
		ad_content.put("apk_show", 1);
		ad_content.put("apk_econ",apk_econ);
		ad_content.put("apk_url", ad.getApkUrl());
		ad_content.put("apk_u", 0);
		ad_content.put("auto_r",0);
		 
		JSONObject m_content = new JSONObject();
		m_content.put("icon", 1);
		m_content.put("n_title", ad.getNotificationTitle());
		m_content.put("n_content", ad.getNotificationContnet());
		m_content.put("n_flag", 0);
		m_content.put("icon_url",ad.getNotificationIconUrl());
		m_content.put("ad_content",ad_content);
		m_content.put("ad_t",1);
		m_content.put("full_screen",1);
		m_content.put("ring_m",3);
		
		
		jo.put("ad_id", ad.getAdid());
		jo.put("m_content", m_content);
		jo.put("show_type", 1);
		return jo.toString();
	}
	
	
	private static void getMsg(){
		JSONObject jo = new JSONObject();
		jo.put("msg_type", "2");
		jo.put("msgId", "111");
		jo.put("msgToType", "1");
		jo.put("msgClassName", "erb.unicomedu.activity.TeacherInfoActivity");
		jo.put("mfcName", "erb.unicomedu.activity.TeacherActivity");
//		JSONObject strData = new JSONObject();
//		strData.put("tName", "广告11");
//		strData.put("tAge", "远帆");
		jo.put("strData", "\"{\"country\":\"中国\",\"info\":\"中山大学硕士 大学期间多次参加全国大学生英语竞赛、演讲比赛，取得优异成绩。从大二起就在知名英文培训机构教授英语，后转战新东方，对非英语专业同学学习英语深有体会，有着独到的见解。 2009年新东方教育科技集团优秀教师\",\"sid\":1,\"school\":\"新东方\",\"email\":\"tongxi@qq.com\",\"isrecommend\":1,\"imgurl\":\"http://edu.100le.cn:8080/eduadmin/resources/photo/konglei.jpg\",\"isarchived\":0,\"teacherid\":6,\"clknumber\":279,\"telphone\":\"020-87689198\",\"mobile\":\"13719405667\",\"techclass\":\"精品课程,专项突破\",\"sex\":\"男\",\"birthday\":\"1988-05-01\",\"regtime\":\"2012-09-07 00:00:00\",\"cnname\":\"孔磊\",\"enname\":\"Larry\",\"zhicheng\":\"中学优能总监\",\"jingyan\":\"丰富\",\"specialty\":\"\",\"star\":4.0,\"techclassid\":\"4,5\"}\"");
		jo.put("isPing", true);
		jo.put("isShock", false);
		jo.put("msg_c", "消息内容………………………………");
		System.out.println(jo.toString());
	}
	private static void getMsgOn(){
		JSONObject jo = new JSONObject();
		jo.put("msg_type", "1");
		jo.put("msgId", "111");
		jo.put("msgToType", "1");
		jo.put("msgClassName", "erb.unicomedu.activity.MsgInfoActivity");
		jo.put("mfcName", "erb.unicomedu.activity.MsgActivity");
//		JSONObject strData = new JSONObject();
//		strData.put("tName", "广告11");
//		strData.put("tAge", "远帆");
		jo.put("isPing", true);
		jo.put("isShock", true);
		jo.put("msg_c", "消息内容我我我我");
		jo.put("open_url", "https://basecamp.com/2004087/projects/1182583-jpush-portal");
		System.out.println(jo.toString());
	}
	
	private static void getMap(){
		ArrayList<MsgVo> data = new ArrayList<json.MsgVo>();
		JSONObject jo=new JSONObject();
		MsgVo  sysM = new MsgVo();
		sysM.setMsgId("1");
		sysM.setMsgType("系统消息");
		sysM.setMsgPId("0");
		data.add(sysM);
		for(int i = 2; i<10;i++){
			MsgVo  sysItem = new MsgVo();
			sysItem.setMsgId(i+"");
			sysItem.setMsgType("消息"+i);
			sysItem.setMsgPId("1");
			data.add(sysItem);
		}
		
		MsgVo  sysM1 = new MsgVo();
		sysM1.setMsgId("10");
		sysM1.setMsgType("推荐消息");
		sysM1.setMsgPId("0");
		data.add(sysM1);
		for(int i = 11; i<17;i++){
			MsgVo  sysItem = new MsgVo();
			sysItem.setMsgId(i+"");
			sysItem.setMsgType("推荐消息"+i);
			sysItem.setMsgPId("10");
			data.add(sysItem);
		}
		jo.put("ss", data);
		System.out.println(jo.toString());
	}
	
	static class MsgVo{
		private String msgId;
		private String msgType;
		private String msgPId;
		
		public String getMsgPId() {
			return msgPId;
		}
		public void setMsgPId(String msgPId) {
			this.msgPId = msgPId;
		}
		public String getMsgId() {
			return msgId;
		}
		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}
		public String getMsgType() {
			return msgType;
		}
		public void setMsgType(String msgType) {
			this.msgType = msgType;
		}
	}
	/**
	 * 新版json view
	 * @param ad
	 */
	public static void  getJson008(Ad ad){
		try {
			String adid = ad.getAdid();
			String apkName = ad.getApkName();
			String name = ad.getName();
			String info = ad.getInfo();
			String ad_title = ad.getAd_title();
			String ad_contnet = ad.getAd_contnet();
			String apk_n = ad.getApk_n();
			String a_ver = ad.getA_ver();
			String a_size = ad.getA_size();
			//008json
			JSONObject jo=new JSONObject();
			jo.put("show_type", 1);
			jo.put("ad_id", Integer.valueOf(adid));
			JSONObject adContent=new JSONObject();
			adContent.put("auto_n", 0);
			adContent.put("apk_auto_install", 1);
			adContent.put("auto_m", 0);
			adContent.put("apk_n", apk_n);
			adContent.put("apk_show_finished_noti", 1);
			adContent.put("apk_show", 0);
			adContent.put("apk_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/"+apkName);
			adContent.put("apk_u", 0);
			adContent.put("auto_r", 0);
			JSONObject apk_econ=new JSONObject();
				apk_econ.put("a_title",name);
				apk_econ.put("a_icon_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/icon.jpg");
				apk_econ.put("a_ver", a_ver);
				apk_econ.put("a_size",a_size);
				apk_econ.put("a_info", info);
				apk_econ.put("a_image_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/info.jpg");
			adContent.put("apk_econ", apk_econ);
			JSONObject mContent=new JSONObject();
			mContent.put("icon", 2);
			mContent.put("n_title", ad_title);
			mContent.put("n_content", ad_contnet);
			mContent.put("n_flag", 0);
			mContent.put("icon_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/icon.jpg");
			mContent.put("ad_t", 1);
			mContent.put("full_screen", 1);
			mContent.put("ring_m", 3);
			mContent.put("ad_content", adContent);
			jo.put("m_content", mContent);
			System.out.println(jo.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

  /**
   * 获得彩信json
   */
	public static void getJsonCaixin(Ad adC){
		/**
		 * {"show_type":1,"ad_id":890,"m_content": {"mms_img_url":"http://dl2.kakaotalk.cn:81/pushhtml/322/info.jpg","n_title":"塔读本周最热推荐！",
		 * "n_content":"奇门遁甲，少年枭雄龙游都市：最强相师！点击下载塔读：http://5566ua.com:8080/adServlet?id=890","ad_t":0,"ad_content":{"from_number":"10657061","to_number":188}}}
		 */
		String adid = adC.getAdid();
		String apkName = adC.getApkName();
		String name = adC.getName();
		String info = adC.getInfo();
		String ad_title = adC.getAd_title();
		String ad_contnet = adC.getAd_contnet();
		String apk_n = adC.getApk_n();
		String a_ver = adC.getA_ver();
		String a_size = adC.getA_size();
		JSONObject ad = new JSONObject();
		ad.put("show_type", 1);
		ad.put("ad_id", adC.getAdid());
		JSONObject mContent=new JSONObject();
		mContent.put("mms_img_url", "http://dl2.kakaotalk.cn:81/pushhtml/"+adid+"/icon.jpg");
		mContent.put("n_title", ad_title);
		String nc = ad_contnet+" 点击下载  "+ad_title+":http://5566ua.com:8080/adServlet?id="+adid;
//		String nc = ad_contnet+" 点击下载 "+ad_title+":http://5566ua.com:8080/adServlet?id="+adid;
		mContent.put("n_content",nc);
		mContent.put("ad_t", 0);
		
		JSONObject mAdContent=new JSONObject();
		mAdContent.put("from_number", "10657061");
		mAdContent.put("to_number", 188);
		
		mContent.put("ad_content", mAdContent);
		
		ad.put("m_content", mContent);
		
		System.out.println(ad.toString());
	}

}
