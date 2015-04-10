package org.followmemusic.notification;

public interface Notification {

	/**
	 * Send a notification
	 * 
	 * @return TRUE if the notification was successfully sent, FALSE otherwise
	 */
	public boolean send();
	
}
