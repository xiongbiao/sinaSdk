package com.hehe;


import java.net.*;

import javax.sound.sampled.*;



/** 

* 把接收到的信息传到麦克，即播放

*/


public class ChartReceive extends Thread {

   //格式

   private AudioFormat format = new AudioFormat(

        AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 44100.0f, false);


   //管道

   private SourceDataLine line;

   private byte[] data;


   public ChartReceive() {

      try {

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        line = (SourceDataLine) AudioSystem.getLine(info);

      } catch (Exception e) {

        e.printStackTrace();

      }

   }


   public void run() {

      System.out.println("receive threading start");

      int length=(int)(format.getFrameSize()*format.getFrameRate()/2.0f);

      try{line.open(format);line.start();

        DatagramSocket socket=new DatagramSocket(Constant.AUDIO_PORT);

        while(true){

           //数组的创建载什么时候，是否影响数据信息？

           data=new byte[length];

           DatagramPacket dp=new DatagramPacket(data,data.length);

           socket.receive(dp);

           line.write(data,0,data.length);

           System.out.println("receive success "+new String(data,"UTF-8"));

        }

      }catch(Exception e){

        e.printStackTrace();

      }

   }

}
