package tier1.station1;

import tier2.businessserver.BusinessServer;
import tier2.businessserver.BusinessServerController;
import tier2.businessserver.IBusinessServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Station1 {

	public static void main(String[] args) {
		try {
			new Station1Controller(BusinessServerController.getRemoteObject());
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 1 \n"+e.getMessage());
		}

	}

}
