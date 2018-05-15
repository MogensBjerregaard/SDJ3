package tier2.businessserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;
import common.Subject;
import javafx.util.Pair;

public interface IBusinessServer extends Remote{
	void enqueueCar(Car car)
			throws RemoteException;
	Car dequeueCar()
			throws RemoteException;
	void registerCarPart(CarPart carPart)
			throws RemoteException;
	void packageProduct(Product product)
			throws RemoteException;
	void subscribe(ISubscriber subscriber, Subject... subjects)
			throws RemoteException;
	void updateView(String message)
			throws RemoteException;
	void generatePallets(String carPartType)
			throws RemoteException;
	int getCarpartTypeQuantity(Integer value, String carPartType)
			throws RemoteException;
	int getNextProductRegistrationNumber()
			throws RemoteException;
	CarPart pickCarPart(String carPartType)
			throws RemoteException;
}
