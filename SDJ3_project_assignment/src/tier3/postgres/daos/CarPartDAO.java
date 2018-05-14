package tier3.postgres.daos;

import common.Car;
import common.CarPart;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarPartDAO implements IDataAccessObject<CarPart> {
	private Connection connection;

	public CarPartDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(CarPart object) throws SQLException {
		String sql = "INSERT INTO car_part (registration_number, type, weight, car_chassis_number) VALUES (?,?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getRegistrationNumber());
		statement.setObject(2, object.getType());
		statement.setObject(3, object.getWeight());
		statement.setObject(4, object.getCar().getChassisNumber());
		statement.execute();
	}

	@Override
	public CarPart read(String primaryKey) throws SQLException {
		String sql = "SELECT * FROM car_part WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, primaryKey);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return createCarPart(resultSet);
	}

	@Override
	public List<CarPart> readAll() throws SQLException {
		String sql = "SELECT * FROM car_part";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<CarPart> result = new ArrayList<>();
		while (resultSet.next()) {
			result.add(createCarPart(resultSet));
		}
		return result;
	}

	@Override
	public void update(CarPart object) throws SQLException {
		String sql = "UPDATE car_part SET type=?, weight=?, car_chassis_number=? WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getType());
		statement.setObject(2, object.getWeight());
		statement.setObject(3, object.getCar().getChassisNumber());
		statement.setObject(4, object.getRegistrationNumber());
		statement.execute();
	}

	@Override
	public void delete(CarPart object) throws SQLException {
		delete(object.getRegistrationNumber());
	}

	@Override
	public void delete(String primaryKey) throws SQLException {
		String sql = "DELETE FROM car_part WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, primaryKey);
		statement.execute();
	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		throw new SQLException("Primary key for class 'CarPart' is not sequenced.");
	}

	public CarPart createCarPart(ResultSet resultSet) throws SQLException {
		String carChassisNumber = resultSet.getString("car_chassis_number");
		String registrationNumber = resultSet.getString("registration_number");
		String type = resultSet.getString("type");
		double weight = resultSet.getDouble("weight");

		Car car = new CarDAO(connection).read(carChassisNumber);
		return new CarPart(registrationNumber, car, type, weight);
	}
}
