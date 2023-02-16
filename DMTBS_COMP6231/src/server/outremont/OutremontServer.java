package server.outremont;

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
import org.json.JSONObject;


public class OutremontServer {

	static JSONObject processHashmap() {
		//		System.out.println(OutremontData.ticketBookings);
		JSONObject movieJSON = new JSONObject();
//		JSONArray obj = new JSONArray();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : OutremontData.ticketBookings.entrySet()) {
			String key = entry.getKey();
			HashMap<String, MovieDetails> value = entry.getValue();

			JSONObject movieIdJSON = new JSONObject();
			for(Map.Entry<String, MovieDetails> movieId : value.entrySet()) {
				// another hashmap
				movieIdJSON.put(movieId.getKey(), new JSONObject(movieId.getValue().getMovieMapObject()));


				//				movieIdJSON.put(movieId.getKey(), movieId.getValue().getCustomerList())
			}
//			movieJSON.put(key, obj.put(movieIdJSON));
			movieJSON.put(key, movieIdJSON);
		}
		//		System.out.println(movieJSON);

		return movieJSON;
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// need to figure out how to send the customer or admin to adwater Implementor
		CustomerOutremontInterface outc = new OutremontServerImplementor();
		AdminOutremontInterface outa = new OutremontServerImplementor();
		//seed data
		new OutremontData();
		System.out.println("Outremont server running....");

		LocateRegistry.createRegistry(5002);
		Naming.rebind("rmi://localhost:5002"+"/Outremont/customer", outc);
		Naming.rebind("rmi://localhost:5002"+"/Outremont/admin", outa);
		DatagramSocket socket;
		try {
			// @SuppressWarnings("resource")
			socket = new DatagramSocket(5012);
			//UDP server runs here and listens to any incoming request
			while (true) {
				// request from client
				DatagramPacket request = new DatagramPacket(new byte[30], 30);
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
					json = JSONObject.valueToString(OutremontServerImplementor.constructCustomerMovieSchedule(customerID));
					break;
				default:
					System.out.println("nothing fetched");
					break;
				}
				
				// get movie schedules movie for OutremontServer
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
