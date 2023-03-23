package server.outremont.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class UDPClient implements Runnable {
	// customer id or movie id
	String ctx;
//	String serverKey;
	String method;
	int port;
	private String json;
	//public boolean ready = false;
	HashMap<String, Integer> servers = new HashMap<>();
	
	public UDPClient(String ctx,String method, String serverKey) {
		
		this.initServers();
		this.ctx= ctx;
		this.port= servers.get(serverKey);
		this.method=method;
	
	}
	
	private void initServers() {
		this.servers.put("ATW", 5000);
		this.servers.put("OUT", 5001);
		this.servers.put("VER", 5002);
	}
	@Override
	public void run(){ 
		System.out.println("Sending from Atwater");
		System.out.println(port);
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			String str = ctx + ':' + method +":"+"error";
			byte [] send = str.getBytes();
			InetAddress dstAddress = InetAddress.getLocalHost();
			DatagramPacket request =new DatagramPacket(send, send.length, dstAddress,port);

			socket.send(request);

			//Receiving Request
			byte[] receive = new byte[1000];
			DatagramPacket reply = new DatagramPacket(receive, receive.length);
			socket.receive(reply);
			//byte  d1[]=(reply.getData());
			this.json= new String(reply.getData(),0,reply.getLength());

		}
		catch (SocketException e)
		{	System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		finally 
		{
			if(socket != null)
				socket.close();
		}
	}
	public String getValue() {
        return json;
    }
}
