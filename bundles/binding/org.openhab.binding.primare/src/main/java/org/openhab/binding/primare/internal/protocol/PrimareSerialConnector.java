/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

import org.openhab.binding.primare.internal.PrimareEventListener;
import org.openhab.binding.primare.internal.PrimareStatusUpdateEvent;
import org.openhab.binding.primare.internal.protocol.PrimareMessage;
import org.openhab.binding.primare.internal.protocol.PrimareMessageFactory;
import org.openhab.binding.primare.internal.protocol.PrimareResponseFactory;
import org.openhab.binding.primare.internal.protocol.PrimareUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for Primare serial communication.
 * 
 * @author Pauli Anttila, Evert van Es, Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSerialConnector extends PrimareConnector {

	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareSerialConnector.class);
	
	/** Connection test interval in milliseconds **/
	private static final int CONNECTION_TEST_INTERVAL = 15000;

	// Lock for synchronized sendBytes 
	private final Object oLock = new Object();

	private String serialPortName = null;
	private SerialPort serialPort = null;

	private DataInputStream inStream = null;
	private OutputStream outStream = null;

	private DataListener dataListener = null;

	private ConnectionSupervisor connectionSupervisor = null;
	
	public PrimareSerialConnector(String deviceId, 
				      String serialPortName,
				      PrimareMessageFactory mf, 
				      PrimareResponseFactory rf) {
		this.deviceId = deviceId;
		this.serialPortName = serialPortName;
		this.messageFactory = mf;
		this.responseFactory = rf;
	}
	

	public static <T extends PrimareSerialConnector> T newForModel(String m, 
								       String deviceId, 
								       String serialPortName) {
		
		T pc = null;
		
		if (m == null) {
			logger.error("connectorForModel called with null argument");
			return null;
		}	
		
		if ("SP31.7".equals(m) || 
		    "SP31".equals(m) || 
		    "SPA20".equals(m) || 
		    "SPA21".equals(m)) {
			pc = (T) new org.openhab.binding.primare.internal.protocol.spa20
				.PrimareSPA20SerialConnector(deviceId, serialPortName);
		} else {
			logger.error("Could not find PrimareSerialConnector for Primare model {m}");
		}
		
		return pc;
	}

	@Override
	public String toString() {
		return String.format("[%s on %s]", deviceId, serialPortName);
	}
	
	@Override
	public void connect() {
		
		logger.debug("Connecting to {}", this.toString());
		
		try {
			connectSerial();
		} catch(Exception unimportant) {}
		
		// Start connection supervisor regardless of initial connection status
		if (connectionSupervisor == null) {
			logger.trace("Starting connection supervisor for {}", this.toString());
			connectionSupervisor = new ConnectionSupervisor(CONNECTION_TEST_INTERVAL);
			logger.trace("Started connection supervisor for {}", this.toString());
		}
	}

	
	@Override
	public void disconnect() {
		disconnectSerial();
	}


	public void sendBytes(byte[] msg) throws IOException {
		
		if (isConnected()) {
			
			logger.trace("Sending (hex) [{}] to {} via Serial",
				     PrimareUtils.byteArrayToHex(msg),
				     this.toString());
		
			try {
				// Synchronize, since both main and supervisor threads 
				// will be writing to outStream
				synchronized (oLock) {
					outStream.write(msg);
					outStream.flush();
				}
				
				bytesSentAt = new Date();
				logger.trace("Sent and flushed (hex) [{}] to {} via Serial",
					     PrimareUtils.byteArrayToHex(msg),
					     this.toString());
				
			} catch (IOException e) {
				connectionBroken = true;			
				throw e;
			}
		}
	}
	

	public boolean isConnected() {
		return serialPort != null && !connectionBroken;
	}

	private void connectSerial() throws Exception {

		logger.debug("Initializing serial port {}", serialPortName);
		
		try {

			CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(serialPortName);
			
			CommPort commPort = portIdentifier
				.open(this.getClass().getName(), 2000);
			
			serialPort = (SerialPort) commPort;
	    
			try {
				serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8,
							       SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				serialPort.enableReceiveThreshold(1);
				serialPort.disableReceiveTimeout();
			} catch (UnsupportedCommOperationException unimportant) {
				// We might have a perfectly usable PTY even if above operations are unsupported
			};


			inStream = new DataInputStream(serialPort.getInputStream());
			outStream = serialPort.getOutputStream();

			outStream.flush();
			if (inStream.markSupported()) {
				inStream.reset();
			}
		
			logger.debug("Starting DataListener for {}", PrimareSerialConnector.this.toString());
			dataListener = new DataListener();
			dataListener.start();
			logger.debug("Starting DataListener for {}", PrimareSerialConnector.this.toString());

			sendInitMessages();

			
		} catch (NoSuchPortException e) {
			logger.error("No such port: {}",serialPortName);

			Enumeration portList = CommPortIdentifier.getPortIdentifiers();
			if (portList.hasMoreElements()) {
				StringBuilder sb = new StringBuilder();
				while(portList.hasMoreElements()){
					CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
					sb.append(String.format("%s ",portId.getName()));
				}
				logger.error("The following communications ports are available: {}",
					     sb.toString().trim());
			} else {
				logger.error("There are no communications ports available");
			}
			logger.error("You may consider OpenHAB startup parameter [ -Dgnu.io.rxtx.SerialPorts={} ]", 
				     serialPortName);
			throw e;
		}
	}


	private void disconnectSerial() {

		logger.debug("Disconnecting {}",PrimareSerialConnector.this.toString());
		
		if (dataListener != null) {
			logger.trace("{} interrupt serial listener",
				     PrimareSerialConnector.this.toString());
			dataListener.interrupt();
		}

		if (outStream != null) {
			logger.trace("{} close serial out stream",
				     PrimareSerialConnector.this.toString());

			IOUtils.closeQuietly(outStream);
		}
		if (inStream != null) {
			logger.trace("{} close serial in stream",
				     PrimareSerialConnector.this.toString());
			IOUtils.closeQuietly(inStream);
		}

		if (serialPort != null) {
			logger.trace("{} close serial port",
				     PrimareSerialConnector.this.toString());
			serialPort.close();
		}

		dataListener = null;
		serialPort = null;
		outStream = null;
		inStream = null;
		
		logger.debug("Connection to {} closed",PrimareSerialConnector.this.toString());

	}
	

	
	public class DataListener extends Thread {
		boolean interrupted = false;
		
		DataListener() {
		}

		public void run() {

			logger.debug("Data listener for {} started", 
				     PrimareSerialConnector.this.toString());

			// continue running as long as we are connected and no interrupt is requested
			while (!connectionBroken && !interrupted) {
				try {
					waitStateMessages();
				} catch (IOException e) {
					logger.error("Reading from serial port failed", e);
					
					// Mark connection broken, supervisor thread will clean up
					connectionBroken = true;
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
			
			logger.debug("Data listener for {} stopped", PrimareSerialConnector.this.toString());
		}



		/**
		 * Read bytes from inStream
		 * 
		 * @throws IOException 
		 * @throws InterruptedException 
		 **/
		private void waitStateMessages() throws IOException, InterruptedException {
			
			logger.debug("Entered waitStateMessages loop for {}",
				     PrimareSerialConnector.this.toString());
			
			while (true) {
				logger.trace("waitStateMessages - waiting data from {}",
					     PrimareSerialConnector.this.toString());
				byte b = inStream.readByte();
				bytesReceivedAt = new Date();
				
				buffer[total++] = b;
				// Access byte[] buffer and consume bytes 0 .. total-1 if DLE ETX has been seen 
				parseData(total-1);
				
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
			
		}
		
	}

	

	private class ConnectionSupervisor {
		private Timer timer;
		
		public ConnectionSupervisor(int milliseconds) {
			logger.debug("Connection {} supervisor started, interval {} milliseconds",
				     PrimareSerialConnector.this.toString(), milliseconds);
			
			timer = new Timer();
			timer.schedule(new ConnectionSupervisorTask(), milliseconds, milliseconds);
		}
		
		class ConnectionSupervisorTask extends TimerTask {
			public void run() {
				logger.debug("Scheduled connection supervisor task started for {}", 
					     PrimareSerialConnector.this.toString());
				
				if (connectionBroken) {
					// unclear connection state, cleanup
					disconnectSerial();
				}
				
				if (!isConnected()) {
					logger.trace("No connection, connecting to {}",
						     PrimareSerialConnector.this.toString());
					try {
						connectSerial();
					} catch(Exception unimportant) {
						logger.trace("Still no connection after retry, failed to connect to {}",
							     PrimareSerialConnector.this.toString());
					}
				} else {
					
					try {
						sendPingMessages();
					} catch(Exception unimportant) {
						logger.trace("Send ping message to {} failed",
							     PrimareSerialConnector.this.toString());
						// unclear connection state, cleanup
						disconnectSerial();
					}
					
				}
			}
		}
	}
	
}
