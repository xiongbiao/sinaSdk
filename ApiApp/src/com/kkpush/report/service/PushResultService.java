package com.kkpush.report.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.report.persistence.PushResultInfoMapper;

@Service
@Transactional
public class PushResultService {
	@Autowired
	PushResultInfoMapper pushResultInfoMapper;

	/**
	 * 获得集合
	 */
	public List<Map<String, Object>> getSystemPushDataList(Map<String, Object> param) {
		return pushResultInfoMapper.getSystemPushDataList(param);

	}

	/**
	 * 获得总数
	 * 
	 * @param param
	 * @return
	 */
	public int getSystemPushDataCount(Map<String, Object> param) {
		return pushResultInfoMapper.getSystemPushDataCount(param);
	}

	/**
	 * 获得集合
	 */
	public List<Map<String, Object>> getCustomizePushDataList(Map<String, Object> param) {
		 List<Map<String, Object>> dataList =  (List<Map<String, Object>>) pushResultInfoMapper.getCustomizePushDataList(param);
		
		 List<Map<String, Object>> dataListTmp = new ArrayList<Map<String,Object>>();
		 dataListTmp.addAll(dataList);
		
		int totalArrivalCount = 0;
		int totalClickCount = 0;

		for (int i = 0; i < dataList.size(); i++) {
			Map<String, Object> data = dataList.get(i);
			Object arrivalCount = data.get("ARRIVAL_COUNT");
			Object clickCount = data.get("CLICK_COUNT");
			if (arrivalCount instanceof Number) {
				totalArrivalCount += ((Number) arrivalCount).intValue();
			}

			if (clickCount instanceof Number) {
				totalClickCount += ((Number) clickCount).intValue();
			}
		}
		if (dataListTmp.size() > 0) {
			Map<String, Object> totalData = new HashMap<String, Object>();
			totalData.put("IDATE", "Total");
			totalData.put("DEV_ID", "-");
			totalData.put("APP_ID", "-");
			totalData.put("AD_ID", "-");
			totalData.put("APP_NAME", "-");
			totalData.put("MSG_TITLE", "-");
			totalData.put("ARRIVAL_COUNT", totalArrivalCount);
			totalData.put("CLICK_COUNT", totalClickCount);
			dataListTmp.add(totalData);
		}
		return dataListTmp;
		
		
	}

	/**
	 * 获得总数
	 * 
	 * @param param
	 * @return
	 */
	public int getCustomizedPushDataCount(Map<String, Object> param) {
		return pushResultInfoMapper.getCustomizePushDataCount(param);
	}

}
