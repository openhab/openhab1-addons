/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.openhab.binding.insteonplm.internal.driver.hub.HubIOStream;
import org.openhab.binding.insteonplm.internal.driver.hub.OldHubIOStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for implementation for I/O stream with anything that looks
 * like a PLM (e.g. the insteon hubs, serial/usb connection etc)
 * 
 * @author Bernd Pfrommer
 * @author Daniel Pfrommer
 * @since 1.7.0
 */

public abstract class IOStream {
	private static final Logger logger = LoggerFactory.getLogger(IOStream.class);
	protected InputStream	m_in	= null;
	protected OutputStream	m_out	= null;

	/**
	 * read data from iostream
	 * @param b byte array (output)
	 * @param offset offset for placement into byte array
	 * @param readSize size to read
	 * @return number of bytes read
	 */
	public int read(byte [] b, int offset, int readSize) throws InterruptedException {
		int len = 0;
		while (len < 1) {
			try {
				len = m_in.read(b, offset, readSize);
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			} catch (IOException e) {
				logger.trace("got exception while reading: {}", e.getMessage());
				while (!reconnect()) {
					logger.trace("sleeping before reconnecting");
					Thread.sleep(10000); 
				}
			}
		}
		return (len);
	}

	/**
	 * Write data to iostream
	 * @param b byte array to write
	 */
	public void write(byte [] b) {
		try {
			m_out.write(b);
		} catch (IOException e) {
			logger.trace("got exception while writing: {}", e.getMessage());
			while (!reconnect()) {
				try {
					logger.trace("sleeping before reconnecting");
					Thread.sleep(10000);
				} catch (InterruptedException ie) {
					logger.warn("interrupted while sleeping on write reconnect");
				}
			}
		}
	}
	
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
	 * reconnects the stream
	 * @return true if reconnect succeeded
	 */
	private synchronized boolean reconnect() {
		close();
		return (open());
	}
	
	/**
	 * Creates an IOStream from an allowed config string:
	 * 
	 * /dev/ttyXYZ        (serial port like e.g. usb: /dev/ttyUSB0 or alias /dev/insteon)
	 * 
	 * /hub2/user:password@myinsteonhub.mydomain.com:25105,poll_time=1000 (insteon hub2 (2014))
	 * 
	 * /hub/myinsteonhub.mydomain.com:9761
	 * 
	 * @param config
	 * @return reference to IOStream
	 */

	public static IOStream s_create(String config) {
		if (config.startsWith("/hub2/")) {
			return makeHub2014Stream(config);
		} else if (config.startsWith("/hub/")) {
			return makeOldHubStream(config);
		} else return new SerialIOStream(config);
	}
	
	private static HubIOStream makeHub2014Stream(String config) {
		config = config.substring(6); //Get rid of the /hub2/ part
		String user 	= null;
		String pass 	= null;
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
		HostPort hp = new HostPort(hostPort, 25105);
		// check if additional options are given
		if (parts.length > 1) {
			if (parts[1].trim().startsWith("poll_time")) {
				pollTime = Integer.parseInt(parts[1].split("=")[1].trim());
			}
		}
		return new HubIOStream(hp.host, hp.port, pollTime, user, pass);
	}

	private static OldHubIOStream makeOldHubStream(String config) {
		config 	= config.substring(5);		//Get rid of the /hub/ part
		String[] parts = config.split(","); // split off options at the end, if any
		String[] hostPort = parts[0].split(":");
		HostPort hp = new HostPort(hostPort, 9761);
		return new OldHubIOStream(hp.host, hp.port);
	}

	private static class HostPort {
		public	String	host = "localhost";
		public	int		port = -1;
		HostPort(String[] hostPort, int defaultPort) {
			port = defaultPort;
			host = hostPort[0];
			try {
				if (hostPort.length > 1) port = Integer.parseInt(hostPort[1]);
			} catch (NumberFormatException e) {
				logger.error("bad format for port {} ", hostPort[1], e);
			}
		}
	}
}
