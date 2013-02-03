package com.kkpush.util;

public class CommonConstants {

	// 系统 session 用户
	public final static String SESSION_USER = "user";

	/** 保存在浏览器中cookie的用户名或者邮箱名称名 */
	public final static String BROWSER_COOKIE_USER_NAME = "devName";

	/** 保存在浏览器中cookie的邮箱密码名 */
	public final static String BROWSER_COOKIE_PASSWORD = "devPassword";

	/** 保存在浏览器中cookie的最大时间15天 */
	public final static int BROWSER_COOKIE_MAX_AGE = 15*24*60*60;

	/** 保存在浏览器中cookie的自动登录状态名 */
	public final static String BROWSER_COOKIE_AUTO_STATUS_NAME = "JPUSHLoginStatus";
	/**cookie  */
	public final static String BROWSER_COOKIE_TOKEN = "JPUSHLoginToken";

	/** 保存在浏览器中cookie的退出登录名 */
	public final static String BROWSER_COOKIE_LOGIN_OUT_NAME = "JPUSHLoginOut";

	public interface API{
		/** 消息回调地址 Key */
		public static final String SEND_MESSAGE ="send_msg";
		
	}

}
