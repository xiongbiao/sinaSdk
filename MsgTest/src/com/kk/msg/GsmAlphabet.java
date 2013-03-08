package com.kk.msg;

import android.util.Log;
import android.util.SparseIntArray;

public class GsmAlphabet {

	public GsmAlphabet() {
	}

	public static int charToGsm(char c) {
		try {

			return charToGsm(c, false);
		} catch (Exception e) {
			// TODO: handle exception
			return sGsmSpaceChar;
		}
	}

	public static int charToGsm(char c, boolean throwException)
			throws Exception {
		int ret = charToGsm.get(c, -1);
		if (ret == -1) {
			ret = charToGsmExtended.get(c, -1);
			if (ret == -1) {
				if (throwException)
					throw new Exception(c + "");
				else
					return sGsmSpaceChar;
			} else {
				return 27;
			}
		} else {
			return ret;
		}
	}

	public static int charToGsmExtended(char c) {
		int ret = charToGsmExtended.get(c, -1);
		if (ret == -1)
			return sGsmSpaceChar;
		else
			return ret;
	}

	public static char gsmToChar(int gsmChar) {
		return (char) gsmToChar.get(gsmChar, 32);
	}

	public static char gsmExtendedToChar(int gsmChar) {
		int ret = gsmExtendedToChar.get(gsmChar, -1);
		if (ret == -1)
			return ' ';
		else
			return (char) ret;
	}

	public static byte[] stringToGsm7BitPacked(String data) throws Exception {
		return stringToGsm7BitPacked(data, 0, -1, 0, false);
	}

	public static byte[] stringToGsm7BitPacked(String data, int dataOffset,
			int maxSeptets, int startingBitOffset, boolean throwException)
			throws Exception {
		int sz = data.length();
		int septetCount;
		if (maxSeptets == -1)
			septetCount = countGsmSeptets(data);
		else
			septetCount = maxSeptets;
		if (septetCount > 255)
			throw new Exception("Payload cannot exceed 32767 septets");
		byte ret[] = new byte[1 + (septetCount * 7 + 7) / 8];
		int bitOffset = startingBitOffset;
		int septets = 0;
		for (int i = dataOffset; i < sz && septets < septetCount;) {
			char c = data.charAt(i);
			int v = charToGsm(c, throwException);
			if (v == 27) {
				v = charToGsmExtended(c);
				packSmsChar(ret, bitOffset, 27);
				bitOffset += 7;
				septets++;
			}
			packSmsChar(ret, bitOffset, v);
			septets++;
			i++;
			bitOffset += 7;
		}
		ret[0] = (byte) septets;
		return ret;
	}

	private static void packSmsChar(byte packedChars[], int bitOffset, int value) {
		int byteOffset = bitOffset / 8;
		int shift = bitOffset % 8;
		packedChars[++byteOffset] |= value << shift;
		if (shift > 1)
			packedChars[++byteOffset] = (byte) (value >> 8 - shift);
	}

	public static String gsm7BitPackedToString(byte pdu[], int offset,
			int lengthSeptets) {
		return gsm7BitPackedToString(pdu, offset, lengthSeptets, 0);
	}

	public static String gsm7BitPackedToString(byte pdu[], int offset,
			int lengthSeptets, int numPaddingBits) {
		StringBuilder ret = new StringBuilder(lengthSeptets);
		try {
			boolean prevCharWasEscape = false;
			for (int i = 0; i < lengthSeptets; i++) {
				int bitOffset = 7 * i + numPaddingBits;
				int byteOffset = bitOffset / 8;
				int shift = bitOffset % 8;
				int gsmVal = 0x7f & pdu[offset + byteOffset] >> shift;
				if (shift > 1) {
					gsmVal &= 127 >> shift - 1;
					gsmVal |= 0x7f & pdu[offset + byteOffset + 1] << 8 - shift;
				}
				if (prevCharWasEscape) {
					ret.append(gsmExtendedToChar(gsmVal));
					prevCharWasEscape = false;
				} else if (gsmVal == 27)
					prevCharWasEscape = true;
				else
					ret.append(gsmToChar(gsmVal));
			}
		} catch (Throwable ex) {
			Log.e("GSM", "Error GSM 7 bit packed: ", ex);
			return null;
		}
		return ret.toString();
	}

