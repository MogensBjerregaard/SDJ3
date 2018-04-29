package tier3.postgres.daos;

import common.CarPart;
import common.Pallet;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PalletDAO implements IDataAccessObject<Pallet> {
	private Connection connection;

	public PalletDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Pallet object) throws SQLException {
		PreparedStatement statement =
				connection.prepareStatement("insert into pallet (registration_number, type_of_part, max_weight) values (?,?,?)");
		statement.setObject(1, object.getRegistrationNumber());
		statement.setObject(2, object.getTypeOfPart());
		statement.setObject(3, object.getMaxWeight());
		statement.execute();

		// inserts references for all car parts on the pallet
		for (CarPart carPart : object.getParts()) {
			statement =
					connection.prepareStatement("update car_part set pallet_registration_number = ? where registration_number = ?");
			statement.setObject(1, object.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public Pallet read(String primaryKey) throws SQLException {
		return null;
	}

	@Override
	public List<Pallet> readAll() throws SQLException {
		return null;
	}

	@Override
	public void update(Pallet object) throws SQLException {

	}

	@Override
	public void delete(Pallet object) throws SQLException {

	}

	@Override
	public void delete(String primaryKey) throws SQLException {

	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		String sql = "SELECT nextVal(pg_get_serial_sequence('pallet', 'registration_number')) AS next";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}
}
