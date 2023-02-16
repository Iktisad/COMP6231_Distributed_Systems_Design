package server.verdun;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class OutremontThread implements Runnable {

	private String json;

	public OutremontThread() {
		this.json = "{}";
	}

	@Override
	public void run() {
		
		int port = 5012;

		try {
			InetAddress verdunAddress = InetAddress.getLocalHost();
			@SuppressWarnings("resource")
			DatagramSocket socket = new DatagramSocket();
			// DatagramPacket(payload/buffer , payload length/buffer length, dest addr, dest port) 
			DatagramPacket request = new DatagramPacket(new byte[1], 1, verdunAddress, port);
			socket.send(request);			
			//store response
			byte[] buffer = new byte[2048];
			DatagramPacket response = new DatagramPacket(buffer, buffer.length);
			socket.receive(response);
			json = new String(buffer, 0, response.getLength());

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getShowsList() {
		return this.json;
	};

}
