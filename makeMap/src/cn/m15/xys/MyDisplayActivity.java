package cn.m15.xys;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyDisplayActivity extends Activity {
	private int screenW, screenH;
	private int rSize = 60;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_play);
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		screenW = dm.widthPixels; 
		screenH = (dm.heightPixels)/2;
		   DisplaySurfaceView dsv = new DisplaySurfaceView(this, screenW, screenH);
		  Button botton1 = (Button)findViewById(R.id.aaaBtn);
	        botton1.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
//		    	dsv.SetMap(rSize, 1, 20);
		    	rSize = rSize + 60;
		    }
		});
//	        addContentView(view, params)
	    	setContentView(dsv);
	}

	public	class DisplaySurfaceView  extends SurfaceView implements Callback, Runnable{
		
		private Canvas mCanvas = null;
		private SurfaceHolder mSurfaceHolder = null;
		private Paint mPaint = null;
		private Bitmap mBitmap = null;
		private int mBitMapWidth ;
		private int mBitMapHeight ;
		
		public DisplaySurfaceView(Context context, int screenWidth, int screenHeight) {
			super(context);
			mPaint = new Paint();
			mBitmap = ReadBitMap(context, R.drawable.test);
			mBitMapWidth = mBitmap.getWidth();
			mBitMapHeight = mBitmap.getHeight();
			/** 获取mSurfaceHolder **/
			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);
			setFocusable(true);
			
		}
		
		public Bitmap ReadBitMap(Context context, int resId) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			// 获取资源图片
			InputStream is = context.getResources().openRawResource(resId);
			return BitmapFactory.decodeStream(is, null, opt);
		}
 

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// surfaceView的大小发生改变的时候
		}


		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			SetMap(0,0,0);
		}
		
		public void SetMap(int x,int y ,int src_xp){
			mCanvas = mSurfaceHolder.lockCanvas();
			if (mCanvas != null) {
				mCanvas.save();
				mCanvas.clipRect(x, y, x + src_xp, y + src_xp);
				mCanvas.drawBitmap(mBitmap, x - src_xp, y - src_xp, mPaint);
				mCanvas.restore();
				/** 绘制结束后解锁显示在屏幕上 **/
				mSurfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		} 


		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
         //销毁的时候
			
			
		}
		
	}
	 
}
