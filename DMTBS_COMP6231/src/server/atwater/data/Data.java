package server.atwater.data;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Data {
	public static HashMap<String, HashMap<String, MovieDetails>> ticketBookings = new HashMap<>();

	public static void seedData() {
		System.out.println("Seeding movie data ...");

		Data.ticketBookings.put("Avatar", new HashMap<String,MovieDetails>());
		// case 1 Remove movie without any problem
		Data.ticketBookings.get("Avatar").put("ATWA140223", new MovieDetails(10));
		// case 2 remove movie with customer
		String[] customers = {"ATWC1234","ATWC1234","ATWC1234","ATWC1235"};
		Data.ticketBookings.get("Avatar").put("ATWM150323", new MovieDetails(10));
		Data.ticketBookings.get("Avatar").get("ATWM150323").addCustomer(customers);
		// case 3 cannot remove movie
		Data.ticketBookings.get("Avatar").put("ATWE070323", new MovieDetails(10));
		Data.ticketBookings.get("Avatar").put("ATWE130323", new MovieDetails(10));

		// Avengers
		Data.ticketBookings.put("Avengers", new HashMap<String,MovieDetails>());
		Data.ticketBookings.get("Avengers").put("ATWA100223", new MovieDetails(10));
		Data.ticketBookings.get("Avengers").put("OUTM100223", new MovieDetails(10));
		Data.ticketBookings.get("Avengers").put("VERE100223", new MovieDetails(10));
		Data.ticketBookings.get("Avengers").put("ATWE130223", new MovieDetails(10));
		// Titanic
		Data.ticketBookings.put("Titanic", new HashMap<String,MovieDetails>());
		Data.ticketBookings.get("Titanic").put("ATWE130223", new MovieDetails(10));
	}
	
	public static JSONObject processHashmap() {
//		System.out.println(VerdunData.ticketBookings);
		JSONObject movieJSON = new JSONObject();
//		JSONArray obj = new JSONArray();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : ticketBookings.entrySet()) {
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
	
	public static String constructCustomerMovieSchedule(String customerID) {
		HashMap<String, HashMap<String, Integer>> atwSchedule = new HashMap<>();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : ticketBookings.entrySet()){
			String movieName = entry.getKey();
			// HashMap<String, MovieDetails> movieId = entry.getValue();
			atwSchedule.put(movieName, new HashMap<String, Integer>());
			// check for every id if there exists the given customer
			for(Map.Entry<String, MovieDetails> val : entry.getValue().entrySet()) {
				String movieId = val.getKey();
				MovieDetails obj = val.getValue();
				if(obj.hasCustomer(customerID)) {
					atwSchedule.get(movieName).put(movieId, obj.getNumberOfTickets(customerID));
					
				}
			}
		}
		for (Map.Entry<String, HashMap<String, Integer>> entry : new HashMap<>(atwSchedule).entrySet()) {
			String key = entry.getKey();
			HashMap<String, Integer> val = entry.getValue();
//			System.out.println("key : "+key);
//			System.out.println("Value : "+ val.toString());
//			System.out.println("value size : "+val.size());
			if (val.size() < 1) atwSchedule.remove(key, val);
			
		}
		JSONObject jsonObj = new JSONObject(atwSchedule);
		String json = JSONObject.valueToString(jsonObj) ;
		return json;
	}
}

