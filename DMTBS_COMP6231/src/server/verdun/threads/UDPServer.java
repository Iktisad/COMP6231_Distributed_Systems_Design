package server.verdun.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.json.JSONObject;

import server.verdun.AdminInterface;
import server.verdun.CustomerInterface;
import server.verdun.data.Data;

public class UDPServer implements Runnable {

	public final int port = 5002;
	private AdminInterface adminImp;
	private CustomerInterface cusImp;
	
	public UDPServer(AdminInterface adminImp, CustomerInterface cusImp){
		this.adminImp = adminImp;
		this.cusImp = cusImp;
	}
	@SuppressWarnings("resource")
	public void run() 	
	{
		//String received="";
		Data.seedData();
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] receive = new byte[1000];
		//receive=Integer.toString(i).getBytes();
		while(true)
		{

			{
				//receiving request
				DatagramPacket request = new DatagramPacket(receive, receive.length);
				try {
					socket.receive(request);
					byte[] received = request.getData();
					
					//first find the byte length
					int byteLength = 0;
					while(received[byteLength]!=0) {
						byteLength++;
					}
					// copy the byte to new length
					byte [] trimData = new byte[byteLength];
					for (int i = 0; i < trimData.length; i++) {
						trimData[i] = received[i];
					}
					
					
					String method = new String(trimData, 0, trimData.length);
					System.out.println();
					String json ="";
					String ctx ="";
					if(method.indexOf(':')>-1) {
						String [] param = method.split(":");
						ctx = param[0];
						method = param[1];
					}
					
					System.out.println(method);
					
					switch (method) {
					case "listMovieShowsAvailability":
						json= JSONObject.valueToString(Data.processHashmap());
						System.out.println(json);
						break;
												
					case "getBookingSchedule":
						json = Data.constructCustomerMovieSchedule(ctx);
						System.out.println(json);
						break;
					case "bookInVerdun":
						String [] movieParams = ctx.split(",");
						String customerID = movieParams[0];
						String movieID = movieParams[1];
						String movieName = movieParams[2];
						int numberOfTickets = Integer.parseInt(movieParams[3]);
	
						json = cusImp.bookMovieTickets(customerID, movieID, movieName, numberOfTickets);
						System.out.println(Data.processHashmap().toString(4));
						break;
					default:
						break;
					}
						
					//	sending request
					byte[] buffer = json.getBytes();
					InetAddress ia = InetAddress.getLocalHost();
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length,ia,request.getPort());
					socket.send(reply);   
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	
	}

	
}
