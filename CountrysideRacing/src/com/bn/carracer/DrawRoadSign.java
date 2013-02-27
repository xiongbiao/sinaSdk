package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
class DrawRoadSign extends BNShape {
	
	public final static float ROADSIGN_WIDTH=30f;//��ͨ�Ƴ���
	public final static float ROADSIGN_HEIGHT=15f;//��ͨ�Ƹ߶�
	
	public final static float CYLINDER_HEIGHT=50;//֧��
	public final static float CYLINDER_R=1.5f;//�뾶
	public final static float DEGREESPAN=18;//����
	public final static int COL=1;//����
	
	float radiusH=0.5f;//С��ܵİ뾶
	float length=10;//С��ܵĳ���
	float height1=CYLINDER_HEIGHT-2-4/15*ROADSIGN_HEIGHT;
	float heitht2=height1-7/15*ROADSIGN_HEIGHT;
	Sign sign;//��ͨ��
	Cylinder cylinder;//֧��
	Cylinder cylinderH;//���
	float yAngle;
	public DrawRoadSign(float scale) {
		super(scale);
		
		sign=new Sign(scale);
		cylinder=new Cylinder(scale,CYLINDER_HEIGHT,CYLINDER_R,DEGREESPAN,COL);//�������߶ȣ��뾶������������
		cylinderH=new Cylinder(scale,length,radiusH,DEGREESPAN,COL);
	}
	@Override
	public void drawSelf(GL10 gl, int texId,int number) {
		gl.glRotatef(yAngle, 0, 1, 0);
		//���ƽ�ͨ��
		gl.glPushMatrix();
		gl.glTranslatef(-(ROADSIGN_WIDTH/2+(length-0.5f))*scale,(CYLINDER_HEIGHT-ROADSIGN_HEIGHT/2-2)*scale,0);//-2��ʾ���ӵ���֧��
		sign.drawSelf(gl, texId,number);
		gl.glPopMatrix();
		//��ͨ�Ʊ���
		gl.glPushMatrix();
		gl.glTranslatef(-(ROADSIGN_WIDTH/2+(length-0.5f))*scale,(CYLINDER_HEIGHT-ROADSIGN_HEIGHT/2-2)*scale,0);
		gl.glRotatef(180, 0, 1, 0);//��ת180��
		sign.drawSelf(gl, texId,number);
		gl.glPopMatrix();
		//����֧��
		gl.glPushMatrix();
		gl.glTranslatef(0, CYLINDER_HEIGHT/2*scale, 0);
		gl.glRotatef(90, 0, 0, 1);
		cylinder.drawSelf(gl, texId);
		gl.glPopMatrix();
		//���ƺ��1
		gl.glPushMatrix();
		gl.glTranslatef(-(length/2-0.5f)*scale, heitht2*scale, 0);
		cylinderH.drawSelf(gl, texId);
		gl.glPopMatrix();
		//���ƺ��2
		gl.glPushMatrix();
		gl.glTranslatef(-(length/2-0.5f)*scale, height1*scale, 0);
		cylinderH.drawSelf(gl, texId);
		gl.glPopMatrix();
	}
	private class Sign{
		private FloatBuffer vertexBuffer;//����Buffer
		private FloatBuffer[] textureBuffer;//��������Buffer
		private int vCount=0;//������
		public Sign(float scale) {
			float[]vertice=new float[]{//��Ŷ������������
										-ROADSIGN_WIDTH/2*scale,ROADSIGN_HEIGHT/2*scale,0,//1
										-ROADSIGN_WIDTH/2*scale,-ROADSIGN_HEIGHT/2*scale,0,//2
										ROADSIGN_WIDTH/2*scale,ROADSIGN_HEIGHT/2*scale,0,//4
										
										ROADSIGN_WIDTH/2*scale,ROADSIGN_HEIGHT/2*scale,0,//4
										-ROADSIGN_WIDTH/2*scale,-ROADSIGN_HEIGHT/2*scale,0,//2
										ROADSIGN_WIDTH/2*scale,-ROADSIGN_HEIGHT/2*scale,0,//3
										
										};
			vCount=vertice.length/3;//��������

			ByteBuffer vbb=ByteBuffer.allocateDirect(vertice.length*4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer=vbb.asFloatBuffer();
			vertexBuffer.put(vertice);
			vertexBuffer.position(0);
			
			float[][]textures=new float[][]{//��������		
					{//��ת��־
					 0,0.26f,
					 0,0.5f,
					 0.5f,0.26f,
					 
					 0.5f,0.26f,
					 0,0.5f,
					 0.5f,0.5f
					},
					{//��ת��־
					 0.5f,0.26f,
					 0.5f,0.5f,
					 1,0.26f,
					 
					 1,0.26f,
					 0.5f,0.5f,
					 1,0.5f
					},
					{//ֱ�б�־
					 0,0.5f,
					 0,0.75f,
					 0.5f,0.5f,
					 
					 0.5f,0.5f,
					 0,0.75f,
					 0.5f,0.75f
					},
					{//��Ƽ�
					 0.5f,0.5f,
					 0.5f,0.75f,
					 1,0.5f,
					 
					 1,0.5f,
					 0.5f,0.75f,
					 1,0.75f
					},
					{//�����
					 0,0.75f,
					 0,1,
					 0.5f,0.75f,
					 
					 0.5f,0.75f,
					 0,1,
					 0.5f,1
					},
					{//����ʯ
					0.5f,0.75f,
					0.5f,1,
					1,0.75f,
					
					1,0.75f,
					0.5f,1,
					1,1
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
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
		
	    //�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=1f/bw;//����
	    	float sizeh=0.125f/bh;//����
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

