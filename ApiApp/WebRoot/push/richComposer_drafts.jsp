<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>发送中消息 - ${Title}</title>
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
					<h3>Rich Push - Drafts</h3>
					<form
						action="/apps/KBBO0yi0RqiuNreThKxI3w/composer/rich-push/draft/delete/"
						method="post" id="drafts-form">
						<div style="display: none">
							<input type="hidden" name="csrfmiddlewaretoken"
								value="8f403e71afcf5e5c4635d058bf6a7985">
						</div>
						<table class="list">
							<thead>
								<tr>
									<th style="width: 20px;"></th>
									<th>Title</th>
									<th>Date</th>
									<th></th>
								</tr>
							</thead>
							<tfoot>
								<tr>
									<td colspan="4"><button type="submit" disabled="disabled">Delete
											Selected</button>
									</td>
								</tr>
							</tfoot>
							<tbody>
							</tbody>
						</table>
					</form>
				</div>

				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>