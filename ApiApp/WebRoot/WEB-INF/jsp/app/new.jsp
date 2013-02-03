<%@page import="net.sf.json.JSONObject"%>
<%@page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%
	out.clear();
	Object model = request.getAttribute("model");
	if (null != model) {
		Map jsonMap = (Map) model;
		JSONObject j = JSONObject.fromObject(jsonMap);
		out.print(j.toString());
	}
%>