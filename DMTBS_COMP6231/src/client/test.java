package client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import server.atwater.AtwaterData;
import server.atwater.MovieDetails;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// initial date 10-12-23
//		new AtwaterData();
		
//		JSONObject var = processHashmap();
//		System.out.println(var.toString(4));
//		ArrayList<Integer> arr = new ArrayList<>();
//		arr.add(10);
//		arr.add(10);
//		arr.add(10);
//		arr.add(20);
//		arr.add(30);
//		arr.add(40);
//		
//		System.out.println(arr.toString());
//		
//		arr.remove((Integer)10);
//		
//		System.out.println(arr.toString());
		byte [] a = new byte [5];
		
		System.out.println(Arrays.toString(a));
	}

	
	static JSONObject processHashmap() {
//		System.out.println(VerdunData.ticketBookings);
		JSONObject movieJSON = new JSONObject();
		for (Map.Entry<String, HashMap<String, MovieDetails>> entry : AtwaterData.ticketBookings.entrySet()) {
			JSONArray obj = new JSONArray();
			String key = entry.getKey();
			HashMap<String, MovieDetails> value = entry.getValue();
			JSONObject movieIdJSON = new JSONObject();
			
			for(Map.Entry<String, MovieDetails> movieId : value.entrySet()) {
				// another hashmap
				movieIdJSON.put(movieId.getKey(), new JSONObject(movieId.getValue().getMovieMapObject()));
				
			}
//			movieJSON.put(key,movieIdJSON);
			movieJSON.put(key,movieIdJSON);
		}
//		System.out.println(movieJSON);
			
		return movieJSON;
	}
	
	
	static Date movieIdDateConverter(String movieID) {
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

}
