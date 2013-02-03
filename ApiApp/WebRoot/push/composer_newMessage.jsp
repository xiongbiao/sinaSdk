<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>新增推送通知 - ${Title}</title>
<%@include file="/includes/style.jsp"%>


<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/push.css" media="screen">
<style type="text/css">
 	#builderId,#sendRecipients{margin-bottom:5px;}
	#preview-scroller ul.list {
		padding: 0;
		margin: 0;
		list-style: none;
		position: absolute;
		width: 9999px;
		height: 100%;
	}
	#preview-scroller ul.list li {
		float: left;
		width:700px;
		height: 100%;
		padding:0px;
	}
	#preview-scroller .list img{
		width: 100%;
		height: 100%;
		border: 0;
	}
	#preview-scroller ul.playerControl {
		margin: 0;
		padding: 0;
		list-style: none;
		position: absolute;
		bottom: 5px;
		right: 5px;
		height: 14px;
	}
	#preview-scroller ul.playerControl li {
		float: left;
		width: 10px;
		height: 10px;
		cursor: pointer;
		margin: 0px 2px;
		background: url(images/cir_ctrl.png) no-repeat -10px 0;
	}
	#preview-scroller ul.playerControl li.current { 
		background-position: 0 0;
	}
	#recipient-choices,#delivery-date-container {
		position: relative;
		top: 2px;
	}
	div#optional-settings div.bare-box {margin-bottom:10px;}
</style>

