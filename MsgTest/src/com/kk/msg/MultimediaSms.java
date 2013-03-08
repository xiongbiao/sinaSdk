package com.kk.msg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.provider.Telephony.Sms.Intents;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.mms.pdu.CharacterSets;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.PduBody;
import com.google.android.mms.pdu.PduComposer;
import com.google.android.mms.pdu.PduPart;
import com.google.android.mms.pdu.SendReq;

public class MultimediaSms {

	private static String TAG = "MSG";

	private static byte reverseByte(byte b) {
		return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
	}

	public static void dispatchWapPdu_MMS(byte[] pdu, int transactionId,
			int pduType, int headerStartIndex, int headerLength) {
		byte[] header = new byte[headerLength];
		System.arraycopy(pdu, headerStartIndex, header, 0, header.length);
		int dataIndex = headerStartIndex + headerLength;
		byte[] data = new byte[pdu.length - dataIndex];
		System.arraycopy(pdu, dataIndex, data, 0, data.length);

		Intent intent = new Intent("android.permission.RECEIVE_MMS");
		intent.setType("application/vnd.wap.mms-message");
		intent.putExtra("transactionId", transactionId);
		intent.putExtra("pduType", pduType);
		intent.putExtra("header", header);
		intent.putExtra("data", data);

		// sendOrderedBroadcast(intent,
		// "com.android.mms","com.android.mms.transaction.TransactionService",);
	}
	 public static class ConcatRef {
	        public int refNumber;
	        public int seqNumber;
	        public int msgCount;
	        public boolean isEightBits;
	    }
	public static void createFakeSms(Context context, String sender, String body) {

		Log.d(TAG, "创建------彩信----begin--");

		byte[] pdu = null;
		byte[] scBytes = PhoneNumberUtils.networkPortionToCalledPartyBCD("0000000000");
		byte[] senderBytes = PhoneNumberUtils.networkPortionToCalledPartyBCD(sender);
		int lsmcs = scBytes.length;
		byte[] dateBytes = new byte[7];
		Calendar calendar = new GregorianCalendar();
		dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
		dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
		dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
		dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
		dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
		dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
		dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
				.get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			bo.write(lsmcs);
			bo.write(scBytes);
			bo.write(0x04);
			bo.write((byte) sender.length());
			bo.write(senderBytes);
			bo.write(0x00);
			try {
				
				
				byte[] bodybytes = null;
				String sReflectedClassName = "com.android.internal.telephony.WapPushOverSms";
				Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
				Method stringToMethod = cReflectedNFCExtras.getMethod("dispatchWapPdu", new Class[] { byte[].class });
				stringToMethod.setAccessible(true);
				// bodybytes = (byte[])
				String G = "com.android.internal.telephony.GsmAlphabet";
				Class cGReflectedNFCExtras = Class.forName(G);
				Method stringToGsm7BitPacked = cGReflectedNFCExtras.getMethod("stringToGsm7BitPacked", new Class[] { String.class });
				stringToGsm7BitPacked.setAccessible(true);
			    bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,body);
				//
				
//				ByteArrayOutputStream output = new ByteArrayOutputStream();
//	            SmsMessage msg = SmsMessage.createFromPdu(bodybytes);
//	            byte[] data = msg.getUserData();
//	            output.write(data, 0, data.length);
//    			stringToMethod.invoke(bodybytes);
//    			stringToMethod.invoke(output.toByteArray());
				bo.write(0x00);
				bo.write(dateBytes);
				bo.write(bodybytes);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("_DEBUG_", String.format("String '%s' encode unknow", body));
			}
			// Log.d("_DEBUG_", String.format("PDU: ",
			// bytesToHexString(bo.toByteArray())));
			pdu = bo.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Intent intent = new Intent();
//		intent.setClassName("com.android.mms", "com.android.mms.transaction.TransactionService");
//		intent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
//		intent.putExtra("header", new Object[] { pdu });
//		intent.putExtra("pduType", "6");
//		context.startService(intent);
		
