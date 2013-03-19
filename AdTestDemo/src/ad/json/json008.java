package ad.json;

import java.io.ByteArrayOutputStream;

public class json008 {

	 
	public static void main(String[] args) {
		System.err.println("mmms");
		String hex = "熊彪";
		System.err.println(encode(hex));
//		int i = Integer.parseInt(hex, 16);
//		System.out.println(hex);
//		String str = b2h(Integer.toBinaryString(i));
//		System.out.println(str);
	}

	
	
	
	

	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";

	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	static String[] hexStr = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F" };

	public static String b2h(String binary) {
		// 这里还可以做些判断，比如传进来的数字是否都是0和1
		System.out.println(binary);
		int length = binary.length();
		int temp = length % 4;
		// 每四位2进制数字对应一位16进制数字
		// 补足4位
		if (temp != 0) {
			for (int i = 0; i < 4 - temp; i++) {
				binary = "0" + binary;
			}
		}
		// 重新计算长度
		length = binary.length();
		StringBuilder sb = new StringBuilder();
		// 每4个二进制数为一组进行计算
		for (int i = 0; i < length / 4; i++) {
			int num = 0;
			// 将4个二进制数转成整数
			for (int j = i * 4; j < i * 4 + 4; j++) {
				num <<= 1;// 左移
				num |= (binary.charAt(j) - '0');// 或运算
			}
			// 直接找到该整数对应的16进制，这里不用switch来做
			sb.append(hexStr[num]);
			// 这里如果要用switch case来做，大概是这个样子
			// switch(num){
			// case 0:
			// sb.append('0');
			// break;
			// case 1:
			// ...
			// case 15:
			// sb.append('F');
			// break;
			// }
		}
		return sb.toString();
	}
}
