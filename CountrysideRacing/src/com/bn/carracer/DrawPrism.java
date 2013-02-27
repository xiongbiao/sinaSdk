package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DrawPrism extends BNShape
{		
	Prism prism;//����
	Cylinder cylinder;//Բ��
	
	float a=1;//��������߳�
	float height=2;//�����߶�
	float cylinder_R=0.1f;//Բ������뾶
	float cylinder_H=1;//Բ���߶�
	
    public float mAngleX;//��x����ת�Ƕ�
    public float mAngleY;//��y����ת�Ƕ� 
    public float mAngleZ;//��z����ת�Ƕ� 

	public DrawPrism(float scale) {
		super(scale);
		prism=new Prism(scale);
		cylinder=new Cylinder(scale,1,0.1f,45f,1);
	}

	@Override
	public void drawSelf(GL10 gl, int texId, int number) {		
    	gl.glRotatef(mAngleZ, 0, 0, 1);//��Z����ת    	
        gl.glRotatef(mAngleY, 0, 1, 0);//��Y����ת
        gl.glRotatef(mAngleX, 1, 0, 0);//��X����ת
        
        gl.glPushMatrix();
        gl.glTranslatef(0, scale*(cylinder_H+height/2), 0);
        
		gl.glPushMatrix();
		prism.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0, -scale*(height/2+cylinder_H/2), 0); 
		gl.glRotatef(90, 0, 0, 1);
		cylinder.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
	}
	
	
	private class Prism
	{
		FloatBuffer vertexBuffer;
		FloatBuffer textureBuffer;
		
		int vCount;
		
		public Prism(float scale)
		{
			float b=(float) Math.sqrt(3);
			
			ArrayList<Float> alVertex=new ArrayList<Float>();//��Ŷ�������
			
			float x1=scale*(-a/2);
			float y1=scale*(height/2);
			float z1=scale*(a/2/b);
			
			float x2=scale*(a/2);
			float y2=scale*(height/2);
			float z2=scale*(a/2/b);
			
			float x3=0;
			float y3=scale*(height/2);
			float z3=scale*(-a/b);
			
			float x4=scale*(-a/2);
			float y4=scale*(-height/2);
			float z4=scale*(a/2/b);
			
			float x5=scale*(a/2);
			float y5=scale*(-height/2);
			float z5=scale*(a/2/b);
			
			float x6=0;
			float y6=scale*(-height/2);
			float z6=scale*(-a/b);
			
			alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
			alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
			alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
						
			alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
			alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
			alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
			
			alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
			alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
			alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
			
			alVertex.add(x5);alVertex.add(y5);alVertex.add(z5);
			alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
			alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
			
			alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
			alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
			alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
			
			alVertex.add(x6);alVertex.add(y6);alVertex.add(z6);
			alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
			alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
			
			vCount=alVertex.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
	    	
	        //��alVertix�е�����ֵת�浽һ��int������
	        float[] vertices=new float[vCount*3];
	    	for(int i=0;i<alVertex.size();i++)
	    	{
	    		vertices[i]=alVertex.get(i);
	    	}
	        //���������������ݻ���
	        //vertices.length*4����Ϊһ�������ĸ��ֽ�
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        vertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        vertexBuffer.put(vertices);//�򻺳����з��붥����������
	        vertexBuffer.position(0);//���û�������ʼλ��
	        
			float[] textures=new float[]{			
					1,0.5f,
					0,0.5f,
					1,1,
					
					0,0.5f,
					0,1,
					1,1,
					
					1,0.5f,
					0,0.5f,
					1,1,
					
					0,0.5f,
					0,1,
					1,1,
					
					1,0.5f,
					0,0.5f,
					1,1,
					
					0,0.5f,
					0,1,
					1,1
				};
			ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
			tbb.order(ByteOrder.nativeOrder());
			textureBuffer=tbb.asFloatBuffer();
			textureBuffer.put(textures);
			textureBuffer.position(0);
		}
		
		public void drawSelf(GL10 gl,int texId) {
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//������������
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,vertexBuffer);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);//��������
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//������������
			gl.glTexCoordPointer(2,GL10.GL_FLOAT, 0, textureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);//������
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
			
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�ر���������	
			gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}	
	
	//Բ���ڲ���
	private class Cylinder
	{
		private FloatBuffer myVertexBuffer;//�������껺�� 
		private FloatBuffer myTexture;//������
		
		int vCount;//��������
		
		public Cylinder(float scale,float length,float circle_radius,float degreespan,int col)
		{			
			float collength=(float)length*scale/col;//Բ��ÿ����ռ�ĳ���
			int spannum=(int)(360.0f/degreespan);
			
			ArrayList<Float> val=new ArrayList<Float>();//�������б�
			
			for(float circle_degree=360.0f;circle_degree>0.0f;circle_degree-=degreespan)//ѭ����
			{
				for(int j=0;j<col;j++)//ѭ����
				{
					float x1 =(float)(j*collength-length/2*scale);
					float y1=(float) (circle_radius*scale*Math.sin(Math.toRadians(circle_degree)));
					float z1=(float) (circle_radius*scale*Math.cos(Math.toRadians(circle_degree)));		
					
					float x2 =(float)(j*collength-length/2*scale);
					float y2=(float) (circle_radius*scale*Math.sin(Math.toRadians(circle_degree-degreespan)));
					float z2=(float) (circle_radius*scale*Math.cos(Math.toRadians(circle_degree-degreespan)));					
							
					float x3 =(float)((j+1)*collength-length/2*scale);
					float y3=(float) (circle_radius*scale*Math.sin(Math.toRadians(circle_degree-degreespan)));
					float z3=(float) (circle_radius*scale*Math.cos(Math.toRadians(circle_degree-degreespan)));
					
					float x4 =(float)((j+1)*collength-length/2*scale);
					float y4=(float) (circle_radius*scale*Math.sin(Math.toRadians(circle_degree)));
					float z4=(float) (circle_radius*scale*Math.cos(Math.toRadians(circle_degree)));							
					
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
		
		public void drawSelf(GL10 gl,int texId)
		{			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//ָ�����㻺��
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
			
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
	    	float sizeh=0.5f/bh;//����
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