package org.followmemusic.location;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.model.Room;
import org.followmemusic.model.User;
import org.followmemusic.notification.UpdateLocationFailureNotification;
import org.followmemusic.notification.UpdateLocationSuccessNotification;
import org.openhab.binding.followmemusic.internal.FollowMeMusicGenericBindingProvider.FollowMeMusicBindingConfig;
import org.openhab.core.types.Command;

public class FollowMeMusicLocationManager extends DatabaseLocationManager {
	
	public FollowMeMusicLocationManager(AbstractDatabaseConnection connection) {
		super(connection);
	}
	
	/**
	 * Update location request has been received
	 * 
	 * @param config
	 * @param command
	 * @throws ObjectNotFoundException 
	 * @throws CommandFormatException 
	 */
	@Override
	public void handleUpdateLocationRequest(FollowMeMusicBindingConfig config, Command command) throws ObjectNotFoundException, CommandFormatException
	{
		this.handleUpdateLocationRequest(config, command, new UpdateLocationRequestCallback() {
			@Override
			public void success(User user, Room newRoom, Room previousRoom) {
				if(newRoom != null && previousRoom != null && newRoom.getId() != previousRoom.getId()) {
					UpdateLocationSuccessNotification notif = new UpdateLocationSuccessNotification(user, newRoom);
					notif.send();
				}
			}
			
			@Override
			public void failure(User user) {
				if(user != null) {
					UpdateLocationFailureNotification notif = new UpdateLocationFailureNotification(user);
					notif.send();
				}
			}
		});
	}

}
