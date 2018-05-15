package tier1.dismantlingstationclient;

import tier2.businessserver.BusinessServerController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

	public static void main(String[] args) {
		
		try {
			new DismantlingStationClientController(BusinessServerController.getRemoteObject());
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 2 \n"+e.getMessage());
		}

	}

}
