<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Android - SDK下载 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>

	<!-- hi begin -->
	<h1 class="container">SDK 下载</h1>
	<!-- h1 end -->
	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%@include file="/includes/jpush_push_left.jsp"%>

		<div class="right_content">
			<div class="columns group" id="content">
				<div class="">
					<p class="intro">Android Push SDK 下载包是个 zip 文件，里边有如下的内容：</p>
					<ul style="list-style-type: square; margin-left: 30px;">
						<li>文档：Android SDK 集成指南</li>
						<li>SDK相关 jar 包与资源</li>
						<li>参考示例项目</li>
					</ul>
					<h2>
						<a href="${ctx}${sdk_a}" class="btn_green download">SDK下载</a>
					</h2>
				</div>
			</div>

			<div class="columns group" id="content">
				<div class="">
					<br />
					<p class="intro">请参考如下文档来集成SDK：</p>
					<ul style="list-style-type: square; margin-left: 30px;">
						<li><a
							href="http://docs.jpush.cn/pages/viewpage.action?pageId=3309574">3分钟快速Demo</a>
						</li>
						<li><a
							href="http://docs.jpush.cn/pages/viewpage.action?pageId=557214">Android
								SDK 集成指南</a>
						</li>
						<li><a
							href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343741">Android
								SDK 调试指南</a>
						</li>
					</ul>
				</div>
			</div>

		</div>
		<div class="clear"></div>
	</div>
	<!--  sub-page end  -->
	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>