package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Obstacle extends BNShape{

	Rect rect;//�����������
	Cylinder cylinder;//Բ����
	static float yOffset;//����Y���ƶ�����
	static float xOffset;//Բ��X���ƶ�����
	static float zOffset;//Բ��Z����ƶ�����
	static float tempYOffset;//�������ƶ���xozƽ������
	static float yAngle;//��Y��ת���ĽǶ�
	static float OffsetY;//�ϰ�����Y���ϵ�λ��
	static float OffsetZ;//�ϰ�����Z���ϵ�λ�� 
	public Obstacle(float scale) {
		super(scale);
		// TODO Auto-generated constructor stub
		rect=new Rect(scale);
		cylinder=new Cylinder(scale);
		yOffset=(float) (rect.width*scale+cylinder.length/2*Math.sin(Math.toRadians(60)));
		xOffset=rect.length*scale-cylinder.circle_radius;
		zOffset=(float) (cylinder.length/2*Math.cos(Math.toRadians(60)));
		tempYOffset=(float) (cylinder.length/2*Math.sin(Math.toRadians(60)));
		Log.d("yOffset", yOffset+"");
	}

	@Override
	public void drawSelf(GL10 gl, int texId, int number) {
		// TODO Auto-generated method stub
		gl.glPushMatrix();
		
		gl.glTranslatef(0, -yOffset, 0);//�ƶ����ĵ�
		gl.glPushMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0, yOffset+tempYOffset, 0);
		rect.drawSelf(gl, texId);//�����������
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0, yOffset+tempYOffset, 0);
		gl.glRotatef(180, 0, 1, 0);
		rect.drawSelf(gl, texId);//�����������
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, tempYOffset, zOffset);//�Ҳ��Բ����
		gl.glRotatef(90, 0, 0, 1);
		gl.glRotatef(30, 0, 1, 0);
		cylinder.drawSelf(gl, texId);//����Բ����
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(xOffset, tempYOffset, -zOffset);
		gl.glRotatef(90, 0, 0, 1);
		gl.glRotatef(-30, 0, 1, 0);
		cylinder.drawSelf(gl, texId);//����Բ����
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-xOffset, tempYOffset, zOffset);//����Բ����
		gl.glRotatef(90, 0, 0, 1);
		gl.glRotatef(30, 0, 1, 0);
		cylinder.drawSelf(gl, texId);//����Բ����
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(-xOffset, tempYOffset, -zOffset);
		gl.glRotatef(90, 0, 0, 1);
		gl.glRotatef(-30, 0, 1, 0);
		cylinder.drawSelf(gl, texId);//����Բ����
		gl.glPopMatrix();
		gl.glPopMatrix();
		
		gl.glPopMatrix();
		
	}
	
	private class Rect
	{
		private FloatBuffer mVertexBuffer;//�����������ݻ���
		private FloatBuffer mTextureBuffer;//�����������ݻ���
		float width;//���
		float length;//����
		int vCount;//��������
		public Rect(float scale)
		{
			vCount=6;
			
			width=1f;//���
			length=2.0f;//����
			float verteices[]=
			{
					-length*scale,width*scale,0,
					-length*scale,-width*scale,0,
					length*scale,-width*scale,0,
					
					length*scale,-width*scale,0,
					length*scale,width*scale,0,
					-length*scale,width*scale,0
			};
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float textureCoors[]=
			{
					0,0,0,1,1,1,
					1,1,1,0,0,0
			};
			ByteBuffer tbb=ByteBuffer.allocateDirect(textureCoors.length*4);//���������������ݻ���
			tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mTextureBuffer=tbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mTextureBuffer.put(textureCoors);//�򻺳����з��붥������
			mTextureBuffer.position(0);//���û�������ʼλ��
		}
		
		public void drawSelf(GL10 gl,int textureId)
		{
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//����ʹ�ö�������
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);//ָ���������ݻ���
			
			gl.glEnable(GL10.GL_TEXTURE_2D);//����ʹ������
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//����ʹ����������
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);//ָ���������ݻ���
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);//����������
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//�������η�ʽ���ƾ���
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�ر���������
			gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);//�رն�������
		}
	}
	
	private class Cylinder
	{
		private FloatBuffer mVertexBuffer;//�������껺�� 
		private FloatBuffer mTextureBuffer;//������	
		int vCount;//��������
		float length;//Բ������
		float circle_radius;//Բ�ػ��뾶
		float degreespan;  //Բ�ػ�ÿһ�ݵĶ�����С
		int col;//Բ������
		public Cylinder(float scale)
		{
			length=2.0f*scale;
			circle_radius=0.5f;
			degreespan=18f;
			col=1;
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
									
					float x3=(float)((j+1)*collength-length/2);
					float y3=(float) (circle_radius*Math.sin(Math.toRadians(circle_degree-degreespan)));
					float z3=(float) (circle_radius*Math.cos(Math.toRadians(circle_degree-degreespan)));
					
					float x4=(float)((j+1)*collength-length/2);
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
			mVertexBuffer=vbb.asFloatBuffer();
			mVertexBuffer.put(vertexs);
			mVertexBuffer.position(0);
			
			//����
			float[] textures=generateTexCoor(col,spannum);
			ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
			tbb.order(ByteOrder.nativeOrder());
			mTextureBuffer=tbb.asFloatBuffer();
			mTextureBuffer.put(textures);
			mTextureBuffer.position(0);
		}
		
		public void drawSelf(GL10 gl,int textureId)
		{
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);//ָ�����㻺��
					
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�رջ���
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		//�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=1f/bw;//����
	    	float sizeh=0.03f/bh;//����
	    	int c=0;
	    	for(int i=0;i<bh;i++)
	    	{
	    		for(int j=0;j<bw;j++)
	    		{
	    			//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
	    			float s=j*sizew;
	    			float t=i*sizeh;
	    			
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
}
