package com.bn.carracer;

import static com.bn.carracer.Constant.*;
import static com.bn.carracer.MyGLSurfaceView.*;
import android.util.Log;
//��������״̬���߳�
public class ThreadKey extends Thread
{
	MyGLSurfaceView mv; 
	static boolean flag=true;
	
	static boolean tisuFlag=false;//�����б�־λ
	static boolean jianyouFlag=false;//���ͱ�־λ
	static boolean shacheFlag=false;//ɲ���б�־λ 
	static boolean zhuangceFlag=false;//ײ�����־λ
	
	public ThreadKey(MyGLSurfaceView mv)
	{
		this.mv=mv;
	}
	
	public void run() 
	{		
		while(flag)
		{			
			if((MyGLSurfaceView.keyState&0x1)!=0) 
			{//��UP�����£����������
				isBrake=false;//δɲ������β����
				if(carV<CAR_MAX_SPEED*SpeedFactor)
				{
					carV=carV+CAR_SPEED_SPAN;//����
					
					if(Activity_GL_Racing.soundFlag==true&&tisuFlag==false)//��Ϊ��Ч����״̬������û�������ʱ������������Ч
					{
						MyGLSurfaceView.activity.playSound(4, 0);//������Ч
						
						tisuFlag=true;//������
						shacheFlag=false;//����ɲ����
						zhuangceFlag=false;//����ײ���� 
						jianyouFlag=false;//���ڼ�����
					}	
				}
			}
			else if((MyGLSurfaceView.keyState&0x2)!=0)
			{//��down������,����ɲ���򵹳�
				isBrake=true;//ɲ������β������
				if(carV>-CAR_MAX_SPEED*SpeedFactor)
				{
					carV=carV-CAR_SPEED_SPAN*2;//ɲ���򵹳�
					
					if(Activity_GL_Racing.soundFlag==true&&shacheFlag==false)//��Ϊ��Ч����״̬������״̬
					{
						MyGLSurfaceView.activity.playSound(2, 0);//ɲ����Ч
						
						shacheFlag=true;//ɲ����
						tisuFlag=false;//����������
						zhuangceFlag=false;//����ײ����
						jianyouFlag=false;//���ڼ�����
					}
				}
			}
			else if((MyGLSurfaceView.keyState&0x2)==0&&(MyGLSurfaceView.keyState&0x1)==0)//������UP��DOWN��ʱ������ֹͣ
			{
				isBrake=false;//δɲ������β����
				if(carV>0)
				{
					carV=carV-CAR_SPEED_SPAN/2;//����
					
					if(Activity_GL_Racing.soundFlag==true&&jianyouFlag==false)//��Ϊ��Ч����״̬������״̬
					{
						MyGLSurfaceView.activity.playSound(3, 0);//������Ч
						
						jianyouFlag=true;//������ 
						tisuFlag=false;//����������
						shacheFlag=false;//����ɲ����
						zhuangceFlag=false;//����ײ���� 
					} 
				}
				else if(carV<0)
				{
					carV=carV+CAR_SPEED_SPAN/2;//����
					
					if(Activity_GL_Racing.soundFlag==true&&jianyouFlag==false)//��Ϊ��Ч����״̬������״̬
					{
						MyGLSurfaceView.activity.playSound(3, 0);//������Ч
						
						jianyouFlag=true;//������
						tisuFlag=false;//����������
						shacheFlag=false;//����ɲ����
						zhuangceFlag=false;//����ײ����
					}
				}
			}			
			
			float tempCarAlpha=0;
			float tempcarAlphaRD=0;
			
			if((MyGLSurfaceView.keyState&0x4)!=0)
			{//��left������
				//����ת��
				tempCarAlpha=carAlpha+DEGREE_SPAN;	
				tempcarAlphaRD=15;//�����Ŷ��Ƕ�ֵ 
			}
			else if((MyGLSurfaceView.keyState&0x8)!=0)
			{//��right������
				//����ת�� 
				tempCarAlpha=carAlpha-DEGREE_SPAN;  
				tempcarAlphaRD=-15; //�����Ŷ��Ƕ�ֵ
			}
			else if((MyGLSurfaceView.keyState&0x8)==0&&(MyGLSurfaceView.keyState&0x4)==0)//������LEFT��RIGHT��ʱ�����Ŷ��Ƕ�Ϊ��
			{
				tempcarAlphaRD=0;//�����Ŷ��Ƕ�ֵ
				tempCarAlpha=carAlpha;
			}
			

			float xOffset=0;//�˲���Xλ��
    		float zOffset=0;//�˲���Zλ��     		
    		xOffset=(float)-Math.sin(Math.toRadians(tempCarAlpha))*carV;
			zOffset=(float)-Math.cos(Math.toRadians(tempCarAlpha))*carV;
			
			boolean b=isCollHead(carX+xOffset,carZ+zOffset,tempCarAlpha)||isCollTail(carX+xOffset,carZ+zOffset,tempCarAlpha);
			if(!b)
			{
				carOldX=carX;
				carX=carX+xOffset;
				carZ=carZ+zOffset;
				carAlpha=tempCarAlpha;
				carAlphaRD=tempcarAlphaRD; 
			} 
			else 
			{ 
				carV=0;//ײ��
				carAlphaRD=0;
				
				if(Activity_GL_Racing.soundFlag==true&&zhuangceFlag==false)//��Ϊ��Ч����״̬������û����ײ��״̬ʱ������ײ����Ч
				{
					MyGLSurfaceView.activity.playSound(6, 0);//ײ����Ч 
//					MyGLSurfaceView.activity.vibrator();//ײ������
					zhuangceFlag=true;//��ײ��״̬
				}
			}  
			
			isHalf(carX,carZ);
			isOneCycle(carX,carZ);
			
			try { 
				Thread.sleep(80);
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			}
		}
	}
	
