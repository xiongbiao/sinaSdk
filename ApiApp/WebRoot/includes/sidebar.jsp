<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%
	String uri = request.getRequestURI();
    
    //新增用户、在线用户、活跃用户
    boolean isUserCountPage = uri.endsWith("user_count.jsp");

	boolean isDetailPage = uri.endsWith("detail.jsp");
	boolean isEditPage = uri.endsWith("edit.jsp");
	boolean isStatisticsPage = uri.endsWith("statistics.jsp");
	
	boolean isComposerPage = uri.indexOf("composer")>0;
	boolean isComposerNewMessagePage = uri.endsWith("composer_newMessage.jsp");
	boolean isComposerScheduledMessagePage = uri.endsWith("composer_scheduledMessage.jsp");
	boolean isComposerHistoryPage = uri.endsWith("composer_history.jsp");
	
	boolean isMsgComposerPage = uri.indexOf("msgComposer")>0;
	boolean isMsgComposerNewMessagePage = uri.endsWith("msgComposer_message_new.jsp");
	boolean isMsgComposerScheduledMessagePage = uri.endsWith("msgComposer_message_schedule.jsp");
	boolean isMsgComposerHistoryPage = uri.endsWith("msgComposer_message_history.jsp");
	
	boolean isRichComposerPage = uri.indexOf("richComposer")>0;
	boolean isRichComposerNewMessagePage = uri.endsWith("richComposer_newMessage.jsp");
	boolean isRichComposerDraftsPage = uri.endsWith("richComposer_drafts.jsp");
	boolean isRichComposerHistoryPage = uri.endsWith("richComposer_history.jsp");
	
	boolean isReportsPage = uri.endsWith("reports.jsp");
	String appId=request.getParameter("appId");
%>
<script type="text/javascript">
    appid=<%=appId %>
</script>
<div class="left_content">
	<div id="premium_sidebar" class="sidebar">
		<div style="display: none;" class="trial-order-now"
			onmouseover="$(this).show();" onmouseout="$(this).hide();">
			<span class="trial-order-arrow"></span> <a href="">推送报表 </a>
		</div>
		<h2>
			<span>工具</span>
			<!-- 
            <a href=""
                onmouseover="$('.trial-order-now').show();"
                onmouseout="$('.trial-order-now').hide();"></a>
        -->
		</h2>
		<ul>
			<li id="premium_composer" class="<%=isComposerPage?"current":"" %>">
				<!--
            	<span><a href="${ctx}/push/composer_newMessage.jsp?appId=<%=appId %>">推送通知</a></span>
            	-->
				<span><a href="${ctx}/push/newMsg/<%=appId %>.html">推送通知</a>
			</span>
				<ul id="premium_push_subnav"
					class="<%=isComposerPage?"expanded":"" %>">
					<li class="<%=isComposerNewMessagePage?"current":"" %>"><a
						href="${ctx}/push/newMsg/<%=appId %>.html">新通知</a>
					</li>
					<li class="<%=isComposerScheduledMessagePage?"current":"" %>">
						<a href="${ctx}/push/scheduleMsg/<%=appId %>.html">发送中</a>
					</li>
					<li class="<%=isComposerHistoryPage?"current":"" %>"><a
						href="${ctx}/push/hisMsg/<%=appId %>.html">发送记录</a></li>
				</ul></li>

			<li id="premium_push_msg"
				class="<%=isMsgComposerPage?"current":"" %>"><span><a
					href="${ctx}/push/new_Msg/<%=appId %>.html">推送消息</a>
			</span>
				<ul id="premium_push_subnav"
					class="<%=isMsgComposerPage?"expanded":"" %>">
					<li class="<%=isMsgComposerNewMessagePage?"current":"" %>"><a
						href="${ctx}/push/new_Msg/<%=appId %>.html">新消息</a>
					</li>
					<li class="<%=isMsgComposerScheduledMessagePage?"current":"" %>">
						<a href="${ctx}/push/schedule_Msg/<%=appId %>.html">发送中</a>
					</li>
					<li class="<%=isMsgComposerHistoryPage?"current":"" %>"><a
						href="${ctx}/push/his_Msg/<%=appId %>.html">发送记录</a></li>
				</ul></li>

			<!--  <li id="premium_rich_push" class="<%=isRichComposerPage?"current":"" %>">
                <span><a href="${ctx}/push/richComposer_newMessage.jsp?appId=<%=appId %>">通知报表</a></span>
                <ul id="premium_rich_push_subnav" class="<%=isRichComposerPage?"expanded":"" %>">
                    <li class="<%=isRichComposerNewMessagePage?"current":"" %>">
                    	<a href="${ctx}/push/richComposer_newMessage.jsp?appId=<%=appId %>">New Message</a>
                   	</li>
                    <li class="<%=isRichComposerDraftsPage?"current":"" %>">
                    	<a href="${ctx}/push/richComposer_drafts.jsp?appId=<%=appId %>">Drafts (0)</a>
                   	</li>
                    <li class="<%=isRichComposerHistoryPage?"current":"" %>">
                    	<a href="${ctx}/push/richComposer_history.jsp?appId=<%=appId %>">History</a>
                    </li>
                </ul>
            </li>
             -->

			<li id="premium_reports" class="<%=isReportsPage?"current":"" %>">
				<span><a href="${ctx}/report/pushes/<%=appId %>.html">推送报表</a>
			</span>
				<ul class="<%=isReportsPage?"expanded":"" %>">
					<li class="current"><a
						href="${ctx}/report/pushes/<%=appId %>.html">发送的消息</a>
					</li>
					<li class=""><a href="${ctx}/report/opens/<%=appId %>.html">用户打开App</a>
					</li>
					<li class=""><a href="${ctx}/report/times/<%=appId %>.html">用户使用App</a>
					</li>
					<!-- <li class=""><a href="${ctx}/report/reports.jsp?times=pushes&appId=<%=appId %>"">Unique Opt-ins</a></li>-->
				</ul>
			</li>
		</ul>
	</div>
	<div id="app_sidebar" class="sidebar">
		<h2>
			<span id="left_appName"></span>
		</h2>
		<ul>
			<li class="<%=isDetailPage?"current":"" %>" id="app_details"><a
				href="${ctx}/app/detail/<%=appId %>.html"> <span> 详情 </span> </a></li>
			<li class="<%=isEditPage?"current":"" %>" id="edit_app"><a
				href="${ctx}/app/edit/<%=appId %>.html"> <span> 修改 </span> </a></li>
			<%-- <li class="<%=isStatisticsPage?"current":"" %>" id="stats">
	            <a href="${ctx}/report/statistics/<%=appId %>.html">
	                <span> 统计 </span>
	            </a>
	        </li> --%>
		</ul>
	</div>

	<!-- 统计报表 -->
	<div id="stats_sidebar" class="sidebar">
		<h2>
			<span id="left_appName">统计报表</span>
		</h2>
		<ul>
			<li class="<%=isUserCountPage?"current":"" %>" id="app_stats">
				<a href="${ctx}/report/user_count/<%=appId %>.html"> <span>用户统计</span>
			</a></li>
		</ul>
	</div>
</div>