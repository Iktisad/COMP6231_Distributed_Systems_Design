package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class AuthServer {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// TODO Auto-generated method stub
		AuthServerInterface auth = new AuthImplementor();
//		AdminInterface admin = new AdminImplementor();
//		CustomerInterface customer = new CustomerImplementor();

		LocateRegistry.createRegistry(5000) ;
		Naming.rebind("rmi://localhost:5000"+"/Auth", auth);
//		Naming.rebind("rmi://localhost:5000"+"/Admin", admin);
//		Naming.rebind("rmi://localhost:5000"+"/Customer", customer);
		
		
		System.out.println("Auth Server Started ...");
	}

}
