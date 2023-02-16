package server.verdun;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import java.util.Map;
import org.json.JSONObject;

import server.verdun.customer_threads.AtwaterCustomerThread;
import server.verdun.customer_threads.OutremontCustomerThread;

public class VerdunServerImplementor extends UnicastRemoteObject implements CustomerVerdunInterface, AdminVerdunInterface {
	
	protected VerdunServerImplementor() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -4350691634355167593L;
	protected String serverInitials = "VER";
	
	private String toJSONString(boolean success, int status, String message) {
		JSONObject obj = new JSONObject();
		obj.put("succss", success);
		obj.put("status", status);
		obj.put("message", message);
		
		return JSONObject.valueToString(obj);
	}

	@Override
	public String addMovieSlots(String movieID, String movieName, int bookingCapacity) throws RemoteException {
		if(!movieName.matches("[0-9A-Za-z]*") || movieName.isBlank()|| bookingCapacity <= 0) {
			System.out.println("Bad Request: malformed data received");
			
			return toJSONString(false, 400, "Bad Request: malformed data received");
		};
		if(!VerdunData.ticketBookings.containsKey(movieName)) {
			VerdunData.ticketBookings.put(movieName, new HashMap<String, MovieDetails>());
			VerdunData.ticketBookings.get(movieName).put(movieID, new MovieDetails(bookingCapacity));
			System.out.println("New Movie " + movieName + "created with movie ID " + movieID + " with capacity " + bookingCapacity);

			return toJSONString(true, 201,"\"New Movie \" + movieName + \"created with movie ID \" + movieID + \" with capacity \" + bookingCapacity");
		}
		
		if(VerdunData.ticketBookings.containsKey(movieName) && 
				VerdunData.ticketBookings.get(movieName).containsKey(movieID)) {
			VerdunData.ticketBookings.get(movieName).get(movieID).setBookingCapacity(bookingCapacity);
			System.out.println("Movie Booking Capacity updated for :"+movieName+" ID : "+ movieID);
			VerdunData.ticketBookings.get(movieName).put(movieID, new MovieDetails(bookingCapacity));
			return toJSONString(true, 200, "\"Movie Booking Capacity updated for :\"+movieName+\" ID : \"+ movieID");
		
		}
		VerdunData.ticketBookings.get(movieName).put(movieID, new MovieDetails(bookingCapacity));
		
		System.out.println("Admin has added the movie ID "+ movieID+" to "
				+ movieName +" with capacity " + bookingCapacity);
		System.out.println(VerdunData.ticketBookings.toString());
		
		return toJSONString(true, 200, "Admin has added the movie ID "+ movieID+" to "
				+ movieName +" with capacity " + bookingCapacity);
	}
	
	private Date movieIdDateConverter(String movieID) {
        movieID = movieID.substring(4, movieID.length());
        movieID = movieID.substring(0,2) + "/" +
		movieID.substring(2,4) + "/20" + movieID.substring(4);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date format = null;
        try {
            format = formatter.parse(movieID);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return format;
    }

	
	@Override
	public String removeMovieSlots(String movieID, String movieName) throws RemoteException {
		// delete movie with the given movieName and movieID
		System.out.println(movieID+" : " +movieName);

		if(!VerdunData.ticketBookings.containsKey(movieName)) {
			System.out.println("Movie does not exist so no update performed");
			//return "404 no movie found!";
			return toJSONString(false, 404, "Movie Not Found!");
		}
		// 
		// below all movies exist a movie exists but needs to be checked if movie ID exists or not
		if(VerdunData.ticketBookings.get(movieName).containsKey(movieID)) {
			// TODO check if the movie is not before the current date, then Admin cannot delete the movie

			// check if the ticket booking is before current date
			Date cancellingDate = movieIdDateConverter(movieID);


			if(cancellingDate.before(new Date())) {
				//						return "Not Authorized";
				return toJSONString(false, 401, "Not Authorized to delete movie before current date");
			}
			// check if any customer has booked this movie or not
			if(VerdunData.ticketBookings.get(movieName).get(movieID).getCustomerCount()> 0 ) {
				// hold customers in a temporary variable
				String[] customerTemp = VerdunData.ticketBookings.get(movieName).get(movieID).getCustomerList();
				int capacity = VerdunData.ticketBookings.get(movieName).get(movieID).getCapcity();
				// find the next available slot for the same theatre;
				//

				//						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now().plusDays(5);
				String date = now.getDayOfMonth()<10? "0"+now.getDayOfMonth(): ""+ now.getDayOfMonth();
				String month = now.getMonthValue()< 10? "0"+ now.getMonthValue(): now.getMonthValue()+"";
				int year = now.getYear() - 2000;
				String nMovieId = movieID.substring(0,4);
				nMovieId = nMovieId.concat(date+month+year);

				addMovieSlots(nMovieId, movieName, capacity);
				System.out.println("Customers shifted to new schedule");
				VerdunData.ticketBookings.get(movieName).get(nMovieId).addCustomer(customerTemp);


				//nMovieId
			}
		}
		VerdunData.ticketBookings.get(movieName).remove(movieID);

		return toJSONString(true, 204, "Movie slot successfully deleted");
	}
	private JSONObject processHashmap() {
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
				
			}
//			movieJSON.put(key, obj.put(movieIdJSON));
			movieJSON.put(key, movieIdJSON);
		}
//		System.out.println(movieJSON);
			
