package com.hehe;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.media.Buffer;
import javax.media.CannotRealizeException;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.IncompatibleSourceException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.Player;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.control.FrameGrabbingControl;
import javax.media.control.StreamWriterControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.util.BufferToImage;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CaptureAudio {
	Vector deviceList = new Vector();// 用于保存设备
	Vector<CaptureDeviceInfo> audioDevices = new Vector<CaptureDeviceInfo>();// 保存音频设备
	Vector<CaptureDeviceInfo> videoDevices = new Vector<CaptureDeviceInfo>();// 保存视频设备
	MediaPlayer audioplayer = null;// 音频播放器
	MediaPlayer videoplayer = null;// 视频播放器
	Player dualPlaye = null;// 能同时播放音频和视频
	MediaLocator audiolocator = null;// 音频设备的位置信息
	MediaLocator videolocator = null;// 视频设备的位置信息
	javax.media.Processor vprocessor = null;// 媒体处理器，用于保存视频
	DataSink filewriter = null;// 保存视频数据池
	CaptureDeviceInfo audiocaptureInfo, videocaptureInfo;// 设备信息
	{
		audioplayer = new MediaPlayer();// 初始化播发器
		videoplayer = new MediaPlayer();
	}

	public Processor getVprocessor() {
		return this.vprocessor;
	}

	public Player getDualPlaye() {
		return this.dualPlaye;
	}

	public MediaPlayer getVideoplayer() {
		return this.videoplayer;
	}

	public MediaPlayer getAudioplayer() {
		return this.audioplayer;
	}

	public CaptureAudio() {// 读取设备列表
		deviceList = CaptureDeviceManager.getDeviceList(null);
		if (deviceList != null & deviceList.size() > 0) {
			int deviceCount = deviceList.size();
			Format[] formats;
			for (int i = 0; i < deviceCount; i++) {
				CaptureDeviceInfo cdi = (CaptureDeviceInfo) deviceList
						.elementAt(i);
				formats = cdi.getFormats();
				System.out.println(cdi.getName());
				for (int j = 0; j < formats.length; j++) {
					if (formats[j] instanceof AudioFormat) {
						audioDevices.addElement(cdi);// 音频设备
						break;
					} else if (formats[j] instanceof VideoFormat) {
						videoDevices.addElement(cdi);// 视频设备
						break;
					}
				}
			}
		}
	}

	public Vector<CaptureDeviceInfo> getAudioDevices() {
		return this.audioDevices;
	}

	public Vector<CaptureDeviceInfo> getVideoDevices() {
		return this.videoDevices;
	}

	public void play(int audio, int video) {// 开如捕捉视频
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));
		audiocaptureInfo = audioDevices.get(audio);
		videocaptureInfo = videoDevices.get(video);
		audiolocator = audiocaptureInfo.getLocator();
		videolocator = videocaptureInfo.getLocator();
		videoplayer.setMediaLocator(videolocator);
		videoplayer.start();
		audioplayer.setMediaLocator(audiolocator);
		audioplayer.start();
	}

	public void stop() {// 停止捕捉
		videoplayer.stop();
		videoplayer.close();
		audioplayer.stop();
		audioplayer.close();
	}

	public void playandsave(int audio, int video) {// 能同时播放音频和视频
		DataSource[] dataSources = new DataSource[2];
		audiocaptureInfo = audioDevices.get(audio);
		videocaptureInfo = videoDevices.get(video);
		audiolocator = audiocaptureInfo.getLocator();
		videolocator = videocaptureInfo.getLocator();
		try {
			Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));
			dataSources[0] = Manager.createDataSource(audiolocator);
			dataSources[1] = Manager.createDataSource(videolocator);
			DataSource ds = Manager.createMergingDataSource(dataSources);
			dualPlaye = Manager.createPlayer(ds);
			dualPlaye.start();
		} catch (NoDataSourceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IncompatibleSourceException e) {
			e.printStackTrace();
		} catch (NoPlayerException e) {
			e.printStackTrace();
		}
	}

	public void savevideo(String type) {// 保存视频
		try {
			Format formats[] = new Format[2];
			FileTypeDescriptor outputType = null;
			if (type.equals("avi")) {// 判断输出的文件类型格式
				formats[0] = new AudioFormat(AudioFormat.IMA4_MS);
				formats[1] = new VideoFormat(VideoFormat.CINEPAK);// 设置为AVI格式
				outputType = new FileTypeDescriptor(FileTypeDescriptor.MSVIDEO);
			} else if (type.equals("mov")) {
				formats[0] = new AudioFormat(AudioFormat.IMA4);
				formats[1] = new VideoFormat(VideoFormat.CINEPAK);
				outputType = new FileTypeDescriptor(
						FileTypeDescriptor.QUICKTIME);// QuickTime格式
			}
			// 创建ProcessorModel,参数为数据轨道输出格式和文件输出类型
			// 由ProcessorModel对象创建Processor对象
			this.vprocessor = Manager
					.createRealizedProcessor(new ProcessorModel(formats,
							outputType));
			this.vprocessor.configure();
			System.out.println("正在配置处理器，请稍后......");
			this.vprocessor.realize();
			// 获取Processor的输出
			DataSource source = vprocessor.getDataOutput();
			File ff = new File("d:/vidio." + type);
			// 建立一个记录保存文件的MediaLocator
			MediaLocator dest = new MediaLocator(ff.toURL());
			// 创建数据池
			filewriter = Manager.createDataSink(source, dest);
			filewriter.open();
			// 设置Processor控制生成文件的大小，只要调用Processor的StreamWriterControl
			StreamWriterControl swc = (StreamWriterControl) vprocessor
					.getControl("javax.media.control.StreamWriterControl");
			// 设置生成文件大小最大5M
			if (swc != null)
				swc.setStreamSizeLimit(5000000);
			filewriter.start();// 开始保存数据文件，启动处理器
			this.vprocessor.start();
			System.out.println(this.vprocessor.getControlPanelComponent());
		} catch (NoProcessorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoDataSinkException e) {
			e.printStackTrace();
		} catch (CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void saveaspic() {// 摄像头拍照，保存为图片
		FrameGrabbingControl fgc = (FrameGrabbingControl) this.getVideoplayer()
				.getControl("javax.media.control.FrameGrabbingControl");
		Buffer buf = fgc.grabFrame();
		// 获取当前祯并存入Buffer类
		BufferToImage bio = new BufferToImage((VideoFormat) buf.getFormat());
		Image img = bio.createImage(buf);
		// 创建image图像缓冲区
		BufferedImage bi = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		// 根据BufferedImage对象创建Graphics2D对象
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, null, null);
		FileOutputStream out = null;
		java.util.Random rand = new java.util.Random();// 随机产生文件名
		File fout = new File("d:/Cameravideo" + rand.nextInt() + ".jpg");
		try {
			out = new FileOutputStream(fout);
			// 转换成JPEG图像格式
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
			param.setQuality(1f, false);// 不压缩图像
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bi);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRTP(String type, int drive, String ip) {// 发送实时媒体数据
		DataSource[] dataSources = new DataSource[1];
		videocaptureInfo = videoDevices.get(drive);
		videolocator = videocaptureInfo.getLocator();
		try {
			dataSources[0] = Manager.createDataSource(videolocator);
			DataSource ds = dataSources[0];
			this.vprocessor = Manager.createProcessor(ds);
			this.vprocessor.configure();
			while (this.vprocessor.getState() != this.vprocessor.Configured)
				System.out.println("正在配置处理器，请稍后......");
			ContentDescriptor cd = new ContentDescriptor(
					ContentDescriptor.RAW_RTP);
			vprocessor.setContentDescriptor(cd);
			// 列出所有的和数据轨道相关的控制器
			TrackControl track[] = vprocessor.getTrackControls();
			boolean programmed = false;
			// 遍历这些控制器，找到可以控制输出格式的控制器
			for (int i = 0; i < track.length; i++) {
				Format format = track[i].getFormat();
				if (track[i].isEnabled() && format instanceof VideoFormat
						&& !programmed) {
					// 找到了数据轨道的格式控制器之后，设置输出格式，输出格式输出为VideoFormat.JPEG_RTP>。
					Dimension size = ((VideoFormat) format).getSize();
					float frameRate = ((VideoFormat) format).getFrameRate();
					int w = (size.width % 8 == 0 ? size.width
							: (int) (size.width / 8) * 8);
					int h = (size.height % 8 == 0 ? size.height
							: (int) (size.height / 8) * 8);
					VideoFormat jpegFormat = new VideoFormat(
							VideoFormat.JPEG_RTP, new Dimension(w, h),
							Format.NOT_SPECIFIED, Format.byteArray, frameRate);
					track[i].setFormat(jpegFormat);
					System.err.println("Video transmitted as:");
					System.err.println(" " + jpegFormat);
					programmed = true;
				} else
					track[i].setEnabled(false);
			}
			while (this.vprocessor.getState() != this.vprocessor.Realized) {
				System.out.println("正在配置处理器，请稍后......");
				this.vprocessor.realize();
			}
			// 获取Processor的输出
			DataSource source = vprocessor.getDataOutput();
			// 可以把这个数据源作为参数传递给manager,通过manager创建一个RTP的数据池。
			// 先设置一个发送数据流的多播目的网址
			String url = "rtp://" + ip + ":49150/audio";
			MediaLocator mDest = new MediaLocator(url);
			// 确定了数据源和发送地址，可以建立数据池DataSink了,建立成功后调用
			// open()和start()方法，就可以在网上以多播方式发送捕获的内容了。
			filewriter = Manager.createDataSink(source, mDest); // 创建数据池
			filewriter.open();
			filewriter.start();// 启动处理器
			source.start();
			vprocessor.start();
			System.out.println(this.vprocessor.getControlPanelComponent());
		} catch (NoProcessorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoDataSinkException e) {
			e.printStackTrace();
		} catch (NoDataSourceException e) {
			e.printStackTrace();
		}
	}

	public void recRTP(String ip) {
		String url = "rtp://" + ip + ":49150/audio";
		// 实现接收单个数据流
		MediaLocator mrl = new MediaLocator(url);
		if (mrl == null) {
			System.err.println("Can't build MRL for RTP");
			return;
		}
		// 根据MediaLocator创建Player
		try {
			this.dualPlaye = Manager.createPlayer(mrl);
			if (dualPlaye != null) {
				dualPlaye.realize();
				dualPlaye.start();
			}
		} catch (NoPlayerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recMutilRTP(String ip) {
		// 实现同时接收多个数据流的方式
		String url = "rtp://" + ip;
//		RTPUtil util = new RTPUtil();
//		util.createManager(url, 49150, 255, true, true);
	}

	public void stopsave() {// 终止保存
		try {
			this.filewriter.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.filewriter.close();
		this.vprocessor.stop();
		this.vprocessor.close();
	}
}
