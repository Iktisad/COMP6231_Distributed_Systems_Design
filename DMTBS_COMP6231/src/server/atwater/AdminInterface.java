package server.atwater;

import java.rmi.RemoteException;

public interface AdminInterface extends CustomerInterface {
	public String addMovieSlots(String movieID, String movieName, int bookingCapacity)throws RemoteException;
	public String removeMovieSlots(String movieID, String movieName) throws RemoteException;
	public String listMovieShowsAvailability(String movieName)throws RemoteException;
}
