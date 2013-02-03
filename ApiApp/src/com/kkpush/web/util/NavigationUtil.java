/**
 * 
 */
package com.kkpush.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kkpush.util.menu.Menu;
import com.kkpush.util.menu.MenuXmlParser;

/**
 * @author eddie 用于生成左侧导航栏
 */
public class NavigationUtil {
	private static final List<Map<String, Object>> navs = new ArrayList<Map<String, Object>>();

	private static NavigationUtil instance;

	private NavigationUtil() {
		
	}

	public List<Map<String, Object>> getNavs() {
		reloadMenu();
		return navs;
	}

	public synchronized static NavigationUtil getInstance() {
		if (instance == null) {
			instance = new NavigationUtil();
		}
		return instance;
	}

	public void reloadMenu() {
		navs.clear();
		List<Menu> menuList = MenuXmlParser.getInstance().getMenuList();
		if (menuList != null) {
			for (Menu menu : menuList) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("id", menu.getId());
				item.put("title", menu.getName());
				String[][] subMenuArr = null;
				if (menu.getSubMenuList() != null) {
					subMenuArr = new String[menu.getSubMenuList().size()][3];
					List<Menu> subMenuList = menu.getSubMenuList();
					for (int i = 0; i < subMenuList.size(); i++) {
						Menu subMenu = subMenuList.get(i);
						subMenuArr[i][0] = subMenu.getId();
						subMenuArr[i][1] = subMenu.getName();
						subMenuArr[i][2] = subMenu.getUrl();
					}
				}
				item.put("sub", subMenuArr);
				navs.add(item);
			}
		}
	}

}
