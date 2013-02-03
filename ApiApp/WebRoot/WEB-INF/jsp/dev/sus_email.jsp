<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Language" content="en-us">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title>验证邮箱成功 - ${Title }</title>
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
                     window.location = '${ctx}/app/application_list.jsp';
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
							<span> 提示：${info }</span>
						</h1>
						<p>
							<font size=4><span id="timer">${seconds}</span> 秒后自动跳转到
								应用页面......</font>
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
