<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>编辑应用 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>
				<div class="right_content">
					<h2>修改应用</h2>
					<div class="protip">
						<strong>提示:</strong> 如果你想把一个应用从生产状态切换开发状态，强烈建议你新建一个开发状态下的应用。
					</div>
					<form method="post" enctype="multipart/form-data" class="blueform"
						id="myForm" onsubmit="return vForm()">
						<div style="display: none">
							<input type="hidden" name="appId" id="appId"> <input
								type="hidden" name="appKey" id="appKey">
						</div>
						<ul class="form">
							<li><label for="id_name"> 应用名称 </label> <input id="appName"
								type="text" name="appName" value="" maxlength="255"> <span
								class="red">*必填</span>
							</li>
							<li><label for="id_name"> 应用包名 (Android) </label> <span
								id="id_appPk" style="display: none;"><input
									id="appPackage" type="text" name="appPackage" value=""
									maxlength="255"> 包名填写后不能修改 </span> <span id="id_appPackage"></span>
							</li>

							<li><label for="id_icon"> 应用图标 </label> <input type="file"
								name="appIcon" id="appIcon" nselectable="on"
								onchange='checkFileType(this,".png、.jpg、.gif");'> <span
								id="appIconTip" class="red"></span> <img
								src="/resources/images/default_app_icon_57x57.png" id="oldIcon"
								alt="push 推送  app icon" name="oldIcon" height="30" width="30"
								style="vertical-align: top;">
							</li>
							<li><label for="id_name"> APNs证书文件(iOS)</label> <input
								type="file" name="appleCertificate" id="appleCertificate"
								nselectable="on" onchange='checkFileType(this,".p12");'>
								<span id="appleCertificateTip" class="red"> </span> <span
								id="iOSCP"> </span>
							</li>
							<li><span class="blue1">APNs证书文件：这是 iOS SDK 能够接收到
									JPush 推送消息的必要文件。具体请参考： <a
									href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343727"
									target="_a">iOS 证书设置指南</a>
							</span></li>
							<li><label for="id_name"> APNs证书密码 </label> <input
								type="password" value="" id="certificatePass" autocomplete="off"
								name="certificatePass" class="inputxt" /> <span
								id="certificatePassTip" class="red"> </span>
							</li>

							<li class="push_form_item"><label for="id_debug_mode">
									部署状态 </label> <!-- <input type="checkbox" id="appStage" name="appStage"> -->
								<input type="radio" name="appStage" checked="checked" value="0">
								开发状态 <input type="radio" name="appStage" value="1"> 生产状态
							</li>
							<li><span class="blue1">开发状态:连接开发服务器。 生产状态:连接生产服务器。</span></li>
							<!--<li>
					                  <label for="id_push_enabled">
					                      开启推送
					                  </label>
					                  <input type="checkbox"   name="isPushMsg" id="isPushMsg"  >
					              </li>
					              -->
							<!--<li>
									<label for="id_push_enabled">
										 上面的推送未开启时，这个字段不可点击，为 disabled 
										允许广告推送
									</label>
									<input type="checkbox" name="isPushAd" id="isPushAd">

								</li>

								-->
							<li><input type="hidden" name="app_group" value="28212"
								id="id_app_group">
							</li>
							<li class="submit-button"><input type="submit" id="btn_sub"
								value="保存修改"> <span id="err" class="red"></span>
							</li>
							<li><small><span class="red">*</span>表示是必填项。 </small>
							</li>
						</ul>
					</form>
				</div>

				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
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
		function vForm() {
			$("#er").html("");
			var isSu = true;
			if (!blur_appName()) {
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

			if (isSu) {
				$('#btn_sub').attr("disabled", "disabled");
			} else {
				$('#btn_sub').removeAttr("disabled");
			}
			return isSu;
		}
		function checkFileType(_this, typeValue) {
			var tv = typeValue.split("、");
			var iiOK = true;
			var str = _this.value
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

		$(function() {
			var appId = appid;
			if (!(appId == null || appId == '')) {
				jQuery.post("${ctx}/app/getApp?appId=" + appId, function(data) {
					$("#appId").attr("value", data.info.appId);
					$("#appKey").attr("value", data.info.appKey);
					$("#appName").val(data.info.appName);
					if (data.info.appIcon != null) {
						$("#oldIcon").attr(
								"src",
								appPath + data.info.appId + "/"
										+ data.info.appIcon);
					}
					if (data.info.appStage == 1) {
						$("[name=appStage]").eq(1).attr("checked", true);
					}
					/*if(data.info.isPushMsg==1){
					   $("#isPushMsg").attr("checked",true);
					 }*/
					if (data.info.isPushAd == 1) {
						$("#isPushAd").attr("checked", true);
					}
					//$("#id_push_enabled").attr("checked",true);
					if (data.info.appPackage != null
							&& data.info.appPackage != "") {
						$("#id_appPk").hide();
						$("#id_appPackage").html(data.info.appPackage);
						$("#appPackage").val(data.info.appPackage);
					} else {
						$("#id_appPk").show();

						$("#id_appPackage").html("");
					}

					if (data.info.appStage == 0) {
						if (data.info.appleDataTest!=null) {
							$("#iOSCP").html("已经上传开发APNs证书");
						} else {
							$("#iOSCP").html("未上传开发APNs证书，应用处于开发状态，请上传开发APNs证书。");
						}
					} else {
						if (data.info.appleData!=null) {
							$("#iOSCP").html("已经上传正式APNs证书");
						} else {
							$("#iOSCP").html("未上传生产APNs证书，应用处于生产状态，请上传生产APNs证书。");
						}
					}

				}); //post
			}
			;
			$('#myForm').ajaxForm({
								url : '${ctx}/app/saveApp',
								dataType : 'json',
								success : function(data) {
									var r = data.success;
									if (r == true) {
										 if(data.ios!=1){
										     window.location = '${ctx}/app/detail.jsp?appId=' + data.appId;
										   } else{
											 $("#err").html(data.info);
										   }
									} else {
										$("#err").html(data.info);
									}//if 
									$('#btn_sub').removeAttr("disabled");
								}//function
							});//ajax 		   
		})
	</script>
</body>
</html>