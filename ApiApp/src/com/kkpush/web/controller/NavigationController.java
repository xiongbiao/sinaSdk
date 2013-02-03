package com.kkpush.web.controller;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkpush.web.util.NavigationUtil;

@Controller
public class NavigationController {
	private static Logger logger = LoggerFactory.getLogger(NavigationController.class);
	private static String navFmt = "<li current=\"%s\" id=\"%s\" class=\"item %s\">" +
									"<a href=\"%s\"></a>" +
									"<ul id=\"sub-%s\" class=\"sub-nav\" style=\"display:%s\">" +
									"%s</ul></li>\n";	
	
	/**
	 * 生成导航栏
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/nav")
	public String buildNavigation(ModelMap map, HttpServletRequest req){
		String ctx = req.getContextPath();
		Iterator<Map<String, Object>> navs = NavigationUtil.getInstance().getNavs().iterator();
		StringBuilder sb = new StringBuilder();
//		HttpSession sess = req.getSession();
		String currentPath = req.getServletPath(); 
		while(navs.hasNext()){
			StringBuilder itemStr = new StringBuilder();
			
			Map<String, Object> item = navs.next();
			logger.debug("ITEM:" + item.get("id") + "|" + item.get("title"));
			
			String[][] subs = (String[][])item.get("sub");
			boolean isCurrent = false;
			for(int i=0; i<subs.length; i++){
				boolean isSubCurrent = false;
				isSubCurrent = subs[i][2].equals(currentPath);
				isCurrent = isCurrent || isSubCurrent;
				logger.debug("SUB ITEM:" +subs[i][0] + "|" + subs[i][1] + "|" + subs[i][2] + "|" + isCurrent);
				
				if (isSubCurrent){
					req.setAttribute("title", subs[i][1]);
				}
				
				itemStr.append("<li id=\"").append(subs[i][0]).append("\" class=\"sub-item")
					.append("\"><a href=\"").append(ctx).append(subs[i][2]).append("\" class=\"").append(isSubCurrent?" current":"").append("\">")
					.append(subs[i][1]).append("</a></li>\n");
			}
			sb.append(String.format(navFmt, new Object[]{
				isCurrent,
				item.get("id"),
				isCurrent?"current":"",
				ctx + subs[0][2],
				item.get("id"),
				isCurrent?"block":"none",
				itemStr
			}));
		}
		req.setAttribute("nav", sb.toString());
		logger.debug("nav html: [ " + sb.toString() + "]");
		return "forward:/left.jsp";
	}
	

	
}
