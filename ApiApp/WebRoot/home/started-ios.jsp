<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kkpush.util.*"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>iOS - 快速入门 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">

	<%@include file="/includes/jpush_header.jsp"%>

	<!-- hi begin -->
	<h1 class="container">快速入门</h1>
	<!-- h1 end -->
	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%@include file="/includes/jpush_push_left.jsp"%>

		<div class="right_content">
			<div class="columns group" id="content">

				<div class="quick-start-guide steps-inner clearfix">
					<div class="step" id="android-step1">
						<div></div>
						<ol>
							<li><a href="${ctx}/signup.jsp" target="_blank">注册</a>开发者账号，</li>
							<li>添加一个应用，并获取AppKey。</li>
						</ol>
					</div>

					<div class="step" id="android-step3">
						<div></div>
						<ol>
							<li id="s3Icon"></li>
							<li>登录<a href="${ctx}/login.jsp" target="_blank">管理Portal</a>，向客户端
								Push 通知，</li>
							<li>并可以及时地查看到状态报告。</li>
						</ol>
					</div>

					<div class="step" id="android-step2">
						<div></div>
						<ol>
							<li><a href="${ctx}${sdk_i}">下载 iOS SDK</a>，</li>
							<li>并参考<a
								href="http://docs.jpush.cn/pages/viewpage.action?pageId=2621727"
								target="_blank">iOS SDK 集成指南</a>来集成 JPush 到应用程序里。</li>
						</ol>
					</div>
				</div>
			</div>
		</div>

		<div class="clear"></div>
	</div>
	<!--  sub-page end  -->


	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>