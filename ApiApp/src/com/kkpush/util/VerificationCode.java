/**
 * 
 */
package com.kkpush.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiongbiao 验证码
 */
public class VerificationCode {

	public static Map<String , Object> getRandom(int width, int height ) {
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics(); // 创建Graphics类的对象
		// Graphics2D g2d=(Graphics2D)g; //通过Graphics类的对象创建一个Graphics2D类的对象
		Random random = new Random(); // 实例化一个Random对象
		Font mFont = new Font("华文宋体", Font.BOLD, 18); // 通过Font构造字体
		g.setColor(getRandColor(200, 250)); // 改变图形的当前颜色为随机生成的颜色
		g.fillRect(0, 0, width, height); // 绘制一个填色矩形
		// 干扰线100个
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 画一条折线
		// BasicStroke bs=new
		// BasicStroke(2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL);
		// //创建一个供画笔选择线条粗细的对象
		// g2d.setStroke(bs); //改变线条的粗细
		// g.setColor(Color.DARK_GRAY); //设置当前颜色为预定义颜色中的深灰色
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		for (int j = 0; j < 3; j++) {
			xPoints[j] = random.nextInt(width - 1);
			yPoints[j] = random.nextInt(height - 1);
		}
		g.drawPolyline(xPoints, yPoints, 3);
		// 生成并输出随机的验证文字
		g.setFont(mFont);
		String sRand = "";
		int itmp = 0;
		for (int i = 0; i < 4; i++) {
			// if(random.nextInt(2)==1){
			// itmp=random.nextInt(26)+65; //生成A~Z的字母
			// }else{
			// itmp=random.nextInt(10)+48; //生成0~9的数字
			// }
			itmp = random.nextInt(10) + 48; // 生成0~9的数字
			char ctmp = (char) itmp;
			sRand += String.valueOf(ctmp);
			Color color = new Color(random.nextInt(110), random.nextInt(110),
					random.nextInt(110));
			// Color color=new Color(255,0,255);
			g.setColor(color);
			/** **随机缩放文字并将文字旋转指定角度* */
			// 将文字旋转指定角度
			Graphics2D g2d_word = (Graphics2D) g;
			AffineTransform trans = new AffineTransform();
			trans.rotate(random.nextInt(45) * 3.14 / 180, 15 * i + 10, 7);
			// 缩放文字
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1.1f)
				scaleSize = 1f;
			trans.scale(scaleSize, scaleSize);
			g2d_word.setTransform(trans);
			/** ********画文字************* */
			g.drawString(String.valueOf(ctmp), 20 * i + 16, 16);
		}
		g.dispose();
		Map<String, Object> resultMap= new HashMap<String, Object>();
		resultMap.put("sRand", sRand);
        resultMap.put("image", image); 		
		return resultMap;
	}

	/*
	 * 给定范围获得随机颜色
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
