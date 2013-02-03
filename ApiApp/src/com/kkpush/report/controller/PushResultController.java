package com.kkpush.report.controller;

import java.util.ArrayList;
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
import com.kkpush.report.service.PushResultService;
import com.kkpush.web.controller.PublicController;

@Controller
@RequestMapping("/report")
public class PushResultController extends PublicController {
	private static Logger logger = LoggerFactory.getLogger(PushResultController.class);
	@Autowired
	PushResultService pushResultService;

	@RequestMapping(value = "/systemPushDailyReport", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> systemPushDailyReport(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = (Developer) request.getSession().getAttribute("user");
		String devId = dev.getDevId().toString();
		String pageStr = request.getParameter("pageIndex");
		String rowStr = request.getParameter("pageSize");
		if (GenericValidator.isBlankOrNull(pageStr)) {
			pageStr = "1";
		}
		if (GenericValidator.isBlankOrNull(rowStr)) {
			rowStr = "10";
		}
		int page = Integer.valueOf(pageStr);
		int row = Integer.valueOf(rowStr);
		String endDate = getEndDate(request);
		String beginDate = getStartDate(request);
		String appId = request.getParameter("appId");
		try {
			// 获得统计集合
			if (!devId.equals("")) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				param.put("appId", appId);
				param.put("startIndex", (page - 1) * row);
				param.put("endIndex", row);
				param.put("endDate", endDate);
				param.put("startDate", beginDate);
				List<Map<String, Object>> dataList = pushResultService.getSystemPushDataList(param);
				int total = pushResultService.getSystemPushDataCount(param);
				responseMap.put("total", total);
				responseMap.put("rows", dataList);
			} else {
				logger.debug("1-------");
			}
			// ArrayList<App> alist = new ArrayList<App>();
			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}

	@RequestMapping(value = "/customizePushDailyReport", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> customizePushDailyReport(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Developer dev = (Developer) request.getSession().getAttribute("user");
		String devId = dev.getDevId().toString();
		String pageStr = request.getParameter("pageIndex");
		String rowStr = request.getParameter("pageSize");
		if (GenericValidator.isBlankOrNull(pageStr)) {
			pageStr = "1";
		}
		if (GenericValidator.isBlankOrNull(rowStr)) {
			rowStr = "10";
		}
		int page = Integer.valueOf(pageStr);
		int row = Integer.valueOf(rowStr);
		String endDate = getEndDate(request);
		String beginDate = getStartDate(request);
		String appId = request.getParameter("appId");
		String adId = request.getParameter("adId");
		String adTitle = request.getParameter("adTitle");
		if (!GenericValidator.isBlankOrNull(adTitle)) {
			adTitle = "%" + adTitle + "%";
		}
		try {
			// 获得统计集合
			if (!devId.equals("")) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("devId", devId);
				param.put("appId", appId);
				param.put("adId", adId);
				param.put("adTitle", adTitle);
				param.put("startIndex", (page - 1) * row);
				param.put("endIndex", row);
				param.put("endDate", endDate);
				param.put("startDate", beginDate);
				int total = pushResultService.getCustomizedPushDataCount(param);
				responseMap.put("total", total);
				if (total > 0) {
					List<Map<String, Object>> dataList = pushResultService.getCustomizePushDataList(param);
					responseMap.put("rows", dataList);
				} else {
					responseMap.put("rows", new ArrayList<Object>());
				}
			} else {
				logger.debug("1-------");
			}

			// ArrayList<App> alist = new ArrayList<App>();

			return responseMap;
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("info", e.getClass() + ":" + e.getMessage());
			return responseMap;
		}
	}

}
