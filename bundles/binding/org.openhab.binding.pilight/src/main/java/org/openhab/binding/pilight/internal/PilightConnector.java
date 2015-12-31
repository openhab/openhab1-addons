/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.openhab.binding.pilight.internal.communication.Action;
import org.openhab.binding.pilight.internal.communication.Identification;
import org.openhab.binding.pilight.internal.communication.Message;
import org.openhab.binding.pilight.internal.communication.Options;
import org.openhab.binding.pilight.internal.communication.Response;
import org.openhab.binding.pilight.internal.communication.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens for updates from the pilight daemon. It is also responsible for requesting 
 * and propagating the current pilight configuration. 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightConnector extends Thread {
	
	private enum ConfigModifyAction {
		AddListener,
		ConfigReceived;
	}
	
	private static Logger logger = LoggerFactory.getLogger(PilightConnector.class);
	
	private static Integer CONFIG_VALID_TIME = 10000; // 10 seconds
	
	private static Integer RECONNECT_DELAY = 10000; // 10 seconds

	private PilightConnection connection;
	
	private IPilightMessageReceivedCallback callback;
	
	private ObjectMapper inputMapper = new ObjectMapper()
				.configure(org.codehaus.jackson.JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
	
	private ObjectMapper outputMapper = new ObjectMapper()
				.configure(Feature.AUTO_CLOSE_TARGET, false)
				.setSerializationInclusion(Inclusion.NON_NULL);

	private boolean running = true;
	
	private boolean updatingConfig = false;
	
	private Date lastConfigUpdate;
	
	private List<IPilightConfigReceivedCallback> configReceivedCallbacks = new ArrayList<IPilightConfigReceivedCallback>();
	
	private ExecutorService delayedUpdateThreadPool = Executors.newSingleThreadExecutor();
	
	public PilightConnector(PilightConnection connection, IPilightMessageReceivedCallback callback) {
		this.connection = connection;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		reconnect();

		while (running) {
			try {
				Socket socket = connection.getSocket();
				if (!socket.isClosed()) {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line = in.readLine();
					while (running && line != null) {
						if (!StringUtils.isEmpty(line)) {
							logger.debug("Received from pilight: {}", line);
							if (line.startsWith("{\"message\":\"config\"")) {
								// Configuration received
								connection.setConfig(inputMapper.readValue(line, Message.class).getConfig());
								configAction(ConfigModifyAction.ConfigReceived, null);
							} else if (line.startsWith("{\"status\":")) {
								// Status message, we're not using this for now. 
								Response response = inputMapper.readValue(line, Response.class);
								logger.trace("Response success: " + response.isSuccess());
							} else if (line.equals("1")) {
								// pilight stopping
								throw new IOException("Connection to pilight lost");
							} else {
								logger.debug(line);
								Status status = inputMapper.readValue(line, Status.class);
								callback.messageReceived(connection, status);
							}
						}
						line = in.readLine();
					}
				}
			} catch (IOException e) {
				logger.debug("Error in pilight listener thread", e);
			}
			
			logger.info("Disconnected from pilight server at {}:{}", connection.getHostname(), connection.getPort());

			if (running) {
				// empty line received (socket closed) or pilight stopped but binding 
				// is still running, try to reconnect
				reconnect();
			}
		}
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
		disconnect();
	}

	/**
	 * Determine if this listener is still connected
	 * 
	 * @return true when connected 
	 */
	public boolean isConnected() {
		return connection.getSocket() != null && !connection.getSocket().isClosed();
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
			outputMapper.writeValue(socket.getOutputStream(), new Action(Action.ACTION_REQUEST_CONFIG));
		} 
	}
	
	private void disconnect() {
		if (connection.getSocket() != null) {
			try {
				connection.getSocket().close();
			} catch (IOException e) {
				logger.debug("Error while closing pilight socket", e);
			}
		}
	}	

	private void reconnect() {
		disconnect();
		
		int delay = 0;
		
		while (!isConnected()) {
			try {
				logger.debug("pilight reconnecting");

				Thread.sleep(delay);
				Socket socket = new Socket(connection.getHostname(), connection.getPort());
				
				Identification identification = new Identification();
				Options options = new Options();
				options.setConfig(true);
				identification.setOptions(options);

				// For some reason, directly using the outputMapper to write to the socket's OutputStream doesn't work.
				PrintStream printStream = new PrintStream(socket.getOutputStream(), true);
				printStream.println(outputMapper.writeValueAsString(identification));
				
				Response response = inputMapper.readValue(socket.getInputStream(), Response.class);
				
				if (response.getStatus().equals(Response.SUCCESS)) {
					logger.info("Established connection to pilight server at {}:{}", connection.getHostname(), connection.getPort());
					connection.setSocket(socket);
				} else {
					logger.debug("pilight client not accepted: {}", response.getStatus());
				}
				
			} catch (IOException e) {
				logger.debug(e.getMessage(), e);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage(), e);
			}
			
			delay = RECONNECT_DELAY;
		}
	}
	
	public void doUpdate(Action action) {
		if (isConnected()) {	
			if (connection.getDelay() != null) {
				delayedUpdateCall(action);
			} else {
				doUpdateCall(action);
			}
		} else {
			logger.debug("Cannot send command, not connected to pilight");
		}
	}

	private void delayedUpdateCall(Action action)
	{
		DelayedUpdate delayed = new DelayedUpdate(action, connection);
		delayedUpdateThreadPool.execute(delayed);
	}

	private void doUpdateCall(Action action) {
		try {
			connection.setLastUpdate(new Date());
			outputMapper.writeValue(connection.getSocket().getOutputStream(), action);
		} catch (IOException e) {
			logger.debug("Error while sending update to pilight server", e);
		}	
	}
	
	/**
	 * Simple thread to allow calls to pilight to be throttled  
	 * 
	 * @author Jeroen Idserda
	 * @since 1.0
	 */
	private class DelayedUpdate implements Runnable {
		
		private Action action;
		
		private PilightConnection connection;
		
		public DelayedUpdate(Action action, PilightConnection connection) {
			this.action = action; 
			this.connection = connection;
		}
		
		@Override
		public void run() {
			long delayBetweenUpdates = connection.getDelay();

			if (connection.getLastUpdate() != null) {
				long diff = new Date().getTime() - connection.getLastUpdate().getTime();
				if (diff < delayBetweenUpdates) {
					long delay = delayBetweenUpdates-diff;
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						logger.debug("Error while processing pilight throttling delay");
					}
				}
			}
			
			doUpdateCall(action);
		}
	}
}
