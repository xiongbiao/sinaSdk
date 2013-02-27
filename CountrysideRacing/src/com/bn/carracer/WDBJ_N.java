package com.bn.carracer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
//�������
public class WDBJ_N {
    int roadTexId;
    int lubiaoTexId;
    int jiantouTexId;
    
    Road road;
    DrawCirque wCirque;
    DrawCirque nCirque;  
    DrawCylinder cylinder; 
    
    float xoffset;//��ƫ����
    float yoffset;
    float zoffset;
    
    float w_RR;//�⻷�뾶
    float n_RR;//�ڻ��뾶
    float z_R;//�����ͷԲ���뾶
    float cr=9.6f;//��Բ����뾶
    float Rstart=-90f;//·�껷���Ƶ���ʼ�Ƕ�
    float Rover=0f;//·�껷���ƵĽ����Ƕ�
    float Cstart=50f;//��������ʼ�Ƕ�
    float Cover=130f;//����������Ƕ�
    
	public float mAngleX;
	public float mAngleY;
	public float mAngleZ;
	
	public void setTexId(int roadTexId,int lubiaoTexId,int jiantouTexId)
	{
		this.roadTexId=roadTexId;
		this.lubiaoTexId=lubiaoTexId;
		this.jiantouTexId=jiantouTexId;
	}
    
    public WDBJ_N
    (
    		float partSize,//�����ߴ�
    		float roadWidth//·��
    )
    {     	
    	xoffset=-partSize/2;
    	yoffset=(float) (-cr*Math.sin(Math.toRadians(50)));
    	zoffset=partSize/2;
    	
    	w_RR=(float) (partSize/2+roadWidth/2+cr*Math.cos(Math.toRadians(50)));
    	n_RR=(float) (partSize/2-roadWidth/2-cr*Math.cos(Math.toRadians(50)));
    	z_R=(float) (partSize/2+roadWidth/2+2*cr*Math.cos(Math.toRadians(50)));
    	
    	road=new Road(partSize,roadWidth);
    	wCirque=new DrawCirque(4.5f,10,w_RR,cr,Rstart,Rover,Cstart,Cover,15);
    	nCirque=new DrawCirque(4.5f,10,n_RR,cr,Rstart,Rover,Cstart,Cover,4);
    	cylinder=new DrawCylinder(30f,z_R,9f,xoffset,zoffset);
    }
    
    public void drawSelf(GL10 gl)
    {
		gl.glRotatef(mAngleX, 1, 0, 0);//��ת
		gl.glRotatef(mAngleY, 0, 1, 0);
		gl.glRotatef(mAngleZ, 0, 0, 1);
		
    	gl.glPushMatrix();
    	road.drawSelf(gl);
    	gl.glPopMatrix();
    	
    	gl.glPushMatrix();
    	gl.glTranslatef(xoffset, yoffset, zoffset);
    	wCirque.drawSelf(gl);
    	gl.glPopMatrix();
    	 
    	gl.glPushMatrix();
    	gl.glTranslatef(xoffset, yoffset, zoffset);
    	nCirque.drawSelf(gl);
    	gl.glPopMatrix();
    	
    	gl.glPushMatrix();
        cylinder.drawSelf(gl);
    	gl.glPopMatrix();
    } 
    
    
    
    
    
    //����Բ������ڲ���
    private class Road
    {
    	private FloatBuffer   mVertexBuffer;//�����������ݻ���
        private FloatBuffer mTextureBuffer;//�����������ݻ���
        int vCount=0;
    	
