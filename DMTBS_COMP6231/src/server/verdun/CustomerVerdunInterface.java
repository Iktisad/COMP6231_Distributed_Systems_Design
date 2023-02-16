package server.verdun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CustomerVerdunInterface extends Remote {
	public String bookMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets)throws RemoteException;
	public String getBookingSchedule(String customerID)throws RemoteException;
	public String cancelMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets)throws RemoteException;
}
