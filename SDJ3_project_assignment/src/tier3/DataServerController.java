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
import java.util.ArrayList;
import java.util.HashSet;

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

	@Override
	public ArrayList<Product> getMatchingProducts(String chassisNumber) throws RemoteException {
		//must query db and return a ArrayList of matching products for the chassisnumber
		
		//for testing - remove before release 
		if (chassisNumber.equals("1234")) {
			
			common.Product productDummy1= new common.Product(323, "Wheelset");
			common.Pallet pallet1 = new common.Pallet("Wheel", 500);
			pallet1.setRegistrationNumber(223);
			HashSet<Pallet> pallets1 = new HashSet<>();
			pallets1.add(pallet1);
			productDummy1.setPalletReferences(pallets1);
			common.Car car = new common.Car("1234", 1200, "BMW");
			common.CarPart carPart1 = new common.CarPart("1234WH1", car, "Wheel", 35);
			ArrayList<CarPart> parts1 = new ArrayList<>();
			parts1.add(carPart1);
			productDummy1.setParts(parts1);
			
			common.Product productDummy2= new common.Product(325, "Seatset");
			common.Pallet pallet2 = new common.Pallet("Seat", 500);
			pallet2.setRegistrationNumber(283);
			HashSet<Pallet> pallets2 = new HashSet<>();
			pallets2.add(pallet2);
			productDummy2.setPalletReferences(pallets2);
			common.CarPart carPart2 = new common.CarPart("1234SE1", car, "Seat", 28);
			common.CarPart carPart3 = new common.CarPart("1234SE2", car, "Seat", 28);
			ArrayList<CarPart> parts2 = new ArrayList<>();
			parts2.add(carPart2);
			parts2.add(carPart3);
			productDummy2.setParts(parts2);
			ArrayList<Product> matchingProducts = new ArrayList<>();
			matchingProducts.add(productDummy1);
			matchingProducts.add(productDummy2);
			
			return matchingProducts;			
		}
		///////////////////////////////////////////
		
		return new ArrayList<>();
	}
}
