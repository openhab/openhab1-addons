/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal.ipcontrolprotocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.pioneeravr.internal.PioneerAvrEventListener;
import org.openhab.binding.pioneeravr.internal.PioneerAvrStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * A class that wraps the communication to Pioneer devices using the
 * Pioneer IpControl protocol
 * 
 * see {@link http://www.pioneerelectronics.com/StaticFiles/PUSA/Files/Home%20Custom%20Install/VSX-1120-K-RS232.PDF} for
 * the protocol specs
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 */
public class IpControl {
	
	private static final Logger logger = LoggerFactory.getLogger(IpControl.class);
	
	/** Instantiated class IP for the receiver to communicate with. **/
	private String receiverIP = "";

	/** default port for IP communication (23=telnet). **/
	public static final int DEFAULT_IPCONTROL_PORT = 23;
	
	/** Connection timeout in milliseconds **/
	private static final int CONNECTION_TIMEOUT = 5000;

	/** Connection test interval in milliseconds **/
	private static final int CONNECTION_TEST_INTERVAL = 60000;
	
	/** Socket timeout in milliseconds **/
	private static final int SOCKET_TIMEOUT = CONNECTION_TEST_INTERVAL + 10000;
	
	/** Instantiated class Port for the receiver to communicate with. **/
	private int receiverPort = DEFAULT_IPCONTROL_PORT;	
	
	private Boolean connectionCheckActive = true;
	
	private static Socket ipControlSocket = null;
	private DataListener dataListener = null;
	private static DataOutputStream outStream = null;
	private static DataInputStream inStream = null;
	private static BufferedReader inBufferedReader = null;
	
	private static boolean connected = false;
	private static List<PioneerAvrEventListener> _listeners = new ArrayList<PioneerAvrEventListener>();
	private static int retryCount = 1;
	private static ConnectionSupervisor connectionSupervisor = null;
	
	/**
	 * Constructor that takes your receivers IP and port.
	 **/
	public IpControl(String ip, int ipControlPort, Boolean doConnectionCheck) {
		if (StringUtils.isNotBlank(ip)) {
			receiverIP = ip;
		}
		if (ipControlPort >= 1) {
			receiverPort = ipControlPort;
		}
		connectionCheckActive = doConnectionCheck;
	}

	/**
	 * Add event listener, which will be invoked when status upadte is received from receiver.
	 **/
	public synchronized void addEventListener(PioneerAvrEventListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Remove event listener.
	 **/
	public synchronized void removeEventListener(PioneerAvrEventListener listener) {
		_listeners.remove(listener);
	}
	
	/**
	 * Get retry count value.
	 **/
	public static int getRetryCount() {
		return retryCount;
	}

	/**
	 * Set retry count value. How many times command is retried when error occurs.
	 **/
	public static void setRetryCount(int retryCount) {
		IpControl.retryCount = retryCount;
	}

	/**
	 * Connects to the receiver by opening a socket connection through the
	 * IP and port defined on constructor.
	 **/
	public boolean connectSocket() {
		return connectSocket(receiverIP, receiverPort, connectionCheckActive);
	}
	
	/**
	 * Connects to the receiver by opening a socket connection through the
	 * IP and port.
	 **/
	public boolean connectSocket(String ip, int port, Boolean doConnectionCheck) {
		
		if (ipControlSocket == null || !connected || !ipControlSocket.isConnected()) {
			try {
				// Creating a socket to connect to the server
				ipControlSocket = new Socket();
				ipControlSocket.connect(new InetSocketAddress(ip, port), CONNECTION_TIMEOUT);
				
				logger.debug("Connected to {} on port {}", ip, port);
				
				// Get Input and Output streams
				outStream = new DataOutputStream(ipControlSocket.getOutputStream());
				inStream = new DataInputStream(ipControlSocket.getInputStream());
				
				inBufferedReader = new BufferedReader(new InputStreamReader(inStream));
				
				ipControlSocket.setSoTimeout(SOCKET_TIMEOUT);
				outStream.flush();
				connected = true;
				
				receiverIP = ip;
				receiverPort = port;
				
				// start status update listener
				if (dataListener == null) {
					dataListener = new DataListener();
					dataListener.start();
				}
				
				// start connection tester when enabled
				if ( doConnectionCheck == true ) {
					logger.debug("conn check enabled, starting hypervisor");
					if (connectionSupervisor == null) {
						connectionSupervisor = new ConnectionSupervisor(CONNECTION_TEST_INTERVAL);
					}
				}
				else {
					logger.debug("conn check disabled, not starting hypervisor");
				}
					 
				
				
			} catch (UnknownHostException unknownHost) {
				logger.error("You are trying to connect to an unknown host!", unknownHost);
			} catch (IOException ioException) {
				logger.error("Can't connect "+ip+":"+port+": " + ioException.getMessage());
			}
		}
		
		return connected;
	}

	/**
	 * Closes the socket connection.
	 * 
	 * @return true if the closed successfully
	 **/
	public boolean closeSocket() {
		try {
			if (dataListener != null) {
				dataListener.setInterrupted(true);
				dataListener = null;
				logger.debug("closed data listener!");
			}
			if (connectionSupervisor != null) {
				connectionSupervisor.stopConnectionTester();
				connectionSupervisor = null;
				logger.debug("closed connection tester!");
			}
			if (inStream != null) {
				inBufferedReader.close();
				inStream.close();
				inStream = null;
				inBufferedReader = null;
				logger.debug("closed input stream!");
			}
			if (outStream != null) {
				outStream.close();
				outStream = null;
				logger.debug("closed output stream!");
			}
			if (ipControlSocket != null) {
				ipControlSocket.close();
				ipControlSocket = null;
				logger.debug("closed socket!");
			}
			connected = false;
		} catch (IOException ioException) {
			logger.error("Closing connection throws an exception!", ioException);
		}
		
		return connected;
	}

	

	/**
	 * Sends to command to the receiver.
	 * It does not wait for a reply.
	 * @param ipCmd the command to send.
	 **/
	public void sendCommand(String ipCmd) {
		logger.debug("Send command: {}", ipCmd);
		
		StringBuilder sb = new StringBuilder();
		sb.append(ipCmd);
		sb.append((char)'\r');
		sendCommand(sb, false, retryCount);
		
		// when comamnd is power on, send it twice. first wakes up receiver
		if( ipCmd.contentEquals( IpControlCommand.POWER_ON.getCommand() ) )	{
			
			// delay and repeat command after 100ms - accoring to Pioneer protocol spec
			try {
				TimeUnit.MILLISECONDS.sleep(100); // wait 100ms to power on
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			sendCommand(sb, false, retryCount);
		}
	}


	/**
	 * Sends to command to the receiver.
	 * 
	 * @param ipCmd the Pioneer IP command to send.
	 * @param closeSocket flag to close the connection when done or leave it open.
	 * @param retry retry count.
	 **/
	private void sendCommand(StringBuilder ipCmd, boolean closeSocket, int retry) {

		if (connectSocket()) {
			try {

				if (logger.isTraceEnabled() ) {
					logger.trace("Sending {} bytes: {}", ipCmd.length(), DatatypeConverter.printHexBinary(ipCmd.toString().getBytes()));
				}
				outStream.writeBytes(ipCmd.toString());
				outStream.flush();
				
			} catch (IOException ioException) {
				logger.error("Error occured when sending command", ioException);
				
				if ( retry > 0) {
					logger.debug("Retry {}...", retry);
					closeSocket();
					sendCommand(ipCmd, closeSocket, retry--);
				}
			}
		}
		
		// finally close the socket if required ...
		if (closeSocket) {
			closeSocket();
		}
	}

	/**
	 * This method wait any state messages form receiver.
	 * 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws IpcontrolException 
	 **/
	private void waitStateMessages() throws NumberFormatException,
			IOException, InterruptedException, IpcontrolException {

		if (connected) {

			PioneerAvrStatusUpdateEvent event = new PioneerAvrStatusUpdateEvent(this);

			logger.trace("Waiting status messages");

			while (true) {
				
				String receivedData = inBufferedReader.readLine();
				
				if (logger.isTraceEnabled()) {
					logger.trace("Received {} bytes: {}", receivedData.length(),
							DatatypeConverter.printHexBinary(receivedData.getBytes()));
				}
				
			
				// send message to event listeners
				try {
					Iterator<PioneerAvrEventListener> iterator = _listeners
							.iterator();

					while (iterator.hasNext()) {
						((PioneerAvrEventListener) iterator.next())
								.statusUpdateReceived(event, receiverIP, receivedData );
					}

				} catch (Exception e) {
					logger.error("Event listener invoking error", e);
				}
			}

		} else {
			throw new IOException("Not Connected to Receiver");
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

			logger.debug("Data listener started");

			boolean restartConnection = false;
			
			// as long as no interrupt is requested, continue running
			while (!interrupted) {
				try {
					waitStateMessages();
					
				} catch (IpcontrolException e) {

					logger.error("Error occured during message waiting", e);					
					
				} catch (SocketTimeoutException e) {
					
					if( connectionCheckActive == true ) {
						logger.error("No data received during supervision interval ({} msec)!", SOCKET_TIMEOUT);
						restartConnection = true;
					}
						
					
				} catch (Exception e) {
					
					if (interrupted != true && this.isInterrupted() != true) {
						logger.error("Error occured during message waiting", e);

						restartConnection = true;
						
						// sleep a while, to prevent fast looping if error situation
						// is permanent
						mysleep(1000);
					}
				}
				
				if (restartConnection) {
					restartConnection = false;
					
					closeSocket();
					
					// reopen connection
					logger.debug("Reconnecting...");
					
					try {
						connected = false;	
						connectSocket();	
					} catch (Exception ex) {
						logger.error("Reconnection invoking error", ex);
					}

				}
			}
			
			logger.debug("Data listener stopped");
		}

		private void mysleep(long milli) {
			try {
				sleep(milli);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
	}
	
	private class ConnectionSupervisor {
		private Timer timer;

		public ConnectionSupervisor(int milliseconds) {
			logger.debug("Connection supervisor started, interval {} milliseconds", milliseconds);

			timer = new Timer();
			timer.schedule(new Task(), milliseconds, milliseconds);
		}

		public void stopConnectionTester() {
			timer.cancel();
		}
		
		class Task extends TimerTask {
			public void run() {
				if (connected) {
					logger.debug("Test connection to {}:{}", receiverIP, receiverPort);
					sendCommand("?P");
				}
			}
		}
	}
}
