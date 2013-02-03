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
<title>注册 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css" href="resources/css/onboard.css"
	media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/def.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>注册</span>
			</h1>
			<div id="content">
				<div class="signup">
					<div class="col_1">
						<form method="post" id="myForm">
							<%
									if ((null != thirdSiteSource) && (!"".equals(thirdSiteSource))) {
										out.print("<input type=\"hidden\" name=\"referer\" value=\""+thirdSiteSource+"\"/>");
									}
								%>
							<div style='display: none'>
								<input type='hidden' name='csrfmiddlewaretoken' value='' />
							</div>
							<h2>基本信息</h2>
							<fieldset>
								<div>
									<label for="id_email"> 真实姓名<span style="color: red;">*</span>
									</label> <input autocomplete="off" name="contact" maxlength="75"
										type="text" class="required" id="contact" /> <span
										id="contactTip"></span>
								</div>
								<div>
									<label for="id_email"> 联系邮箱 <span style="color: red;">*</span>
									</label> <input autocomplete="off" name="email" maxlength="75"
										type="text" class="required" id="email" /> <span
										id="emailTip" style="font-size: 12px;"></span>
								</div>
								<div>
									<label for="id_email"> 联系方式 <span style="color: red;">*</span>
									</label> <input autocomplete="off" name="mobilePhone" maxlength="75"
										type="text" class="required" id="mobilePhone" /> <span
										id="mobilePhoneTip" class="fieldError"></span>
								</div>

								<div>
									<label for="id_email"> 联系QQ号 </label> <input autocomplete="off"
										name="QQ" maxlength="75" type="text" class="required" id="QQ" />
									<span id="QQTip" class="fieldError"></span>
								</div>

								<div>
									<label for="id_company_name"> 公司名称 </label> <input
										id="company_name" type="text" class="required"
										name="company_name" maxlength="255" />
								</div>
							</fieldset>
							<h2>帐号信息</h2>
							<fieldset>
								<div>
									<label for="id_username"> 用户名 <span style="color: red;">*</span>
									</label> <input autocomplete="off" name="devName" maxlength="30"
										type="text" class="required" id="devName" /> <span
										id="devNameTip"> </span> <span id="devName_er"
										style="color: red;"> </span>
								</div>

								<div class="width_med">
									<label for="id_password1"> 密码<span style="color: red;">*</span>
									</label> <input type="password" name="password" id="password" /> <span
										id="passwordTip"></span>
								</div>
								<div class="width_med">
									<label for="id_password2"> 确认密码<span
										style="color: red;">*</span> </label> <input type="password"
										name="password_again" id="password_again" /> <span
										id="password_againTip"></span>
								</div>
							</fieldset>
							<fieldset class="submit">
								<div>
									<input type="submit" name="submit" value="我同意，现在注册">
									注册需要同意 <a
										href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343745"
										target="_blank">JPush开发者协议</a>。
								</div>
							</fieldset>
						</form>
					</div>
					<div class="col_2">
						<h2>已有帐号？</h2>
						<p>
							<a href="${ctx}/login.jsp">点击登录</a> JPush
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/regfooter.jsp"%>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
			$(function(){
            $.formValidator.initConfig({formid:"myForm",onerror:function(msg){alert(msg);return false;},onsuccess:function(){ 
 				return true; }});
		    $("#devName").formValidator({onshow:"&nbsp;",onfocus:"用户名至少5个字符,最多20个字符",oncorrect:"该用户名可用"}).inputValidator({min:5,max:20,onerror:"至少5个字符,最多20个字符"}).regexValidator({regexp:"username",datatype:"enum",onerror:"用户名格式不正确 只能是英文和数字的组合 不能使用特殊字符"}).ajaxValidator({
		    type : "post",
			url : "developer/isExistsName",
			datatype : "json",
			success : function(data){
	            if( data.success == "true" )
				{
	                return true;
				}
	            else
				{
	                return false;
				}
		},
		buttons: $("#button"),
		error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
		onerror : "<span style=\"color:red\">该用户名被注册，请更换用户名</span>",
		onwait : "用户名检查中，请稍候..."
	});
	$("#email").formValidator({onshow:"&nbsp;",onfocus:"邮箱 如 jpush@jpush.cn",oncorrect:"该邮箱可用",defaultvalue:""}).inputValidator({min:1,onerror:"邮箱不能为空"}).regexValidator({regexp:"email",datatype:"enum",onerror:"邮箱格式不正确"}).ajaxValidator({
	    type : "post",
		url : "developer/isExistsEmail",
		datatype : "json",
		success : function(data){
            if( data.success == "true" )
			{  
                return true;
			}
            else
			{
                return false;
			}
		},
		buttons: $("#button"),
		error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
		onerror : "<span style=\"color:red\">该邮箱已被注册，请更换邮箱</span>",
		onwait : "邮箱检查中，请稍候..."
	});
	$("#contact").formValidator({onshow:"&nbsp;",onfocus:"真实姓名最少2个字符",oncorrect:"&nbsp;"})
	.inputValidator({min:2,onerror:"真实姓名最少2个字符"});
	
    $("#password").formValidator({onshow:"&nbsp;",onfocus:"密码最少5个字符",oncorrect:"&nbsp;"})
    .inputValidator({min:5,max:30,onerror:"密码最少5个字符"})
    .regexValidator({regexp:"notempty",datatype:"enum",onerror:"密码不能为空"});
    
    $("#password_again").formValidator({onshow:"&nbsp;",onfocus:"两次密码必须一致",oncorrect:"&nbsp;"})
    .inputValidator({min:5,onerror:"确认密码最少5个字符"})
    .compareValidator({desid:"password",operateor:"=",onerror:"两次密码不一致,请确认"});
    
//	$("#QQ").formValidator({onshow:"请输入QQ",onfocus:"例如-123456",oncorrect:"&nbsp;"}).inputValidator({min:0,onerror:"QQ不能为空,请确认"}).regexValidator({regexp:"qq",datatype:"enum",onerror:"你输入的QQ格式不正确"});
	 $("#mobilePhone").formValidator({onshow:"&nbsp;",onfocus:"格式例如：0755-88888888或11位手机号码",oncorrect:"输入正确"})
	 .inputValidator({min:1,onerror:"电话不能为空,请确认"})
	 .regexValidator({regexp:"((^((([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$))|(^(13|15|18)[0-9]{9}$))",onerror:"你输入的电话格式不正确"});
	
	
            $('#myForm').ajaxForm({ 
               url:'developer/save',
               success: function(data){   
   			     //动态改变节点的内容
   			     var r=data.success; 
   			     if(r=="true"){
   			      // window.location = 'success.jsp';
   			    	window.location = '${ctx}/redirect.jsp';
   			     }else{
	   			     if(data.info=="邮件已经被注册"){
	   			       $("#emailTip").html('<span style="color:red">'+data.info+'</span>');   
	   			       $("#userTip").html("");  
	   			     }else{
	   			        $("#userTip").html(data.info);  
	   			        $("#emailTip").html("");
	   			    }
   			     }
               }  
            }); 
			});		
		</script>
</body>
</html>

