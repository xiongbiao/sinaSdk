package com.kkpush.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kkpush.account.domain.Developer;

public class AuthFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	private String jspStr;

	private boolean isNeedSessionStore(String pageUrl, String contextPath) {
		String[] pageList = jspStr.split(";");
		boolean isSession = true;
		if (pageList != null && pageList.length > 0) {
			for (int i = 0; i < pageList.length; i++) {
				if (((contextPath + "/" + pageList[i]).equals(pageUrl))) {
					isSession =  false;
				}
			}
		}
		logger.debug("check session :"+isSession);
	
		return isSession;
	}

	public void destroy() {
		jspStr = null;
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String contextPath=request.getContextPath();// 获得项目名称
		String currentURL = request.getRequestURI();// 取得根目录所对应的绝对路径:  
		HttpSession session = request.getSession(false);
		logger.debug("currentURL : " + currentURL);

		//过滤需要session支持的页面
		if (session == null || session.getAttribute(CommonConstants.SESSION_USER) == null) {
			//session为空可能是由于未登录和会话失效造成，这里先检测cookie的rememberMe功能

			boolean redirectToLoginPage = true;

			String devName = null;
			String autoStatus = null;
			String loginToken = null;

			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					// 判断Cookie的邮箱名是否等于"JPUSHUserEmail"
					if (CommonConstants.BROWSER_COOKIE_USER_NAME.equals(cookie.getName())) {
						devName = cookie.getValue().trim();
					}
					// 获取Cookie的login token
					if (CommonConstants.BROWSER_COOKIE_TOKEN.equals(cookie.getName())) {
						loginToken = cookie.getValue().trim();
					}
					// 判断Cookie的自动登录状态名是否等于"JPUSHAutoLoginStatus"
					if (CommonConstants.BROWSER_COOKIE_AUTO_STATUS_NAME.equals(cookie.getName())) {
						autoStatus = cookie.getValue().trim();
					}
				}
			}

			logger.debug(String.format("Session is null, check cookie:[%s, %s, %s]", devName, loginToken, autoStatus));

			if (null != autoStatus && autoStatus.equals("1")) {
				if (null != devName && null != loginToken && devName.length() > 0 && loginToken.length() > 0) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("devName", devName);
					param.put("loginToken", loginToken);
					param.put("isRemember", 1);
					Developer developer = WebInterface.PushLogin(param);

					if (null != developer) {
						request.getSession().setAttribute(CommonConstants.SESSION_USER, developer);
						redirectToLoginPage = false;
						logger.debug("Session already populated, db login success");
					}
				}else{
					logger.debug("----cookie信息资料不全,放弃自动登陆----");
				}
			}
		
			if (redirectToLoginPage && isNeedSessionStore(currentURL,contextPath)) {
				// 登陆失败,跳转到登陆页面
				response.sendRedirect("/login.jsp");
				return;
			}

		}
		// 加入filter链继续向下执行 
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		jspStr = SystemConfig.getProperty("validationJsp", "");
	}

}