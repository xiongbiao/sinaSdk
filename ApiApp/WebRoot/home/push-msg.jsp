<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kkpush.util.*"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>推送工具 - 产品介绍 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>
	<!-- h1 begin -->
	<h1 class="container">推送工具</h1>
	<!-- h1 end -->
	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%@include file="/includes/jpush_push_left.jsp"%>

		<div class="right_content">
			<div class="columns group" id="content">


				<div class="  left">
					<h2>易用的推送工具</h2>
					<p class="intro">极光推送管理 Portal
						提供易用的消息推送工具，使得您公司的业务运营人员，能够很简单方便地操作、推送通知。</p>

					<h3>消息内容</h3>
					<p>
						<img alt="push 推送    消息内容" style="width: 650px;"
							src="${ctx}/resources/jpushResources/images/img_tools_desc_1.jpg">
					</p>

					<h3>选择接收用户</h3>
					<p>
						<img alt="push 推送  选择接收用户" style="width: 650px;"
							src="${ctx}/resources/jpushResources/images/img_tools_desc_2.jpg">
					</p>

					<h3>接收到消息</h3>
					<p>
						<img alt="push 推送  接收到消息" style="width: 650px;"
							src="${ctx}/resources/jpushResources/images/img_tools_desc_3.jpg">
					</p>
				</div>

				<h2 class="benefits">功能</h2>
				<ul class="benefits">
					<li>
						<h3>简单的 Web 操作页面</h3>
						<p>极光推送提供简单 、 直观的 Portal 操作界面 ，使得普通商务、运营人员都可轻易地操作、查看、 分析。</p>
						<ul>
							<li style="margin: 0 0 5px 30px;">通知预览</li>
							<li style="margin: 0 0 5px 30px;">推送历史</li>
							<li style="margin: 0 0 5px 30px;">统一推送结果图表</li>
						</ul></li>
					<li>
						<h3>标签</h3>
						<p>
							标签使得你可以向不同部分用户发送不同的通知。开发者可以根据用户行为、兴趣、邮政编码、设备类型等各种信息，给用户打标签。</p>
						<ul>
							<li style="margin: 0 0 5px 30px;">推送消息给指定标签的部分用户</li>
							<li style="margin: 0 0 5px 30px;">SDK 客户端提供 API 供修改该用户的标签</li>
							<li style="margin: 0 0 5px 30px;">推送可以在不同的时间，到达不同的用户群</li>
						</ul></li>
					<li>
						<h3>别名</h3>
						<p>别名使得你可以为某个特定用户发送通知。开发者可以根据用户ID 、用户名等属性 ，设置为用户的别名。</p>
						<ul>
							<li style="margin: 0 0 5px 30px;">推送消息给指定的用户</li>
							<li style="margin: 0 0 5px 30px;">SDK 客户端提供 API 供修改该用户的别名</li>
							<li style="margin: 0 0 5px 30px;">消息发送 API
								也支持别名，这样开发者可以在自己的业务服务器里，根据用户的业务状态，随时调用 JPush 消息推送 API
								给该用户推送通知或者消息。</li>
						</ul></li>
					<li>
						<h3>定时推送</h3>
						<p>可以给某条消息推送，设定执行时间。</p></li>
				</ul>

				<h2 class="benefits">优势</h2>
				<ul class="benefits">
					<li>
						<h3>简单易用的 Portal</h3>
						<p>推送消息与查询结果可以不需要开发人员参与。</p></li>
					<li>
						<h3>向特定用户发送</h3>
						<p>提供标签与别名的机制，可向特定用户推送通知与消息。</p></li>
				</ul>
			</div>

		</div>
		<div class="clear"></div>
	</div>
	<!--  sub-page end  -->

	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>