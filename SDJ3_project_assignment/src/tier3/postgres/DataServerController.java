package tier3.postgres;

import common.Car;
import common.CarPart;
import common.Product;
import org.postgresql.Driver;
import tier3.DataServerView;
import tier3.IDataServer;
import tier3.IDataServerView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DataServerController extends UnicastRemoteObject implements IDataServer {
	private String jdbcUrl;
	private String username;
	private String password;
	private IDataAccessObjectFactory daoFactory;
	private IDataServerView view;

	public DataServerController(String jdbcUrl, String username, String password, IDataAccessObjectFactory daoFactory, IDataServerView view)
			throws RemoteException {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.daoFactory = daoFactory;
		this.view = view;
		try {
			DriverManager.registerDriver(new Driver());
		} catch (SQLException e) {
			throw new RemoteException("No JDBC driver", e);
		}
	}

	private Connection getConnection()
			throws RemoteException {
		try {
			if (username == null) {
				return DriverManager.getConnection(jdbcUrl);
			} else {
				return DriverManager.getConnection(jdbcUrl, username, password);
			}
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void create(T object)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object, connection);
			dataAccessObject.create(object);
			message("Inserted: " + object.toString());
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public <T> T read(Class<T> classOfObject, String primaryKey)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(classOfObject, connection);
			T result = dataAccessObject.read(primaryKey);
			message("Read: " + result.toString());
			return result;
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> List<T> readAll(Class<T> classOfObject)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(classOfObject, connection);
			List<T> result = dataAccessObject.readAll();
			message("Read All: " + classOfObject.getSimpleName());
			return result;
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void update(T object)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object, connection);
			dataAccessObject.update(object);
			message("Updated: " + object.toString());
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void delete(T object)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object, connection);
			dataAccessObject.delete(object);
			message("Deleted: " + object.toString());
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void delete(Class<T> classOfObject, String primaryKey)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(classOfObject, connection);
			dataAccessObject.delete(primaryKey);
			message("Deleted: " + primaryKey + " from " + classOfObject.getSimpleName());
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> int getNextPrimaryKey(Class<T> classOfObject)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(classOfObject, connection);
			int result = dataAccessObject.getNextPrimaryKey();
			message("Next value in sequence requested for: " + classOfObject.getSimpleName());
			return result;
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public ArrayList<Product> getMatchingProducts(String carChassisNumber)
			throws RemoteException {
		try (Connection connection = getConnection()) {
			String sql = "SELECT * FROM product WHERE registration_number IN (SELECT product_registration_number FROM car_part WHERE car_chassis_number= ?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setObject(1, carChassisNumber);
			ResultSet resultSet = statement.executeQuery();
			ArrayList<Product> products = new ArrayList<>();
			while (resultSet.next()) {
				int registrationNumber = resultSet.getInt("registration_number");
				String productType = resultSet.getString("product_type");
				products.add(new Product(registrationNumber, productType));
			}
			message("Products traced for: " + carChassisNumber);
			return products;
		} catch (SQLException e) {
			message("EXCEPTION:\n" + e.getLocalizedMessage());
			throw new RemoteException(e.getMessage(), e);
		}
	}

	private void message(String message) {
		LocalDateTime timePoint = LocalDateTime.now();
		view.update(timePoint + ": " + message);
	}
}
