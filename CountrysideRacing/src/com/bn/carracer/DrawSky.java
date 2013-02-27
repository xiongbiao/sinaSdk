package com.bn.carracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class DrawSky extends BNShape{

	Ball ball;
	public DrawSky(float scale) {
		super(scale);
		ball=new Ball(scale,1000);
	}

	@Override
	public void drawSelf(GL10 gl, int texId, int number) {
		gl.glPushMatrix();
		ball.drawSelf(gl, texId);
		gl.glPopMatrix();
	}
	private class Ball {

		private FloatBuffer   mVertexBuffer;//�����������ݻ���
	    private FloatBuffer mTextureBuffer;//�����������ݻ���
	    int vCount=0;//��������
	    public Ball(float scale,float radius) 
	    {
	    	float ANGLE_SPAN=5f;
	    	float angleV=45;
	    	//��ȡ�з���ͼ����������
	    	float[] texCoorArray= 
	         generateTexCoor
	    	 (
	    			 (int)(360/ANGLE_SPAN), //����ͼ�зֵ�����
	    			 (int)(angleV/ANGLE_SPAN)  //����ͼ�зֵ�����
	    	);
	        int tc=0;//�������������
	        int ts=texCoorArray.length;//�������鳤��
	    	
	    	ArrayList<Float> alVertix=new ArrayList<Float>();//��Ŷ��������ArrayList
	    	ArrayList<Float> alTexture=new ArrayList<Float>();//������������ArrayList
	    	
	        for(float vAngle=angleV+5;vAngle>5;vAngle=vAngle-ANGLE_SPAN)//��ֱ����angleSpan��һ��
	        {
	        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//ˮƽ����angleSpan��һ��
	        	{
	        		//����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ��ı��ζ�������
	        		//��������������ı��ε�������
	        		
	        		double xozLength=scale*radius*Math.cos(Math.toRadians(vAngle));
	        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
	        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
	        		float y1=(float)(scale*radius*Math.sin(Math.toRadians(vAngle)));
	        		
	        		xozLength=scale*radius*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
	        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
	        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
	        		float y2=(float)(scale*radius*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
	        		
	        		xozLength=scale*radius*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
	        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float y3=(float)(scale*radius*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
	        		
	        		xozLength=scale*radius*Math.cos(Math.toRadians(vAngle));
	        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
	        		float y4=(float)(scale*radius*Math.sin(Math.toRadians(vAngle)));   
	        		
	        		//������һ������
	        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
	        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4); 
	        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
	        		       		
	        		//�����ڶ�������
	        		
	        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
	        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
	        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
	        		
	        		//��һ������3�������6����������
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);        		
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		//�ڶ�������3�������6����������
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);        		
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);
	        		alTexture.add(texCoorArray[tc++%ts]);       		
	        	}
	        } 	
	        
	        vCount=alVertix.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
	    	
	        //��alVertix�е�����ֵת�浽һ��float������
	        float vertices[]=new float[vCount*3];
	    	for(int i=0;i<alVertix.size();i++)
	    	{
	    		vertices[i]=alVertix.get(i);
	    	}
	        
	        //�������ƶ������ݻ���
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��     
	        
	       
	                
	        //�����������껺��
	        float textureCoors[]=new float[alTexture.size()];//��������ֵ����
	        for(int i=0;i<alTexture.size();i++) 
	        {
	        	textureCoors[i]=alTexture.get(i);
	        }
	        
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
	        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mTextureBuffer = tbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mTextureBuffer.put(textureCoors);//�򻺳����з��붥����ɫ����
	        mTextureBuffer.position(0);//���û�������ʼλ��
	    }

	    public void drawSelf(GL10 gl,int texId)
	    {
	        //����ʹ�ö�������
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			//Ϊ����ָ��������������
	        gl.glVertexPointer
	        (
	        		3,				//ÿ���������������Ϊ3  xyz 
	        		GL10.GL_FLOAT,	//��������ֵ������Ϊ GL_FIXED
	        		0, 				//����������������֮��ļ��
	        		mVertexBuffer	//������������
	        );        
			
	        //��������
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //����ʹ������ST���껺��
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //Ϊ����ָ������ST���껺��
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
	        //�󶨵�ǰ����
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
	     
	        gl.glPushMatrix();
	        //����ͼ��
	        gl.glDrawArrays
	        (
	        		GL10.GL_TRIANGLES, 		//�������η�ʽ���
	        		0, 			 			//��ʼ����
	        		vCount					//��������
	        );        
	        gl.glPopMatrix();
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//������������
	        gl.glDisable(GL10.GL_TEXTURE_2D);//�ر�����
	    }
	    
	    //�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=2.0f/bw;//����
	    	float sizeh=1.0f/bh;//����
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
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			
	    			
	    			
	    			
	    			
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t+sizeh;    			
	    		}
	    	}
	    	return result;
	    }
	}

}
