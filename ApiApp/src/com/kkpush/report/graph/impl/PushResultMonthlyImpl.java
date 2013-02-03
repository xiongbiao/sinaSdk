package com.kkpush.report.graph.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import com.kkpush.report.common.util.ReportField;
import com.kkpush.util.Constants;

public class PushResultMonthlyImpl extends AbstractReportImpl {

	@Override
	protected String getDetailSql(List<Object> paramList, Map<String, String> parameters) {
		StringBuffer sql = new StringBuffer();
		sql.append("select IDATE div 100 as IDATE,KPI_CODE as KPI_FIELD,SUM(KPI_VALUE) as KPI_VALUE ");
		sql.append("from t_dev_kpi_day  where dev_id=? and field=? ");
		sql.append("and kpi_code =?  and idate between ? and ? group by IDATE div 100");
		paramList.add(parameters.get(Constants.RPT_PARAM_DEV_ID));
		paramList.add(parameters.get(Constants.RPT_PARAM_APP_ID));
		paramList.add(parameters.get(Constants.RPT_PARAM_KPI_FIELD_ID));
		paramList.add(Integer.valueOf(parameters.get(Constants.RPT_PARAM_START_DATE)));
		paramList.add(Integer.valueOf(parameters.get(Constants.RPT_PARAM_END_DATE)));
		return sql.toString();
	}

	@Override
	protected void setParameter(Map<String, String> parameters) {
		String startDateStr = (String) parameters.get(Constants.RPT_PARAM_START_DATE);
		String endDateStr = (String) parameters.get(Constants.RPT_PARAM_END_DATE);
		int defaultDays = 31;
		String defaultDayStr = reportFunction.getAttributeValue("defaultDays");
		String queryDays = reportFunction.getAttributeValue("queryDays");
		if ("1".equals(queryDays)) {
			setSimpleQueryDay(parameters);
			return;
		}
		if (GenericValidator.isInt(parameters.get(Constants.RPT_PARAM_DEFAULT_DAYS))) {
			defaultDays = Integer.valueOf(parameters.get(Constants.RPT_PARAM_DEFAULT_DAYS));
		} else if (GenericValidator.isInt(defaultDayStr)) {
			defaultDays = Integer.valueOf(defaultDayStr);
		}

		if (GenericValidator.isBlankOrNull(startDateStr) && GenericValidator.isBlankOrNull(endDateStr)) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.add(Calendar.MONTH, 0 - (defaultDays / 30));
			int startDate = getStartDateForDayReport(c.get(Calendar.YEAR), 0, 1);
			parameters.put(Constants.RPT_PARAM_START_DATE, String.valueOf(startDate));
			parameters.put(Constants.RPT_PARAM_END_DATE, String.valueOf(getEndDate()));

		} else if (GenericValidator.isBlankOrNull(startDateStr)) {
			try {
				Date endDate = df.parse(endDateStr);
				Calendar c = Calendar.getInstance();
				c.setTime(endDate);
				c.add(Calendar.DAY_OF_MONTH, -defaultDays);
				int iStartDate = getStartDateForDayReport(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));

				parameters.put(Constants.RPT_PARAM_START_DATE, String.valueOf(iStartDate));
				parameters.put(Constants.RPT_PARAM_END_DATE, df.format(endDate));
			} catch (Exception e) {

			}
		} else if (GenericValidator.isBlankOrNull(endDateStr)) {
			try {
				Date startDate = df.parse(startDateStr);

				parameters.put(Constants.RPT_PARAM_START_DATE, df.format(startDate));
				parameters.put(Constants.RPT_PARAM_END_DATE, String.valueOf(getEndDate()));
			} catch (Exception e) {

			}
		} else {
			try {
				Date startDate = df.parse(startDateStr);
				Date endDate = df.parse(endDateStr);
				parameters.put(Constants.RPT_PARAM_START_DATE, df.format(startDate));
				parameters.put(Constants.RPT_PARAM_END_DATE, df.format(endDate));
			} catch (Exception e) {

			}
		}

	}

	/**
	 * 只有一个日期的查询
	 * 
	 * @param parameters
	 */
	protected void setSimpleQueryDay(Map<String, String> parameters) {
		String startDateStr = (String) parameters.get(Constants.RPT_PARAM_START_DATE);
		if (GenericValidator.isBlankOrNull(startDateStr)) {
			parameters.put(Constants.RPT_PARAM_START_DATE, String.valueOf(getStartDateForDayReport(1)));
			parameters.put(Constants.RPT_PARAM_END_DATE, String.valueOf(getStartDateForDayReport(1)));
		} else {
			parameters.put(Constants.RPT_PARAM_START_DATE, startDateStr);
			parameters.put(Constants.RPT_PARAM_END_DATE, startDateStr);
		}
	}

	@Override
	protected List<ReportField> getReportFieldList(Map<String, String> parameters) {
		List<ReportField> rptFieldList = new ArrayList<ReportField>();
		
		ReportField rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_PUSHES));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_OPENS));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_TIMES));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_NEWUSER));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_ACTIVEUSER));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		rptField = reportFunction.getReportFieldByAlias(parameters.get(Constants.RPT_PARAM_ONLINEUSER));
		if (rptField != null) {
			rptFieldList.add(rptField);
		}
		return rptFieldList;
	}
	
	@Override
	protected Map<String, Map<String, String>> filterData(Map<String, Map<String, String>> data,
			Map<String, String> parameters) {
		
		return data; /*
		List<ReportField> fieldList = getReportFieldList(parameters);
		try {
			Date startDate = df.parse(parameters.get(Constants.RPT_PARAM_START_DATE));
			Date endDate = df.parse(parameters.get(Constants.RPT_PARAM_END_DATE));
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			Map<String, Map<String, String>> dataTemp = new HashMap<String, Map<String, String>>();
			while (endDate.compareTo(c.getTime()) >= 0) {
				String dateStr = monthDf.format(c.getTime());
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
				c.add(Calendar.MONTH, 1);
			}
			return dataTemp;
		} catch (Exception e) {

		}
		return data;*/
	}
}
