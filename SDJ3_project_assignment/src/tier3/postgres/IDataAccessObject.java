package tier3.postgres;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface IDataAccessObject<T> {
	void create(T object) throws SQLException;
	T read(String primaryKey) throws SQLException;
	List<T> readAll() throws SQLException;
	void update(T object) throws SQLException;
	void delete(T object) throws SQLException;
	void delete(String primaryKey) throws SQLException;
	int getNextPrimaryKey() throws SQLException;
}
