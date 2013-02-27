package com.airpush.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.airpush.data.MsgInfo;
import com.airpush.data.SetPreferences;
import com.airpush.data.DownloadMsgInfo;
import com.airpush.util.LogUtil;
import com.airpush.util.StringUtils;

public class FormatAds implements IConstants {
	private Context context;
	private long nextMessageCheckValue;
	private String adType;
	private static String TAG = LogUtil.makeLogTag(FormatAds.class);

	public FormatAds(Context context) {
		this.context = context;
	}

	public synchronized void parseMsg(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			String mId = json.optString(MsgInfo.KEY_MSG_ID, "");
			if (!StringUtils.isEmpty(mId)) {
				MsgInfo msg = null;
				int msgType = json.optInt(MsgInfo.KEY_MSG_TYPE, -1);
				switch (msgType) {
				case 1:
					msg = new DownloadMsgInfo();
					break;
				default:
					LogUtil.w(TAG, "Unknow ad type - " + adType);
					return;
				}
				msg.parse(context, json);
				new DeliverNotification(this.context,msg);
			    this.nextMessageCheckValue = getNextMessageCheckTime(json);
				SetPreferences.setSDKStartTime(this.context,this.nextMessageCheckValue);
				PushNotification.reStartSDK(this.context, true);
			} else {
				LogUtil.i(TAG, "msgId is not present in json");
			}
		} catch (JSONException je) {
			LogUtil.e(TAG, "Error in push JSON: " + je.getMessage());
			LogUtil.e(TAG, "Message Parsing.....Failed : " + je.toString());
		} catch (Exception e) {
			LogUtil.e(TAG, "Epush parse: " + e.getMessage());
		}

	}

	void pareseMsg() {

	}

