package server.outremont;

import java.util.HashMap;

public class OutremontData {
	// Admin can create movie slots
	// therefore movieList should contain list of movies with their 
	// {movieName: {movieId: capacity}}
	public static HashMap<String, HashMap<String, MovieDetails>> ticketBookings = new HashMap<>();
	// users bookings or records will be kept in userBookings 
	// {customerID: {movieName:{movieId: booking number}}}
	public static HashMap<String,HashMap<String, HashMap<String, Integer>>> customerBookings = new HashMap<>();
	
	public OutremontData() {
		System.out.println("Seeding movie data ...");
		
		OutremontData.ticketBookings.put("Avatar", new HashMap<String,MovieDetails>());
		// case 1 Remove movie without any problem
		OutremontData.ticketBookings.get("Avatar").put("OUTA140223", new MovieDetails(10));
		// case 2 remove movie with customer
		String[] customers = {"OUTC1234","OUTC1234","ATWC1234","VERC1235"};
		OutremontData.ticketBookings.get("Avatar").put("OUTM150223", new MovieDetails(10));
		OutremontData.ticketBookings.get("Avatar").get("OUTM150223").addCustomer(customers);
		// case 3 cannot remove movie
		OutremontData.ticketBookings.get("Avatar").put("ATWE100223", new MovieDetails(10));
		OutremontData.ticketBookings.get("Avatar").put("OUTE130223", new MovieDetails(10));
		
		// Avengers
		OutremontData.ticketBookings.put("Avengers", new HashMap<String,MovieDetails>());
		OutremontData.ticketBookings.get("Avengers").put("OUTA100223", new MovieDetails(10));
		OutremontData.ticketBookings.get("Avengers").put("OUTM100223", new MovieDetails(10));
		OutremontData.ticketBookings.get("Avengers").put("OUTE100223", new MovieDetails(10));
		OutremontData.ticketBookings.get("Avengers").put("VERE130223", new MovieDetails(10));
		// Titanic
		OutremontData.ticketBookings.put("Titanic", new HashMap<String,MovieDetails>());
		OutremontData.ticketBookings.get("Titanic").put("ATWE130223", new MovieDetails(10));	}

}
