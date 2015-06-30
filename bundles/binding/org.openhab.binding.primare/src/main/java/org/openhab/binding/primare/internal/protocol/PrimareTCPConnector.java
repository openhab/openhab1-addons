/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.net.UnknownHostException;

import org.openhab.binding.primare.internal.PrimareEventListener;
import org.openhab.binding.primare.internal.PrimareStatusUpdateEvent;
import org.openhab.binding.primare.internal.protocol.PrimareMessage;
import org.openhab.binding.primare.internal.protocol.PrimareMessageFactory;
import org.openhab.binding.primare.internal.protocol.PrimareResponseFactory;
import org.openhab.binding.primare.internal.protocol.PrimareUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for Primare TCP communication. This connector has only
 * been tested with a linux box running socat between a socket and a 
 * serial interface, not a Primare device with a built-in Ethernet
 * interface.
 * 
 * @author juslive
 * @since 1.7.0
 */
public class PrimareTCPConnector extends PrimareConnector {

	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareTCPConnector.class);
	
	/** Connection timeout in milliseconds **/
	private static final int CONNECTION_TIMEOUT = 5000;

	/** Connection test interval in milliseconds **/
	private static final int CONNECTION_TEST_INTERVAL = 15000;

	/** Socket timeout in milliseconds **/
	private static final int SOCKET_TIMEOUT = CONNECTION_TEST_INTERVAL + 10000;
	
	// Locks 
	private final Object oLock = new Object();
	private final Object iLock = new Object();

	private String host;
	private int port;

	private Socket socket = null;
	private DataListener dataListener = null;

	private DataInputStream inStream = null;
	private OutputStream outStream;

	private ConnectionSupervisor connectionSupervisor = null;

	public PrimareTCPConnector(String deviceId, String host, int port, PrimareMessageFactory mf, PrimareResponseFactory rf) {
		this.deviceId = deviceId;
		this.host = host;
		this.port = port;
		this.messageFactory = mf;
		this.responseFactory = rf;
	}


	public static <T extends PrimareTCPConnector> T newForModel(String m, String deviceId, String host, int port) {
		
		T pc = null;
		
		if (m == null) {
			logger.error("connectorForModel called with null argument");
			return null;
		}	
		
		if ("SP31.7".equals(m) || 
		    "SP31".equals(m) || 
		    "SPA20".equals(m) || 
		    "SPA21".equals(m)) {
			pc = (T) new org.openhab.binding.primare.internal.protocol.spa20.PrimareSPA20TCPConnector(deviceId, host, port);
		} else {
			logger.error("Could not find PrimareTCPConnector for Primare model {m}");
		}
		
		return pc;
	}


	public String toString() {
		return String.format("[%s at %s:%s (%s%s)]",
				     deviceId, host, port,
				     socket!=null && socket.isConnected() ? "connected" : "not connected",
				     connectionBroken ? ",broken" : "");
	}

	public boolean isConnected() {
		return (socket != null && socket.isConnected());
	}


	private boolean needRestart() {
		return (connectionBroken || (socket != null && !socket.isConnected()));
	}


	@Override
	public void connect() {
		
		logger.debug("Connecting to {}", this.toString());
		
		try {
			connectSocket();
		} catch(Exception unimportant) {
		}
		
		// Start connection supervisor regardless of initial connection status
		if (connectionSupervisor == null) {
			logger.trace("Starting connection supervisor for {}", this.toString());
			connectionSupervisor = new ConnectionSupervisor(CONNECTION_TEST_INTERVAL);
			logger.trace("Started connection supervisor for {}", this.toString());
		}
	}
	
	@Override
	public void disconnect() {
		disconnectSocket();
	}

	@Override
	public void sendBytes(byte[] msg) throws IOException {

		logger.trace("Sending (hex) [{}] to {} via TCP",
			     PrimareUtils.byteArrayToHex(msg),
			     this.toString());
		
		try {
			synchronized (oLock) {
				outStream.write(msg);
				outStream.flush();
			}

			bytesSentAt = new Date();
			logger.trace("Sent and flushed (hex) [{}] to {} via TCP",
				     PrimareUtils.byteArrayToHex(msg),
				     this.toString());
			
		} catch (SocketException e) {
			
			connectionBroken = true;
			throw e;
			
		} catch (IOException e) {
			
			connectionBroken = true;			
			throw e;
		}
		
	}




	private void connectSocket() throws UnknownHostException, IOException {
		
		//
		// Do not connect if we have a valid connection
		//
		if (socket != null && (connectionBroken || !socket.isConnected())) {
			disconnect(); // cleanup
		}
		
		if (socket == null) {
			
			try {		
				// Creating a socket to connect to the server
				socket = new Socket();
				socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
				
				logger.debug("Socket connected to {}", PrimareTCPConnector.this.toString());
				
				inStream = new DataInputStream(socket.getInputStream());
				outStream = socket.getOutputStream();
				
				socket.setSoTimeout(SOCKET_TIMEOUT);
				outStream.flush();
				
				// Start DataListener before sending init message in case there is a response
				logger.trace("connect - starting DataListener for {}", PrimareTCPConnector.this.toString());
				dataListener = new DataListener();
				dataListener.start();
				logger.trace("connect - started DataListener update listener for {}", this.toString());

				final PrimareMessage[] deviceInitMessages = messageFactory.getInitMessages();
				
				if (deviceInitMessages != null) {
					try {
						logger.trace("Sending init messages to {}",this.toString());
						/* logger.trace("Sending init messages (hex) [{}] to {}",
						   PrimareUtils.byteArrayToHex(deviceInitMessage.escaped()),
						   this.toString()); */
						sendMessage(deviceInitMessages);
					} catch (Exception e) {
						/* logger.warn("Failed to send init message {} to {} at {}:{}", 
						   PrimareUtils.byteArrayToHex(deviceInitMessage.escaped()),this.toString()); */
						logger.warn("Failed to send init messages to {}", this.toString());
						return;
					}
				} else {
					logger.trace("No init message found for {}", this.toString());

				}
				
				
			} catch (UnknownHostException unknownHost) {
				logger.error("connect - unknown host for {} at", this.toString());
				throw unknownHost;
			} catch (IOException ioException) {
				logger.error("Can not connect to socket for {} : {}",
					     this.toString(), ioException.getMessage());
				throw ioException;
			}
			
		}
	}


	private void disconnectSocket() {
		
		if (socket != null) {
			try {
				socket.close();
			} catch (Exception unimportant) {
			}
			socket = null;
			connectionBroken = false;
		}
	}
	

	
	private class DataListener extends Thread {
		
		private boolean interrupted = false;

		DataListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
			this.interrupt();
		}

		@Override
		public void run() {
			
			logger.debug("DataListener for {} started", PrimareTCPConnector.this.toString());
			
			// as long as we are connected and no interrupt is requested, continue running
			while (!connectionBroken && socket!=null && socket.isConnected() && !interrupted) {
				try {
					waitStateMessages();
				} catch (SocketTimeoutException e) {
					
					logger.debug("No data received from {} during supervision interval ({} sec)!",
						     PrimareTCPConnector.this.toString(), SOCKET_TIMEOUT);
					
				} catch (Exception e) {
					
					if (interrupted != true && this.isInterrupted() != true) {
						logger.error("Error for {} during message waiting: {}",
							     PrimareTCPConnector.this.toString(), e);
						
						// Unspecified problem, let's mark this connection broken
						connectionBroken = true;
						break;
					}
				}
			}
			
			logger.debug("DataListener for {} stopped", PrimareTCPConnector.this.toString());
		}

		
		
		/**
		 * Read bytes from inStream
		 * 
		 * @throws IOException 
		 * @throws InterruptedException 
		 **/
		private void waitStateMessages() throws NumberFormatException,
							IOException, InterruptedException {
			
			PrimareStatusUpdateEvent event = new PrimareStatusUpdateEvent(this);
			
			logger.debug("Entered waitStateMessages loop for {}",
				     PrimareTCPConnector.this.toString());
			
			while (true) {
				logger.trace("waitStateMessages - waiting for data");
				byte b = inStream.readByte();
				bytesReceivedAt = new Date();
				
				// logger.trace("waitStateMessages - data from {} READ: [{}] ({})",
				//	     PrimareTCPConnector.this.toString(), String.format("0x%02x",b), b);
				
				buffer[total++] = b;
				// Access byte[] buffer and consume bytes 0 .. total-1 if DLE ETX has been seen 
				parseData(total-1);
			}
			
		}
		
	}
	

	private class ConnectionSupervisor {
		private Timer timer;
		
		public ConnectionSupervisor(int milliseconds) {
			logger.debug("Connection supervisor started, interval {} milliseconds", milliseconds);
			
			timer = new Timer();
			timer.schedule(new ConnectionSupervisorTask(), milliseconds, milliseconds);
		}
		
		public void activate(int milliseconds) {
			logger.debug("Connection supervisor (re)activated, interval {} milliseconds", milliseconds);
			timer.schedule(new ConnectionSupervisorTask(), milliseconds, milliseconds);
		}
		
		public void deactivate() {
			logger.debug("Connection supervisor deactivated");
			timer.cancel();
		}
		
		class ConnectionSupervisorTask extends TimerTask {
			public void run() {
				logger.debug("Scheduled connection supervisor task started for {}", 
					     PrimareTCPConnector.this.toString());
				
				if (needRestart()) {
					disconnectSocket(); // cleanup
				}
				
				if (socket == null) {
					logger.debug("No connection, connecting to {}",
						     PrimareTCPConnector.this.toString());
					try {
						connectSocket();
					} catch(Exception unimportant) {
						logger.debug("Still no connection after retry, failed to connect to {}",
							     PrimareTCPConnector.this.toString());
					}
					
				} else {
					logger.debug("Connection to {} exists, last message sent:{} received:{}",
						     PrimareTCPConnector.this.toString(), messageSentAt, messageReceivedAt);
					try {
						sendPingMessages();
					} catch(Exception unimportant) {
						logger.trace("Connection to {} send ping message failed",
							     PrimareTCPConnector.this.toString());
						// Variable connectionBroken has already been set by sendBytes,
						// no need to set it here
					}
				}
			}
		}
	}

}
