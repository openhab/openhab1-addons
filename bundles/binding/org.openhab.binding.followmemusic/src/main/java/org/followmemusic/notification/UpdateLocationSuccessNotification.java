package org.followmemusic.notification;

import org.followmemusic.model.Room;
import org.followmemusic.model.User;

public class UpdateLocationSuccessNotification extends UpdateLocationNotification {
	public UpdateLocationSuccessNotification(User user, Room newRoom)
	{		
		super("position:success:"+user.getId()+":"+newRoom.getId());
	}
}
