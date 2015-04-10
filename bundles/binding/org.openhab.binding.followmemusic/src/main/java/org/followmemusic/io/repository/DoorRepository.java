package org.followmemusic.io.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.UnexpectedSqlParameter;
import org.followmemusic.location.ObjectNotFoundException;
import org.followmemusic.model.Door;
import org.followmemusic.model.Sensor;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class DoorRepository extends DatabaseRepository<Door> {

	public DoorRepository(AbstractDatabaseConnection connection) {
		super(connection);
	}

	@Override
	public String getTableName() {
		return "Door";
	}

	@Override
	protected String getUpdateSql() {
		// Disable update method
		return null;
	}

	@Override
	protected ArrayList<Object> getUpdateParams(Door object) {
		// Disable update method
		return null;
	}
	
	public Door findDoorWithSensor(Sensor sensor) throws IncompleteResultException, ObjectNotFoundException {
		
		// SQL query
		String sql = "SELECT * FROM "+this.getTableName()+" WHERE sensor1_id = ? OR sensor2_id = ? LIMIT 1";
		ArrayList<Object> params = new ArrayList<>();
		params.add(sensor.getId());
		params.add(sensor.getId());
		
		try {
			// Execute SQL query
			java.util.Hashtable<String, Object> result = connection.executeQuery(sql, params);
			ResultSet rs = (ResultSet) result.get("result");
			PreparedStatement statement = (PreparedStatement)result.get("statement");
				
			// Find object
			Door door = null;
			if(rs.first()) {
				door = this.instanceFromSingleResultSet(rs, true);
			}
			
			// Close statement
			statement.close();
			
			return door;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnexpectedSqlParameter e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public Door instanceFromSingleResultSet(ResultSet rs, boolean falt)
			throws IncompleteResultException, SQLException,
			ObjectNotFoundException {
		
		// Get data
		int id = rs.getInt("id");
		int sensor1Id = rs.getInt("sensor1_id");
		int sensor2Id = rs.getInt("sensor2_id");
		
		// Allocate door
		Door door = new Door(id);
		
		if(falt) {
			Sensor[] sensors = new Sensor[2];
			sensors[0] = new Sensor(sensor1Id);
			sensors[1] = new Sensor(sensor2Id);
			door.setSensors(sensors);
		}
		else {
			// Find sensors
			SensorRepository sensorRepo = new SensorRepository(this.connection);
			Sensor[] sensors = new Sensor[2];
			sensors[0] = sensorRepo.findById(sensor1Id);
			sensors[1] = sensorRepo.findById(sensor2Id);
			door.setSensors(sensors);
		}
		
		return door;
	}

	@Override
	protected String getInsertSql() {
		// No insert
		return null;
	}

	@Override
	protected ArrayList<Object> getInsertParams(Door object) {
		// No insert
		return null;
	}

	
}
