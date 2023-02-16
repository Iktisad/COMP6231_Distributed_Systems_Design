package server.verdun;

import java.rmi.RemoteException;

public interface AdminVerdunInterface extends CustomerVerdunInterface{
	public String addMovieSlots(String movieID, String movieName, int bookingCapacity)throws RemoteException;
	public String removeMovieSlots(String movieID, String movieName) throws RemoteException;
	public String listMovieShowsAvailability(String movieName)throws RemoteException;
}
