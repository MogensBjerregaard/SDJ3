package tier3.postgres.daos;

import common.Car;
import common.CarPart;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		return null;
	}

	@Override
	public void update(CarPart object) throws SQLException {

	}

	@Override
	public void delete(CarPart object) throws SQLException {

	}

	@Override
	public void delete(String primaryKey) throws SQLException {

	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		throw new SQLException("Primary key for class 'CarPart' is not sequenced.");
	}

	private CarPart createCarPart(ResultSet resultSet) throws SQLException {
		String registration_number = resultSet.getString("registration_number");
		String type = resultSet.getString("type");
		double weight = resultSet.getDouble("weight");
		String model = resultSet.getString("model");
		boolean dismantled = resultSet.getBoolean("dismantled");
		return null;
//		Car car = new Car();

//		return new CarPart(registration_number, type, weight);
	}
}
