<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%@ include file="/commons/permission.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="zh">
<head>
<title>意见反馈 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<link rel="stylesheet" href="${ctx}/plugin/kk/css/el-grid.css"
	type="text/css"></link>
<link rel="stylesheet" href="${ctx}/skin/default/uapush.css" />
</head>

<body>
	<%-- 	<%@ include file="/top.jsp"%>
	<jsp:include page="/nav" flush="true" />
 --%>
	<div id="main">
		<div id="changePassword" class="main-title">意见反馈</div>
		<div id="content" class="main-content">
			<form method="post" action="${ctx}/about/save" id="myForm">
				<ul id="frmInformation" class="form">
					<li><label>意见内容：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label> <textarea
							id="feed"
							style="reSize: none; margin-top: 12px; width: 600px; height: 100px;"
							rows="5" cols="510" name="feed"></textarea>
					</li>
					<li><span id="scc" style="color: green; font-size: 13px;"></span>
					</li>
					<li><span id="feedTip"></span>
					</li>
				</ul>
				<button id="btnSubmit">保&nbsp;&nbsp;存</button>
			</form>
			<div id="processBar"></div>
		</div>

		<div class="main-bottom"></div>

	</div>
	<%-- <%@ include file="/foot.jsp"%> --%>
	<script type="text/javascript" src="${ctx}/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/uapush.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<!--
	<script type="text/javascript" src="${ctx}/js/vanadium.js"></script>
	<script type="text/javascript" src="${ctx}/js/verform.js"></script>
	 -->
	<!-- 验证 -->
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>

	<script type="text/javascript" src="${ctx}/plugin/kk/el-grid.js"></script>

	<script type="text/javascript">
	 
		$(function() {
		    //  $('#msg').show();//展示提示 
		    //$('#msginfo').html("数据保存中 请稍后…………");  //提示消息
		    
		    var pb = $('#processBar').ELProcessBar({
				title:'数据保存中 请稍后...',
				showText:true
			});
		    $.formValidator.initConfig({formid:"myForm",onerror:function(msg){ 
		     //alert(msg)
		     return false; 
		     },onsuccess:function(){
			      // $('#msg').show();//展示提示 
		          // $('#msginfo').html("数据保存中 请稍后…………");  //提示消息
		           pb.start();
			       return true;
		     }}); 
	        $("#feed").formValidator({onshow:"请填写你的意见",onfocus:"意见至少要输入5个汉字或10个字符,最多1000字符",oncorrect:"&nbsp;",defaultvalue:""}).inputValidator({min:5,max:1000,onerror:"意见长度不够！"});
			$('#myForm').ajaxForm({
				success : function(data) {
				   // $('#msg').hide();
					pb.stop();
					//动态改变节点的内容
					var r = data.success;
					if (r == "true") {
						$("#scc").html(data.info);
						$('#myForm').resetForm();
					} else {
						$("#pass_er").html(data.info);
					}
				}
			});
		});
	</script>
</body>
</html>