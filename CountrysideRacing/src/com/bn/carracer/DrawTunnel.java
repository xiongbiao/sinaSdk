package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class DrawTunnel extends BNShape
{
	//�����峤������
	private static final float LENGTH=123.94f;//������һ��
	private static final float WIDTH=542f;//�������һ��
	private static final float HEIGHT=1f;//�������
	private static final float TOP_HEIGHT=45f;//����߶�
	
	private static final float COL=1;//����  
	private static final float COLUMN=25;//����
	
	//�����������ת���Ƕ�
	public float mAngleX;
	public float mAngleY;
	public float mAngleZ; 
	
	Cube left;
	Cube right;
	Cube top;
	Texture leftFace;
	Texture rightFace;
	TextureForTop topFace;
	
	public DrawTunnel(float scale) {
		super(scale);
		
		top=new Cube(scale,LENGTH,WIDTH,HEIGHT);
		right=new Cube(scale,TOP_HEIGHT,WIDTH,HEIGHT);
		left=new Cube(scale,TOP_HEIGHT,WIDTH,HEIGHT);
		leftFace=new Texture(scale,TOP_HEIGHT,WIDTH);
		rightFace=new Texture(scale,TOP_HEIGHT,WIDTH);
		topFace=new TextureForTop(scale,LENGTH,WIDTH);
	}

	@Override
	public void drawSelf(GL10 gl, int texId, int number) {
		gl.glRotatef(mAngleX, 1, 0, 0);//��ת
		gl.glRotatef(mAngleY, 0, 1, 0);
		gl.glRotatef(mAngleZ, 0, 0, 1);
//		
//		gl.glTranslatef(0, 0, 0);
		
		gl.glPushMatrix();
		gl.glTranslatef(0, scale*(2*TOP_HEIGHT+HEIGHT), 0);
		top.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0, scale*(2*TOP_HEIGHT), 0);
		gl.glRotatef(180, 0, 0, 1);
		topFace.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*(-LENGTH+HEIGHT), scale*TOP_HEIGHT, 0);
		gl.glRotatef(90, 0, 0, 1);
		left.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*(-LENGTH+2*HEIGHT), scale*TOP_HEIGHT, 0);
		gl.glRotatef(-90, 0, 0, 1);
		leftFace.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(scale*(LENGTH-HEIGHT), scale*TOP_HEIGHT, 0);
		gl.glRotatef(-90, 0, 0, 1);
		right.drawSelf(gl, texId);
		gl.glPopMatrix();
		
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glPushMatrix();
		gl.glTranslatef(scale*(LENGTH-2*HEIGHT), scale*TOP_HEIGHT, 0);
		gl.glRotatef(90, 0, 0, 1);
		gl.glRotatef(180, 0, 1, 0);
		rightFace.drawSelf(gl, texId);
		gl.glPopMatrix();
		gl.glEnable(GL10.GL_CULL_FACE);
	}
	
	private class Cube {
		private FloatBuffer mVertexBuffer;//�����������ݻ���
		private FloatBuffer mTextureBuffer;	//�����������ݻ���
		public float mOffsetX;
		public float mOffsetY;	//�������С
		int vCount;//��������
		public Cube(float scale,float LENGTH,float WIDTH,float HEIGHT)
		{
			vCount=30;
			float[] verteices=
			{
					
					//����
					-scale*LENGTH,HEIGHT,-WIDTH,
					-scale*LENGTH,HEIGHT,WIDTH,
					scale*LENGTH,HEIGHT,WIDTH,
					
					-scale*LENGTH,HEIGHT,-WIDTH,
					scale*LENGTH,HEIGHT,WIDTH,
					scale*LENGTH,HEIGHT,-WIDTH,
					
					//����
					scale*LENGTH,HEIGHT,-WIDTH,
					scale*LENGTH,-HEIGHT,-WIDTH,
					-scale*LENGTH,-HEIGHT,-WIDTH,
					
					scale*LENGTH,HEIGHT,-WIDTH,
					-scale*LENGTH,-HEIGHT,-WIDTH,
					-scale*LENGTH,HEIGHT,-WIDTH,
					
					//ǰ��
					-scale*LENGTH,HEIGHT,WIDTH,
					-scale*LENGTH,-HEIGHT,WIDTH,
					scale*LENGTH,-HEIGHT,WIDTH,
					
					-scale*LENGTH,HEIGHT,WIDTH,
					scale*LENGTH,-HEIGHT,WIDTH,
					scale*LENGTH,HEIGHT,WIDTH,
					
					//����
					-scale*LENGTH,-HEIGHT,-WIDTH,
					-scale*LENGTH,-HEIGHT,-WIDTH,
					-scale*LENGTH,-HEIGHT,WIDTH,
					
					-scale*LENGTH,HEIGHT,-WIDTH,
					-scale*LENGTH,-HEIGHT,WIDTH,
					-scale*LENGTH,HEIGHT,WIDTH,
					
					//����
					scale*LENGTH,HEIGHT,WIDTH,
					scale*LENGTH,-HEIGHT,WIDTH,
					scale*LENGTH,-HEIGHT,-WIDTH,
					
					scale*LENGTH,HEIGHT,WIDTH,
					scale*LENGTH,-HEIGHT,-WIDTH,
					scale*LENGTH,HEIGHT,-WIDTH
							
			};
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float[] textureCoors=new float[vCount*2];
			for(int i=0;i<vCount/6;i++)//��������������
			{
				textureCoors[i*12]=0;
				textureCoors[(i*12)+1]=0;
				
				textureCoors[(i*12)+2]=0;
				textureCoors[(i*12)+3]=0.125f;
				
				textureCoors[(i*12)+4]=0.125f;
				textureCoors[(i*12)+5]=0.125f;
				
				textureCoors[(i*12)+6]=0;
				textureCoors[(i*12)+7]=0;
				
				textureCoors[(i*12)+8]=0.125f;
				textureCoors[(i*12)+9]=0.125f;
				
				textureCoors[(i*12)+10]=0.125f;
				textureCoors[(i*12)+11]=0;

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
		
		public Texture(float scale,float LENGTH,float WIDTH)
		{
			vCount=150;
			ArrayList<Float> al=new ArrayList<Float>();
			
			for(int i=0;i<COL;i++)
			{
				for(int j=0;j<COLUMN;j++)
				{
					float x1=-scale*LENGTH+2*i*scale*LENGTH/COL;
					float y1=0;
					float z1=-WIDTH+2*j*WIDTH/COLUMN;
					
					float x2=-scale*LENGTH+2*i*scale*LENGTH/COL;
					float y2=0;
					float z2=-WIDTH+2*(j+1)*WIDTH/COLUMN;
					
					float x3=-scale*LENGTH+2*(i+1)*scale*LENGTH/COL;
					float y3=0;
					float z3=-WIDTH+2*j*WIDTH/COLUMN;
					
					float x4=-scale*LENGTH+2*(i+1)*scale*LENGTH/COL;
					float y4=0;
					float z4=-WIDTH+2*(j+1)*WIDTH/COLUMN;
					
					al.add(x1);al.add(y1);al.add(z1);
					al.add(x2);al.add(y2);al.add(z2);
					al.add(x4);al.add(y4);al.add(z4);
					
					al.add(x1);al.add(y1);al.add(z1);
					al.add(x4);al.add(y4);al.add(z4);
					al.add(x3);al.add(y3);al.add(z3);
				}
			}
			
			float [] verteices=new float[vCount*3];
			for(int i=0;i<al.size();i++)
			{
				verteices[i]=al.get(i);
			}
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float[] textureCoors=new float[vCount*2];
			for(int i=0;i<vCount/6;i++)//��������������
			{
				textureCoors[i*12]=0;
				textureCoors[(i*12)+1]=0.5f;
				
				textureCoors[(i*12)+2]=0;
				textureCoors[(i*12)+3]=1;
				
				textureCoors[(i*12)+4]=1;
				textureCoors[(i*12)+5]=1;
				
				textureCoors[(i*12)+6]=0;
				textureCoors[(i*12)+7]=0.5f;
				
				textureCoors[(i*12)+8]=1;
				textureCoors[(i*12)+9]=1;
				
				textureCoors[(i*12)+10]=1;
				textureCoors[(i*12)+11]=0.5f;

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
	
	private class TextureForTop
	{
		private FloatBuffer mVertexBuffer;//�����������ݻ���
		private FloatBuffer mTextureBuffer;	//�����������ݻ���
		public float mOffsetX;
		public float mOffsetY;	
		int vCount;//��������	
		
		public TextureForTop(float scale,float LENGTH,float WIDTH)
		{
			vCount=150;
			ArrayList<Float> al=new ArrayList<Float>();
			
			for(int i=0;i<COL;i++)
			{
				for(int j=0;j<COLUMN;j++)
				{
					float x1=-scale*LENGTH+2*i*scale*LENGTH/COL;
					float y1=0;
					float z1=-WIDTH+2*j*WIDTH/COLUMN;
					
					float x2=-scale*LENGTH+2*i*scale*LENGTH/COL;
					float y2=0;
					float z2=-WIDTH+2*(j+1)*WIDTH/COLUMN;
					
					float x3=-scale*LENGTH+2*(i+1)*scale*LENGTH/COL;
					float y3=0;
					float z3=-WIDTH+2*j*WIDTH/COLUMN;
					
					float x4=-scale*LENGTH+2*(i+1)*scale*LENGTH/COL;
					float y4=0;
					float z4=-WIDTH+2*(j+1)*WIDTH/COLUMN;
					
					al.add(x1);al.add(y1);al.add(z1);
					al.add(x2);al.add(y2);al.add(z2);
					al.add(x4);al.add(y4);al.add(z4);
					
					al.add(x1);al.add(y1);al.add(z1);
					al.add(x4);al.add(y4);al.add(z4);
					al.add(x3);al.add(y3);al.add(z3);
				}
			}
			
			float [] verteices=new float[vCount*3];
			for(int i=0;i<al.size();i++)
			{
				verteices[i]=al.get(i);
			}
			
			ByteBuffer vbb=ByteBuffer.allocateDirect(verteices.length*4); //���������������ݻ���
			vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
			mVertexBuffer=vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
			mVertexBuffer.put(verteices);//�򻺳����з��붥����������
			mVertexBuffer.position(0);//���û�������ʼλ��
			
			float[] textureCoors=new float[vCount*2];
			for(int i=0;i<vCount/6;i++)//��������������
			{
				textureCoors[i*12]=0.125f;
				textureCoors[(i*12)+1]=0;
				
				textureCoors[(i*12)+2]=0.125f;
				textureCoors[(i*12)+3]=0.5f;
				
				textureCoors[(i*12)+4]=1;
				textureCoors[(i*12)+5]=0.5f;
				
				textureCoors[(i*12)+6]=0.125f;
				textureCoors[(i*12)+7]=0;
				
				textureCoors[(i*12)+8]=1;
				textureCoors[(i*12)+9]=0.5f;
				
				textureCoors[(i*12)+10]=1;
				textureCoors[(i*12)+11]=0;

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
	
}
