<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>应用详情 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<%@include file="/includes/sidebar.jsp"%>
				<div class="right_content">
					<h2>应用详情</h2>
					<h2>
						<div id="app_icon" class="app_icon"
							style="background-image: url(/resources/images/default_app_icon_57x57.png);">
						</div>
						<span id="detail_appname"></span>
					</h2>
					<div class="protip" id="id_protip">
						<strong>提示:</strong> <span id="id_no_package"> </span><br>
					</div>
					<div class="bluebox">
						<dl class="app_info">
							<dt>部署状态</dt>
							<dd id="app_stage"></dd>
							<dt>应用标识 (AppKey)</dt>
							<dd id="app_key"></dd>
							<dt>API MasterSecert</dt>
							<dd id="master_secert"></dd>
							<dt>应用包名 (Android)</dt>
							<dd id="appPackage"></dd>
							<dt></dt>
							<dd style="color: red" id="appAttention"></dd>
							<dt>APNs证书文件 (iOS)</dt>
							<dd id="iOSCP"></dd>
							<dt>创建时间</dt>
							<dd id="app_itime"></dd>
							<dt>用户数量（全部）</dt>
							<dd>
								<span class="num" id="device_total">-</span>
							</dd>
							<dt>活跃的用户（当月）</dt>
							<dd>
								<span class="num" id="active_user">-</span>
							</dd>
							<dt>推送的数量（当月）</dt>
							<dd>
								<span class="num" id="monthly_pushes">-</span>
							</dd>
							<dt>推送的数量（全部）</dt>
							<dd>
								<span class="num" id="all_pushes">-</span>
							</dd>
							<dt></dt>
							<dd>
								<div style="margin-top: 40px;">
									<button name="下载  Android Example" id="dowmloadbut">下载
										Android Example</button>
									<button name="删除应用" id="delbut">删除应用</button>
								</div>
							</dd>
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
   $('#app_secret_show').click(function(){
		  $('#app_secret').show();
		  $('#app_secret_show').addClass("hidden");
		  $('#app_secret_hide').removeClass("hidden");
		});
     $('#app_secret_hide').click(function(){
		   $('#app_secret').hide();
		   $('#app_secret_hide').addClass("hidden");
		   $('#app_secret_show').removeClass("hidden");
		});	
  
	 if(appid!=null){
    	jQuery.post("/app/getApp?appId="+appid,  function(data) {
    	      $("#detail_appname").html(data.info.appName);
    	      $("#app_key").html(data.info.appKey);
    	      $("#master_secert").html(data.info.apiMasterSecret);
    	      $("#appPackage").html(data.info.appPackage);
    	      if(data.info.appPackage==null||data.info.appPackage==""){
    	    	  $("#id_protip").show();
    	    	  $("#id_no_package").html("你应用还没有Android包名 不能接收到消息。 你通过 <a href='${ctx}/app/edit/"+appid+".html' > <span> 修改应用 </span> </a>来填写你的包名");
    	      }else{
    	    	  $("#id_no_package").html('');
    	    	  $("#id_protip").hide();
    	      }
    	      
    	      var appStage="";
    	       var serverT = ""; 
    	       var isIOS = false;
    	       var appstage = data.info.appStage;
		        if(appstage ==0){
		          appStatus="开发状态，连接到开发服务器。";
		          serverT = "连接到开发服务器。";
		        }
		        if(appstage ==1){
		           appStatus="生产状态，连接到生产服务器。";
		            serverT = "连接到生产服务器。";
		            isIOS= true;
		        }
    	      $("#app_stage").html(appStatus);
    	      if(isIOS){
    	    	  if(data.info.appleData!=null){
    	    		  $("#iOSCP").html("已经上传生产APNs证书");
    	    	  }
    	    	  else{
 					 $("#iOSCP").html("未上传生产APNs证书");
 				 }
    	      }
    	      else{
				 if(data.info.appleDataTest!=null){
					 $("#iOSCP").html("已经上传开发APNs证书");
    	    	  }
				 else{
					 $("#iOSCP").html("未上传开发APNs证书");
				 }
    	      }
    	     
    	      var isPushMsg = "";
    	      if(data.info.isPushMsg==1){
    	         isPushMsg = "启用,"+serverT;
    	      }else{
    	         isPushMsg = "禁用。不能推送消息和通知。";
    	      }
    	      $("#app_itime").html(data.info.itime);
    	      $("#isPushMsg").html(isPushMsg);
    	      $("#device_total").html(data.info.devicesPer);
    	      $("#active_user").html(data.info.activeUserPer);
    	      $("#monthly_pushes").html(data.info.pushesAmount);
    	      $("#all_pushes").html(data.info.pushesPer);
    	      if(data.info.appIcon!=null){
    	      $("#app_icon").css("backgroundImage","url("+appPath+data.info.appId+"/"+data.info.appIcon+")");
    	      }
    		  $("#appAttention").html("Android开发者注意：这里的包名和AppKey必须与AndroidManifest.xml配置的保持一致。");
    	      var appId = data.info.appId;
    	      $("#delbut").bind('click',{appId:appId,appstage:appstage},function(event) {
    	    	  delapp(event);
    	      });
    	      $("#dowmloadbut").bind('click',{appId:appId,type:"android",isUpdate:"0","appPackage":data.info.appPackage},function(event){
    	    	 dowmloadExamlpe(event); 
    	      });
    	});
    }
 })

	 function dowmloadExamlpe(event){
	    var appPackage  =  event.data.appPackage ;
	     if(appPackage!=null&&appPackage.length!=""){
	        var appid  =  event.data.appId ;
			 var type = event.data.type;
			 var isUpdate = event.data.isUpdate;
			 jQuery.post("/app/dowmloadExample?appId="+appid+"&type="+type+"&isUpdate="+isUpdate,  function(data) {
		    	 if(data.success){
	 	    		 window.location.href = "${ctx}"+data.info;
		    	 }else{
		    		 alert(data.info);
		    	 }
			 }); 
	     }else{
	    	  alert("应用不存在Android包名,请完善应用资料。");	 
		 }
	 }
 
	function delapp(event){
		 var appstage = event.data.appstage ;
		 if(appstage != 0){
			 alert('只有开发状态的应用才能删除!');
			 return false ;
		 }
		 var appid = event.data.appId;
		 var gnl=confirm("应用删除后将不能恢复，确定删除应用吗?"); 
		 if(gnl){
			 $("#delbut").attr("disabled", "disabled");
			 jQuery.post("/app/deleteApp?appId="+appid,  function(data) {
		    	 if(data.success){
		    		 window.location.href = '${ctx}/app/application_list.jsp';
		    	 }else{
		    		 alert(data.info);
		    		 $("#delbut").removeAttr("disabled");
		    	 }
			 }); 
		 }
	 }
</script>
</body>
</html>