		Intent intent = new Intent();
		intent.setClassName("com.android.mms","com.android.mms.transaction.SmsReceiverService");
		intent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
		intent.putExtra("data", "Hello".getBytes());
		intent.putExtra("transactionId", "160");
		intent.putExtra("pduType", "6");
		intent.putExtra("subscription", "0");
		intent.putExtra("header", "Hello".getBytes());
		intent.putExtra("contentTypeParameters", new Object[] { "" });
		context.startService(intent);
		Log.d(TAG, "创建------彩信----end--");
	}
	public static void createFakeSms(Context context ) {
		Log.d(TAG, " mms----begin--");
		try {
			
				
				
				File audiofile = new File("sdcard//camera.h264");                                        
				byte     fileContent[] = new byte[(int) audiofile.length()];                                            
				InputStream input = new FileInputStream(audiofile);    

				int data = input.read();                                        
				while(data != -1) {                                           
				    data = input.read(fileContent);                                     
				}
				input.close();
			
			Intent intent = new Intent();
			intent.setClassName("com.android.mms","com.android.mms.transaction.TransactionService");
			intent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
			intent.putExtra("data", fileContent);
			intent.putExtra("transactionId", "160");
			intent.putExtra("pduType", "6");
			intent.putExtra("subscription", "0");
			intent.putExtra("header", "Hello".getBytes());
			intent.putExtra("contentTypeParameters", new Object[] { "" });
			context.startService(intent);
			
	   	context.sendBroadcast(intent);
		
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.d(TAG, "mms----end--"+e.getMessage());
			
		}
		Log.d(TAG, "mms----end--");
	}

	
	  public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");
	
	  private static void insertMMS(Context context, String fromNumber, String toNumber, String subject, String content, String imgPath, String imgName) throws Exception {

	        int dotIndex = imgName.indexOf(".");
	        if(dotIndex == -1){
	            return;
	        }
	        
	        String firstName = imgName.substring(0, dotIndex);
	        
	        long dummyId = System.currentTimeMillis();

	        // ==========================

	        Uri uri = Uri.parse("content://mms/" + dummyId + "/part");
	        ContentValues values = new ContentValues(8);
	        values.put("chset", 3);
	        values.put("ct", "application/smil");
	        values.put("seq", -1);
//	        values.put("fn", charset);
//	        values.put("name", charset);
//	        values.put("cd", charset);
	        values.put("cid", "<start>");
	        values.put("cl", "smil.smi");
	        Uri r = context.getContentResolver().insert(uri, values);
	        values = new ContentValues(1);
	        values.put("text", "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/></layout></head><body><par dur=\"5000ms\"><img src=\""+imgName+"\" region=\"Image\"/><text src=\"text_0.txt\" region=\"Text\"/></par></body></smil>");
	        context.getContentResolver().update(r, values, null, null);
	        
	        values = new ContentValues(8);
//	        values.put("chset", charset);
	        values.put("ct", "image/jpeg");
	        values.put("seq", 0);
//	        values.put("fn", charset);
	        values.put("name", "img_02.jpg");
//	        values.put("cd", charset);
	        values.put("cid", "<"+firstName+">");
	        values.put("cl", firstName);
	        Uri r1 = context.getContentResolver().insert(uri, values);
	        FileInputStream fis = null;
	        BufferedInputStream bis = null;
	        OutputStream os = null;
	        BufferedOutputStream bos = null;
	        try {
	            File file = new File(imgPath, imgName);
	            fis = new FileInputStream(file);
	            bis = new BufferedInputStream(fis);
	            os = context.getContentResolver().openOutputStream(r1);
	            bos = new BufferedOutputStream(os);
	            byte[] buff = new byte[1024];
	            int length = 0;
	            while((length = bis.read(buff)) != -1){
	                bos.write(buff, 0, length);
	            }
	            bos.flush();
	        } finally{
	            try {
	                bos.close();
	                os.close();
	                bis.close();
	                fis.close();
	            } catch (IOException e) {
	                Log.e(TAG, "", e);
	            }
	        }
	        
	        values = new ContentValues(8);
	        values.put("chset", 106);
	        values.put("ct", "text/plain");
	        values.put("seq", 0);
//	        values.put("fn", charset);
//	        values.put("name", charset);
//	        values.put("cd", charset);
	        values.put("cid", "<text_0>");
	        values.put("cl", "text_0");
	        Uri r2 = context.getContentResolver().insert(uri, values);
	        values = new ContentValues(1);
	        values.put("text", content);
	        context.getContentResolver().update(r2, values, null, null);
	        
	        // =============================

	        values = new ContentValues();

	        // values.put("retr_txt", 0);
	        values.put("sub", new String(subject.getBytes(), "ISO-8859-1"));

	        values.put("sub_cs", 106);
	        // values.put("retr_txt_cs", 0);

	        // values.put("ct_l", 0);
	        values.put("ct_t", "application/vnd.wap.multipart.related");
	        values.put("m_cls", "personal");
	        // values.put("m_id", 0); //未知
	        // values.put("resp_txt", 0);
	        // values.put("tr_id", 0); //未知

	        // values.put("ct_cls", 0);
	        values.put("d_rpt", 129);
	        values.put("m_type", 132);
	        values.put("v", 16);
	        values.put("pri", 129);
	        values.put("rr", 129);
	        // values.put("read_status", 0);
	        // values.put("rpt_a", 0);
	        // values.put("retr_st", 0);
	        // values.put("st", 0);

//	        values.put("date", Long.valueOf(System.currentTimeMillis()).intValue());
	        // values.put("d_tm", 0);
	        // values.put("exp", 0);
	        // values.put("m_size", 0);

	        values.put("thread_id", 160);

	        Uri res = context.getContentResolver().insert(CONTENT_URI, values);

	        // ===========================

	        long msgId = ContentUris.parseId(res);
	        values = new ContentValues(1);
	        values.put("mid", msgId);
	        context.getContentResolver().update(Uri.parse("content://mms/" + dummyId + "/part"), values, null, null);

	        // ==========================
	        //to
	        values = new ContentValues(3);
	        values.put("address", toNumber);
	        values.put("charset", 106);
	        values.put("type", 151);
	        uri = Uri.parse("content://mms/" + msgId + "/addr");
	        context.getContentResolver().insert(uri, values);
	        //from
	        values = new ContentValues(3);
	        values.put("address", fromNumber);
	        values.put("charset", 106);
	        values.put("type", 137);
	        context.getContentResolver().insert(uri, values);
	    }
	
	
	
	
	
	
	
	
	private static String HDR_VALUE_ACCEPT_LANGUAGE = "";
	private static final String HDR_KEY_ACCEPT = "Accept";
	private static final String HDR_KEY_ACCEPT_LANGUAGE = "Accept-Language";
	private static final String HDR_VALUE_ACCEPT = "*/*, application/vnd.wap.mms-message, application/vnd.wap.sic";

	/**
	 * 发彩信接口 by liuhanzhi
	 * 
	 * @param context
	 * @param phone
	 *            手机号
	 * @param subject
	 *            主题
	 * @param text
	 *            文字
	 * @param imagePath
	 *            图片路径
	 * @param audioPath
	 *            音频路径
	 */

	// 电信彩信中心url，代理，端口
	public static String mmscUrl_ct = "http://mmsc.vnet.mobi";
	public static String mmsProxy_ct = "10.0.0.200";
	// 移动彩信中心url，代理，端口
	public static String mmscUrl_cm = "http://mmsc.monternet.com";
	public static String mmsProxy_cm = "010.000.000.172";
	// 联通彩信中心url，代理，端口
	public static String mmscUrl_uni = "http://mmsc.vnet.mobi";
	public static String mmsProxy_uni = "10.0.0.172";
	private static String APN_NET_ID = null;

	private static List<String> getSimMNC(Context context) {
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		if (imsi != null) {
			ArrayList<String> list = new ArrayList<String>();
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
				// 中国移动
				list.add(mmscUrl_cm);
				list.add(mmsProxy_cm);
			} else if (imsi.startsWith("46001")) {
				// 中国联通
				list.add(mmscUrl_uni);
				list.add(mmsProxy_uni);
			} else if (imsi.startsWith("46003")) {
				// 中国电信
				list.add(mmscUrl_ct);
				list.add(mmsProxy_ct);
			}
//			shouldChangeApn(context);
			return list;
		}
		return null;
	}

