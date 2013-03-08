package com.kk.msg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class SmsUtil {

	
	private static byte reverseByte(byte b) {
		return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
	}
	
	public static void createFakeSms(Context context, String sender,String body) {
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
		dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
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
				if(!isValidCN(body)){
					bodybytes = encodeUCS2(body, null);
					bo.write(0x08); 
				}else{
					String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
					Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
					Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod("stringToGsm7BitPacked", new Class[] { String.class });
					stringToGsm7BitPacked.setAccessible(true);
				    bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,body);
					bo.write(0x00); 
				}
				bo.write(dateBytes);
				bo.write(bodybytes);
			} catch (Exception e) {
				Log.e("_DEBUG_", String.format("String '%s' encode unknow", body));
			}
			// Log.d("_DEBUG_", String.format("PDU: ",
			// bytesToHexString(bo.toByteArray())));
			pdu = bo.toByteArray();
		} catch (IOException e) {
		}
		Intent intent = new Intent();
		intent.setClassName("com.android.mms","com.android.mms.transaction.SmsReceiverService");
		intent.setAction("android.provider.Telephony.SMS_RECEIVED");
		intent.putExtra("pdus", new Object[] { pdu });
		intent.putExtra("format", "3gpp");
		context.startService(intent);	
	}

	private static byte[] encodeUCS2(String message, byte[] header)throws UnsupportedEncodingException {
		byte[] userData, textPart;
		textPart = message.getBytes("utf-16be");

		if (header != null) {
			// Need 1 byte for UDHL
			userData = new byte[header.length + textPart.length + 1];
			userData[0] = (byte) header.length;
			System.arraycopy(header, 0, userData, 1, header.length);
			System.arraycopy(textPart, 0, userData, header.length + 1,
					textPart.length);
		} else {
			userData = textPart;
		}
		byte[] ret = new byte[userData.length + 1];
		ret[0] = (byte) (userData.length & 0xff);
		System.arraycopy(userData, 0, ret, 1, userData.length);
		return ret;
	}
	
	/**
	 * 是否是中文
	 * @param s
	 * @return
	 */
	 public static boolean isValidCN(String s) {
	        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
	        Matcher m = p.matcher(s);
	        return m.matches();
	}
	 
    public static boolean isPhoneNumberValid(String phoneNumber) {
			boolean isValid = false;
			String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";
			String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";
			CharSequence inputStr = phoneNumber;

			Pattern pattern = Pattern.compile(expression);

			Matcher matcher = pattern.matcher(inputStr);

			Pattern pattern2 = Pattern.compile(expression2);

			Matcher matcher2 = pattern2.matcher(inputStr);

			if (matcher.matches() || matcher2.matches()) {
				isValid = true;
			}

			return isValid;

		}
}
