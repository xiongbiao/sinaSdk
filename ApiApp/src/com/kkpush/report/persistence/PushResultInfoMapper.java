package com.kkpush.report.persistence;

import java.util.List;
import java.util.Map;

public interface PushResultInfoMapper {
	public List<Map<String, Object>> getCustomizePushDataList(Map<String, Object> params);

	public Integer getCustomizePushDataCount(Map<String, Object> params);

	public List<Map<String, Object>> getSystemPushDataList(Map<String, Object> params);

	public Integer getSystemPushDataCount(Map<String, Object> params);

}
