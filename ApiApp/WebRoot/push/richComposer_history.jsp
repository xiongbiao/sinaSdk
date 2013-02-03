<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>已发消息 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/push.css" media="screen">
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>

				<div class="right_content">
					<h3>Rich Push - Sent Messages</h3>
					<table class="list">
						<thead>
							<tr>
								<th>Title</th>
								<th>Date</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="3">You have not sent any Rich Push messages</td>
							</tr>
						</tbody>
					</table>
				</div>

				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>