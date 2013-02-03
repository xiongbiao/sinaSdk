/**
 * $Header: /data/cvs/TSVDB/src/java/com/gs/core/batchjob/report/AbstractReportImpl.java,v 1.3 2010/07/15 08:44:55 martinliu Exp $ 
 * $Revision: 1.3 $ 
 * $Date: 2010/07/15 08:44:55 $ 
 * 
 * ==================================================================== 
 * 
 * Copyright (c) 2009 Media Data Systems Pte Ltd All Rights Reserved. 
 * This software is the confidential and proprietary information of 
 * Media Data Systems Pte Ltd. You shall not disclose such Confidential 
 * Information. 
 * 
 * ==================================================================== 
 * 
 */
package com.kkpush.report.graph.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.validator.GenericValidator;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.kkpush.report.common.model.ReportResultDTO;
import com.kkpush.report.common.util.ReportField;
import com.kkpush.report.common.util.ReportFunction;
import com.kkpush.report.common.util.ReportUtil;
import com.kkpush.report.common.util.Reportable;
import com.kkpush.util.Constants;
import com.kkpush.util.SpringContextUtils;
import com.mysql.jdbc.PreparedStatement;

/**
 * this class will do report operation
 * 
 * @author Peter Yang
 * 
 */
public abstract class AbstractReportImpl implements Reportable {
	JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringContextUtils.getBean("jdbcTemplate");

	private static Logger logger = LoggerFactory.getLogger(AbstractReportImpl.class.getName());

	protected static DateFormat df = new SimpleDateFormat("yyyyMMdd");
	protected static DateFormat sfm = new SimpleDateFormat("yyyy,MM,dd");
	protected static DateFormat monthDf = new SimpleDateFormat("yyyyMM");
	protected static DateFormat yearDf = new SimpleDateFormat("yyyy");

	public static SimpleDateFormat dformat  = new SimpleDateFormat("dd");

	protected ReportFunction reportFunction;

	protected static DecimalFormat numFormat = new DecimalFormat("#");

	protected static DecimalFormat decFormat = new DecimalFormat(".##");

	public ReportResultDTO generateReport(Map<String, String> parameters) throws Exception {
		processParameter(parameters);
		ReportResultDTO resultDto = processData(parameters);
		return resultDto;

	}

	/**
	 * process the parameter
	 * 
	 * @param parameters
	 */
	protected void processParameter(Map<String, String> parameters) {
		String function = (String) parameters.get(Constants.REPORT_FUNCTION);

		logger.debug(">>>>>>> function:"+function);

		reportFunction = ReportUtil.getReportFunction(function);
		setParameter(parameters);
	}

