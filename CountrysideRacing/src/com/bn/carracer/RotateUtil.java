package com.bn.carracer;

public class RotateUtil
{
	//返回值为旋转后的向量
	public static double[] pitchRotate(double angle,double[] gVector)
	{
		double[][] matrix=//绕x轴旋转变换矩阵
		{
		   {1,0,0,0},
		   {0,Math.cos(angle),Math.sin(angle),0},		   
		   {0,-Math.sin(angle),Math.cos(angle),0},
		   {0,0,0,1}	
		};		
		double[] tempDot={gVector[0],gVector[1],gVector[2],gVector[3]};
		for(int j=0;j<4;j++)
		{
			gVector[j]=(tempDot[0]*matrix[0][j]+tempDot[1]*matrix[1][j]+
			             tempDot[2]*matrix[2][j]+tempDot[3]*matrix[3][j]);    
		}		
		return gVector;
	}
	//返回值为旋转后的向量
	public static double[] rollRotate(double angle,double[] gVector)
	{
		double[][] matrix=//绕y轴旋转变换矩阵
		{
		   {Math.cos(angle),0,-Math.sin(angle),0},
		   {0,1,0,0},
		   {Math.sin(angle),0,Math.cos(angle),0},
		   {0,0,0,1}	
		};		
		double[] tempDot={gVector[0],gVector[1],gVector[2],gVector[3]};
		for(int j=0;j<4;j++)
		{
			gVector[j]=(tempDot[0]*matrix[0][j]+tempDot[1]*matrix[1][j]+
			             tempDot[2]*matrix[2][j]+tempDot[3]*matrix[3][j]);    
		}		
		return gVector;
	}		
	//返回值为旋转后的向量
	public static double[] yawRotate(double angle,double[] gVector)
	{
		double[][] matrix=//绕z轴旋转变换矩阵
		{
		   {Math.cos(angle),Math.sin(angle),0,0},		   
		   {-Math.sin(angle),Math.cos(angle),0,0},
		   {0,0,1,0},
		   {0,0,0,1}	
		};		
		double[] tempDot={gVector[0],gVector[1],gVector[2],gVector[3]};
		for(int j=0;j<4;j++)
		{
			gVector[j]=(tempDot[0]*matrix[0][j]+tempDot[1]*matrix[1][j]+
			             tempDot[2]*matrix[2][j]+tempDot[3]*matrix[3][j]);    
		}		
		return gVector;
	}		
	public static int[] getDirectionDot(double[] values)
	{
		double yawAngle=-Math.toRadians(values[0]);//获取Yaw轴旋转角度弧度
		double pitchAngle=-Math.toRadians(values[1]);//获取Pitch轴旋转角度弧度
		double rollAngle=-Math.toRadians(values[2]);//获取Roll轴旋转角度弧度				
		double[] gVector={0,0,-100,1};		
		//yaw轴
		gVector=RotateUtil.yawRotate(yawAngle,gVector);		
		//pitch轴
		gVector=RotateUtil.pitchRotate(pitchAngle,gVector);			
		//roll轴
		gVector=RotateUtil.rollRotate(rollAngle,gVector);		
		double mapX=gVector[0];
		double mapY=gVector[1];				
		int[] result={(int)mapX,(int)mapY};
		return result;
	}	
}