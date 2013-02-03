<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<% 
	String uri = request.getRequestURI();
	boolean isPushNotificationsLeft = uri.endsWith("push-notifications.jsp");
	boolean isPushMsgLeft = uri.endsWith("push-msg.jsp");
	boolean isPushReportLeft = uri.endsWith("push-report.jsp");
	
	boolean isStartedAndroidLeft = uri.endsWith("started-android.jsp");
	boolean isStartediOSLeft = uri.endsWith("started-ios.jsp");
	
	boolean isDownloadAndroidLeft = uri.endsWith("download-android.jsp");
	boolean isDownloadiOSLeft = uri.endsWith("download-ios.jsp");

%>
<div class="left_content">
	<div class="sidebar" id="left-nav">
		<% if(isPushNotificationsLeft||isPushMsgLeft||isPushReportLeft){%>
		<ul>
			<li class="<%=isPushNotificationsLeft?"current":"" %>"><a
				href="${ctx}/home/push-notifications.jsp"> <span> 推送通知 </span> </a>
			</li>
			<li class="<%=isPushMsgLeft?"current":"" %>"><a
				href="${ctx}/home/push-msg.jsp"> <span> 推送工具 </span> </a></li>
			<li class="<%=isPushReportLeft?"current":"" %>"><a
				href="${ctx}/home/push-report.jsp"> <span> 统计图表 </span> </a></li>
		</ul>
		<%}%>
		<% if(isStartedAndroidLeft||isStartediOSLeft){%>
		<ul>
			<li class="<%=isStartedAndroidLeft?"current":"" %>"><a
				href="${ctx}/home/started-android.jsp"> <span> Android </span> </a>
			</li>
			<li class="<%=isStartediOSLeft?"current":"" %>"><a
				href="${ctx}/home/started-ios.jsp"> <span> iOS </span> </a>
			</li>
		</ul>
		<%}%>
		<% if(isDownloadAndroidLeft||isDownloadiOSLeft){%>
		<ul>
			<li class="<%=isDownloadAndroidLeft?"current":"" %>"><a
				href="${ctx}/home/download-android.jsp"> <span> Android </span>
			</a>
			</li>
			<li class="<%=isDownloadiOSLeft?"current":"" %>"><a
				href="${ctx}/home/download-ios.jsp"> <span> iOS </span> </a>
			</li>
		</ul>
		<%}%>
	</div>
</div>