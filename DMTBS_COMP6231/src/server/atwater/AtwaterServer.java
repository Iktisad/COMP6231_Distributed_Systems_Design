package server.atwater;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import server.verdun.MovieDetails;
import server.verdun.VerdunData;


public class AtwaterServer {
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
		CustomerAtwaterInterface atwc = new AtwaterServerImplementor();
		AdminAtwaterInterface atwa = new AtwaterServerImplementor();
		new AtwaterData();
		System.out.println("Atwater server running....");

		LocateRegistry.createRegistry(5001);
		Naming.rebind("rmi://localhost:5001"+"/Atwater/customer", atwc);
		Naming.rebind("rmi://localhost:5001"+"/Atwater/admin", atwa);
		
		DatagramSocket socket;
		try {
			// @SuppressWarnings("resource")
			socket = new DatagramSocket(5011);
			//UDP serv er runs here and listens to any incoming request
			while (true) {
				// request from client
				DatagramPacket request = new DatagramPacket(new byte[1], 1);
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
				String json ="";
				String customerID ="";
				if(method.indexOf(':')>-1) {
					String [] param = method.split(":");
					method = param[0];
					customerID = param[1];
				}
				
				switch (method) {
				case "listMovieShowsAvailability":
					json = JSONObject.valueToString(processHashmap());
					
					break;
				case "getBookingSchedule":
					json = JSONObject.valueToString(AtwaterServerImplementor.constructCustomerMovieSchedule(customerID));
					break;
				default:
					break;
				}				// get movie schedules movie for VerdunServer
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