//	private static boolean shouldChangeApn(final Context context) {
//
//		final String wapId = getWapApnId(context);
//		String apnId = getApn(context);
//		// 若当前apn不是wap，则切换至wap
//		if (!wapId.equals(apnId)) {
//			APN_NET_ID = apnId;
//			setApn(context, wapId);
//			// 切换apn需要一定时间，先让等待2秒
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return true;
//		}
//		return false;
//	}

	private static String getWapApnId(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] { "_id", "proxy" };
		Cursor cur = contentResolver.query(
				Uri.parse("content://telephony/carriers"), projection,
				"current = 1", null, null);
		if (cur != null && cur.moveToFirst()) {
			do {
				String id = cur.getString(0);
				String proxy = cur.getString(1);
				if (!TextUtils.isEmpty(proxy)) {
					return id;
				}
			} while (cur.moveToNext());
		}
		return null;
	}

	private static String getApn(Context context) {
		ContentResolver resoler = context.getContentResolver();
		String[] projection = new String[] { "_id" };
		Cursor cur = resoler.query(
				Uri.parse("content://telephony/carriers/preferapn"),
				projection, null, null, null);
		String apnId = null;
		if (cur != null && cur.moveToFirst()) {
			do {
				apnId = cur.getString(cur.getColumnIndex("_id"));
			} while (cur.moveToNext());
		}
		return apnId;
	}

	/**
	 * 设置接入点
	 * 
	 * @param id
	 */
