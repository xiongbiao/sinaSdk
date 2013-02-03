package com.kkpush.report.common.util;

import java.util.Map;

import com.kkpush.report.common.model.ReportResultDTO;

/**
 * Interface Exportable If the Business class should use for importing data, it
 * should implements this interface.
 * 
 * @author Martin Liu
 * 
 */
public interface Reportable {

	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * Parse data, and save to DB
	 * 
	 * @param reader
	 * @param tsID
	 * @throws Exception
	 */
	public ReportResultDTO generateReport(Map<String, String> parameters) throws Exception;

}
