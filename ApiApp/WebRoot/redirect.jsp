<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="/commons/permission.jsp"%>
<%@page import="com.kkpush.util.*"%>
<% 


/* SpringContextUtils su = new SpringContextUtils();
Developer devs=(Developer)request.getSession().getAttribute("user"); 
String devName =su.encode(devs.getContact());
String web = su.encode("http://localhost:48000");
String url = "http://localhost:48000/loginservlet?contact="+devName+"&web="+web;
  response.sendRedirect(url); */
//  out.clear(); 
//out = pageContext.pushBody();  
 
 response.sendRedirect("app/application_list.jsp");
 
%>
