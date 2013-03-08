package com.kk.msg;

import java.io.UnsupportedEncodingException;

/**
 * PDU编码实现，7bit,8bit,以及UCS2编码;代码主体是网上来源（Url我忘记了，很遗憾） 自己完善了一点，测试后暂时没错。
 * 
 */
public class PduPack {
	// 短消息中心号长度
	private String smscLen;

	private String smscFormat;
	// 短消息中心号
	private String smsc;
	// 目的手机地址长度
	private int addrLen;
	private String addrFormat;
	// 目的手机地址
	private String addr;
	// 短消息内容编码方式,tp_dcs
	private String msgCoding;
	private int msgLen;
	// 短消息内容,tp_ud
	private String msgContent;

	public PduPack() {
		smscLen = "08";
		smscFormat = "91";
		addrLen = 13;
		addrFormat = "91";
	}

	public PduPack(String src) {
		if (src != null && src.length() > 44) {
			String temp = src.substring(4, 18);
			smsc = PduPack.interChange(temp);

			if (smsc != null && smsc.length() > 1) {
				smsc = smsc.substring(0, smsc.length() - 1);
				if (smsc.length() == 13)
					smsc = smsc.substring(2);
			}
			temp = src.substring(20, 22);
			addrLen = Integer.parseInt(temp, 16);

			if (addrLen % 2 == 0) {
				temp = src.substring(24, 24 + addrLen);
			} else {
				temp = src.substring(24, 24 + addrLen + 1);
			}

			addr = PduPack.interChange(temp);

			// 去掉为补齐为偶数加上的那一位
			if (addr != null && addr.length() % 2 == 0) {
				addr = addr.substring(0, addr.length() - 1);

				if (addr.length() == 13) {// 如果前面有86，去掉它
					addr = addr.substring(2);
				}
			}

			if (addrLen % 2 == 0) {
				msgCoding = src.substring(24 + addrLen + 2, 24 + addrLen + 4);
				temp = src.substring(24 + addrLen + 4 + 16);
			} else {
				msgCoding = src.substring(24 + addrLen + 3, 24 + addrLen + 5);
				temp = src.substring(24 + addrLen + 5 + 16);
			}

			if (msgCoding.equals("08")) {
				msgContent = PduPack.unicode2gb(temp);
			} else {
				msgContent = PduPack.decode7bit(temp);
			}
		}
	}

	/**
	 * 设置短信中心号码
	 * 
	 * @param s
	 */
	public void setSmsc(String s) {
		if (s != null) {
			String centerNo = null;

			if (s.length() == 11 && s.substring(0, 2).equals("13")) {
				centerNo = "86" + s;
			} else if (s.length() == 13 && s.substring(0, 4).equals("8613")) {
				centerNo = s;
			} else if (s.length() == 14 && s.substring(0, 5).equals("+8613")) {
				centerNo = s.substring(1);
			} else {
				return;
			}
			this.smsc = PduPack.interChange(centerNo);
		}
	}

	/**
	 * 设置目的地址
	 * 
	 * @param ad
	 */
	public void setAddr(String ad) {
		if (ad != null) {
			String centerNo = null;

			if (ad.length() == 11 && ad.substring(0, 2).equals("13")) {
				centerNo = "86" + ad;
			} else if (ad.length() == 13 && ad.substring(0, 4).equals("8613")) {
				centerNo = ad;
			} else if (ad.length() == 14 && ad.substring(0, 5).equals("+8613")) {
				centerNo = ad.substring(1);
			} else if (ad.length() > 0) { // 特服号
				addrFormat = "A1";
				addrLen = ad.length();
				centerNo = ad;
			} else {
				return;
			}
			addr = PduPack.interChange(centerNo);
		}
	}

	/**
	 * 设置编码方式
	 * 
	 * @param encoding
	 *            0:表示7-BIT编码 4:表示8-BIT编码 8:表示UCS2编码
	 */
	public void setMsgCoding(int encoding) {
		if (encoding == 8) {
			msgCoding = "08";
		} else if (encoding == 4) {
			msgCoding = "04";
		} else {
			msgCoding = "00";
		}
	}

	/**
	 * 短消息内容
	 * 
	 * @param content
	 */
	public void setMsgContent(String content) {
		if (content != null) {
			if (content.length() == content.getBytes().length) {
				msgCoding = "00";
				msgLen = content.getBytes().length;
				msgContent = encode7bit(content);
			} else {
				msgCoding = "08";
				msgContent = PduPack.Chinese2unicode(content);
				if (msgContent != null)
					msgLen = msgContent.length() / 2;
			}
			if (msgContent != null) {
				msgContent = msgContent.toUpperCase();
			}
		}
	}

