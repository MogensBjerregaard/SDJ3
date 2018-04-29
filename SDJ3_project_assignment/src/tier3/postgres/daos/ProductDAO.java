package tier3.postgres.daos;

import common.CarPart;
import common.Product;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDAO implements IDataAccessObject<Product> {
	private Connection connection;

	public ProductDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Product object) throws SQLException {
		Product product = (Product) object;
		PreparedStatement statement = connection.prepareStatement("insert into product (registration_number, product_type) values (?,?)");
		statement.setObject(1, product.getRegistrationNumber());
		statement.setObject(2, product.getProductType());
		statement.execute();

		// inserts references for all car parts associated with the given product
		for (CarPart carPart : product.getParts()) {
			statement = connection.prepareStatement("update car_part set product_registration_number = ? where registration_number = ?");
			statement.setObject(1, product.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public Product read(String primaryKey) throws SQLException {
		return null;
	}

	@Override
	public List<Product> readAll() throws SQLException {
		return null;
	}

	@Override
	public void update(Product object) throws SQLException {

	}

	@Override
	public void delete(Product object) throws SQLException {

	}

	@Override
	public void delete(String primaryKey) throws SQLException {

	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		String sql = "SELECT nextVal(pg_get_serial_sequence('product', 'registration_number')) AS next";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}
}
