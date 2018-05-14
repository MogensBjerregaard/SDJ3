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
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DataServerController extends UnicastRemoteObject implements IDataServer {
	private String jdbcUrl;
	private String username;
	private String password;
	private IDataAccessObjectFactory daoFactory;
	private IDataServerView view;

	public DataServerController(String jdbcUrl, String username, String password, IDataAccessObjectFactory daoFactory, IDataServerView view) throws RemoteException {
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

	private Connection getConnection() throws RemoteException {
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
	public <T> void create(T object) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object.getClass().getSimpleName(), connection);
			dataAccessObject.create(object);
			view.update("Inserted: " + object.toString());
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> T read(Class<T> objectType, String primaryKey) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(objectType.getSimpleName(), connection);
			T result = dataAccessObject.read(primaryKey);
			view.update("Read: " + result.toString());
			return result;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> List<T> readAll(Class<T> objectType) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(objectType.getSimpleName(), connection);
			List<T> result = dataAccessObject.readAll();
			view.update("Read All: " + objectType.getSimpleName());
			return result;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void update(T object) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object.getClass().getSimpleName(), connection);
			dataAccessObject.update(object);
			view.update("Updated: " + object.toString());
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void delete(T object) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(object, connection);
			dataAccessObject.delete(object);
			view.update("Deleted: " + object.toString());
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> void delete(Class<T> objectType, String primaryKey) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(objectType, connection);
			dataAccessObject.delete(primaryKey);
			view.update("Deleted: " + primaryKey + " from " + objectType.getSimpleName());
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public <T> int getNextPrimaryKey(Class<T> objectType) throws RemoteException {
		try (Connection connection = getConnection()) {
			IDataAccessObject<T> dataAccessObject =
					daoFactory.getDataAccessObject(objectType, connection);
			int result = dataAccessObject.getNextPrimaryKey();
			view.update("Next value in sequence requested for: " + objectType.getSimpleName());
			return result;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public void initiateQueues() throws RemoteException {

	}

	@Override
	public ArrayList<Product> getMatchingProducts(String carChassisNumber) throws RemoteException {
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
			view.update("Products traced for: " + carChassisNumber);
			return products;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public List<Car> getNondismantledCars() throws RemoteException {
		try (Connection connection = getConnection()) {
			String sql = "SELECT * FROM car WHERE dismantled=false";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			ArrayList<Car> nondismantledCars = new ArrayList<>();
			while (resultSet.next()) {
				String chassisNumber = resultSet.getString("chassis_number");
				double weight = resultSet.getDouble("weight");
				String model = resultSet.getString("model");
				boolean dismantled = resultSet.getBoolean("dismantled");
				nondismantledCars.add(new Car(chassisNumber, weight, model, dismantled));
			}
			return nondismantledCars;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public List<CarPart> getUnusedCarParts() throws RemoteException {
		try (Connection connection = getConnection()) {
			String sql = "SELECT * FROM car_part JOIN car ON (car_part.car_chassis_number=car.chassis_number) WHERE pallet_registration_number is null AND product_registration_number is null";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			ArrayList<CarPart> unusedCarParts = new ArrayList<>();
			while (resultSet.next()) {
				String carChassisNumber = resultSet.getString("chassis_number");
				double carWeight = resultSet.getDouble("weight");
				String carModel = resultSet.getString("model");
				boolean carIsDismantled = resultSet.getBoolean("dismantled");
				Car car = new Car(carChassisNumber, carWeight, carModel, carIsDismantled);

				String registrationNumber = resultSet.getString("registration_number");
				String type = resultSet.getString("type");
				double weight = resultSet.getDouble("weight");
				String model = resultSet.getString("model");
				unusedCarParts.add(new CarPart(registrationNumber, car, type, weight));
			}
			return null;
		} catch (SQLException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
}
