package org.followmemusic.notification;

import org.followmemusic.model.User;

public class UpdateLocationFailureNotification extends
		UpdateLocationNotification {

	public UpdateLocationFailureNotification(User user) {
		super("position:failure:"+user.getId());
	}

}
