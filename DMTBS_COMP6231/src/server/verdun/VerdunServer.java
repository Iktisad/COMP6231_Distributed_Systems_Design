package server.verdun;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import server.verdun.threads.UDPServer;


public class VerdunServer {


	public static void main(String[] args) throws RemoteException, MalformedURLException {
		// need to figure out how to send the customer or admin to adwater Implementor
		CustomerInterface customer = new VerdunImplementor();
		AdminInterface admin = new VerdunImplementor();
		//seed data
		System.out.println("Verdun server running....");

		LocateRegistry.createRegistry(5003);
		Naming.rebind("rmi://localhost:5003"+"/Verdun/customer", customer);
		Naming.rebind("rmi://localhost:5003"+"/Verdun/admin", admin);

		// wait for invocations from clients
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
