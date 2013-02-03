<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>域名认证 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/account_sidebar.jsp"%>
				<div class="right_content">
					<h2>域名认证</h2>
					<form method="post" id="myForm" class="blueform" autocomplete="off"
						onsubmit="return vForm()">
						<div style='display: none'>
							<input type='hidden' name='apiId' id="apiId" value='${api.id }' />
						</div>
						<ul class="form">
							<li><label for="id_email"> 域名根地址：</label> 
							<input type="text"
								name="base_url"
								value="${api.baseUrl == null ? '例如：http://www.jpush.cn/': api.baseUrl}"
								oninput="btnDsable()" onblur="onblur_callbackUrl(this.value)"
								onclick="clearExam(this.value)"
								id="base_url" />
								
								<span id="id_base_url" style="color: red;">
							</span><br> <span style="color: red;">*</span> <span
								style="color: ;">若修改域名根地址，请重新下载证文件后再次认证！</span></li>

							<li><label for="id_email"> 下载文件： </label>正确填写回调地址后，点击 <input
								type="button" value=" 下载  " ${api.apiWebsiteValidate==
								0 ? "":" disabled=true "} onclick="downloadKey()" id="dw_key"
								name="dw_key" /> <span style="color: red;"> *</span></li>


							<li><label for="id_email"> 上传文件： </label> 请将文件上传至您网站的根目录。 <span
								style="color: red;"> *</span></li>
							<li><label for="id_email"> 开始认证：</label> 
							<input type="submit" name="validate_url" ${api.apiWebsiteValidate== 0 ? "":"disabled=true "} value=" 开始认证  " id="validate_url" />
								&nbsp; &nbsp; &nbsp;<img src="/resources/images/circle-loader-large.gif" style="display: none" id="img_load" /> 
								<c:if test="${api.apiWebsiteValidate == 1 }">
									<span id="rz_sc" style="color: green;"> <strong>认证成功</strong>
									</span>
								</c:if> 
								 <c:if test="${api.apiWebsiteValidate != 1 }"><span id="df_err" style="color: red;">网站尚未认证！请完成认证！</span></c:if> 
								<span id="scc" style="color: green;"></span>
								 <span id="er" style="color: red;"></span>
							 </li>

							<%-- <li>
									
								</li> --%>

						</ul>
					</form>
				</div>
				<br>
				<div class="right_content">
					<h2>API回调地址设置</h2>
					<form method="post" id="myForm2" class="blueform">
						<div style='display: none'>
							<input type='hidden' name='csrfmiddlewaretoken' value=' ' />
						</div>
						<ul class="form">
							<c:if test="${empty callbackUrls }">
								<li><label for="id_email"> 消息推送API： </label> <strong
									style="color: "> <span class="api_balk_list" id="api_balk_list">${api.baseUrl}</span> 
									<input type='text' name='${cburl.type}_callback' id="${cburl.type}_callback" value="${cburl.backUrl}" style="width: 20%" /> </strong>
									 <input type='button' name='${cburl.type}_button' id="${cburl.type}_button" value=" 保  存  " onClick="updateCallbackUrl(document.getElementById('${cburl.type}_callback').value,'${cburl.type}')" />
									<span id="${cburl.type}_bak_sc" style="color: green;"></span>
									<span id="${cburl.type}_bak_er" style="color: red;"></span></li>
							</c:if>
							<c:forEach items="${callbackUrls }" var="cburl">
								<li><label for="id_email"> <c:if test="${cburl.type=='send_msg' }">
											消息推送API：
										</c:if> <c:if test="${cburl.type=='send_voice' }">
											语音推送回调地址：
										</c:if> </label> <strong style="color: "> <span class="api_balk_list" id="api_balk_list">${api.baseUrl}</span>
										<input type='text' name='${cburl.type}_callback' id="${cburl.type}_callback" oninput="btnSave('${cburl.type}_button')" value="${cburl.backUrl}" style="width: 20%" /> </strong>
										 <input type='button' name='${cburl.type}_button' ${empty cburl.backUrl ? '': 'disabled' }   id="${cburl.type}_button" value=" 保  存  " onClick="updateCallbackUrl(document.getElementById('${cburl.type}_callback').value,'${cburl.type}')" />
									<span id="${cburl.type}_bak_sc" style="color: green;"></span>
									<span id="${cburl.type}_bak_er" style="color: red;"></span></li>
							</c:forEach>
						</ul>
					</form>
				</div>
				<br>

				<div class="clear"></div>
			</div>
		</div>
	</div>

	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		    function vForm(){
		      var re=true;
		        if(!onblur_callbackUrl()){
		         re=false;
		       }
		      return re;
		    }
		    function clearExam(v){
		    	
		    	if(v.indexOf("例如") >= 0){
		    		$("#base_url").val('');
		    	}
		    }
		   
		    function btnDsable(){
		    	  $('#validate_url').attr('disabled',false);
		    	  $('#dw_key').attr('disabled',false);
		    	  $("#rz_sc").html("");
		    	  $("#er").html("");
		    	  $("#scc").html("");
		    	  $(".api_balk_list").html($("#base_url").val());
		    }
		    
		    function downloadKey(){
		        var cburl=$('#base_url').val();
		        if(!onblur_callbackUrl()){
		        	  $('#id_base_url').html("请输入正确的顶级域名!");
			    	  $("#id_base_url").focus();
			    	  return;
		        }
		        var apiId = $('#apiId').val();
		        var url = "${ctx}/api/download_key?baseurl="+cburl+"&saveorupdate="+apiId;
		        location.href=url;
		    }
		   function btnSave(v){
			  var vc = '#'+v;
			  $(vc).attr('disabled',false);
			
		   }
		    function onblur_callbackUrl(v){
		    
		   		  var val=$('#base_url').val();
		          val = $.trim(val);
		    	  var myReg=new RegExp("^(http://(\\w+\\.)+?[a-zA-Z]{2,3})\/?$");
			     if(!myReg.test(val)){
			    	  $('#id_base_url').html("请输入正确的顶级域名!");
			    	  $("#id_base_url").focus();
			    	  return false;
			     }else{
			         $('#id_base_url').html("");
			    	 return true;
			     }
		    }
		    

			 $(function(){
				
					$('#myForm').ajaxForm({ 
						
								beforeSubmit:function(){$('#img_load').show();},
					            url:'${ctx}/api/validate_key',
							    success: function(data){
							    	$("#df_err").html('');
									var r = data.success;
									if(r == true ){
										$('#validate_url').attr('disabled','disabled');
										$('#dw_key').attr('disabled','disabled');
										$('#img_load').hide();
										$("#scc").html(data.info);
										$("#er").html("");  
									}else{
										$('#img_load').hide();
										$("#er").html(data.info); 
										$("#scc").html("");
									}
								} 
					 }); 
				 });
			 
 	
					 function updateCallbackUrl(callbackUrl,type) {
						
						var target_url = "${ctx}/api/updateCallbakUrl";
						var baseUrl = '${api.baseUrl}';
						var vc = '#'+type+'_button'; 
						 $(vc).attr('disabled',true);
						     $.ajax({
						    		url:target_url,
						    		 type:'POST',
						    		 data:{'baseUrl':baseUrl,'callbackUrl':callbackUrl,'type':type},
						         	 success:function( data ) {
						         		var r = data.success;
										if(r == true ){
											
											$("#"+type+"_bak_sc").html(data.info);
											$("#"+type+"_bak_er").html("");  
										}else{
											$("#"+type+"_bak_sc").html(data.info); 
											$("#"+type+"_bak_er").html("");
										}
						        	  }
						    		 });
						      };
			 
						    
      </script>
</body>
</html>