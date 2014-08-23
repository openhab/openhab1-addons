/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal.eiscp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onkyo.internal.OnkyoEventListener;
import org.openhab.binding.onkyo.internal.OnkyoStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p><b>Note:</b>This class was originally developed by Tom Gutwin but suffered heavy 
 * refactoring recently.</p><br> 
 * 
 * A class that wraps the communication to Onkyo/Integra devices using the
 * Ethernet Integra Serial Control Protocol (eISCP).
 * 
 * @author Tom Gutwin P.Eng
 * @author Thomas.Eichstaedt-Engelen (Refactoring)
 * @author Pauli Anttila (Simplified, rewritten and added status update listener functionality)
 */
public class Eiscp {
	
	private static final Logger logger = LoggerFactory.getLogger(Eiscp.class);
	
	/** Instantiated class IP for the receiver to communicate with. **/
	private String receiverIP = "";

	/** default eISCP port. **/
	public static final int DEFAULT_EISCP_PORT = 60128;
	
	/** Connection timeout in milliseconds **/
	private static final int CONNECTION_TIMEOUT = 5000;

	/** Connection test interval in milliseconds **/
	private static final int CONNECTION_TEST_INTERVAL = 60000;
	
	/** Socket timeout in milliseconds **/
	private static final int SOCKET_TIMEOUT = CONNECTION_TEST_INTERVAL + 10000;
	
	/** Instantiated class Port for the receiver to communicate with. **/
	private int receiverPort = DEFAULT_EISCP_PORT;	
	
	private static Socket eiscpSocket = null;
	private DataListener dataListener = null;
	private static ObjectOutputStream outStream = null;
	private static DataInputStream inStream = null;
	private static boolean connected = false;
	private static List<OnkyoEventListener> _listeners = new ArrayList<OnkyoEventListener>();
	private static int retryCount = 1;
	private static ConnectionSupervisor connectionSupervisor = null;
	
	/**
	 * Constructor that takes your receivers IP and port.
	 **/
	public Eiscp(String ip, int eiscpPort) {
		if (StringUtils.isNotBlank(ip)) {
			receiverIP = ip;
		}
		if (eiscpPort >= 1) {
			receiverPort = eiscpPort;
		}
	}

