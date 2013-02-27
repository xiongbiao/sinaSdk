package com.bn.carracer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.carracer.Constant.*;
public class ViewStart extends SurfaceView implements SurfaceHolder.Callback{

	Activity_GL_Racing activity;
	Paint paint;
	int currentAloha=0;  //��ǰ��͸����
	int screenWidth=480;
	int screenHeight=320;
	int sleepSpan=50;
	
	Bitmap[] logos=new Bitmap[2];     //logoͼƬ����
	Bitmap currentLogo;               //��ǰlogoͼƬ����
	Bitmap sound;
	int currentX; 
	int currentY;
	public ViewStart(Activity_GL_Racing activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity=activity;
		this.getHolder().addCallback(this);//�����������ڻص��ӿڵ�ʵ����		
		paint=new Paint();        //��������
		paint.setAntiAlias(true);    //�򿪿����
				
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.baina);//���ذ���ͼƬ
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.bnkjs);//����logoͼƬ
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		try
		{
			//���ƺ�ɫ�������屳��
			paint.setColor(Color.BLACK);
			paint.setAlpha(255);
			canvas.drawRect(0, 0,screenWidth, screenHeight ,paint);
			 
			//����ƽ����ͼ 
			if(currentLogo==null)return;
			paint.setAlpha(currentAloha);
			
//			float k=Activity_GL_Racing.screenWidth/2-screenWidth/2;						
			canvas.drawBitmap(currentLogo, Activity_GL_Racing.screen_xoffset,0,paint);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {//����ʱ����
		// TODO Auto-generated method stub
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;
					//ͼƬ��λ��
					currentX=screenWidth/2-bm.getWidth()/2;     //X����λ��
					currentY=screenHeight/2-bm.getHeight()/2;   //Y����λ��
					
					for(int i=255;i>-10;i=i-10)  //��̬����ͼƬ��͸����ֵ�������ػ�	
					{			
						currentAloha=i;
						if(currentAloha<0)
						{
							currentAloha=0;
						}
						SurfaceHolder myholder=ViewStart.this.getHolder();
						Canvas canvas = myholder.lockCanvas();//��ȡ����
						try
						{
							synchronized(myholder)
							{
								onDraw(canvas);    //����
							}							
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas !=null)
							{
								myholder.unlockCanvasAndPost(canvas);								
							}							
						}
						
						try
						{
							if(i==255)
							{
								Thread.sleep(1000);							
							}
							Thread.sleep(sleepSpan);						
						}catch(Exception e)
						{
							e.printStackTrace();
						}
											
					}//��һ��forѭ��				
				}//�ڶ���forѭ��
				activity.toAnotherView(ENTER_SOUND);//�����������ƽ���
			}
			
		}.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

}
