package com.bn.carracer;

import static com.bn.carracer.Constant.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class DrawPool
{
	float partSize;
	int grassTextureId;
	int waterTextureId;
	
	static boolean waterFlag=true;//ˮ��֡�̱߳�־λ
	
	DrawPoolLand poolLand;//����½��
	DrawWater[] water=new DrawWater[8];//ˮ�桪������8֡
	int currentWaterIndex;//��ǰˮ��֡����
	
	static ThreadWater waterThread;
	
	public void setTexId(int grassTextureId,int waterTextureId)
	{
		this.grassTextureId=grassTextureId;
		this.waterTextureId=waterTextureId;
	}
	
	public DrawPool(float partSize)
	{
		this.partSize=partSize;
		
		poolLand=new DrawPoolLand(CyArray,ROWS,COLS);//����½��
        for(int i=0;i<water.length;i++)//ˮ��
        {
        	water[i]=new DrawWater
        	(
        			Math.PI*2*i/water.length//ˮ����ʼ�����Ƕ�
        	);
        }        
        waterThread=new ThreadWater();
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		poolLand.drawSelf(gl);
		gl.glPopMatrix();
		
        //����ˮ 
        gl.glPushMatrix();
        gl.glEnable(GL10.GL_BLEND);//�������
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glTranslatef(0,-4, 0);
        water[currentWaterIndex].drawSelf(gl);
        gl.glDisable(GL10.GL_BLEND);//������� 
        gl.glPopMatrix();  
	}
	
	
	
	//���������ڲ���
	private class DrawPoolLand {
		private FloatBuffer   mVertexBuffer;//�����������ݻ���
	    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
	    int vCount;//��������
	    
	    public DrawPoolLand(short[][] yArray,int rows,int cols)
	    {	    	
	    	//�����������ݵĳ�ʼ��================begin============================
	        
	        ArrayList<Float> alf=new ArrayList<Float>();
	        
	        for(int j=0;j<rows;j++)//ѭ����
	        {
	        	for(int i=0;i<cols;i++)//ѭ����
	        	{        		  		
	        		float zsx=X_OFFSET+i*UNIT_SIZE;
	        		float zsz=Z_OFFSET+j*UNIT_SIZE;     
	        		
	        		alf.add(zsx);//����
	        		alf.add((yArray[j][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz);
	        		
	        		alf.add(zsx);//����
	        		alf.add((yArray[j+1][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        		
	        		alf.add(zsx+UNIT_SIZE);//����
	        		alf.add((yArray[j][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz); 
	        		
	        		alf.add(zsx+UNIT_SIZE);//����
	        		alf.add((yArray[j][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz);
	        		
	        		alf.add(zsx);//����
	        		alf.add((yArray[j+1][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        		
	        		alf.add(zsx+UNIT_SIZE);//����
	        		alf.add((yArray[j+1][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        	}
	        } 
	        vCount=alf.size()/3;//ʵ�ʶ�����       
	       
	        float vertices[]=new float[alf.size()];
	        for(int i=0;i<alf.size();i++)
	        {
	        	vertices[i]=alf.get(i);
	        }
			
	        //���������������ݻ���
	        //vertices.length*4����Ϊһ�������ĸ��ֽ�
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //�����������ݵĳ�ʼ��================end============================
	        
	        //�����������ݵĳ�ʼ��================begin============================
	    	//�Զ������������飬20��15��
	    	float textures[]=generateTexCoor(rows,cols,yArray);
	        
	        //���������������ݻ���
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
	        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
	        mTextureBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //�����������ݵĳ�ʼ��================end============================
	    }

	    public void drawSelf(GL10 gl)
	    {        
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//���ö�����������
	        
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
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, grassTextureId);
			
	        //����ͼ��
	        gl.glDrawArrays    
	        (
	        		GL10.GL_TRIANGLES, 		//�������η�ʽ���
	        		0,
	        		vCount 
	        );
	        
	        //�ر�����
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_TEXTURE_2D); 
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    }
	    
	   //�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bh,int bw,short[][] yArray)
	    {
	    	ArrayList<Float> alf=new ArrayList<Float>();

	    	float REPEAT=1;
	    	
	    	float sizeh=REPEAT/bh;//����
	    	float sizew=REPEAT/bw;//����
	    	
	    	for(int i=0;i<bh;i++)//��ѭ��
	    	{
	    		for(int j=0;j<bw;j++)//��ѭ��
	    		{ 
	    				//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
	        			float s=j*sizew;
	        			float t=i*sizeh; 
	        			   
	        			alf.add(s);//����
	        			alf.add(t);
	        			
	        			alf.add(s);//����
	        			alf.add(t+sizeh);
	        			
	        			alf.add(s+sizew);//����
	        			alf.add(t);
	        			
	        			alf.add(s+sizew);//����
	        			alf.add(t);			
	        			  
	        			alf.add(s);//����
	        			alf.add(t+sizeh);
	        			
	        			alf.add(s+sizew);//����
	        			alf.add(t+sizeh); 	  
	    		}
	    	}
	    	
	    	float[] result=new float[alf.size()]; 
	    	for(int i=0;i<alf.size();i++)
	        {
	    		result[i]=alf.get(i);
	        }
	    	return result;
	    }
	}
	
	
	
	
	//ˮ���ڲ���
	private class DrawWater {
		private FloatBuffer   mVertexBuffer;//�����������ݻ���
	    private FloatBuffer   mTextureBuffer;//������ɫ���ݻ���
	    int vCount;//��������
	    
	    public DrawWater(double startAngle)
	    {  	 
	    	
	    	int rows=8;//ˮ�����������
	    	int cols=8;
	    	float height=2;
	    	int bei=CyArray.length/8;//ˮ����ӵ�λ���������½�ص�λ���ȵı�����ϵ��
	    	
	    	final double angleSpan=Math.PI*4/cols;//ÿ��ĵ�λ����
	    	final float LOCAL_UNIT_SIZE=bei*UNIT_SIZE;
	    	
	    	//�����������ݵĳ�ʼ��================begin============================
	        vCount=cols*rows*2*3;//ÿ���������������Σ�ÿ��������3������        
	        float vertices[]=new float[vCount*3];//ÿ������xyz��������
	        int count=0;//���������
	        for(int j=0;j<rows;j++)
	        {
	        	for(int i=0;i<cols;i++)
	        	{ 
	        		//���㵱ǰ�������ϲ������ 
	        		float zsx=-LOCAL_UNIT_SIZE*cols/2+i*LOCAL_UNIT_SIZE;
	        		float zsz=-LOCAL_UNIT_SIZE*rows/2+j*LOCAL_UNIT_SIZE;
	        		float zsy=(float)Math.sin(startAngle+i*angleSpan)*height;
	        		//�����ұ�һ�е�y����
	        		float zsyp=(float)Math.sin(startAngle+(i+1)*angleSpan)*height;
	        		
	        		vertices[count++]=zsx;
	        		vertices[count++]=zsy;
	        		vertices[count++]=zsz;
	        		
	        		vertices[count++]=zsx;
	        		vertices[count++]=zsy;
	        		vertices[count++]=zsz+LOCAL_UNIT_SIZE;
	        		
	        		vertices[count++]=zsx+LOCAL_UNIT_SIZE;
	        		vertices[count++]=zsyp;
	        		vertices[count++]=zsz;
	        		
	        		vertices[count++]=zsx+LOCAL_UNIT_SIZE;
	        		vertices[count++]=zsyp;
	        		vertices[count++]=zsz;
	        		
	        		vertices[count++]=zsx;
	        		vertices[count++]=zsy;
	        		vertices[count++]=zsz+LOCAL_UNIT_SIZE;
	        		        		
	        		vertices[count++]=zsx+LOCAL_UNIT_SIZE;
	        		vertices[count++]=zsyp;
	        		vertices[count++]=zsz+LOCAL_UNIT_SIZE;   
	        	}
	        }
			
	        //���������������ݻ���
	        //vertices.length*4����Ϊһ�������ĸ��ֽ�
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //�����������ݵĳ�ʼ��================end============================
	        
	        //�����������ݵĳ�ʼ��================begin============================
	    	//�Զ������������飬20��15��
	    	float textures[]=generateTexCoor(cols,rows);
	        
	        //���������������ݻ���
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
	        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
	        mTextureBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //�����������ݵĳ�ʼ��================end============================
	    }

	    public void drawSelf(GL10 gl)
	    {        
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//���ö�����������
	        
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
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, waterTextureId);
			
	        //����ͼ��
	        gl.glDrawArrays
	        (
	        		GL10.GL_TRIANGLES, 		//�������η�ʽ���
	        		0,
	        		vCount
	        );
	        
	        //�ر�����
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_TEXTURE_2D); 
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    }
	    
	   //�Զ��з����������������ķ���
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=4.0f/bw;//����
	    	float sizeh=3f/bh;//����
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
	    			
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t;
	    			
	    			result[c++]=s;
	    			result[c++]=t+sizeh;
	    			
	    			result[c++]=s+sizew;
	    			result[c++]=t+sizeh;    			
	    		}
	    	}
	    	return result;
	    }
	}	
	
	//����һ���̶߳�̬�л�ˮ��֡
	public class ThreadWater extends Thread
	{
    	@Override
    	public void run()
    	{
    		while(waterFlag)
    		{
    			currentWaterIndex=(currentWaterIndex+1)%water.length;            		
        		try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace(); 
				}
    		}
    	}
	}
}
