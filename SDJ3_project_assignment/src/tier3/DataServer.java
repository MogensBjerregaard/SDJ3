package tier3;

import java.rmi.RemoteException;

public class DataServer{

	public static void main(String[] args) {
		try {
			new DataServerController();
		} catch (RemoteException e) {
			System.out.println("Unable to start Data Server -\n"+e.getMessage());
		}
	}

}
