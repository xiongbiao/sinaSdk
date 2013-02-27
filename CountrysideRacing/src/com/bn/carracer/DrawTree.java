package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

class DrawTree extends BNShape{
	public final static float TREE_WIDTH=20f;//������
	public final static float TREE_HEIGHT=20f;//���߶�
	Tree tree;
	float direction=0;//Tree �ĳ���
	public DrawTree(float scale){
		super(scale);
		tree=new Tree(scale);
	}
	public void calculateDirection(float xOffset,float zOffset,float cx,float cz)
	{//���������λ�ü���������
		float xspan=xOffset-cx;
		float zspan=zOffset-cz;
		if(zspan<=0)
		{
			direction=(float)Math.toDegrees(Math.atan(xspan/zspan));	
		}
		else
		{ 
			direction=180+(float)Math.toDegrees(Math.atan(xspan/zspan));
		}
	} 
	@Override
	public void drawSelf(GL10 gl, int texId,int number) {
		
		gl.glPushMatrix();
		gl.glTranslatef(0, scale*TREE_WIDTH/2, 0);//TREE_HEIGHT/2*scale
		gl.glRotatef(direction, 0, 1, 0);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		tree.drawSelf(gl, texId,number);
		gl.glDisable(GL10.GL_BLEND);
		gl.glPopMatrix();	
	}
	private class Tree{
		private FloatBuffer vertexBuffer;//����Buffer
		private FloatBuffer[] textureBuffer;//��������Buffer
		private int vCount=0;//������
		public Tree(float scale) {
			float[]vertice=new float[]{//��Ŷ������������
										-TREE_WIDTH/2*scale,TREE_WIDTH/2*scale,0,//1
										-TREE_WIDTH/2*scale,-TREE_WIDTH/2*scale,0,//2
										TREE_WIDTH/2*scale,TREE_WIDTH/2*scale,0,//4
					
										TREE_WIDTH/2*scale,TREE_WIDTH/2*scale,0,//4
										-TREE_WIDTH/2*scale,-TREE_WIDTH/2*scale,0,//2
										TREE_WIDTH/2*scale,-TREE_WIDTH/2*scale,0,//3
										};
			vCount=vertice.length/3;//��������

			ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer=vbb.asFloatBuffer();
			vertexBuffer.put(vertice);
			vertexBuffer.position(0);
			float[][]textures=new float[][]{//������������
					{//��һ�������������� 
					 0,			0,
					 0,			0.7343f,
					 0.1484f,	0,
					 
					 0.1484f,	0,
					 0,			0.7343f,
					 0.1484f,	0.7343f
					},
					{//�ڶ���������������
					 0.1484f,	0,
					 0.1484f,	0.7343f,
					 0.2968f,	0,
					 
					 0.2968f,	0,
					 0.1484f,	0.7343f,
					 0.2968f,	0.7343f
					},
					{//������������������
					 0.2972f,	0,
					 0.2972f,	0.7343f,
					 0.4453f,	0,
					 
					 0.4453f,	0,
					 0.2972f,	0.7343f,
					 0.4453f,	0.7343f
					},
					{//���Ŀ�������������
					 0.4453f,	0,
					 0.4453f,	0.7343f,
					 0.5933f,	0,
					 
					 0.5933f,	0,
					 0.4453f,	0.7343f,
					 0.5933f,	0.7343f
					},
					{//�����������������
					 0.5937f,	0,
					 0.5937f,	0.7343f,
					 0.7422f,	0,
					 
					 0.7422f,	0,
					 0.5937f,	0.7343f,
					 0.7422f,	0.7343f
					},
					{//������������������
					 0.7424f,	0,
					 0.7424f,	0.7343f,
					 0.8906f,	0,
					 
					 0.8906f,	0,
					 0.7424f,	0.7343f,
					 0.8906f,	0.7343f,
					}					
			};
			textureBuffer=new FloatBuffer[6];
			for(int i=0;i<textures.length;i++)
			{
				ByteBuffer tbb=ByteBuffer.allocateDirect(textures[i].length*4);
				tbb.order(ByteOrder.nativeOrder());
				textureBuffer[i]=tbb.asFloatBuffer();
				textureBuffer[i].put(textures[i]);
				textureBuffer[i].position(0);
			}
		}
		public void drawSelf(GL10 gl,int texId,int number) {
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//������������
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0,vertexBuffer);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);//��������
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//������������
			gl.glTexCoordPointer(2,GL10.GL_FLOAT, 0, textureBuffer[number]);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);//������
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);
			
			gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�ر���������
		}
	}
}
