<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>修改密码 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/account_sidebar.jsp"%>


				<div class="right_content">
					<h2>修改密码</h2>
					<form method="post" id="myForm" class="blueform" autocomplete="off"
						onsubmit="return vForm()">
						<div style='display: none'>
							<input type='hidden' name='csrfmiddlewaretoken' value=' ' />
						</div>
						<ul class="form">
							<li><label for="id_email"> 旧密码： </label> <input
								type="password" name="oldPass" onblur="onblur_oPass()"
								id="id_oldPass" /> <span style="color: red;">*</span> <span
								id="id_oldPassTip" style="color: red;"> </span></li>
							<li><label for="id_email"> 新密码： </label> <input
								type="password" name="newPass" onblur="onblur_nPass()"
								id="id_newPass" /> <span style="color: red;">*</span> <span
								id="id_newPassTip" style="color: red;"> </span></li>
							<li><label for="id_email"> 再次确认： </label> <input
								type="password" name="newPass_again" onblur="onblur_naPass()"
								id="id_newPass_again" /> <span style="color: red;">*</span> <span
								id="id_newPass_againTip" style="color: red;"> </span></li>
							<li><input type="submit" value="提交修改" /> <span id="scc"></span>
								<span id="er" style="color: red;"></span></li>
						</ul>
					</form>
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		    function vForm(){
		      var re=true;
		       if(!onblur_nPass()){
		         re=false;
		       }else if(!onblur_oPass()){
		         re=false;
		       }else if(!onblur_naPass()){
		         re=false;
		       }
		      return re;
		    }
		    function onblur_nPass(){
		       var val=$('#id_newPass').val();
		       if(val.length<5){
		           $('#id_newPassTip').html("密码长度最少5位");
		            return false;
		       }else{
		         $('#id_newPassTip').html("")
		         return true;
		       }
		    }
		    
		    function onblur_oPass(){
		       var val=$('#id_oldPass').val();
		       if(val.length<=0){
		         $('#id_oldPassTip').html("旧密码不能为空")
		          return false;
		       }else{
		         $('#id_oldPassTip').html("")
		         return true;
		       }
		    }
		     function onblur_naPass(){
		       var val=$('#id_newPass').val();
		       var val_a=$('#id_newPass_again').val();
		       if(val_a.length<5){
		         $('#id_newPass_againTip').html("密码长度最少5位");
		          return false;
		       }else if(val!=val_a) {
		         $('#id_newPass_againTip').html("两次输入密码不一致");
		           return false;
		       } else{
		         $('#id_newPass_againTip').html("")
		         return true;
		       }
		    }
		    
		
			 $(function(){
				$('#myForm').ajaxForm({ 
				            url:'${ctx}/developer/UpdatePass',
						    success: function(data){
								//动态改变节点的内容
								var r=data.success;
								if(r=="true"){
									$("#scc").html(data.info);
									$("#er").html("");  
								}else{
									$("#er").html(data.info); 
									$("#scc").html("");
								}
							}  
				 }); 
			 })
      </script>
</body>
</html>