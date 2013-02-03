package com.kkpush.report.graph.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.GenericValidator;

import com.kkpush.report.common.util.ReportField;
import com.kkpush.util.Constants;

public class PushResultHourlyImpl extends AbstractReportImpl {

	@Override
	protected String getDetailSql(List<Object> paramList, Map<String, String> parameters) {
		StringBuffer sql = new StringBuffer();
		sql.append("select if(ihour<10,concat('0',ihour,':00'),concat(ihour,':00')) as IDATE,KPI_CODE as KPI_FIELD,SUM(KPI_VALUE) as KPI_VALUE ");
		sql.append("from t_dev_kpi_hour  where dev_id=? and field=? ");
		sql.append("and kpi_code =?  and idate=? group by IHOUR");
		paramList.add(parameters.get(Constants.RPT_PARAM_DEV_ID));
		paramList.add(parameters.get(Constants.RPT_PARAM_APP_ID));
		paramList.add(parameters.get(Constants.RPT_PARAM_KPI_FIELD_ID));
		paramList.add(Integer.valueOf(parameters.get(Constants.RPT_PARAM_START_DATE)));
		return sql.toString();
	}

	@Override
	protected void setParameter(Map<String, String> parameters) {
		String startDateStr = (String) parameters.get(Constants.RPT_PARAM_START_DATE);
		if (!GenericValidator.isBlankOrNull(startDateStr)) {
			parameters.put(Constants.RPT_PARAM_START_DATE, startDateStr);
			parameters.put(Constants.RPT_PARAM_END_DATE, startDateStr);
		} else if (GenericValidator.isInt(parameters.get(Constants.RPT_PARAM_DEFAULT_DAYS))) {
			int defaultDays = Integer.valueOf(parameters.get(Constants.RPT_PARAM_DEFAULT_DAYS));
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, 0 - defaultDays);
			parameters.put(Constants.RPT_PARAM_START_DATE, df.format(c.getTime()));
			parameters.put(Constants.RPT_PARAM_END_DATE, df.format(c.getTime()));
		} else {
			Calendar c = Calendar.getInstance();
			parameters.put(Constants.RPT_PARAM_START_DATE, df.format(c.getTime()));
			parameters.put(Constants.RPT_PARAM_END_DATE, df.format(c.getTime()));
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
		return data;

	}
}
