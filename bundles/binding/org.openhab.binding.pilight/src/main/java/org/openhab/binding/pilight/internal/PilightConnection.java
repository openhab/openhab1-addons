/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.pilight.internal.communication.Config;
import org.openhab.binding.pilight.internal.communication.Identification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A connection to a single pilight instance. Also responsible for connecting to this instance. 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightConnection {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(PilightConnection.class);
	
	/* Configuration properties */
	private String instance;
	
	private String hostname;
	
	private int port;
		
	private Long delay;
	
	/* Runtime properties */
	private PilightListener listener;
	
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
	
	public PilightListener getListener() {
		return listener;
	}

	public void setListener(PilightListener listener) {
		this.listener = listener;
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
		return getListener() != null && getListener().isConnected();
	}
	
	/**
	 * Try to connect to the pilight instance represented by this object  
	 * 
	 * @param inputMapper The JSON inputmapper to use
	 * @param outputMapper The JSON outputmapper to use
	 * @return true when successfully connected 
	 */
	@SuppressWarnings("resource")
	public boolean connect(ObjectMapper inputMapper, ObjectMapper outputMapper) {
		try {
			Socket socket = new Socket(getHostname(), getPort());
			Identification id = new Identification(Identification.CLIENT_GUI);
			outputMapper.writeValue(socket.getOutputStream(), id);
			
			Identification response = inputMapper.readValue(socket.getInputStream(), Identification.class);
			
			if (response.getMessage().equals(Identification.ACCEPTED)) 
				setSocket(socket);
			else {
				logger.error("pilight client not accepted: {}", response.getMessage());
				return false;
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		
		return true;
	}
}
