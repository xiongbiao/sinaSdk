package ad.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
 
public class UDPServer implements Runnable {
 
    private static final int PORT = 6001;
 
    private byte[] msg = new byte[1024*10];
 
    private boolean life = true;
 
    public UDPServer() {
    }
 
    /**
     * @return the life
     */
    public boolean isLife() {
        return life;
    }
 
    /**
     * @param life
     *            the life to set
     */
    public void setLife(boolean life) {
        this.life = life;
    }
 
    @Override
    public void run() {
        DatagramSocket dSocket = null;
        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
        try {
            dSocket = new DatagramSocket(PORT);
            while (life) {
                try {
                    dSocket.receive(dPacket);
                   System.out.println("msg sever received --->> "+ new String(dPacket.getData(),"utf-8"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws IOException {
    	 // 开启服务器
        ExecutorService exec = Executors.newCachedThreadPool();
        UDPServer server = new UDPServer();
        exec.execute(server);
    }
}