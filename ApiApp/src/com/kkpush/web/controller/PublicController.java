package com.kkpush.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.GenericValidator;

import sun.util.logging.resources.logging;

import com.kkpush.account.domain.Developer;
import com.kkpush.model.Mailinfo;
import com.kkpush.util.MD5;
import com.kkpush.util.SendMail;
import com.kkpush.util.StringUtils;
import com.kkpush.util.SystemConfig;

/**
 * 
 * @author xiongbiao
 * 
 */
public class PublicController {

	protected static DateFormat dayDf = new SimpleDateFormat("yyyyMMdd");

	public String getStartDate(HttpServletRequest request) {
		return (String) getDayParameter(request).get("beginTime");

	}


	public String getEndDate(HttpServletRequest request) {
		return (String) getDayParameter(request).get("endTime");

	}

	private Map<String, Object> getDayParameter(HttpServletRequest request) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String startDateStr = request.getParameter("beginTime");
		String endDateStr = request.getParameter("endTime");

		if (GenericValidator.isBlankOrNull(startDateStr)
				&& GenericValidator.isBlankOrNull(endDateStr)) {
			parameters.put("beginTime",
					String.valueOf(getStartDateForDayReport()));
			parameters.put("endTime", String.valueOf(getEndDate()));
		} else if (GenericValidator.isBlankOrNull(startDateStr)) {
			try {
				Date endDate = dayDf.parse(endDateStr);
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.MONTH, -1);
				int iStartDate = getStartDateForDayReport(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				parameters.put("beginTime", String.valueOf(iStartDate));
				parameters.put("endTime", dayDf.format(endDate));
			} catch (Exception e) {

			}
		} else if (GenericValidator.isBlankOrNull(endDateStr)) {
			try {
				Date startDate = dayDf.parse(startDateStr);

				parameters.put("beginTime", dayDf.format(startDate));
				parameters.put("endTime", String.valueOf(getEndDate()));
			} catch (Exception e) {

			}
		} else {
			try {
				Date startDate = dayDf.parse(startDateStr);
				Date endDate = dayDf.parse(endDateStr);
				parameters.put("beginTime", dayDf.format(startDate));
				parameters.put("endTime", dayDf.format(endDate));
			} catch (Exception e) {

			}
		}

