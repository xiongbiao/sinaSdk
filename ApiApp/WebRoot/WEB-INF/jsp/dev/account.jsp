<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>个人详情 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/account_sidebar.jsp"%>

				<div class="right_content">
					<h2>帐户</h2>
					<div class="bluebox">
						<dl>
							<dt>用户名</dt>
							<dd>${dev.devName}</dd>
							<dt>真实姓名</dt>
							<dd>${dev.contact}</dd>
							<dt>邮箱</dt>
							<dd>
								${dev.email} &nbsp;

								<c:if test="${dev.emailFlag == 0 || dev.emailFlag == 2}">
									<input onclick="validate_email(this)" type="button"
										value="认证邮箱" id="valEmail" name="valEmail" />
								</c:if>
								<c:if test="${dev.emailFlag==2 }">
									<font color="red"><span id="se_m">(认证信息已发到您的邮箱，
											请查收邮件！)</span>
									</font>
								</c:if>
								<c:if test="${dev.emailFlag == 1 }">
									<font color='gran'><span id="se_m">邮箱已认证</span>
									</font>
								</c:if>
								<c:if test="${dev.emailFlag==0 }">
									<font color='red'><span id="se_m"></span>
									</font>
								</c:if>
							</dd>
							<dt>公司名称</dt>
							<dd>${dev.companyName}</dd>
							<dt>联系QQ号</dt>
							<dd>${dev.qq}</dd>
							<dt>联系电话</dt>
							<dd>${dev.mobilePhone}</dd>
							<dt>应用数量(生产)</dt>
							<dd>
								<span id="generate_app_count"></span>
							</dd>
							<dt>应用数量(开发)</dt>
							<dd>
								<span id="test_app_count"></span>
							</dd>
							<dt>创建时间</dt>
							<dd>${dev.regTime}</dd>

						</dl>
					</div>

				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript">
		 $(function(){
				jQuery.post("${ctx}/developer/userApp", function(data) {
					$("#generate_app_count").html(data.user_app.generate_app_count);  		
					$("#test_app_count").html(data.user_app.test_app_count);  	
				});
		 });
		 
		function validate_email(se){
			se.disabled=true;
		 var i = 3;
			$.ajax({
				type:'GET',
				  url: '${ctx}/developer/sendValidatEmail',
				  success: function(data) {
					  if(data.success == 'true'){
					   	 	$('#se_m').html("("+data.info+")");
					  }else if(data.success == 'false' && data.loginout =='true'){
						 	 $('#se_m').html("("+data.info+",3秒后跳到登陆页面！)");
						 	setTimeout(red,3000);
					  }else{
							 $('#se_m').html("("+data.info+")");
					  }
				  },
				  error: function(XMLHttpRequest, textStatus, errorThrown) {
						 $('#se_m').html("服务器忙,请稍后再试..........");
                  }
				});
		}
		
		function red(){
			location.href='/login.jsp';
		}
		

</script>
</body>
</html>