package com.kkpush.report.common.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.kkpush.util.file.xml.XMLParser;

public class ReportXmlParser extends XMLParser {

	public final static String XML_ELEMENT_ATTRIBUTE_NAME = "name";

	public final static String XML_ELEMENT_ATTRIBUTE_CODE = "code";

	public final static String XML_ELEMENT_ATTRIBUTE_FUNCID = "funcId";

	public final static String XML_ELEMENT_ATTRIBUTE_PROCESSCLASS = "processClass";

	public static final String XML_ELEMENT_ATTRIBUTE_KPICODE = "kpiCode";

	public static final String XML_ELEMENT_STYLES = "styles";

	public static final String XML_ELEMENT_SQL = "sql";

	public static final String XML_ELEMENT_REAL_SQL = "realSql";

	public static final String XML_ELEMENT_APPEND = "append";

	private static Map<String, ReportFunction> functionMap = new HashMap<String, ReportFunction>();

	private static List<ReportFunction> functionList = new ArrayList<ReportFunction>();

	private static ReportXmlParser instances = null;

	public static synchronized ReportXmlParser getInstance() {
		if (instances == null) {
			instances = new ReportXmlParser();
		}
		return instances;
	}

	private ReportXmlParser() {

	}

	/**
	 * get the xml file name
	 */
	protected String getFileName() {
		String fileName = getClass().getResource("/") + "/ReportConfig.xml";

		return fileName;
	}

	/**
	 * get import function
	 */
	protected void parseContent() {
		functionList.clear();
		functionMap.clear();
		Element root = doc.getRootElement();
		Element styles = root.getChild(XML_ELEMENT_STYLES);
		String style = "";
		if (styles != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLOutputter xmlOut = new XMLOutputter();
			try {
				xmlOut.output(styles, baos);
				style = new String(baos.toByteArray(), "UTF-8");
			} catch (Exception e) {

			}
		}

		List<?> elements = root.getChildren(XML_ELEMENT_NAME);
		for (int i = 0; i < elements.size(); i++) {
			Element function = (Element) elements.get(i);
			ReportFunction importFunction = convertElement(function);
			functionMap.put(importFunction.getCode(), importFunction);
			functionList.add(importFunction);

			Element childStyle = function.getChild(XML_ELEMENT_STYLES);
			if (childStyle != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				XMLOutputter xmlOut = new XMLOutputter();
				try {
					xmlOut.output(childStyle, baos);
					String childStyleStr = new String(baos.toByteArray(), "UTF-8");
					importFunction.setStyle(childStyleStr);
				} catch (Exception e) {

				}
			} else {
				importFunction.setStyle(style);
			}

			Element sql = function.getChild(XML_ELEMENT_SQL);
			if (sql != null) {
				importFunction.setSql(sql.getTextTrim());
			}

			Element realSql = function.getChild(XML_ELEMENT_REAL_SQL);
			if (realSql != null) {
				importFunction.setRealSql(realSql.getTextTrim());
			}
			Element append = function.getChild(XML_ELEMENT_APPEND);
			if (append != null) {
				List<?> eleList = append.getChildren();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				XMLOutputter xmlOut = new XMLOutputter();
				try {
					for (int j = 0; j < eleList.size(); j++) {
						xmlOut.output((Element) eleList.get(j), baos);
					}
					String appendStr = new String(baos.toByteArray(), "UTF-8");
					importFunction.setAppend(appendStr);
				} catch (Exception e) {

				}
			}

		}

	}

	/**
	 * convert xml element to java object
	 * 
	 * @param element
	 * @return
	 */
	private ReportFunction convertElement(Element element) {
		ReportFunction reportElement = new ReportFunction();
		String name = element.getAttributeValue(XML_ELEMENT_ATTRIBUTE_NAME);
		String code = element.getAttributeValue(XML_ELEMENT_ATTRIBUTE_CODE);
		String funcId = element.getAttributeValue(XML_ELEMENT_ATTRIBUTE_FUNCID);
		String processClass = element.getAttributeValue(XML_ELEMENT_ATTRIBUTE_PROCESSCLASS);
		String kpiCode = element.getAttributeValue(XML_ELEMENT_ATTRIBUTE_KPICODE);

		List<?> attriuts = element.getAttributes();
		Map<String, String> attributesMap = new HashMap<String, String>();
		for (int i = 0; i < attriuts.size(); i++) {
			Attribute attribute = (Attribute) attriuts.get(i);
			attributesMap.put(attribute.getName(), attribute.getValue());
		}
		reportElement.setAttributes(attributesMap);

		reportElement.setName(name);
		reportElement.setCode(code);
		reportElement.setFuncId(funcId);
		reportElement.setProcessClass(processClass);
		reportElement.setKpiCode(kpiCode);
		List<ReportField> rfList = new ArrayList<ReportField>();
		List<?> fieldList = element.getChildren(XML_CELL_NAME);
		if (fieldList != null && fieldList.size() > 0) {
			for (int i = 0; i < fieldList.size(); i++) {
				Element child = (Element) fieldList.get(i);
				ReportField rf = convertField(child);
				rfList.add(rf);
			}
		}
		reportElement.setReportFieldList(rfList);

		return reportElement;
	}

	/**
	 * convert field
	 * @param child fiel elements
	 * @return
	 */
	private ReportField convertField(Element child) {
		String id = child.getAttributeValue("id");
		String name = child.getAttributeValue("name");
		String desc = child.getAttributeValue("child");
		ReportField field = new ReportField();
		field.setId(id);
		field.setName(name);
		field.setDesc(desc);
		List<?> attributeList = child.getAttributes();
		Map<String, String> attributesMap = new HashMap<String, String>();
		if (attributeList != null && attributeList.size() > 0) {
			for (int i = 0; i < attributeList.size(); i++) {
				Attribute attribute = (Attribute) attributeList.get(i);
				attributesMap.put(attribute.getName(), attribute.getValue());
			}
		}
		field.setAttribute(attributesMap);
		return field;
	}

	public ReportFunction getFunction(String code) {
		return (ReportFunction) functionMap.get(code);
	}

	/**
	 * get the list of function
	 * 
	 * @return
	 */
	public List<ReportFunction> getFunctionList() {
		return functionList;
	}

}
