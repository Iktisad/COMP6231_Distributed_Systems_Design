package server.atwater;

import java.util.HashMap;

public class AtwaterData {
	// Admin can create movie slots
	// therefore movieList should contain list of movies with their 
	// {movieName: {movieId: MovieDetails Object}}
	public static HashMap<String, HashMap<String, MovieDetails>> ticketBookings = new HashMap<>();
	
	// users bookings or records will be kept in userBookings 
	// {customerID: {movieName:{movieId: booking number}}}
	public static HashMap<String,HashMap<String, HashMap<String, Integer>>> customerBookings = new HashMap<>();
	
	// single area theatre 
	
//	HashMap<movieName, HashMap<movieID, movieDetails>>
	//seed values
	public AtwaterData() {
		System.out.println("Seeding movie data ...");
		
		AtwaterData.ticketBookings.put("Avatar", new HashMap<String,MovieDetails>());
		// case 1 Remove movie without any problem
		AtwaterData.ticketBookings.get("Avatar").put("ATWA140223", new MovieDetails(10));
		// case 2 remove movie with customer
		String[] customers = {"ATWC1234","ATWC1234","ATWC1234","ATWC1235"};
		AtwaterData.ticketBookings.get("Avatar").put("ATWM150223", new MovieDetails(10));
		AtwaterData.ticketBookings.get("Avatar").get("ATWM150223").addCustomer(customers);
		// case 3 cannot remove movie
		AtwaterData.ticketBookings.get("Avatar").put("ATWE100223", new MovieDetails(10));
		AtwaterData.ticketBookings.get("Avatar").put("ATWE130223", new MovieDetails(10));
		
		// Avengers
		AtwaterData.ticketBookings.put("Avengers", new HashMap<String,MovieDetails>());
		AtwaterData.ticketBookings.get("Avengers").put("ATWA100223", new MovieDetails(10));
		AtwaterData.ticketBookings.get("Avengers").put("OUTM100223", new MovieDetails(10));
		AtwaterData.ticketBookings.get("Avengers").put("VERE100223", new MovieDetails(10));
		AtwaterData.ticketBookings.get("Avengers").put("ATWE130223", new MovieDetails(10));
		// Titanic
		AtwaterData.ticketBookings.put("Titanic", new HashMap<String,MovieDetails>());
		AtwaterData.ticketBookings.get("Titanic").put("ATWE130223", new MovieDetails(10));
	}
}