	public static String gsm8BitUnpackedToString(byte data[], int offset,
			int length) {
		StringBuilder ret = new StringBuilder(length);
		boolean prevWasEscape = false;
		for (int i = offset; i < offset + length; i++) {
			int c = data[i] & 0xff;
			if (c == 255)
				break;
			if (c == 27) {
				if (prevWasEscape) {
					ret.append(' ');
					prevWasEscape = false;
				} else {
					prevWasEscape = true;
				}
				continue;
			}
			if (prevWasEscape)
				ret.append((char) gsmExtendedToChar.get(c, 32));
			else
				ret.append((char) gsmToChar.get(c, 32));
			prevWasEscape = false;
		}
		return ret.toString();
	}

	public static byte[] stringToGsm8BitPacked(String s) {
		int septets = 0;
		septets = countGsmSeptets(s);
		byte ret[] = new byte[septets];
		stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
		return ret;
	}

	public static void stringToGsm8BitUnpackedField(String s, byte dest[],
			int offset, int length) {
		int outByteIndex = offset;
		int i = 0;
		for (int sz = s.length(); i < sz && outByteIndex - offset < length; i++) {
			char c = s.charAt(i);
			int v = charToGsm(c);
			if (v == 27) {
				if ((outByteIndex + 1) - offset >= length)
					break;
				dest[outByteIndex++] = 27;
				v = charToGsmExtended(c);
			}
			dest[outByteIndex++] = (byte) v;
		}
		while (outByteIndex - offset < length)
			dest[outByteIndex++] = -1;
	}

