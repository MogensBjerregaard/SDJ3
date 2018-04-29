package tier3.postgres.daos;

import common.Car;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDAO implements IDataAccessObject<Car> {
	private Connection connection;

	public CarDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Car object) throws SQLException {
		String sql = "INSERT INTO car (chassis_number, weight, model, dismantled) values (?,?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getChassisNumber());
		statement.setObject(2, object.getWeight());
		statement.setObject(3, object.getModel());
		statement.setObject(4, object.isDismantled());
		statement.execute();
	}

	@Override
	public Car read(String primaryKey) throws SQLException {
		String sql = "SELECT * FROM car WHERE chassis_number = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, primaryKey);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return createCar(resultSet);
	}

	@Override
	public List<Car> readAll() throws SQLException {
		String sql = "SELECT * FROM car";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		List<Car> result = new ArrayList<>();
		while (resultSet.next()) {
			result.add(createCar(resultSet));
		}
		return result;
	}

	@Override
	public void update(Car object) throws SQLException {
		String sql = "UPDATE car SET weight=?, model=?, dismantled=? WHERE chassis_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getWeight());
		statement.setObject(2, object.getModel());
		statement.setObject(3, object.isDismantled());
		statement.setObject(4, object.getChassisNumber());
		statement.execute();
	}

	@Override
	public void delete(String primaryKey) throws SQLException {
		String sql = "DELETE FROM car WHERE chassis_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, primaryKey);
		statement.execute();
	}

	@Override
	public void delete(Car object) throws SQLException {
		delete(object.getChassisNumber());
	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		throw new SQLException("Primary key for class 'Car' is not sequenced.");
	}

	private Car createCar(ResultSet resultSet) throws SQLException {
		String chassisNumber = resultSet.getString("chassis_number");
		double weight = resultSet.getDouble("weight");
		String model = resultSet.getString("model");
		boolean dismantled = resultSet.getBoolean("dismantled");
		return new Car(chassisNumber, weight, model, dismantled);
	}
}
