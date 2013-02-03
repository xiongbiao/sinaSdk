<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>


<html>
<head>
<title>账户未激活 -${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
<script languge="javascript"> 
           var second = ${seconds};
            function timer()
            {
                second = second - 1;

                if (second == 0)
                {
                    window.clearInterval("timer()");
                     window.location = 'index.jsp';
                }
                document.getElementById("timer").innerHTML = second;
            }

            window.setInterval("timer()", 1000);
        </script>

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>

<script type="text/javascript">
    function showProcess(isShow, title, msg) {  
	    if (!isShow) {  
	        $.messager.progress('close');  
	      return;  
	   }  
	     var win = $.messager.progress({title: title,msg: msg });  
	 }
    </script>
</head>
<body>

	<div class="container">
		<%@include file="/includes/def.jsp"%>
		<div class="row">
			<div class="span12 page-middle">
				<div class="span4">&nbsp;</div>
				<div class="span8">
					<div style="margin-top: 100px;">
						<div>
							<div class="noamsg"></div>
							<div style="color: #BD1222; float: left;">
								<h1>&nbsp;&nbsp; 用户未激活 ！</h1>
							</div>
							<br> <br> <br> <br>
							<p>
								<br> &nbsp;&nbsp; &nbsp;&nbsp; 本页将在 <span id="timer">${seconds}</span>
								秒后跳转，如没有跳转，请<a href="<c:url value='${ctx}/login.jsp' />"><font
									color=blue>点击此处</font> </a>
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>

		<%@include file="/includes/loginfooter.jsp"%>
	</div>
</body>
</html>
