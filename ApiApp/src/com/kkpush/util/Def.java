/**
 * 
 */
package com.kkpush.util;

/**
 * @author xiongbiao
 * 
 */
public class Def {
	public static String getErrStr(int code) {
		String result = "";
		switch (code) {
		case -1:
			result = "发送失败";
			break;
		case 1001:
			result = "不支持GET";
			break;
		case 1002:
			result = "缺少了必须的参数";
			break;
		case 1003:
			result = "参数值不合法";
			break;
		case 1004:
			result = "验证失败";
			break;
		case 1005:
			result = "消息体太大";
			break;
		case 1006:
			result = "用户名或者密码错误";
			break;
		case 1007:
			result = "参数不合法";
			break;
		case 1008:
			result = "appKey不合法";
			break;
		case 1009:
			result = "系统繁忙";
			break;
		case 1010:
			result = "消息内容不合法";
			break;
		case 1011:
			result = "没用满足条件的用户";
			break;
		default:
			result = "系统繁忙";
			break;
		}
		return result;
	}
	public static String getAppErrStr(int code) {
		String result = "";
		switch (code) {
		case -101:
			result = "应用包名已存在";
			break;
		case -102:
			result = "未知错误 联系管理员";
			break;
		case -1001:
//            result = "只支持 HTTP Post 方法，不支持 Get 方法";
            result = "未知错误 联系管理员";
            break;
		case -1002:
            result = "token验证失败";
            break;
		case -1003:
            result = "缺少必要参数或参数不合法";
            break;
		case -1004:
            result = "包名已存在";
            break;    
		default:
			result = "未知错误 联系管理员";
			break;
		}
		return result;
	}
	 
}
