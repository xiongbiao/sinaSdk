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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.account.domain.Developer;
/**
 * 
 * 
 * @author xiongbiao
 * time:2012年9月21日9:12:30
 * 功能 ：cookie 登录
 */
public class CookieLoginFilter  implements Filter {

	private static Logger logger = LoggerFactory.getLogger(CookieLoginFilter.class);
	private String  jspStr ;
	private boolean isLogin(String pageUrl,String contextPath){
       
		String[] pageList = jspStr.split(";");
		if (pageList != null && pageList.length > 0) {
			for (int i = 0; i < pageList.length; i++) {
				if ((contextPath + "/" + pageList[i]).equals(pageUrl)) {
					return true;
				}
			}
		}
		return false;
	}
	public void destroy() {
		jspStr=null;
	} 
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextPath=request.getContextPath();// 获得项目名称
		String currentURL = request.getRequestURI();// 取得根目录所对应的绝对路径:  
		Object object = request.getSession().getAttribute(CommonConstants.SESSION_USER);

		String devName = null;
		String password = null;
		String autoStatus = "1";

		if (object == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				logger.info("cookie begin ------------------"); 
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					// 判断Cookie的邮箱名是否等于"JPUSHUserEmail"
					if (CommonConstants.BROWSER_COOKIE_USER_NAME.equals(cookie.getName())) {
						devName = cookie.getValue().trim();
					}

					// 判断Cookie的密码名是否等于"JPUSHUserPassword"
					if (CommonConstants.BROWSER_COOKIE_PASSWORD.equals(cookie
							.getName())) {
						password = cookie.getValue().trim();
					}

					// 判断Cookie的自动登录状态名是否等于"JPUSHAutoLoginStatus"
					if (CommonConstants.BROWSER_COOKIE_AUTO_STATUS_NAME.equals(cookie.getName())) {
						autoStatus = cookie.getValue().trim();
					}
				}
				
				if (autoStatus!=null&&autoStatus.equals("1")) {
					if (devName != null && password != null && devName.length() > 0 && password.length() > 0) {
						
						logger.info("1----操作  dev controller ------");
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("devName", devName);
						param.put("password", password);
						param.put("isRemember", 1);
						Developer developer = new Developer();
						developer.setDevName(devName);
						developer.setPassword(password);
						developer = WebInterface.PushLogin(param);
						if (developer!=null) {
							 // 将该user放入到session中  
                            request.getSession().setAttribute( CommonConstants.SESSION_USER, developer);  
                            response.sendRedirect(contextPath + "/app/application_list.jsp");
                            return ;
						} else {
							chain.doFilter(request, response);
						}
					} else {
						logger.info("1----未  操作  dev controller ------");
						chain.doFilter(request, response);
					}
				} else {
					chain.doFilter(request, response);
				}

			} else {
//				chain.doFilter(request, response);
				logger.info("---cookie is null ------");
				if (!isLogin(currentURL,contextPath)) {// 判定当前页是否是重定向以后的登录页面页面，假如是就不做session的判定，防止出现死循环
							logger.debug("request.getContextPath()=" + contextPath);  
//							request.getRequestDispatcher(contextPath + "/login.jsp").forward(request, response);// 假如session为空表示用户没有登录就重定向到login.jsp页面
							
			             	return; 
					}
//				response.sendRedirect(contextPath + "/login.jsp");
			}
		} else {
			logger.info("---Session is null ------");
			if (!isLogin(currentURL,contextPath)) {// 判定当前页是否是重定向以后的登录页面页面，假如是就不做session的判定，防止出现死循环
				logger.debug("request.getContextPath()=" + contextPath);  
//				request.getRequestDispatcher(contextPath + "/login.jsp").forward(request, response);// 假如session为空表示用户没有登录就重定向到login.jsp页面
             	return; 
		    }
			chain.doFilter(request, response);
		}
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		 jspStr = SystemConfig.getProperty("validationJsp", "");
	}

}


