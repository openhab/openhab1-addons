/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Heatmiser network communications connector.
 * Maintains the IP connection and reconnects on error.
 * If responses stop, the connection is reconnected.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
public class HeatmiserConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(HeatmiserConnector.class);

	private static List<HeatmiserEventListener> _listeners = new ArrayList<HeatmiserEventListener>();

	private String ipAddress;
	private int ipPort;
	
	private Socket socket = null;
	private InputStream in = null;
	private OutputStream out = null;

	Thread inputThread = null;
	
	// The connectionStateCount is used to keep track of errors. It counts up by 1 for each message sent
	// and down by 2 for each message received. Thus if it ever gets "too high" then <50% of messages
	// are receiving a response.
	int connectionStateCount = 0;

	public enum States {
		SEARCHING,
		LENGTH1,
		LENGTH2,
		RECEIVING
	}
	
	public HeatmiserConnector() {
	}

	public void connect(String address, int port) throws IOException {
		ipAddress = new String(address);
		ipPort = port;
		
		doConnect();
	}

	private void doConnect() throws IOException {
		try {
			socket = new Socket(ipAddress, ipPort);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (UnknownHostException e) {
			logger.error("Can't find host: {}:{}.", ipAddress, ipPort);
		} catch (IOException e) {
			logger.error("Couldn't get I/O for the connection to: {}:{}.", ipAddress, ipPort);
			return;
		}

		inputThread = new InputReader(in);
		inputThread.start();
		
		connectionStateCount = 0;
	}

	public void disconnect() {
		if(socket == null)
			return;

		logger.debug("Interrupt connection");
		inputThread.interrupt();

		logger.debug("Close connection");
		try {
			out.close();
		} catch (IOException e) {
			logger.error("Error closing Heatmiser connection: ", e.getMessage());
		}

		socket = null;
		in = null;
		out = null;
		inputThread = null;
		
		logger.debug("Ready");
	}

	/**
	 * Sends a message
	 * @param data data to send
	 */
	public void sendMessage(byte[] data)  {
		if(socket == null) {
			logger.debug("Heatmiser disconnected: Performing reconnect");
			try {
				doConnect();
			} catch (IOException e) {
				logger.error("Error reconnecting Heatmiser");
			}
		}

		if(socket == null)
			return;

		// Increment the state counter by 1
		connectionStateCount++;
		
		try {
			out.write(data);
			out.flush();
		} catch (IOException e) {
			logger.error("HEATMISER: Error sending message "+e.getMessage());
			disconnect();
		}
	}

	public synchronized void addEventListener(HeatmiserEventListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(HeatmiserEventListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * Data receive thread
	 */
	public class InputReader extends Thread {
		InputStream in;

		public InputReader(InputStream in) {
			this.in = in;
		}

		public void interrupt() {
			super.interrupt();
			try {
				in.close();
			} catch (IOException e) {
				logger.error("Error reading Heatmiser connection: ", e.getMessage());
			} // quietly close
		}

		public void run() {
			final int dataBufferMaxLen = 256;

			byte[] dataBuffer = new byte[dataBufferMaxLen];

			int msgLen = 0;
			int index = 0;
			States state = States.SEARCHING;

			try {
				byte[] tmpData = new byte[150];
				int len = -1;

				while ((len = in.read(tmpData)) > 0) {
					for (int i = 0; i < len; i++) {

						if (index >= dataBufferMaxLen) {
							// too many bytes received, try to find new start
							state = States.SEARCHING;
						}

						if (state == States.SEARCHING && (int)(tmpData[i] & 0xff) == 0x81) {
							state = States.LENGTH1;
							index = 0;
							dataBuffer[index++] = tmpData[i];
						}
						else if (state == States.LENGTH1) {
							state = States.LENGTH2;
							msgLen = tmpData[i];
							dataBuffer[index++] = tmpData[i];
						} else if (state == States.LENGTH2) {
							state = States.RECEIVING;
							msgLen += tmpData[i] * 256;
							dataBuffer[index++] = tmpData[i];
						}
						else if (state == States.RECEIVING) {
							dataBuffer[index++] = tmpData[i];
							if (index == msgLen) {
								// whole message received, send an event
								byte[] msg = new byte[msgLen];

								for (int j = 0; j < msgLen; j++)
									msg[j] = dataBuffer[j];

								HeatmiserResponseEvent event = new HeatmiserResponseEvent(this);

								// Decrement the state counter by 2
								if(connectionStateCount <= 2)
									connectionStateCount = 0;
								else
									connectionStateCount-=2;

								try {
									Iterator<HeatmiserEventListener> iterator = _listeners
											.iterator();

									while (iterator.hasNext()) {
										((HeatmiserEventListener) iterator
												.next()).packetReceived(event,
												msg);
									}

								} catch (Exception e) {
									logger.error("Event listener error", e);
								}

								// find new start
								state = States.SEARCHING;
							}
						}
					}
				}
			} catch (InterruptedIOException e) {
				Thread.currentThread().interrupt();
				logger.error("Interrupted via InterruptedIOException");
			} catch (IOException e) {
				logger.error("Reading from network failed", e);
			}

			logger.debug("Ready reading from network");
		}
	}
}
