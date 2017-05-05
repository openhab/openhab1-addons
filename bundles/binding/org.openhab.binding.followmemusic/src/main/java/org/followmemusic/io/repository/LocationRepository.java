package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.UnexpectedSqlParameter;
import org.followmemusic.location.ObjectNotFoundException;
import org.followmemusic.model.Location;
import org.followmemusic.model.Sensor;
import org.followmemusic.model.User;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class LocationRepository extends DatabaseRepository<Location> {

	public LocationRepository(AbstractDatabaseConnection connection) {
		super(connection);
	}
		
	public void exportLocationsOfUser(User user) {
		
		// /tmp/followme_out.csv
		// SQL query
		String sql = "SELECT sensor_id, date, sensor_value "+
					 "FROM Location "+
					 "WHERE user_id = ?"+
					 "INTO OUTFILE '/tmp/followme_out_"+user.getId()+"_"+(new Date()).toString()+".csv'"+
					 "FIELDS TERMINATED BY ',' "+
					 "ENCLOSED BY '\"' "+
					 "LINES TERMINATED BY '\n';";
		
		// Params
		ArrayList<Object> params = new ArrayList<>();
		params.add(user.getId());
		
		// Execute SQL query
		java.util.Hashtable<String, Object> result;
		try {
			result = connection.executeQuery(sql, params);
			
			PreparedStatement statement = (PreparedStatement)result.get("statement");
			
			statement.close();
			
		} catch (SQLException | UnexpectedSqlParameter e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean deleteLocationsOfUser(User user) {
		
		try {
			// SQL query
			String sql = "DELETE FROM "+this.getTableName()+" WHERE user_id = ?";
			
			ArrayList<Object> params = new ArrayList<>();
			params.add(user.getId());
			
			java.util.Hashtable<String, Object> results = connection.executeUpdate(sql, params, false);
	        if (results != null) {
	            boolean success = ((Integer)results.get("result")) == 1;

	            if (success) {

	                PreparedStatement statement = (PreparedStatement)results.get("statement");
	                try {
	                    statement.close();
	                } catch (SQLException e) {
	                    
	                }

	                return true;
	            }

	        }

	        return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private ArrayList<Location> findUserLocations(User user, String sql, ArrayList<Object> params, boolean falt) throws IncompleteResultException, ObjectNotFoundException
	{		
		try {
			// Execute SQL query
			java.util.Hashtable<String, Object> result = connection.executeQuery(sql, params);
			ResultSet rs = (ResultSet) result.get("result");
			PreparedStatement statement = (PreparedStatement)result.get("statement");
				
			// Find locations
			ArrayList<Location> locations = this.instancesFromResultSet(rs, falt);
			
			// Close statement
			statement.close();
			
			return locations;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<Location> findRecentLocationsOfUser(User user, boolean falt) throws IncompleteResultException, ObjectNotFoundException
	{
		//String sql = "SELECT * FROM Location WHERE user_id = ? AND date >= DATE_SUB(\"2015-03-30 18:52:19\", INTERVAL 2 MINUTE) ORDER BY date DESC";
		String sql = "SELECT * FROM Location WHERE user_id = ? AND date >= DATE_SUB(NOW(), INTERVAL 1 SECOND) ORDER BY date DESC";
		
		ArrayList<Object> params = new ArrayList<>();
		params.add(user.getId());
			
		return this.findUserLocations(user, sql, params, falt);
	}
		
	public ArrayList<Location> findAllLocationsOfUser(User user, boolean falt) throws IncompleteResultException, ObjectNotFoundException {
		
		// SQL query
		String sql = "SELECT * FROM Location WHERE user_id = ? ORDER BY date DESC";
		
		ArrayList<Object> params = new ArrayList<>();
		params.add(user.getId());
		
		return this.findUserLocations(user, sql, params, falt);
	}

	@Override
	public Location instanceFromSingleResultSet(ResultSet rs, boolean falt)
			throws IncompleteResultException, SQLException,
			ObjectNotFoundException {
		
		// Get data
		int id = rs.getInt("id");
		int sensorId = rs.getInt("sensor_id");
		int userId = rs.getInt("user_id");
		Date date = rs.getDate("date");
		int sensorValue = rs.getInt("sensor_value");
				
		// Allocate user
		Location location = new Location(id, date, null, null, sensorValue);
		
		// Falt data
		if(falt) {
			location.setUser(new User(id));
			location.setSensor(new Sensor(sensorId));
		}
		else  {
			// Load data
			UserRepository userRepo = new UserRepository(this.connection);
			location.setUser(userRepo.findById(userId, true));
			
			SensorRepository sensorRepo = new SensorRepository(this.connection);
			location.setSensor(sensorRepo.findById(sensorId, true));
		}
		
		return location;
	}


	@Override
	public String getTableName() {
		return "Location";
	}


	@Override
	protected String getUpdateSql() {
		return "UPDATE "+this.getTableName()+" SET date = ?, sensor_value = ?";
	}


	@Override
	protected ArrayList<Object> getUpdateParams(Location object) {
		ArrayList<Object> params = new ArrayList<>();
		params.add(object.getDate());
		params.add(object.getSensorValue());
		return params;
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "+this.getTableName()+" (sensor_id, user_id, date, sensor_value) VALUES (?,?,?,?)";
	}

	@Override
	protected ArrayList<Object> getInsertParams(Location object) {
		if(object != null && object.getSensor() != null && object.getUser() != null) {
			ArrayList<Object> params = new ArrayList<>();
			params.add(object.getSensor().getId());
			params.add(object.getUser().getId());
			params.add(object.getDate());
			params.add(object.getSensorValue());
			return params;
		}
			
		return null;
	}
	

}
