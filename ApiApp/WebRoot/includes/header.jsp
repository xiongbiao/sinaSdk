<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<%@ include file="/commons/permission.jsp"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ include file="collectionData.jsp"%>
<script type="text/javascript">
var devId =<%=dev == null ? "" : dev.getDevId()%>
var appPath = "${ctx}/upload_file/appfile/"+devId+"/";
var appid ;
var appsPath="${ctx}";
</script>
<div class="header">
	<div class="container">
		<a href="${ctx}/index.jsp" title="JPush" class="logo"> <span>JPush</span>
		</a>
		<% if(dev!=null){ %>
		<div class="appselect">
			<a href="" title="" class="appselect_button"><span
				id="header_appName">选择应用</span><span class="arrow"></span> </a>
			<div style="display: none;" class="popover">
				<div class="search">
					<input name="appselect_search" id="appselect_search"
						placeholder="请输入要查询的应用" type="text"> <span class="icon"></span>
				</div>
				<ul id="id_menu_app_list">
					<li style="text-align: center; color: white;">应用加载中...</li>
				</ul>
				<div class="buttons">
					<a href="${ctx}/app/newapp.jsp">新增应用</a> <a
						href="${ctx}/app/application_list.jsp">所有应用 <span
						id="app_total">(0)</span> </a>
				</div>
			</div>
		</div>
		<%  } %>
		<ul class="nav">
			<% if(dev!=null){
			  %>
			<li><a class="" href=""> ${user.contact}<span class="arrow"></span>
			</a>
				<div style="display: none;" class="popover">
					<ul>
						<li><a href="${ctx}/dev/account.html">账 号</a></li>
						<!--<li>
							<a href="">广 告</a>
						</li>
						-->
						<li><a href="${ctx}/developer/loginout">退 出</a></li>
					</ul>
				</div></li>
			<%  } %>
			<li><a class="" href="">帮助<span class="arrow"></span> </a>
				<div style="display: none;" class="popover">
					<ul>


						<li><a
							href="http://docs.jpush.cn/pages/viewpage.action?pageId=557074"
							target="_a">常见问题</a></li>

						<li><a
							href="http://docs.jpush.cn/pages/viewpage.action?pageId=557335"
							target="_a">联系我们</a></li>
					</ul>
				</div></li>
		</ul>
	</div>
	<!-- 维护信息
	<div style="position: relative;top: -17px;font-size: 14px;color: #ff0000;margin: 0 auto;z-index: 1;white-space: nowrap;height: 20px;"><div class="protip" style="height: 20px;position: absolute;left: 270px;padding: 3px 10px 0px 10px;">维护通知：系统将于2012年12月18日 23 点至19日 1 点进行维护。给您带来的不便，敬请谅解！</div></div>
	 -->
</div>