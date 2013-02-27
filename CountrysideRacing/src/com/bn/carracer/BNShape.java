package com.bn.carracer;

import javax.microedition.khronos.opengles.GL10;

abstract class BNShape {

	float scale;// 尺寸比例

	public BNShape(float scale) {
		this.scale = scale;
	}

	public abstract void drawSelf(GL10 gl, int texId, int number);
}
