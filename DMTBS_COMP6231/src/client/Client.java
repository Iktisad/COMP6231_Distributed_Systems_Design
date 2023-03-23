package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
//import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

//import org.json.JSONArray;
import org.json.JSONObject;

import server.AuthServerInterface;
//import server.atwater.AdminInterface;
//import server.atwater.CustomerInterface;
//import server.outremont.AdminInterface;
import server.verdun.AdminInterface;
import server.verdun.CustomerInterface;


public class Client {

	static void customerOperation(Scanner sc) {
		System.out.println("Which Theatre would you like to watch? \n Press 1 for Atwater \n Press 2 for Verdun "
				+ "\n Press 3 for Outremon");
		
		byte theatreSelect = sc.nextByte();
		
		switch (theatreSelect) {
		case 1: {
			System.out.println("Welcome to Atwater Theatre! We are showing....");
			break;
		}
		case 2: {
			System.out.println("Welcome to Verdun Theatre! We are showing....");
			break;
		}
		case 3: {
			System.out.println("Welcome to Outremont Theatre! We are showing....");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + theatreSelect);
		}
	}
	
	static List<Object> constructMovieParameters(Scanner sc, String area) {
		String movieName = null;
		String date;
		int bookingCapacity = 0;
		String movieID = null;
		
		System.out.println("Enter movie name");
		movieName = new String(sc.nextLine());
//			sc.nextLine();
		
		System.out.println("Enter movie date in the format DDMMYY");
		date = new String(sc.nextLine());
//			sc.nextLine();
		
		System.out.println("Enter timeslot, Press 'M' for morning,'A' for Afternoon, E for Evening.");
		String timeslot = new String(sc.nextLine());
//			sc.nextLine();
		
		System.out.println("Enter Theatre Booking Capacity");
		bookingCapacity =Short.parseShort(sc.nextLine());
//			sc.nextLine();
		movieID = area + timeslot + date;
		
		
		return Arrays.asList(movieID, movieName, bookingCapacity);
	}
	
	static List<Object> constructBookOrCancelMovieParameters(Scanner sc){
		
		
		System.out.println("Please enter Customer ID");
		String customerID = new String(sc.nextLine());
		
		
		System.out.println("Please enter Movie Name");
		String movieName = new String(sc.nextLine());
		
		
		System.out.println("Please enter  Movie ID");
		String movieID = new String(sc.nextLine());
		
		System.out.println("Please enter the the number of tickets you would like");
		int numberOfTickets = Integer.parseInt(sc.nextLine());
		
		
		return Arrays.asList(customerID, movieID, movieName, numberOfTickets);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Ask for login Credentials only id
		System.out.println("Please enter your login credentials.");
		System.out.print("login id : ");
		try (Scanner sc = new Scanner(System.in)) {
			String input = sc.nextLine();
			AuthServerInterface user = (AuthServerInterface)Naming.lookup("rmi://localhost:5000"+"/Auth");
			String [] usr = user.login(input);
			if(usr[0].length()==0) {
				throw new Exception("User id is not valid!");
			};
			
			// based on who the user is we will have the following rmis available to use
			
			System.out.println("Welcome "+ usr[0] + " Status : "+ usr[1]);
			
			if(usr[1].equals("C")){
				// user rmis are defined
//				CustomerInterface customer = (CustomerInterface)Naming.lookup("rmi://localhost:5000"+"/Customer");
//				customerOperation(sc, customer);
				System.out.println("Which Theatre would you like visit? \n Press 1 for Atwater \n Press 2 for Verdun "
						+ "\n Press 3 for Outremon");
				
				byte area = Byte.parseByte(sc.nextLine());
				
				switch (area) {
				case 1:
					server.atwater.CustomerInterface atwc = (server.atwater.CustomerInterface)Naming.lookup("rmi://localhost:5001" + "/Atwater/customer");
					String server = "Atwater";
					byte atwcInput;
					while(true) {
						
						System.out.println("\nPress 1 to Add a booking"
								+ "\nPress 2 to cancel a ticket for a customer "
								+ "\nPress 3 to see a your schedule."
								+ "\nPress 4 to exit Atwater.\n");
						atwcInput = Byte.parseByte(sc.nextLine());
						System.out.println("");
						
						 if(atwcInput == (byte)1) {
							
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = atwc.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(atwcInput == (byte)2) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							
							String result = atwc.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(atwcInput == (byte)3) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());
							
							System.out.println("Fetchin Customer Schedule....");
//							Thread.sleep(1000);
							String result = atwc.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(atwcInput == (byte)4) {
							System.out.println("Exiting "+server+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}
						
					}
					
					break;
				case 2:
					String serverOut = "Outremont";
					server.outremont.CustomerInterface outc = (server.outremont.CustomerInterface)Naming.lookup("rmi://localhost:5002" + "/Outremont/customer");
					byte outcInput;
					while(true) {

						System.out.println("\nPress 1 to Add a booking"
								+ "\nPress 2 to cancel a ticket for a customer "
								+ "\nPress 3 to see a your schedule."
								+ "\nPress 4 to exit Atwater.\n");
						outcInput = Byte.parseByte(sc.nextLine());
						System.out.println("");

						if(outcInput == (byte)1) {

							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = outc.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(outcInput == (byte)2) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);

							String result = outc.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));

							break;
						}
						else if(outcInput == (byte)3) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());

							System.out.println("Fetchin Customer Schedule....");
							Thread.sleep(1000);
							String result = outc.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));

