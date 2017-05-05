package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.location.ObjectNotFoundException;
import org.followmemusic.model.Room;

import com.mysql.jdbc.ResultSet;

public class RoomRepository extends DatabaseRepository<Room> {

	public RoomRepository(AbstractDatabaseConnection connection) {
		super(connection);
	}

	@Override
	public Room instanceFromSingleResultSet(ResultSet rs, boolean falt)
			throws IncompleteResultException, SQLException, ObjectNotFoundException {
		
		// Get data
		int id = rs.getInt("id");
		String name = rs.getString("name");
		
		// Allocate room
		Room room = new Room(id, name);
		
		if(!falt) {
			// Find users
			UserRepository userRepo = new UserRepository(this.connection);
			room.setCurrentUsers(userRepo.findUsersInRoom(room));

			// Find sensors
			SensorRepository sensorRepo = new SensorRepository(this.connection);
			room.setSensors(sensorRepo.findSensorsInRoom(room));
		}
		
		return room;
	}

	@Override
	public String getTableName() {
		return "Room";
	}

	@Override
	protected String getUpdateSql() {
		return "UPDATE "+this.getTableName()+" SET name = ?";
	}

	@Override
	protected ArrayList<Object> getUpdateParams(Room object) {
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
	protected ArrayList<Object> getInsertParams(Room object) {
		// No insert
		return null;
	}

}
