package tier2.businessserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;

public interface IBusinessServer extends Remote{
	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException;
	public Car dequeueCar() throws RemoteException;
	public void registerCarPart(CarPart carPart) throws RemoteException;
	public void packageProduct(Product product) throws RemoteException;
	public void subscribeToCarQueue(ISubscriber subscriber) throws RemoteException;
	public void subscribeToCarPartsQueue(ISubscriber subscriber) throws RemoteException;
	public void subscribeToPalletsQueue(ISubscriber subscriber) throws RemoteException;
	public void subscribeToProductsQueue(ISubscriber subscriber) throws RemoteException;
	public void updateView(String message) throws RemoteException;
	public void generatePallets(String carPartType) throws RemoteException;
	public int getCarpartTypeQuantity(Integer value, String carPartType) throws RemoteException;
	public int getNextProductRegistrationNumber() throws RemoteException;
	public HashMap<CarPart, Pallet> getNextCarPartFromPallet(String carPartType) throws RemoteException;
}
