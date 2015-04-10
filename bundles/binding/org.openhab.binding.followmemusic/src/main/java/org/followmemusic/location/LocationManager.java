package org.followmemusic.location;

import org.followmemusic.model.Location;
import org.followmemusic.model.Room;
import org.followmemusic.model.Sensor;
import org.followmemusic.model.User;
import org.openhab.binding.followmemusic.internal.FollowMeMusicGenericBindingProvider.FollowMeMusicBindingConfig;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author pieraggi
 *
 */
public abstract class LocationManager {
		
	protected static final Logger logger = 
			LoggerFactory.getLogger(LocationManager.class);
	
	/**
	 * Callback after an update location request
	 * @author pieraggi
	 *
	 */
	public interface UpdateLocationRequestCallback {
		/**
		 * Update is successful
		 */
		public void success(User user, Room newRoom, Room previousRoom);
		/**
		 * Update has failed
		 */
		public void failure(User user);
	}
	
	/**
	 * Find user that have the specified id
	 * Inside the user, the list of all his previous locations should be filled
	 * 
	 * @param User's id
	 * @return User
	 * @throws ObjectNotFoundException 
	 */
	protected abstract User findUser(int id) throws ObjectNotFoundException;
	
	/**
	 * Extract the user id from the command
	 * 
	 * @param command
	 * @return User's id
	 * @throws CommandFormatException 
	 */
	protected abstract int extractUserId(Command command) throws CommandFormatException;
	
	/**
	 * Extract the value of the sensor from the command
	 * @param command
	 * @return sensor's value
	 * @throws CommandFormatException 
	 */
	protected abstract int extractSensorValue(Command command) throws CommandFormatException;
	
	/**
	 * Algorithm that computes previous user's locations and determine the room in which the user is currently
	 * @param user
	 * @return
	 */
	protected abstract Room computeUserLocation(User user);
	
	/**
	 * Save user location if wanted
	 * Should be overridden in child class
	 * @param location
	 */
	protected void persistLocation(Location location)
	{
		logger.error("Location not persisted");
	}
	
	/**
	 * Save user data if wanted
	 * Should be overridden in child class
	 * @param user
	 */
	protected void updateUser(User user)
	{
		logger.debug("User not persisted");
	}
	
	/**
	 * Save room data if wanted
	 * Should be overridden in child class
	 * @param room
	 */
	protected void updateRoom(Room room) {
		logger.debug("Room not persisted");
	}
	
	/**
	 * Do some additional work after the room is updated
	 * @param user
	 */
	protected void newRoomUpdated(User user) {
		logger.debug("No work done in \"newRoomUpdated\"");
	}
	
	/**
	 * Update location request has been received
	 * 
	 * @param config
	 * @param command
	 * @param callback
	 * @throws ObjectNotFoundException 
	 * @throws CommandFormatException 
	 */
	public void handleUpdateLocationRequest(FollowMeMusicBindingConfig config, Command command, UpdateLocationRequestCallback callback) throws ObjectNotFoundException, CommandFormatException
	{
		// Extract data from command
		int userId = this.extractUserId(command);
		int sensorValue = this.extractSensorValue(command);
		
		// Get model objects
		Sensor sensor = (Sensor)config.getItemModel();
		User user = this.findUser(userId);
		
		// Create new location
		Location location = new Location(user, sensor, sensorValue);
		// Add location to user
		user.addLocation(location);
		
		// Persist if needed
		this.persistLocation(location);
		
		//try {
			// Compute user's position
			Room newRoom = this.computeUserLocation(user);
			Room previousRoom = user.getCurrentRoom();
			
			if(newRoom != null && previousRoom != null && newRoom.getId() != previousRoom.getId()) {
				logger.info(user.getName()+" : New room -> "+newRoom.getId());
				
				// Update user
				user.setCurrentRoom(newRoom);
				newRoom.addCurrentUser(user);
				previousRoom.removeCurrentUser(user);
				
				// Save data
				this.updateRoom(previousRoom);
				this.updateRoom(newRoom);
				this.updateUser(user);
				
				// Do some work if wanted
				this.newRoomUpdated(user);
			}
			else {
				logger.info(user.getName()+" -> No new room");
			}	
			
			// Notify
			if(callback != null) {
				callback.success(user, newRoom, previousRoom);
			}
			
		/*} catch (UserLocationException e) {
			e.printStackTrace();
			callback.failure(user);
		}*/
		
	}
	
	
	/**
	 * Update location request has been received
	 * 
	 * @param config
	 * @param command
	 * @throws ObjectNotFoundException 
	 * @throws CommandFormatException 
	 */
	public void handleUpdateLocationRequest(FollowMeMusicBindingConfig config, Command command) throws ObjectNotFoundException, CommandFormatException
	{
		this.handleUpdateLocationRequest(config, command, null);
	}
	
}
