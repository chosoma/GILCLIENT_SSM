package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.dbutils.handlers.AbstractListHandler;

public class TableListHandler extends AbstractListHandler<Vector<Object>> {

	public TableListHandler() {
	}

	@Override
	protected Vector<Object> handleRow(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		Vector<Object> result = new Vector<Object>();
		for (int i = 0; i < cols; i++) {
			result.add(rs.getObject(i + 1));
		}
		return result;
	}

}
