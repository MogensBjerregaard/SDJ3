package tier3;

import common.Car;
import common.CarPart;
import common.Product;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IDataServer extends Remote {
	// Generics methods to be used with any class.
	<T> void create(T object)
			throws RemoteException;
	<T> T read(Class<T> classOfObject, String primaryKey)
			throws RemoteException;
	<T> List<T> readAll(Class<T> classOfObject	)
			throws RemoteException;
	<T> void update(T object)
			throws RemoteException;
	<T> void delete(T object)
			throws RemoteException;
	<T> void delete(Class<T> objectType, String primaryKey)
			throws RemoteException;
	<T> int getNextPrimaryKey(Class<T> objectType)
			throws RemoteException;

	// Query to be used by web server.
	ArrayList<Product> getMatchingProducts(String carChassisNumber)
			throws RemoteException;
}
