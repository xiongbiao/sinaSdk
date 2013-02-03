<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改基本信息 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/account_sidebar.jsp"%>


				<div class="right_content">
					<h2>修改基本信息</h2>
					<form method="post" id="myForm" class="blueform" autocomplete="off"
						onsubmit="return vForm()">
						<div style='display: none'>
							<input type='hidden' name='csrfmiddlewaretoken' value=' ' />
						</div>
						<ul class="form">
							<li><label for="id_email"> 真实姓名： </label> <input type="text"
								name="contant" value="${user.contact}" onblur="onblur_contant()"
								id="id_contant" /> <span style="color: red;">*</span> <span
								id="id_contantTip" style="color: red;"> </span></li>
							<li><label for="id_email"> 联系电话： </label> <input type="text"
								name="mobilePhone" value="${user.mobilePhone }"
								id="id_mobilePhone" /> <span style="color: red;">*</span> <span
								id="id_mobilePhoneTip" style="color: red;"> </span></li>


							<li><label for="id_email"> 联系QQ号： </label> <input
								type="text" name="qq" value="${user.qq }" id="id_qq" /> <span
								id="id_qqTip" style="color: red;"> </span></li>
							<li><label for="id_email"> 公司名称： </label> <input type="text"
								name="companyName" value="${user.companyName }"
								id="id_companyName" /> <span id="id_companyNameTip"
								style="color: red;"> </span></li>


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
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidator_min.js"></script>
	<script type="text/javascript"
		src="${ctx}/plugin/formValidator/formValidatorRegex.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		    function vForm(){
		      var re=true;
		        if(!onblur_contant()){
		         re=false;
		       }else if(!onblur_mobilePhone()){
		    	   re = false;
		       }
		      return re;
		    }
		    //mobile:"^(13|15|18)[0-9]{9}$",	
		    function onblur_qq(){
		       var val=$('#id_qq').val();
		       val = $.trim(val);
		       if(val.length<=0){
		           $('#id_qqTip').html("联系QQ不能为空");
		          
		            return false;
		       }
		       else if(isNaN(val)){
		    	   $('#id_qqTip').html("QQ不能是字符，请从新填写");
		    	     return false;
		       }
		       else{
		         $('#id_qqTip').html("");
		         return true;
		       }
		    }
		    
		    function onblur_contant(){
		       var val=$('#id_contant').val();
		       val = $.trim(val);
		       if(val.length<=0){
		         $('#id_contantTip').html("真实姓名不能为空");
		          return false;
		       }
		       else{
		         $('#id_contantTip').html("");
		         return true;
		       }
		    }
		    
		    function  onblur_companyName(){
		       var val=$('#id_companyName').val();
		       val = $.trim(val);
		       if(val.length<=0){
		         $('#id_companyNameTip').html("公司名称不能为空");
		          return false;
		       }
		       else{
		         $('#id_companyNameTip').html("");
		         return true;
		       }
		    }
		    
		   
		    function onblur_mobilePhone(){
			       var val=$('#id_mobilePhone').val();
			       
			       var mobile = /^(13|15|18)[0-9]{9}$/;
			       var tel = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/;
			      

			       if(val.length<=0){
			         $('#id_mobilePhoneTip').html("联系方式不能为空");
			         $("#id_mobilePhoneTip").focus();
			          return false;
			       }else if(!mobile.test(val) && !tel.test(val)  ){
			    	   $('#id_mobilePhoneTip').html("联系方式格式错误!(0755-88888888或11位手机号码)");
				         $("#id_mobilePhoneTip").focus();
				          return false;
			       }else{
			         $('#id_mobilePhoneTip').html("");
			         return true;
			       }
			    }
			    
		   
		  
		
			 $(function(){
				$('#myForm').ajaxForm({ 
				            url:'${ctx}/developer/updateBase',
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