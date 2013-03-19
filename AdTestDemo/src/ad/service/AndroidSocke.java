package ad.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class AndroidSocke implements Runnable {

	public static final String SERVERIP = "110.149";
	public static final int SERVERPORT = 51706;
	public void run() {
		try {
			System.out.println("S: Connecting...");
			ServerSocket serverSocket = new ServerSocket(SERVERPORT);
			while (true) {

				Socket client = serverSocket.accept();
				System.out.println("S: Receiving...");
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					System.out.println("S: Received: '" + str + "'");
				} catch (Exception e) {
					System.out.println("S: Error");
					e.printStackTrace();
				} finally {
					client.close();
					System.out.println("S: Done.");
				}
			}

		} catch (Exception e) {
			System.out.println("S: Error");
			e.printStackTrace();
		}

	}

	public static void main(String a[]) {

		Thread desktopServerThread = new Thread(new AndroidSocke());

		desktopServerThread.start();

	}
}
