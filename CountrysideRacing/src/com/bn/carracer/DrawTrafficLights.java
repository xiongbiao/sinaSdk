package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class DrawTrafficLights extends BNShape
{
	//�����峤������
	private static float LENGTH=12f;
	private static float WIDTH=2f;
	private static float HEIGHT=4f;
	
	//��뾶���и�Ƕ�
	private final float RADIUS=4f;
    private final float ANGLE_SPAN=18;
    
    //Բ���ߡ��뾶���и����������
	private static float CYLINDER_HEIGHT=40f;
	private static float CIRCLE_RADIUS=2f;
	private static float DEGREESPAN=18f;
	private static int COL=1;
    
	static boolean flag=true;//�߳�������־λ	
	Cube lightBoard;
	DrawBall light;
	Texture face;
	DrawCylinder pole;
	DrawCylinder poleh;
	
	static LightTurn lt;
	
	static float count=0;
	float mAngleX=0;
	float mAngleY=0;
	
	public DrawTrafficLights(float scale) 
	{
		super(scale);
		lightBoard=new Cube(scale);
		light=new DrawBall(scale);
		face=new Texture(scale);
		pole = new DrawCylinder(
				scale*CYLINDER_HEIGHT,
				scale*CIRCLE_RADIUS,
				scale*DEGREESPAN,
				COL);
		poleh=new DrawCylinder(
				scale*CYLINDER_HEIGHT*0.2f,
				scale*CIRCLE_RADIUS*0.2f,
				scale*DEGREESPAN,
				COL);
		
	}
	
	//��ʼ����ͨ���߳�
	public void initLightTurn()
	{
		lt=new LightTurn();
	}

	@Override
	public void drawSelf(GL10 gl, int texId, int number) 
	{
		gl.glRotatef(mAngleX, 1, 0, 0);
		gl.glRotatef(mAngleY, 0, 1, 0);
		
		gl.glPushMatrix();
		gl.glTranslatef(0, scale*CYLINDER_HEIGHT/2,0);
		gl.glRotatef(90, 0, 0, 1);
		pole.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*RADIUS, scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.06f, 0);
		poleh.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*RADIUS, scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.12f, 0);
		poleh.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*RADIUS+scale*CYLINDER_HEIGHT*0.2f/2+scale*LENGTH, 
				scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.1f, 
				0);
		lightBoard.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*RADIUS+scale*CYLINDER_HEIGHT*0.2f/2+scale*LENGTH, 
				scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.1f, 
				scale*WIDTH);
		face.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		if(count%3==2)//�̵�
		{
			gl.glPushMatrix();
			float[] directionParams={0,0,35f,0};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams,0);
			initMaterial(gl,0,1,0,1);//��ʼ������
			gl.glTranslatef(scale*RADIUS+scale*CYLINDER_HEIGHT*0.2f/2+5*RADIUS*scale, 
					scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.1f, 
					scale*WIDTH+0.1f-RADIUS*scale/2);
			gl.glRotatef(90, 1, 0, 0);
            initLight(gl,1,1,1,1);//����
            light.drawSelf(gl);//����
            closeLight(gl);//�ص�
			gl.glPopMatrix();
		}
		
		if(count%3==0)//���
		{
			gl.glPushMatrix();
			float[] directionParams={0,0,35f,0};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams,0);
			initMaterial(gl,1,0,0,1);//��ʼ������
			gl.glTranslatef(scale*RADIUS+scale*CYLINDER_HEIGHT*0.2f/2+RADIUS*scale, 
					scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.1f, 
					scale*WIDTH+0.1f-RADIUS*scale/2);
			gl.glRotatef(90, 1, 0, 0);
            initLight(gl,1,1,1,1);//����
            light.drawSelf(gl);//����
            closeLight(gl);//�ص�
			gl.glPopMatrix();
		}
		
		if(count%3==1)//�Ƶ�
		{
			gl.glPushMatrix();
			float[] directionParams={0,0,35f,0};
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, directionParams,0);
			initMaterial(gl,1,1,0,1);//��ʼ������
			gl.glTranslatef(scale*RADIUS+scale*CYLINDER_HEIGHT*0.2f/2+3*RADIUS*scale, 
					scale*CYLINDER_HEIGHT-scale*CYLINDER_HEIGHT*0.1f, 
					scale*WIDTH+0.1f-RADIUS*scale/2);
			gl.glRotatef(90, 1, 0, 0);
            initLight(gl,1,1,1,1);//����
            light.drawSelf(gl);//����
            closeLight(gl);//�ص�
			gl.glPopMatrix();
		}
	}
	
	private class DrawBall
	{
		private FloatBuffer   mVertexBuffer;//�����������ݻ���
	    private FloatBuffer mNormalBuffer;//�����������ݻ���
	    int vCount=0;//��������
	    public DrawBall(float scale) 
	    {    	
	    	ArrayList<Float> alVertix=new ArrayList<Float>();//��Ŷ��������ArrayList
	    	
	        for(float vAngle=90;vAngle>30;vAngle=vAngle-ANGLE_SPAN)//��ֱ����angleSpan��һ��
	        {
	        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//ˮƽ����angleSpan��һ��
	        	{
	        		//����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ��ı��ζ�������
	        		//��������������ı��ε�������
	        		
	        		double xozLength=scale*RADIUS*Math.cos(Math.toRadians(vAngle));
	        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
	        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
	        		float y1=(float)(scale*RADIUS*Math.sin(Math.toRadians(vAngle)));
	        		
	        		xozLength=scale*RADIUS*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
	        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
	        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
	        		float y2=(float)(scale*RADIUS*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
	        		
	        		xozLength=scale*RADIUS*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
	        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float y3=(float)(scale*RADIUS*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
	        		
	        		xozLength=scale*RADIUS*Math.cos(Math.toRadians(vAngle));
	        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float y4=(float)(scale*RADIUS*Math.sin(Math.toRadians(vAngle)));   
	        		
	        		//������һ������
	        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
	        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
	        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
	        		//�����ڶ�������
	        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
	        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
	        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
	        		      		
	        	}
	        } 	
	        
	        vCount=alVertix.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
	    	
	        //��alVertix�е�����ֵת�浽һ��int������
	        float vertices[]=new float[vCount*3];
	    	for(int i=0;i<alVertix.size();i++)
	    	{
	    		vertices[i]=alVertix.get(i);
	    	}
	        
	        //�������ƶ������ݻ���
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��     
	                
	        //�������㷨�������ݻ���
	        //vertices.length*4����Ϊһ��float�ĸ��ֽ�
	        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
	        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mNormalBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mNormalBuffer.put(vertices);//�򻺳����з��붥����������
	        mNormalBuffer.position(0);//���û�������ʼλ��	        
	    }

	    public void drawSelf(GL10 gl)
	    {	        
	    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);//ָ�����㻺��
			
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//�򿪷���������
			gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);//ָ������������
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�رջ���
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	    }
	}
	
	private class Cube 
	{
		private FloatBuffer mVertexBuffer;//�����������ݻ���
		private FloatBuffer mTextureBuffer;	//�����������ݻ���
		public float mOffsetX;
		public float mOffsetY;	//�������С
		int vCount;//��������
		public Cube(float scale)
		{
			vCount=30;
			float[] verteices=
			{
					
					//����
					-scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					
					-scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					
					//����
					scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					
					scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					
					//ǰ��
//					-scale*LENGTH,HEIGHT*scale,scale*WIDTH,
//					-scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
//					scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
//					
//					-scale*LENGTH,HEIGHT*scale,scale*WIDTH,
//					scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
//					scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					
					//����
					-scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					
					-scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					
					//����
					-scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					
					-scale*LENGTH,HEIGHT*scale,-scale*WIDTH,
					-scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					-scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					
					//����
					scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					
					scale*LENGTH,HEIGHT*scale,scale*WIDTH,
					scale*LENGTH,-HEIGHT*scale,-scale*WIDTH,
					scale*LENGTH,HEIGHT*scale,-scale*WIDTH
							
			};
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float[] textureCoors=new float[vCount*2];
			for(int i=0;i<vCount/6;i++)//��������������
			{
				textureCoors[i*12]=0.5f;
				textureCoors[(i*12)+1]=0;
				
				textureCoors[(i*12)+2]=0.5f;
				textureCoors[(i*12)+3]=0.75f;
				
				textureCoors[(i*12)+4]=1;
				textureCoors[(i*12)+5]=0.75f;
				
				textureCoors[(i*12)+6]=0.5f;
				textureCoors[(i*12)+7]=0;
				
				textureCoors[(i*12)+8]=1;
				textureCoors[(i*12)+9]=0.75f;
				
				textureCoors[(i*12)+10]=1;
				textureCoors[(i*12)+11]=0f;

			}
			
			ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//���������������ݻ���
			tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mTextureBuffer=tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mTextureBuffer.put(textureCoors);//�򻺳����з��붥����������
			mTextureBuffer.position(0);//���û�������ʼλ��		
		}
		
		public void drawSelf(GL10 gl,int texId)
		{
			gl.glRotatef(mOffsetX, 1, 0, 0);
			gl.glRotatef(mOffsetY, 0, 1, 0);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);//��������
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//����ʹ����������
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//ָ����������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);//������
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�ر���������
			gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
			
		}
	}
	
	private class Texture
	{
		private FloatBuffer mVertexBuffer;//�����������ݻ���
		private FloatBuffer mTextureBuffer;	//�����������ݻ���
		public float mOffsetX;
		public float mOffsetY;	
		int vCount;//��������	
		
		public Texture(float scale)
		{
			vCount=6;
			float [] verteices={
				-scale*LENGTH,scale*HEIGHT,0,
				-scale*LENGTH,-scale*HEIGHT,0,
				scale*LENGTH,-scale*HEIGHT,0,
				
				-scale*LENGTH,scale*HEIGHT,0,
				scale*LENGTH,-scale*HEIGHT,0,
				scale*LENGTH,scale*HEIGHT,0
			};
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float[] textureCoors={
				0,0.75f,0,1,0.75f,1,
				0,0.75f,0.75f,1,0.75f,0.75f
			};
			
			ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//���������������ݻ���
			tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mTextureBuffer=tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mTextureBuffer.put(textureCoors);//�򻺳����з��붥����������
			mTextureBuffer.position(0);//���û�������ʼλ��
			
		}
		
		public void drawSelf(GL10 gl,int texId)
		{
			gl.glRotatef(mOffsetX, 1, 0, 0);
			gl.glRotatef(mOffsetY, 0, 1, 0);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);//��������
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//����ʹ����������
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//ָ����������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);//������
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�ر���������
			gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
			
		}
	}
	
	private class DrawCylinder
	{
		private FloatBuffer myVertexBuffer;//�������껺�� 
		private FloatBuffer myTexture;//������
		int vCount;//��������
		
		public float mAngleX;
		public float mAngleY;
		public float mAngleZ;
		
		public DrawCylinder(float length,float circle_radius,float degreespan,int col)
		{			
			float collength=(float)length/col;//Բ��ÿ����ռ�ĳ���
			int spannum=(int)(360.0f/degreespan);
			
			ArrayList<Float> val=new ArrayList<Float>();//�������б�
			
			for(float circle_degree=360.0f;circle_degree>0.0f;circle_degree-=degreespan)//ѭ����
			{
				for(int j=0;j<col;j++)//ѭ����
				{
					float x1 =(float)(j*collength-length/2);
					float y1=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));
					float z1=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));

					float x2 =(float)(j*collength-length/2);
					float y2=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));
					float z2=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));
					
					float x3 =(float)((j+1)*collength-length/2);
					float y3=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));
					float z3=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));
					
					float x4 =(float)((j+1)*collength-length/2);
					float y4=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree)));
					float z4=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree)));
					
					val.add(x1);val.add(y1);val.add(z1);//���������Σ���6�����������
					val.add(x2);val.add(y2);val.add(z2);
					val.add(x4);val.add(y4);val.add(z4);
					
					val.add(x2);val.add(y2);val.add(z2);
					val.add(x3);val.add(y3);val.add(z3);
					val.add(x4);val.add(y4);val.add(z4);
				}
			}
			 
			vCount=val.size()/3;//ȷ����������
			
			//����
			float[] vertexs=new float[vCount*3];
			for(int i=0;i<vCount*3;i++)
			{
				vertexs[i]=val.get(i);
			}
			ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);
			vbb.order(ByteOrder.nativeOrder());
			myVertexBuffer=vbb.asFloatBuffer();
			myVertexBuffer.put(vertexs);
			myVertexBuffer.position(0);
			
			//����
			float[] textures=generateTexCoor(col,spannum);
			ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
			tbb.order(ByteOrder.nativeOrder());
			myTexture=tbb.asFloatBuffer();
			myTexture.put(textures);
			myTexture.position(0);
		}
		
		public void drawSelf(GL10 gl,int textureId)
		{
			gl.glRotatef(mAngleX, 1, 0, 0);//��ת
			gl.glRotatef(mAngleY, 0, 1, 0);
			gl.glRotatef(mAngleZ, 0, 0, 1);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//ָ�����㻺��
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�رջ���
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		
		
		//�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizeh=0.5f/bh;//����
	    	float sizew=0.125f/bw;//����
	    	int c=0;
	    	for(int i=0;i<bh;i++)
	    	{
	    		for(int j=0;j<bw;j++)
	    		{
	    			//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
	    			float t=i*sizeh;
	    			float s=j*sizew;    	
	    			
	    			result[c++]=s;
	    			result[c++]=t;
	    		 
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			   			 
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t+sizeh;   
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    		}
	    	}
	    	return result;
	    }
	}
	
	//��ʼ����
	private void initLight(GL10 gl,float r,float g,float b,float a)
	{    
        gl.glEnable(GL10.GL_LIGHTING);//�������    
        gl.glEnable(GL10.GL_LIGHT0);//��0�ŵ�  
        
        //����������
        float[] ambientParams={1f*r,1f*g,1f*b,1.0f*a};//����� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            
        
        //ɢ�������
        float[] diffuseParams={1f*r,1f*g,1f*b,1.0f*a};//����� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
        
        //���������
        float[] specularParams={0f*r,0f*g,0f*b,1.0f*a};//����� RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0); 
	}
	
	//�رյ�
	private void closeLight(GL10 gl)
	{
		gl.glDisable(GL10.GL_LIGHT0);
		gl.glDisable(GL10.GL_LIGHTING);
	}
	
	//��ʼ������
	private void initMaterial(GL10 gl,float r,float g,float b,float a)
	{
        //������
        float ambientMaterial[] = {1*r, 1*g, 1*b, 1.0f*a};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);
        //ɢ���
        float diffuseMaterial[] = {1*r, 1*g, 1*b, 1.0f*a};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);
        //�߹����
        float specularMaterial[] = {0.5f*r, 0.5f*g, 0.5f*b, 1.0f*a};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 100.0f);
	}
	
	class LightTurn extends Thread
	{
		public LightTurn()
		{		 
		}
		
		@Override
		public void run()  
		{ 
			while(flag) 
			{ 
				try{
					sleep(2400);
					count+=1;
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				if(count>1)
				{					
					count=2;
					flag=false;//��ͨ��ֹͣ����
				}
				
			}
		}
	}
}
