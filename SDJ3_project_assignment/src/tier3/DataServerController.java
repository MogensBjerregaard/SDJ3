package tier3;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import common.Car;
import common.CarPart;
import common.Pallet;
import common.Product;

public class DataServerController extends UnicastRemoteObject implements IDataServer {
	private static final long serialVersionUID = 1L;
	private DataServerView view;
	private static final String registryName = "DataServer";
	public DataServerController() throws RemoteException{
		this.view = new DataServerView();
		bindToRegistry();
		updateView("Registry naming rebind using '"+registryName+"' successful");
		updateView(registryName+" is running");
	}
	
	public Connection getConnection() throws SQLException {
//		DriverManager.registerDriver(new org.postgresql.Driver());
		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/SDJ3_Project", "postgres", "Postgres");
	}
	
	public void updateView(String message) {
		LocalDateTime timePoint = LocalDateTime.now();
		this.view.updateTextArea(timePoint+": "+message);
	}
	private void bindToRegistry(){
		try {
			LocateRegistry.createRegistry(1099);
			Naming.rebind(registryName, this);
		} catch (RemoteException | MalformedURLException e) {
			System.out.println("Error initiating RMI -\n"+e.getMessage());
		}	
	}
	@Override
	public void insertCar(Car car) {
		try {
			PreparedStatement statement = getConnection().prepareStatement("insert into car (chassis_number, weight, model) values (?,?,?)");
			statement.setString(1, car.getChassisNumber());
			statement.setDouble(2, car.getWeight());
			statement.setString(3, car.getModel());
			statement.execute();
		} catch (SQLException e) {
			System.out.println("DB insert error \n"+e.getMessage());
		}
		
	}
	@Override
	public void insertCarPart(CarPart carPart) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void insertPallet(Pallet pallet) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updatePallet(Pallet pallet) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void insertProduct(Product product) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteCarFromQueue(Car car) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getNextPalletRegistrationNumber() throws RemoteException {
		// should return next pallet registration number from db
		return 0;
	}
	@Override
	public void deleteFromCarPartQueue() throws RemoteException {
		// delete carpart row from car part queue table
		
	}
}
