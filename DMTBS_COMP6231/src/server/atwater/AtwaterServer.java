package server.atwater;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import server.atwater.threads.UDPServer;


public class AtwaterServer {
	

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		
		// need to figure out how to send the customer or admin to adwater Implementor
		CustomerInterface customer = new AtwaterImplementor();
		AdminInterface admin = new AtwaterImplementor();
		System.out.println("Atwater server running....");
		
		LocateRegistry.createRegistry(5001);
		Naming.rebind("rmi://localhost:5001"+"/Atwater/customer", customer);
		Naming.rebind("rmi://localhost:5001"+"/Atwater/admin", admin);
		Runnable udps = new UDPServer(admin, customer);
		Thread udp = new Thread(udps);
		udp.start();
		try {
			udp.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
