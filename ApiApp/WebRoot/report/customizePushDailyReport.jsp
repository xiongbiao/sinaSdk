<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%@ include file="/commons/permission.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<html lang="zh">
<head>
<title>自定义推送报表 - ${Title}</title>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />

<link rel="stylesheet" href="${ctx}/plugin/alert/jquery.alerts.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/plugin/themes/default/easyui.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/skin/default/appsetting.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/plugin/kk/css/el-grid.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/skin/default/page.css"
	type="text/css" />
<link rel="stylesheet" href="${ctx}/skin/default/uapush.css"
	type="text/css" />
</head>

<body>
	<%@ include file="/top.jsp"%>
	<jsp:include page="/nav" flush="true" />

	<div id="main">
		<div id="" class="main-title">自定义推送报表</div>
		<div id="content" class="main-content">
			<div id="divSearch">
				<span style="font-size: 16px;">选择应用</span> <select id="appId"
					class="easyui-combobox" name="appId" style="width: 120px;">
					<option value="">-- 请选择应用 --</option>
				</select> <span style="font-size: 16px;">消息标题</span> <input id="adTitle"
					type="text" name="adTitle" style="width: 80px; height: 25px" /> <span
					style="font-size: 16px;">时间</span> <input class="whyGreen"
					style="font-size: 12px; width: 80px; height: 25px;" type="text"
					id="beginTime" name="beginTime"
					onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01 ',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" />
				<span style="font-size: 16px;">至</span> <input class="whyGreen"
					style="font-size: 12px; width: 80px; height: 25px;" type="text"
					id="endTime" name="endTime"
					onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" />
				<button class="btn" onclick="Search()" style="width: 80px">查&nbsp;&nbsp;询</button>
			</div>

			<div id="dataGrid"></div>

		</div>
		<div class="main-bottom"></div>
	</div>
	<%@ include file="/foot.jsp"%>

	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/uapush.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/alert/jquery.alerts.js"></script>
	<script type="text/javascript" src="${ctx}/plugin/kk/el-grid.js"></script>
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

			t = $('#dataGrid').ELGrid({
		        //width:685,
		        msg:'数据加载中, 请稍后…………',
		        pageSize:5,
				url : '${ctx}/report/customizePushDailyReport',
				colModel : [ {
					'field' : 'IDATE',
					'label' : '时间',
					'width' : 80
				}, {
					'field' : 'APP_NAME',
					'label' : '应用名称',
					'width' : 210
				}, {
					'field' : 'MSG_TITLE',
					'label' : '广告名称',
					'width' : 210
				}, {
					'field' : 'ARRIVAL_COUNT',
					'label' : '送达数',
					'width' : 100
				}, {
					'field' : 'CLICK_COUNT',
					'label' : '点击数',
					'width' : 98
				}]
		    });
		}); //ready

		function Search() {
			var appid = $('#appId').combobox('getValue');
			var beginTime = $('#beginTime').val();
			var endTime = $('#endTime').val();
			var adTitle = $('#adTitle').val();
			var params = {
				"appId" : appid,
				"startDate" : beginTime,
				"endDate" : endTime,
				"adTitle":adTitle
			};
			t.load(params);
		}
	</script>
</body>
</html>