package com.android.go.home;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioHandler  extends Thread{
	private ServerSocket sSocket = null;
	private boolean isStopTalk = false; 
	
	public void setStopTalk(boolean isStopTalk) {
		this.isStopTalk = isStopTalk;
	}
		//	private G711Codec codec;
		public AudioHandler(){}
		@Override
		public void run() {
			super.run();
			try {
				sSocket = new ServerSocket(Constant.AUDIO_PORT);//监听音频端口
				System.out.println("Audio Handler socket started ...");
				while(!sSocket.isClosed() && null!=sSocket){
					try{
						Socket socket = sSocket.accept();
						socket.setSoTimeout(5000);
						audioPlay(socket);
					}catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(sSocket!=null)	
			               sSocket.close();					
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//用来启动音频播放子线程
		public void audioPlay(Socket socket){
			new AudioPlay(socket).start();
		}
		//用来启动音频发送子线程
		public  void audioSend(){
			new AudioSend().start();
		}
		
		//音频播线程
		public class AudioPlay extends Thread{
			Socket socket = null;
			public AudioPlay(Socket socket){
				this.socket = socket;
			//	android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO); 
			}
			
			@Override
			public void run() {
				super.run();
				try {
					InputStream is = socket.getInputStream();
					//获得音频缓冲区大小
					int bufferSize = android.media.AudioTrack.getMinBufferSize(8000,
							AudioFormat.CHANNEL_CONFIGURATION_MONO,
							AudioFormat.ENCODING_PCM_16BIT);

					//获得音轨对象
					AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC, 
							8000,
							AudioFormat.CHANNEL_CONFIGURATION_MONO,
							AudioFormat.ENCODING_PCM_16BIT,
							bufferSize,
							AudioTrack.MODE_STREAM);

					//设置喇叭音量
					player.setStereoVolume(1.0f, 1.0f);
					//开始播放声音
					player.play();
					byte[] audio = new byte[160];//音频读取缓存
					int length = 0;
					
					while(!isStopTalk){
						length = is.read(audio);//从网络读取音频数据
						if(length>0 && length%2==0){
						//	for(int i=0;i<length;i++)audio[i]=(byte)(audio[i]*2);//音频放大1倍
							player.write(audio, 0, length);//播放音频数据
						}
					}
					player.stop();
					is.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//音频发送线程
		public class AudioSend extends Thread{
			
			private String ip = "";
//			Person person = null;
			
			public AudioSend(){
//				this.person = person;
			//	android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO); 
			}
			@Override
			public void run() {
				super.run();
				Socket socket = null;
				OutputStream os = null;
				AudioRecord recorder = null;
				try {
					Log.d(this.getClass().getName(), "ip address : " + ip);
					socket = new Socket(ip, Constant.AUDIO_PORT);
					socket.setSoTimeout(5000);
					os = socket.getOutputStream();
					//获得录音缓冲区大小
					int bufferSize = AudioRecord.getMinBufferSize(8000,
							AudioFormat.CHANNEL_CONFIGURATION_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
					
					//获得录音机对象
					recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
							8000,AudioFormat.CHANNEL_CONFIGURATION_MONO,
							AudioFormat.ENCODING_PCM_16BIT,
							bufferSize*10);
					
					recorder.startRecording();//开始录音
					byte[] readBuffer = new byte[640];//录音缓冲区
					
					int length = 0;
					
					while(!isStopTalk){
						length = recorder.read(readBuffer,0,640);//从mic读取音频数据
						if(length>0 && length%2==0){
							os.write(readBuffer,0,length);//写入到输出流，把音频数据通过网络发送给对方
						}
					}
					recorder.stop();
					os.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void release() {
			try {
				System.out.println("Audio handler socket closed ...");
				if(null!=sSocket)sSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
