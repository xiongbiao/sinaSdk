package com.yan.android;


public abstract interface IConstants {
	public class URL {
//		@Deprecated
//		public static final String URL_PUSH_TEST = "http://117.135.160.60:8090/cgi-bin/settestuid.py";
//		public static final String URL_SET_UID = "http://117.135.160.60:8090/cgi-bin/settestuid.py";
		@Deprecated
		public static final String URL_SEND_PUSH_TEST = "http://117.135.160.59:8090/cgi-bin/sendmsg3.py";
        		
		public static final String URL_SEND_PUSH = "http://api.jpush.cn:8800/sendmsg/sendmsg";
		public static final String URL_GEI_UID = "http://183.232.25.111:8090//cgi-bin/getuidbyappidimei.py";
	}
}
