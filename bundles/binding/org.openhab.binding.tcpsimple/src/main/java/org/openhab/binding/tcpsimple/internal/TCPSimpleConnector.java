/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.tcpsimple.internal;

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
 * TCPSimple network communications connector. Maintains the IP connection and
 * reconnects on error. If responses stop, the connection is reconnected.
 * 
 * @author Chris Jackson
 * @since 1.3.0
 */
public class TCPSimpleConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(TCPSimpleConnector.class);

	private static List<TCPSimpleEventListener> _listeners = new ArrayList<TCPSimpleEventListener>();

	private String ipAddress;
	private int ipPort;

	private Socket socket = null;
	private InputStream in = null;
	private OutputStream out = null;
	
	private long lastReceive;

	Thread inputThread = null;

	public TCPSimpleConnector() {
	}

	public void connect(String address, int port) throws IOException {
		ipAddress = new String(address);
		ipPort = port;

		// Initialise the last receive time to avoid immediate reconnect
		lastReceive = System.currentTimeMillis();

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
			logger.error("Couldn't get I/O for the connection to: {}:{}.",
					ipAddress, ipPort);
			
			return;
		}

		inputThread = new InputReader(in, this);
		inputThread.start();

	}

	public void disconnect() {
		if (socket == null)
			return;

		logger.debug("Interrupt connection");
		inputThread.interrupt();

		logger.debug("Close connection");
		try {
			out.close();
		} catch (IOException e) {
			logger.error("Error closing TCPSimple connection: ", e.getMessage());
		}

		socket = null;
		in = null;
		out = null;
		inputThread = null;

		logger.debug("Ready");
	}

	public void sendMessage(byte[] data) {
		if (socket == null) {
			logger.debug("TCPSimple disconnected: Performing reconnect");
			try {
				doConnect();
			} catch (IOException e) {
				logger.error("Error reconnecting Heatmiser");
			}
		}

		if (socket == null)
			return;

		try {
			out.write(data);
			out.flush();
		} catch (IOException e) {
			logger.error("TCPSimple: Error sending message " + e.getMessage());
			disconnect();
		}
	}

	public synchronized void addEventListener(TCPSimpleEventListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeEventListener(TCPSimpleEventListener listener) {
		_listeners.remove(listener);
	}
	
	public long getLastReceive() {
		return lastReceive;
	}

	public class InputReader extends Thread {
		InputStream in;
		TCPSimpleConnector connector;

		public InputReader(InputStream in, TCPSimpleConnector connector) {
			this.in = in;
			this.connector = connector;
		}

		public void interrupt() {
			super.interrupt();
			try {
				in.close();
			} catch (IOException e) {
				logger.error("Error reading TCPSimple connection: ",
						e.getMessage());
			} // quietly close
		}

		public void run() {
			final int dataBufferMaxLen = 1024;

			byte[] dataBuffer = new byte[dataBufferMaxLen];

//			int msgLen = 0;
			int index = 0;

			try {
				byte[] tmpData = new byte[150];
				int len = -1;

				while ((len = in.read(tmpData)) > 0) {
					lastReceive = System.currentTimeMillis();
					for (int i = 0; i < len; i++) {
						// Ignore CR
						if (tmpData[i] == '\r')
							continue;
						
						// Process new line
						if (tmpData[i] == '\n') {
							// Terminate data
//							dataBuffer[index] = 0;
							
							// whole message received, send an event
							String msg = new String(dataBuffer, 0, index);
							index = 0;

							try {
								TCPSimpleResponseEvent event = new TCPSimpleResponseEvent(
										connector);

								Iterator<TCPSimpleEventListener> iterator = _listeners
										.iterator();

								while (iterator.hasNext()) {
									((TCPSimpleEventListener) iterator.next())
											.packetReceived(event, msg);
								}

							} catch (Exception e) {
								logger.error("Event listener error", e);
							}

						}
						else {
							if (index >= dataBufferMaxLen) {
								// too many bytes received - RESET!
								index = 0;
							}

							dataBuffer[index++] = tmpData[i];
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
