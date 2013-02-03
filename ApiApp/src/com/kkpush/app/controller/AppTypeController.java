package com.kkpush.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kkpush.app.domain.AppType;
import com.kkpush.app.service.AppTypeService;
@Controller
@RequestMapping("/appType")
public class AppTypeController { 
	
	@Autowired
	AppTypeService appTypeService;
	private static Logger logger = LoggerFactory.getLogger(AppTypeController.class);
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody ArrayList<AppType> getAppTypeList(){
		Map<String, Object> param = new HashMap<String, Object>();
		try { 
			ArrayList<AppType> atList=(ArrayList<AppType>)appTypeService.getAppList(param);
			logger.debug("atlist size : "+ (atList==null));
			return atList;
		} catch (Exception e) { 
			e.printStackTrace();
			logger.error(e.toString());
		}
		
		return null;
	}  
}
