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

				<div class="right_content" style="width: 72%;">
					<h2>消息推送历史记录</h2>
					<table class="list">
						<thead>
							<tr>
								<th style="width: 20%;">发送时间</th>
								<th style="">消息内容</th>
								<th style="width: 12%;">接收者</th>
								<th style="width: 10%;">发送结果</th>
								<th style="width: 10%;">满足条件</th>
								<th style="width: 10%;">推送成功</th>
                                <th style="width: 6%;">来源</th>
								<th style="width: 12%;">操作</th>
							</tr>
						</thead>
						<tbody id="pushList">
						</tbody>
					</table>
					<div style="text-align: center;" id="loadstatus">数据加载中 ...</div>
				</div>

				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>

	<script type="text/javascript">
		   $(function(){
				function getReceiverType(type){
					var result = '';
					switch(type){
					case 4:
						result = "全部用户";
			    		 break;
					case 3:
						result = "别名";
			    		 break;
					case 1:
						result = "IMEI";
			    		 break;
					case 2:
						result = "标签";
			    	 	break;
			    	 default :
			    		 result = "-";
			    	 break; 	
					}
					return result;
				}
		      //加载消息列表pageIndex	1
		     jQuery.post("${ctx}/pushMsg/getPushList",{"pageIndex" : 1,"msgType":0,"appId":<%=appId %>,"isScheduled": false},  function(data) {

		    	 var str="";
		    	 if (data.rows.length > 0) {
			     for(var i=0;i<data.rows.length;i++){
			    	 var receiverType = data.rows[i].receiverType;
			    	 var totalUserStr = '' ;
			    	 var sendCntStr = '' ;
			    	 if(data.rows[i].errorCode == 0){
			    		totalUserStr= data.rows[i].totalUser;
			    		sendCntStr = data.rows[i].sendCount;
			    		if(data.rows[i].totalUser == -1){
			    			totalUserStr = '' ;
					    	sendCntStr = '' ;
			    		}
			    	 }
			    	 var rv = data.rows[i].receiverValue;
			    	 if(rv == null){
			    		 rv = '';
			    	 }
                     var rvnum = rv.split(',')
			    	 
			    	 var showrv = '';
			    	 if(rvnum.length>3){
			    		 for(var j = 0 ;j < 3;j++){
			    			 showrv += rvnum[j];
			    			 if(j == 0){
			    				 showrv += ',';
			    			 }
			    		 }
			    		 showrv += ',...';
			    	 }else{
			    		 showrv = rv;
			    	 }
			    	 if (showrv) {
			    		 if (showrv.length <= 8) {
			    			 showrv = '('+showrv+')';
			    		 } else {
			    			 showrv = '<br/>('+showrv+')';
			    		 }
			    		 
			    	 }
			        str = str+ ' <tr>' +
								'	<td> '+formatDate(data.rows[i].scheduledTime)+' </td>' +
								'	<td> '+data.rows[i].content+' </td>' +


								'	<td id="td_type"><span msg="'+rv+'">' + getReceiverType(receiverType) +showrv+'</span></td>' +

								'   <td>'+getErrmsg(data.rows[i].errorCode)+'</td>'+
							    '   <td>'+totalUserStr+'</td>'+
								'   <td>'+sendCntStr+'</td>'+
                                '   <td>'+(data.rows[i].sendSource?'api':'portal')+'</td>'+
								'	<td> <a href="${ctx}/push/new_Msg/<%=appId%>/'+data.rows[i].msgId+'.html">复制</a>'+
								'&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);" onclick="if(confirm(\'确认删除！\')){delMsg(this,'+data.rows[i].msgId+')};">删除</a>'+


								'   </td>' +
								'</tr>';
			     }
		    	 } else {
		    		 str += '<td colspan="8" align="center">没有任何发送历史记录！</td>';
		    	 }
				 $('#pushList').append(str);
				 $("#loadstatus").hide();
				 $('#td_type span').mouseover(function(e){
						var toolTip = "<div id='tooltip' width='100px' height='36px' style='position:absolute;border:solid #aaa 1px;background-color:#F9F9F9'>" + ($(this).attr("msg")||$(this).html()) + "</div>";
					       $("body").append(toolTip);
							$("#tooltip").css({
								"top" :e.pageY + "px",
								"left" :e.pageX + "px"
							});
					});
					$("#td_type span").mouseout(function(){
					      $("#tooltip").remove();
					});
					$("#td_type span").mousemove(function(e){
						$("#tooltip").css({
							"top" :(e.pageY+5) + "px",
							"left" :(e.pageX+2) + "px"
						});
					});
				}); //post
		    
		   });
		   function formatDate(time) {
				return new Date(time).format("MM/dd HH:mm:ss");
			}
		   function getErrmsg(code) {
				var result = "";
				switch (code) {
				case -1:
					result = "";
					break;
				case 0:
					result = "成功";
					break;
				case 1001:
					result = "不支持GET";
					break;
				case 1002:
					result = "缺少了必须的参数";
					break;
				case 1003:
					result = "参数值不合法";
					break;
				case 1004:
					result = "验证失败";
					break;
				case 1005:
					result = "消息体太大";
					break;
				case 1006:
					result = "用户名或者密码错误";
					break;
				case 1007:
					result = "参数不合法";
					break;
				case 1008:
					result = "appKey不合法";
					break;
				case 1009:
					result = "系统繁忙";
					break;
				case 1010:
					result = "消息内容不合法";
					break;
				case 1011:
					result = "没用满足条件的用户";
					break;
				default:
					result = "未知";
					break;
				}
				return result;
			}
		   Date.prototype.format = function(format) { 
				var o = { 
					"M+" : this.getMonth()+1, //month 
					"d+" : this.getDate(),    //day 
					"H+" : this.getHours(),   //hour 
					"m+" : this.getMinutes(), //minute 
					"s+" : this.getSeconds(), //second 
					"q+" : Math.floor((this.getMonth()+3)/3),  //quarter 
					"S" : this.getMilliseconds() //millisecond 
				};
				if(/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
				for (var k in o) {
					if(new RegExp("("+ k +")").test(format)) { 
						format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
					}
				}
				return format; 
			}

		   function delMsg(obj,msgId){
			   jQuery.post("${ctx}/pushMsg/delete",{"msgids":msgId},  function(data) {
				   if(data && data.success){
					   $(obj).parent().parent().remove();
				   }
			   });
		   }
		 
		</script>
</body>
</html>