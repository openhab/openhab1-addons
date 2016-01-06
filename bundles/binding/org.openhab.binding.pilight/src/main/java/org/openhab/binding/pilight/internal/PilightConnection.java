/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.net.Socket;
import java.util.Date;

import org.openhab.binding.pilight.internal.communication.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A connection to a single pilight instance. Also responsible for connecting to this instance. 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightConnection {
	
	@SuppressWarnings("unused")
	private static final Logger logger = 
			LoggerFactory.getLogger(PilightConnection.class);
	
	/* Configuration properties */
	private String instance;
	
	private String hostname;
	
	private int port;
		
	private Long delay;
	
	/* Runtime properties */
	private PilightConnector connector;
	
	private Socket socket;

	private Date lastUpdate; 
	
	private Config config;
	
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public PilightConnector getConnector() {
		return connector;
	}

	public void setConnector(PilightConnector connector) {
		this.connector = connector;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	public boolean isConnected() {
		return getConnector() != null && getConnector().isConnected();
	}
	
}
