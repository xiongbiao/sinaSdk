/**
 * 
 */
package com.kkpush.report.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Martin
 *
 */
public class ReportField {
	private String id;

	private String name;

	private String desc;

	Map<String, String> attribute;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the attribute
	 */
	public Map<String, String> getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

	public Map<String, String> getGraphAttributes() {
		Iterator<String> keys = attribute.keySet().iterator();
		Map<String, String> map = new HashMap<String, String>();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.startsWith("p_")) {
				String attrName = key.substring(2);
				map.put(attrName, attribute.get(key));
			}
		}
		return map;
	}

}
