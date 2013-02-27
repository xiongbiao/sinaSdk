package com.bn.carracer;

import javax.microedition.khronos.opengles.GL10;

import static com.bn.carracer.Constant.*;
import static com.bn.carracer.MyGLSurfaceView.*;
//����ײ����������
public class KZBJForControl
{
	int id;//��Ӧ����ײ��id��0��ʾ��ͨͲ��1��ʾ�ϰ���
	boolean state=false;//false��ʾ�ɱ���ײ��true��ʾ��ײ������У����򲻿���ײ��
	float x;//�ڷŵĳ�ʼλ��
	float y;
	float z;
	
	float alpha;//ת���Ƕ�
	float alphaX;//ת��������
	float alphaY;
	float alphaZ;
	
	float currentX;//�����еĵ�ǰλ��
	float currentY;
	float currentZ;
	
	int row;//λ�����ڵ�ͼ�к���
	int col;
	
	float vx;//�����е��ٶȷ���
	float vy;
	float vz;
	
	float timeFly;//�����ۼ�ʱ��
	
	public KZBJForControl(int id,float x,float y,float z,int row,int col)
	{
		this.id=id;
		this.x=x;
		this.y=y;
		this.z=z;
		this.row=row;
		this.col=col;
	}
	
	public void drawSelf(GL10 gl) 
	{		
		gl.glPushMatrix();
		gl.glDisable(GL10.GL_CULL_FACE);
		if(!state)
		{//ԭʼ״̬����
			gl.glTranslatef(x, y, z);
			kzbjyylb[id].drawSelf(gl,kzbjwllb[id], 0);
		}
		else
		{//�����л���
			if(currentY>-40) 
			{//����Ѿ����е��������£��Ͳ��ٻ���
				gl.glTranslatef(currentX, currentY, currentZ);
				gl.glRotatef(alpha,alphaX, alphaY, alphaZ);
				kzbjyylb[id].drawSelf(gl,kzbjwllb[id], 0);
			}
		}
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glPopMatrix();
	}
	
	//���ݳ���λ�ü������ͷλ�ã����ж��Ƿ���ĳ����ײ������ײ
	public void checkColl(float carXTemp,float carZTemp,float carAlphaTemp)
	{		
		final float R=30f;//�����ĵ㵽��ͷ���롣		
		//�ɳ����ĵ�λ�ü�����ĳ�ͷ����
		float bPointX=0;
		float bPointZ=0;		
		
		//���������ײ��������
		bPointX=(float) (carXTemp-R*Math.sin(Math.toRadians(carAlphaTemp)));
		bPointZ=(float) (carZTemp-R*Math.cos(Math.toRadians(carAlphaTemp)));
		
		float P=X_SPAN;//½�ؿ���
		//������ײ���ڵ�ͼ�ϵ��к���
		float carCol=(float) Math.floor(bPointX/P);
		float carRow=(float) Math.floor(bPointZ/P);
		
		if(carRow==row&&carCol==col)
		{//��������ͬһ������������ϸ����ײ���KZBJBJ
			double disP2=(bPointX-x)*(bPointX-x)+(bPointZ-z)*(bPointZ-z);
			if(disP2<=KZBJBJ[id])
			{//��ײ��
				if(Activity_GL_Racing.soundFlag==true)
				{
					MyGLSurfaceView.activity.playSound(6, 0);//ײ����Ч 
				}
				state=true;//����״̬Ϊ������״̬
				timeFly=0;//���г���ʱ������
				alpha=0;
				alphaX=(float) (-40*Math.cos(Math.toRadians(carAlphaTemp)));
				alphaY=0;
				alphaZ=(float) (40*Math.sin(Math.toRadians(carAlphaTemp)));
				currentX=x;//���÷�����ʼ��Ϊԭʼ�ڷŵ�
				currentY=y;
				currentZ=z;
				//���ݳ����н�����ȷ�������ٶȵ���������
				vx=(float) (-100*Math.sin(Math.toRadians(carAlphaTemp)));
				vy=40;
				vz=(float) (-100*Math.cos(Math.toRadians(carAlphaTemp)));
			}
		}
	}
	
	//�����ƶ��������̶߳�ʱ���ô˷�����ʵ�ֿ�ײ�������
	public void go()
	{
		if(!state)
		{//������ڷ���״̬�в���Ҫgo
			return;
		}
		
		timeFly=timeFly+0.6f;//���г���ʱ������
		alpha=alpha+10;
		//���ݷ����ٶȵ��������������г���ʱ������������㵱ǰλ��
		currentX=x+vx*timeFly;
		currentZ=z+vz*timeFly;
		currentY=y+vy*timeFly-0.5f*5*timeFly*timeFly;//5Ϊ�������ٶ�
		//����ײ��������䵽��������2000ʱ�ָ�ԭλ
		if(currentY<-8000)
		{
			state=false;
			
		}
	}
}