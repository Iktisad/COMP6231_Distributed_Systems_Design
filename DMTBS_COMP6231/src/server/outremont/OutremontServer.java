package server.outremont;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import server.outremont.threads.UDPServer;


public class OutremontServer {

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// need to figure out how to send the customer or admin to adwater Implementor
		CustomerInterface customer = new OutremontImplementor();
		AdminInterface admin = new OutremontImplementor();
		//seed data
		System.out.println("Outremont server running....");

		LocateRegistry.createRegistry(5002);
		Naming.rebind("rmi://localhost:5002"+"/Outremont/customer", customer);
		Naming.rebind("rmi://localhost:5002"+"/Outremont/admin", admin);
		
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