	/**
	 * Add event listener, which will be invoked when status upadte is received from receiver.
	 **/
	public synchronized void addEventListener(OnkyoEventListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Remove event listener.
	 **/
	public synchronized void removeEventListener(OnkyoEventListener listener) {
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
		Eiscp.retryCount = retryCount;
	}

	/**
	 * Connects to the receiver by opening a socket connection through the
	 * IP and port defined on constructor.
	 **/
	public boolean connectSocket() {
		return connectSocket(receiverIP, receiverPort);
	}
	
	/**
	 * Connects to the receiver by opening a socket connection through the
	 * IP and port.
	 **/
	public boolean connectSocket(String ip, int port) {
		
		if (eiscpSocket == null || !connected || !eiscpSocket.isConnected()) {
			try {
				// Creating a socket to connect to the server
				eiscpSocket = new Socket();
				eiscpSocket.connect(new InetSocketAddress(ip, port), CONNECTION_TIMEOUT);
				
				logger.debug("Connected to {} on port {}", ip, port);
				
				// Get Input and Output streams
				outStream = new ObjectOutputStream(eiscpSocket.getOutputStream());
				inStream = new DataInputStream(eiscpSocket.getInputStream());

				eiscpSocket.setSoTimeout(SOCKET_TIMEOUT);
				outStream.flush();
				connected = true;
				
				receiverIP = ip;
				receiverPort = port;
				
				// start status update listener
				if (dataListener == null) {
					dataListener = new DataListener();
					dataListener.start();
				}
				
				// start connection tester
				if (connectionSupervisor == null) {
					connectionSupervisor = new ConnectionSupervisor(CONNECTION_TEST_INTERVAL);
				}
				
				
			} catch (UnknownHostException unknownHost) {
				logger.error("You are trying to connect to an unknown host!", unknownHost);
			} catch (IOException ioException) {
				logger.error("Can't connect: " + ioException.getMessage());
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
				inStream.close();
				inStream = null;
				logger.debug("closed input stream!");
			}
			if (outStream != null) {
				outStream.close();
				outStream = null;
				logger.debug("closed output stream!");
			}
			if (eiscpSocket != null) {
				eiscpSocket.close();
				eiscpSocket = null;
				logger.debug("closed socket!");
			}
			connected = false;
		} catch (IOException ioException) {
			logger.error("Closing connection throws an exception!", ioException);
		}
		
		return connected;
	}

	/**
	 * Wraps a command in a eiscp data message (data characters).
	 * 
	 * @param eiscpCmd
	 *            eISCP command.
	 * @return StringBuffer holing the full iscp message packet
	 **/
	private StringBuilder getEiscpMessage(String eiscpCmd) {

		StringBuilder sb = new StringBuilder();
		int eiscpDataSize = eiscpCmd.length() + 2; // this is the eISCP data size
		int eiscpMsgSize = eiscpDataSize + 1 + 16; // this is the eISCP data size

		/*
		 * This is where I construct the entire message character by character.
		 * Each char is represented by a 2 digit hex value
		 */
		sb.append("ISCP");
		// the following are all in HEX representing one char

		// 4 char Big Endian Header
		sb.append((char) 0x00);
		sb.append((char) 0x00);
		sb.append((char) 0x00);
		sb.append((char) 0x10);

		// 4 char Big Endian data size
		sb.append( (char) ((eiscpMsgSize >> 24) & 0xFF) );
		sb.append( (char) ((eiscpMsgSize >> 16) & 0xFF) );
		sb.append( (char) ((eiscpMsgSize >> 8) & 0xFF) );
		sb.append( (char) (eiscpMsgSize & 0xFF) );
		
		// eiscp_version = "01";
		sb.append((char) 0x01);

		// 3 chars reserved = "00"+"00"+"00";
		sb.append((char) 0x00);
		sb.append((char) 0x00);
		sb.append((char) 0x00);

		// eISCP data
		// Start Character
		sb.append("!");

		// eISCP data - unit type char '1' is receiver
		sb.append("1");

		// eISCP data - 3 char command and param ie PWR01
		sb.append(eiscpCmd);

		// msg end - EOF
		sb.append((char) 0x0D);

		return sb;
	}

	/**
	 * Sends to command to the receiver.
	 * It does not wait for a reply.
	 * @param eiscpCmd the eISCP command to send.
	 **/
	public void sendCommand(String eiscpCmd) {
		logger.debug("Send command: {}", eiscpCmd);
		StringBuilder sb = getEiscpMessage(eiscpCmd);
		sendCommand(sb, false, retryCount);
	}

	/**
	 * Sends to command to the receiver and close the connection when done.
	 * It does not wait for a reply.
	 * @param eiscpCmd the eISCP command to send.
	 **/
	public void sendCommandAndClose(String eiscpCmd) {
		logger.debug("Send command: {}", eiscpCmd);
		StringBuilder sb = getEiscpMessage(eiscpCmd);
		sendCommand(sb, true, retryCount);
	}

	/**
	 * Sends to command to the receiver.
	 * 
	 * @param eiscpCmd the eISCP command to send.
	 * @param closeSocket flag to close the connection when done or leave it open.
	 * @param retry retry count.
	 **/
	private void sendCommand(StringBuilder eiscpCmd, boolean closeSocket, int retry) {

		if (connectSocket()) {
			try {

				if (logger.isTraceEnabled() ) {
					logger.trace("Sending {} bytes: {}", eiscpCmd.length(), DatatypeConverter.printHexBinary(eiscpCmd.toString().getBytes()));
				}
				
				outStream.writeBytes(eiscpCmd.toString());
				outStream.flush();
			} catch (IOException ioException) {
				logger.error("Error occured when sending command", ioException);
				
				if ( retry > 0) {
					logger.debug("Retry {}...", retry);
					closeSocket();
					sendCommand(eiscpCmd, closeSocket, retry--);
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
	 * @throws EiscpException 
	 **/
	private void waitStateMessages() throws NumberFormatException,
			IOException, InterruptedException, EiscpException {

		if (connected) {

			OnkyoStatusUpdateEvent event = new OnkyoStatusUpdateEvent(this);

			logger.trace("Waiting status messages");

			while (true) {

				// 1st 4 chars are the leadIn
				
				if (inStream.readByte() != 'I') continue;
				if (inStream.readByte() != 'S') continue;
				if (inStream.readByte() != 'C') continue;
				if (inStream.readByte() != 'P') continue;

				// header size
				final int headerSize = (inStream.readByte() & 0xFF) << 24
						| (inStream.readByte() & 0xFF) << 16
						| (inStream.readByte() & 0xFF) << 8
						| (inStream.readByte() & 0xFF);

				logger.trace("Header size: {}", headerSize);
				
				if (headerSize != 16) {
					throw new EiscpException("Unsupported header size: "
							+ headerSize);
				}
				
				// header size
				final int dataSize = (inStream.readByte() & 0xFF) << 24
						| (inStream.readByte() & 0xFF) << 16
						| (inStream.readByte() & 0xFF) << 8
						| (inStream.readByte() & 0xFF);

				logger.trace("Data size: {}", dataSize);

				// version
				final byte versionChar = inStream.readByte();
				if (versionChar != 1) {
					throw new EiscpException("Unsupported version "
							+ String.valueOf(versionChar));
				}

				// skip 3 reserved bytes
				inStream.readByte();
				inStream.readByte();
				inStream.readByte();
				
				byte[] data = new byte[dataSize] ;
				
				final int bytesReceived = inStream.read(data, 0, data.length);
				
				if (logger.isTraceEnabled()) {
					logger.trace("Received {} bytes: {}", bytesReceived,
							DatatypeConverter.printHexBinary(data));
				}
				
				if (bytesReceived != dataSize) {
					throw new EiscpException("Data missing: "
							+ (dataSize - bytesReceived));
				}
				
				// start char
				final byte startChar = data[0];

				if (startChar != '!') {
					throw new EiscpException("Illegal start char " + startChar);
				}

				// unit type
				@SuppressWarnings("unused")
				final byte unitType = data[1];

				// data should be end to "[EOF]" or "[EOF][CR]" or
				// "[EOF][CR][LF]" characters depend on model
				// [EOF] End of File ASCII Code 0x1A
				// [CR] Carriage Return ASCII Code 0x0D (\r)
				// [LF] Line Feed ASCII Code 0x0A (\n)

				int endBytes = 0;

				if (data[dataSize - 4] == (byte) 0x1A
						&& data[dataSize - 3] == '\r'
						&& data[dataSize - 2] == '\n'
						&& data[dataSize - 1] == 0x00) {

					// skip "[EOF][CR][LF][NULL]"
					endBytes = 4;

				} else if (data[dataSize - 3] == (byte) 0x1A
						&& data[dataSize - 2] == '\r'
						&& data[dataSize - 1] == '\n') {

					// skip "[EOF][CR][LF]"
					endBytes = 3;

				} else if (data[dataSize - 2] == (byte) 0x1A
						&& data[dataSize - 1] == '\r') {

					// "[EOF][CR]"
					endBytes = 2;

				} else if (data[dataSize - 1] == (byte) 0x1A) {

					// "[EOF]"
					endBytes = 1;

				} else {
					throw new EiscpException("Illegal end of message");
				}

				int bytesToCopy = dataSize - 2 - endBytes;

				byte[] message = new byte[bytesToCopy];

				// skip 2 first bytes and copy all bytes before end bytes 
				System.arraycopy(data, 2, message, 0, bytesToCopy);

				// send message to event listeners
				try {
					Iterator<OnkyoEventListener> iterator = _listeners
							.iterator();

					while (iterator.hasNext()) {
						((OnkyoEventListener) iterator.next())
								.statusUpdateReceived(event, receiverIP,
										new String(message));
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
					
				} catch (EiscpException e) {

					logger.error("Error occured during message waiting", e);					
					
				} catch (SocketTimeoutException e) {
					
					logger.error("No data received during supervision interval ({} sec)!", SOCKET_TIMEOUT);
					
					restartConnection = true;
					
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
					sendCommand("PWRQSTN");
				}
			}
		}
	}
}
