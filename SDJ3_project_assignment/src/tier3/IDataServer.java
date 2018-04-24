package tier3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import common.Car;
import common.CarPart;
import common.Pallet;
import common.Product;

public interface IDataServer extends Remote{
	void insertCar(Car car) throws RemoteException;
	void insertCarPart(CarPart carPart) throws RemoteException;
	void insertPallet(Pallet pallet) throws RemoteException;
	void updatePallet(Pallet pallet) throws RemoteException;
	void insertProduct(Product product) throws RemoteException;
	void updateView(String string) throws RemoteException;
	void deleteCarFromQueue(Car car) throws RemoteException;
	int getNextPalletRegistrationNumber() throws RemoteException;
	void deleteFromCarPartQueue() throws RemoteException;
	ArrayList<Product> getMatchingProducts(String chassisNumber) throws RemoteException;
	int getNextProductRegistrationNumber() throws RemoteException;
	void initiateQueues() throws RemoteException;
	HashMap<CarPart, Pallet> getNextCarPartFromPallet(String carPartType) throws RemoteException;
}
