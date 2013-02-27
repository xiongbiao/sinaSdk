package com.bn.carracer;

import static com.bn.carracer.Constant.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ViewAbout extends SurfaceView implements SurfaceHolder.Callback{

	Activity_GL_Racing activity;//��������
	Bitmap about;
	static int viewFlag=0;//����ı�־λ��0��ʾ��һ�����ڽ��棬1��ʾ�ڶ������ڽ���ͼ��
	float screenWidth=480;//ͼƬ���
	float x_Offset=Activity_GL_Racing.screenWidth/2-screenWidth/2;
//	Bitmap aboutTwo;
	static ThreadAboutView avt;//�߳�����
	public ViewAbout(Activity_GL_Racing activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
		this.activity=activity;
		about=BitmapFactory.decodeResource(this.getResources(), R.drawable.about);
//		aboutTwo=BitmapFactory.decodeResource(this.getResources(), R.drawable.abouttwo);
		avt=new ThreadAboutView(this);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				int x = (int) event.getX();//��ȡX����
				int y = (int) event.getY();//��ȡY����
				if(viewFlag==0)
				{
//					if(x>10+x_Offset&&x<97+x_Offset&&y>278&&y<304)//�����һҳ
//					{
//						viewFlag=1;//�ı��־λ����ڶ���ͼ
//					}
					if(x>382+x_Offset&&x<470+x_Offset&&y>278&&y<305)//�������
					{
						activity.toAnotherView(ENTER_MENU);//���ز˵�����
						avt.flag=false;//�ر��߳�
					}
				}
//				if(viewFlag==1)
//				{
//					if(x>382+x_Offset&&x<470+x_Offset&&y>278&&y<305)//�������
//					{
//						viewFlag=0;//���ز˵�����
//					}
//				}
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if(viewFlag==0)
		{
			canvas.drawBitmap(about, Activity_GL_Racing.screenWidth/2-screenWidth/2,0, null);
		}
//		if(viewFlag==1)
//		{
//			canvas.drawBitmap(aboutTwo, Activity_GL_Racing.screenWidth/2-screenWidth/2,0,null);
//		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		avt.start();//�����߳�
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
