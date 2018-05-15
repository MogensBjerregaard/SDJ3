package tier1.carregistrationclient;

import tier2.businessserver.BusinessServerController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Station1 {

	public static void main(String[] args) {
		try {
			new CarRegistrationClientController(BusinessServerController.getRemoteObject());
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 1 \n"+e.getMessage());
		}

	}

}
