package tier2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import tier3.IDataServer;

public class BusinessServer {

	public static void main(String[] args) throws MalformedURLException, NotBoundException {
		
			try {
				final String registryName = "DataServer"; //name of data server
				IDataServer dataServer = (IDataServer) Naming.lookup("rmi://localhost/"+registryName);
				new BusinessServerController(dataServer);
			} catch (RemoteException e) {
				System.out.println("Unable to start Business Server -\n"+e.getMessage());
			}
	}
}
