package com.bn.carracer;
import static com.bn.carracer.Constant.*;
import static com.bn.carracer.Activity_GL_Racing.*;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ViewHistory extends SurfaceView implements SurfaceHolder.Callback{

	Activity_GL_Racing activity;
	Bitmap history;//����
	Paint paint;//����
	
	ArrayList<String[]> al=new ArrayList<String[]>();//������ȡ�����	
	boolean moveFlag=false;//�ƶ��᲻�ƶ��ı�־λ falseΪ���ƶ���trueΪ�ƶ�
	private float startMoveY=0;//��¼��ʼ��λ��
	float width=25;//��֮��Ŀ��
	
	int TOTAL_ROWS=5;//��ʾ���������
	int rowFirst=0;//��ǰ����һ���к�
	
	public ViewHistory(Activity_GL_Racing activity) {
		super(activity);
		// TODO Auto-generated constructor stub
		this.activity=activity;
		paint=new Paint();
		getHolder().addCallback(this);
		history=BitmapFactory.decodeResource(this.getResources(), R.drawable.history);//���ر���ͼƬ
		al=DBUtil.getResult();		
	}


	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		      //д���ذ�ť�Ĵ���
		      if(x>387+Activity_GL_Racing.screen_xoffset&&x<474+Activity_GL_Racing.screen_xoffset&&y>280&&y<310)
		      {
		    	  activity.toAnotherView(CHOOSE);//���ص�ѡ�����
		      }
		    break;
			case MotionEvent.ACTION_MOVE:				
				int dy=(int) (y-startMoveY);//��ȡy�����ϵ��ƶ���
				
				if(al.size()<=TOTAL_ROWS)
				{
					return true;
				}
				
				if(dy>25)//����ƶ��ľ������һ����ľ���
				{
					rowFirst=rowFirst-1;
					if(rowFirst<0){rowFirst=0;};
					startMoveY=y;//����ǰyֵ���迪ʼ��
					repaint();
				}
				if(dy<-25)
				{
					rowFirst=rowFirst+1;
					if(rowFirst>al.size()-TOTAL_ROWS)
					{
						rowFirst=al.size()-TOTAL_ROWS;
					}
					startMoveY=y;//����ǰyֵ���迪ʼ��
					repaint();
				}				
				break;
		}
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		canvas.drawBitmap(history, Activity_GL_Racing.screen_xoffset,0, paint);//���Ʊ���		
		for(int i=0;i<5;i++)//����ʹ��ʱ��
		{
			if (i+rowFirst>al.size()-1) break;
			
			int currIndex=i+rowFirst;
			 
			String timeStr=al.get(currIndex)[0];
			String dateStr=al.get(currIndex)[1];
			for(int j=0;j<timeStr.length();j++)
			{
				char c=timeStr.charAt(j);
				if(c==':')
				{
					canvas.drawBitmap(number[10], 21+j*16+Activity_GL_Racing.screen_xoffset, 139+i*width, paint);
				}else
				{ 
					canvas.drawBitmap(number[c-'0'], 21+j*16+Activity_GL_Racing.screen_xoffset, 139+i*width, paint);
				}
			}
			for(int j=0;j<dateStr.length();j++)
			{
				char d=dateStr.charAt(j);				
				if(d=='-')
				{
					canvas.drawBitmap(number[11], 134+j*16+Activity_GL_Racing.screen_xoffset, 139+i*width, paint);
				}else if(d==' ')
				{
					continue;
				}else if(d==':')
				{
					canvas.drawBitmap(number[10], 134+j*16+Activity_GL_Racing.screen_xoffset, 139+i*width, paint);
				}else
				{
					canvas.drawBitmap(number[d-'0'], 134+j*16+Activity_GL_Racing.screen_xoffset, 139+i*width, paint);
				}
			}			
		}	
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
	//�ػ�ķ���
	public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
	


}
