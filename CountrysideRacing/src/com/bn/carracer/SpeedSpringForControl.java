package com.bn.carracer;

import javax.microedition.khronos.opengles.GL10;
import static com.bn.carracer.MyGLSurfaceView.*;
import static com.bn.carracer.Constant.*;

public class SpeedSpringForControl {
	int id;
	float x;
	float y;
	float z;
	
	int row;
	int col;
	
	float alpha=0;
	
	public SpeedSpringForControl(int id,float x,float y,float z,int row,int col)
	{
		this.id=id;
		this.x=x;
		this.y=y;
		this.z=z;
		this.row=row;
		this.col=col;
	}
	
	public void drawSelf(GL10 gl,int row,int col)
    {
		if(row!=this.row||col!=this.col)
		{
			return;
		}
		
    	gl.glPushMatrix();
    	gl.glEnable(GL10.GL_LIGHTING);
        initLight(gl);
    	gl.glTranslatef(x, y, z);
    	gl.glRotatef(alpha, 0, 1, 0);
    	if(id==0)//��ײ����ٵĻ�ɫ����
    	{
    		lovn_speed_spring.drawSelf(gl, 1f,1f,0.5f);
    	}
    	else if(id==1)//��ײ����ٵ���ɫ����
    	{
    		lovn_speed_spring.drawSelf(gl, 0.5f,0.5f,1f);
    	}
    	
    	gl.glDisable(GL10.GL_LIGHT1);//�ر�1�ŵ�
    	gl.glDisable(GL10.GL_LIGHTING);
    	
    	gl.glPopMatrix();
    }
	 
	private void initLight(GL10 gl)
	{
	    gl.glEnable(GL10.GL_LIGHT0);//��0�ŵ�  
	    
	    //���������� 
	    float[] ambientParams={0.8f,0.8f,0.8f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

	    //ɢ�������
	    float[] diffuseParams={0.9f,0.9f,0.9f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
	    
	    //���������
	    float[] specularParams={0.8f,0.8f,0.8f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);    
	    
	    float[] positionParamsGreen=//����0��ʾ�Ƕ�λ��
	    {
	    		-100,
	    		80,
	    		-100,
	    		0
	    };
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParamsGreen,0); 
	    
	    //================================================================================
	    
        gl.glEnable(GL10.GL_LIGHT1);//��1�ŵ�  
	    
	    //����������
	    ambientParams=new float[]{0.8f,0.8f,0.8f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams,0);            

	    //ɢ�������
	    diffuseParams=new float[]{0.9f,0.9f,0.9f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams,0); 
	    
	    //���������
	    specularParams=new float[]{0.8f,0.8f,0.8f,1.0f};//����� RGBA
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams,0);    
	    
	    positionParamsGreen=//����0��ʾ�Ƕ�λ��
	    new float[]{
	    		100,
	    		100,
	    		100,
	    		0
	    };
	    gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, positionParamsGreen,0); 	    
	}

    public void checkColl(float carXTemp,float carZTemp,float carAlphaTemp)
    {
    	alpha=(alpha+5)%360.0f;
    	
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

			if(disP2<=JJSTHBJ)
			{//��ײ��
				if(Activity_GL_Racing.soundFlag==true)
				{
					MyGLSurfaceView.activity.playSound(7, 0);//ײ����Ч 
				}
				
				MyGLSurfaceView.ssfcList.remove(this); 
				
				if(id==0)
				{//ִ�м��ٶ���
					if(SpeedFactorControl==0)
					{
						SpeedFactorControl=1;
						SpeedFactor=SpeedFactor+0.3f;
					}
					else if(SpeedFactorControl==-1)
					{
						SpeedFactorControl=0;
						SpeedFactor=SpeedFactor+0.3f;
					}
				}
				else if(id==1)
				{//ִ�м��ٶ���
					if(SpeedFactorControl==0)
					{
						SpeedFactorControl=-1;
						SpeedFactor=SpeedFactor-0.3f;
						if(carV>CAR_MAX_SPEED*0.7f)
						{
							carV=CAR_MAX_SPEED*0.7f;
						}
					}
					else if(SpeedFactorControl==1)
					{
						SpeedFactorControl=0;
						SpeedFactor=SpeedFactor-0.3f;
						if(carV>CAR_MAX_SPEED)
						{
							carV=CAR_MAX_SPEED;
						}
					}
				}
			}
		}
    }
}
