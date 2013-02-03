<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%
	Object devObj = session.getAttribute("user");
	Developer dev = null;
	if (null != devObj) {
		dev = (Developer) devObj;
		if (dev != null) {
			session.setAttribute("userName", dev.getContact());
			ServletContext ContextA = session.getServletContext();
			ContextA.setAttribute("session", session);
		}
	}
%>
