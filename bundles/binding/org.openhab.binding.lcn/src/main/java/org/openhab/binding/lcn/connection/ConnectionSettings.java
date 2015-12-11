/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.connection;

import java.util.Dictionary;

import org.openhab.binding.lcn.common.LcnDefs;

/**
 * Settings for a connection to LCN-PCHK.
 * 
 * @author Tobias Jüttner
 */
public class ConnectionSettings {
	
	/** Unique identifier for this connection. */
	private final String id;
	
	/** The user name for authentication. */
	private final String username;
	
	/** The password for authentication. */
	private final String password;
	
	/** The TCP/IP address or IP of the connection. */
	private final String address;
	
	/** The TCP/IP port of the connection. */
	private final int port;
	
	/** The dimming mode to use. */
	private final LcnDefs.OutputPortDimMode dimMode;
	
	/** The status-messages mode to use. */
	private final LcnDefs.OutputPortStatusMode statusMode;
	
	/** The default timeout to use for requests. Worst case: Requesting threshold 4-4 takes at least 1.8s */
	private static final long DEFAULT_TIMEOUT_MSEC = 3500;
	
	/** Timeout for requests. */
	private final long timeoutMSec;
	
	/**
	 * Constructor.
	 * 
	 * @param id the connnection's unique identifier
	 * @param address the connection's TCP/IP address or IP
	 * @param port the connection's TCP/IP port
	 * @param username the user name for authentication
	 * @param password the password for authentication 
	 * @param dimMode the dimming mode
	 * @param statusMode the status-messages mode
	 * @param timeout the request timeout
	 */
	private ConnectionSettings(String id, String address, int port, String username, String password,
		LcnDefs.OutputPortDimMode dimMode, LcnDefs.OutputPortStatusMode statusMode, int timeout) {
		this.id = id;
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.dimMode = dimMode;
		this.statusMode = statusMode;
		this.timeoutMSec = timeout;
	}

	/**
	 * Gets the unique identifier for the connection.
	 * 
	 * @return the unique identifier
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Gets the user name used for authentication.
	 * 
	 * @return the user name
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Gets the password used for authentication.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Gets the TCP/IP address or IP of the connection.
	 *  
	 * @return the address or IP
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * Gets the TCP/IP port of the connection.
	 * 
	 * @return the port
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Gets the dimming mode to use for the connection.
	 * 
	 * @return the dimming mode
	 */
	public LcnDefs.OutputPortDimMode getDimMode() {
		return this.dimMode;
	}
	
	/**
	 * Gets the status-messages mode to use for the connection.
	 * 
	 * @return the status-messages mode
	 */
	public LcnDefs.OutputPortStatusMode getStatusMode() {
		return this.statusMode;
	}
	
	/**
	 * Gets the request timeout.
	 * 
	 * @return the timeout in milliseconds
	 */
	public long getTimeout() {
		return this.timeoutMSec;
	}
	
	/**
	 * Tries to parse LCN-PCHK connection settings from the given openHAB configuration.
	 * 
	 * @param config the binding's configuration
	 * @param counter 0..x (1..x+1 in actual configuration file)
	 * @return the connection settings on success or null
	 */
	public static ConnectionSettings tryParse(Dictionary<String, ?> config, int counter) {
		String id = (String)config.get("id" + (counter + 1));
		id = id == null ? "" : id.trim();
		String addressWithOptPort = (String)config.get("address" + (counter + 1));
		addressWithOptPort = addressWithOptPort == null ? "" : addressWithOptPort.trim();
		String username = (String)config.get("username" + (counter + 1));
		username = username == null ? "" : username.trim();
		String password = (String)config.get("password" + (counter + 1));
		password = password == null ? "" : password.trim();
		String mode = (String)config.get("mode" + (counter + 1));
		mode = mode == null ? "" : mode.trim();
		if (id.isEmpty() || addressWithOptPort.isEmpty() || username.isEmpty() || password.isEmpty() || mode.isEmpty()) {
			return null;
		}
		String dataRequestTimeout = (String)config.get("timeout" + (counter + 1));
		dataRequestTimeout = dataRequestTimeout == null ? Long.valueOf(DEFAULT_TIMEOUT_MSEC).toString() : dataRequestTimeout.trim();
		try {
			String address = addressWithOptPort.contains(":") ? addressWithOptPort.split(":")[0] : addressWithOptPort;
			int port = addressWithOptPort.contains(":") ? Integer.parseInt(addressWithOptPort.split(":")[1]) : 4114;
			LcnDefs.OutputPortDimMode dimMode =
				mode.equalsIgnoreCase("percent200") || mode.equalsIgnoreCase("native200") ?
				LcnDefs.OutputPortDimMode.STEPS200 : LcnDefs.OutputPortDimMode.STEPS50;
			LcnDefs.OutputPortStatusMode statusMode =
				mode.equalsIgnoreCase("percent50") || mode.equalsIgnoreCase("percent200") ?
				LcnDefs.OutputPortStatusMode.PERCENT : LcnDefs.OutputPortStatusMode.NATIVE;
			int timeout = Integer.parseInt(dataRequestTimeout);
			return new ConnectionSettings(id, address, port, username, password, dimMode, statusMode, timeout);
		} catch (NumberFormatException ex) { }
		return null;		
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ConnectionSettings)) {
			return false;
		}
		ConnectionSettings other = (ConnectionSettings)o;
		return this.id.equals(other.id) && this.address.equals(other.address) && this.port == other.port &&
			this.username.equals(other.username) && this.password.equals(other.password) &&
			this.dimMode == other.dimMode && this.statusMode == other.statusMode &&
			this.timeoutMSec == other.timeoutMSec;
	}
	
}