//	public synchronized void parseJson(String jsonString) {
//		this.nextMessageCheckValue = 14400000L;
//		JSONObject json = null;
//		if (jsonString.contains("nextmessagecheck")) {
//			try {
//				json = new JSONObject(jsonString);
//				this.nextMessageCheckValue = getNextMessageCheckTime(json);
//				this.adType = (json.isNull("adtype") ? "invalid" : json
//						.getString("adtype"));
//				if (!this.adType.equals("invalid")) {
//					MsgInfo.setMsgType(this.adType);
//					getAds(json);
//					return;
//				}
//				SetPreferences.setSDKStartTime(this.context,
//						this.nextMessageCheckValue);
//				PushNotification.reStartSDK(this.context, true);
//			} catch (JSONException je) {
//				LogUtil.e(TAG, "Error in push JSON: " + je.getMessage());
//				LogUtil.e(TAG, "Message Parsing.....Failed : " + je.toString());
//			} catch (Exception e) {
//				LogUtil.e(TAG, "Epush parse: " + e.getMessage());
//			}
//		} else {
//			LogUtil.i(TAG, "nextmessagecheck is not present in json");
//		}
//	}

	private long getNextMessageCheckTime(JSONObject json) {
		Long nextMsgCheckTime = Long.valueOf(Long.parseLong("300") * 1000L);
		try {
			nextMsgCheckTime = Long.valueOf(Long.parseLong(json.get(
					"nextmessagecheck").toString()) * 1000L);
		} catch (Exception e) {
			e.printStackTrace();
			return 14400000L;
		}
		return nextMsgCheckTime.longValue();
	}

	private void buildeWeb(MsgInfo msg, JSONObject json) {
		try {

		} catch (Exception e) {
			LogUtil.e(TAG, "Push parsing error: " + e.getMessage());

			LogUtil.e(TAG, "Push Message Parsing.....Failed ");
			try {
				SetPreferences.setSDKStartTime(this.context,
						this.nextMessageCheckValue);
				PushNotification.reStartSDK(this.context, true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} finally {
			try {
				SetPreferences.setSDKStartTime(this.context,
						this.nextMessageCheckValue);
				PushNotification.reStartSDK(this.context, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	private void getAds(JSONObject json) {
//		try {
//			String title = json.isNull("title") ? "New Message" : json
//					.getString("title");
//			String text = json.isNull("text") ? "Click here for details!"
//					: json.getString("text");
//			String creativeid = json.isNull("creativeid") ? "" : json
//					.getString("creativeid");
//			String campaignid = json.isNull("campaignid") ? "" : json
//					.getString("campaignid");
//
//			MsgInfo.NotificationInfo.setNotification_title(title);
//			MsgInfo.NotificationInfo.setNotification_text(text);
//			Util.setCampId(campaignid);
//			MsgInfo.setMsgId(creativeid);
//
//			// if ((this.adType.equals("W")) || (this.adType.equals("BPW"))) {
//			// String url = json.isNull("url") ? "nothing" :
//			// json.getString("url");
//			// String header = json.isNull("header") ? "Advertisment" :
//			// json.getString("header");
//			// MsgInfo.WebShow.setNotificationUrl(url);
//			// Util.setHeader(header);
//			// } else if ((this.adType.equals("A")) ||
//			// (this.adType.equals("BPA"))) {
//			// String url = json.isNull("url") ? "nothing" : json
//			// .getString("url");
//			// String header = json.isNull("header") ? "Advertisment" : json
//			// .getString("header");
//			// Util.setNotificationUrl(url);
//			// Util.setHeader(header);
//			// } else if ((this.adType.equals("CM"))
//			// || (this.adType.equals("BPCM"))) {
//			// String number = json.isNull("number") ? "0" : json
//			// .getString("number");
//			// String sms = json.isNull("sms") ? "" : json.getString("sms");
//			// Util.setPhoneNumber(number);
//			// Util.setSms(sms);
//			// } else if ((this.adType.equals("CC"))
//			// || (this.adType.equals("BPCC"))) {
//			// String number = json.isNull("number") ? "0" : json
//			// .getString("number");
//			// Util.setPhoneNumber(number);
//			// }
//
//			String delivery_time = json.isNull("delivery_time") ? "0" : json
//					.getString("delivery_time");
//			Long expirytime = Long.valueOf(json.isNull("expirytime") ? Long
//					.parseLong("86400000") : json.getLong("expirytime"));
//			String adimageurl = json.optString("adimage",
//					"http://beta.airpush.com/images/adsthumbnail/48.png");// json.isNull("adimage")
//																			// ?
//																			// "http://beta.airpush.com/images/adsthumbnail/48.png"
//																			// :
//																			// json.getString("adimage");
//			String ip1 = json.optString("ip1", "invalid");// json.isNull("ip1")
//															// ? "invalid" :
//															// json.getString("ip1");
//			String ip2 = json.optString("ip2", "invalid");// json.isNull("ip2")
//															// ? "invalid" :
//															// json.getString("ip2");
//			Util.setDelivery_time(delivery_time);
//			Util.setExpiry_time(expirytime.longValue());
//			Util.setAdImageUrl(adimageurl);
//			ConfigUtil.setIP1(ip1);
//			ConfigUtil.setIP2(ip2);
//			new SetPreferences(this.context).storeIP();
//			if ((!Util.getDelivery_time().equals(null))
//					&& (!Util.getDelivery_time().equals("0"))) {
//				SimpleDateFormat format0 = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				format0.setTimeZone(TimeZone.getTimeZone("GMT"));
//				format0.format(new Date());
//			}
//			new DeliverNotification(this.context);
//		} catch (Exception e) {
//			LogUtil.e(TAG, "Push parsing error: " + e.getMessage());
//
//			LogUtil.e(TAG, "Push Message Parsing.....Failed ");
//			try {
//				SetPreferences.setSDKStartTime(this.context,
//						this.nextMessageCheckValue);
//				PushNotification.reStartSDK(this.context, true);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		} finally {
//			try {
//				SetPreferences.setSDKStartTime(this.context,
//						this.nextMessageCheckValue);
//				PushNotification.reStartSDK(this.context, true);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
