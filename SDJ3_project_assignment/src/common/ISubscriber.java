package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISubscriber extends Remote{
	void updateEnqueuedCarList(String message) throws RemoteException;
	void updateCarPartsList(String message) throws RemoteException;
	void updatePalletsList(String message) throws RemoteException;
	void updateProductsList(String message) throws RemoteException;
}
