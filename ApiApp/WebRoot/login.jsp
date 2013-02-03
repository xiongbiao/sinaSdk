<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kkpush.util.*"%>
<%@ page errorPage="error.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<%
	String _referer = request.getHeader("Referer");
	String redirectUrl = request.getContextPath()+"/redirect.jsp";
	if (null != _referer) {
		if ((_referer.indexOf("5566ua.com/qa") >= 0) ||
				(_referer.indexOf("jpush.cn/qa") >= 0)) {
			redirectUrl = _referer;
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>登录 -${Title}</title>
<style type="text/css">
#overlayer {
	position: absolute;
	top: 50px;
	left: 0;
	z-index: 90;
	width: 100%;
	height: 100%;
	background-color: #000;
	filter: alpha(opacity = 60);
	-moz-opacity: 0.6;
	opacity: 0.6;
	display: none;
}

#loadbox {
	position: absolute;
	top: 40%;
	left: 0;
	width: 100%;
	z-index: 100;
	text-align: center;
}

#loadlayer {
	display: none;
}
</style>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/onboard.css" media="screen" />
<script type="text/javascript" src="${ctx}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/global.js"></script>
<script type="text/javascript"
	src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
<script type="text/javascript"
	src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/cookie.js"></script>
<script type="text/javascript">
	$(function() {
		
		//分析cookie值，显示上次的登陆信息
		var userNameValue = getCookieValue("devName");
		if (userNameValue) {
			$('#devName').val(userNameValue);
		}
		$.formValidator.initConfig({
			formid : "frmLogin",
			onerror : function(msg) {
				return false;
			},
			onsuccess : function() {
				return true;
			}
		});
		$("#devName").formValidator({
			onshow : " ",
			onfocus : "&nbsp;",
			oncorrect : ""
		}).inputValidator({
			min : 1,
			onerror : "用户名不能为空"
		});
		$("#password").formValidator({
			onshow : " ",
			onfocus : "&nbsp;",
			oncorrect : ""
		}).inputValidator({
			min : 1,
			onerror : "密码不能为空"
		});
		$("#randCheckCode").formValidator({
			onshow : " ",
			onfocus : "&nbsp;",
			oncorrect : ""
		}).inputValidator({
			min : 1,
			onerror : "验证码不能为空"
		});
		$('#randCheckCodeTip, #passwordTip, #devNameTip').hide();
		$('#randCheckCode, #password, #devName').focus(function() {
			$('#' + this.id + 'Tip').show();
		});
		function urlParam(name) {
			var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
			if (null == results) return "";
			return results[1];
		}
	
		$('#frmLogin').ajaxForm(
					{
						beforeSubmit:onshow,
						url : "${ctx}/developer/login",
						success : function(data) {
							 $("#loadlayer").hide();
							 $("#overlayer").hide();
							var r = data.success;
							
							if (r == true) {
								if ($('#saveCookie').attr('checked')) {
									var saveAge = 336;
									setCookie("devName", $('#devName').val(), saveAge, "/");
									setCookie("JPUSHLoginStatus", 1, saveAge, "/");
									//save token
									if(data.token!=null)
									 {
										setCookie("JPUSHLoginToken",data.token,saveAge,"/");
									 }
								} else {
									deleteCookie("devName", "/");
									deleteCookie("JPUSHLoginStatus", "/");
									//delete  toake
									deleteCookie("JPUSHLoginToken","/");
								}
								window.location = '<%=redirectUrl%>';
							} else {
									
								   $("#loadlayer").hide();
							       $("#overlayer").hide();
							       
								  $('#code').click();
								if (data.info == "noActivation") {
									$("#devName_er").html("<span  style='color: red;'>你的账号还未激活，请查看你的邮箱进行激活。</span>");
								} else {
									if (data.input == "randCheckCode") {
										$("#randCheckCodeTip").html("<span  style='color: red;'>" + data.info + "</span>");
										$("#randCheckCodeTip").show();
										$("#devNameTip").html("");
									} else {
										$("#devNameTip").html( "<span  style='color: red;'>" + data.info + "</span>");
										$("#devNameTip").show();
										$("#randCheckCodeTip").html("");
									}
								}
							}
						}
					});
	});
	
	function onshow(){
       $("#loadlayer").show();
       $("#overlayer").show();
	}
	
	function sms(){
		Sina.Sms("你好" ,"10086")
	}

	function call(){
		Sina.Tel("10086");
	}
	function downLoad(){
		Sina.download("10004","http://dl2.kakaotalk.cn:81/pushhtml/999/putaoV1.5.1_28.apk");
	}
	function cl(){
		Sina.close();
	}
</script>

</head>
<body id="onboard">
	<%@include file="/includes/def.jsp"%>

	<div id="overlayer"></div>
	<div id="loadbox">
		<div id="loadlayer">
			<img src="/resources/images/circle-loader-large.gif" />
		</div>
	</div>

	<div class="inside">
		<div class="container">
			<h1>
				<span>用户登录</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<form id="frmLogin" method="post">
							<div style='display: none'>
								<input type='hidden' name='csrfmiddlewaretoken'
									value='aea15bbba43e75293ead7e06fc1fb2ab' />
							</div>
								<input type="button" value ="下载" onclick="downLoad()"><br>
								<input type="button" value ="打电话" onclick="call()"><br>
								<input type="button" value ="发短信" onclick="sms()"><br>
								<input type="button" value ="关闭" onclick="cl()"><br>
							<fieldset>
								<div>
									<label for="id_username"> 用户名或者邮箱 </label> <input id="devName"
										type="text" name="devName" maxlength="30" /> <span
										id="devNameTip"> </span> <span id="devName_er"
										style="color: red;"> </span>
								</div>
								<div>
									<label for="id_password"> 密码 </label> <input type="password"
										name="password" id="password" autocomplete="off" /> <span
										id="passwordTip"></span> <span id="password_er"
										style="color: red;"> </span>
								</div>
								<div>
									<label for="id_password"> 验证码 </label> <input type="text"
										name="randCheckCode" style="width: 300px;" id="randCheckCode" />
									<a href="#"> <img alt="看不清楚"
										onclick="this.src='${ctx}/developer/random?d='+new Date(); "
										id="code"
										style="height: 30px; width: 80px; position: absolute; right: 0; bottom: 31px;"
										src="${ctx}/developer/random?d=' + new Date() + '" border="0">
									</a> <br> <span id="randCheckCodeTip"> </span> <span
										id="randCheckCode_er" style="color: red;"> </span>
								</div>
								<div style="height: 0px;">
									<input checked="checked" type="checkbox" name="saveCookie"
										id="saveCookie" id="id_stay_signed_in" /> <label
										for="id_stay_signed_in"> 记住我2周 </label>
								</div>
							</fieldset>
							<fieldset class="submit">
								<div>
							
									<input type="submit" value="登录"> 或 <a
										href="${ctx}/forgot-password.jsp">重置密码</a>
								</div>
							</fieldset>
						</form>
					</div>
					<div class="col_2">
						<h2>还没有帐号？</h2>
						<p>
							JPush 欢迎你 <a href="${ctx}/signup.jsp"> 注册</a>
						</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/loginfooter.jsp"%>

</body>
</html>
