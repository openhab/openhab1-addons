package org.followmemusic.notification;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.followmemusic.location.LocationManager;
import org.followmemusic.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author pieraggi
 */
public class TCPNotification implements Notification {

	protected static final Logger logger = 
			LoggerFactory.getLogger(LocationManager.class);
	
	private String targetIP;
	private int port;
	private String message;
	
	public TCPNotification(String targetIP, int port, String message) {
		this.targetIP = targetIP;
		this.port = port;
		this.message = message;
	}

	@Override
	public boolean send() {
 
		try {
			// Create TCP socket
			Socket clientSocket = new Socket(this.getTargetIP(), this.getPort());
			
			// Open stream writer
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			// Send message
			outToServer.writeBytes(this.getMessage());

			// Close socket
			clientSocket.close();
			
			// Log
			logger.debug("TCP Notification sent ("+this.targetIP+":"+this.port+") : "+this.message);
			
			// Success
			return true;
			
		} catch (IOException e) {
			
			logger.error("TCP Notification failed ("+this.targetIP+":"+this.port+") : "+e.getLocalizedMessage());
			
			// Failure
			return false;
		}
	}

	public String getTargetIP() {
		return targetIP;
	}

	public void setTargetIP(String targetIP) {
		this.targetIP = targetIP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