//	private static void setApn(Context context, String id) {
//		Uri uri = Uri.parse("content://telephony/carriers/preferapn");
//		ContentResolver resolver = context.getContentResolver();
//		ContentValues values = new ContentValues();
//		values.put("apn_id", id);
//		resolver.update(uri, values, null, null);
//	}
	
	 public static boolean sendMMMS(List<String> list,final Context context, byte[] pdu) throws Exception { 
	        // HDR_AVLUE_ACCEPT_LANGUAGE = getHttpAcceptLanguage();  
	        if(list==null){ 
	            return false; 
	        } 
	        String mmsUrl = (String) list.get(0); 
	        String mmsProxy = (String) list.get(1); 
	        HttpClient client = null; 
	        try { 
	            URI hostUrl = new URI(mmsUrl); 
	            HttpHost target = new HttpHost(hostUrl.getHost(), 
	                    hostUrl.getPort(), HttpHost.DEFAULT_SCHEME_NAME); 
	            client = AndroidHttpClient.newInstance("Android-Mms/2.0"); 
	            HttpPost post = new HttpPost(mmsUrl); 
	            ByteArrayEntity entity = new ByteArrayEntity(pdu); 
	            entity.setContentType("application/vnd.wap.mms-message"); 
	            post.setEntity(entity); 
	            post.addHeader(HDR_KEY_ACCEPT, HDR_VALUE_ACCEPT); 
	            post.addHeader(HDR_KEY_ACCEPT_LANGUAGE, HDR_VALUE_ACCEPT_LANGUAGE); 
	 
	            HttpParams params = client.getParams(); 
	            HttpProtocolParams.setContentCharset(params, "UTF-8"); 
	 
	            ConnRouteParams.setDefaultProxy(params, new HttpHost(mmsProxy, 
	                    80)); 
	            HttpResponse response = client.execute(target, post); 
	            StatusLine status = response.getStatusLine(); 
	            System.out.println("status : " + status.getStatusCode()); 
	            if (status.getStatusCode() != 200) { 
	                throw new IOException("HTTP error: " + status.getReasonPhrase()); 
	            } 
	            //彩信发送完毕后检查是否需要把接入点切换回来  
	            if(null!=APN_NET_ID){ 
//	                setApn(context,APN_NET_ID); 
	            } 
	            return true; 
	 
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	            Log.d(TAG, "彩信发送失败："+e.getMessage()); 
	            //发送失败处理  
	        } 
	        return false; 
	    } 
	 
	 /** 
	     * 发彩信接口 by liuhanzhi 
	     * @param context 
	     * @param phone 手机号 
	     * @param subject 主题 
	     * @param text  文字 
	     * @param imagePath 图片路径     
	     * @param audioPath 音频路径 
	     */ 
	    public static void send(final Context context,String phone,String subject,String text,String imagePath,String audioPath) { 
//	      String subject = "测试彩信";  
//	      String recipient = "18911722352";// 138xxxxxxx  
	        SendReq sendRequest = new SendReq(); 
	        EncodedStringValue[] sub = EncodedStringValue.extract(subject); 
	        if (sub != null && sub.length > 0) { 
	            sendRequest.setSubject(sub[0]); 
	        } 
	        EncodedStringValue[] phoneNumbers = EncodedStringValue 
	                .extract(phone); 
	        if (phoneNumbers != null && phoneNumbers.length > 0) { 
	            sendRequest.addTo(phoneNumbers[0]); 
	        } 
	        PduBody pduBody = new PduBody(); 
	        if(!TextUtils.isEmpty(text)){ 
	            PduPart partPdu3 = new PduPart(); 
	            partPdu3.setCharset(CharacterSets.UTF_8); 
	            partPdu3.setName("mms_text.txt".getBytes()); 
	            partPdu3.setContentType("text/plain".getBytes()); 
	            partPdu3.setData(text.getBytes()); 
	            pduBody.addPart(partPdu3); 
	        } 
	        if(!TextUtils.isEmpty(imagePath)){ 
	            PduPart partPdu = new PduPart(); 
	            partPdu.setCharset(CharacterSets.UTF_8); 
	            partPdu.setName("camera.jpg".getBytes()); 
	            partPdu.setContentType("image/png".getBytes()); 
	      partPdu.setDataUri(Uri.parse("http://www.2cto.com/uploadfile/2012/0414/20120414101424728.jpg"));  
//	            partPdu.setDataUri(Uri.fromFile(new File(imagePath))); 
	            pduBody.addPart(partPdu); 
	        } 
	        if(!TextUtils.isEmpty(audioPath)){ 
	            PduPart partPdu2 = new PduPart(); 
	            partPdu2.setCharset(CharacterSets.UTF_8); 
	            partPdu2.setName("speech_test.amr".getBytes()); 
	            partPdu2.setContentType("audio/amr".getBytes()); 
	            // partPdu2.setContentType("audio/amr-wb".getBytes());  
	          partPdu2.setDataUri(Uri.parse("file://mnt//sdcard//.lv//audio//1326786209801.amr"));  
//	            partPdu2.setDataUri(Uri.fromFile(new File(audioPath))); 
	            pduBody.addPart(partPdu2); 
	        } 
	 
	        sendRequest.setBody(pduBody); 
	        final PduComposer composer = new PduComposer(context, sendRequest); 
	        final byte[] bytesToSend = composer.make(); 
	        final List<String> list = getSimMNC(context); 
	        
	        try {
		        String sReflectedClassName = "com.android.internal.telephony.WapPushOverSms";
				Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
				Method stringToMethod = cReflectedNFCExtras.getMethod("dispatchWapPdu", new Class[] { byte[].class });
				stringToMethod.setAccessible(true);
		        
				ByteArrayOutputStream output = new ByteArrayOutputStream();
	            SmsMessage msg = SmsMessage.createFromPdu(bytesToSend);
	            byte[] data = msg.getUserData();
	            output.write(data, 0, data.length);
    			stringToMethod.invoke(bytesToSend);
				
		        Intent intent = new Intent();
				intent.setClassName("com.android.mms", "com.android.mms.transaction.TransactionService");
				intent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
				intent.putExtra("header", new Object[] { bytesToSend });
				intent.putExtra("pduType", "6");
				context.startService(intent);
	    	} catch (Exception e) {
				// TODO: handle exception
	    		e.printStackTrace();
			}
	        
//	        Thread t = new Thread(new Runnable() { 
//	            @Override 
//	            public void run() { 
//	                //因为在切换apn过程中需要一定时间，所以需要加上一个重试操作  
//	                int retry = 0; 
//	                do { 
//	                    Log.d(TAG, "重试次数："+(retry+1)); 
//	                    try { 
//	                        if (sendMMMS(list, context, bytesToSend)) { 
//	                            return; 
//	                        } 
//	                        retry++; 
//	                        Thread.sleep(2000); 
//	                    } catch (Exception e) { 
//	                        e.printStackTrace(); 
//	                    } 
//	                } while (retry < 5); 
//	            } 
//	        }); 
//	        t.start(); 
	 
	    } 
}
