<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%
 
String uri = request.getRequestURI();
boolean isaccount = uri.endsWith("account.jsp");
boolean isChangeEmail = uri.endsWith("account_change_email.jsp");
boolean isChangePasswd = uri.endsWith("account_change_passwd.jsp");
boolean isChangeBase = uri.endsWith("account_change_base.jsp");
boolean isValidateWebsite = uri.endsWith("account_validate_website.jsp");
 
%>
<div class="left_content">
	<div class="sidebar" id="account_sidebar">
		<h2>
			<span> 帐户 </span>
		</h2>
		<ul>
			<li class="<%=isaccount?"current":"" %>"><a
				href="${ctx}/dev/account.html"> <span> 概览 </span> </a></li>
			<li class="<%=isChangeEmail?"current":"" %>"><a
				href="${ctx}/dev/change_email.html"> <span> 修改Email </span> </a></li>
			<li class="<%=isChangePasswd?"current":"" %>"><a
				href="${ctx}/dev/change_passwd.html"> <span> 修改密码 </span> </a></li>
			<li class="<%=isChangeBase?"current":"" %>"><a
				href="${ctx}/dev/change_base.html"> <span> 修改基本资料 </span> </a></li>
			<li class="<%=isValidateWebsite?"current":"" %>"><a
				href="${ctx}/dev/validate_website.html"> <span> API回调地址设置 </span> </a></li>

		</ul>
	</div>
</div>
