package com.kkpush.report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkpush.account.domain.Developer;
import com.kkpush.report.common.model.ReportResultDTO;
import com.kkpush.report.common.util.ReportUtil;
import com.kkpush.report.service.ReportService;
import com.kkpush.util.Constants;
import com.kkpush.web.controller.PublicController;
import com.sun.accessibility.internal.resources.accessibility;

@Controller
@RequestMapping("/report")
public class ReportController extends PublicController {
	@Autowired
	ReportService reportService;

	public static Logger logger = LoggerFactory.getLogger(ReportController.class);

	@RequestMapping(value = "/systemPushHourlyReport", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> hourlyReport(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = new HashMap<String, String>();
		Developer user = (Developer) request.getSession().getAttribute("user");
		String devId = user.getEmail();
		String appId = request.getParameter("appId");
		if (GenericValidator.isBlankOrNull(appId)) {
			appId = "all";
		}
		String pageStr = request.getParameter("pageIndex");
		String rowStr = request.getParameter("pageSize");
		if (GenericValidator.isBlankOrNull(pageStr)) {
			pageStr = "1";
		}
		if (GenericValidator.isBlankOrNull(rowStr)) {
			rowStr = "15";
		}
		String startDate = getStartDate(request);

		params.put("DEV_ID", devId);
		params.put("APP_ID", appId);
		params.put("PAGE_NO", pageStr);
		params.put("PAGE_SIZE", rowStr);
		params.put("START_DATE", startDate);

		Map<String, Object> result = reportService.getData(params);
		return result;
	}

	@RequestMapping(value = "/dailyReport", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> dailyReport(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("调试信息");
		logger.info("重要提示的信息");
		logger.warn("警告信息");
		logger.error("错误信息");
		return null;
	}
	
	@RequestMapping(value = "/viewReportCommon")
	public @ResponseBody String viewReportCommon(HttpServletRequest request, HttpServletResponse response) {
		Developer user = (Developer) request.getSession().getAttribute("user");
		Map<String, String> parameters = new HashMap<String, String>();
		
		Integer devId = user.getDevId();
		String appId = request.getParameter("appId");
		String function = request.getParameter(Constants.REPORT_FUNCTION);
		String startDate = request.getParameter(Constants.RPT_PARAM_START_DATE);
		String endDate = request.getParameter(Constants.RPT_PARAM_END_DATE);
		String defaultDays = request.getParameter(Constants.RPT_PARAM_DEFAULT_DAYS);
		String newUser = request.getParameter(Constants.RPT_PARAM_NEWUSER);
		String onlineUser = request.getParameter(Constants.RPT_PARAM_ONLINEUSER);
		String activeUser = request.getParameter(Constants.RPT_PARAM_ACTIVEUSER);
		if(startDate == null || startDate.equals("")) 
			parameters.put("isCour", "true");
		
		
		parameters.put(Constants.RPT_PARAM_APP_ID, appId);
		parameters.put(Constants.RPT_PARAM_DEV_ID, String.valueOf(devId));
		parameters.put(Constants.REPORT_FUNCTION, function);
		parameters.put(Constants.RPT_PARAM_START_DATE, startDate);
		parameters.put(Constants.RPT_PARAM_END_DATE, endDate);
		parameters.put(Constants.RPT_PARAM_DEFAULT_DAYS, defaultDays);
		parameters.put(Constants.RPT_PARAM_NEWUSER, newUser);
		parameters.put(Constants.RPT_PARAM_ONLINEUSER, onlineUser);
		parameters.put(Constants.RPT_PARAM_ACTIVEUSER, activeUser);
		logger.debug("viewReportCommon param-------------------------------------------------"+parameters.toString());
		logger.debug("viewReportCommon params:["+parameters.toString()+"]");
		
		try {
			ReportResultDTO result  = ReportUtil.getInstance().generateReport(parameters);
			return result.getJsonData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//通知报表
	@RequestMapping(value = "/viewReport")
	public @ResponseBody String viewReport(HttpServletRequest request, HttpServletResponse response) {
		Developer user = (Developer) request.getSession().getAttribute("user");
		Integer devId = user.getDevId();
		String appId = request.getParameter("appId");
		String function = request.getParameter(Constants.REPORT_FUNCTION);
		String startDate = request.getParameter(Constants.RPT_PARAM_START_DATE);
		String endDate = request.getParameter(Constants.RPT_PARAM_END_DATE);
		String pushes = request.getParameter(Constants.RPT_PARAM_PUSHES);
		String opens = request.getParameter(Constants.RPT_PARAM_OPENS);
		String times = request.getParameter(Constants.RPT_PARAM_TIMES);
		String defaultDays = request.getParameter(Constants.RPT_PARAM_DEFAULT_DAYS);
	
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(Constants.RPT_PARAM_APP_ID, appId);
		parameters.put(Constants.RPT_PARAM_DEV_ID, String.valueOf(devId));
		parameters.put(Constants.REPORT_FUNCTION, function);
		parameters.put(Constants.RPT_PARAM_START_DATE, startDate);
		parameters.put(Constants.RPT_PARAM_END_DATE, endDate);
		parameters.put(Constants.RPT_PARAM_PUSHES, pushes);
		parameters.put(Constants.RPT_PARAM_OPENS, opens);
		parameters.put(Constants.RPT_PARAM_TIMES, times);
		parameters.put(Constants.RPT_PARAM_DEFAULT_DAYS, defaultDays);
		if(startDate == null || startDate.equals("")) 
			parameters.put("isCour", "true");
		
		logger.debug("params-------------------------------------------------"+parameters.toString());
		logger.debug("report controller params:["+parameters.toString()+"]");
		
		try {
			ReportResultDTO result  = ReportUtil.getInstance().generateReport(parameters);
//			Map<String,Object> data = new HashMap<String, Object>();
//			data.put("data", result.getJsonData());
			return result.getJsonData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
