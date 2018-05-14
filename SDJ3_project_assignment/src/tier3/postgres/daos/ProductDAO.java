package tier3.postgres.daos;

import common.CarPart;
import common.Pallet;
import common.Product;
import tier3.postgres.IDataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IDataAccessObject<Product> {
	private Connection connection;

	public ProductDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void create(Product object) throws SQLException {
		String sql = "INSERT INTO product (registration_number, product_type) VALUES (?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getRegistrationNumber());
		statement.setObject(2, object.getProductType());
		statement.execute();

		// inserts references for all car parts associated with the given product
		for (CarPart carPart : object.getParts()) {
			String carPartSql = "UPDATE car_part SET product_registration_number=? WHERE registration_number=?";
			statement = connection.prepareStatement(carPartSql);
			statement.setObject(1, object.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public Product read(String primaryKey) throws SQLException {
		String sql = "SELECT * FROM product WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, Integer.parseInt(primaryKey));
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return nextProduct(resultSet);
	}

	@Override
	public List<Product> readAll() throws SQLException {
		String sql = "SELECT * FROM product";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Product> productList = new ArrayList<>();
		while (resultSet.next()) {
			productList.add(nextProduct(resultSet));
		}
		return productList;
	}

	@Override
	public void update(Product object) throws SQLException {
		String sql = "UPDATE product SET product_type=? WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getProductType());
		statement.setObject(2, object.getRegistrationNumber());
		statement.execute();

		sql = "UPDATE car_part SET product_registration_number=NULL WHERE product_registration_number=?";
		statement = connection.prepareStatement(sql);
		statement.setObject(1, object.getRegistrationNumber());
		statement.execute();

		for (CarPart carPart : object.getParts()) {
			sql = "UPDATE car_part SET product_registration_number=? WHERE registration_number=?";
			statement = connection.prepareStatement(sql);
			statement.setObject(1, object.getRegistrationNumber());
			statement.setObject(2, carPart.getRegistrationNumber());
			statement.execute();
		}
	}

	@Override
	public void delete(Product object) throws SQLException {
		delete(Integer.toString(object.getRegistrationNumber()));
	}

	@Override
	public void delete(String primaryKey) throws SQLException {
		String sql = "DELETE FROM product WHERE registration_number=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setObject(1, Integer.parseInt(primaryKey));
		statement.execute();
	}

	@Override
	public int getNextPrimaryKey() throws SQLException {
		String sql = "SELECT nextVal(pg_get_serial_sequence('product', 'registration_number')) AS next";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	private Product nextProduct(ResultSet resultSet) throws SQLException {
		// loads product
		int registrationNumber = resultSet.getInt("registration_number");
		String productType = resultSet.getString("product_type");
		Product product = new Product(registrationNumber, productType);

		// fetches all parts stored as part of the product
		String carPartSql = "SELECT registration_number, pallet_registration_number FROM car_part WHERE product_registration_number=?";
		PreparedStatement statement = connection.prepareStatement(carPartSql);
		statement.setObject(1, registrationNumber);
		ResultSet carPartResultSet = statement.executeQuery();
		while (carPartResultSet.next()) {
			String carPartRegistrationNumber = carPartResultSet.getString("registration_number");
			CarPart nextCarPart = new CarPartDAO(connection).read(carPartRegistrationNumber);
			product.addPart(nextCarPart);

			// if the part is stored on a pallet, that pallet is referenced in the product
			String palletRegistrationNumber = carPartResultSet.getString("pallet_registration_number");
			if (palletRegistrationNumber != null) {
				Pallet pallet = new PalletDAO(connection).read(palletRegistrationNumber);
				product.addPalletReference(pallet);
			}
		}
		return product;
	}
}
