package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthServerInterface extends Remote {
	public String[] login(String userid)throws RemoteException;
	public void authenticate(String userid)throws RemoteException;
}
