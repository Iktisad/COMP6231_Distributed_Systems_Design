package server.verdun;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieDetails {
	private int capacity;
	private ArrayList<String> customerList;
	
	public MovieDetails(int capacity) {
		this.capacity = capacity;
		this.setCustomerList(new ArrayList<String>());
	}


	public String [] getCustomerList() {
		String []arr = new String[this.customerList.size()];
		return this.customerList.toArray(arr);
	}
	public int getCapcity() {
		return this.capacity;
	}
	
	public int getCustomerCount() {
		return this.customerList.size();
	};
	public int getNumberOfTickets(String customerID) {
		int counter = 0;
		for (int i = 0; i < this.customerList.size(); i++) {
			if(this.customerList.get(i).equals(customerID)) {
				counter++;
			};
		}
		return counter;
	};
	public boolean hasCustomer(String customerID) {
		return this.customerList.contains(customerID);
	}
	
	public boolean remove(String customerID, int numberOfTickets) {
		
		if(this.customerList.contains(customerID)) {
			
			for (int i = 0; i < numberOfTickets; i++) 
				this.customerList.remove(customerID);
			return true;
		}
		
		return false; 
	}
	public void setBookingCapacity(int a) {
		this.capacity = a;
	};
	private void setCustomerList(ArrayList<String> arrayList) {
		// TODO Auto-generated method stub
		this.customerList = arrayList;
		
	}
	public boolean addCustomer(String customerID) {
		if(theatreHasSpace()) {
		
			this.customerList.add(customerID);
			System.out.println("cutomer added");
			return true;
		}
		System.out.println("Theatre does not have space!");
		return false;
	}
	public boolean addCustomer(String[] pendingCustomers) {
		for(int i=0; i<pendingCustomers.length; i++) {
			this.customerList.add(pendingCustomers[i]);
		}
		return true;
	}
	public boolean removeCustomer(String customerID) {
		return this.customerList.remove(customerID);
	}
	private boolean theatreHasSpace() {
		
		return this.customerList.size() <= capacity;
	}
	public int getSeatsRemaining() {
		return capacity - this.customerList.size();
	}
	public HashMap<String, Object> getMovieMapObject(){
		// movie capacity : integer
		HashMap<String, Object> map = new HashMap<>();
		map.put("capacity", capacity);
		map.put("booked", this.customerList.size());
		map.put("seats_remaining", capacity - this.customerList.size());
		map.put("customers", getCustomerList());
		
		return map ;
	}
	
	
}
