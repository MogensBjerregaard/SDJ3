package tier1.station3;

import tier2.businessserver.BusinessServerController;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
public class Station3 {

	public static void main(String[] args) {
		try {
			new Station3Controller(BusinessServerController.getRemoteObject());
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("Error starting Station 3 \n"+e.getMessage());
		}

	}

}
