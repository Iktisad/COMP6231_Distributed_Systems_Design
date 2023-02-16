package server.verdun;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


public class VerdunServer {
	static JSONObject processHashmap() {
//		System.out.println(VerdunData.ticketBookings);
		JSONObject movieJSON = new JSONObject();
//		JSONArray obj = new JSONArray();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : VerdunData.ticketBookings.entrySet()) {
			String key = entry.getKey();
			HashMap<String, MovieDetails> value = entry.getValue();
			
			JSONObject movieIdJSON = new JSONObject();
			for(Map.Entry<String, MovieDetails> movieId : value.entrySet()) {
				// another hashmap
				movieIdJSON.put(movieId.getKey(), new JSONObject(movieId.getValue().getMovieMapObject()));
				
				
//				movieIdJSON.put(movieId.getKey(), movieId.getValue().getCustomerList())
			}
			movieJSON.put(key, movieIdJSON);
//			movieJSON.put(key, obj.put(movieIdJSON));
		}
//		System.out.println(movieJSON);
			
		return movieJSON;
	}
	
	

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// need to figure out how to send the customer or admin to adwater Implementor
		CustomerVerdunInterface verc = new VerdunServerImplementor();
		AdminVerdunInterface vera = new VerdunServerImplementor();
		//seed data
		new VerdunData();
		System.out.println("Verdun server running....");

		LocateRegistry.createRegistry(5003);
		Naming.rebind("rmi://localhost:5003"+"/Verdun/customer", verc);
		Naming.rebind("rmi://localhost:5003"+"/Verdun/admin", vera);
		DatagramSocket socket;
		try {
			// @SuppressWarnings("resource")
			socket = new DatagramSocket(5013);
			//UDP server runs here and listens to any incoming request
			while (true) {
				// request from client
				DatagramPacket request = new DatagramPacket(new byte[30], 30);
				socket.receive(request);
				byte[] received = request.getData();
//				byte[] trimData = trim(received);
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
				String json ="";
				String customerID ="";
				System.out.println(method);
				if(method.indexOf(':')>-1) {
					String [] param = method.split(":");
					method = param[0];
					customerID = param[1];
					System.out.println(Arrays.toString(param));
				}
				
				switch (method) {
				case "listMovieShowsAvailability":
					json = JSONObject.valueToString(processHashmap());
					System.out.print("Returned show availabily:");
					System.out.println(json);
					
					break;
				case "getBookingSchedule":
					json = JSONObject.valueToString(VerdunServerImplementor.constructCustomerMovieSchedule(customerID));
					break;
				default:
					System.out.println("nothing fetched");
					break;
				}
				
				// get movie schedules movie for VerdunServer
				byte [] buffer = json.getBytes();
				
				
				// response to client 
				InetAddress clientAddress = request.getAddress();
				int clientPort = request.getPort();
				DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
				socket.send(response);
				
				System.out.println(new String(response.getData(), 0, response.getData().length));
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
}
