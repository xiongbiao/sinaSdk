package com.kkpush.util.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.util.file.xml.XMLParser;

public class MenuXmlParser extends XMLParser {

	private static List<Menu> menuList = new ArrayList<Menu>();

	public static final String XML_MENUS = "menus";
	public static final String XML_MENU = "menu";
	public static final String XML_MENU_ATTR_ID = "id";
	public static final String XML_MENU_ATTR_NAME = "name";
	public static final String XML_MENU_ATTR_URL = "url";
	public static final String XML_MENU_ATTR_SEQ = "seq";
	public static final String XML_MENU_ATTR_DEFAULT_INDEX = "defaultIndex";
	

	public static final String XML_SUB_MENU = "subMenu";

	private static MenuXmlParser instances = null;

	private static Logger logger = LoggerFactory.getLogger(MenuXmlParser.class);

	public static synchronized MenuXmlParser getInstance() {
		if (instances == null) {
			instances = new MenuXmlParser();
		}
		return instances;
	}

	private MenuXmlParser() {

	}

	/**
	 * get the xml file name
	 */
	protected String getFileName() {
		String fileName = "";
		if (System.getProperty("CONFIG_PATH") != null) {
			fileName = System.getProperty("CONFIG_PATH") + "/conf/MenuConfig.xml";
		} else {
			fileName = MenuXmlParser.class.getClassLoader().getResource("MenuConfig.xml").getPath();
		}
		logger.info("xml file=" + fileName);
		return fileName;
	}

	/**
	 * get import function
	 */
	protected void parseContent() {
		try {
			Element root = doc.getRootElement();

			// get server information

			List<?> menuElementList = root.getChild(XML_MENUS).getChildren(XML_MENU);
			if (menuElementList != null && menuElementList.size() > 0) {
				for (int j = 0; j < menuElementList.size(); j++) {
					Menu menu = new Menu();
					Element menuElement = (Element) menuElementList.get(j);
					List<?> menuAttrList = menuElement.getAttributes();
					if (menuAttrList != null && menuAttrList.size() > 0) {
						HashMap<String, String> menuAttr = new HashMap<String, String>();
						for (int i = 0; i < menuAttrList.size(); i++) {
							Attribute attr = (Attribute) menuAttrList.get(i);
							menuAttr.put(attr.getName(), attr.getValue());
							
						}
						menu.setAttributes(menuAttr);
					}
					String id = menuElement.getAttributeValue(XML_MENU_ATTR_ID);
					String name = menuElement.getAttributeValue(XML_MENU_ATTR_NAME);
					String url = menuElement.getAttributeValue(XML_MENU_ATTR_URL);
					Attribute seqEle = menuElement.getAttribute(XML_MENU_ATTR_SEQ);
					Integer seq = 0;
					if (seqEle != null) {
						seq = seqEle.getIntValue();
					}
					Attribute defaultIndexEle = menuElement.getAttribute(XML_MENU_ATTR_DEFAULT_INDEX);
					Integer defaultIndex = 0;
					if (defaultIndexEle != null) {
						defaultIndex = defaultIndexEle.getIntValue();
					}
					menu.setId(id);
					menu.setName(name);
					menu.setUrl(url);
					menu.setSeq(seq);
					menu.setDefaultIndex(defaultIndex);
					List<Menu> subMenuList = getFieldList(menuElement);
					menu.setSubMenuList(subMenuList);
					menuList.add(menu);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		}

	}

	private List<Menu> getFieldList(Element element) throws Exception {
		List<Menu> menuList = new ArrayList<Menu>();
		if (element != null) {
			List<?> subMenuElementList = element.getChildren(XML_SUB_MENU);
			if (subMenuElementList != null && subMenuElementList.size() > 0) {
				for (int i = 0; i < subMenuElementList.size(); i++) {
					Map<String, String> attrMap = new HashMap<String, String>();
					Menu menu = new Menu();
					Element fieldElement = (Element) subMenuElementList.get(i);
					String id = fieldElement.getAttributeValue(XML_MENU_ATTR_ID);
					String name = fieldElement.getAttributeValue(XML_MENU_ATTR_NAME);
					String url = fieldElement.getAttributeValue(XML_MENU_ATTR_URL);
					Attribute seqEle = fieldElement.getAttribute(XML_MENU_ATTR_SEQ);
					Integer seq = 0;
					if (seqEle != null) {
						seq = seqEle.getIntValue();
					}
					List<?> attributes = fieldElement.getAttributes();
					for (int j = 0; j < attributes.size(); j++) {
						Attribute attribute = (Attribute) attributes.get(j);
						attrMap.put(attribute.getName(), attribute.getValue());
					}
					menu.setId(id);
					menu.setName(name);
					menu.setUrl(url);
					menu.setSeq(seq);
					menu.setAttributes(attrMap);

					List<Menu> subMenuList = getFieldList(fieldElement);
					menu.setSubMenuList(subMenuList);
					menuList.add(menu);
				}
			}

		}
		Collections.sort(menuList);
		return menuList;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}
}
