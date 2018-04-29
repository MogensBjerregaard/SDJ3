package tier3.postgres;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataAccessObjectFactory {
	IDataAccessObject getDataAccessObject(String className, Connection connection) throws SQLException;
	IDataAccessObject getDataAccessObject(Object objectType, Connection connection) throws SQLException;
	<T> IDataAccessObject getDataAccessObject(Class<T> classOfObject, Connection connection) throws SQLException;
}
