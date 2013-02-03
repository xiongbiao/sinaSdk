<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%@ include file="/commons/permission.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="collectionData.jsp"%>
<%  
		String uriTop = request.getRequestURI();
		boolean isHome = uriTop.endsWith("index.jsp") || uriTop.equals("/");
		boolean isPushMsg = uriTop.endsWith("push-notifications.jsp");
		boolean isPushNotifications = uriTop.endsWith("push-msg.jsp");
		boolean isPushReport = uriTop.endsWith("push-report.jsp");
		
		boolean isStartedAndroid = uriTop.endsWith("started-android.jsp");
		boolean isStartediOS = uriTop.endsWith("started-ios.jsp");
		
		boolean isDownloadAndroid = uriTop.endsWith("download-android.jsp");
		boolean isDownloadiOS = uriTop.endsWith("download-ios.jsp");
		
		String serverName = request.getServerName();
		String serverPrefix = "";
		if (serverName.indexOf("www.") >= 0) {
			serverPrefix = "www.";
		}
		
		
	/*	String _referer = request.getHeader("Referer");
	
		if (null != _referer) {
			if ((_referer.indexOf("5566ua.com/qa") >= 0) ||
					(_referer.indexOf("jpush.cn/qa") >= 0)) {
				response.sendRedirect(_referer);
				return;
			}
		}*/
		
%>

<div class="container" id="header">
	<a class="logo" href="/" id="logo"><span>JPush</span>
	</a>
	<ul class="action">
		<% if(dev!=null){ %>
		<li><a href="${ctx}/app/application_list.jsp">${user.contact}</a>
		</li>
		<li><a href="${ctx}/developer/loginout">退出</a></li>
		<%}else{ %>
		<li><a href="${ctx}/login.jsp">登录</a></li>
		<li><a href="${ctx}/signup.jsp">注册</a>
		</li>
		<%} %>
	</ul>
	<ul class="top-nav" style="margin-top: 11px;">
		<li id="home">
			<% if(isHome){%> <span>首页</span> <%} else{ %> <a href="${ctx}/index.jsp">首页</a>
			<%}%>
		</li>
		<li id="product">
			<% if(isPushMsg||isPushNotifications||isPushReport){%> <span>产品</span>
			<%} else{ %> <a href="${ctx}/home/push-notifications.jsp">产品</a> <%}%>
		</li>
		<li id="started">
			<% if(isStartedAndroid||isStartediOS){%> <span>快速入门</span> <%} else{ %> <a
			href="${ctx}/home/started-android.jsp">快速入门</a> <%}%>
		</li>
		<li id="download">
			<% if(isDownloadAndroid||isDownloadiOS){%> <span>SDK下载</span> <%} else{ %>
			<a href="${ctx}/home/download-android.jsp">SDK下载</a> <%}%>
		</li>
		<!-- <li id="faq"><a href="http://docs.jpush.cn/pages/viewpage.action?pageId=557074" target="_blank">常见问题</a></li>  -->
		<li id="docs"><a href="http://docs.jpush.cn/" target="_blank">文档</a>
		</li>
		<li id="faq"><a href="http://<%=serverPrefix %>jpush.cn/qa">问答</a>
		</li>
		<li id="blog"><a href="http://blog.jpush.cn/" target="_blank">Blog</a>
		</li>
		<li id="price"><a
			href="http://docs.jpush.cn/pages/viewpage.action?pageId=3309648"
			target="_blank">价格</a>
		</li>
	</ul>
	<!-- 维护信息
	<div style="position: relative;top: 85px;font-size: 14px;color: #ff0000;margin: 0 auto;z-index: 1;white-space: nowrap;height: 20px;"><div style="height: 20px;position: absolute;left: 165px;">维>护通知：系统将于2013年01月05日 23 点至05日 24 点进行维护。给您带来的不便，敬请谅解！</div></div>
	 -->
</div>