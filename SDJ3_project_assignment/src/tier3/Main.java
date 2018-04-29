package tier3;

import tier3.postgres.DAOFactory;
import tier3.postgres.DataServerController;
import tier3.postgres.IDataAccessObjectFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {

	public static void main(String[] args) {
		try {
			String jdbcUrl = "jdbc:postgresql://localhost:5432/SDJ3_Project";
			String username = "postgres";
			String password = "Postgres";
			IDataAccessObjectFactory daoFactory = new DAOFactory();
			IDataServerView view = new DataServerView();
			String rmiRegistryName = "DataServer";

			IDataServer dataServer = new DataServerController(jdbcUrl, username, password, daoFactory, view);

			try {
				LocateRegistry.createRegistry(1099);
				Naming.rebind(rmiRegistryName, dataServer);
			} catch (RemoteException | MalformedURLException e) {
				System.out.println("Error initiating RMI -\n"+e.getMessage());
			}

		} catch (RemoteException e) {
			System.out.println("Unable to start Data Server -\n"+e.getMessage());
		}
	}

}
