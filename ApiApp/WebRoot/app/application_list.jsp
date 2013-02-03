<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>你的所有应用 - ${Title}</title>
<%@include file="/includes/style.jsp"%>
<style>
#loading {
	width: 150px;
	height: 38px;
	border-top: none;
	top: 0px;
	left: 50%;
	margin-left: 40%;
	text-align: center;
	line-height: 25px;
	font-size: 12px;
	font-weight: bold;
}

#loadingi {
	background: url("/resources/images/circle-loader-large.gif");
	width: 38px;
	margin-left: 40px;
	height: 38px;
}

.tablesorter {
	border: 1px solid #C7C7C7;
}

.tablesorter th,.tablesorter td {
	vertical-align: middle;
}

.tablesorter tr th {
	background-color: #F0F0F0;
	font-weight: normal;
	text-shadow: 0 1px 0 white;
	height: 30px;
	border-right: 1px solid #D2D2D2;
	color: #444;
}

.tablesorter tr.first_tr th:nth-child(n+2) {
	border-bottom: 1px solid #D2D2D2;
	box-shadow: inset 0 1px 0 white;
	cursor: pointer;
}

.tablesorter tr th.last,.tablesorter tr td.last {
	border-right: 0;
}

.tablesorter tr td {
	border-right: 1px solid #CCC;
	padding: 10px 0 10px 5px;
	line-height: 200%;
}

.app_name_w #reports_tab {
	max-width: 95%;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	float: left;
}

.tablesorter tr.odd_number td {
	background-color: #F0F0F0;
}

.tablesorter tr.even_number td {
	background-color: #FFFFFF;
}

.tablesorter .app_name_w {
	position: relative;
}

.tablesorter a.delete_app {
	display: block;
	position: absolute;
	right: 13px;
	z-index: 100;
	top: 2px;
	text-decoration: none;
}

.tablesorter tr td.last span a {
	height: 16px;
	padding-left: 25px;
	display: block;
	margin-right: 5px;
}

.tablesorter tr td.last {
	text-align: center;
}

.tablesorter tr td.last span.char_01 a {
	background: url(${ctx}/resources/images/icon_reports.png) no-repeat;
}

.tablesorter tr td.last span.char_03 a {
	background: url(${ctx}/resources/images/icon_push_composer.png)
		no-repeat;
}

.fl {
	float: left;
}