</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>
				<div class="right_content">
					<h2>新通知</h2>
					<div class="protip" style="display: none;" id="id_protip">
						<strong>提示:</strong> <span id="id_no_package"> </span><br>
					</div>
					<div id="id_form_c" style="display: none;">
						<form method="post" id="myForm" class="confirm_this" onsubmit="return rForm()">
							<div style="display: none">
								<input type="hidden" name="isPushEnable" id="isPushEnable"
									value="1"> <input type="hidden" name="appId" id="appId"
									value="<%=appId%>"> 
									<input type="hidden" name="msgType" id="msgType" value="1">
							</div>
							<div class="bare-box">
								<h4 class="leader">1、通知标题（可选，仅适应于 Android）</h4>
								<div id="step-1">
									<div class="bare-box">
										<ul class="form">
											<li><input id="adTitle" type="text" name="adTitle"
												onkeydown="gbcount()" onkeyup="gbcount()"
												placeholder="仅适应于 Android。不填则默认使用应用名称。"></input></li>
											<li id="preview-chars-title">还可以输入30个汉字</li>
										</ul>
									</div>
								</div>
								<h4 class="leader">2、通知内容（必须）</h4>
								<div id="step-1">
									<div class="bare-box">
										<ul class="form">
											<li><textarea id="adContent" onblur="blur_adContent()"
													rows="2" cols="510" style="resize: none;" name="adContent"
													onkeydown="gbcount()" onkeyup="gbcount()"></textarea></li>
											<li id="preview-chars">还可以输入40个汉字</li>
										</ul>
									</div>
									<!--  -->
									<div id="previews">
										<a id="preview-scroller-prev"  onclick="previous();return false"  href="javascript:void(0)">Previous</a>
										
										<div id="preview-scroller">
                                         <ul class="list" id="id_list">
	                                          <li id="li_android"><div class="preview" id="preview-android">
													<span style="display: block;" class="show-content_android"></span>
													<h3 class="show-title_android"></h3>
													<p class="show-content_android"></p>
												</div>
											 </li>
	                                          <li id="li_ios"> 
	                                            <div class="preview" id="preview-ios">
													<span style="display: block;" id="show-title_ios1"></span>
													<!-- <h3>应用名称</h3> -->
													<p id="show-content_ios"></p>
												</div>
											 </li>
                                         </ul>
										</div>
										<a id="preview-scroller-next" onclick="next(); return false"   href="#">Next</a>
										<div id="preview-position">
											 <span class="active" id="android_click">•</span><span id="ios_click" class="previews">•</span>  
										</div>
									</div>
								</div>
								<!-- 高级：通知标题（可选）、通知模板（可选） -->
								<h4>3、选择接收人</h4>
								<div class="bare-box">
									<ul class="form" id="device-specific"
										style="border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(204, 204, 204);">
										<li class="leader">
										<span id = "form_ios">
										<input  type="checkbox" name="ios" value="ios" id="ios" checked="checked"
											onclick="pfrom()"> <label for="id_aps"> <span
												class="icon apple"></span> iOS
										</label> 
										</span>	
										<span id = "form_android">	
										 <input   type="checkbox" name="android" checked="checked"
											value="android" id="android" onclick="pfrom()"> <label
											for="id_android"> <span class="icon android"></span>
												Android
										</label>
										</span>
										</li>
									</ul>
									<ul class="form">
										<li id="recipient-choices"><label
											for="id_recipient_type_0"> <input type="radio"
												id="id_recipient_type_0" onclick="showTag(this)" value="0"
												name="recipientType" checked="checked"> 所有人（广播）
										</label> 
                                        <label for="id_recipient_type_3"> <input type="radio"
                                                id="id_recipient_type_3" onclick="showTag(this)" value="3"
                                                name="recipientType"> 别名
                                        </label>
										<label for="id_recipient_type_1"> <input type="radio"
												id="id_recipient_type_1" onclick="showTag(this)" value="2"
												name="recipientType"> 标签
										</label> 
										<label for="id_recipient_type_2"> <input type="radio"
												id="id_recipient_type_2" onclick="showTag(this)" value="1"
												name="recipientType"> IMEI
										</label> 
										</li>
										<li id="broadcast-notice" class="revealed-field"></li>
										<li id="tag-search-box" class="revealed-field">
											<div id="tag-search-container">
												<input type="hidden" name="tags" id="id_tags"> <input
													type="text" name="sendRecipients" id="sendRecipients"
													value="" placeholder="请填写标签" autocomplete="off" maxLength="100">
												&nbsp; <span id="sp_tag" style="display: none;">“标签”具体说明请参考：<a
													href="http://docs.jpush.cn/pages/viewpage.action?pageId=557241"
													target="_a">标签与别名API</a>
												</span> <span id="sp_imei" style="display: none;">“IMEI”具体说明：填写Android IMEI。由于有些 Android设备不能取到有效的 IMEI，建议只在测试时使用。　
												</span> <span id="sp_alias" style="display: none;">“别名”具体说明请参考：<a
													href="http://docs.jpush.cn/pages/viewpage.action?pageId=557241"
													target="_a">标签与别名API</a>
												</span>
												<ul id="tag-list">
												</ul>
												<div id="selected-tags"></div>
											</div>
										</li>
										<li id="token-container" class="revealed-field"><input
											autocomplete="off" name="device_token" maxlength="72"
											placeholder="Enter a Device ID" type="text"
											id="id_device_token"></li>
									</ul>
								</div>
								<div id="optional-settings">
									<h4 onclick="showKV()">
										<span>可选设置</span> <input type="hidden" name="custom_count"
											id="custom_count" value="1"> <input type="hidden"
											name="isShow" id="isShow" value="1">
									</h4>
									<div class="bare-box">
										<ul class="form">
											<li id="delivery-date-container">
											<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">4、确定发送时间</h5>
											<label
												for="pushTypejs"> <input checked="checked"
													type="radio" onclick="showTime(this)" id="pushTypejs"
													value="0" name="pushType"> 即时
											</label> <label for="pushTypeds"> <input type="radio"
													id="pushTypeds" onclick="showTime(this)" value="1"
													name="pushType"> 定时
											</label>
												<div id="setTime" class="revealed-field">
													<script language="javascript" type="text/javascript"
														src="${ctx}/plugin/date/WdatePicker.js"></script>
													<input class="whyGreen"
														style="font-size: 12px; width: 290px;" type="text"
														id="beginTime" name="beginTime"
														onFocus="WdatePicker({skin:'whyGreen',minDate:'%y-%M-%d %H:%m:%s',startDate:'%y-%M-%d %H:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})" />
												</div></li>
										</ul>
									</div>
									<div class="bare-box">
										<ul class="form">
											<li class="options">
												<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">5、通知栏样式编号（仅适用于 Android）</h5>
												<input id="builderId" type="text" name="builderId" class="custom_key" style="width: 150px;height:28px;" maxlength="4">
												<br/>
												<span id="sp_style">客户端自定义通知栏样式的编号，值为1到1000的数字，没设置则不填写，请参考：
													<a href="http://docs.jpush.cn/pages/viewpage.action?pageId=557243" target="_blank">Android通知栏样式定制 API。</a>
												</span>
											</li>
										</ul>
									</div>
									<div class="bare-box">
										<ul class="form">
											<li class="options">
												<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">6、离线消息保存时间（秒为单位）</h5>
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
										<ul class="form">
											<li class="options custom_fields">
												<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">7、IOS 扩展字段</h5>
												<label for="id_key1">
													badge：
												</label>
												<input id="badge" type="text" name="badge"
													class="custom_value" value ="" maxlength="10">
												<label for="id_key1">
													sound：
												</label>
												<input id="sound" type="text" name="sound"
													class="custom_value" maxlength="100">
													<br>
												<span id="sp_style">
												 sound:自定义通知铃声，铃声必须存在你的应用中。如：（jpush.mp3)
												</span>
											</li>
										</ul>
									</div>
									<div class="bare-box">
										<ul class="form"  id = "ul-setting">
											<li class="options custom_fields" id="custom_fields1" data-count="1">
												<h5 style="color: #19538A; font-size: 15px;margin-bottom: 5px;">8、 自定义内容</h5>
												<label for="id_key1">
													键
												</label>
												<input id="id_key1" type="text" name="custom_key1"
													class="custom_key" maxlength="255">
												<label for="id_value1">
													值
												</label>
												<input id="id_value1" type="text" name="custom_value1"
													class="custom_value" maxlength="255">
												 
												<a class="btn mini tertiary" rel="add_field"   onclick="addKeyValue()"
													id="add_fields1" >添加</a>
											</li>
										</ul>
										<span id="sp_mC" style="margin: 2px 5px 5px 5px;display: block;">  <a href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343602" target="_a">客户端获取自定义数据说明</a> </span>
									</div>
								</div>
								<!-- 说明：对于通知，模板放到这个可选段里。展开时可设置。 -->

								<div class="actions">
									<button type="submit" id="sub_form" disabled="disabled">
										发送通知</button>
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
		    	      if(data.info.appPackage == null|| data.info.appPackage == ""){
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
                    if(isProtip && isAndroid){
                    	  $("#id_protip").show();
		    	    	  $("#id_form_c").hide();
		    	    	  $("#id_no_package").html("你应用资料不全 。 通过 <a href='${ctx}/app/edit/"+appid+".html' > <span> 修改应用 </span> </a>来完善资料.");
                    }else{
                    	if(isAndroid){
                    		 $("#id_no_package").html("你应用还没有Android包名 ,Android用户不能接收到消息。 通过 <a href='${ctx}/app/edit/"+appid+".html' > <span> 修改应用 </span> </a>来填写你应用的包名.");
                    		 $("#id_protip").show();
                    		 $("#form_android").hide();
                    		 $("#preview-android").hide();
                    		 $("#android_click").hide();
                    		 $("#preview-scroller-next").attr("onclick", "return false");
                    		 $("#preview-scroller-prev").attr("onclick", "return false");
                    		  $("#li_android").remove();//删除指定属性的元素  
                    	}
                    	if(isProtip){
                    		 $("#id_protip").show();
                   		     $("#id_no_package").html("你应用还没有未上传APNs证书 , iOS用户不能接收到消息。 通过 <a href='${ctx}/app/edit/"+appid+".html' > <span> 修改应用 </span> </a>来上传你应用的APNs证书 .");
                   		     $("#form_ios").hide();
                   		     $("#preview-ios").hide();
                   		     $("#ios_click").hide();
                   		     $("#preview-scroller-next").attr("onclick", "return false");
                   		     $("#preview-scroller-prev").attr("onclick", "return false");
                   	     }
                    	if(!isAndroid&&!isProtip){
			    	    	  $("#id_protip").hide();
			    	    	  $("#id_no_package").html("");
                    	}
		    	    	  $("#id_form_c").show();
                    }
		    	     
		    	});
		    }
			
		   var msgid=<%=request.getParameter("msgid")%>
		   if(!(msgid==null||msgid=='')){
				jQuery.post("${ctx}/pushMsg/getMsg?msgid="+msgid,  function(data) {
		                $("#adTitle").val(data.info.title);
		                $(".show-title_android").html(data.info.title);
		                $("#show-title_ios").html(data.info.title);
				        $("#adContent").val(data.info.content);
				        $(".show-content_android").html(data.info.content);
				        $("#show-content_ios").html(data.info.content);
				        $("#beginTime").val(''); 
				        $("#sub_form").removeAttr("disabled");
				        $("#sendRecipients").val(data.info.receiverValue); 
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
				        	if (titleErr) {
				        		$("#adTitle").focus().select();
				        		return false;
				        	} else if (contentErr) {
				        		$("#adContent").focus().select();
				        		return false;
				        	} else {
					        	var builderId = $("#builderId").val();
					        	var timetolive = $("#timetolive").val();
					        	if (builderId) {
					        		if ((builderId.indexOf("0")!=0) && (!isNaN(builderId)) && (parseInt(builderId) <= 1000) && (parseInt(builderId) >= 1)) {
						        		
					        		} else {
					    				$("#optional-settings div.bare-box").show();
					    				$("#isShow").attr("value", 0);
					        			$("#builderId").select();
						        		$("#er").html("通知栏样式编号不合法");
						        		return false;
						        	}
					        	}
					        	if (timetolive) {
									if ((isNaN(timetolive)) || timetolive < 0) {
					    				$("#optional-settings div.bare-box").show();
					    				$("#isShow").attr("value", 0);
					        			$("#timetolive").select();
						        		$("#er").html("离线消息时间不合法");
						        		return false;
						        	}
					        		if(timetolive > 864000){
					    				$("#optional-settings div.bare-box").show();
					    				$("#isShow").attr("value", 0);
					        			$("#timetolive").select();
						        		$("#er").html("离线消息时间保存最大天数为10天。");
						        		return false;
						        	}
					        	}
				        	}
				        	$("#er").html("");
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
						                window.location = '${ctx}/push/composer_scheduledMessage.jsp?appId=<%=appId%>';
						     		}else{
						     			 window.location = '${ctx}/push/composer_history.jsp?appId=<%=appId%>';
						     		}
								 } else {
									 $("#er").html(data.info);
									}//if 
								}//function   
							});//ajax 
		});
		function pfrom() {
			var message = $('#adContent').val();
			if (message.length == 0 || (!$('#ios').attr("checked") && !$('#android').attr("checked"))) {
				$('#sub_form').attr("disabled", "disabled");
			} else {
				$("#sub_form").removeAttr("disabled");
			}
		}
		function rForm() {
			var isok = true;
			if (!$('#ios').attr("checked") && !$('#android').attr("checked")) {
				isok = false;
				//$("#er").html("请选择接收平台");
			} else {
				//$("#er").html("");
			}
			return isok;
		}
		function checktext(text) {
			allValid = true;
			for (i = 0; i < text.length; i++) {
				if (text.charAt(i) != " ") {
					allValid = false;
					break;
				}
			}
			return allValid;
		}
		var titleErr = false;
		var contentErr = false;
		function gbcount() {
			//var pushmsg=$('#pushmsg') ;
			//var pushmsg1=$('#pushmsg1') ;
			var showContent_android = $('.show-content_android');
			var showContent_ios = $('#show-content_ios');
			var previewChars = $('#preview-chars');
			var message = $('#adContent').val();
			var max = 40;
			var messageLen = Math.ceil(calcCount(message)/3);
			if (messageLen > max) {
				previewChars.html("<span style=\"color:red\">已经超过"+(messageLen-max)+"个汉字</span>");
				contentErr = true;
			} else {
				var ssi = max - messageLen;
				previewChars.html("还可以输入"+ssi+"个汉字");
				contentErr = false;
			}
			showContent_ios.html(message);
			showContent_android.html(message);
			if (messageLen == 0
					|| (!$('#ios').attr("checked") && !$('#android').attr(
							"checked"))) {
				$('#sub_form').attr("disabled", "disabled");
			} else {
				$("#sub_form").removeAttr("disabled");
			}
			//----------
			var showTitle_android = $('.show-title_android');
			var showTitle_ios = $('#show-title_ios');
			var previewCharsTitle = $('#preview-chars-title');
			var messageTitle = $('#adTitle').val();
			var maxTitle = 30;
			var titleLen = Math.ceil(calcCount(messageTitle)/3);
			if (titleLen > maxTitle) {
				previewCharsTitle.html("<span style=\"color:red\">已经超过"+(titleLen-maxTitle)+"个汉字</span>");
				titleErr = true;
			} else {
				var ssi = maxTitle - titleLen;
				previewCharsTitle.html("还可以输入"+ssi+"个汉字");
				titleErr = false;
			}
			showTitle_android.html(messageTitle);
			showTitle_ios.html(messageTitle);
		}
		function showKV() {
			var isshow = $("#isShow").val();
			if (isshow == 0) {
				$("#optional-settings .bare-box").hide();
				$("#isShow").attr("value", 1)
			} else {
				$("#optional-settings .bare-box").show();
				$("#isShow").attr("value", 0)
			}
		}
		function showTime(thi) {
		  
			var t = (thi.value == 0);
			if (t) {
				$("#setTime").hide();
			} else {
				$("#setTime").show();
			}
		}
		function showTag(thi) {
			var _checked = thi.value;
			if (_checked == 0) {
				$("#tag-search-box").hide();
			} else {
				$("#tag-search-box").show();
			}
			var selval = $(thi).attr("data-selval");
			$("#sendRecipients").val(selval||"");
			if (_checked == 2) {
				$("#sendRecipients").attr("placeholder", "请填写标签");
				$("#sp_tag").show();
				$("#sp_imei").hide();
				$("#sp_alias").hide();
			} else if (_checked == 1) {
				$("#sendRecipients").attr("placeholder", "请填写Android IMEI。建议只在测试时使用。");
				$("#sp_imei").show();
				$("#sp_tag").hide();
				$("#sp_alias").hide();
			} else if (_checked == 3) {
				$("#sendRecipients").attr("placeholder", "请填写别名");
				$("#sp_alias").show();
				$("#sp_tag").hide();
				$("#sp_imei").hide();
			}
		}
		function blur_adContent() {
			if ($('#adContent').val().length == 0) {
				$('#sub_form').attr("disabled", "disabled");
			} else {
				$("#sub_form").removeAttr("disabled");
			}
		}
		function addKeyValue() {
			var count = $("#custom_count").val();
			var v = parseInt(count) + 1;
			$("#custom_count").attr("value", v)
			$('#ul-setting')
					.append(
							'<li class="options custom_fields" id="custom_fields'+v+'" data-count="'+v+'"> '
									+ '		<label for="id_key'+v+'"> 键 </label>'
									+ '		<input id="id_key'+v+'" type="text" name="custom_key" class="custom_key" maxlength="255">'
									+ '		<label for="id_value'+v+'">值</label>'
									+ '		<input id="id_value'+v+'" type="text" name="custom_value" class="custom_value" maxlength="255">'
									+ '		<a class="btn mini tertiary" rel="kill_key" onclick="removeKey('
									+ v + ')" id="kill_key">删除</a>' + '	</li>')
		}
		function removeKey(id) {
			$("li").remove("#custom_fields" + id);
		}
		
		function next(){
			var $block = $('#preview-scroller'),
			$slides = $('ul.list', $block),
			_width = $block.width(),
			$li = $('li', $slides),
			_animateSpeed = 600 ;
			$slides.stop().animate({
				left: _width * 1 * -1
			}, _animateSpeed, function(){
			});
			
			var android_click = $('#android_click');
			var ios_click = $('#ios_click');
			android_click.removeClass('active');
			android_click.addClass('previews');
			ios_click.removeClass('previews');
			ios_click.addClass('active');
			return false;
		}
		
		function previous() {
			var $block = $('#preview-scroller'),
			$slides = $('ul.list', $block),
			_width = $block.width(),
			$li = $('li', $slides),
			_animateSpeed = 600 ;
			$slides.stop().animate({
				left: _width * 0 * -1
			}, _animateSpeed, function(){
			});
			
			var android_click = $('#android_click');
			var ios_click = $('#ios_click');
			android_click.removeClass('previews');
			android_click.addClass('active');
			ios_click.removeClass('active');
			ios_click.addClass('previews');
			return false;
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
		
		function valiedateTimeLive(v){
			if(v > 864000){
				$("#err_timelive").html("离线消息时间保存最大天数为10天。");
				return false;
			}else{
				$("#err_timelive").html("");
				return true;
			}
		}
	</script>
</body>
</html>