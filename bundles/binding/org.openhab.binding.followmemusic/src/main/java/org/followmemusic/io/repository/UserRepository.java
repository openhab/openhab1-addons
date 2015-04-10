package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.UnexpectedSqlParameter;
import org.followmemusic.location.ObjectNotFoundException;
import org.followmemusic.model.Room;
import org.followmemusic.model.User;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class UserRepository extends DatabaseRepository<User> {
	
	public UserRepository(AbstractDatabaseConnection connection) {
		super(connection);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws ObjectNotFoundException 
	 * @throws IncompleteResultException 
	 * @throws SQLException 
	 */
	@Override
	public User instanceFromSingleResultSet(ResultSet rs, boolean falt) throws IncompleteResultException, SQLException, ObjectNotFoundException {
		
		// Get data
		int id = rs.getInt("id");
		String name = rs.getString("name");
		int roomId = rs.getInt("currentRoom_id");
		
		// Allocate user
		User user = new User(id, name);
		
		if(falt) {
			user.setCurrentRoom(new Room(roomId));
		}
		else {
			// Find room
			RoomRepository roomRepository = new RoomRepository(this.connection);
			user.setCurrentRoom(roomRepository.findById(roomId, true));
			
			// Find locations
			LocationRepository locationRepository = new LocationRepository(this.connection);
			user.setLocations(locationRepository.findAllLocationsOfUser(user, falt));
		}
		
		return user;
	}
	
	public ArrayList<User> findUsersInRoom(Room room) throws IncompleteResultException, ObjectNotFoundException {
		
		// SQL query
		String sql = "SELECT * FROM "+this.getTableName()+" WHERE currentRoom_id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(room.getId());
		
		try {
			// Execute SQL query
			java.util.Hashtable<String, Object> result = connection.executeQuery(sql, params);
			ResultSet rs = (ResultSet) result.get("result");
			PreparedStatement statement = (PreparedStatement)result.get("statement");
				
			// Find locations
			ArrayList<User> users = this.instancesFromResultSet(rs, true);
			
			// Close statement
			statement.close();
			
			return users;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void updateRelationship(User user)
	{
		String sql = "UPDATE "+this.getTableName()+" SET currentRoom_id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add( (user.getCurrentRoom() != null) ? user.getCurrentRoom().getId() : null );
		
		this.update(sql, params);
	}
	
	@Override
	public String getTableName() {
		return "User";
	}

	@Override
	protected String getUpdateSql() {
		return "UPDATE "+this.getTableName()+" SET name = ?";
	}

	@Override
	protected ArrayList<Object> getUpdateParams(User object) {
		ArrayList<Object> params = new ArrayList<>();
		params.add(object.getName());
		return params;
	}

	@Override
	protected String getInsertSql() {
		// No insert
		return null;
	}

	@Override
	protected ArrayList<Object> getInsertParams(User object) {
		// No insert
		return null;
	}

}
