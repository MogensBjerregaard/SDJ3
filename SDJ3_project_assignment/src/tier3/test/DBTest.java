package tier3.test;

import common.Car;
import common.Product;
import tier3.DataServerView;
import tier3.IDataServer;
import tier3.postgres.DAOFactory;
import tier3.postgres.DataServerController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

public class DBTest {
	public static void main(String[] args) {
		try {
//			DataServerControllerOLD dataServer = new DataServerControllerOLD();
//			Car car01 = new Car("133101", 5001, "Lado");
//			CarPart carPart = new CarPart("3321331", car01, "Wheel", 540);
//			CarPart carPart02 = new CarPart("444", car01, "Seat", 500);
//			Pallet pallet = new Pallet("Wheel", 5000);
//			pallet.addParts(carPart);
//			Product product = new Product(9999, "Mixed Parts");
//			ArrayList<CarPart> parts = new ArrayList<>();
//			parts.add(carPart);
//			parts.add(carPart02);
//			product.setParts(parts);
//
//			dataServer.insertCar(car01);
//			dataServer.insertCarPart(carPart);
//			dataServer.insertCarPart(carPart02);
//			dataServer.insertProduct(product);
//			dataServer.insertPallet(pallet);;
			IDataServer dataServer = new DataServerController("jdbc:postgresql://localhost:5432/SDJ3_Project", "postgres", "Postgres", new DAOFactory(), new DataServerView());





//			System.out.println("next: " + dataServer.getNextPrimaryKey(Pallet.class));

			ArrayList<Product> products = dataServer.getMatchingProducts("123");
			for (Product product : products) {
				System.out.println(product);
			}

			Car car = dataServer.read(Car.class, "123");
			System.out.println(car.toString());

//			Car car01 = new Car("133101", 5001, "Lado");
////			Car carRead = dataServer.read(Car.class, "TEST123");
//			List<Car> carList = dataServer.readAll(Car.class);
//			for (Car car : carList) {
//				System.out.println(car.toString());
//				dataServer.delete(car);
//			}



		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
