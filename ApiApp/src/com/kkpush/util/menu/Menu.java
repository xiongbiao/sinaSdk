package com.kkpush.util.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu implements Comparable<Menu> {
	private String id;
	private String name;
	private String url;
	private Integer seq;
	private Integer defaultIndex;
	private Map<String, String> attributes = new HashMap<String, String>();
	private List<Menu> subMenuList = new ArrayList<Menu>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void addAttributes(String attrName, String attrValue) {
		this.attributes.put(attrName, attrValue);
	}

	public List<Menu> getSubMenuList() {
		return subMenuList;
	}

	public void addSubMenu(Menu subMenu) {
		this.subMenuList.add(subMenu);
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setSubMenuList(List<Menu> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public int compareTo(Menu o) {
		return getSeq() - o.getSeq();
	}
	public Integer getDefaultIndex() {
		return defaultIndex;
	}
	public void setDefaultIndex(Integer defaultIndex) {
		this.defaultIndex = defaultIndex;
	}
	 

}
