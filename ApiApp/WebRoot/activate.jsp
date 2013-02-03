<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>账号激活成功 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/onboard.css" media="screen" />
<script type="text/javascript">
            var second = ${seconds};
            function timer()
            {
                second = second - 1;
                if (second == 0)
                {
                    window.clearInterval("timer()");
                     window.location = '${ctx}/login.jsp';
                }
                document.getElementById("timer").innerHTML = second;
            }
            window.setInterval("timer()", 1000);
    </script>
</head>
<body id="onboard">
	<%@include file="/includes/def.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<div class="login">
					<div class="col_1">
						<h1>
							<span> 提示：您账户已经激活成功！</span>
						</h1>
						<p>
							<span id="timer">${seconds}</span> 秒后自动跳转登录界面......
						</p>
						<p>
							如没有跳转，可以点击这里 <a href="<c:url value='${ctx}/login.jsp' />"><font
								color=blue>进入登录</font> </a>
						</p>
					</div>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/loginfooter.jsp"%>
	<script type="text/javascript" src="${ctx}/resources/js/global.js"></script>
</body>
</html>
