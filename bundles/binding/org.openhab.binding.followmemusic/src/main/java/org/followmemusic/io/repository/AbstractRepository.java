package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.location.ObjectNotFoundException;

import com.mysql.jdbc.ResultSet;

public abstract class AbstractRepository<T> {
	
	/**
	 * Find an object with his id
	 * @param id
	 * @return
	 * @throws ObjectNotFoundException 
	 * @throws IncompleteResultException 
	 */
	public abstract T findById(int id, boolean falt) throws ObjectNotFoundException, IncompleteResultException;
	
	public T findById(int id) throws ObjectNotFoundException, IncompleteResultException {
		return this.findById(id, false);
	}
	
	/**
	 * Update object
	 * @param object
	 */
	public abstract void update(T object);
	
	/**
	 * Insert object
	 * @param object
	 */
	public abstract void insert(T object);
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws IncompleteResultException
	 * @throws SQLException 
	 * @throws ObjectNotFoundException 
	 */
	public abstract T instanceFromSingleResultSet(ResultSet rs, boolean falt) throws IncompleteResultException, SQLException, ObjectNotFoundException;
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws IncompleteResultException
	 * @throws SQLException
	 * @throws ObjectNotFoundException
	 */
	public ArrayList<T> instancesFromResultSet(ResultSet rs, boolean falt) throws IncompleteResultException, SQLException, ObjectNotFoundException
	{
		try {
            // List of objects
            ArrayList<T> instances = new ArrayList<T>();

            // For each instance present in the ResultSet
            while (rs.next()) {
                // Parse and add to the list
                instances.add(this.instanceFromSingleResultSet(rs, falt));
            }

            return instances;
        }
        catch (SQLException e) {
            
        }

        return null;
	}
}
