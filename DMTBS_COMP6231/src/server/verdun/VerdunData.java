package server.verdun;

import java.util.HashMap;



public class VerdunData {
	// Admin can create movie slots
	// therefore movieList should contain list of movies with their 
	// {movieName: {movieId: capacity}}
	public static HashMap<String, HashMap<String, MovieDetails>> ticketBookings = new HashMap<>();
	// users bookings or records will be kept in userBookings 
	// {customerID: {movieName:{movieId: booking number}}}
	public static HashMap<String,HashMap<String, HashMap<String, Integer>>> customerBookings = new HashMap<>();
	
	public VerdunData() {
		System.out.println("Seeding movie data ...");
		
		VerdunData.ticketBookings.put("Avatar", new HashMap<String,MovieDetails>());
		// case 1 Remove movie without any problem
		VerdunData.ticketBookings.get("Avatar").put("VERA140223", new MovieDetails(10));
		// case 2 remove movie with customer
		String[] customers = {"VERC1234","VERC1234","ATWC1234","VERC1235"};
		VerdunData.ticketBookings.get("Avatar").put("VERM150223", new MovieDetails(10));
		VerdunData.ticketBookings.get("Avatar").get("VERM150223").addCustomer(customers);
		// case 3 cannot remove movie
		VerdunData.ticketBookings.get("Avatar").put("ATWE100223", new MovieDetails(10));
		VerdunData.ticketBookings.get("Avatar").put("VERE130223", new MovieDetails(10));
		
		// Avengers
		VerdunData.ticketBookings.put("Avengers", new HashMap<String,MovieDetails>());
		VerdunData.ticketBookings.get("Avengers").put("VERA100223", new MovieDetails(10));
		VerdunData.ticketBookings.get("Avengers").put("OUTM100223", new MovieDetails(10));
		VerdunData.ticketBookings.get("Avengers").put("VERE100223", new MovieDetails(10));
		VerdunData.ticketBookings.get("Avengers").put("VERE130223", new MovieDetails(10));
		// Titanic
		VerdunData.ticketBookings.put("Titanic", new HashMap<String,MovieDetails>());
		VerdunData.ticketBookings.get("Titanic").put("ATWE130223", new MovieDetails(10));	}

}