    	public Road(float L,float W)
    	{	    	
	    	float x0=-L/2;
	    	float z0=L/2;
	    	
	    	//�����������ݵĳ�ʼ��================begin============================
	        ArrayList<Float> alfv=new ArrayList<Float>();//��������
	        ArrayList<Float> alft=new ArrayList<Float>();//��������
	        float REPEAT=10.0f;        
	        float rn=(L-W)/2;//��Ȧ�뾶
	        float rw=rn+W;//��Ȧ�뾶
	        float alphaSpan=0.9f; 
	        float texCorSpan=REPEAT/(90/alphaSpan);
	        for(float alpha=0;alpha<90;alpha=alpha+alphaSpan)
	        {
	        	float c=alpha/alphaSpan;
	        	
	        	float xn1=(float)(x0+Math.sin(Math.toRadians(alpha))*rn);
	        	float zn1=(float)(z0-Math.cos(Math.toRadians(alpha))*rn);
	        	float yn1=0;
	        	float sn1=texCorSpan*c;
	        	float tn1=1.0f;
	        	
	        	float xw1=(float)(x0+Math.sin(Math.toRadians(alpha))*rw);
	        	float zw1=(float)(z0-Math.cos(Math.toRadians(alpha))*rw);
	        	float yw1=0;
	        	float sw1=texCorSpan*c;
	        	float tw1=0.0f;
	        	
	        	float xn2=(float)(x0+Math.sin(Math.toRadians(alpha+alphaSpan))*rn);
	        	float zn2=(float)(z0-Math.cos(Math.toRadians(alpha+alphaSpan))*rn);
	        	float yn2=0;
	        	float sn2=texCorSpan*(c+1);
	        	float tn2=1.0f;
	        	
	        	float xw2=(float)(x0+Math.sin(Math.toRadians(alpha+alphaSpan))*rw);
	        	float zw2=(float)(z0-Math.cos(Math.toRadians(alpha+alphaSpan))*rw);
	        	float yw2=0;
	        	float sw2=texCorSpan*(c+1);
	        	float tw2=0.0f;
	        	
	        	alfv.add(xn1);alfv.add(yn1);alfv.add(zn1);
	        	alfv.add(xn2);alfv.add(yn2);alfv.add(zn2);
	        	alfv.add(xw1);alfv.add(yw1);alfv.add(zw1);
	        	
	        	alfv.add(xw2);alfv.add(yw2);alfv.add(zw2);        	
	        	alfv.add(xw1);alfv.add(yw1);alfv.add(zw1);
	        	alfv.add(xn2);alfv.add(yn2);alfv.add(zn2);
	        	
	        	alft.add(sn1);alft.add(tn1);
	        	alft.add(sn2);alft.add(tn2);
	        	alft.add(sw1);alft.add(tw1);
	        	
	        	alft.add(sw2);alft.add(tw2);
	        	alft.add(sw1);alft.add(tw1);
	        	alft.add(sn2);alft.add(tn2);
	        }
	        
	        float vertices[]=new float[alfv.size()];
	        for(int i=0;i<alfv.size();i++)
	        {
	        	vertices[i]=alfv.get(i);
	        }
	        
	        vCount=alfv.size()/3;
			
	        //���������������ݻ���
	        //vertices.length*4����Ϊһ�������ĸ��ֽ�
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	        mVertexBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //�����������ݵĳ�ʼ��================end============================
	        
	        //���� �������ݳ�ʼ��
	        float[] texST=new float[alft.size()];
	        for(int i=0;i<alft.size();i++)
	        {
	        	texST[i]=alft.get(i);
	        }
	        	
	        ByteBuffer tbb = ByteBuffer.allocateDirect(texST.length*4);
	        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mTextureBuffer = tbb.asFloatBuffer();//ת��Ϊint�ͻ���
	        mTextureBuffer.put(texST);//�򻺳����з��붥����ɫ����
	        mTextureBuffer.position(0);//���û�������ʼλ��         
        
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
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, roadTexId);
			
