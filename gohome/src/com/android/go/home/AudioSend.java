package com.android.go.home;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioSend extends Thread{
	
	private String ip = "192.168.1.137";
	
    private boolean isStopTalk = false; 
	
	public void setStopTalk(boolean isStopTalk) {
		this.isStopTalk = isStopTalk;
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
			Log.d("send---", "bufferSize : " +bufferSize);
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