	public static int countGsmSeptets(char c) {

		try {

			return countGsmSeptets(c, false);

		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	public static int countGsmSeptets(char c, boolean throwsException)
			throws Exception {
		if (charToGsm.get(c, -1) != -1)
			return 1;
		if (charToGsmExtended.get(c, -1) != -1)
			return 2;
		if (throwsException)
			throw new Exception("" + c);
		else
			return 1;
	}

	public static int countGsmSeptets(String s) {
		try {

			return countGsmSeptets(s, false);

		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		// Exception ex;
		// ex;
	}

	public static int countGsmSeptets(String s, boolean throwsException)
			throws Exception {
		int charIndex = 0;
		int sz = s.length();
		int count = 0;
		for (; charIndex < sz; charIndex++)
			count += countGsmSeptets(s.charAt(charIndex), throwsException);
		return count;
	}

	public static int findGsmSeptetLimitIndex(String s, int start, int limit) {
		int accumulator = 0;
		int size = s.length();
		for (int i = start; i < size; i++) {
			accumulator += countGsmSeptets(s.charAt(i));
			if (accumulator > limit)
				return i;
		}
		return size;
	}

	static final String LOG_TAG = "GSM";
	public static final byte GSM_EXTENDED_ESCAPE = 27;
	private static int sGsmSpaceChar;
	private static final SparseIntArray charToGsm;
	private static final SparseIntArray gsmToChar;
	private static final SparseIntArray charToGsmExtended;
	private static final SparseIntArray gsmExtendedToChar;
	static {
		charToGsm = new SparseIntArray();
		gsmToChar = new SparseIntArray();
		charToGsmExtended = new SparseIntArray();
		gsmExtendedToChar = new SparseIntArray();
		int i = 0;
		charToGsm.put(64, i++);
		charToGsm.put(163, i++);
		charToGsm.put(36, i++);
		charToGsm.put(165, i++);
		charToGsm.put(232, i++);
		charToGsm.put(233, i++);
		charToGsm.put(249, i++);
		charToGsm.put(236, i++);
		charToGsm.put(242, i++);
		charToGsm.put(199, i++);
		charToGsm.put(10, i++);
		charToGsm.put(216, i++);
		charToGsm.put(248, i++);
		charToGsm.put(13, i++);
		charToGsm.put(197, i++);
		charToGsm.put(229, i++);
		charToGsm.put(916, i++);
		charToGsm.put(95, i++);
		charToGsm.put(934, i++);
		charToGsm.put(915, i++);
		charToGsm.put(923, i++);
		charToGsm.put(937, i++);
		charToGsm.put(928, i++);
		charToGsm.put(936, i++);
		charToGsm.put(931, i++);
		charToGsm.put(920, i++);
		charToGsm.put(926, i++);
		charToGsm.put(65535, i++);
		charToGsm.put(198, i++);
		charToGsm.put(230, i++);
		charToGsm.put(223, i++);
		charToGsm.put(201, i++);
		charToGsm.put(32, i++);
		charToGsm.put(33, i++);
		charToGsm.put(34, i++);
		charToGsm.put(35, i++);
		charToGsm.put(164, i++);
		charToGsm.put(37, i++);
		charToGsm.put(38, i++);
		charToGsm.put(39, i++);
		charToGsm.put(40, i++);
		charToGsm.put(41, i++);
		charToGsm.put(42, i++);
		charToGsm.put(43, i++);
		charToGsm.put(44, i++);
		charToGsm.put(45, i++);
		charToGsm.put(46, i++);
		charToGsm.put(47, i++);
		charToGsm.put(48, i++);
		charToGsm.put(49, i++);
		charToGsm.put(50, i++);
		charToGsm.put(51, i++);
		charToGsm.put(52, i++);
		charToGsm.put(53, i++);
		charToGsm.put(54, i++);
		charToGsm.put(55, i++);
		charToGsm.put(56, i++);
		charToGsm.put(57, i++);
		charToGsm.put(58, i++);
		charToGsm.put(59, i++);
		charToGsm.put(60, i++);
		charToGsm.put(61, i++);
		charToGsm.put(62, i++);
		charToGsm.put(63, i++);
		charToGsm.put(161, i++);
		charToGsm.put(65, i++);
		charToGsm.put(66, i++);
		charToGsm.put(67, i++);
		charToGsm.put(68, i++);
		charToGsm.put(69, i++);
		charToGsm.put(70, i++);
		charToGsm.put(71, i++);
		charToGsm.put(72, i++);
		charToGsm.put(73, i++);
		charToGsm.put(74, i++);
		charToGsm.put(75, i++);
		charToGsm.put(76, i++);
		charToGsm.put(77, i++);
		charToGsm.put(78, i++);
		charToGsm.put(79, i++);
		charToGsm.put(80, i++);
		charToGsm.put(81, i++);
		charToGsm.put(82, i++);
		charToGsm.put(83, i++);
		charToGsm.put(84, i++);
		charToGsm.put(85, i++);
		charToGsm.put(86, i++);
		charToGsm.put(87, i++);
		charToGsm.put(88, i++);
		charToGsm.put(89, i++);
		charToGsm.put(90, i++);
		charToGsm.put(196, i++);
		charToGsm.put(214, i++);
		charToGsm.put(327, i++);
		charToGsm.put(220, i++);
		charToGsm.put(167, i++);
		charToGsm.put(191, i++);
		charToGsm.put(97, i++);
		charToGsm.put(98, i++);
		charToGsm.put(99, i++);
		charToGsm.put(100, i++);
		charToGsm.put(101, i++);
		charToGsm.put(102, i++);
		charToGsm.put(103, i++);
		charToGsm.put(104, i++);
		charToGsm.put(105, i++);
		charToGsm.put(106, i++);
		charToGsm.put(107, i++);
		charToGsm.put(108, i++);
		charToGsm.put(109, i++);
		charToGsm.put(110, i++);
		charToGsm.put(111, i++);
		charToGsm.put(112, i++);
		charToGsm.put(113, i++);
		charToGsm.put(114, i++);
		charToGsm.put(115, i++);
		charToGsm.put(116, i++);
		charToGsm.put(117, i++);
		charToGsm.put(118, i++);
		charToGsm.put(119, i++);
		charToGsm.put(120, i++);
		charToGsm.put(121, i++);
		charToGsm.put(122, i++);
		charToGsm.put(228, i++);
		charToGsm.put(246, i++);
		charToGsm.put(241, i++);
		charToGsm.put(252, i++);
		charToGsm.put(224, i++);
		charToGsmExtended.put(12, 10);
		charToGsmExtended.put(94, 20);
		charToGsmExtended.put(123, 40);
		charToGsmExtended.put(125, 41);
		charToGsmExtended.put(92, 47);
		charToGsmExtended.put(91, 60);
		charToGsmExtended.put(126, 61);
		charToGsmExtended.put(93, 62);
		charToGsmExtended.put(124, 64);
		charToGsmExtended.put(8364, 101);
		int size = charToGsm.size();
		for (int j = 0; j < size; j++)
			gsmToChar.put(charToGsm.valueAt(j), charToGsm.keyAt(j));
		size = charToGsmExtended.size();
		for (int j = 0; j < size; j++)
			gsmExtendedToChar.put(charToGsmExtended.valueAt(j),charToGsmExtended.keyAt(j));
		sGsmSpaceChar = charToGsm.get(32);
	}
}
