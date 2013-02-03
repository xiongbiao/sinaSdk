package com.kkpush.report.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

/**
 * 数据 xml属性类
 * 
 * @author
 * 
 */
public class ReportFunction {
	private String name;

	private String code;

	private String processClass;

	private boolean isDisplayShow;

	private String funcId;

	private boolean isSync;

	private String mailTo;

	private String mailCc;

	private String kpiCode;

	private Map<String, String> attributes;

	private List<ReportField> reportFieldList;

	private String style;

	private String sql;

	private String realSql;

	private String append;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the processClass
	 */
	public String getProcessClass() {
		return processClass;
	}

	/**
	 * @param processClass
	 *            the processClass to set
	 */
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	/**
	 * @return the isDisplayShow
	 */
	public boolean isDisplayShow() {
		return isDisplayShow;
	}

	/**
	 * @param isDisplayShow
	 *            the isDisplayShow to set
	 */
	public void setDisplayShow(boolean isDisplayShow) {
		this.isDisplayShow = isDisplayShow;
	}

	/**
	 * @return the funcId
	 */
	public String getFuncId() {
		return funcId;
	}

	/**
	 * @param funcId
	 *            the funcId to set
	 */
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	/**
	 * @return the isSync
	 */
	public boolean isSync() {
		String value = getAttributeValue("isSync");
		isSync = "true".equalsIgnoreCase(value);
		return isSync;
	}

	/**
	 * @param isSync
	 *            the isSync to set
	 */
	public void setSync(boolean isSync) {
		this.isSync = isSync;
	}

	/**
	 * @return the mailTo
	 */
	public String getMailTo() {
		return mailTo;
	}

	/**
	 * @param mailTo
	 *            the mailTo to set
	 */
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the mailCc the mailCc to set
	 */
	public String getMailCc() {
		return mailCc;
	}

	/**
	 * @param mailCc
	 *            the mailCc the mailCc to set
	 */
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	/**
	 * @return the reportFieldList
	 */
	public List<ReportField> getReportFieldList() {
		return reportFieldList;
	}

	/**
	 * @param reportFieldList
	 *            the reportFieldList to set
	 */
	public void setReportFieldList(List<ReportField> reportFieldList) {
		this.reportFieldList = reportFieldList;
	}

	/**
	 * @return the kpiCode
	 */
	public String getKpiCode() {
		return kpiCode;
	}

	/**
	 * @param kpiCode
	 *            the kpiCode to set
	 */
	public void setKpiCode(String kpiCode) {
		this.kpiCode = kpiCode;
	}

	public String getAttributeValue(String attrName) {
		String value = attributes.get(attrName);
		if (value == null) {
			value = "";
		}
		return value;
	}

	public Map<String, String> getGraphAttributes() {
		Iterator<String> keys = attributes.keySet().iterator();
		Map<String, String> map = new HashMap<String, String>();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.startsWith("p_")) {
				String attrName = key.substring(2);
				map.put(attrName, attributes.get(key));
			}
		}
		return map;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the realSql
	 */
	public String getRealSql() {
		return realSql;
	}

	/**
	 * @param realSql
	 *            the realSql to set
	 */
	public void setRealSql(String realSql) {
		this.realSql = realSql;
	}

	/**
	 * @return the append
	 */
	public String getAppend() {
		return append;
	}

	/**
	 * @param append
	 *            the append to set
	 */
	public void setAppend(String append) {
		this.append = append;
	}

	public ReportField getReportFieldByAlias(String alias) {
		if (reportFieldList != null && reportFieldList.size() > 0 && !GenericValidator.isBlankOrNull(alias)) {
			for (ReportField field : reportFieldList) {
				if (alias.equalsIgnoreCase(field.getAttribute().get("alias"))) {
					return field;
				}
			}
		}
		return null;
	}

}
