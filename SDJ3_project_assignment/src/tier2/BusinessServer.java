package tier2;

import java.rmi.RemoteException;

public class BusinessServer {

	public static void main(String[] args) {
		
			try {
				new BusinessServerController();
			} catch (RemoteException e) {
				System.out.println("Unable to start Business Server -\n"+e.getMessage());
			}
	}
}
