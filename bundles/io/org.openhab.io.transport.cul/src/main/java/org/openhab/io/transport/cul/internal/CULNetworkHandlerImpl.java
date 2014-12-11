/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation for culfw based devices which communicate via network port
 * (CUN for example).
 * 
 * @author Markus Heberling
 * @since 1.5.0
 */
public class CULNetworkHandlerImpl extends AbstractCULHandler {

	private static final int CUN_DEFAULT_PORT = 2323;
	/**
	 * Thread which receives all data from the CUL.
	 * 
	 * @author Markus Heberling
	 * @since 1.5.0
	 * 
	 */
	private class ReceiveThread extends Thread {

		private final Logger logger = LoggerFactory.getLogger(ReceiveThread.class);

		/**
		 * Mark this thread
		 */
		ReceiveThread(){
			super("CUL ReceiveThread");
		}
		
		@Override
		public void run() {

			try {
				while (!isInterrupted()) {
					processNextLine();

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						logger.debug("Error while sleeping in ReceiveThread", e);
					}
				}
				logger.debug("ReceiveThread exiting.");
			} catch (CULCommunicationException e) {
				log.error("Connection to CUL broken, terminating receive thread for " + deviceName);
			}
		}

	}
	
	final static Logger log = LoggerFactory.getLogger(CULNetworkHandlerImpl.class);

	private ReceiveThread receiveThread;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	
	public CULNetworkHandlerImpl(String deviceName, CULMode mode) {
		super(deviceName, mode);
	}

	/**
	 * Constructor including property map for specific configuration. Just for compatibility with CulSerialHandlerImpl
	 * @param deviceName
	 * 			String representing the device.
	 * @param mode
	 * 			The RF mode for which the device will be configured.
	 * @param properties
	 * 			Properties are ignored
	 */
	public CULNetworkHandlerImpl(String deviceName, CULMode mode, Map<String, ?> properties){
		super(deviceName, mode);		
	}
	
	@Override
	protected void openHardware() throws CULDeviceException {
		log.debug("Opening network CUL connection for " + deviceName);
		try {
			 URI uri = new URI("cul://" + deviceName);
			  String host = uri.getHost();
			  int port = uri.getPort()==-1?CUN_DEFAULT_PORT:uri.getPort();

			  if (uri.getHost() == null || uri.getPort() == -1) {
					throw new CULDeviceException("Could not parse host:port from "+deviceName);
			  }
			log.debug("Opening network CUL connection to " + host+":"+port);
			socket=new Socket(host, port);
			log.info("Connected network CUL connection to " + host+":"+port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));

			log.debug("Starting network listener Thread");
			receiveThread=new ReceiveThread();
			receiveThread.start();
		} catch (IOException e) {
			throw new CULDeviceException(e);
		} catch (URISyntaxException e) {
			throw new CULDeviceException("Could not parse host:port from "+deviceName, e);
		}

	}

	@Override
	protected void closeHardware() {
		receiveThread.interrupt();
		
		log.debug("Closing network device " + deviceName);
		
		try {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
		} catch (IOException e) {
			log.error("Can't close the input and output streams propberly", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.error("Can't close the socket propberly", e);
				}
			}
		}
	}	
}
