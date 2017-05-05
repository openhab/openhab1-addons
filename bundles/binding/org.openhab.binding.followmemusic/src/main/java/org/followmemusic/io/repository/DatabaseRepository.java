package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.UnexpectedSqlParameter;
import org.followmemusic.location.ObjectNotFoundException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public abstract class DatabaseRepository<T> extends AbstractRepository<T> {

	protected AbstractDatabaseConnection connection;
	
	public DatabaseRepository(AbstractDatabaseConnection connection) {
		this.connection = connection;
	}
	
	public abstract String getTableName();
	
	@Override
	public final T findById(int id, boolean falt) throws ObjectNotFoundException,
			IncompleteResultException {
		
		// SQL query
		String sql = "SELECT * FROM "+this.getTableName()+" WHERE id = ? LIMIT 1";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(id);
		
		try {
			// Execute SQL query
			java.util.Hashtable<String, Object> result = connection.executeQuery(sql, params);
			ResultSet rs = (ResultSet) result.get("result");
			PreparedStatement statement = (PreparedStatement)result.get("statement");
				
			// Find object
			T obj = null;
			if(rs.first()) {
				obj = this.instanceFromSingleResultSet(rs, true);
			}
			else {
				throw new ObjectNotFoundException(this.getTableName(), id);
			}
			
			// Close statement
			statement.close();
			
			return obj;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected abstract String getUpdateSql();
	protected abstract ArrayList<Object> getUpdateParams(T object);
	
	@Override
	public final void update(T object) {
		
		String sql = this.getUpdateSql();
		ArrayList<Object> params = this.getUpdateParams(object);
		
		this.update(sql, params);
	}
	
	public final void update(String sql, ArrayList<Object> params)
	{
		if(sql != null && params != null)
		{
			try {
				// Execute SQL query
				java.util.Hashtable<String, Object> result = connection.executeUpdate(sql, params, false);
				
				//ResultSet rs = (ResultSet) result.get("result");
				PreparedStatement statement = (PreparedStatement)result.get("statement");
				
				// Close statement
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (UnexpectedSqlParameter e) {
				e.printStackTrace();
			}
		}
	}
	
	protected abstract String getInsertSql();
	protected abstract ArrayList<Object> getInsertParams(T object);
	
	@Override
	public final void insert(T object) {
		
		String sql = this.getInsertSql();
		ArrayList<Object> params = this.getInsertParams(object);
		
		if(sql != null && params != null)
		{
			try {
				// Execute SQL query
				java.util.Hashtable<String, Object> result = connection.executeUpdate(sql, params, false);
				
				//ResultSet rs = (ResultSet) result.get("result");
				PreparedStatement statement = (PreparedStatement)result.get("statement");
				
				// Close statement
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (UnexpectedSqlParameter e) {
				e.printStackTrace();
			}
		}
	}
}