.fr {
	float: right;
}
</style>
</head>
<body id="default">
	<%@include file="/includes/header.jsp"%>
	<div class="inside">
		<div class="container">
			<div id="content">
				<h2>
					<span id="allAppTitle" style="display: none;">你的所有应用</span>
				</h2>

				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="tablesorter" id="apps-table">
					<tr class="first_tr">
						<th width="6%"></th>
						<th>应用名</th>
						<th width="8%">用户总数</th>
						<th width="14%">在线用户[今日/昨日]</th>
						<th width="14%">新增用户[今日/昨日]</th>
						<th width="14%">活跃用户[今日/昨日]</th>
						<th width="14%">启动次数[今日/昨日]</th>
						<th width="11%" class="last">操作</th>
					</tr>
				</table>

				<div id='loading'>
					<div id="loadingi"></div>
					<div>正在加载……</div>
				</div>

				<div>
					<h2>创建应用程序</h2>
					<form method="post" action="${ctx}/app/saveApp" id="myForm"
						enctype="multipart/form-data" class="blueform"
						onsubmit="return vForm()">
						<div style="display: none">
							<input type="hidden" name="" value="">
						</div>
						<ul class="form">
							<li><label for="id_name"> 应用名称 </label> <input type="text"
								value="" id="appName" name="appName" onblur="blur_appName()"
								placeholder="例如：海绵宝宝" datatype="s1-1000" errormsg="应用名称不能为空！" />
								<span class="red">*</span> <span id="appNameTip"></span></li>
							<li><label for="id_icon"> 应用图标 </label> <input type="file"
								name="appIcon" id="appIcon" nselectable="on"
								onchange='checkFileType(this,".png、.jpg、.gif");'> <span
								id="appIconTip" class="red"></span>
								<div class="Validform_checktip"></div></li>
							<li><label for="id_name"> 应用包名 (Android) </label> <input
								type="text" value="" id="appPackage" onblur="blur_appPackage()"
								placeholder="例如：cn.g.haimian 请确保与您的Android应用包名相同"
								name="appPackage" class="inputxt"
								ajaxurl="/app/isExistsAppPackage" datatype="s1-1000"
								errormsg="Android 包名不能为空!" /> <span id="appPackageTip">包名填写后不可更攺。</span>
							<li><span class="blue1">特别提示：这里填写的包名，必须与您自己的
									Android客户端应用包名保持一致。</span></li>
							</li>
							<li><label for="id_name"> APNs证书文件 (iOS) </label> <input
								type="file" name="appleCertificate" id="appleCertificate"
								nselectable="on" onchange='checkFileType(this,".p12");'>
								<span id="appleCertificateTip" class="red"> </span></li>
							<li><span class="blue1">APNs证书文件：这是 iOS SDK 能够接收到
									JPush 推送消息的必要文件。具体请参考： <a
									href="http://docs.jpush.cn/pages/viewpage.action?pageId=1343727"
									target="_a">iOS 证书设置指南</a>
							</span></li>
							<li><label for="id_name"> APNs证书密码 (iOS) </label> <input
								type="password" value="" id="certificatePass" autocomplete="off"
								name="certificatePass" class="inputxt" /> <span
								id="certificatePassTip" class="red"></span></li>
							<li><label for="id_mode"> 部署状态 </label> <!-- <input type="checkbox"   id="appStage" name="appStage" checked/>
								    开发状态 --> <input type="radio" checked="checked"
								name="appStage" value="0"> 开发状态 <input type="radio"
								name="appStage" value="1"> 生产状态</li>
							<li><span class="blue1">开发状态:连接开发服务器。 生产状态:连接生产服务器。</span></li>
						</ul>
						<!-- PUSH -->
						<ul class="form">
							<!--<li>
									<label for="id_push_enabled">
										开启推送
									</label>
									<input type="checkbox" name="push_enabled" id="id_push_enabled">
								</li>
								-->
							<!--<li>
									<label for="id_push_enabled">
										 上面的推送未开启时，这个字段不可点击，为 disabled 
									</label>
									<input type="checkbox" name="push_enabled" id="id_push_enabled">
								</li>
							-->
						</ul>
						<!-- SUBMIT -->
						<ul class="form">
							<li><input type="hidden" name="app_group" value="28212"
								id="id_app_group"> <input type="submit" id="btn_sub"
								value="创建应用程序"><span id="er" style="color: red;"></span>
							</li>
							<li><small><span class="red">*</span> 表示必须填写。 </small></li>
						</ul>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%@include file="/includes/footer.jsp"%>
	<script type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<script type="text/javascript">
		function LTrim(str) { //去掉字符串 的头空格
			var i;
			for (i = 0; i < str.length; i++) {
				if (str.charAt(i) != " " && str.charAt(i) != " ")
					break;
			}
			str = str.substring(i, str.length);
			return str;
		}

		function RTrim(str) {
			var i;
			for (i = str.length - 1; i >= 0; i--) {
				if (str.charAt(i) != " " && str.charAt(i) != " ")
					break;
			}
			str = str.substring(0, i + 1);
			return str;
		}
		function Trim(str) {
			return LTrim(RTrim(str));
		}

		var result=false;
		function blur_appPackage() {
			var result = false;
			var _value = $('#appPackage').val();
			_value = Trim(_value);
			if (_value.length>0) { 
				$('#appPackageTip').html("<span >Android 包名检查中……</span>");
				$.ajax({
	                type: "POST",
	                dataType:"json",
	                url: $('#appPackage').attr("ajaxurl"),
	                data: { "appid": null ,"name":$('#appPackage').attr("name"),"param":_value},
	                success: function(data) {
						result=data.success;
						if (data.success=="true") {
						    $('#appPackageTip').html("<span >此包名可用</span>");
						} else {
						    $('#appPackageTip').html("<span class='red'>"+data.info+"</span>");   
						}
	                },
	                error: function() {
	                    $('#appPackageTip').html("<span >服务器繁忙稍后再试……</span>");   
	                }
				}); 
		    }else{
				$('#appPackageTip').html(""); 
				return true;
		    }
		    return result;
		}
		function blur_appName(){
			var _value = $('#appName').val();
			if(_value.length==0){
				$('#appNameTip').html("<span class='red'>"+$('#appName').attr("errormsg")+"</span>")
				return false;
			}else{
				$('#appNameTip').html("");
				return true;
			}
		}
		function checkFileType(_this,typeValue) {  
            var tv = typeValue.split("、");
            var iiOK = true;
            var str = _this.value
            var pos = str.lastIndexOf(".");  
            var lastname = str.substring(pos,str.length);  
            var resultName=lastname.toLowerCase();  
             for(var i =0 ; i<tv.length;i++){
               if (tv[i] == resultName.toString()){
                  iiOK = false;
                  break;
               }
            }
            if (iiOK){
               $('#'+_this.name+'Tip').html('请上传'+typeValue+'类型的文件');
               _this.value = "";
            }  
            else{
               $('#'+_this.name+'Tip').html("");
            }
        }  
		function vForm() {
			$("#er").html("");
			var isSu = true;
			
			 if(!blur_appName()){
				    isSu = false;
				    return isSu;
		    }
			
			var acf = $("#appleCertificate").val();
			var acfPass = $("#certificatePass").val();
			if ((acf != "")) {
				if (acfPass == null || acfPass == "") {
					isSu = false;
					$("#certificatePassTip").html("请输入APNs证书密码");
				} else {
					$("#certificatePassTip").html("");
				}
			} else {
				$("#certificatePassTip").html("");
			}
			if(acfPass!=""){
				if (acf == null || acf == "") {
					isSu = false;
					$("#appleCertificateTip").html("你填写APNs证书密码 ,请上传APNs证书");
				} else {
					$("#appleCertificateTip").html("");
				}
			} else {
				$("#appleCertificateTip").html("");
			}
			if(isSu){
				  $('#btn_sub').attr("disabled","disabled");
			}
			else{
				  $('#btn_sub').removeAttr("disabled");
				//$("#conditions1").hide();
			}
			return isSu;
		}
 
		$(function(){
		   jQuery.post("${ctx}/app/getAppList", {"pageSize":500}, function(data) {
		       var allApp = '';
		       var apps = [];
		       if(data!=null){
		         var total = data.total;
		         var rows = data.rows;
		         if(rows!=null&&rows.length>0){
		         $('#allAppTitle').show();
			      for(var i =0; i<rows.length;i++ ){
			      	var appStatus ="";
			        if(rows[i].appStage==0){
			        appStatus="开发状态";
			        }
			        if(rows[i].appStage==1){
			        appStatus='<span style="color:red">生产状态</span>';
			        }
			        var platform = [];
			        if (rows[i].platform) {
				        if (rows[i].platform.indexOf('a')>=0) {
				        	platform.push('<img width="20" alt="android" title="android" src="${ctx}/resources/images/icon-android.png">');
				        }
				        if (rows[i].platform.indexOf('i')>=0) {
				        	platform.push('<img width="20" alt="ios" title="ios" src="${ctx}/resources/images/icon-ios.png">');
				        }
			        }
					var row = rows[i];
					apps.push('<tr class="'+(i%2==0?'even_number':'odd_number')+'">');
					apps.push('<th class="first">'+platform.join("&nbsp;&nbsp;")+'</th>');
					apps.push('<td><div class="app_name_w"><a id="reports_tab" href="${ctx}/app/detail/'+row.appId+'.html">', row.appName, '</a><a class="delete_app">'+appStatus+'</a></div></td>');
					apps.push('<td>', row.devicesPer, '</td>');
					apps.push('<td>', row.onlineUserToday , ' / ', row.onlineUserYesterday, '</td>');
					apps.push('<td>', row.newUserToday , ' / ', row.newUserYesterday, '</td>');
					apps.push('<td>', row.activeUserToday , ' / ', row.activeUserYesterday, '</td>');
					apps.push('<td>', row.startAppToday , ' / ', row.startAppYesterday, '</td>');
			        apps.push('<td class="last"><span class="fl char_01" title="统计"><a href="${ctx}/report/pushes/'+row.appId+'.html"></a></span>');
			        apps.push('<span class="fl char_03" title="发送通知"><a href="${ctx}/push/newMsg/'+row.appId+'.html"></a></span><div class="clear"></div></td>');
			        apps.push("</tr>");
			       } 
		         }else{
		          $('#allAppTitle').hide();
		          apps.push('<tr class="even_number"><th class="first"></th><td colspan="7" style="text-align:center">您还没有应用!</td></tr>');
		         }
		       }
		        $('#loading').hide();
		        $("#apps-table").append(apps.join(''));
		    });
		
            $('#myForm').ajaxForm({  
				        url:'${ctx}/app/saveApp',
						dataType : 'json',
				        success: function(data){ 
						     var r=data.success;
						     if (r == true) {
								 if(data.ios!=1){
								    window.location = '${ctx}/app/detail.jsp?appId=' + data.appId;
								   } else{
									 $("#er").html(data.info);
								   }
							} else {
								  $("#appDescription").html(data.info);
							      $("#er").html(data.info);
							}//if 
				        	 $('#btn_sub').removeAttr("disabled");
				       }//function   
				    });//ajax 
			
		});
		</script>
</body>
</html>
