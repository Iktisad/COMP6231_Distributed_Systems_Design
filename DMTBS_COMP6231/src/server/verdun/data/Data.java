package server.verdun.data;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class Data {
	public static HashMap<String, HashMap<String, MovieDetails>> ticketBookings = new HashMap<>();

	public static void seedData() {
		System.out.println("Seeding movie data ...");

		ticketBookings.put("Avatar", new HashMap<String,MovieDetails>());
		// case 1 Remove movie without any problem
		ticketBookings.get("Avatar").put("VERA270223", new MovieDetails(10));
		// case 2 remove movie with customer
		String[] customers = {"VERC1234","VERC1234","VERC1235"};
		ticketBookings.get("Avatar").put("VERM150323", new MovieDetails(10));
		ticketBookings.get("Avatar").get("VERM150323").addCustomer(customers);
		// case 3 cannot remove movie
		ticketBookings.get("Avatar").put("VERE100323", new MovieDetails(10));
		ticketBookings.get("Avatar").put("VERE130323", new MovieDetails(10));

		// Avengers
		ticketBookings.put("Avengers", new HashMap<String,MovieDetails>());
		ticketBookings.get("Avengers").put("VERA100323", new MovieDetails(10));
		ticketBookings.get("Avengers").put("OUTM100223", new MovieDetails(10));
		ticketBookings.get("Avengers").put("VERE100223", new MovieDetails(10));
		ticketBookings.get("Avengers").put("VERE130223", new MovieDetails(10));
		
		// Titanic
		String [] customers_titanic = {"VERC1234",
				"VERC1234","VERC1234","VERC1234",
				"VERC1234", "OUTC1234","OUTC1234",
				"VERC1235","VERC1235","VERC1235"};
		
		ticketBookings.put("Titanic", new HashMap<String,MovieDetails>());
		ticketBookings.get("Titanic").put("VERE160323", new MovieDetails(10));
		ticketBookings.get("Titanic").get("VERE160323").addCustomer(customers_titanic);
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
		HashMap<String, HashMap<String, Integer>> verSchedule = new HashMap<>();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : ticketBookings.entrySet()){
			String movieName = entry.getKey();
			// HashMap<String, MovieDetails> movieId = entry.getValue();
			verSchedule.put(movieName, new HashMap<String, Integer>());
			// check for every id if there exists the given customer
			for(Map.Entry<String, MovieDetails> val : entry.getValue().entrySet()) {
				String movieId = val.getKey();
				MovieDetails obj = val.getValue();
				if(obj.hasCustomer(customerID)) {
					verSchedule.get(movieName).put(movieId, obj.getNumberOfTickets(customerID));

				}
			}
		}


		for (Map.Entry<String, HashMap<String, Integer>> entry : new HashMap<>(verSchedule).entrySet()) {
			String key = entry.getKey();
			HashMap<String, Integer> val = entry.getValue();
			if (val.size() < 1) verSchedule.remove(key, val);

		}
		JSONObject jsonObj = new JSONObject(verSchedule);
		String json = JSONObject.valueToString(jsonObj) ;
		return json;	
	}
}

