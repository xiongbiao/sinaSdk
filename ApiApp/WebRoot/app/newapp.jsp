<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>新增应用 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<h2>创建应用程序</h2>
				<form method="post" action="${ctx}/app/saveApp" id="newAppForm"
					enctype="multipart/form-data" class="blueform"
					onsubmit="return vForm()">
					<ul class="form">
						<li><label for="id_name"> 应用名称 </label> <input type="text"
							value="" id="appName" name="appName" onblur="blur_appName()"
							placeholder="例如：海绵宝宝" datatype="s1-1000" errormsg="应用名称不能为空！" />
							<span class="red">*</span> <span id="appNameTip"></span>
						</li>
						<li><label for="id_icon"> 应用图标 </label> <input type="file"
							name="appIcon" id="appIcon" nselectable="on"
							onchange='checkFileType(this,".png、.jpg、.gif");'> <span
							id="appIconTip" class="red"></span>
							<div class="Validform_checktip"></div>
						</li>
						<li><label for="id_name"> 应用包名 (Android) </label> <input
							type="text" value="" id="appPackage" onblur="blur_appPackage()"
							placeholder="例如：cn.g.haimian 请确保与您的Android应用包名相同"
							name="appPackage" class="inputxt"
							ajaxurl="/app/isExistsAppPackage" datatype="s1-1000"
							errormsg="Android 包名不能为空!" /> <span id="appPackageTip">包名填写后不可更攺。</span>
						<li><span class="blue1">特别提示：这里填写的包名，必须与您自己的
								Android客户端应用包名保持一致。</span></li>
						</li>
						<li><label for="id_name"> APNs证书文件 (iOS) </label> <input
							type="file" name="appleCertificate" id="appleCertificate"
							nselectable="on" onchange='checkFileType(this,".p12");'>
							<span id="appleCertificateTip" class="red"> </span>
						</li>
						<li><span class="blue1">APNs证书文件：这是 iOS SDK 能够接收到
								JPush 推送消息的必要文件。具体请参考： <a
								href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343727"
								target="_a">iOS 证书设置指南</a>
						</span></li>
						<li><label for="id_name"> APNs证书密码 (iOS) </label> <input
							type="password" value="" id="certificatePass" autocomplete="off"
							name="certificatePass" class="inputxt" /> <span
							id="certificatePassTip" class="red"> </span> <!-- <span id="cfpTip">APNs证书密码。</span> -->
						</li>
						<li><label for="id_mode"> 部署状态</label> <!-- <input type="checkbox" id="appStage" name="appStage" checked /> 开发状态 -->
							<input type="radio" checked="checked" name="appStage" value="0">
							开发状态 <input type="radio" name="appStage" value="1"> 生产状态
						</li>
						<li><span class="blue1">开发状态：连接开发服务器。 生产状态：连接生产服务器。</span></li>
					</ul>
					<!-- PUSH -->
					<!--<ul class="form">
							<li>
								<label for="id_push_enabled">
									 上面的推送未开启时，这个字段不可点击，为 disabled 
									允许广告推送
								</label>
								<input type="checkbox" name="isPushAd" id="isPushAd">
							</li>
						</ul>
						-->
					<!-- SUBMIT -->
					<ul class="form">
						<li><input type="hidden" name="app_group" value="28212"
							id="id_app_group"> <input type="submit" id="btn_sub"
							value="创建应用程序"> <span id="er" style="color: red;"></span>
						</li>
						<li><small><span class="red">*</span> 表示必须填写。</small>
						</li>
					</ul>
				</form>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		//javascript去空格函数 

		function LTrim(str) { //去掉字符串 的头空格
			var i;
			for (i = 0; i < str.length; i++) {
				if (str.charAt(i) != " " && str.charAt(i) != " ")
					break;
			}
			str = str.substring(i, str.length);
			return str;
		}

		function RTrim(str) {
			var i;
			for (i = str.length - 1; i >= 0; i--) {
				if (str.charAt(i) != " " && str.charAt(i) != " ")
					break;
			}
			str = str.substring(0, i + 1);
			return str;
		}
		function Trim(str) {
			return LTrim(RTrim(str));
		}

		var result = false;
		function blur_appPackage(){
		   var result = false;
		   var  _value = $('#appPackage').val();
		    _value = Trim(_value);
		   if(_value.length>0){ 
		    $('#appPackageTip').html("<span >Android 包名检查中……</span>");
		    $.ajax({
                    type: "POST",
                    dataType:"json",
                    url: $('#appPackage').attr("ajaxurl"),
                    data: { "appid": null ,"name":$('#appPackage').attr("name"),"param":_value},
                    success: function(data){
                      result=data.success;
                      if(data.success=="true"){
                           $('#appPackageTip').html("<span >此包名可用</span>");
                      }else{
                           $('#appPackageTip').html("<span class='red'>"+data.info+"</span>");   
                      }
                    },
                    error: function(){
                            $('#appPackageTip').html("<span >服务器繁忙稍后再试……</span>");   
                    }
             }); 
		   }else{
			   $('#appPackageTip').html(""); 
			   return true;
		   }
		   return result;
		}
		function vType() {
			alert("appleCertificate")
		}
		function blur_appName() {
			var _value = $('#appName').val()
			if (_value.length == 0) {
				$('#appNameTip').html(
						"<span class='red'>" + $('#appName').attr("errormsg")
								+ "</span>")
				return false;
			} else {
				$('#appNameTip').html("");
				return true;
			}
		}
		function checkFileType(_this, typeValue) {
			var tv = typeValue.split("、");
			var iiOK = true;
			var str = _this.value;
			var pos = str.lastIndexOf(".");
			var lastname = str.substring(pos, str.length);
			var resultName = lastname.toLowerCase();
			for ( var i = 0; i < tv.length; i++) {
				if (tv[i] == resultName.toString()) {
					iiOK = false;
					break;
				}
			}
			if (iiOK) {
				$('#' + _this.name + 'Tip').html('请上传' + typeValue + '类型的文件');
				_this.value = "";
			} else {
				$('#' + _this.name + 'Tip').html("");
			}
		}

		function vForm() {
			$("#er").html("");
			var isSu = true;
			
			 if(!blur_appName()){
				    isSu = false;
				    return isSu;
		    }
			
			var acf = $("#appleCertificate").val();
			var acfPass = $("#certificatePass").val();
			if ((acf != "")) {
				if (acfPass == null || acfPass == "") {
					isSu = false;
					$("#certificatePassTip").html("请输入APNs证书密码");
				} else {
					$("#certificatePassTip").html("");
				}
			} else {
				$("#certificatePassTip").html("");
			}
			if(acfPass!=""){
				if (acf == null || acf == "") {
					isSu = false;
					$("#appleCertificateTip").html("你填写APNs证书密码 ,请上传APNs证书");
				} else {
					$("#appleCertificateTip").html("");
				}
			} else {
				$("#appleCertificateTip").html("");
			}
			if(isSu){
				  $('#btn_sub').attr("disabled","disabled");
			}
			else{
				  $('#btn_sub').removeAttr("disabled");
			}
			
			return isSu;
		}
		$(function() {
			$('#newAppForm').ajaxForm({
								url : '${ctx}/app/saveApp',
								dataType : 'json',
								success : function(data) {
									var r = data.success;
									if (r == true) {
										 if(data.ios!=1){
  										    window.location = '${ctx}/app/detail.jsp?appId=' + data.appId;
										 } else{
											 $("#er").html(data.info);
										 }
									} else {
										$("#er").html(data.info);
									}//if 
									 $('#btn_sub').removeAttr("disabled");
								},//function 
								error:function(){
									alert("错误");
								}		
							});//ajax 
		})
	</script>
</body>
</html>
