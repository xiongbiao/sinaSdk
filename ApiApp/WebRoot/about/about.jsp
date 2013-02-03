<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>和讯华谷 - 公司简介 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>

	<!-- hi begin -->
	<h1 class="container">公司简介</h1>
	<!-- h1 end -->
	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%--  <%@include file="/includes/jpush_push_left.jsp"%> --%>

		<div class="right_content" style="width: 90%;">
			<div class="columns group" id="content">
				<div class="">
					<p class="ht14">
						深圳市和讯华谷信息技术有限公司成立于2011年，总部位于中国广东省深圳市。 <br />
						公司致力于为全球的移动应用开发者提供专业、高效的移动消息推送服务。 <br>
						极光推送（JPush）是公司的移动消息推送服务品牌。
					</p>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<!--  sub-page end  -->
	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>