	//�ж��Ƿ񿪵��˰�Ȧ
	public void isHalf(float carTempX,float carTempZ)
	{
		double dis=Math.sqrt
		(
			(carTempX-RACE_HALF_X)*	(carTempX-RACE_HALF_X)
			+(carTempZ-RACE_HALF_Z)*(carTempZ-RACE_HALF_Z)
		);
		if(dis<=186)
		{
			halfFlag=true;
			Log.d("halfFlag", halfFlag+"");
		}
	}
	
	//�ж��Ƿ�����һȦ
	public void isOneCycle(float carTempX,float carTempZ)
	{
		double dis=Math.sqrt
		(
				(carTempX-RACE_BEGIN_X)*(carTempX-RACE_BEGIN_X)
				+(carTempZ-RACE_BEGIN_Z)*(carTempZ-RACE_BEGIN_Z)
		);
		if(dis<=186&&carOldX<RACE_BEGIN_X&&carTempX>=RACE_BEGIN_X)
		{
			if(halfFlag==true)
			{
				quanshu++;//Ȧ����1
				
				if(quanshu==MAX_QUANSHU)//���Ȧ���������Ȧ�����������Ϸ��
				{
					mv.initState();//��ʼ������
					
					boolean b=DBUtil.getNewRecord(Math.floor(MyGLSurfaceView.gameContinueTime()/1000));//�ж��Ƿ��Ƽ�¼
					if(b)
					{
						MyGLSurfaceView.activity.toAnotherView(BREAKING);//��ת���Ƽ�¼����
					}
					else
					{
						MyGLSurfaceView.activity.toAnotherView(STRIVE);//��ת���Ƽ�¼����
					}
					
//					MyGLSurfaceView.activity.toAnotherView(OVER);//���ؽ���
				}
				
				benquanStartTime=System.currentTimeMillis();//���¿�ʼ��һȦ��ʱ��
//				Log.d("quanshu", quanshu+""); 
			}
			halfFlag=false;
		}
		else if(dis<=186&&carOldX>=RACE_BEGIN_X&&carTempX<RACE_BEGIN_X)
		{
			halfFlag=false;
		}
	}
	
	//���ָ������ײ���з���ײ
	public boolean isColl(float bPointX,float bPointZ)
	{
		float P=X_SPAN;//½�ؿ���
		//������ײ���ڵ�ͼ�ϵ��к���
		float col=(float) Math.floor(bPointX/P);
		float row=(float) Math.floor(bPointZ/P);
		
		//������ײ���ڶ�Ӧ�����и����е�x��z���꣬ÿ��С���ӵ����ĵ㼴Ϊ�ø��ӵ�����ԭ��
		float xIn=bPointX-col*P-0.5f*P;
		float zIn=bPointZ-row*P-0.5f*P;
		//������ײ�����ڸ��ӵ��к��У���ȡ����ײ�����ڸ��ӵ�������š�
		int sdNumber=MAP_LEVEL1[(int) row][(int) col]; 

		//��Ϊ0�����������ж�z�����ڲ��ڷ�Χ�ڡ�
		if(sdNumber==0)
		{
			if(zIn>=ROAD_W/2||zIn<=-ROAD_W/2)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		//��Ϊ1�����������ж�x�����ڲ��ڷ�Χ�ڡ�
		if(sdNumber==1)
		{
			if(xIn>=ROAD_W/2||xIn<=-ROAD_W/2)
			{
				return true;
			}
			else
			{ 
				return false;
			}
		}
		
		//��Ϊ2~9������������������ŵ������в��Ҷ�Ӧ�����ŵ�����Բ�����꣬�������ײ��
		//������Բ�ĵľ��룬Ȼ���ж�������Բ�ĵľ����Ƿ����Ҫ��
		if(sdNumber>=2&&sdNumber<=9)
		{
			int k=(sdNumber-2)%4;
			float halfP=P/2;
			double dis=Math.sqrt(
				(xIn-WD_COLL[0][k]*halfP)*(xIn-WD_COLL[0][k]*halfP)+
				(zIn-WD_COLL[1][k]*halfP)*(zIn-WD_COLL[1][k]*halfP)
			);
			if(dis<(P-ROAD_W)/2||dis>(P+ROAD_W)/2)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
    //��⳵ͷ�з���ײ
	public boolean isCollHead(float carXTemp,float carZTemp,float carAlphaTemp)//����true��ʾײ�ˣ�����false��ʾûײ��
	{
		final float R=30f;//�����ĵ㵽��ͷ���롣		
		//�ɳ����ĵ�λ�ü�����ĳ�ͷ����
		float bPointX=0;
		float bPointZ=0;		
		
		//���������ײ��������
		bPointX=(float) (carXTemp-R*Math.sin(Math.toRadians(carAlphaTemp)));
		bPointZ=(float) (carZTemp-R*Math.cos(Math.toRadians(carAlphaTemp)));
	
		return isColl(bPointX,bPointZ);
	}
	
	//��⳵β�з���ײ
	public boolean isCollTail(float carXTemp,float carZTemp,float carAlphaTemp)//����true��ʾײ�ˣ�����false��ʾûײ��
	{
		final float R=30f;//�����ĵ㵽��ͷ���롣		
		//�ɳ����ĵ�λ�ü�����ĳ�ͷ����
		float bPointX=0; 
		float bPointZ=0;		
		
		//���������ײ��������
		bPointX=(float) (carXTemp+R*Math.sin(Math.toRadians(carAlphaTemp)));
		bPointZ=(float) (carZTemp+R*Math.cos(Math.toRadians(carAlphaTemp)));
			
		return isColl(bPointX,bPointZ);
	}
}
