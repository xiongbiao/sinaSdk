<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.kkpush.account.domain.Developer"%>
<div class="header">
	<a class="logo" href="http://www.jpush.cn/"></a>
	<div class="header_apps">
		<a class="header_apps_button" title="packageTest"
			onclick="headerButton()"> 应用列表 <span class="arrow"></span> </a>
		<div class="popover" id="app_list" style="display: none;">
			<div class="search">
				<input type="text" placeholder="请输入应用名称" id="appselect_search"
					name="appselect_search"> <span class="icon"></span>
			</div>
			<ul id="id_menu_app_list">
				<li onclick="window.location='/apps/qebbtNq6QZWfSb-k4vaFUA/'">
					<div class="app_icon"></div>
					<h3></h3>
					<p class="meta">Development</p>
					<p class="meta">
						<em><span data-type="devices"
							data-key="qebbtNq6QZWfSb-k4vaFUA" class="app_counts_ajax">0</span>
						</em> Active Users
					</p></li>
				<li onclick="window.location='/apps/VSod43YoST2wq7-YqsxUJw/'">
					<div class="app_icon"></div>
					<h3>dd</h3>
					<p class="meta">Development</p>
					<p class="meta">
						<em><span data-type="devices"
							data-key="VSod43YoST2wq7-YqsxUJw" class="app_counts_ajax">0</span>
						</em> Active Users
					</p></li>

				<li onclick="window.location='/apps/26KyQie7R-atRKb_2bbzXQ/'">
					<div class="app_icon"></div>
					<h3>33</h3>
					<p class="meta">Development</p>
					<p class="meta">
						<em><span data-type="devices"
							data-key="26KyQie7R-atRKb_2bbzXQ" class="app_counts_ajax">0</span>
						</em> Active Users
					</p></li>

			</ul>
			<div class="buttons">
				<a href="/apps/new/">Create New App</a> <a href="/apps/">View
					All Apps <span>(12)</span>
				</a>
			</div>
		</div>
	</div>

	<ul class="nav">
		<li><a onclick="devNameButton()">xiongbiao<span class="arrow"></span>
		</a>
			<div class="popover" id="dev_info">
				<ul>
					<li><a href="/accounts/">财务管理</a>
					</li>
					<li><a href="/accounts/">个人信息</a>
					</li>
					<li><a href="/accounts/logout/">退出</a>
					</li>
				</ul>
			</div></li>
	</ul>

	<%  Developer topDev =request.getSession().getAttribute("user")==null?null:((Developer)request.getSession().getAttribute("user")) ;
	      if(topDev!=null){
	    	  %>
	<div style="float: right; margin-right: 100px; margin-top: 30px;">
		<span style="color: white;"> 欢迎您 ： ${user.contact}</span>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
			href="${ctx}/login.jsp"> <span> 退出 </span> </a>
	</div>
	<%
	      }
	    %>
</div>
