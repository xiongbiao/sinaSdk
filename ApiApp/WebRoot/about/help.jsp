<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>帮助- ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="onboard">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>帮助</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<div>
							<label for="id_username"> 帮助文档 </label>
							<p>1、开发文档 2、帮助文档</p>
						</div>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/loginfooter.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/global.js"></script>
</body>
</html>
