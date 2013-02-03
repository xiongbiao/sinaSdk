<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%@ include file="/commons/permission.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="zh">
<head>
<title>按天报表 - ${Title}</title>

<link rel="stylesheet" href="${ctx}/plugin/alert/jquery.alerts.css"
	type="text/css"></link>
<link rel="stylesheet" href="${ctx}/plugin/themes/default/easyui.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/skin/default/appsetting.css"
	type="text/css"></link>

<link rel="stylesheet" href="${ctx}/plugin/grid/css/gridLoad.css"
	type="text/css"></link>
<link rel="stylesheet" href="${ctx}/plugin/grid/css/grid.css"
	type="text/css"></link>
<link rel="stylesheet" href="${ctx}/skin/default/page.css"
	type="text/css"></link>

<link rel="stylesheet" href="${ctx}/skin/default/uapush.css"
	type="text/css"></link>
</head>

<body>
	<%@ include file="/top.jsp"%>
	<jsp:include page="/nav" flush="true" />

	<div id="main">
		<div id="" class="main-title">按天报表</div>
		<div id="content" class="main-content">
			<div id="divSearch">
				<span style="font-size: 16px;">选择应用</span> <select id="appId"
					class="easyui-combobox" name="appId" style="width: 150px;">
					<option value="">-- 请选择应用 --</option>
				</select> <span style="font-size: 16px;">时间</span> <input class="whyGreen"
					style="font-size: 12px; width: 100px; height: 25px;" type="text"
					id="beginTime" name="beginTime"
					onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01 ',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" />
				<span style="font-size: 16px;">至</span> <input class="whyGreen"
					style="font-size: 12px; width: 100px; height: 25px;" type="text"
					id="endTime" name="endTime"
					onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" />
				<button class="btn" onclick="Search()">查&nbsp;&nbsp;询</button>
			</div>

			<div class="cloud-configure">
				<div class="configure-table">
					<div class="configure">
						<table id="mytb">

						</table>
					</div>
				</div>
			</div>


		</div>
		<div class="main-bottom"></div>
	</div>
	<%@ include file="/foot.jsp"%>

	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/uapush.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/alert/jquery.alerts.js"></script>
	<script type="text/javascript" src="${ctx}/plugin/grid/grid.js"></script>
	<script type="text/javascript" src="${ctx}/plugin/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/plugin/date/WdatePicker.js"></script>
	<script type="text/javascript">
		var t;
		$(function() {
			$('#appId').combobox({
				url : '${ctx}/app/getDevApp',
				valueField : 'appId',
				textField : 'appName'
			});
			t = $('#mytb').grid({
				width : 700,
				msg : '数据加载中 请稍后…………',
				size : 10,
				url : '${ctx}/pushResult/getPushResultList',
				colModel : [ {
					"dataflie" : "IDATE",
					"headfile" : "时间",
					"width" : 50
				}, {
					"dataflie" : "TOTAL_USER",
					"headfile" : "总用户量",
					"width" : 70
				}, {
					"dataflie" : "NEW_REGISTER_USER",
					"headfile" : "新增用户",
					"width" : 80
				}, {
					"dataflie" : "SEND_COUNT",
					"headfile" : "(新增)发送次数",
					"width" : 80
				}, {
					"dataflie" : "SHOW_COUNT",
					"headfile" : "(新增)展示次数",
					"width" : 80
				}, {
					"dataflie" : "RATE",
					"headfile" : "转化率",
					"width" : 50
				}, {
					"dataflie" : "REVENUE",
					"headfile" : "收入",
					"width" : 50
				} ]
			});
		}); //ready

		function Search() {
			var appid = $('#appId').combobox('getValue');
			var beginTime = $('#beginTime').val();
			var endTime = $('#endTime').val();
			var params = {
				"appId" : appid,
				"startDate" : beginTime,
				"endDate" : endTime
			};
			$('#mytb').re(t, params);
		}
	</script>
</body>
</html>