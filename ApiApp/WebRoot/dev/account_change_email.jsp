<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>修改邮箱 - ${Title}</title>
<link rel="stylesheet"
	href="${ctx}/plugin/formValidator/style/validator.css" type="text/css"></link>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/account_sidebar.jsp"%>
				<div class="right_content">
					<h2>修改邮箱</h2>
					<form method="post" class="blueform" id="myForm" autocomplete="off">
						<div style='display: none'>
							<input type='hidden' value='' />
						</div>
						<ul class="form">
							<li><label for="id_email"> 新邮箱： </label> <input type="text"
								name="email" id="email" /> <span style="color: red;">*</span>
								<span id="emailTip"> </span></li>
							<li><input type="submit" id="button" name="button"
								value="提交修改" /> <span id="scc"></span> <span id="er"
								style="color: red;"></span></li>
						</ul>
					</form>
				</div>
				<div class="clear"></div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		 function vForm(){
		      var re=true;
		       if(!onblur_email()){
		         re=false;
		       } 
		      return re;
		    }
		  function   onblur_email(){
		     var val=$('#email').val();
		     var reg = new RegExp("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$");
		       if(val.length<=0){
		         $('#id_emailTip').html("邮箱不能为空");
		          return false;
		       }else if(!reg.test(val)){
		         $('#id_emailTip').html("邮箱格式不对");
		         return false;
		       }else{
		         $('#id_emailTip').html("");
		         return true;
		       }
		  
		  }
		    
		 $(function(){
		  $.formValidator.initConfig({formid:"myForm",onerror:function(msg){alert(msg);return false;},onsuccess:function(){ 
 				return true; }});
	 		$("#email").formValidator({onshow:"&nbsp;",onfocus:"邮箱 如 service@jpush.cn",oncorrect:"该邮箱可用",defaultvalue:""})
	 		.inputValidator({min:1,onerror:"邮箱不能为空"})
	 		.regexValidator({regexp:"email",datatype:"enum",onerror:"邮箱格式不正确"})
	 		.ajaxValidator({
			    type : "post",
				url : "${ctx}/developer/isExistsEmail?isUpdate=1",
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
				onerror : "该邮箱已被占用，请更换邮箱",
				onwait : "邮箱检查中，请稍候..."
			});	
 				
			  $('#myForm').ajaxForm({ 
				            url:'${ctx}/developer/UpdateEmail',
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