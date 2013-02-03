<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%
	//session.invalidate();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>设置新密码 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/onboard.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/def.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>设置新密码</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<form id="upass" method="post">
							<div style='display: none'>
								<input type="hidden" value="<%=request.getParameter("em") %>"
									size="40" name="email" id=email> <input type="hidden"
									value="<%=request.getParameter("token") %>" size="40"
									name="token" id=token>
							</div>
							<fieldset>
								<div>
									<label for="id_username"> 新密码 </label> <input type="password"
										value="" size="40" name="newPass" id=newPass
										autocomplete="off"> <span id="newPassTip"></span>
								</div>
								<div>
									<label for="id_username"> 再次确认密码 </label> <input
										type="password" value="" size="40" name="password_again"
										id="password_again" autocomplete="off"> <span
										id="password_againTip"></span>
								</div>
							</fieldset>
							<fieldset class="submit">
								<div>
									<input type="submit" value="设置密码"> <span id="scc"></span>
								</div>
							</fieldset>
						</form>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/regfooter.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/global.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
			$(function(){
					   //分析cookie值，显示上次的登陆信息
						$.formValidator.initConfig({
							formid:"upass",
							onerror:function(msg){
								return false;
							},
							onsuccess:function(){
							    $("#scc").html("");
								return true;
							}
						});
					    $("#newPass").formValidator({  onshow:"&nbsp;",onfocus:"密码最少5个字符",oncorrect:"&nbsp;" }).inputValidator({min:5,onerror:"密码最少5个字符"}).regexValidator({regexp:"notempty",datatype:"enum",onerror:"密码不能为空"});
			            $("#password_again").formValidator({onshow:"&nbsp;",onfocus:"两次密码必须一致",oncorrect:"&nbsp;"}).inputValidator({min:5,onerror:"确认密码最少5个字符"}).compareValidator({desid:"newPass",operateor:"=",onerror:"两次密码不一致"});
						$('#upass').ajaxForm({
							url:"${ctx}/developer/upass_r",
				            success: function(data){
				            	var r=data.success;
							    if(r=="true"){
							        window.location = 'redirect.jsp';
							    }else{
				 			        $("#scc").html("<span style='color: red;'>"+data.info+"<span>");
								}
				        	}
						});
					});		
			</script>
</body>
</html>
