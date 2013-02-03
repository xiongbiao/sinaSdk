<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>消息任务 - ${Title}</title>
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
					<h2>发送中的通知</h2>
					<div id="tab-content">
						<form method="post" id="drafts-form">
							<div style="display: none">
								<input type="hidden" id="msgids" name="msgids" value="">
							</div>
							<fieldset id="tab-api-content" class="tab-content">
								<table class="list">
									<thead>
										<tr>
											<th style="width: 5%;"></th>
											<th style="width: 18%;">定时时间</th>
											<th style="">消息内容</th>
										</tr>
									</thead>
									<tbody id="pushList">
										<tr>
											<td colspan="3" align="center">数据加载中 ...</td>
										</tr>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="3">
												<button type="submit" onclick="sub()" id="sub_form"
													disabled="disabled">删除</button></td>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						</form>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
			function isShow(){
			  //alert($("#cb_"+id).attr("checked"))
			  var che=false;
			  $("input[name='cbox']").each(function(){
				  if(this.checked == true){
				     che=true;
				  }
				});
				if(!che){
				  $('#sub_form').attr("disabled","disabled");
				}else{
				  $("#sub_form").removeAttr("disabled");
				}
			}
			function sub(){
			  var str="";
				$("input[name='cbox']").each(function(){
				  if(this.checked == true){
				     str+=$(this).val()+"," ;
				  }
				});
		      $('#msgids').attr("value",str);
			}
		   $(function(){
		   
		    $('#drafts-form').ajaxForm({  
				        url:'${ctx}/pushMsg/delete',
				        success: function(data){   
						     var r=data.success;
						     if(r=="true"){ 
						         window.location = '${ctx}/push/composer_scheduledMessage.jsp?appId=<%=appId %>';
						     }else{
						        $("#appDescription").html(data.info);
						     }//if 
				       }//function   
				    });//ajax 
				    
		      //加载消息列表pageIndex	1
            
		     jQuery.post("${ctx}/pushMsg/getPushList",{"pageIndex": 1,"msgType":1,"appId":<%=appId %>,"isScheduled": true},  function(data) {
			     var str="";
			     for(var i=0;i<data.rows.length;i++){
			        str = str+ '   <tr>' +
							   '     <td><input type="checkbox" value="'+data.rows[i].msgId+'" id="cb_'+data.rows[i].msgId+'" onclick="isShow()" name="cbox"></td>' +
							   '     <td>'+formatDate(data.rows[i].scheduledTime)+'</td>' +
							   '     <td>'+data.rows[i].content+'</td>' +
							   '   </tr>';
			     }
				 $('#pushList').html(str)
				}); //post
		    
		   });
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
		   function formatDate(time) {
				return new Date(time).format("MM/dd HH:mm:ss");
			}
		 
		</script>

</body>
</html>