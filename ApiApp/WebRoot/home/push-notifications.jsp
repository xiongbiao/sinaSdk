<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.kkpush.util.*"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>推送通知 - 产品介绍 - ${Title }</title>
<%@include file="/includes/jpush_style.jsp"%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/jpushResources/css/sub-page.css" media="screen" />
</head>
<body id="onboard">
	<%@include file="/includes/jpush_header.jsp"%>
	<!-- h1 begin -->
	<h1 class="container">推送通知</h1>
	<!-- h1 end -->

	<!-- sub-page begin   -->
	<div class="container" id="sub-page">
		<%@include file="/includes/jpush_push_left.jsp"%>
		<div class="right_content">
			<div class="columns group" id="content">

				<div class="two-thirds left">
					<h2>保持用户粘性</h2>
					<p class="intro">
						一般来说，用户安装了应用程序后，开发者对其是只有很小的控制力的，也没有办法主动与用户交互，只有被动地等待用户打开应用程序。</p>
					<p class="intro">当您的应用内嵌了 JPush SDK
						后，情况将会改变。您可以通过极光推送平台，主动地向您的用户发起交互，向其发送日程提醒、活动预告、进度提示、动态更新等。</p>

					<p class="intro">在 iOS 平台上，推送功能几乎是所有 App
						的标准配置。用户会经常收到来自于开发者发送的定制通知、提醒信息。 只要这些信息不是过于垃圾，一般也不会取消收取通知 。
						事实证明，这些推送通知，能够有效地提高应用程序的粘性，提高用户留存率。</p>


				</div>

				<div class="one-third right">
					<div id="push-handsets"></div>
				</div>

				<h2 class="benefits">功能</h2>

				<ul class="benefits">
					<li>
						<h3>强大的消息平台</h3>
						<p>极光推送拥有经过海量用户考验的平台推送能力，能够在短时间内向大量用户下发消息推送，并反馈推送结果。</p></li>
					<li>
						<h3>消息推送API</h3>
						<p>极光推送提供消息发送
							API，使得开发者可以在自己的业务服务器上，根据具体的业务需求，向特定的用户发送通知，或者自定义消息。</p></li>
					<li>
						<h3>推送高级控制</h3>
						<ul>
							<li style="margin: 0 0 5px 30px;"><b>定时发送。</b>设定发送通知或者消息的时间，而不是马上向用户发送。
							</li>
							<li style="margin: 0 0 5px 30px;"><b>向指定标签的用户发送。</b>可以给用户打标签，然后发送通知/消息时，以标签的方式，向特定一群用户发送。
							</li>
							<li style="margin: 0 0 5px 30px;"><b>向指定别名的用户发送。</b>使用业务本身的机制，给特定用户取别名。这样推送通知/消息时，可以向指定的特定用户推送了。
							</li>
						</ul></li>

				</ul>

				<h2 class="benefits">好处</h2>
				<ul class="benefits">
					<li>
						<h3>定制化的消息推送</h3>
						<p>在指定的时间，向指定的大批量用户或者少量用户，发送定制的通知或者消息。</p></li>
					<li>
						<h3>留住用户</h3>
						<p>提高用户程序关注度，延长用户使用时间。</p></li>
					<li>
						<h3>用户控制</h3>
						<p>你的用户，可以控制是否打开推送，以及定义推送时间段。</p></li>
					<li>
						<h3>特定用户群</h3>
						<p>使用标签或者别名属性，可以向指定的特定用户推送消息。</p></li>
					<li>
						<h3>清晰的图表</h3>
						<p>一个统一的图表，展示发送的通知数、用户打开应用数与用户使用时长，使得评估通知推送效果变得非常简单。</p></li>
					<li>
						<h3>易于集成</h3>
						<p>客户端 SDK 提供简单易用的接口，少量几个动作即可把集成做起来。</p></li>
				</ul>
			</div>
		</div>

		<div class="clear"></div>
	</div>
	<!-- sub-page end -->

	<%@include file="/includes/jpush_footer.jsp"%>
</body>
</html>