							break;
						}
						else if(outcInput == (byte)4) {
							System.out.println("Exiting "+serverOut+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}

					}

					break;
				case 3:
					String serverVer = "Verdun";
					CustomerInterface verc = (CustomerInterface)Naming.lookup("rmi://localhost:5003" + "/Verdun/customer");
					byte vercInput;
					while(true) {

						System.out.println("\nPress 1 to Add a booking"
								+ "\nPress 2 to cancel a ticket for a customer "
								+ "\nPress 3 to see a your schedule."
								+ "\nPress 4 to exit Atwater.\n");
						vercInput = Byte.parseByte(sc.nextLine());
						System.out.println("");

						if(vercInput == (byte)1) {

							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = verc.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(vercInput == (byte)2) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);

							String result = verc.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));

							break;
						}
						else if(vercInput == (byte)3) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());

							System.out.println("Fetchin Customer Schedule....");
							Thread.sleep(1000);
							String result = verc.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));

							break;
						}
						else if(vercInput == (byte)4) {
							System.out.println("Exiting "+serverVer+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}

					}
					break;
				default:
					break;
				}
				
			}
			else if(usr[1].equals("A")) {
				// find which area they are from
				System.out.println("Which Theatre area would you like to make changes? \n Press 1 for Atwater \n Press 2 for Verdun "
						+ "\n Press 3 for Outremon");
				
				byte area = Byte.parseByte(sc.nextLine());
				
				switch (area) {
				case 1:
					server.atwater.AdminInterface admin = (server.atwater.AdminInterface)Naming.lookup("rmi://localhost:5001" + "/Atwater/admin");
					String server = "Atwater";
					byte adminInput;
					while(true) {
						
						System.out.println("Press 1 to Add movie "
								+ "\nPress 2 to Remove Movie "
								+ "\nPress 3 to List Movie Availability"
								+ "\nPress 4 to Add a booking for a customer "
								+ "\nPress 5 to cancel a ticket for a customer "
								+ "\nPress 6 to see a customer schedule "
								+ "\nPress 7 to exit Atwater.\n");
						adminInput = Byte.parseByte(sc.nextLine());
						System.out.println("");
						
						if(adminInput == (byte)1) {
							
							List<Object> params = constructMovieParameters(sc, "ATW");
							System.out.println(params.toString());
							String result = admin.addMovieSlots((String)params.get(0), (String)params.get(1), (int)params.get(2));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
						}
						else if(adminInput == (byte)2) {
							System.out.println("Please enter the movie name you would like to remove.");
							String movieName = new String(sc.nextLine());
							
							System.out.println("Please enter the movie id you would like to remove.");
							String movieID = new String(sc.nextLine());
							
							//call remove function
							String result = admin.removeMovieSlots(movieID, movieName);
							System.out.println(result);
						}
						else if(adminInput == (byte)3) {
							System.out.println("Please Enter the movie name to see schedule");
							
							String movieName = new String(sc.nextLine());
							
							String list = admin.listMovieShowsAvailability(movieName);
							JSONObject listObject = new JSONObject(list);
//							System.out.println(list);
							System.out.println(listObject.toString(4));
							
						}
						else if(adminInput == (byte)4) {
							
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = admin.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(adminInput == (byte)5) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							
							String result = admin.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminInput == (byte)6) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());
							
							System.out.println("Fetchin Customer Schedule....");
							Thread.sleep(1000);
							String result = admin.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminInput == (byte)7) {
							System.out.println("Exiting "+server+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}
						
					}
					
					break;
				case 2:
					server.outremont.AdminInterface adminOut = (server.outremont.AdminInterface)Naming.lookup("rmi://localhost:5002" + "/Outremont/admin");
					String serverOut = "Outremont";
					byte adminOutInput;
					while(true) {
						
						System.out.println("Press 1 to Add movie "
								+ "\nPress 2 to Remove Movie "
								+ "\nPress 3 to List Movie Availability"
								+ "\nPress 4 to Add a booking for a customer "
								+ "\nPress 5 to cancel a ticket for a customer "
								+ "\nPress 6 to see a customer schedule "
								+ "\nPress 7 to exit Atwater.\n");
						adminOutInput = Byte.parseByte(sc.nextLine());
						System.out.println("");
						
						if(adminOutInput == (byte)1) {
							
							List<Object> params = constructMovieParameters(sc, "ATW");
							System.out.println(params.toString());
							String result = adminOut.addMovieSlots((String)params.get(0), (String)params.get(1), (int)params.get(2));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
						}
						else if(adminOutInput == (byte)2) {
							System.out.println("Please enter the movie name you would like to remove.");
							String movieName = new String(sc.nextLine());
							
							System.out.println("Please enter the movie id you would like to remove.");
							String movieID = new String(sc.nextLine());
							
							//call remove function
							String result = adminOut.removeMovieSlots(movieID, movieName);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
						}
						else if(adminOutInput == (byte)3) {
							System.out.println("Please Enter the movie name to see schedule");
							
							String movieName = new String(sc.nextLine());
							
							String list = adminOut.listMovieShowsAvailability(movieName);
							JSONObject listObject = new JSONObject(list);
							
							System.out.println(listObject.toString(4));
							
						}
						else if(adminOutInput == (byte)4) {
							
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = adminOut.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(adminOutInput == (byte)5) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							
							String result = adminOut.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminOutInput == (byte)6) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());
							
							System.out.println("Fetchin Customer Schedule....");
							Thread.sleep(1000);
							String result = adminOut.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminOutInput == (byte)7) {
							System.out.println("Exiting "+serverOut+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}
						
					}
					break;
				case 3:
					String serverVer = "Verdun";
					AdminInterface adminVer = (AdminInterface)Naming.lookup("rmi://localhost:5003" + "/Verdun/admin");
					byte adminVerInput;
					while(true) {
						
						System.out.println("Press 1 to Add movie "
								+ "\nPress 2 to Remove Movie "
								+ "\nPress 3 to List Movie Availability"
								+ "\nPress 4 to Add a booking for a customer "
								+ "\nPress 5 to cancel a ticket for a customer "
								+ "\nPress 6 to see a customer schedule "
								+ "\nPress 7 to exit Atwater.\n");
						adminVerInput = Byte.parseByte(sc.nextLine());
						System.out.println("");
						
						if(adminVerInput == (byte)1) {
							
							List<Object> params = constructMovieParameters(sc, "ATW");
							System.out.println(params.toString());
							String result = adminVer.addMovieSlots((String)params.get(0), (String)params.get(1), (int)params.get(2));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
						}
						else if(adminVerInput == (byte)2) {
							System.out.println("Please enter the movie name you would like to remove.");
							String movieName = new String(sc.nextLine());
							
							System.out.println("Please enter the movie id you would like to remove.");
							String movieID = new String(sc.nextLine());
							
							//call remove function
							String result = adminVer.removeMovieSlots(movieID, movieName);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
						}
						else if(adminVerInput == (byte)3) {
							System.out.println("Please Enter the movie name to see schedule");
							
							String movieName = new String(sc.nextLine());
							
							String list = adminVer.listMovieShowsAvailability(movieName);
							JSONObject listObject = new JSONObject(list);
							
							System.out.println(listObject.toString(4));
							
						}
						else if(adminVerInput == (byte)4) {
							
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							String result = adminVer.bookMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							break;
						}
						else if(adminVerInput == (byte)5) {
							System.out.println("Please enter the following");
							List<Object> params = constructBookOrCancelMovieParameters(sc);
							
							String result = adminVer.cancelMovieTickets((String)params.get(0), (String)params.get(1), (String)params.get(2), (int)params.get(3));
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminVerInput == (byte)6) {
							System.out.println("Please enter Customer ID");
							String customerID = new String(sc.nextLine());
							
							System.out.println("Fetchin Customer Schedule....");
							Thread.sleep(1000);
							String result = adminVer.getBookingSchedule(customerID);
							JSONObject json = new JSONObject(result);
							System.out.println(json.toString(4));
							
							break;
						}
						else if(adminVerInput == (byte)7) {
							System.out.println("Exiting "+serverVer+" Server");
							break;
						}
						else {
							System.out.println("Invalid number, TRY AGAIN!!");
							continue;
						}		
					}
					break;
				default:
					break;
				}				
				
			}			
			sc.close();
		}catch(MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {
			
		} 
		
	}
	
	

}
