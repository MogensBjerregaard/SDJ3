package tier3.postgres.daos;

import common.CarPart;
import common.Pallet;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PalletDAO implements IDataAccessObject<Pallet> {
	private Connection connection;

	public PalletDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Pallet object) throws SQLException {
		String sql = "INSERT INTO pallet (registration_number, type_of_part, max_weight) VALUES (?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getRegistrationNumber());
		statement.setObject(2, object.getTypeOfPart());
		statement.setObject(3, object.getMaxWeight());
		statement.execute();

		// inserts references for all car parts on the pallet
		for (CarPart carPart : object.getParts()) {
			sql = "UPDATE car_part SET pallet_registration_number=? WHERE registration_number=?";
			statement = connection.prepareStatement(sql);
			statement.setObject(1, object.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public Pallet read(String primaryKey) throws SQLException {
		String sql = "SELECT * FROM pallet WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, Integer.parseInt(primaryKey));
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return createPallet(resultSet);
	}

	@Override
	public List<Pallet> readAll() throws SQLException {
		String sql = "SELECT * FROM pallet";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Pallet> pallets = new ArrayList<>();
		while (resultSet.next()) {
			Pallet pallet = createPallet(resultSet);
			pallets.add(pallet);
		}
		return pallets;
	}

	@Override
	public void update(Pallet object) throws SQLException {
		String sql = "UPDATE pallet SET type_of_part=?, max_weight=? WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getTypeOfPart());
		statement.setObject(2, object.getMaxWeight());
		statement.setObject(3, object.getRegistrationNumber());
		statement.execute();

		// clears all foreign keys to the pallet
		sql = "UPDATE car_part SET pallet_registration_number=NULL WHERE pallet_registration_number=?";
		statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getRegistrationNumber());
		statement.execute();

		// inserts references for all car parts on the pallet
		for (CarPart carPart : object.getParts()) {
			sql = "UPDATE car_part SET pallet_registration_number=? WHERE registration_number=?";
			statement = connection.prepareStatement(sql);
			statement.setObject(1, object.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public void delete(Pallet object) throws SQLException {
		delete(Integer.toString(object.getRegistrationNumber()));
	}

	@Override
	public void delete(String primaryKey) throws SQLException {
		String sql = "DELETE FROM pallet WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, Integer.parseInt(primaryKey));
		statement.execute();
	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		String sql = "SELECT nextVal(pg_get_serial_sequence('pallet', 'registration_number')) AS next";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	private Pallet createPallet(ResultSet resultSet) throws SQLException {
		int registrationNumber = resultSet.getInt("registration_number");
		String typeOfPart = resultSet.getString("type_of_part");
		double maxWeight = resultSet.getDouble("max_weight");
		Pallet pallet = new Pallet(registrationNumber, typeOfPart, maxWeight);

		String sql = "SELECT registration_number FROM car_part WHERE pallet_registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, pallet.getRegistrationNumber());
		ResultSet carPartResultSet = statement.executeQuery();
		CarPartDAO carPartDAO = new CarPartDAO(connection);
		while (carPartResultSet.next()) {
			String carPartRegistrationNumber = carPartResultSet.getString("registration_number");
			pallet.addParts(carPartDAO.read(carPartRegistrationNumber));
		}

		return pallet;
	}
}
