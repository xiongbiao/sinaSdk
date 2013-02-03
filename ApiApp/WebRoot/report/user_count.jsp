<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.validator.GenericValidator"%>
<%@ include file="/commons/taglibs.jsp"%>
<%  
  request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表统计 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/reports.css" media="all" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/reports-print.css" media="print" />
</head>
<% 
String defaultDays = "0";
String content = "用户信息";
%>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>
				<div class="right_content" id="header">
					<h2><%=content %></h2>
					<div id="date-range"
						style="display: none; float: left; margin-left: 20px;">
						<span style="font-size: 16px;">时间</span> <input
							style="font-size: 12px; width: 80px; height: 25px;" type="text"
							id="startDate" name="startDate"
							onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01 ',dateFmt:'yyyyMMdd',alwaysUseStartDate:true})"
							readonly="readonly" /> <span style="font-size: 16px;">至</span> <input
							style="font-size: 12px; width: 80px; height: 25px;" type="text"
							id="endDate" name="endDate"
							onFocus="WdatePicker({skin:'whyGreen',endDate:'%y-%M-01',dateFmt:'yyyyMMdd',alwaysUseStartDate:true})"
							readonly="readonly" /> <span><button onclick="search()"
								style="width: 80px">查&nbsp;&nbsp;询</button>
						</span>
					</div>
					<div id="settings">
						<div id="search-container">
							<div id="current-search">
								<span>今天</span>
							</div>
							<ul id="searches" style="display: none;">
								<li><a href="javascript:void(0)"
									onclick="dateCondition('0' );" id="D-0">今天</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('1');" id="D-1">昨天</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('7');" id="D-7">本周</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('14');" id="D-14">上周</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('30');" id="D-30">本月</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('31');" id="D-31">上个月</a>
								</li>
								<!-- <li><a href="javascript:void(0)" onclick="dateCondition('90');" id="D-90">本季度</a></li> -->
								<li><a href="javascript:void(0)"
									onclick="dateCondition('365');" id="D-365">本年度</a>
								</li>
								<li><a href="javascript:void(0)"
									onclick="dateCondition('CUSTOMIZE');" id="D-CUSTOMIZE">自定义查询</a>
								</li>
							</ul>
						</div>
					</div>
					<div id="chart-container" style="clear: both">
						<input name="defaultDays" value="<%=defaultDays%>"
							id="defaultDays" type="hidden" /> <input type="hidden"
							name="reportFunction" id="reportFunction" value="userstatsbyhour" />
						<div id="chart" style="height: 400px;"></div>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/highcharts/highcharts.js"></script>
	<script type="text/javascript" src="${ctx}/plugin/date/WdatePicker.js"></script>
	<script type="text/javascript">
        var chart = null;
        var chartConfig = {
            allow_type: ['dev_new_user', 'dev_user_online', 'dev_user_active'],
            series_info: {
                dev_new_user : { text:'新增用户', index:0, color:'#2d6c8b' },
                dev_user_online : { text:'在线用户', index:1, color:'#e68b3b' },
                dev_user_active : { text:'活跃用户', index:2, color:'#99261c' }
            }
        };
        
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });
         
        $(function(){
            $(document).ready(function(){
                init();

            //    $('.btn-type').click(typeSelect);
            });
        });
        
        var init = function () {
           /*  $("#app-pushes").addClass($('#pushes').val()!=''?"active":'');
            $("#app-opens").addClass($('#opens').val()!=''?"active":'');
            $("#app-times").addClass($('#times').val()!=''?"active":''); */
            createChart();
            query('userstatsbyday');
        };

        var createChart = function(){
        	var seriesInfo = chartConfig.series_info;
        	
            chart = new Highcharts.Chart({
                title: '',
                loading: {},// 'load...',
                chart: {
                    renderTo: 'chart',
                    type: 'area'
                },
                credits: {
	               enabled: false
                },
                yAxis: [
                    {
                        minPadding: 0,
                    	title: { text: ''/*'推送数量'*/, style:{ color:seriesInfo['dev_new_user'].color } },
                    	labels: { style:{ color:seriesInfo['dev_new_user'].color } }
                    },
                    {
                        minPadding: 0,
                     	title: { text: ''/*'打开应用(次数)'*/, style:{ color:seriesInfo['dev_user_online'].color } },
                     	labels: { style:{ color:seriesInfo['dev_user_online'].color }}
                    },
                    {
                        minPadding: 0,
                        title: { text: ''/*'使用时长(分钟)'*/, style:{ color:seriesInfo['dev_user_active'].color } },
                    	labels: { style:{ color:seriesInfo['dev_user_active'].color } }
                    }
                ],
                xAxis: {
                	id: 'xAxis',
                    type: 'datetime',
                    showFirstLabel: true,
                    labels: {
                        formatter: dtFormat,
                        rotation: -65,
                        step: 1,
                        y: 40,
                        x: -5
                    }
                },
                tooltip: {
                    crosshairs: true,
                    formatter: function() {
                        var htmlStr = [];
                        htmlStr.push('<small>');
                        htmlStr.push(dtFormat(this.x));
                        htmlStr.push('</small><br />');
                        
                        $.each(this.points, function(i, point) {
                            htmlStr.push('<span style="color: ');
                            htmlStr.push(point.series.color);
                            htmlStr.push('">');
                            htmlStr.push(point.series.name);
                            htmlStr.push(': </span><span style="text-align: right"><b>');
                            htmlStr.push(point.y);
                            htmlStr.push('</b></span><br />');
                        });
                        return htmlStr.join('');
                    },
                    shared: true
                },
                plotOptions: {
                    area: {
                        fillOpacity: 0.1
                    }
                },
                series: [
		        ]
            });
        };
        
        var drawChart = function(data){
            if ( !data || chart==null ) { return false; }

            try{
                var serieses = data.series;
                var seriesNames = data.series_name;
				
                $(seriesNames).each(function(i, n){
                    var series = serieses[n];
                    if ( !series ) return false;

                    drawLine(series);
                });
            }catch(e){
                alert(e);
            }
        }

        var drawLine = function(series){
            $(series.data).each(function(i, n){
                n.x = dtParser(n.x);
                n.y = n.y*1;
            });
            var seriesCfg = chartConfig.series_info[series.id];
            $.extend(series, {
            	yAxis: seriesCfg.index,
            	name:seriesCfg.text,
            	color: seriesCfg.color
            });
            chart.addSeries(series);
        }

        var clearChart = function () {
            var seriess = chart.seriess;
            if (!!seriess) {
                $(seriess).each(function(i, n){
                    n.remove();
                });
            }
        }

        var redrawChart = function(data){
            clearChart();
            drawChart(data);
        }

        function query(reportId, reportType) {
            chart.showLoading();
            var params = $("#header div:visible input").serialize();
            if (params.length>0) {
            	params+='&';
            }
            params+='newUser=newUser&activeUser=activeUser&onlineUser=onlineUser';
            var startDate = $("#startDate").val();
            var endDate = $("#endDate").val();
            $.ajax({
                url : "${ctx}/report/viewReportCommon",
                data : params+ '&appId=<%=appId%>',
                type : "post",
                dataType: 'json',
                success : function(data){
                	chart.hideLoading();
                    if (!data){
                        alert('no data');
                        return false;
                    }
                	redrawChart.call(this, data);
                }
            });
        }
        var dtFormat = function(date){
        	var funType = $("#reportFunction").val();
        	var result = '';
        	
            if (!date){
        	   date = this.value;
            }
        	if ( funType=='userstatsbyhour' ){
        		result = dateTimeFormater(date);
        	} else if ( funType=='userstatsbyday' ){
        		result = dateFormater(date);
        	} else {
        		result = monFormater(date);
        	}
        	
        	return result;
        };
        
        var monFormater = function(d) { return Highcharts.dateFormat('%Y-%m', d); };
        var dateFormater = function(d) { return  Highcharts.dateFormat('%Y-%m-%d', d); };
        var dateTimeFormater = function(d) { return  Highcharts.dateFormat('%H:00', d); };
        
        var dtParser = function(data){
            var funType = $("#reportFunction").val();
            var result = null;
            
            if ( funType=='userstatsbyhour' ){
                result = dateTimeParser(data);
            } else if ( funType=='userstatsbyday' ){
            	result = dateParser(data);
            } else {
            	result = monParser(data);
            }
            
            return result;
        };
        var dateTimeParser = function(data) {
        	var D =  new Date();
        	var hh = data.split(':')[0];
        	D.setHours(hh);
        	D.setMinutes(0);
        	return D;
        };
        var dateParser = function(data) {
            var yyyy = data.substr(0,4);
            var mm = data.substr(4,2);
            var dd = data.substr(6,2);
            var D = new Date(yyyy,mm-1,dd).setHours(0);

            return D;
        };
        var monParser = function(data){
        	var yyyy = data.substr(0, 4);
        	var mm = data.substr(4, 2);
        	var D = new Date(yyyy, mm-1, 1).setHours(0);
        	return D;
        };


        function search() {
            var startDateStr = $('#startDate').val();
            var endDateStr = $('#endDate').val();
            
            if ( startDateStr!='' && endDateStr!='' ) {
                var startDate = new Date( startDateStr.substring(0,4), startDateStr.substring(4,6), startDateStr.substring(6,8) );
                var endDate = new Date( endDateStr.substring(0,4), endDateStr.substring(4,6), endDateStr.substring(6,8) );
                var days = (endDate.getTime() - startDate.getTime())/(24*60*60*1000) +1;
                
                if(days<1) {
                    alert("结束时间不能小于开始时间");
                } else {
                    if(days==1) {
                    	var xAxis = chart.get('xAxis');
                        $("#reportFunction").val("userstatsbyhour");
                    } else if(days<31) {
                        $("#reportFunction").val("userstatsbyday");
                    } else {
                        $("#reportFunction").val("userstatsbymonth");
                    }
                    init();
                }
            }
        }

        $("#current-search").click(function(e){
            e.stopPropagation();
            var searches = $("#searches");
            if (searches.is(":hidden")) {
                $("#searches").fadeIn();
            } else {
                $("#searches").fadeOut();
            }
        });
        $("body").click(function(){
            $("#searches").fadeOut();
        });
        
        function dateCondition(dateId) {
            $("#current-search").html("<span>"+$("#D-"+dateId).html()+"</span>");
            if(dateId == "CUSTOMIZE") {
                $("#date-range").fadeIn();
                $("#defaultDays").val("7");
            } else {
                $("#current-search").html("<span>"+$("#D-"+dateId).html()+"</span>");
                
                $("#date-range").fadeOut();
                $("#defaultDays").val(dateId);
                dataId = parseInt(dateId);
                if(dateId<=1) {
                    $("#reportFunction").val("userstatsbyhour");
                } else if(dateId < 90) {
                    $("#reportFunction").val("userstatsbyday");
                } else {
                    $("#reportFunction").val("userstatsbymonth");
                }
                init();
            }
        }
        
        /* function typeSelect() {
            var type = this.id.replace('app-', '');

            var value = $("#"+type).val();
            if ( $(this).hasClass('active') ){
                $('#'+type).val('');
                $('#app-'+type).removeClass("active");
            }else{
                $('#'+type).val(type);
                $('#app-'+type).addClass("active");
            }
            init();
        } */
        
        
    </script>
</body>
</html>