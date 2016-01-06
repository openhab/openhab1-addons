/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage telnet connection to the Denon Receiver 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonListener extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(DenonListener.class);
	
	private static final Integer RECONNECT_DELAY = 10000; // 10 seconds
	
	private DenonConnectionProperties connection;
	
	private DenonUpdateReceivedCallback callback;
	
	private TelnetClient tc; 
	
	boolean running = true;
	
	public DenonListener(DenonConnectionProperties connection, DenonUpdateReceivedCallback callback) {
		logger.debug("Denon listener created");
		this.tc = new TelnetClient();
		this.connection = connection;
		this.callback = callback;
	}
	
	@Override
	public void run() {
		while (running) {
			 if (!tc.isConnected()) {
				 connectTelnetClient();
			 }
             
			 boolean end_loop = false;
			 BufferedReader reader = new BufferedReader(new InputStreamReader(tc.getInputStream()));

             do {
            	 try {
            		 String line = reader.readLine();
            		 
					 if (!StringUtils.isBlank(line)) {
						 logger.trace("Received from {}: {}", connection.getHost(), line);
						 callback.updateReceived(line);
					 }
            	 } catch (IOException e) {
            		logger.warn("Error in telnet connection", e);
            		end_loop = true;
				}
             } 
             while(running && end_loop == false);
		}
	}
	
	private void connectTelnetClient() {
		int delay = 0;
		while (!tc.isConnected()) {
			try {
				Thread.sleep(delay);
				logger.debug("Connecting to {}", connection.getHost());
				tc.connect(connection.getHost(), connection.getTelnetPort());
			}  catch (SocketException e) {
				logger.error("Error connecting to {}", connection.getHost(), e);
			} catch (IOException e) {
				logger.error("Error connecting to {}", connection.getHost(), e);
			} catch (InterruptedException e) {
				logger.error("Error connecting to {}", connection.getHost(), e);
			}
			delay = RECONNECT_DELAY;
		}

		logger.debug("Denon telnet client connected to {}", connection.getHost());
	}

	public void shutdown(){
		this.running = false;
		
		try {
			this.tc.disconnect();
		} catch (IOException e) {
			logger.debug("Error while disconnecting telnet client", e);
		}
	}
}
