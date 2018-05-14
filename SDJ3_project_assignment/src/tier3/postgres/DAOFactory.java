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
	public IDataAccessObject getDataAccessObject(String className, Connection connection) throws SQLException {
		if (className.equals(Car.class.getSimpleName())) {
			return new CarDAO(connection);
		} else if (className.equals(CarPart.class.getSimpleName())) {
			return new CarPartDAO(connection);
		} else if (className.equals(Pallet.class.getSimpleName())) {
			return new PalletDAO(connection);
		} else if (className.equals(Product.class.getSimpleName())) {
			return new ProductDAO(connection);
		} else {
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
