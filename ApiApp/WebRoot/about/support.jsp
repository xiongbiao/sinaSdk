<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>支持 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="onboard">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>支持</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<form id="fpass" method="post">
							<fieldset>
								<div>
									<label for="id_username"> 开发技术支持 </label>
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
