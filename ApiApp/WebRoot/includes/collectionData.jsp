<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.log4j.Logger"%>
<%!
	private Logger logger = Logger.getLogger(this.getClass());
	private final String EXT_SITE_SOURCE = "EXT_3RD_SITE_SOURCE";
%>
<%
	//1 第三方到首页,再到注册（设置cookie，注册页面取cookie）
	//2 第三方直接到达注册（直接取referer）
	String servletName = request.getServletPath();
	String referer = request.getHeader("Referer");
	String thirdSiteSource = null;
	if (null != referer) {
		if ((referer.indexOf("jpush.cn") >= 0) || (referer.indexOf("localhost:") >= 0) || (referer.indexOf("5566ua.com")) >= 0) {
			//ignore
		} else {
			logger.debug("ThirdSite Collection #referer= "+referer);
			thirdSiteSource = referer;
			
			// 第三方直接跳转到注册界面不需要设置到cookit
			if (servletName.indexOf("/signup") < 0) {
				Cookie cookie = new Cookie(EXT_SITE_SOURCE, referer);
				cookie.setPath("/");
				cookie.setMaxAge(3600);
				
				//设置到cookie
				response.addCookie(cookie);
			}
		}
	}
	
	//referer不存在从cookie取
	if (servletName.indexOf("/signup") >= 0) {
		if (null == thirdSiteSource) {
			Cookie cookies[] = request.getCookies();
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				if (null != cookieName && EXT_SITE_SOURCE.equals(cookieName)) {
					thirdSiteSource = cookie.getValue();
				}
			}
		}
	}
%>