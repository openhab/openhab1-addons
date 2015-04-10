package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.UnexpectedSqlParameter;
import org.followmemusic.location.ObjectNotFoundException;
import org.followmemusic.model.Room;
import org.followmemusic.model.Sensor;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class SensorRepository extends DatabaseRepository<Sensor> {

	public SensorRepository(AbstractDatabaseConnection connection) {
		super(connection);
	}

	@Override
	public Sensor instanceFromSingleResultSet(ResultSet rs, boolean falt)
			throws IncompleteResultException, SQLException,
			ObjectNotFoundException {
		
		// Get data
		int id = rs.getInt("id");
		int roomId = rs.getInt("room_id");
		
		// Allocate sensor
		Sensor sensor = new Sensor(id);
		
		if(falt) {
			sensor.setRoom(new Room(roomId));
		}
		else {
			// Find room
			RoomRepository roomRepository = new RoomRepository(this.connection);
			sensor.setRoom(roomRepository.findById(roomId, true));
			
			// Find door
			DoorRepository doorRepository = new DoorRepository(this.connection);
			sensor.setDoor(doorRepository.findDoorWithSensor(sensor));
		}
		
		return sensor;
	}

	public ArrayList<Sensor> findSensorsInRoom(Room room) throws IncompleteResultException, ObjectNotFoundException {
		
		// SQL query
		String sql = "SELECT * FROM "+this.getTableName()+" WHERE room_id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(room.getId());
		
		try {
			// Execute SQL query
			java.util.Hashtable<String, Object> result = connection.executeQuery(sql, params);
			ResultSet rs = (ResultSet) result.get("result");
			PreparedStatement statement = (PreparedStatement)result.get("statement");
				
			// Find locations
			ArrayList<Sensor> sensors = this.instancesFromResultSet(rs, true);
			
			// Close statement
			statement.close();
			
			return sensors;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@Override
	public String getTableName() {
		return "RFSensor";
	}

	@Override
	protected String getUpdateSql() {
		// Disable update method
		return null;
	}

	@Override
	protected ArrayList<Object> getUpdateParams(Sensor object) {
		// Disable update method
		return null;
	}

	@Override
	protected String getInsertSql() {
		// No insert
		return null;
	}

	@Override
	protected ArrayList<Object> getInsertParams(Sensor object) {
		// No insert
		return null;
	}

}