	protected String getSql(List<Object> paramList, Map<String, String> parameters) {
		if ("2".equals(reportFunction.getAttributeValue("calType"))) {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from (");

			sql.append(getDetailSql(paramList, parameters));

			sql.append(") order by idate");

			return sql.toString();
		} else {
			StringBuffer sql = new StringBuffer();
			final List<ReportField> rfList = getReportFieldList(parameters);
			sql.append("select * from (");
			for (int i = 0; i < rfList.size(); i++) {
				ReportField rf = rfList.get(i);
				parameters.put(Constants.RPT_PARAM_KPI_FIELD_ID, rf.getId());
				sql.append(getDetailSql(paramList, parameters));

				if (i != getReportFieldList(parameters).size() - 1) {
					sql.append(" union all ");
				}
			}
			sql.append(") a order by IDATE,KPI_FIELD");

			return sql.toString();

		}
	}

	protected abstract String getDetailSql(List<Object> paramList, Map<String, String> parameters);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Map<String, String>> getData(Map<String, String> parameters) {
		final Map<String, Map<String, String>> dataMap = new LinkedHashMap<String, Map<String, String>>();

		if (getReportFieldList(parameters).size() > 0) {
			List<Object> paramList = new ArrayList<Object>();

			String sql = getSql(paramList, parameters);
			logger.debug("report search sql>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+sql) ;
			logger.debug(assembleSQL(sql, paramList.toArray()));

			RowMapper rm = new DateDBResult(dataMap);
			if ("2".equals(reportFunction.getAttributeValue("calType"))) {
				String maxRecordStr = reportFunction.getAttributeValue("maxRecord");
				int maxRecord = Integer.MAX_VALUE;

				if (!GenericValidator.isBlankOrNull(maxRecordStr)) {
					maxRecord = Integer.valueOf(maxRecordStr);
				}
				rm = new FieldDBResult(dataMap);
				jdbcTemplate.query(sql.toString(), paramList.toArray(), rm);

				getTopRecord(dataMap, maxRecord);

			} else {
				jdbcTemplate.query(sql.toString(), paramList.toArray(), rm);
			}
		}

		return dataMap;
	}

	/***
	 * set the parametar which is n
	 * 
	 * @param parameters
	 */
	protected abstract void setParameter(Map<String, String> parameters);

	/**
	 * process the data
	 * 
	 * @param parameters
	 * @return
	 */
	protected ReportResultDTO processData(Map<String, String> parameters) {

		logger.debug(">>>>>> report result dto process data.");

		Map<String, Map<String, String>> dataMap = getData(parameters);

		logger.debug(">>>>>> dat:["+dataMap.toString()+"]");

		dataMap = filterData(dataMap, parameters);

		logger.debug(">>>>> filter data ---> dataMap:["+dataMap.toString()+"]");


		Map<String, String> data = getReturnData(dataMap, parameters);

		logger.debug(">>>>> return data:["+data.toString()+"]");

		/**
		 * { "success":true, "result_detail":{
		 * 		title:
		 * 		serials: [{
		 * 				title: "serial-1",
		 * 				data: [{x:0, y:1}, {x:1, y:10}, {x:2, y:30}, {x:3, y:22}...]
		 * 			}, {
		 * 				title: "serial-2",
		 * 				data: [{x:0, y:3}, {x:1, y:8}, {x:2, y:50}, {x:3, y:32}...]
		 * 			}]
		 * }}
		 */
		// xmlData="<chart yAxisName='Sales Figure' caption='Top 5 Performers in Sales' numberPrefix='$' useRoundEdges='1' logoURL='images/fusionchartslogo.png' logoPosition='TL' logoLink='n-http://www.fusioncharts.com/' showAboutMenuItem='1' aboutMenuItemLabel='FusionCharts' aboutMenuItemLink='n-http://www.fusioncharts.com/'>"
		// +"<set label='Alex' value='25000'  />"
		// +"<set label='Mark' value='35000' />"
		// +" <set label='David' value='42300' />"
		// +"<set label='Graham' value='35300' />"
		// +"<set label='John' value='31300' />"
		// +"</chart>";
		ReportResultDTO result = new ReportResultDTO();
		//	result.setXmlData(data.get("xml"));
		result.setJsonData(data.get("json"));
		logger.debug("xml----------------->"+result.getXmlData());
		logger.debug("json---------------->"+result.getJsonData());
		return result;
	}

	/**
	 * get the start date of report, the report is by day base on monthly
	 * 
	 * @return
	 */
	public int getStartDateForDayReport(int defaultDays) {
		Calendar c = Calendar.getInstance();
		return getStartDateForDayReport(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
				- defaultDays);
	}

	/**
	 * get the start date of report, the report is by day base on monthly
	 * 
	 * @return
	 */
	public int getStartDateForDayReport(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		return Integer.parseInt(df.format(c.getTime()));
	}

	/**
	 * get the start date of report, the report is by day base on monthly
	 * 
	 * @return
	 */
	protected int getStartDateForSeasonReport() {
		Calendar c = Calendar.getInstance();
		return getStartDateForSeasonReport(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * get the start date of report, the report is by day base on monthly
	 * 
	 * @return
	 */
	protected int getStartDateForSeasonReport(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_YEAR, -12 * 7);
		return Integer.parseInt(df.format(c.getTime()));
	}

	/**
	 * //n number of week0 current week, -1 last view ,1 next week
	 * 
	 * @param n
	 * @return
	 */
	public int getLastWeekStartDate(int n) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, n);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return Integer.parseInt(df.format(cal.getTime()).trim());
	}

	/**
	 * //n number of week0 current week, -1 last view ,1 next week
	 * 
	 * @param n
	 * @return
	 */
	public int getLastWeekEndDate(int n) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, n);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return Integer.parseInt(df.format(cal.getTime()).trim());
	}

	/**
	 * get the start date of report, the report is by day base on year
	 * 
	 * @return
	 */
	protected int getStartDateForMonthReport(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		return Integer.parseInt(df.format(c.getTime()).trim());
	}

	/**
	 * get the start date of report, the report is by day base on year
	 * 
	 * @return
	 */
	public int getStartDateForMonthReport() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONDAY, -11);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return getStartDateForMonthReport(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * get end date , the end date is current date by default
	 * 
	 * @return
	 */
	public int getEndDate() {
		Calendar c = Calendar.getInstance();
		// c.add(Calendar.DAY_OF_MONTH, -1);
		return Integer.parseInt(df.format(c.getTime()));
	}

	/**
	 * format the data
	 * 
	 * @param value
	 * @return
	 */
	public String formatData(Double value) {

		String decimals = reportFunction.getGraphAttributes().get("decimals");

		if (GenericValidator.isInt(decimals)) {
			int decimalNum = Integer.valueOf(decimals);
			String format = ".";
			for (int i = 0; i < decimalNum; i++) {
				format += "#";
			}
			logger.debug("format----------"+format);
			DecimalFormat decFormat = new DecimalFormat(format);
			return decFormat.format(value);
		} else {
			return numFormat.format(value);
		}

	}

	/**
	 * format the string data
	 */
	public String formatData(String value) {
		if (value == null || value.equals("")) {
			return formatData(0.00);
		}
		return formatData(Double.valueOf(value));
	}

	public Map<String, String> getReturnData(Map<String, Map<String, String>> dataMap, Map<String, String> parameters) {
		Map<String, String> retVal = new HashMap<String, String>();
		String xmlStr = null, jsonStr = null;

		if ("true".equalsIgnoreCase(reportFunction.getAttributeValue("isMs"))) {
			xmlStr = getMultipleXmlData(dataMap, parameters) ;
			jsonStr = getMultipleJsonData(dataMap, parameters);
		} else {
			if (getReportFieldList(parameters).size() > 1) {
				xmlStr = getMultipleXmlData(dataMap, parameters);
				jsonStr = getMultipleJsonData(dataMap, parameters);
			} else {
				xmlStr = getSimpleXmlData(dataMap, parameters);
				jsonStr = getSimpleJsonData(dataMap, parameters);
			}
		}

		retVal.put("xml", xmlStr);
		retVal.put("json", jsonStr);

		return retVal;		
	}


	protected String getSimpleJsonData(Map<String, Map<String, String>> dataMap, Map<String, String> parameters) {
		JSONObject serials = new JSONObject();

		Map<String, String> attrMap = reportFunction.getGraphAttributes();
		Iterator<String> attrKeys = attrMap.keySet().iterator();
		ReportField rptField = getReportFieldList(parameters).get(0);
		Iterator<String> keys = dataMap.keySet().iterator();

		JSONArray seriesNames = new JSONArray();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = null;

			if ( "2".equals(reportFunction.getAttributeValue("calType")) ) {
				value = dataMap.get(key).get("VALUE");
			} else {
				value = dataMap.get(key).get(rptField.getId());
			}

			attrMap = rptField.getGraphAttributes();
			attrKeys = attrMap.keySet().iterator();
			while (attrKeys.hasNext()) {
				String field = attrKeys.next();

				JSONObject serial = serials.getJSONObject(field);
				if ( serial==null || serial.isNullObject() ){
					serial = new JSONObject();
					serial.put("title", field);
					serials.put(field, serial);

					seriesNames.add(field);
				}

				JSONArray line = serial.getJSONArray("data");
				if ( line==null || line.isEmpty() ){
					line = new JSONArray();
					serial.put("data", line);
				}

				JSONObject point = new JSONObject();
				point.put("x", key);
				point.put("y", value);
				line.add(point);
			}
		}

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", reportFunction.getName());
		jsonObj.put("series_name", seriesNames);
		jsonObj.put("serials", serials);
		String retStr = jsonObj.toString();

		logger.debug(retStr);
		return retStr;
	}

	protected String getMultipleJsonData(Map<String, Map<String, String>> dataMap, Map<String, String> parameters) {
		JSONObject serials = new JSONObject();

		Map<String, String> attrMap = reportFunction.getGraphAttributes();
		Iterator<String> attrKeys = attrMap.keySet().iterator();

		TreeSet<String> dataKeys = new TreeSet<String>(dataMap.keySet());
		Iterator<String> keys = dataKeys.iterator();

		List<ReportField> rfList = getReportFieldList(parameters);
		JSONArray seriesNames = new JSONArray();
		for (int i = 0; i < rfList.size(); i++) {
			ReportField rptField = rfList.get(i);

			attrMap = rptField.getGraphAttributes();
			attrKeys = attrMap.keySet().iterator();

			keys = dataMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = dataMap.get(key).get(rptField.getId());

				JSONObject serial = serials.getJSONObject(rptField.getId());
				if ( serial==null || serial.isNullObject() ) {
					serial = new JSONObject();
					serial.put("id", rptField.getId());

					seriesNames.add(rptField.getId());
				}

				JSONArray line = null;
				Object obj = serial.get("data");
				if ( obj==null ) {
					line = new JSONArray();
				}else{
					line = JSONArray.fromObject(obj);
				}

				JSONObject point = new JSONObject();
				point.put("x", key);

				if ( GenericValidator.isBlankOrNull(value) ){
					point.put("y", 0);	
				} else {
					point.put("y", value);
				}
				line.add(point);
				logger.debug(line.toString());

				serial.put("data", line);
				serials.put(rptField.getId(), serial);
			}
		}
		//{startDate=null, reportFunction=userstatsbyhour, activeUser=activeUser, 
		//appId=2199, isCour=true, onlineUser=onlineUser, endDate=null, defaultDays=0, devId=14, newUser=newUser}
		//"dev_new_user","dev_user_active","dev_user_online"

		if(serials == null || serials.isEmpty()){
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<serials is null,get default data>>>>>>>>>>>>>>>>>>>>>>");
			StringBuffer buffer = getShowinfo(parameters);

			if(buffer != null && buffer.length() > 0){
				String isCour = parameters.get("isCour");
				if(isCour != null && isCour.equals("true")){
					parameters.put("startDate", null);
					parameters.put("endDate", null);
				}
				List listData = getDefaultData(parameters);
				String[] serStrings = buffer.toString().split(",");

				for(int i = 0; i < serStrings.length; i++){
					JSONObject object = new JSONObject();
					object.put("id", serStrings[i]);
					object.put("data", listData);
					serials.put(serStrings[i], object);
					seriesNames.add(serStrings[i]);
				}

			}

		} 
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>"+serials);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("title", reportFunction.getName());
		jsonObj.put("series", serials);
		jsonObj.put("series_name", seriesNames);
		String retStr = jsonObj.toString();

		logger.debug(retStr);
		return retStr;
	}

	//取要显示的项
	public StringBuffer getShowinfo(Map<String, String> parameters){
		StringBuffer buffer = new StringBuffer();
		if(parameters.get("opens") != null && !parameters.get("opens").equals("")){
			buffer.append("user_open_app").append(",");
		}

		if(parameters.get("pushes") != null && !parameters.get("pushes").equals("")){

			buffer.append("pushes_per_app").append(",");
		}
		if(parameters.get("times") != null && !parameters.get("times").equals("")){
			buffer.append("time_in_app");
		}

		if(parameters.containsKey("newUser") && parameters.get("newUser") != null){
			buffer.append("dev_new_user").append(",");
		}
		if(parameters.containsKey("activeUser") && parameters.get("activeUser") != null){
			buffer.append("dev_user_active").append(",");
		}
		if(parameters.containsKey("onlineUser") && parameters.get("onlineUser") != null){
			buffer.append("dev_user_online");
		}
		return buffer;
	}
	//取默认数据
	public List getDefaultData( Map<String, String> parameters){
		SimpleDateFormat dFormat = new SimpleDateFormat("dd");
		SimpleDateFormat mFormat = new SimpleDateFormat("MM");

		List<Map<String,  Object>> result = new ArrayList<Map<String,Object>>();
		int thisManday = getMonthDay(0);
		int nowDay = Integer.parseInt(dFormat.format(new Date()));
		int nowMonth = Integer.parseInt(mFormat.format(new Date()));
		String nowYear = yearDf.format(new Date());

		if(parameters.get("startDate")!= null && !parameters.get("startDate").equals("")){
			result = cursomDay(parameters.get("startDate"), parameters.get("endDate"));
		}else{
			if(parameters.get("reportFunction").equals("pushbyhour") 
					|| parameters.get("reportFunction").equals("userstatsbyhour")){

				for(int i = 1; i<=24; i++){
					//	if(i  % 2 == 0){
					Map map = new HashMap();
					map.put("x", i+":00");
					map.put("y", "0");
					result.add(map);
					//	}
				}
			}
			else if(parameters.get("reportFunction").equals("pushbyday") 
					|| parameters.get("reportFunction").equals("userstatsbyday")){ //pushbymonth
				int day =Integer.parseInt(parameters.get("defaultDays"));

				if(day == 7){//   4
					day = getWeekDay();
					int start = nowDay - (day-1) ;
					int end = start+7;

					int j = 0;
					for (int i =start; i<end; i++){
						Map map = new HashMap();
						if(i  > thisManday){
							j++;
							int mon = nowMonth + 1 > 12 ? 1: nowMonth+1;
							map.put("x", nowYear + getMonthString(mon) + (j<10?("0"+j):j));
							map.put("y", "0");
						}else{
							map.put("x",nowYear + getMonthString(nowMonth)+(i));	
							map.put("y", "0");
						}

						result.add(map);
					}
				}
				else if(day == 14){

					String[] ymd = getLastOneWeek(1);
					day = Integer.parseInt(ymd[2]);
					int mon = Integer.parseInt(ymd[1]);

					int lastMon = getMonthDay(1);
					int j = 1;
					for(int i = day; i<(day+7); i++){
						Map map = new HashMap();
						if(i > lastMon){
							map.put("x", nowYear +  getMonthString(mon+1) +j);
							map.put("y", "0");
							j++;
						}else{
							map.put("x", nowYear + getMonthString(mon) +i);
							map.put("y", "0");
						}
						result.add(map);
					}
				}
				else if(day == 30 ){
					day = getMonthDay(0);
					for(int i =1; i<=day; i++){
						if(i % 1 == 0){
							Map map = new HashMap();
							map.put("x", nowYear + getMonthString(nowMonth) + i);
							map.put("y", "0");
							result.add(map);
						}
					}
				}
				else if(day == 31){
					day = getMonthDay(1);
					for(int i =1; i<=day; i++){
						if(i % 1 == 0){
							Map map = new HashMap();
							map.put("x", nowYear + getMonthString(nowMonth-1) +i);
							map.put("y", "0");
							result.add(map);
						}
					}
				}
			}

			if(parameters.get("reportFunction").equals("pushbymonth") 
					|| parameters.get("reportFunction").equals("userstatsbymonth")){

				for(int i =1; i<=12; i++){
					Map map = new HashMap();
					map.put("x",nowYear + i);
					map.put("y", "0");
					result.add(map);

				}
			}
		}
		return result;
	}
	private static String getMonthString(int month){

		String monthString = (month < 10 ? ("0"+month):String.valueOf(month));
		return monthString;
	}
	protected String getSimpleXmlData(Map<String, Map<String, String>> dataMap, Map<String, String> parameters) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<graph ");
		Map<String, String> attrMap = reportFunction.getGraphAttributes();
		Iterator<String> attrKeys = attrMap.keySet().iterator();
		double yMinValue = 0;
		double yMaxValue = 0;
		while (attrKeys.hasNext()) {
			String key = attrKeys.next();
			buffer.append(key).append("='").append(attrMap.get(key)).append("' ");
		}
		String customizeYaxisValue = reportFunction.getAttributeValue("customizeYaxisValue");
		buffer.append("labelStep='").append(getStepLabelNumber(dataMap.size())).append("' ");
		if (!"false".equals(customizeYaxisValue)) {
			buffer.append("yAxisMinValue='{0}' ");
			buffer.append("yAxisMaxValue='{1}'");
			buffer.append(">");
		}
		if (!GenericValidator.isBlankOrNull(reportFunction.getStyle())) {
			buffer.append(reportFunction.getStyle());
		}
		if (!GenericValidator.isBlankOrNull(reportFunction.getAppend())) {
			buffer.append(reportFunction.getAppend());
		}
		ReportField rptField = getReportFieldList(parameters).get(0);
		Iterator<String> keys = dataMap.keySet().iterator();
		int i = 0;
		while (keys.hasNext()) {
			String key = keys.next();
			String value = "";
			if ("2".equals(reportFunction.getAttributeValue("calType"))) {
				value = dataMap.get(key).get("VALUE");
			} else {
				value = dataMap.get(key).get(rptField.getId());
			}
			if (GenericValidator.isDouble(value)) {
				double dValue = Double.valueOf(value);
				if (dValue < yMinValue || yMinValue == 0) {
					yMinValue = dValue;
				}
				if (dValue > yMaxValue) {
					yMaxValue = dValue;
				}
			} else {
				value = "";
			}
			buffer.append("<set name='").append(key).append("' value='").append(value).append("'");
			buffer.append(" color='").append(getColor(i++)).append("' ");
			attrMap = rptField.getGraphAttributes();
			attrKeys = attrMap.keySet().iterator();
			while (attrKeys.hasNext()) {
				key = attrKeys.next();

				buffer.append(key).append("='").append(attrMap.get(key)).append("' ");
			}
			buffer.append("/>");
		}

		buffer.append("</graph>");
		String returnStr = buffer.toString();

		if (yMinValue == yMaxValue) {
			yMinValue = yMinValue * 0.5;
			yMaxValue = yMaxValue * 1.5;
		}

		returnStr = returnStr.replaceFirst("\\{0\\}", String.valueOf(getYMinValue(yMinValue,dataMap.size())));
		returnStr = returnStr.replaceFirst("\\{1\\}", String.valueOf(getYMaxvalue(yMaxValue,dataMap.size())));
		logger.debug(returnStr);
		return returnStr;
	}

	protected String getMultipleXmlData(Map<String, Map<String, String>> dataMap, Map<String, String> parameters) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<graph ");
		Map<String, String> attrMap = reportFunction.getGraphAttributes();
		Iterator<String> attrKeys = attrMap.keySet().iterator();
		while (attrKeys.hasNext()) {
			String key = attrKeys.next();
			buffer.append(key).append("='").append(attrMap.get(key)).append("' ");
		}
		String customizeYaxisValue = reportFunction.getAttributeValue("customizeYaxisValue");
		buffer.append("labelStep='").append(getStepLabelNumber(dataMap.size())).append("' ");

		if (!"false".equals(customizeYaxisValue)) {
			buffer.append("yAxisMinValue='{0}' ");
			buffer.append("yAxisMaxValue='{1}'");
		}
		buffer.append(">");
		double yMinValue = 0;
		double yMaxValue = 0;
		if (!GenericValidator.isBlankOrNull(reportFunction.getStyle())) {
			buffer.append(reportFunction.getStyle());
		}

		if (!GenericValidator.isBlankOrNull(reportFunction.getAppend())) {
			buffer.append(reportFunction.getAppend());
		}

		buffer.append(" <categories font='Arial' fontSize='11' fontColor='000000'>");
		TreeSet<String> dataKeys = new TreeSet<String>(dataMap.keySet());
		Iterator<String> keys = dataKeys.iterator();
		while (keys.hasNext()) {
			buffer.append(" <category name='").append(keys.next()).append("'/>");
		}
		buffer.append(" </categories>");
		List<ReportField> rfList = getReportFieldList(parameters);
		for (int i = 0; i < rfList.size(); i++) {
			ReportField rptField = rfList.get(i);

			buffer.append("<dataset seriesname='").append(rptField.getName()).append("' ");
			buffer.append("color='").append(getColor(i)).append("' ");
			attrMap = rptField.getGraphAttributes();
			attrKeys = attrMap.keySet().iterator();
			while (attrKeys.hasNext()) {
				String key = attrKeys.next();
				buffer.append(key).append("='").append(attrMap.get(key)).append("' ");
			}
			buffer.append(">");
			keys = dataMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = dataMap.get(key).get(rptField.getId());
				if (GenericValidator.isDouble(value)) {
					double dValue = Double.valueOf(value);
					if (dValue < yMinValue || yMinValue == 0) {
						yMinValue = dValue;
					}
					if (dValue > yMaxValue) {
						yMaxValue = dValue;
					}

					buffer.append("<set name='").append(key).append("' value='").append(value).append("'");

					buffer.append("/>");
				} else {
					buffer.append("<set name='").append(key).append("' value=''");
					buffer.append("/>");
				}
			}
			buffer.append("</dataset>");
		}
		buffer.append("</graph>");
		String returnStr = buffer.toString();
		returnStr = returnStr.replaceFirst("\\{0\\}", String.valueOf(getYMinValue(yMinValue,dataMap.size())));
		returnStr = returnStr.replaceFirst("\\{1\\}", String.valueOf(getYMaxvalue(yMaxValue,dataMap.size())));
		logger.debug(returnStr);
		return returnStr;
	}

	public String getColor(int index) {

		String[] colors = new String[] { "D64646", "8E468E", "588526", "F1683C", "C9198D", "4567AA", "DBDC25",
				"008ED6", "008E8E", "AFD8F8", "F6BD0F", "8BBA00", "FF8E46", "B3AA00", "9D080D", "A186BE", "99CC00",
				"F47E00", "FF5904", "99CC99", "839F2F", "56B9F9", "FDC12E", "1D8BD1", "2AD62A", "B1D1DC", "C8A1D1",
				"3366FF", "A1B1C1", "B2C2D2", "C3D3E3" };
		return colors[index % colors.length];
	}

	protected class FieldDBResult implements RowMapper {

		private Map<String, Map<String, String>> dataMap;

		public FieldDBResult(Map<String, Map<String, String>> dataMap) {
			this.dataMap = dataMap;
		}

		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			String kpiField = rs.getString("KPI_FIELD");

			String value = formatData(rs.getString("KPI_VALUE"));
			Map<String, String> data = dataMap.get(kpiField);
			if (data == null) {
				data = new HashMap<String, String>();
				dataMap.put(kpiField, data);
			}
			if (data.containsKey("VALUE")) {
				String tmpValue = data.get("VALUE");
				data.put("VALUE", formatData(Double.valueOf(tmpValue) + Double.valueOf(value)));
			} else {
				data.put("VALUE", value);
			}
			return null;

		}

	}

	protected class DateDBResult implements RowMapper {

		private Map<String, Map<String, String>> dataMap;

		public DateDBResult(Map<String, Map<String, String>> dataMap) {
			this.dataMap = dataMap;
		}

		public Object mapRow(ResultSet rs, int arg1) throws SQLException {
			String date = rs.getString("IDATE");
			String kpiField = rs.getString("KPI_FIELD");
			String value = formatData(rs.getString("KPI_VALUE"));

			Map<String, String> data = dataMap.get(date);
			if (data == null) {
				data = new HashMap<String, String>();
				dataMap.put(date, data);
			}

			data.put(kpiField, value);
			return data;

		}

	}

	/**
	 * get top record
	 * 
	 * @param dataMap
	 *            the data map
	 * @param maxRecord
	 *            max record return
	 */
	protected void getTopRecord(Map<String, Map<String, String>> dataMap, int maxRecord) {
		List<Map.Entry<String, Map<String, String>>> info = new ArrayList<Map.Entry<String, Map<String, String>>>(
				dataMap.entrySet());
		Collections.sort(info, new Comparator<Map.Entry<String, Map<String, String>>>() {
			public int compare(Map.Entry<String, Map<String, String>> obj1, Map.Entry<String, Map<String, String>> obj2) {
				String v1 = obj1.getValue().get("VALUE");
				if (v1 == null || v1.equals("")) {
					v1 = "0";
				}
				String v2 = obj2.getValue().get("VALUE");
				if (v2 == null || v2.equals("")) {
					v2 = "0";
				}
				double dv1 = Double.valueOf(v1);
				double dv2 = Double.valueOf(v2);
				if (dv1 > dv2) {
					return -1;
				} else if (dv1 == dv2) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		Map<String, Map<String, String>> dataTmp = new LinkedHashMap<String, Map<String, String>>();
		maxRecord = info.size() > maxRecord ? maxRecord : info.size();
		logger.debug("record return,size=" + maxRecord);
		for (int i = 0; i < maxRecord; i++) {
			Entry<String, Map<String, String>> entry = info.get(i);
			dataTmp.put(entry.getKey(), entry.getValue());
		}
		dataMap.clear();
		dataMap.putAll(dataTmp);

	}

	protected List<ReportField> getReportFieldList(Map<String, String> parameters) {
		return reportFunction.getReportFieldList();
	}

	protected double getYMinValue(double defaultValue, Integer dataSize) {
		if (dataSize == 1) {
			return 0;
		} else {
			return defaultValue;
		}
	}

	protected double getYMaxvalue(double defaultValue, Integer dataSize) {
		return defaultValue;
	}

	/**
	 * Print SQL with param
	 * 
	 * @param sql
	 * @param params
	 * @param seperator
	 * @return
	 */
	public static String assembleSQL(String sql, Object[] params) {
		String seperator = "?";
		StringBuffer retValue = new StringBuffer();
		try {
			if ((sql != null) && (sql.length() > 0)) {
				retValue.append(sql);
				if ((params != null) && (params.length > 0)) {
					for (int i = 0; i < params.length; i++) {
						int pos1 = retValue.indexOf(seperator);
						if (params[i] == null) {
							retValue.replace(pos1, pos1 + seperator.length(), "");
						} else if (params[i] instanceof Date) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = (Date) params[i];
							retValue.replace(pos1, pos1 + seperator.length(), "to_date('" + sdf.format(date)
									+ "', 'yyyy-mm-dd hh24:mi:ss')");
						} else if (params[i] instanceof String)
							retValue.replace(pos1, pos1 + seperator.length(), "'" + params[i].toString() + "'");
						else
							retValue.replace(pos1, pos1 + seperator.length(), params[i].toString());
					}
				}
			}
		} catch (Exception ex) {
		}
		return retValue.toString();
	}

	/**
	 * Print SQL with param
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String assembleQueryString(Map map) {
		StringBuffer result = new StringBuffer();

		for (Object key : map.keySet()) {
			/*
			 * if (result.length()==0){ result.append("?"); }else{
			 * result.append("&"); }
			 */
			result.append("&");
			result.append(key).append("=").append(map.get(key));
		}
		return result.toString();
	}

	protected Map<String, Map<String, String>> filterData(Map<String, Map<String, String>> data,
			Map<String, String> parameters) {
		List<ReportField> fieldList = getReportFieldList(parameters);
		try {
			Date startDate = df.parse(parameters.get(Constants.RPT_PARAM_START_DATE));
			Date endDate = df.parse(parameters.get(Constants.RPT_PARAM_END_DATE));
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			Map<String, Map<String, String>> dataTemp = new HashMap<String, Map<String, String>>();
			while (endDate.compareTo(c.getTime()) >= 0) {
				String dateStr = df.format(c.getTime());
				Map<String, String> dailyData = data.get(dateStr);
				if (dailyData == null) {
					dailyData = new HashMap<String, String>();
				}
				dataTemp.put(dateStr, dailyData);
				for (ReportField reportField : fieldList) {
					if (dailyData.get(reportField.getId()) == null) {
						dailyData.put(reportField.getId(), "0");
					}
				}
				c.add(Calendar.DAY_OF_YEAR, 1);
			}
			return dataTemp;
		} catch (Exception e) {

		}
		return data;
	}

	public int getDays(String startDateStr, String endDateStr) {
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date startDate = df.parse(startDateStr);
			Date endDate = df.parse(endDateStr);
			int days = (int) (((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000)) + 1);
			return days;
		} catch (Exception e) {

		}
		return 0;
	}

	protected int getStepLabelNumber(int dataSize) {
		if (dataSize > 20) {
			return 3;
		} else if (dataSize > 10) {
			return 2;
		} else {
			return 1;
		}
	}


	//得到今天是星期几
	public static int getWeekDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar.get(Calendar.DAY_OF_WEEK)-1;
	}

	//取得上,月数，周一是几号
	public static String[] getLastOneWeek(int weeks){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -(weeks*7));
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String data = sfm.format(calendar.getTime());
		String[] ymd = data.split(",");
		return ymd;
	}

	//取得当前月
	public int getMonth(){
		Calendar calendar = Calendar.getInstance();   
		return calendar.get(Calendar.MONTH)+1;
	}
	//取得当前月
	public static int getYear(){
		Calendar calendar = Calendar.getInstance();   
		return calendar.get(Calendar.YEAR);
	}

	//得到上个月的最后一天
	public static  int getMonthDay(int m){

		Calendar calendar = Calendar.getInstance();    
		int month = calendar.get(Calendar.MONTH);  
		calendar.set(Calendar.MONTH, month-m);  
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));    
		Date strDateTo = calendar.getTime();    

		return Integer.parseInt(dformat.format(strDateTo));

	}

	public static String[] getYMD(String date){
		String[] ares = new String[3];
		ares[0] = date.substring(0,4);
		ares[1] = date.substring(4,6);
		ares[2] = date.substring(6);
		return ares;
	}

	public static List<Map<String, Object>> cursomDay(String sartTime,String endTime){
		String[] startDate = getYMD(sartTime);
		String[] endDate = getYMD(endTime); 

		int sYear = Integer.parseInt(startDate[0]);
		int sMonth = Integer.parseInt(startDate[1]);
		int sDay = Integer.parseInt(startDate[2]);
		int eYear = Integer.parseInt(endDate[0]);
		int eMonth = Integer.parseInt(endDate[1]);
		int eDay = Integer.parseInt(endDate[2]);

		List<Map<String, Object>> resultsList = new ArrayList<Map<String,Object>>();
		Map<String, Object> sourMap = new HashMap<String, Object>();
		sourMap.put("x", endTime);
		sourMap.put("y", "0");

		//按天算
		if((sYear == eYear) && (sMonth == eMonth)){
			String daysString = sYear+""+sMonth;
			for(int i = sDay; i<=eDay; i++){
				Map map = new HashMap();
				map.put("x", daysString+i);
				map.put("y", "0");
				resultsList.add(map);
			}

		}else if((sYear == eYear)){
			String daysString = "";
			int count = 0;
			for(int i = sMonth; i < eMonth; i++){
				logger.debug("i="+i);
				daysString = sYear+""+(i<10 ? "0"+i:i);
				int j = sDay+count;
				if(j>= eDay) break;
				for(; j < eDay; j++){
					daysString  += (j<10  ? "0"+j : j);
					break;
				}
				count = j++;
				logger.debug(daysString);
				Map map = new HashMap();
				map.put("x", daysString);
				map.put("y", "0");
				resultsList.add(map);
			}


		}else{
			Map<String, Object> startSourMap = new HashMap<String, Object>();
			startSourMap.put("x", sartTime);
			startSourMap.put("y", "0");
			resultsList.add(startSourMap);
		}

		resultsList.add(sourMap);
		return resultsList;

	}
	public static void main(String[] args) {
		System.err.println(getMonthDay(1));
	}


}