		return parameters;
	}

	public int getStartDateForDayReport() {
		Calendar c = Calendar.getInstance();
		return getStartDateForDayReport(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 31);
	}

	/**
	 * get the start date of report, the report is by day base on monthly
	 * 
	 * @return
	 */
	public int getStartDateForDayReport(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		return Integer.parseInt(dayDf.format(c.getTime()));
	}

	/**
	 * get end date , the end date is current date by default
	 * 
	 * @return
	 */
	public int getEndDate() {
		Calendar c = Calendar.getInstance();
		// c.add(Calendar.DAY_OF_MONTH, -1);
		return Integer.parseInt(dayDf.format(c.getTime()));
	}

	public Map<String, Object> getResultMap(boolean isSuccess, String info) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("success", isSuccess);
		param.put("info", info);
		return param;
	}

	/***
	 * 返回 map
	 * 
	 * @param methor
	 * @param isSuccess
	 * @param info
	 * @return
	 */
	public Map<String, Object> getResponseMap(String methor, boolean isSuccess,
			String info) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("method", methor);
		responseMap.put("success", isSuccess);
		responseMap.put("info", info);
		return responseMap;
	}

	public static String getMapToString(Map<String, Object> param) {
		if (param == null) {
			return "";
		}
		StringBuffer strBuffer = new StringBuffer();
		String retStr = "";
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			strBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
		}
		retStr = strBuffer.toString();
		if (retStr.equals("")) {
			return "";
		} else {
			return retStr.substring(0, retStr.length() - 1);
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> requestToMap(HttpServletRequest request) {
		Enumeration<String> names =  request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] s = request.getParameterValues(name);
			if (s != null) {
				if (s.length > 1)
					map.put(name, s);
				else {
					map.put(name, s[0]);
				}
			}
		}
		return map;
	}

	public static String getEmail(String url ,String token ,String email ) {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append("<div>尊敬的极光推送开发者用户：</div>");
		sb.append("<div><br> </div>");
		sb.append("<div>");
		sb.append("	本邮件是应您在<a HREF=\"http://www.jpush.cn\">极光推送开发者网站</a>上，提交的重置密码请求，从而发到您邮箱的重置密码方法邮件。");
		sb.append("</div>");
		sb.append("<div>如果您没有提交重置密码请求而收到此邮件，我们非常抱歉打扰您，请忽略本邮件。</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	要重置您在<a HREF=\"http://www.jpush.cn\">极光推送开发者网站</a>上的用户密码，请点击以下链接。该链接会在浏览器上打开一个页面，让您来重设密码。");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/login_r.jsp" +  "?token=" + token + "&em="+email+  "\">现在重新设置密码</a>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>或者如果您的邮件没有正确地显示以上一行的链接，请直接复制以下一行的url完整信息到浏览器里打开，从而在打开的页面里重设密码。</div>");
		sb.append("<div>");
		sb.append("<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/login_r.jsp" +  "?token=" + token + "&em="+email+  "\">"+SystemConfig.getProperty("httpUrl", "")+"/login_r.jsp" +  "?token=" + token + "&em="+email+  "</a>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<br>");
		sb.append("</div>");
		sb.append("<div>上述重设密码页面，在您发出重置密码请求后一天内有效。</div>");
		sb.append("<div>");
		sb.append("<br> </div> <div> <br> </div> <div>&nbsp;－ 极光推送支持团队</div> <div> <br> </div>");

		return sb.toString();
	}


	/**
	 * 
	 * @author zengzhiwu
	 * @date 2012-12-17
	 * @desc  验证邮箱内容
	 * @param ramString
	 * @param email
	 * @return
	 */
	public static String validateEmail(String ramString,String email ) {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		sb.append("<div>尊敬的极光推送开发者用户：</div>");
		sb.append("<div><br> </div>");
		sb.append("<div>");
		sb.append("	本邮件是应您在<a HREF=\"http://www.jpush.cn\">极光推送开发者网站</a>上提交的邮箱验证请求，而给您发过来的验证邮件。");
		sb.append("</div>");
		sb.append("<div>如果您没有提交验证邮箱请求而收到此邮件，我们非常抱歉打扰您，请忽略本邮件。</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	验证您在<a HREF=\"http://www.jpush.cn\">极光推送开发者网站</a>上的用户邮箱，请点击以下链接。");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail" +  "?rms=" + ramString + "&em="+email+"\">现在进行邮箱验证</a>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("	<br>");
		sb.append("</div>");
		sb.append("<div>或者如果您的邮件没有正确地显示以上一行的链接，请直接复制以下一行的url完整信息到浏览器里打开，完成邮箱认证。</div>");
		sb.append("<div>");
		sb.append("<br>");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail?rms=" + ramString + "&em="+email+"\">"+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail?rms=" + ramString + "&em="+email+"</a>");
		sb.append("</div>");
		/*	sb.append("<div>");
		sb.append("<br>");
		sb.append("</div>");*/
		sb.append("<div>");
		sb.append("<br> </div> <div> <br> </div> <div>&nbsp;－ 极光推送支持团队</div> <div> <br> </div>");

		return sb.toString();
	}

	/**
	 * 
	 * @author zengzhiwu
	 * @date 2012-12-17
	 * @desc  注册欢迎邮箱内容
	 * @param ramString
	 * @param dev
	 * @return
	 */
	public static String getSuccessRegistEmail(String ramString,Developer dev){
		StringBuffer sb = new StringBuffer();

		sb.append("<div>尊敬的极光推送开发者用户：</div>");
		sb.append("<div><br> </div>");
		sb.append("<div>");
		sb.append("	热烈欢迎您注册极光推送开发者帐户，成为众多极光推送开发者中一员，为您的移动App注入更多的活力。");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("您的帐号名称为:<strong>"+dev.getDevName()+"</strong>");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("您现在就可以正常地登录、使用极光推送的所有功能了！");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("如果您忘记了极光推送开发者帐户密码，可以通过本邮箱在网站上使用重置密码的功能来重新登录。");
		sb.append("<br>");
		sb.append("<br>");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("我们强烈建议您验证邮箱，因为这样，我们就确信，您的这个邮箱地址是有效的。极光推送有服务方面的变更时，就会通过本邮箱与您及时地取得联系。");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("	验证您在<a HREF=\"http://www.jpush.cn\">极光推送开发者网站</a>上的用户邮箱，请点击以下链接。");
		sb.append("</div><br>");

		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail" +  "?rms=" + ramString + "&em="+dev.getEmail()+"\">现在进行邮箱验证</a>");
		sb.append("</div><br>");

		sb.append("<div>");
		sb.append("<div>或者如果您的邮件没有正确地显示以上一行的链接，请直接复制以下一行的url完整信息到浏览器里打开，完成邮箱认证。</div>");
		sb.append("<div>");

		sb.append("<div>");
		sb.append("<a HREF=\""+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail?rms=" + ramString + "&em="+dev.getEmail()+"\">"+SystemConfig.getProperty("httpUrl", "")+"/developer/validateEmail?rms=" + ramString + "&em="+dev.getEmail()+"</a>");
		sb.append("</div>");

		sb.append("<br><br><div>");
		sb.append("以下是几个常见的可能对你有帮助的链接：");
		sb.append("</div>");

		sb.append("<div>");
		sb.append("1.官方网站：<a href='http://www.jpush.cn'>http://www.jpush.cn</a>").append("<br>");
		sb.append("2.文档网站：<a href='http://docs.jpush.cn'>http://docs.jpush.cn</a> 　极光推送所有的文档都在这里").append("<br>");
		sb.append("3.问答网站：<a href=	'http://www.jpush.cn/qa'>http://www.jpush.cn</a> 有问题时，请访问这里提问，或者查找之前别人遇到过的").append("<br>");
		sb.append("4.技术支持邮箱：<a href='mailto:support@jpush.cn'>support@jpush.cn</a>").append("<br>");
		sb.append("</div>");


		sb.append("<br><br><div>");
		sb.append("-- 极光推送支持团队");
		sb.append("</div>");

		return sb.toString();
	}

	/**
	 * 
	 * @author zengzhiwu
	 * @date 2012-12-17
	 * @desc  生产验证文件（验证开发者url）
	 * @param devId
	 * @return
	 */
	public static String getValidateKey(int devId){
		String key = devId+"rd5s"+new Date().getTime();
		key = MD5.MD5Encode(key);

		return key;
	}

	/**
	 * 
	 * @author zengzhiwu
	 * @date 2012-12-20
	 * @desc  获取顶级域名或者参数
	 * @param url
	 * @param rootUrl true:顶级域名,false:获取参数
	 * @param params  true:获取参数
	 * @return
	 */
	public static String getRootUrl(String url,boolean rootUrl){
		if(url == null || url.equals("")) return null;
		
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i<url.length(); i++){
			if(i > 2) break;
			int index = url.indexOf("/");
			if(index == -1 && i == 2){
				buffer.append(url).append("/");
			}else{
				buffer.append(url.substring(0, index+1));
			}
			String netxUrl = url.substring(index+1);
			url = netxUrl;
		}
		if(rootUrl) return buffer.toString();
		else  return url;
	}
	
	public static void main(String[] args) {
		String url = "http://kktalk.cn.net?sdfmg/msg?kktal=sdf";
		System.out.println(getRootUrl(url,false));
	}



	//-----------------激活账号------------------------
	//				Mailinfo mailInfo = new Mailinfo();
	//				mailInfo.setToMail(email);
	//				mailInfo.setMailbody("亲 欢迎加入Ua Push家园 <br/> <a href='"+SystemConfig.getProperty("httpUrl", "")+"/developer/activate?code="
	//								+ acCode
	//								+ "&name="
	//								+ devName
	//								+ "'>点击激活账号 更多精彩等着你……</a>");
	//				mailInfo.setSubject("欢迎加入Ua Push家园");
	//				SendMail.fromMail(mailInfo);


}