	/**
	 * 
	 * @return 经过PDU编码的结果,十六进制字符串形式
	 */
	public String getCodedResultStr() {
		String result = null;
		final String tp_mti = "11";
		final String tp_mr = "00";
		final String tp_pid = "00";
		final String tp_vp = "00";
		if (smsc != null && addr != null && msgContent != null) {
			result = smscLen + smscFormat + smsc + tp_mti + tp_mr
					+ PduPack.byte2hex((byte) addrLen) + addrFormat + addr
					+ tp_pid + msgCoding + tp_vp
					+ PduPack.byte2hex((byte) msgLen) + msgContent;
			result = result.toUpperCase();
			System.out.println("处理前的："+result);
//			result = "0891683108707515F0240D91688186603896F1000021113171947323027831";
		}
		return result;
	}

	/**
	 * 
	 * @return 经过PDU编码的结果,byte数组格式
	 */
	public byte[] getCodedResultByte() {
		return getCodedResultStr().getBytes();
	}

	public String getAddr() {
		return addr;
	}

	public String getMsgCoding() {
		return msgCoding;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public String getSmsc() {
		return smsc;
	}

	/**
	 * 7-BIT编码 把ASCII码值最高位为0的字符串进行压缩转换成8位二进制表示的字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String encode7bit(String src) {
		String result = null;
		String hex = null;
		byte value;
		if (src != null && src.length() == src.getBytes().length) {
			result = "";
			byte left = 0;
			byte[] b = src.getBytes();
			for (int i = 0, j = 0; i < b.length; i++) {
				j = i & 7;
				if (j == 0)
					left = b[i];
				else {
					value = (byte) ((b[i] << (8 - j)) | left);
					left = (byte) (b[i] >> j);
					hex = PduPack.byte2hex((byte) value);
					result += hex;
					if (i == b.length - 1)
						result += PduPack.byte2hex(left);
				}
			}
			result = result.toUpperCase();
		}
		return result;
	}

	/**
	 * 对7-BIT编码进行解码
	 * 
	 * @param src
	 *            十六进制的字符串，且为偶数个
	 * @return 源字符串
	 */
	public static String decode7bit(String src) {
		String result = null;
		int[] b;
		String temp = null;
		byte srcAscii;
		byte left = 0;
		if (src != null && src.length() % 2 == 0) {
			result = "";
			b = new int[src.length() / 2];
			temp = src + "0";

			for (int i = 0, j = 0, k = 0; i < temp.length() - 2; i += 2, j++) {
				b[j] = Integer.parseInt(temp.substring(i, i + 2), 16);
				k = j % 7;
				srcAscii = (byte) (((b[j] << k) & 0x7F) | left);
				result += (char) srcAscii;
				left = (byte) (b[j] >>> (7 - k));

				if (k == 6) {
					result += (char) left;
					left = 0;
				}

				if (j == src.length() / 2) {
					result += (char) left;
				}
			}
		}
		return result;
	}

	/**
	 * 把UNICODE编码的字符串转化成汉字编码的字符串
	 * 
	 * @param hexString
	 * @return
	 */
	public static String unicode2gb(String hexString) {
		StringBuffer sb = new StringBuffer();
		if (hexString == null)
			return null;
		for (int i = 0; i + 4 <= hexString.length(); i = i + 4) {
			try {
				int j = Integer.parseInt(hexString.substring(i, i + 4), 16);
				sb.append((char) j);
			} catch (NumberFormatException e) {
				return hexString;
			}
		}
		return sb.toString();
	}

	/**
	 * 把汉字转化成UNICODE编码的字符串
	 * 
	 * @param s
	 * @return
	 */
	public static String Chinese2unicode(String s) {
		String s1 = new String();
		String s2 = new String();
		byte abyte0[] = null;
		try {
			abyte0 = s.getBytes("Unicode");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		for (int j = 2; j < abyte0.length; j += 2) {
			String s3 = Integer.toHexString(abyte0[j + 1]);
			int i = s3.length();

			if (i < 2) {
				s1 = s1 + "0" + s3;
			} else {
				s1 = s1 + s3.substring(i - 2);
			}

			s3 = Integer.toHexString(abyte0[j]);
			i = s3.length();

			if (i < 2) {
				s1 = s1 + "0" + s3;
			} else {
				s1 = s1 + s3.substring(i - 2);
			}
		}
		return s1;
	}

	/**
	 * byte数据转化成为十六进制ASCII字符
	 * 
	 * @param ib
	 * @return
	 */
	public static String byte2hex(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);

		return s;
	}

	/**
	 * 把相临的两个字符对换，字符串长度为奇数时最后加“F”
	 * 
	 * @param src
	 * @return
	 */
	public static String interChange(String src) {
		String result = null;
		if (src != null) {
			if (src.length() % 2 != 0) {
				src += "F";
			}

			src += "0";
			result = "";

			for (int i = 0; i < src.length() - 2; i += 2) {
				result += src.substring(i + 1, i + 2);
				result += src.substring(i, i + 1);
			}
		}
		return result;
	}
}
