package server.verdun.customer_threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AtwaterCustomerThread implements Runnable{
	private String json;
	private String customerID;
	public AtwaterCustomerThread(String customerID) {
		this.json = "{}";
		this.customerID = customerID;
	}

	@Override
	public void run() {
		
		int port = 5011;

		try {
			InetAddress verdunAddress = InetAddress.getLocalHost();
			@SuppressWarnings("resource")
			DatagramSocket socket = new DatagramSocket();
			String message = "getBookingSchedule:"+customerID;
			byte[] payload = message.getBytes();
			// DatagramPacket(payload/buffer , payload length/buffer length, dest addr, dest port) 
			DatagramPacket request = new DatagramPacket(payload, payload.length, verdunAddress, port);
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

	public String getCustomerSchedule() {
		return this.json;
	};
}