		return movieJSON;
	}
	@Override
	public String listMovieShowsAvailability(String movieName) throws RemoteException {
		JSONObject collection=new JSONObject();
		AtwaterThread atwObj = new AtwaterThread();
		OutremontThread outObj = new OutremontThread();
		Thread t1 = new Thread(atwObj);
		Thread t2 = new Thread(outObj);
		JSONObject verJSON = processHashmap();
		try {
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			
			JSONObject atwJSON = new JSONObject(atwObj.getShowsList());
			JSONObject outJSON = new JSONObject(outObj.getShowsList());
			
			collection.put("Atwater",atwJSON.get(movieName));
			collection.put("Outremont",outJSON.get(movieName));
			collection.put("Verdun",verJSON.get(movieName));			
			
//			System.out.println(collection.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return JSONObject.valueToString(collection);
	}
	
	@Override
	public String bookMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) throws RemoteException {
		if(!VerdunData.ticketBookings.containsKey(movieName)) return toJSONString(false, 404, "Movie does not exist");

		// below movie may exist but movieID may not 
		if(!VerdunData.ticketBookings.get(movieName).containsKey(movieID))
			return toJSONString(false, 404, "Movie ID does not exist");
		// from here we consider all movie and their id exist
		// check where the user is from from customer id prefix
		String customerPrefix = customerID.substring(0,3);
		boolean isVerdunCustomer = false;

		if(customerPrefix.equals("ATW")) isVerdunCustomer = true;


		// check capacity for movie

		MovieDetails movieObject = VerdunData.ticketBookings.get(movieName).get(movieID);
		if(numberOfTickets > movieObject.getSeatsRemaining()) return toJSONString(false, 507, "Not enough room!");

		if(isVerdunCustomer) {
			// can book as many tickets as he wants to in Verdun theatre
			// but cannot book more than three tickets anywhere 
			// based on the number of tickets the customer id will be replicated
			String[] temp = new String[numberOfTickets];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = customerID;
			}
			VerdunData.ticketBookings.get(movieName).get(movieID).addCustomer(temp);
			return toJSONString(true,201,"Ticket Booked : "+numberOfTickets);
		}

		// now for people who are not from this server
		// check how many tickets customer has already booked.
		if(numberOfTickets>3) return toJSONString(false,401, "Only Verdun cutomer can book more than 3 tickets");

		String[] customers = movieObject.getCustomerList();
		int maxBooking =0;
		for(int i = 0; i<customers.length; i++) 
			if(customers[i].equals(customerID)) maxBooking++;

		if(maxBooking >= 3) return toJSONString(false, 401, "You have already reached maximum booking limit of 3");

		String[] temp = new String[numberOfTickets];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = customerID;
		}
		VerdunData.ticketBookings.get(movieName).get(movieID).addCustomer(temp);
		return toJSONString(true,201,"Ticket Booked : "+numberOfTickets);
	}
	
	@SuppressWarnings("exports")
	public static JSONObject constructCustomerMovieSchedule(String customerID) {
		HashMap<String, HashMap<String, Integer>> verSchedule = new HashMap<>();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : VerdunData.ticketBookings.entrySet()){
			String movieName = entry.getKey();
//			HashMap<String, MovieDetails> movieId = entry.getValue();
			verSchedule.put(movieName, new HashMap<String, Integer>());
			// check for every id if there exists the given customer
			for(Map.Entry<String, MovieDetails> val : entry.getValue().entrySet()) {
				String movieId = val.getKey();
				MovieDetails obj = val.getValue();
				if(obj.hasCustomer(customerID))
					verSchedule.get(movieName).put(movieId, obj.getNumberOfTickets(customerID));
					
			}
		}
		return new JSONObject(verSchedule);
	}
	
	@Override
	public String getBookingSchedule(String customerID) throws RemoteException {
		// first get the booking schedule withing the server;
		// atwater customer booking
		// {Avatar : [{movieID:numberOfTickets}]}
		// Hashmap<String, HashMap<String, Integer>> atwaterSchedules
		// JSONObject(atwaterSchedules)

		JSONObject verSchJSON = constructCustomerMovieSchedule(customerID);

		OutremontCustomerThread outCus = new OutremontCustomerThread(customerID);
		AtwaterCustomerThread atwCus = new AtwaterCustomerThread(customerID);
		JSONObject collection = new JSONObject();
		Thread t1 = new Thread(outCus);
		Thread t2 = new Thread(atwCus);

		try {

			t1.start();
			t2.start();
			t1.join();
			t2.join();

			JSONObject outSchJSON = new JSONObject(outCus.getCustomerSchedule());
			JSONObject atwSchJSON = new JSONObject(atwCus.getCustomerSchedule());

			collection.put("Atwater", atwSchJSON);
			collection.put("Outremont", outSchJSON);
			collection.put("Verdun", verSchJSON);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return JSONObject.valueToString(collection);
	}
	
	@Override
	public String cancelMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) throws RemoteException {
		if(VerdunData.ticketBookings.containsKey(movieName)) return toJSONString(false, 404, "Movie not found!");
		if(VerdunData.ticketBookings.get(movieName).containsKey(movieID)) return toJSONString(false, 404, "Movie ID not found!");
		
		// all movies and ids should exist below
		
		MovieDetails movieObject = VerdunData.ticketBookings.get(movieName).get(movieID);
		if(!movieObject.hasCustomer(customerID)) return toJSONString(false, 404, "Customer has not booked this movie");
		
		// now number of tickets to cancel 
		if(VerdunData.ticketBookings.get(movieName).get(movieID).remove(customerID, numberOfTickets))
			return toJSONString(true, 200, "Tickets cancelled : "+ numberOfTickets);
		
		return toJSONString(false, 304, "Not Modified for some reason...");
	}

}