	        //����ͼ��
	        gl.glDrawArrays
	        (
	        		GL10.GL_TRIANGLES, 		//�������η�ʽ���
	        		0, 			 			//��ʼ����
	        		vCount					//���������
	        );
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_TEXTURE_2D);
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    }
    }
    
    
    //����Բ������ڲ���
    private class DrawCirque
    {
    	private FloatBuffer myVertex;//���㻺��
    	private FloatBuffer myTexture;//������
    	
    	int vcount;
    	
    	int repeat;
    	
    	public DrawCirque    	
    	(
    			float ring_Span,float circle_Span,float ring_Radius,float circle_Radius,
    			float RstartAngle,float RoverAngle,float CstartAngle,float CoverAngle,
    			int repeat
    	)
    	{     
    		//ring_Span��ʾ��ÿһ�ݶ��ٶȣ�circle_Span��ʾԲ�ػ�ÿһ�ݶ��ٶ�;ring_Radius��ʾ���뾶��circle_RadiusԲ����뾶��
    		this.repeat=repeat;
    		
    		ArrayList<Float> val=new ArrayList<Float>();
    		
    		for(float circle_Degree=CstartAngle;circle_Degree<CoverAngle;circle_Degree+=circle_Span)
    		{
    			for(float ring_Degree=RstartAngle;ring_Degree<RoverAngle;ring_Degree+=ring_Span)
    			{
    				float x1=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree)))*Math.cos(Math.toRadians(ring_Degree)));
    				float y1=(float) (circle_Radius*Math.sin(Math.toRadians(circle_Degree)));
    				float z1=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree)))*Math.sin(Math.toRadians(ring_Degree)));
    				
    				float x2=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree)))*Math.cos(Math.toRadians(ring_Degree+ring_Span)));
    				float y2=(float) (circle_Radius*Math.sin(Math.toRadians(circle_Degree)));
    				float z2=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree)))*Math.sin(Math.toRadians(ring_Degree+ring_Span)));
    				
    				float x3=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree+circle_Span)))*Math.cos(Math.toRadians(ring_Degree+ring_Span)));
    				float y3=(float) (circle_Radius*Math.sin(Math.toRadians(circle_Degree+circle_Span)));
    				float z3=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree+circle_Span)))*Math.sin(Math.toRadians(ring_Degree+ring_Span)));
    				
    				float x4=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree+circle_Span)))*Math.cos(Math.toRadians(ring_Degree)));
    				float y4=(float) (circle_Radius*Math.sin(Math.toRadians(circle_Degree+circle_Span)));
    				float z4=(float) ((ring_Radius+circle_Radius*Math.cos(Math.toRadians(circle_Degree+circle_Span)))*Math.sin(Math.toRadians(ring_Degree)));
    				
    				val.add(x1);val.add(y1);val.add(z1);
    				val.add(x4);val.add(y4);val.add(z4);
    				val.add(x2);val.add(y2);val.add(z2);
    							
    				val.add(x2);val.add(y2);val.add(z2);
    				val.add(x4);val.add(y4);val.add(z4);
    				val.add(x3);val.add(y3);val.add(z3); 
    			}
    		}
    		vcount=val.size()/3;
    		float[] vertexs=new float[vcount*3];
    		for(int i=0;i<vcount*3;i++)
    		{
    			vertexs[i]=val.get(i);
    		}
    		ByteBuffer vbb=ByteBuffer.allocateDirect(vertexs.length*4);
    		vbb.order(ByteOrder.nativeOrder());
    		myVertex=vbb.asFloatBuffer();
    		myVertex.put(vertexs);
    		myVertex.position(0);
    		
    		//����
    		
    		int row=(int) ((CoverAngle-CstartAngle)/circle_Span);
    		int col=(int) ((RoverAngle-RstartAngle)/ring_Span);
    		float[] textures=generateTexCoor(col,row);
    		
    		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
    		tbb.order(ByteOrder.nativeOrder());
    		myTexture=tbb.asFloatBuffer();
    		myTexture.put(textures);
    		myTexture.position(0);
    	}
    	
    	public void drawSelf(GL10 gl)
    	{	
    		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertex);
    		
    		gl.glEnable(GL10.GL_TEXTURE_2D);
    		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
    		gl.glBindTexture(GL10.GL_TEXTURE_2D, lubiaoTexId);
    		
    		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vcount);
    		
    		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�رջ���
    		gl.glEnable(GL10.GL_TEXTURE_2D);
    		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    	}
    	
        //�Զ��з����������������ķ���
        public float[] generateTexCoor(int bw,int bh)
        {
        	float[] result=new float[bw*bh*6*2]; 
        	float REPEAT=repeat;
        	float sizew=REPEAT/bw;//����
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
    
    
    
    
    
    
    //����Բ������ڲ���
    private class DrawCylinder
    {
    	private FloatBuffer myVertexBuffer;//�������껺�� 
    	private FloatBuffer myTexture;//������
    	
    	int vCount;//��������
    	
    	public DrawCylinder(float height,float circle_radius,float degreespan,float xoffset,float zoffset)
    	{		    		
    		ArrayList<Float> val=new ArrayList<Float>();//�������б�
    		ArrayList<Float> tal=new ArrayList<Float>();//�������б�
    		
    		degreespan=3;

    		for(float circle_degree=0;circle_degree<90;circle_degree+=degreespan)//ѭ����
    		{
    			if((circle_degree/degreespan)%2==1){continue;}
    			
    			float x1=(float) (xoffset+circle_radius*Math.sin(Math.toRadians(circle_degree)));
    			float y1=height;
    			float z1=(float) (zoffset-circle_radius*Math.cos(Math.toRadians(circle_degree)));
    			
    			float x2=(float) (xoffset+circle_radius*Math.sin(Math.toRadians(circle_degree)));
    			float y2=0;
    			float z2=(float) (zoffset-circle_radius*Math.cos(Math.toRadians(circle_degree)));
    			
    			float x3=(float) (xoffset+circle_radius*Math.sin(Math.toRadians(circle_degree+degreespan)));
    			float y3=0;
    			float z3=(float) (zoffset-circle_radius*Math.cos(Math.toRadians(circle_degree+degreespan)));
    			
    			float x4=(float) (xoffset+circle_radius*Math.sin(Math.toRadians(circle_degree+degreespan)));
    			float y4=height;
    			float z4=(float) (zoffset-circle_radius*Math.cos(Math.toRadians(circle_degree+degreespan)));
    			
    			float s1=0;
    			float t1=0;
    			
    			float s2=0;
    			float t2=1;
    			
    			float s3=1;
    			float t3=1;
    			
    			float s4=1;
    			float t4=0;
    				
    				val.add(x1);val.add(y1);val.add(z1);//���������Σ���6�����������
    				val.add(x2);val.add(y2);val.add(z2);
    				val.add(x4);val.add(y4);val.add(z4);
    				
    				val.add(x4);val.add(y4);val.add(z4);
    				val.add(x2);val.add(y2);val.add(z2);
    				val.add(x3);val.add(y3);val.add(z3);
	
    				tal.add(s1);tal.add(t1);
    				tal.add(s2);tal.add(t2);
    				tal.add(s4);tal.add(t4);

    				tal.add(s4);tal.add(t4);
    				tal.add(s2);tal.add(t2);
    				tal.add(s3);tal.add(t3);
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
    		float[] textures=new float[vCount*2];
    		for(int i=0;i<vCount*2;i++)
    		{
    			textures[i]=tal.get(i);
    		}
    		ByteBuffer tbb=ByteBuffer.allocateDirect(textures.length*4);
    		tbb.order(ByteOrder.nativeOrder());
    		myTexture=tbb.asFloatBuffer();
    		myTexture.put(textures);
    		myTexture.position(0);
    	}
    	
    	public void drawSelf(GL10 gl)
    	{	
    		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//�򿪶��㻺��
    		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, myVertexBuffer);//ָ�����㻺��
    		
    		gl.glEnable(GL10.GL_TEXTURE_2D);
    		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, myTexture);
    		gl.glBindTexture(GL10.GL_TEXTURE_2D, jiantouTexId);
    		
    		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//����ͼ��
    		 
    		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//�رջ���
    		gl.glEnable(GL10.GL_TEXTURE_2D);
    		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    	}
    }
}
