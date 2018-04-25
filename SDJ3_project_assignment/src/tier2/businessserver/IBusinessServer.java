package tier2.businessserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Pallet;
import common.Product;
import common.Subject;

public interface IBusinessServer extends Remote{
	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException;
	public Car dequeueCar() throws RemoteException;
	public void registerCarPart(CarPart carPart) throws RemoteException;
	public void packageProduct(Product product) throws RemoteException;
	public void subscribe(ISubscriber subscriber, Subject... subjects) throws RemoteException;
	public void updateView(String message) throws RemoteException;
	public void generatePallets(String carPartType) throws RemoteException;
	public int getCarpartTypeQuantity(Integer value, String carPartType) throws RemoteException;
	public int getNextProductRegistrationNumber() throws RemoteException;
	public HashMap<CarPart, Pallet> getNextCarPartFromPallet(String carPartType) throws RemoteException;
}
