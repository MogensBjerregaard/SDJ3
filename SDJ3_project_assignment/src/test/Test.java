package test;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import common.Car;
import tier1.station1.Station1Controller;
import tier1.station2.Station2Controller;
import tier2.BusinessServerController;
import tier3.DataServerController;

public class Test {

	public static void main(String[] args) {
		try {
			DataServerController dataServerController = new DataServerController();
//			Class.forName("org.postgresql.Driver");
			Connection connection = dataServerController.getConnection();
			dataServerController.insertCar(new Car("fisse", 1231, "øl-mobil"));
		} catch (RemoteException | SQLException e) {
			System.out.println("Unable to start Data Server -\n"+e.getMessage());
		}
		try {
			new BusinessServerController();
		} catch (RemoteException e) {
			System.out.println("Unable to start Business Server -\n"+e.getMessage());
		}
		try {
			new Station1Controller();
		} catch (RemoteException e) {
			System.out.println("Error starting Station 1 \n"+e.getMessage());
		}
		try {
			new Station2Controller();
		} catch (RemoteException e) {
			System.out.println("Error starting Station 2 \n"+e.getMessage());
		}


	}

}
