/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.io.InputStream;
import java.io.OutputStream;

import org.openhab.binding.insteonplm.internal.driver.hub.HubIOStream;

/**
 * Abstract class for implementation for I/O stream with anything that looks
 * like a PLM (e.g. the insteon hub)
 * 
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.7.0
 */

public abstract class IOStream {
	protected InputStream	m_in	= null;
	protected OutputStream	m_out	= null;
	/**
	 * Getter for input stream from modem/hub
	 * @return input stream for incoming data
	 */
	public InputStream in() { return m_in; }
	/**
	 * Getter for output stream for outgoing data to the modem/hub
	 * @return
	 */
	public OutputStream out() { return m_out; }
	
	/**
	 * Opens the IOStream
	 * @return true if open was successful, false if not
	 */
	public abstract boolean open();
	/**
	 * Closes the IOStream
	 */
	public abstract void close();
	
	/**
	 * Creates an IOStream from an allowed config string:
	 * 
	 * /dev/ttyXYZ        (serial port like e.g. usb: /dev/ttyUSB0 or alias /dev/insteon)
	 * 
	 * /hub2/user:password@myinsteonhub.mydomain.com:25105,poll_time=1000 (insteon hub2 (2014))
	 * 
	 * /hub/user:password@myinsteonhub.mydomain.com:9761    (pre-2014 hub with raw tcp PLM access)
	 * 
	 * @param config
	 * @return reference to IOStream
	 */

	public static IOStream s_create(String config) {
		if (config.startsWith("/hub2/")) {
			config = config.substring(6); //Get rid of the /hub2/ part
			
			String user 	= null;
			String pass 	= null;
			String host 	= null;
			int port		= 25105;
			int pollTime	= 1000; // poll time in milliseconds
			
			String[] parts = config.split(","); // split off options at the end
			
			//Parse the first part, the address
			String[] adr = parts[0].split("@");
			String[] hostPort;
			if (adr.length > 1) {
				String[] userPass = adr[0].split(":");
				user = userPass[0];
				pass = userPass[1];
				hostPort = adr[1].split(":");
			} else {
				hostPort = parts[0].split(":");
			}
			
			host = hostPort[0];
			if (hostPort.length > 1) port = Integer.parseInt(hostPort[1]);
			
			// check if additional options are given
			if (parts.length > 1) {
				if (parts[1].trim().startsWith("poll_time")) {
					pollTime = Integer.parseInt(parts[1].split("=")[1].trim());
				}
			}
			return new HubIOStream(host, port, pollTime, user, pass);
		} else if (config.startsWith("/hub/")) {
			throw new IllegalArgumentException("old-style hub not implemented yet!");
		} else return new SerialIOStream(config);
	}
}
