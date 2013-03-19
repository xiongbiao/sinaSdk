package org.loon.game.sample.llk;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.resource.LPKResource;
import org.loon.framework.android.game.utils.GraphicsUtils;


public class Images {

	private static Images imagefactory;

	private static LImage images[];

	private Images() {

		images = new LImage[17];
		for (int i = 0; i < 8; i++) {
			images[i] = GraphicsUtils.loadImage("res/" + i + ".jpg",false);
		}

		final String res="res/res.lpk";
		
		images[8] = LPKResource.openImage(res, "a0.png");
		images[9] = LPKResource.openImage(res, "dot.png");
		images[10] = LPKResource.openImage(res, "background.jpg");
		images[11] = LPKResource.openImage(res, "role0.png");
		images[12] = LPKResource.openImage(res, "role1.png");
		images[13] = LPKResource.openImage(res, "role2.png");
		images[14] = LPKResource.openImage(res, "win.png");
		images[15] = LPKResource.openImage(res, "start.png");
		images[16] = LPKResource.openImage(res, "gameover.png");
	
	}

	public LImage getImage(int i) {
		return images[i];
	}

	public static synchronized Images getInstance() {
		if (imagefactory != null) {
			return imagefactory;
		} else {
			imagefactory = new Images();
			return imagefactory;
		}
	}

}
