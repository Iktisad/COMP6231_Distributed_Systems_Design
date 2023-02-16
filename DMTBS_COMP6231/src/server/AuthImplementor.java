package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class AuthImplementor extends UnicastRemoteObject implements AuthServerInterface {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<String> list;
	
	
	public AuthImplementor() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
		list = new ArrayList<String>();
		list.add("ATWA1234");
		list.add("ATWC1234");
		list.add("VERA1235");
		list.add("VERC1235");
		list.add("OUTA1236");
		list.add("OUTC1236");
	}

	@Override
	public String[] login(String userid)  {
		// TODO Auto-generated method stub
		// check the id and look for the character A or C
		String[] user = new String[3];
		char c = userid.charAt(3);
		String area = userid.substring(0, 3);
		// find the user in admin or customer list;
		if(!list.contains(userid))
			return user;
		
		user[0] = userid;
		
		System.out.println("User id provided :" + userid);
		if(c == 'A'|| c == 'a') {
			user[1]= c+"";
			user[2] = area;
//			System.out.println("User is admin");
		}else if (c == 'C' || c == 'c') {
			user[1]= c+"";
			user[2] = area;
//			System.out.println("User is Customer");
			
		}
		
		return user;
	}

	@Override
	public void authenticate(String userid) {
		// TODO Auto-generated method stub
		
	}

}
