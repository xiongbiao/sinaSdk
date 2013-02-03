<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kkpush.util.*"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计图表 - 产品介绍 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>
	<!-- hi begin -->
	<h1 class="container">统计图表</h1>
	<!-- h1 end -->
	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%@include file="/includes/jpush_push_left.jsp"%>

		<div class="right_content">
			<div class="columns group" id="content">


				<div id="guts">
					<p>
						<img alt="push 推送  统计图表" width="370" class="right"
							style="margin-left: 10px; margin-top: -10px;"
							src="${ctx}/resources/jpushResources/images/img_report.png"
							alt="JPush Reports">
					</p>
					<h2>统计图表</h2>
					<p class="intro">包括 发送通知的数据、用户打开应用的时间、用户使用应用的时间。</p>
					<p class="intro"></p>
					<h2>&nbsp;</h2>
					<p>
						<br> <br> <br> <br>
					</p>
					<h3 class="benefits">用户打开应用</h3>
					<p>用户打开应用的时间数据来源 在一段时间范围内, 统计所有终端打开应用的次数。 我们可以通过这个数据查看应用的用户活跃度。
					</p>
					<h3 class="benefits">
						<br>用户使用应用
					</h3>
					<p>用户使用应用的时间 统计在一段范围内 终端使用应用总的时间 除以 终端数 等到平均使用应用的时间</p>
					<h3 class="benefits">
						<br>发送通知
					</h3>
					<p>发送通知的数据 统计在一段时间范围内 你推送消息和通知的数量</p>
				</div>
			</div>
		</div>

		<div class="clear"></div>
	</div>
	<!--  sub-page end  -->


	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>