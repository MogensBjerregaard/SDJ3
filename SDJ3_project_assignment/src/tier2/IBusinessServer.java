package tier2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.Car;
import common.CarPart;
import common.ISubscriber;
import common.Product;

public interface IBusinessServer extends Remote{
	public void enqueueCar(String chassisNumber, double weight, String model) throws RemoteException;
	public Car dequeueCar() throws RemoteException;
	public void registerCarPart(CarPart carPart) throws RemoteException;
	public void packageProduct(Product product) throws RemoteException;
	public void subscribeToCarQueue(ISubscriber subscriber) throws RemoteException;
	public void subscribeToCarPartsQueue(ISubscriber subscriber) throws RemoteException;
	public void subscribeToPalletsQueue(ISubscriber subscriber) throws RemoteException;
	public void updateView(String message) throws RemoteException;
	public void generatePallets(String carPartType) throws RemoteException;
}
