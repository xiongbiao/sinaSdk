package com.kkpush.util;


/***
 * 接口URl
 * @author xiongbiao
 *
 */
public class HttpUrls {
 
	/**注册AUTH_TOKEN连接*/
	public static final String AUTH_TOKEN_URL=SystemConfig.getProperty("reg_url");
	/**单独推送*/
	public static final String PUSH_MSG_URL=SystemConfig.getProperty("one_url");
	/**批量推送*/
	public static final String PUSH_BATH_MSG_URL=SystemConfig.getProperty("all_url");
    /**添加新应用**/
	public static final String NEW_APP_URL=SystemConfig.getProperty("app.url");
	 /**删除应用**/
	public static final String DEL_APP_URL=SystemConfig.getProperty("delapp.url");
	/***开发推送地址*/
	public static final String PUSH_M_URL=SystemConfig.getProperty("pushmsg.url");
	/**查询包名是否存在*/
	public static final String SEARCH_APP_URL=SystemConfig.getProperty("app.search");
	/**修改应用的包名*/
	public static final String UPADTE_APP_URL=SystemConfig.getProperty("app.SEARCH");
	/***开发推送地址*/
	public static final String PUSH_M_LIST_URL=SystemConfig.getProperty("pushmsg.list.url");
	/***回调url**/
	public static final String PUSH_CALLBACK=SystemConfig.getProperty("pushmsg.callback");
	/***登录**/
	public static final String PUSH_LOGIN=SystemConfig.getProperty("push.login");
	/**iOS验证接口*/
	public static final String IOS_C_V = SystemConfig.getProperty("push.ios.certificatevalidation");
	
}
