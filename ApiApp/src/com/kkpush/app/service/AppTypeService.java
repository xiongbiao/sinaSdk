package com.kkpush.app.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.app.domain.AppType;
import com.kkpush.app.persistence.AppTypeMapper;

@Service
@Transactional
public class AppTypeService {
	@Autowired
	private AppTypeMapper appTypeMapper;
	private static Logger logger = LoggerFactory.getLogger(AppTypeService.class);
	public List<AppType> getAppList(Map<String, Object> param) throws Exception {
		try {
		    return	appTypeMapper.getAppTypeList(param); 
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			throw new Exception("查询应用类别出错");
		}
	}

}
