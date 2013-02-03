<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>新增推送自定义消息 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/push.css" media="screen">
<style>
#nBuilderId,#sendRecipients {
	margin-bottom: 5px;
}

#recipient-choices,#delivery-date-container {
	position: relative;
	top: 2px;
}

div#optional-settings div.bare-box {
	margin-bottom: 10px;
}
</style>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>
				<div class="right_content">
					<h2>自定义消息</h2>
					<div class="protip" style="display: none;" id="id_protip">
						<strong>提示:</strong> <span id="id_no_package"> </span><br>
					</div>
					<div id="id_form_c" style="display: none;">
						<form method="post" id="myForm" class="confirm_this"
							onsubmit="return rForm()">

							<input type="hidden" name="isPushEnable" id="isPushEnable"
								value="0"> <input type="hidden" name="appId" id="appId"
								value="<%=appId %>"> <input type="hidden" name="msgType"
								id="msgType" value="0">
							<div class="bare-box">
								<h4 class="leader">1、消息内容*</h4>
								<div id="step-1">
									<div class="bare-box">
										<ul class="form">
											<li><textarea id="adContent" onblur="blur_adContent()"
													rows="5" cols="510" style="resize: none;" name="adContent"
													onkeydown="gbcount()" onkeyup="gbcount()"
													placeholder="请知悉：自定义消息并不在客户端显示到通知栏里，查看日志可以看到。请参考相关文档处理。"></textarea>
											</li>
											<li id="preview-chars">还可以输入230个汉字</li>
										</ul>
									</div>
									<!--  -->

								</div>
								<!-- 高级：通知标题（可选）、通知模板（可选） -->
								<h4>2、选择接收人</h4>
								<div class="bare-box">
									<ul class="form" id="device-specific"
										style="border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(204, 204, 204);">
										<li class="leader">
											<!--<input checked="checked" type="checkbox" name="ios" id="ios" value="ios"  onclick="pfrom()">
											<label for="id_aps">
												<span class="icon apple"></span> iOS
											</label>
											--> <input checked="checked" type="checkbox" name="android"
											value="android" id="android" onclick="pfrom()"> <label
											for="id_android"> <span class="icon android"></span>
												Android </label></li>
									</ul>
									<ul class="form">
										<li id="recipient-choices"><label
											for="id_recipient_type_0"> <input type="radio"
												id="id_recipient_type_0" onclick="showTag(this)" value="0"
												name="recipientType" checked="checked"> 所有人（广播） </label> <label
											for="id_recipient_type_3"> <input type="radio"
												id="id_recipient_type_3" onclick="showTag(this)" value="3"
												name="recipientType"> 别名 </label> <label
											for="id_recipient_type_1"> <input type="radio"
												id="id_recipient_type_1" onclick="showTag(this)" value="2"
												name="recipientType"> 标签 </label> <label
											for="id_recipient_type_2"> <input type="radio"
												id="id_recipient_type_2" onclick="showTag(this)" value="1"
												name="recipientType"> IMEI </label></li>
										<li id="broadcast-notice" class="revealed-field"></li>
										<li id="tag-search-box" class="revealed-field">
											<div id="tag-search-container">
												<input type="hidden" name="tags" id="id_tags"> <input
													type="text" name="sendRecipients" id="sendRecipients"
													value="" placeholder="请填写标签" autocomplete="off"
													maxLength="100"> &nbsp; <span id="sp_tag"
													style="display: none;">“标签”具体说明请参考：<a
													href="http://docs.jpush.cn/pages/viewpage.action?pageId=557241"
													target="_a">标签与别名API</a> </span> <span id="sp_imei"
													style="display: none;">“IMEI”具体说明：填写Android
													IMEI。由于有些 Android设备不能取到有效的 IMEI，建议只在测试时使用。 </span> <span
													id="sp_alias" style="display: none;">“别名”具体说明请参考：<a
													href="http://docs.jpush.cn/pages/viewpage.action?pageId=557241"
													target="_a">标签与别名API</a> </span>
												<ul id="tag-list">
												</ul>
												<div id="selected-tags"></div>
											</div></li>
										<li id="token-container" class="revealed-field"><input
											autocomplete="off" name="device_token" maxlength="72"
											placeholder="Enter a Device ID" type="text"
											id="id_device_token"></li>
									</ul>
								</div>
								<div id="optional-settings">
									<h4 onclick="showKV()">
										可选设置 <input type="hidden" name="custom_count"
											id="custom_count" value="1"> <input type="hidden"
											name="isShow" id="isShow" value="1">
									</h4>
									<div class="bare-box">
										<ul class="form">
											<li id="delivery-date-container">
												<h5
													style="color: #19538A; font-size: 15px; margin-bottom: 5px;">3、确定发送时间</h5>
												<label for="pushTypejs"> <input checked="checked"
													type="radio" onclick="showTime(this)" id="pushTypejs"
													value="0" name="pushType"> 即时 </label> <label
												for="pushTypeds"> <input type="radio"
													id="pushTypeds" onclick="showTime(this)" value="1"
													name="pushType"> 定时 </label>
												<div id="setTime" class="revealed-field"
													style="margin: 3px;">
													<script language="javascript" type="text/javascript"
														src="${ctx}/plugin/date/WdatePicker.js"></script>
													<input class="whyGreen"
														style="font-size: 12px; width: 290px;" type="text"
														id="beginTime" name="beginTime"
														onFocus="WdatePicker({skin:'whyGreen',startDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})" />
												</div></li>
										</ul>
									</div>
									
									<div class="bare-box">
										<ul class="form">
											<li class="options custom_fields" data-count="1"><label
												for="id_value1">
													<h5
														style="color: #19538A; font-size: 15px; margin-bottom: 5px;">4、
														标题</h5> </label> <input id="adTitle" type="text" name="adTitle"
												class="custom_value" onkeydown="titlecount()"
												onkeyup="titlecount()" style="width: 88%;">
												<div id="preview-title-chars" style="margin: 5px 0 0 3px;">还可以输入20个汉字</div>
											</li>
										</ul>
									</div>
									
									<div class="bare-box">
										<ul class="form">
											<li class="options">
												<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">5、离线消息保存时间（秒为单位）</h5>
												<select name="def_timetolive" id="def_timetolive" style="width:100px" onchange="selectChnage(this.value)">
												
													<option value="86400" selected="selected">-默认-</option>
													<option value="0">不保存</option>
													<option value="60">1分钟</option>
													<option value="600">10分钟</option>
													<option value="3600">1小时</option>
													<option value="10800">3小时</option>
													<option value="21600">6小时</option>
													<option value="43200">12小时</option>
													<option value="86400" >1天</option>
													<option value="259200" >3天</option>
													<option value="864000">10天</option>
													<option value="-1">自定义时间</option>
												</select>&nbsp;&nbsp;
												<input id="timetolive"  type="hidden" name="timetolive"  class="custom_key" style="width: 150px; height:28px;" maxlength="6">
												<span id="span_timetolive" style="display:none">
												：<input id="cus_timetolive"  type="text" name="cus_timetolive" value="" class="custom_key" oninput="$('#timetolive').val(this.value)"
												style="width: 150px;height:28px;" maxlength="6">（秒）
													<span id="err_timelive" style="color:red"> </span>
												</span>
												<br/>
												 <span id="sp_style">
													默认为保存1天的离线消息（86400秒）,最多支持10天（864000秒）
												</span> 
											</li>
										</ul>
									</div>
									<div class="bare-box">
										<ul class="form" id="ul-setting">

											<li class="options custom_fields" id="custom_fields1"
												data-count="1"><input type="hidden" name="custom_keys"
												id="id_custom_keys"> <label style="width: 100%;"
												for="id_value1">
													<h5
														style="color: #19538A; font-size: 15px; margin-bottom: 5px;">6、
														自定义内容</h5> </label> <label for="id_key1"> 键 </label> <input
												id="id_key1" type="text" name="custom_key1"
												class="custom_key" maxlength="255"> <label
												for="id_value1"> 值 </label> <input id="id_value1"
												type="text" name="custom_value1" class="custom_value"
												maxlength="255"> <a class="btn mini tertiary"
												rel="add_field" onclick="addKeyValue()" id="add_fields1">添加</a>
											</li>
										</ul>
										<span id="sp_mC"
											style="margin: 2px 5px 5px 5px; display: block;"> <a
											href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343602"
											target="_a">客户端获取自定义数据说明</a> </span>
									</div>
									
									
								</div>
								
								<!--
								说明 ：对于通知，模板放到这个可选段里。展开时可设置。
								-->
								<div class="actions">
									<button type="submit" id="sub_form" disabled="disabled">
										发送消息</button>
									<span id="er" style="color: red;"></span>
								</div>
							</div>
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
		$(function(){ 
			//提示消息
			if(appid!=null){
		    	jQuery.post("/app/getApp?appId="+appid,  function(data) {
		    	      $("#appPackage").html(data.info.appPackage);
		    	      var isProtip = false ;
		    	      var isAndroid = false;
		    	      var errStr ;
		    	     
		    	      if(data.info.appPackage==null||data.info.appPackage==""){
		    	    	  isAndroid =true;
		    	      }  
		    	      if (data.info.appStage == 0) {
						if (data.info.appleDataTest==null) {
							isProtip = true;	
						}
					 } else {
						if (data.info.appleData==null) { 
							isProtip = true;
						}
					} 
                    if( isAndroid){
                    	$("#id_protip").show();
		    	   		$("#id_form_c").hide();
		    	    	$("#id_no_package").html("本功能只支持 Android平台。您的Android应用资料不全，通过 <a href='${ctx}/app/edit/"+appid+".html' > <span> 修改应用 </span> </a>来完善资料。");
                    }else{
                    	$("#id_protip").show();
			    	    $("#id_no_package").html("本功能只支持 Android平台。");
		    	    	$("#id_form_c").show();
                    }
		    	     
		    	});
		    }
		 var msgid=<%=request.getParameter("msgid")%>
		   if(!(msgid==null||msgid=='')){
				jQuery.post("${ctx}/pushMsg/getMsg?msgid="+msgid,  function(data) {
				        $("#adContent").val(data.info.content);
				       // $("#msgType").val(data.info.msgType);
				        $("#beginTime").val(''); 
				        $("#sub_form").removeAttr("disabled");
				        $("#sendRecipients").val(data.info.receiverValue);
				        $("#adTitle").val(data.info.title);
				        var strs= new Array();  
				        strs=data.info.platform.split(",");  
				        for (var i=0;i<strs.length ;i++ )   
				        {   
				        	 if(strs[i]=="android"){
					        	 $("#android").attr("checked",'checked');
				        	 }else if(strs[i]=="ios"){
					        	 $("#ios").attr("checked",'checked');
				        	 }
				        } 
				        $("input[name=pushType][value=0]").attr("checked",true);
				        $("input[name=recipientType][value="+data.info.receiverType+"]").attr("data-selval", data.info.receiverValue).trigger("click");
				}); //post
		    };     
				     $('#myForm').ajaxForm({  
				        url:'${ctx}/pushMsg/save',
				        beforeSubmit: function() {
				        	if (contentErr) {
				        		$("#adContent").focus().select();
				        		return false;
				        	} else if (titleErr) {
				        		$("#adTitle").focus().select();
				        		return false;
				        	}
				        	
				        	$('#sub_form').attr("disabled", "disabled");
				        	return true;
				        },
				        failure: function() {
				        	$('#sub_form').removeAttr("disabled");
				        },
				        success: function(data){   
				        	$('#sub_form').removeAttr("disabled");
						     var r=data.success;
						     var clothType = $("input[name='pushType']:checked").val();
						     if(r=="true"){ 
						    	 if(clothType==1){
						    		  window.location = '${ctx}/push/msgComposer_message_schedule.jsp?appId=<%=appId %>';
						     		}else{
						     		  window.location = '${ctx}/push/msgComposer_message_history.jsp?appId=<%=appId %>';
						     		}
						     }else{
						        $("#er").html(data.info);
						     } 
				       }   
				    }); 
	       	})
	       	function pfrom(){
	         	 var message = $('#adContent').val(); 
				if(message.length==0||(!$('#ios').attr("checked")&&!$('#android').attr("checked"))){
		               $('#sub_form').attr("disabled","disabled");
		          }else{
		                 $("#sub_form").removeAttr("disabled");
		          }
				}
	       	function rForm(){
		   var isok = true;
          if(!$('#ios').attr("checked")&&!$('#android').attr("checked")){
             isok  = false;
            //  $("#er").html("请选择接收平台");
          }else{
              //$("#er").html("");
          }
		    return isok;
		}
			function checktext(text)
			{
			   allValid = true;
			   for (i = 0; i < text.length; i++)
				  {
				    if (text.charAt(i) != " ")
					{
						 allValid = false;
						  break;
					 }
					 }
				return allValid;
				}		
		var contentErr = false;
		function gbcount(){
			 var showContent =$('#show-content') ;
			 var previewChars=$('#preview-chars') ;
			 var message = $('#adContent').val(); 
			 var max = 230;
			 var messageLen = Math.ceil(calcCount(message)/3);
			if (messageLen > max) {
				previewChars.html("<span style=\"color:red\">已经超过"+(messageLen-max)+"个汉字</span>");
				contentErr = true;
			}else{
				var ssi = max - messageLen;
				previewChars.html("还可以输入"+ssi+"个汉字");
				contentErr = false;
			}
			showContent.html(message);
			
			if(message.length==0||(!$('#ios').attr("checked")&&!$('#android').attr("checked"))){
		          $('#sub_form').attr("disabled","disabled");
		      } else{
		          $("#sub_form").removeAttr("disabled");
		      }
		}
		var titleErr = false;
		function titlecount(){
			 var previewChars=$('#preview-title-chars') ;
			 var title = $('#adTitle').val(); 
			 var max = 30;
			 var titleLen = Math.ceil(calcCount(title)/3);
			if (titleLen > max) {
				previewChars.html("<span style=\"color:red\">已经超过"+(titleLen-max)+"个汉字</span>");
				titleErr = true;
			}else{
				var ssi = max - titleLen;
				previewChars.html("还可以输入"+ssi+"个汉字");
				titleErr = false;
			}
		}
			
		function showKV(){
		var isshow =  $("#isShow").val();
		
		if(isshow==0){
		    $("#optional-settings .bare-box").hide();
		    $("#isShow").attr("value",1)
		 }else{
		    $("#optional-settings .bare-box").show();
		    $("#isShow").attr("value",0)
		 }       
		}	
	     function showTime(thi){
			 var t = (thi.value ==0);
			 if(t){
			    $("#setTime").hide(); 
		     }else{
			    $("#setTime").show();
			 }		 	
		}
			
	    function showTag(thi){
	         var _checked = thi.value;
					 if(_checked == 0){
					    $("#tag-search-box").hide(); 
				     }else {
					    $("#tag-search-box").show();
					 } 
					 var selval = $(thi).attr("data-selval");
					 $("#sendRecipients").val(selval||"");
					  if(_checked==2){
					    $("#sendRecipients").attr("placeholder","请填写标签");
					    $("#sp_tag").show();
					    $("#sp_imei").hide();
					    $("#sp_alias").hide();
					 }else if(_checked==1){
					    $("#sendRecipients").attr("placeholder","请填写Android IMEI。建议只在测试时使用。");
					    $("#sp_imei").show();
					    $("#sp_tag").hide();
					    $("#sp_alias").hide();
					 } else if(_checked==3){
					    $("#sendRecipients").attr("placeholder","请填写别名");
					    $("#sp_alias").show();
					    $("#sp_tag").hide();
					    $("#sp_imei").hide();
					 } 
	    }
	    
	    function addKeyValue(){
	         var count = $("#custom_count").val();
	         var v =parseInt(count)+1;
	         $("#custom_count").attr("value",v);
	         $('#ul-setting').append('<li class="options custom_fields" id="custom_fields'+v+'" data-count="'+v+'"> ' +
										'		<input type="hidden" name="custom_keys" id="id_custom_keys">' +
										'		<label for="id_key'+v+'"> 键 </label>' +
										'		<input id="id_key'+v+'" type="text" name="custom_key'+v+'" class="custom_key" maxlength="255">' +
										'		<label for="id_value'+v+'">值</label>' +
										'		<input id="id_value'+v+'" type="text" name="custom_value'+v+'" class="custom_value" maxlength="255">' +
										'		<a class="btn mini tertiary" rel="kill_key" onclick="removeKey('+v+')" id="kill_key">删除</a>' +
										'	</li>');
	    
	    }			
	    function removeKey(id){
	       $("li").remove("#custom_fields"+id);
	    }			
	    
	    function  blur_adContent(){
	     if( $('#adContent').val().length==0){
	          $('#sub_form').attr("disabled","disabled");
	      } else{
	         $("#sub_form").removeAttr("disabled");
	      }
	    }
		function calcCount(str) {
			str = str.replace(/[\u4e00-\u9fa5]/g, "aaa").replace(/[^\x00-\xff]/g,"aaa");
			return str.length;
		}
		
		function selectChnage(v){
			if(v == -1){
				$("#span_timetolive").show();
				
			}else{
				$("#span_timetolive").hide();
			}
			$("#timetolive").val(v);
		}
</script>
</body>
</html>