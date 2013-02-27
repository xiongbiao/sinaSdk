package com.bn.carracer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.carracer.Constant.*;
public class ViewChoose extends SurfaceView implements SurfaceHolder.Callback{

	Activity_GL_Racing activity;//��������
	Bitmap choose;//���ý��汳��
	
	float screenWidth=480;//ͼƬ���
	float x_Offset=Activity_GL_Racing.screenWidth/2-screenWidth/2;

	Paint paint;//���� 
	
	public ViewChoose(Activity_GL_Racing activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity=activity;
		getHolder().addCallback(this);
		paint=new Paint();
		choose=BitmapFactory.decodeResource(this.getResources(), R.drawable.choose);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch(event.getAction()) 
		{
		case MotionEvent.ACTION_DOWN:
			int x = (int) event.getX();//��ȡX����
			int y = (int) event.getY();
			if(x>135+x_Offset&&x<343+x_Offset&&y>100&&y<169)//������˵�
			{
				activity.toAnotherView(LOADING);//������Ϸ
			}
			if(x>135+x_Offset&&x<343+x_Offset&&y>169&&y<247)//�������һ��
			{
				activity.toAnotherView(HISTORY);//������ʷ����
			} 
			if(x>387+x_Offset&&x<474+x_Offset&&y>280&&y<310)//������ؼ�
			{
				activity.toAnotherView(ENTER_MENU);//�������˵� 
			}
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);		
		canvas.drawBitmap(choose, Activity_GL_Racing.screenWidth/2-screenWidth/2,0, paint);		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Canvas canvas = holder.lockCanvas();//��ȡ����
		try{
			synchronized(holder){
				onDraw(canvas);//����
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(canvas != null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
