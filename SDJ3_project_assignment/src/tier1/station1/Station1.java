package tier1.station1;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Station1 {

	public static void main(String[] args) {
		try {
			new Station1Controller();
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 1 \n"+e.getMessage());
		}

	}

}
