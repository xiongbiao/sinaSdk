<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="application/xhtml+xml; charset=utf-8" />
		<style type="text/css">
* {
	padding: 0;
	margin: 0;
	font-size: 20px;
	font-family: 宋体;
}

h3 {
	background-color: #E6E6E6;
	padding: 0 0 0 15px;
	height: 32px;
	line-height: 32px;
}

.bd {
	line-height: 22px;
	padding: 10px 20px 15px;
}

.b {
	background-image: -webkit-linear-gradient(#79D71B, #4C8611);
	-webkit-border-radius: 10px;
	background-color: #70C519;
	height: 40px;
	width: 100px;
	display: block;
	text-decoration: none;
	color: black;
	text-align: center;
	margin-top: 10px;
	margin-left: 30px;
}

.c {
	display: block;
	padding-top: 8px;
	font: 22px bold;
}
</style>
	</head>
	<body>
		<div style="padding: 15px;">
			<img src="${imgicon!"
				"}" style="float: left; padding-right: 15px; margin-top: 50px" />
			<p>
				${apkName!""}
				<br />
				<br />
				类别:${apkType!""}
				<br />
				版本:${apkVersion!""}
				<br />
				大小:${apkSize!""} MB
			</p> 
		</div>
		<div class="m_b">
			<h3>
				软件介绍
			</h3>
			<div class="bd">
				<p>
					${apkInfo!""}
				</p>
			</div>
		</div>
		<div class="m_b">
			<h3>
				软件截图
			</h3>
			<div class="bd">
				<img src="${imgInfo!" "}" />
			</div>
		</div>
	</body>
</html>