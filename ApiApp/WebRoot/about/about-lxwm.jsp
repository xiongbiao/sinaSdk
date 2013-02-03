<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>联系我们- ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="onboard">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>联系我们</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<form id="fpass" method="post">
							<div style='display: none'></div>
							<fieldset>
								<div>
									<label> 电话： </label> 0755-12345445
								</div>
								<div>
									<label> 地址： </label> 深圳湖田
								</div>
							</fieldset>
						</form>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>
