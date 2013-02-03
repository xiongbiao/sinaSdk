<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<title>首页</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="skin/default/page-demo.css" type="text/css"></link>
<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/ddaccordion.js"></script>

<script type="text/javascript">
			ddaccordion.init({
				headerclass: "navhead", 
				contentclass: "navcontent", 
				revealtype: "click", 		
				mouseoverdelay: 400, 
				collapseprev: true, 	
				defaultexpanded: [], 	
				onemustopen: false, 	
				animatedefault: true, 		
				persiststate: true, 		
				toggleclass: ["", ""], 	
				togglehtml: ["none", "", ""], 
				animatespeed: "normal", 	
				oninit:function(expandedindices){ },
				onopenclose:function(header, index, state, isuseractivated){ }
			})
			window.onload=function(){ 
		    document.onmousewheel=function(){if(event.ctrlKey) return false;}  
		} 
		</script>
</head>

<body>
	<div class="container">
		<%-- <%@ include file="/top.jsp"%> --%>
		<div class="row">
			<div class="span12 page-middle">
				<%@ include file="/left.jsp"%>
				<div id="body" class="span9">
					<button class="btn btn-primary">&nbsp;&nbsp; 应用管理
						&nbsp;&nbsp;&nbsp;</button>
				</div>
			</div>
		</div>
		<%-- <%@ include file="/foot.jsp"%> --%>
	</div>
</body>
</html>
