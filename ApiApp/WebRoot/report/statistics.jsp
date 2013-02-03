<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>数据统计 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>

				<div class="right_content">
					<h2>应用统计</h2>
					<!-- <h2>推送一览</h2> -->
					<div class="bluebox">
						<dl>
							<dt>设备数量</dt>
							<span class="app_counts_ajax hidden"
								data-key="KBBO0yi0RqiuNreThKxI3w" data-type="device_tokens"></span>
							<dd style="">
								<span class="num" data-platform="device_tokens" id="devices">-</span>
							</dd>
							<dt>活跃的设备数量</dt>
							<dd style="">
								<span class="num" data-platform="active_device_tokens"
									id="active_devices">-</span>
							</dd>
							<dt>活跃的用户（月）</dt>
							<dd>
								<span class="app_counts_ajax" id="active_users">-</span>
							</dd>
							<dt>推送的数量（月）</dt>
							<dd>
								<span class="app_counts_ajax" data-key="KBBO0yi0RqiuNreThKxI3w"
									data-type="pushes" data-last-month="True" id="monthly_pushes">-</span>
							</dd>
							<dt>推送的数量（全部）</dt>
							<dd>
								<span class="app_counts_ajax" id="all_pushes">-</span>
							</dd>
						</dl>
					</div>

				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>
<script>
	jQuery.post("${ctx}/app/getAppList?appId=<%=request.getParameter("appId")%>", {"pageSize":500}, function(data) {
	       var allApp ="";
	       if(data!=null){
	         var total = data.total;     
	         var rows = data.rows;    
	         if(rows!=null&&rows.length>0){
	        	 $("#devices").html(""+rows[0].devicesPer+"");
	        	 $("#active_devices").html(""+rows[0].activeDevicesPer+"");
	        	 $("#active_users").html(""+rows[0].activeUserPer+"");
	        	 $("#monthly_pushes").html(""+rows[0].pushesPer+"");
	        	 $("#all_pushes").html(""+rows[0].pushesAmount+"");
	         }
	       }
	   });
</script>