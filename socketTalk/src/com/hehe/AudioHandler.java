package com.hehe;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import sun.audio.AudioData;
import sun.audio.AudioDataStream;
import sun.audio.AudioPlayer;
import sun.audio.ContinuousAudioDataStream;

public class AudioHandler extends Thread {
	private ServerSocket sSocket = null;
	private boolean isStopTalk = false;
	final int bufSize = 16384;
	SourceDataLine line;

	public void setStopTalk(boolean isStopTalk) {
		this.isStopTalk = isStopTalk;
	}

	// private G711Codec codec;
	public AudioHandler() {
	}

	@Override
	public void run() {
		super.run();
		try {
			sSocket = new ServerSocket(Constant.AUDIO_PORT);// 监听音频端口
			System.out.println("Audio Handler socket started ...");

			while (!sSocket.isClosed() && null != sSocket) {
				try {
					Socket socket = sSocket.accept();
					socket.setSoTimeout(5000);
					audioPlay(socket);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (sSocket != null)
						sSocket.close();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 用来启动音频播放子线程
	public void audioPlay(Socket socket) {
		new AudioPlay(socket).start();
	}

	// 用来启动音频发送子线程

	// 音频播线程
	public class AudioPlay extends Thread {
		Socket socket = null;

		public AudioPlay(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			super.run();
			try {
				// try {
				// // 用输入流打开一音频文件
//				 InputStream in = socket.getInputStream();
////				 AudioSystem.getAudioInputStream(new DataInputStream(in));
//				//
//				// // 从输入流中创建一个AudioStream对象
//				 AudioStream as = new AudioStream( in);
				//
				// // AudioSystem.getAudioInputStream(in );
				// // AudioPlayer.player.start(as);// 用静态成员player.start播放音乐
				// // AudioPlayer.player.stop(as);//关闭音乐播放
				// // 如果要实现循环播放，则用下面的三句取代上面的“AudioPlayer.player.start(as);”这句
				//
//				 AudioData data1 = as.getData();
//				 ContinuousAudioDataStream gg= new ContinuousAudioDataStream
//				 (data1);
//				 AudioPlayer.player.start(gg );// Play audio. 　　
				// // 如果要用一个 URL 做为声音流的源(source)，则用下面的代码所示替换输入流来创建声音流：
				// /*
				// * AudioStream as = new AudioStream (url.openStream()); 　　
				// */
				// } catch (FileNotFoundException e) {
				// e.printStackTrace();
				// System.out.print("FileNotFoundException ");
				// } catch (IOException e) {
				// e.printStackTrace();
				// System.out.println("有错误!");
				// }

				System.out.println("-------->>>>>>>>>>>> begin-----------");
				
				InputStream is = socket.getInputStream();
				// //获得音频缓冲区大小
//				int bufferSize = android.media.AudioTrack.getMinBufferSize(
//						8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//						AudioFormat.ENCODING_PCM_16BIT);
//
//				// 获得音轨对象
//				AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC,
//						8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
//						AudioFormat.ENCODING_PCM_16BIT, bufferSize,
//						AudioTrack.MODE_STREAM);
//
//				// 设置喇叭音量
//				player.setStereoVolume(1.0f, 1.0f);
//				// 开始播放声音
//				player.play();
				byte[] audio = new byte[160];// 音频读取缓存
				int length = 0;
				while (!isStopTalk) {
//					AudioStream as = new AudioData();
					length = is.read(audio);// 从网络读取音频数据
					if (length > 0 && length % 2 == 0) {
 						for (int i = 0; i < length; i++){
 							audio[i] = (byte) (audio[i] * 2);// 音频放大1倍
 						}
//						player.write(audio, 0, length);// 播放音频数据
 						System.out.println("length : " + length);
						AudioData data = new AudioData(audio);
						ContinuousAudioDataStream gg= new ContinuousAudioDataStream(data);
						AudioDataStream cc = new AudioDataStream(data);
//						format = stream.getFormat();
//						AudioPlayer.player.start(gg);
						AudioPlayer.player.start(cc);
//						play(is);
					}else{
						isStopTalk = true;
						 System.out.println("length : " + length);
					}
				}
				 System.out.println("------------>>>>>>>>>end<<<<<<<<------------");
//				player.stop();
				is.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
   private AudioFormat format = new AudioFormat(
		        AudioFormat.Encoding.PCM_SIGNED, 32000.0f, 8, 1, 2, 32000.0f, false);;
	 public void play(InputStream source) {

	        // use a short, 100ms (1/10th sec) buffer for real-time
	        // change to the sound stream
	        int bufferSize = format.getFrameSize() *
	            Math.round(format.getSampleRate() / 10);
	        System.out.println("bufferSize : "+ bufferSize);
//	        bufferSize = 640;
	          
	        byte[] buffer = new byte[bufferSize];

	        // create a line to play to
	        SourceDataLine line;
	        try {
	            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	            line = (SourceDataLine)AudioSystem.getLine(info);
	            line.open(format, bufferSize);
	        }
	        catch (LineUnavailableException ex) {
	            ex.printStackTrace();
	            return;
	        }

	        // start the line
	        line.start();

	        // copy data to the line
	        try {
	            int numBytesRead = 0;
	            while (numBytesRead != -1) {
	                numBytesRead =
	                    source.read(buffer, 0, buffer.length);
	                if (numBytesRead != -1) {
	                   line.write(buffer, 0, numBytesRead);
	                }
	            }
	        }
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }

	        // wait until all data is played, then close the line
	        line.drain();
	        line.close();

	    }
}
