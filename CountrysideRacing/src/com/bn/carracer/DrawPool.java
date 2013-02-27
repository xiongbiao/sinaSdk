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
	
	static boolean waterFlag=true;//水换帧线程标志位
	
	DrawPoolLand poolLand;//池塘陆地
	DrawWater[] water=new DrawWater[8];//水面————8帧
	int currentWaterIndex;//当前水面帧索引
	
	static ThreadWater waterThread;
	
	public void setTexId(int grassTextureId,int waterTextureId)
	{
		this.grassTextureId=grassTextureId;
		this.waterTextureId=waterTextureId;
	}
	
	public DrawPool(float partSize)
	{
		this.partSize=partSize;
		
		poolLand=new DrawPoolLand(CyArray,ROWS,COLS);//池塘陆地
        for(int i=0;i<water.length;i++)//水面
        {
        	water[i]=new DrawWater
        	(
        			Math.PI*2*i/water.length//水面起始波动角度
        	);
        }        
        waterThread=new ThreadWater();
	}
	
	public void drawSelf(GL10 gl)
	{
		gl.glPushMatrix();
		poolLand.drawSelf(gl);
		gl.glPopMatrix();
		
        //绘制水 
        gl.glPushMatrix();
        gl.glEnable(GL10.GL_BLEND);//开启混合
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glTranslatef(0,-4, 0);
        water[currentWaterIndex].drawSelf(gl);
        gl.glDisable(GL10.GL_BLEND);//开启混合 
        gl.glPopMatrix();  
	}
	
	
	
	//池塘地形内部类
	private class DrawPoolLand {
		private FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	    private FloatBuffer   mTextureBuffer;//顶点着色数据缓冲
	    int vCount;//顶点数量
	    
	    public DrawPoolLand(short[][] yArray,int rows,int cols)
	    {	    	
	    	//顶点坐标数据的初始化================begin============================
	        
	        ArrayList<Float> alf=new ArrayList<Float>();
	        
	        for(int j=0;j<rows;j++)//循环行
	        {
	        	for(int i=0;i<cols;i++)//循环列
	        	{        		  		
	        		float zsx=X_OFFSET+i*UNIT_SIZE;
	        		float zsz=Z_OFFSET+j*UNIT_SIZE;     
	        		
	        		alf.add(zsx);//左上
	        		alf.add((yArray[j][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz);
	        		
	        		alf.add(zsx);//左下
	        		alf.add((yArray[j+1][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        		
	        		alf.add(zsx+UNIT_SIZE);//右上
	        		alf.add((yArray[j][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz); 
	        		
	        		alf.add(zsx+UNIT_SIZE);//右上
	        		alf.add((yArray[j][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz);
	        		
	        		alf.add(zsx);//左下
	        		alf.add((yArray[j+1][i]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        		
	        		alf.add(zsx+UNIT_SIZE);//右下
	        		alf.add((yArray[j+1][i+1]-255)*LAND_HIGHEST/255.0f);
	        		alf.add(zsz+UNIT_SIZE);
	        	}
	        } 
	        vCount=alf.size()/3;//实际顶点数       
	       
	        float vertices[]=new float[alf.size()];
	        for(int i=0;i<alf.size();i++)
	        {
	        	vertices[i]=alf.get(i);
	        }
			
	        //创建顶点坐标数据缓冲
	        //vertices.length*4是因为一个整数四个字节
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
	        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
	        mVertexBuffer.position(0);//设置缓冲区起始位置
	        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
	        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	        //顶点坐标数据的初始化================end============================
	        
	        //顶点纹理数据的初始化================begin============================
	    	//自动生成纹理数组，20列15行
	    	float textures[]=generateTexCoor(rows,cols,yArray);
	        
	        //创建顶点纹理数据缓冲
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
	        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mTextureBuffer= tbb.asFloatBuffer();//转换为Float型缓冲
	        mTextureBuffer.put(textures);//向缓冲区中放入顶点着色数据
	        mTextureBuffer.position(0);//设置缓冲区起始位置
	        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
	        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	        //顶点纹理数据的初始化================end============================
	    }

	    public void drawSelf(GL10 gl)
	    {        
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//启用顶点坐标数组
	        
			//为画笔指定顶点坐标数据
	        gl.glVertexPointer
	        (
	        		3,				//每个顶点的坐标数量为3  xyz 
	        		GL10.GL_FLOAT,	//顶点坐标值的类型为 GL_FIXED
	        		0, 				//连续顶点坐标数据之间的间隔
	        		mVertexBuffer	//顶点坐标数据
	        );
			
	        //开启纹理
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //允许使用纹理ST坐标缓冲
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //为画笔指定纹理ST坐标缓冲
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
	        //绑定当前纹理
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, grassTextureId);
			
	        //绘制图形
	        gl.glDrawArrays    
	        (
	        		GL10.GL_TRIANGLES, 		//以三角形方式填充
	        		0,
	        		vCount 
	        );
	        
	        //关闭纹理
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_TEXTURE_2D); 
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    }
	    
	   //自动切分纹理产生纹理数组的方法
	    public float[] generateTexCoor(int bh,int bw,short[][] yArray)
	    {
	    	ArrayList<Float> alf=new ArrayList<Float>();

	    	float REPEAT=1;
	    	
	    	float sizeh=REPEAT/bh;//行数
	    	float sizew=REPEAT/bw;//列数
	    	
	    	for(int i=0;i<bh;i++)//行循环
	    	{
	    		for(int j=0;j<bw;j++)//列循环
	    		{ 
	    				//每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
	        			float s=j*sizew;
	        			float t=i*sizeh; 
	        			   
	        			alf.add(s);//左上
	        			alf.add(t);
	        			
	        			alf.add(s);//左下
	        			alf.add(t+sizeh);
	        			
	        			alf.add(s+sizew);//右上
	        			alf.add(t);
	        			
	        			alf.add(s+sizew);//右上
	        			alf.add(t);			
	        			  
	        			alf.add(s);//左下
	        			alf.add(t+sizeh);
	        			
	        			alf.add(s+sizew);//右下
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
	
	
	
	
	//水面内部类
	private class DrawWater {
		private FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
	    private FloatBuffer   mTextureBuffer;//顶点着色数据缓冲
	    int vCount;//顶点数量
	    
	    public DrawWater(double startAngle)
	    {  	 
	    	
	    	int rows=8;//水面格子行列数
	    	int cols=8;
	    	float height=2;
	    	int bei=CyArray.length/8;//水面格子单位长度与池塘陆地单位长度的倍数关系。
	    	
	    	final double angleSpan=Math.PI*4/cols;//每格的单位弧度
	    	final float LOCAL_UNIT_SIZE=bei*UNIT_SIZE;
	    	
	    	//顶点坐标数据的初始化================begin============================
	        vCount=cols*rows*2*3;//每个格子两个三角形，每个三角形3个顶点        
	        float vertices[]=new float[vCount*3];//每个顶点xyz三个坐标
	        int count=0;//顶点计数器
	        for(int j=0;j<rows;j++)
	        {
	        	for(int i=0;i<cols;i++)
	        	{ 
	        		//计算当前格子左上侧点坐标 
	        		float zsx=-LOCAL_UNIT_SIZE*cols/2+i*LOCAL_UNIT_SIZE;
	        		float zsz=-LOCAL_UNIT_SIZE*rows/2+j*LOCAL_UNIT_SIZE;
	        		float zsy=(float)Math.sin(startAngle+i*angleSpan)*height;
	        		//计算右边一列的y坐标
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
			
	        //创建顶点坐标数据缓冲
	        //vertices.length*4是因为一个整数四个字节
	        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mVertexBuffer = vbb.asFloatBuffer();//转换为int型缓冲
	        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
	        mVertexBuffer.position(0);//设置缓冲区起始位置
	        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
	        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	        //顶点坐标数据的初始化================end============================
	        
	        //顶点纹理数据的初始化================begin============================
	    	//自动生成纹理数组，20列15行
	    	float textures[]=generateTexCoor(cols,rows);
	        
	        //创建顶点纹理数据缓冲
	        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
	        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
	        mTextureBuffer= tbb.asFloatBuffer();//转换为Float型缓冲
	        mTextureBuffer.put(textures);//向缓冲区中放入顶点着色数据
	        mTextureBuffer.position(0);//设置缓冲区起始位置
	        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
	        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	        //顶点纹理数据的初始化================end============================
	    }

	    public void drawSelf(GL10 gl)
	    {        
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//启用顶点坐标数组
	        
			//为画笔指定顶点坐标数据
	        gl.glVertexPointer
	        (
	        		3,				//每个顶点的坐标数量为3  xyz 
	        		GL10.GL_FLOAT,	//顶点坐标值的类型为 GL_FIXED
	        		0, 				//连续顶点坐标数据之间的间隔
	        		mVertexBuffer	//顶点坐标数据
	        );
			
	        //开启纹理
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //允许使用纹理ST坐标缓冲
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        //为画笔指定纹理ST坐标缓冲
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
	        //绑定当前纹理
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, waterTextureId);
			
	        //绘制图形
	        gl.glDrawArrays
	        (
	        		GL10.GL_TRIANGLES, 		//以三角形方式填充
	        		0,
	        		vCount
	        );
	        
	        //关闭纹理
	        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        gl.glDisable(GL10.GL_TEXTURE_2D); 
	        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    }
	    
	   //自动切分纹理产生纹理数组的方法
	    public float[] generateTexCoor(int bw,int bh)
	    {
	    	float[] result=new float[bw*bh*6*2]; 
	    	float sizew=4.0f/bw;//列数
	    	float sizeh=3f/bh;//行数
	    	int c=0;
	    	for(int i=0;i<bh;i++)
	    	{
	    		for(int j=0;j<bw;j++)
	    		{
	    			//每行列一个矩形，由两个三角形构成，共六个点，12个纹理坐标
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
	
	//启动一个线程动态切换水面帧
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
