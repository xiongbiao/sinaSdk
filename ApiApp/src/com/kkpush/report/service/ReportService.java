package com.kkpush.report.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.validator.GenericValidator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kkpush.util.HttpInvoke;
import com.kkpush.util.SystemConfig;

@Service
@Transactional
public class ReportService {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getData(Map<String, String> params) {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {
			List<NameValuePair> qparams = getParam(params);
			qparams.add(new BasicNameValuePair("sender_id", params.get("sender_id")));
			String[] data = HttpInvoke.invokeDoPost(SystemConfig.getProperty("redis.http.url"), qparams);
			if (data[0].equals("200")) {
				String content =  data[1];
				JSONObject jsonObj = JSONObject.fromObject(content);
				if (jsonObj.getInt("rsp_code") == 200) {
					JSONArray jsonArray = jsonObj.getJSONArray("rsp_info");
					if (jsonArray != null && jsonArray.size() > 0) {
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject record = jsonArray.getJSONObject(i);
							Map<String, Object> obj = (Map<String, Object>) JSONObject.toBean(record, Map.class);
							dataList.add(obj);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataList = totalData(dataList, params);
		sortData(dataList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", dataList.size());

		result.put("rows", pagination(dataList, params));
		return result;
	}

	public List<Map<String, Object>> totalData(List<Map<String, Object>> dataList, Map<String, String> params) {
		List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();
		Map<String, Map<String, Object>> dataMap = new HashMap<String, Map<String, Object>>();
		for (Map<String, Object> data : dataList) {
			String key = data.get("sender_id") + "_" + data.get("timestr");
			Map<String, Object> dataTmp;
			if (dataMap.containsKey(key)) {
				dataTmp = dataMap.get(key);
			} else {
				dataTmp = new HashMap<String, Object>();
				dataMap.put(key, dataTmp);
				dataTmp.put("sender_id", data.get("sender_id"));
				String timeStr = getTimeString(getInteger(data.get("timestr")));
				dataTmp.put("timeStr", timeStr);
				dataTmp.put("time", getInteger(data.get("timestr")));
			}

			Integer newObj = getInteger(data.get("reguid_cnt"));
			Integer existObj = getInteger(dataTmp.get("reguid_cnt"));
			dataTmp.put("reguid_cnt", newObj + existObj);

			newObj = getInteger(data.get("totaluid_cnt"));
			existObj = getInteger(dataTmp.get("totaluid_cnt"));
			dataTmp.put("totaluid_cnt", newObj + existObj);

			Double sendRate = getDouble(data.get("send_cnvrtproportion"));
			Double showRate = getDouble(data.get("showrate_cnvrtproportion"));

			if (sendRate == 0) {
				sendRate = 1.0;
			}
			if (showRate == 0) {
				showRate = 1.0;
			}

			newObj = getInteger(data.get("load_cnt"));

			int existLoadCntUpdate = getInteger(dataTmp.get("load_cnt_update"));
			int loadCntUpdate = (int) Math.round(newObj * sendRate) + existLoadCntUpdate;
			dataTmp.put("load_cnt_update", loadCntUpdate);

			int existShowCntUpdate = getInteger(dataTmp.get("show_cnt_update"));
			int showCntUpdate = (int) Math.round(loadCntUpdate * showRate) + existShowCntUpdate;
			dataTmp.put("show_cnt_update", showCntUpdate);

			double rate = showCntUpdate * 1.0 / loadCntUpdate;
			DecimalFormat df = new DecimalFormat("0.00");
			String rateStr = df.format(rate);
			dataTmp.put("rate", rateStr);

			double revenue = showCntUpdate * Float.valueOf(SystemConfig.getProperty("show.price", "1.0"));
			String revenueStr = df.format(revenue);
			dataTmp.put("revenue", revenueStr);
		}
		totalList.addAll(dataMap.values());
		return totalList;

	}

	public void sortData(List<Map<String, Object>> dataList) {
		Collections.sort(dataList, new Comparator<Map<String, Object>>() {
			 
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((Integer) o2.get("time")) - ((Integer) o1.get("time"));
			}
		});
	}

	private Integer getInteger(Object value) {
		if (value != null) {
			if (value instanceof Integer) {
				return (Integer) value;
			} else if (value instanceof String) {
				return Integer.valueOf((String) value);
			}
		}
		return 0;
	}

	private Double getDouble(Object value) {
		if (value != null) {
			if (value instanceof Double) {
				return (Double) value;
			} else if (value instanceof String) {
				return Double.valueOf((String) value);
			}
		}
		return 0.0;
	}

	public String getTestData() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("rsp_code", 200);
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Random r = new Random();
		int totalRegister = 10000;
		for (int i = 1; i <= 12; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			/**
			 * "sender_id":'a', "app_id":'abcd123' "detestr":1234 "timestr":2
			 * "send_cnt": 32012, "show_cnt": 31000, "click_cnt": 30000,
			 * "load_cnt": 2800, "download_cnt": 29000, "install_cnt": 20000,
			 * "send_cnvrtproportion": 0.7, "showrate_cnvrtproportion": 0.8,
			 * "reguid_cnt": 20000, "totaluid_cnt": 20000000]
			 */
			data.put("sender_id", "martinliu@kktalk.cn");
			data.put("timestr", i);
			int value = r.nextInt(1000000) + 50000;
			data.put("send_cnt", (int) (value * 0.9));
			data.put("show_cnt", (int) (value * 0.8));
			data.put("click_cnt", (int) (value * 0.7));
			data.put("load_cnt", (int) (value * 0.5));
			data.put("download_cnt", (int) (value * 0.4));
			data.put("install_cnt", (int) (value * 0.3));
			data.put("send_cnvrtproportion", 0.7);
			data.put("showrate_cnvrtproportion", 0.8);
			int reg = r.nextInt(5000);
			data.put("reguid_cnt", reg);
			totalRegister += reg;
			data.put("totaluid_cnt", totalRegister);
			dataList.add(data);
		}
		jsonObject.put("rsp_info", dataList);
		return jsonObject.toString();
	}

	private List<NameValuePair> getParam(Map<String, String> params) {
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		if (params.get("DEV_ID") != null) {
			paramList.add(new BasicNameValuePair("sender_id", (String) params.get("DEV_ID")));
		} else {
			paramList.add(new BasicNameValuePair("sender_id", "all"));
		}
		if (params.get("APP_ID") != null) {
			paramList.add(new BasicNameValuePair("appid", (String) params.get("APP_ID")));
		} else {
			paramList.add(new BasicNameValuePair("appid", "all"));
		}
		if (params.get("START_DATE") != null) {
			paramList.add(new BasicNameValuePair("datestr", (String) params.get("START_DATE")));
		}

		return paramList;
	}

	List<Map<String, Object>> pagination(List<Map<String, Object>> dataList, Map<String, String> params) {

		if (dataList.size() == 0) {
			return null;
		}

		String pageNoStr = params.get("PAGE_NO");
		if (!GenericValidator.isInt(pageNoStr)) {
			pageNoStr = "1";
		}

		String pageSizeStr = params.get("PAGE_SIZE");
		if (!GenericValidator.isInt(pageSizeStr)) {
			pageSizeStr = "15";
		}
		int pageNo = Integer.valueOf(pageNoStr);
		if (pageNo == 0) {
			pageNo = 1;
		}
		int limit = Integer.valueOf(pageSizeStr);
		if (limit == 0) {
			limit = 15;
		}
		int start = (pageNo - 1) * limit;
		if (start > dataList.size()) {
			start = 0;
		}
		int end = pageNo * limit;
		if (end > dataList.size()) {
			end = dataList.size();
		}
		return dataList.subList(start, end);

	}

	private String getTimeString(Integer time) {
		String startTime = (time - 1) * 2 + ":00";
		String endTime = (time - 1) * 2 + 1 + ":59";
		return startTime + " - " + endTime;
	}
}
