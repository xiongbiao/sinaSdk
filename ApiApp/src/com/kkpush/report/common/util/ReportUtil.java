package com.kkpush.report.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kkpush.report.common.model.ReportResultDTO;
import com.kkpush.util.Constants;

/**
 * The Class ReportUtil.
 */
public class ReportUtil {
	/** The log. */
	private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class.getName());

	/**
	 * Instantiates a new report util.
	 */
	private ReportUtil() {

	}

	/** The instance. */
	private static ReportUtil instance;

	/**
	 * Gets the single instance of ReportUtil.
	 * 
	 * @return single instance of ReportUtil
	 */
	public synchronized static ReportUtil getInstance() {

		if (instance == null) {
			instance = new ReportUtil();
		}
		return instance;

	}

	/**
	 * get report function list
	 * 
	 * @return report function list
	 */

	public static List<ReportFunction> getFunctions() {
		return ReportXmlParser.getInstance().getFunctionList();
	}

	/**
	 * get funciton object by function code
	 * 
	 * @param code
	 * @return
	 */
	public static ReportFunction getReportFunction(String code) {
		logger.debug(" get report function");
		List<ReportFunction> functions = getFunctions();

		logger.debug("---------" + functions.toString());
		for (int i = 0; i < functions.size(); i++) {
			ReportFunction reportFunction = functions.get(i);
			if (code.equalsIgnoreCase(reportFunction.getCode())) {
				logger.debug("report  function is exist");
				return reportFunction;
			}
		}
		return null;
	}

	public static Reportable getProcessImp(ReportFunction reportFunction) throws Exception {
		try {
			String processClass = reportFunction.getProcessClass();
			Class<?> clazz = Class.forName(processClass);
			Reportable reportable = (Reportable) clazz.newInstance();
			return reportable;

		} catch (Exception e) {
			logger.warn("Can't load Reportable class : " + e);
			throw new Exception("Can't load Reportable class");
		}
	}

	/**
	 * export the data from server
	 * 
	 * @param parameters
	 * @return
	 */
	public synchronized ReportResultDTO generateReport(Map<String, String> parameters) throws Exception {
		String function = (String) parameters.get(Constants.REPORT_FUNCTION);
		ReportFunction reportFunction = getReportFunction(function);

		logger.debug("reportFunction: name=["+reportFunction.getName()+"],kpiCode=["+reportFunction.getKpiCode()+"]");
		
		if (reportFunction == null) {
			logger.warn("Can't get the report function: " + function);
			throw new Exception("Can't get the report function");
		}

		logger.debug("report function isSync:"+reportFunction.isSync());
		
		Reportable reportable = getProcessImp(reportFunction);
		if (reportFunction.isSync()) {
			return reportable.generateReport(parameters);
		} 
		ReportResultDTO obj = new ReportResultDTO();
		obj.setSuccess(true);
		obj.setSuccess(reportFunction.isSync());
		return obj;

	}

	

	/**
	 * generate javascript for import page
	 * 
	 * @param functions
	 * @return
	 */
	public static String generateScript(List<ReportFunction> functions) {
		StringBuffer buffer = new StringBuffer();
		List<String> displayElement = new ArrayList<String>();
		for (int i = 0; i < functions.size(); i++) {
			ReportFunction function = (ReportFunction) functions.get(i);
			buffer.append("\t if (value == '" + function.getCode() + "')");
			buffer.append("{").append((char) 13);
			Map<String, String> attributes = function.getAttributes();
			Iterator<String> keySet = attributes.keySet().iterator();
			while (keySet.hasNext()) {
				String key = keySet.next();
				if (GenericValidator.isBlankOrNull(key)) {
					continue;
				} else if (!key.startsWith("isDisplay")) {
					continue;
				}
				String value = (String) attributes.get(key);
				if (GenericValidator.isBlankOrNull(value)) {
					continue;
				}
				if (key.length() < 10) {
					continue;
				}
				String elementName = key.substring(9);
				if (value.equalsIgnoreCase("true")) {
					buffer.append("\t\tvar element = document.getElementById('");
					buffer.append(elementName).append("')").append((char) 13);
					buffer.append("\t\tif(element){").append((char) 13);
					buffer.append("\t\t\telement.style.display = '';").append((char) 13);
					buffer.append("\t\t}").append((char) 13);
					if (!displayElement.contains(elementName)) {
						displayElement.add(elementName);
					}
				} else {
					buffer.append("\t\tvar element = document.getElementById('");
					buffer.append(elementName);
					buffer.append("')").append((char) 13);

					buffer.append("\t\tif(element){").append((char) 13);

					buffer.append("\t\t\telement.style.display = 'none';").append((char) 13);

					buffer.append("\t\t}").append((char) 13);

				}
			}
			buffer.append("\t}").append((char) 13);

		}
		StringBuffer returnBuffer = new StringBuffer();
		for (int i = 0; i < displayElement.size(); i++) {
			String key = (String) displayElement.get(i);
			returnBuffer.append("\t\tvar element = document.getElementById('");
			returnBuffer.append(key);
			returnBuffer.append("')").append((char) 13);

			returnBuffer.append("\t\tif(element){").append((char) 13);

			returnBuffer.append("\t\t\telement.style.display = 'none';").append((char) 13);

			returnBuffer.append("\t\t}").append((char) 13);
		}

		returnBuffer.append(buffer);
		return returnBuffer.toString();
	}
}