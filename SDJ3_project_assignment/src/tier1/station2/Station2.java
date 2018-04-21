package tier1.station2;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Station2 {

	public static void main(String[] args) {
		
		try {
			new Station2Controller();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 2 \n"+e.getMessage());
		}

	}

}
