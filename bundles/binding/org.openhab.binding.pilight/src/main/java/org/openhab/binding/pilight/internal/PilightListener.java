/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openhab.binding.pilight.internal.communication.Config;
import org.openhab.binding.pilight.internal.communication.Identification;
import org.openhab.binding.pilight.internal.communication.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens for updates from the pilight daemon. It is also responsible for requesting 
 * and propagating the current pilight configuration. 
 * 
 * The initial connection to the pilight daemon is made in {@link PilightConnection#connect(ObjectMapper, ObjectMapper)}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightListener extends Thread {
	
	private enum ConfigModifyAction {
		AddListener,
		ConfigReceived;
	}
	
	private static Logger logger = LoggerFactory.getLogger(PilightListener.class);
	
	private static Integer CONFIG_VALID_TIME = 10000; // 10 seconds
	
	private static Integer RECONNECT_DELAY = 10000; // 10 seconds

	private PilightConnection connection;
	
	private IPilightMessageReceivedCallback callback;
	
	private ObjectMapper inputMapper;
	
	private ObjectMapper outputMapper;
	
	private boolean running = true;
	
	private boolean updatingConfig = false;
	
	private Date lastConfigUpdate;
	
	private List<IPilightConfigReceivedCallback> configReceivedCallbacks = new ArrayList<IPilightConfigReceivedCallback>();
	
	public PilightListener(PilightConnection connection, IPilightMessageReceivedCallback callback) {
		this.connection = connection;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = connection.getSocket();
				if (!socket.isClosed()) {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line = in.readLine();
					while (running && line != null) {
						if (!StringUtils.isEmpty(line)) {
							if (line.startsWith("{\"config\":{")) {
								connection.setConfig(getInputMapper().readValue(line, Config.class));
								configAction(ConfigModifyAction.ConfigReceived, null);
							} else if (line.equals("1")) {
								// pilight stopping
								connection.getSocket().close();
								throw new IOException("Connection to pilight lost");
							} else {
								Status status = getInputMapper().readValue(line, Status.class);
								callback.messageReceived(connection, status);
							}
						}
						line = in.readLine();
					}
				}
			} catch (IOException e) {
				logger.error("Error in pilight listener thread", e);
			}
			
			// empty line received (socket closed) or pilight stopped, try to reconnect
			reconnect();
		}
				
		cleanup();
	}
	
	/**
	 * Tells the listener to refetch the configuration 
	 * 
	 * @param callback {@link IPilightConfigReceivedCallback#configReceived(PilightConnection)} is called when configuration was received
	 */
	public void refreshConfig(IPilightConfigReceivedCallback callback) {
		try {
			if (lastConfigUpdate == null || (new Date().getTime() - lastConfigUpdate.getTime()) > CONFIG_VALID_TIME) {
				configAction(ConfigModifyAction.AddListener, callback);
			} else {
				callback.configReceived(connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the listener
	 */
	public void close() {
		running = false;
	}

	/**
	 * Determine if this listener is still connected
	 * 
	 * @return true when connected 
	 */
	public boolean isConnected() {
		return !connection.getSocket().isClosed();
	}

	private void notifyConfigReceived() {
		logger.info("Config for pilight received");
		updatingConfig = false;
		lastConfigUpdate = new Date();
		for (IPilightConfigReceivedCallback callback : configReceivedCallbacks) {
			callback.configReceived(connection);
		}
	}
	
	private synchronized void configAction(ConfigModifyAction action, IPilightConfigReceivedCallback callback) throws JsonGenerationException, JsonMappingException, IOException {
		switch (action) {
			case AddListener:
				configReceivedCallbacks.add(callback);
				internalRefreshConfig();
				break;
			case ConfigReceived:
				notifyConfigReceived();
				configReceivedCallbacks.clear();
				break;
		}
	}

	private void internalRefreshConfig() throws JsonGenerationException, JsonMappingException, IOException {
		if (!updatingConfig) {
			updatingConfig  = true;
			logger.info("Updating pilight config");
			Socket socket = connection.getSocket();
			getOutputMapper().writeValue(socket.getOutputStream(), new Identification(Identification.REQUEST_CONFIG));
		} 
	}
	
	private ObjectMapper getInputMapper() {
		if (inputMapper == null)
			inputMapper = new ObjectMapper().configure(
					org.codehaus.jackson.JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
		
		return inputMapper;
	}

	private ObjectMapper getOutputMapper() {
		if (outputMapper == null) 
			outputMapper = new ObjectMapper().configure(
					Feature.AUTO_CLOSE_TARGET, false); 
		
		return outputMapper;
	}
	
	private void reconnect() {
		try {
			Thread.sleep(RECONNECT_DELAY);
		} catch (InterruptedException e) {
			// noop
		}
		logger.debug("pilight reconnecting");
		if (connection.connect(getInputMapper(), getOutputMapper())) {
			logger.info("Established connection to pilight server at {}:{}", connection.getHostname(), connection.getPort());
		}
	}

	private void cleanup() {
		try {
			connection.getSocket().close();
		} catch (IOException e) {
			logger.error("Error while closing pilight socket", e);
		}
		logger.info("Thread pilight listener stopped");
	}
}
