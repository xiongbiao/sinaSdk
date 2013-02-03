<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="sdk_a" value="/app/download?type=android&file=Jpush-Android-sdk-1.3.3.zip"/>
<c:set var="sdk_i" value="/app/download?type=ios&file=JPush-iOS-sdk-1.2.6.zip"/>

<c:set var="seconds" value="5" />
<c:set var="Title" value="JPush极光推送  开发者平台" />
