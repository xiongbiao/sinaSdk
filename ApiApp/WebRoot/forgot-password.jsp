<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>密码重置 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/onboard.css" media="screen" />
<style>
img:-moz-broken {
	-moz-force-broken-image-icon: 1 !important;
}

input[type="submit"] {
	widows: 100px;
}

table {
	width: 100%;
	border: 0;
	border-collapse: collapse;
	line-height: 30px;
	margin: 10px 0;
}

table th {
	text-align: right;
	width: 20%;
	font-weight: normal;
}

table td span {
	color: red;
}

input.rightformcss,select.rightformcss,textarea.rightformcss {
	border: 1px solid green;
}

.failmsg {
	color: red;
}

.msgvaluecss {
	font-style: italic;
}

input.failformcss,select.failformcss,textarea.failformcss {
	border: 1px solid red;
}

#emailLink {
	font-size: 14px;
	padding: 32px 0 0 8px;
	color: #008FFF;
}
</style>
</head>
<body id="onboard">
	<%@include file="/includes/def.jsp"%>
	<div class="inside">
		<div class="container">
			<h1>
				<span>密码重置</span>
			</h1>
			<div id="content">
				<div class="login">
					<div class="col_1">
						<form id="fpass" method="post">
							<div style='display: none'>
								<input type='hidden' name='csrfmiddlewaretoken'
									value='aea15bbba43e75293ead7e06fc1fb2ab' />
							</div>
							<div id="emailLink"></div>
							<fieldset>
								<div id="emailActions">
									<label for="id_username"> 请输入注册的邮箱 </label> <input id="email"
										name="email" type="text" /> <span id="emailTip">&nbsp;</span>
								</div>
							</fieldset>
							<fieldset class="submit">
								<div>
									<input type="submit" id="btn_sub" value="重置密码"> 或 <a
										href="login.jsp">登录</a>
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
	var hash={
		    'qq.com': 'http://mail.qq.com',
		    'gmail.com': 'http://mail.google.com',
		    'sina.com': 'http://mail.sina.com.cn',
		    '163.com': 'http://mail.163.com',
		    '126.com': 'http://mail.126.com',
		    'yeah.net': 'http://www.yeah.net/',
		    'sohu.com': 'http://mail.sohu.com/',
		    'tom.com': 'http://mail.tom.com/',
		    'sogou.com': 'http://mail.sogou.com/',
		    '139.com': 'http://mail.10086.cn/',
		    'hotmail.com': 'http://www.hotmail.com',
		    'live.com': 'http://login.live.com/',
		    'live.cn': 'http://login.live.cn/',
		    'live.com.cn': 'http://login.live.com.cn',
		    '189.com': 'http://webmail16.189.cn/webmail/',
		    'yahoo.com.cn': 'http://mail.cn.yahoo.com/',
		    'yahoo.cn': 'http://mail.cn.yahoo.com/',
		    'eyou.com': 'http://www.eyou.com/',
		    '21cn.com': 'http://mail.21cn.com/',
		    'foxmail.coom': 'http://www.foxmail.com'
		};
			$(function(){
		            $.formValidator.initConfig({formid:"fpass",onerror:function(msg){return false;},onsuccess:function(){ return true;   }});
		            $("#email").formValidator({onshow:"&nbsp;",onfocus:"邮箱格式 如 jpush@jpush.cn",oncorrect:"&nbsp;",defaultvalue:""}).inputValidator({min:1,onerror:"邮箱不能为空"}).regexValidator({regexp:"email",datatype:"enum",onerror:"邮箱格式不正确"});
				
			    $(".mail").each(function() {
			        var url = $(this).text().split('@')[1];
			        for (var j in hash){
			            $(this).attr("href", hash[url]);
			        }
			    });
			    var sendEmail = false;
				$('#fpass').ajaxForm({
					url:"${ctx}/developer/fpass",
					beforeSend: function() {
						if (sendEmail) {
							return false;
						}
						sendEmail = true;
						$("#emailTip").html('发送邮件中...请稍等！');
						$("#btn_sub").attr("disabled", "disabled");
					},
		            success: function(data){
		            	var r=data.success;
					    if(r=="true"){
					      $("#emailActions").hide();
					      $("fieldset.submit").hide();
					      $("#emailLink").html(data.info+'。<div style="padding-top:3px;">如果您没有收到邮件<a href="javascript:location.reload()">点击重新发送邮件</a>，或者<a href="login.jsp">重新登陆</a><div>'); 
					    }else{
			        		$("#btn_sub").removeAttr("disabled");
			        		sendEmail = false;
					      $("#emailTip").html("<span  style='color: red;'>"+data.info+"</span>"); 
						}
		        	}
				});
			});		
	</script>
</body>
</html>
