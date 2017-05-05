package org.followmemusic.notification;

public class UpdateLocationNotification extends TCPNotification {
	
	private static final String targetIP = "127.0.0.1";
	private static final int port = 4545;
	
	public UpdateLocationNotification(String message) {
		super(targetIP, port, message);
	}

}
