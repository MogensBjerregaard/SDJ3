package tier3.postgres;

import common.Car;
import common.CarPart;
import common.Pallet;
import common.Product;
import tier3.postgres.daos.CarDAO;
import tier3.postgres.daos.CarPartDAO;
import tier3.postgres.daos.PalletDAO;
import tier3.postgres.daos.ProductDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory implements IDataAccessObjectFactory {
	@Override
	public IDataAccessObject getDataAccessObject(String className, Connection connection)
			throws SQLException {
		switch (className) {
			case "Car":
				return new CarDAO(connection);
			case "CarPart":
				return new CarPartDAO(connection);
			case "Pallet":
				return new PalletDAO(connection);
			case "Product":
				return new ProductDAO(connection);
			default:
				throw new SQLException("Class does not exist as entity in database");
		}
	}

	@Override
	public IDataAccessObject getDataAccessObject(Object objectType, Connection connection) throws SQLException {
		return getDataAccessObject(objectType.getClass().getSimpleName(), connection);
	}

	@Override
	public IDataAccessObject getDataAccessObject(Class classOfObject, Connection connection) throws SQLException {
		return getDataAccessObject(classOfObject.getSimpleName(), connection);
	}
}
