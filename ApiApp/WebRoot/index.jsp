<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>首页 - JPush极光推送</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/index.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>
	<!-- adLayout begin -->
	<div id="adLayout">
		<div class="container"></div>
		<div id="frontCloud">
			<div class="container">
				<div id="desc">
					<img title="为你的移动应用加油" alt="为你的移动应用加油"
						src="${ctx}/resources/jpushResources/images/ad_info.png">
					<p>极光推送平台，使得你可以即时向安装你的应用程序的用户推送通知或者消息，与用户保持互动，从而有效提高留存率，提升用户体验。
					</p>
					<a class="btn_green" href="${ctx}/home/started-android.jsp#main">快速入门</a>
				</div>
			</div>
		</div>
	</div>
	<!-- adLayout end -->

	<!-- guide begin   -->
	<ul class="container" id="guide">
		<li class="step" id="step1">
			<div class="idxIcon"></div>
			<div class="title">免费 Push服务</div>
			<div class="desc">
				服务器端向用户 Push消息，<br>即时到达客户端。
			</div></li>
		<li class="step" id="step2">
			<div class="idxIcon"></div>
			<div class="title">SDK节能</div>
			<div class="desc">客户端SDK以非常低的代价保持连接，电量、流量消耗都很少。</div></li>
		<li class="step" id="step3">
			<div class="idxIcon"></div>
			<div class="title">轻松推送</div>
			<div class="desc">
				推送平台技术架构先进，经受过千万级用户的考验；<br>支持 Portal上推送，也支持 API调用。
			</div></li>
	</ul>
	<!--guide end   -->
	<!-- introduce begin -->
	<ul class="container" id="introduce">
		<li id="push"><img title="推送通知" alt="push 推送通知"
			src="${ctx}/resources/jpushResources/images/icon_push_notify.jpg">
			<div class="function">
				<h2>推送通知</h2>
				<h3>使你的应用程序保持高注意力</h3>
				<p>即使用户没有打开应用程序，也能够推送通知到达用户手机。您可以即时向用户推送活动提示、动态更新、积分信息等通知。</p>
			</div></li>
		<li id="custom"><img title="推送自定义消息" alt="push 推送自定义消息"
			src="${ctx}/resources/jpushResources/images/icon_push_custom.jpg">
			<div class="function">
				<h2>推送自定义消息</h2>
				<h3>强大的自定义消息推送通道</h3>
				<p>开发者可以推送自定义的消息内容。JPush SDK 把内容完全转给开发者应用程序，由开发者应用程序去处理自定义消息。</p>
			</div></li>
		<li id="tools"><img title="推送工具" alt="push 推送工具"
			src="${ctx}/resources/jpushResources/images/icon_push_tool.jpg">
			<div class="function">
				<h2>推送工具</h2>
				<h3>灵活地根据需要进行推送</h3>
				<p>管理Portal上的推送助手，方便快速推送，普通用户都可操作。另外也提供API调用方式，灵活强大。</p>
			</div></li>
		<li id="report"><img title="推送图表" alt="push 推送图表"
			src="${ctx}/resources/jpushResources/images/icon_push_report.jpg">
			<div class="function">
				<h2>图表</h2>
				<h3>推送效果直观呈现</h3>
				<p>推送到达了多少？到达后，用户有没有点击打开应用？打开应用后，用户逗留的时间有没有延长 ？
					直观的对比图表，让你轻松地评估推送效果。</p>
			</div></li>
	</ul>
	<!-- introduce end -->

	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>