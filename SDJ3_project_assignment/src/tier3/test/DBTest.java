package tier3.test;

import common.Car;
import common.CarPart;
import common.Pallet;
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
import java.util.List;

public class DBTest {
	public static void main(String[] args) {
		try {
//			DataServerControllerOLD dataServer = new DataServerControllerOLD();
//			Car car01 = new Car("133101", 5001, "Lado");
//			CarPart carPart = new CarPart("3321331", car01, "Wheel", 540);
//			CarPart carPart02 = new CarPart("444", car01, "Seat", 500);
//			Pallet pallet = new Pallet("Wheel", 5000);
//			pallet.addPart(carPart);
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

//			Car car = new Car("999", 999, "test");
//			dataServer.create(car);
//
//			CarPart carPart01 = new CarPart("999x", car, "test", 999);
//			CarPart carPart02 = new CarPart("999y", car, "test", 999);
//			CarPart carPart03 = new CarPart("999z", car, "test", 999);
//			dataServer.create(carPart01);
//			dataServer.create(carPart02);
//			dataServer.create(carPart03);
//
//			Pallet pallet = new Pallet("test", 3000);
//			pallet.addPart(carPart01);
//			pallet.addPart(carPart02);
//			pallet.addPart(carPart03);
//			dataServer.create(pallet);
//
			Product product = new Product(9999, "testType");
			dataServer.delete(product);
//			product.addPart(carPart01);
//			product.addPart(carPart02);
//			product.addPart(carPart03);
//			dataServer.create(product);

//			List<Product> productRead = dataServer.readAll(Product.class);
//			for (Product product : productRead) {
//				System.out.println(product);
//				for (CarPart part : product.getParts()) {
//					System.out.println(part);
//				}
//				for (Pallet palletRead : product.getPalletReferences()) {
//					System.out.println(palletRead);
//				}
//				System.out.println();
//			}

//			List<Pallet> palletRead = dataServer.readAll(Pallet.class);
//			for (Pallet pallet : palletRead) {
//				System.out.println(pallet);
//				for (CarPart carPart : pallet.getParts()) {
//					System.out.println(carPart);
//				}
//				System.out.println();
//			}

//			List<CarPart> carParts = dataServer.readAll(CarPart.class);
//			for (CarPart carPart : carParts) {
//				System.out.println(carPart.toString());
//				System.out.println(carPart.getCar().toString());
//			}

//			List<Pallet> pallets = dataServer.readAll(Pallet.class);
//			for (Pallet pallet : pallets) {
//				System.out.println(pallet.toString());
//				for (CarPart carPart : pallet.getParts()) {
//					System.out.println(carPart);
//					System.out.println(carPart.getCar());
//				}
//				System.out.println();
//			}

//			Pallet pallet = dataServer.read(Pallet.class, "8");
//			System.out.println(pallet);
//			for (CarPart carPart : pallet.getParts()) {
//				System.out.println(carPart);
////				System.out.println(carPart.getCar());
//			}
//			System.out.println();
//
////			pallet = new Pallet(pallet.getRegistrationNumber(), "Wheel", 9999);
//			pallet.getNextCarPart();
//			dataServer.update(pallet);
//			System.out.println();
//
//			pallet = dataServer.read(Pallet.class, "8");
//			System.out.println(pallet);
//			for (CarPart carPart : pallet.getParts()) {
//				System.out.println(carPart);
////				System.out.println(carPart.getCar());
//			}
//			System.out.println();

//			Pallet pallet = dataServer.read(Pallet.class, "8");
//			System.out.println(pallet);
//			for (CarPart carPart : pallet.getParts()) {
//				System.out.println(carPart);
//				System.out.println(carPart.getCar());
//			}



//			System.out.println("next: " + dataServer.getNextPrimaryKey(Pallet.class));

//			ArrayList<Product> products = dataServer.getMatchingProducts("123");
//			for (Product product : products) {
//				System.out.println(product);
//			}
//
//			Car car = dataServer.read(Car.class, "123");
//			System.out.println(car.toString());

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
