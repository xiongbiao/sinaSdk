package com.kkpush.util;

/**
 * 
 * @author
 * 
 */
public class Constants {

	/**
	 * report function
	 */
	public static final String REPORT_FUNCTION = "reportFunction";

	public static final String PARAMETER_TOKEN = "-^-";

	public static final String PARAMETER_VALUE_TOKEN = "~";

	public static final String RPT_PARAM_START_DATE = "startDate";

	public static final String RPT_PARAM_END_DATE = "endDate";

	public static final String RPT_PARAM_FIELD = "field";

	public static final String RPT_PARAM_KPI_FIELD_ID = "kpiFieldId";

	public static final String RPT_PARAM_DEFAULT_DAYS = "defaultDays";

	public static final String RPT_DF = "rpt_df";

	public static final String RPT_KPI_CODE_NEW_REGISTER_USER_FRIEND = "NEW_REGISTER_USER_FRIEND";

	public static final String RPT_KPI_CODE_LBS_PUSH_FRIEND = "LBS_PUSH_FRIEND";
	
	public static final String RPT_PARAM_DEV_ID = "devId";
	public static final String RPT_PARAM_APP_ID = "appId";
	public static final String RPT_PARAM_ALIAS = "alias";
	public static final String RPT_PARAM_PUSHES="pushes";
	public static final String RPT_PARAM_OPENS = "opens";
	public static final String RPT_PARAM_TIMES = "times";
	public static final String RPT_PARAM_NEWUSER = "newUser";
	public static final String RPT_PARAM_ONLINEUSER = "onlineUser";
	public static final String RPT_PARAM_ACTIVEUSER = "activeUser";

	public interface PUSH {
		public interface CHANNEL {
			public static final int ENABLED_FLAG_ENABLED = 1;

			public static final int ENABLED_FLAG_DISABLED = 0;
		}

		public interface AD {

			/** 通知 */
			public static final int MSG_D = 1;
			/** 自定义消息 */
			public static final int MSG_MY = 0;

			// 广告类型
			public static final int TYPE_ALL = -1;
			public static final int TYPE_SHOW = 0;
			public static final int TYPE_APK = 1;
			public static final int TYPE_VIDEO = 2;

			// 客户端提醒模式
			public static final int RING_MODE_FULL = 0;
			public static final int RING_MODE_SOUND = 1;
			public static final int RING_MODE_VIBRATION = 2;
			public static final int RING_MODE_NONE = 3;

			// 消息栏图标
			public static final int NOTIFY_ICON_DEFAULT = 0;
			public static final int NOTIFY_ICON_DOWNLOAD = 1;

			// 运营商
			public static final int NET_ALL = 0;
			public static final int NET_CHINA_MOBILE = 1;
			public static final int NET_CHINAUNICOM = 2;
			public static final int NET_CHINA_TELECOM = 3;

			public static final int STATUS_NORMAL = 0;
			public static final int STATUS_DELETE = 1;
			public static final int STATUS_IN_POOL = 2;

		}
	}

	public interface FINANCE_STATUS {
		public static final int NOTSUB = 0;
		public static final int AUDITING = 1;
		public static final int AVAILABLE = 2;
	}

	public static final String RATE_TYPE_ARRIVAL = "ARRIVAL_RATE";
	public static final String RATE_TYPE_SHOW_CLICK = "SHOW_CLICK_RATE";
	public static final Integer DEFAULT_RATE_ID = 0;
}
