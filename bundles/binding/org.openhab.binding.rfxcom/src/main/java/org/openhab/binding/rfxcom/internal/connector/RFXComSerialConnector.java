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
package org.openhab.binding.rfxcom.internal.connector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFXCOM connector for serial port communication.
 * 
 * @author Pauli Anttila, Evert van Es
 * @since 1.2.0
 */
public class RFXComSerialConnector implements RFXComConnectorInterface {

	private static final Logger logger = LoggerFactory
			.getLogger(RFXComSerialConnector.class);

	private static List<RFXComEventListener> _listeners = new ArrayList<RFXComEventListener>();

	InputStream in = null;
	OutputStream out = null;
	SerialPort serialPort = null;
	Thread readerThread = null;

	public RFXComSerialConnector() {
	}

	@Override
	public void connect(String device) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(device);

		CommPort commPort = portIdentifier
				.open(this.getClass().getName(), 2000);

		serialPort = (SerialPort) commPort;
		serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.enableReceiveThreshold(1);
		serialPort.disableReceiveTimeout();
		
		in = serialPort.getInputStream();
		out = serialPort.getOutputStream();
		
		out.flush();
		if (in.markSupported()) {
			in.reset();
		}

		readerThread = new SerialReader(in);
		readerThread.start();
	}

	@Override
	public void disconnect() {
		logger.debug("Disconnecting");
		
		if (readerThread != null) {
			logger.debug("Interrupt serial listener");
			readerThread.interrupt();
		}

		if (out != null) {
			logger.debug("Close serial out stream");
			IOUtils.closeQuietly(out);
		}
		if (in != null) {
			logger.debug("Close serial in stream");
			IOUtils.closeQuietly(in);
		}

		if (serialPort != null) {
			logger.debug("Close serial port");
			serialPort.close();
		}

		readerThread = null;
		serialPort = null;
		out = null;
		in = null;
		
		logger.debug("Closed");
	}
	
	
	@Override
	public void sendMessage(byte[] data) throws IOException {
		out.write(data);
		out.flush();
	}

	public synchronized void addEventListener(RFXComEventListener rfxComEventListener) {
		_listeners.add(rfxComEventListener);
	}

	public synchronized void removeEventListener(RFXComEventListener listener) {
		_listeners.remove(listener);
	}

	public class SerialReader extends Thread {
		boolean interrupted = false;
		InputStream in;

		public SerialReader(InputStream in) {
			this.in = in;
		}
		
		@Override
		public void interrupt() {
			interrupted = true;
			super.interrupt();
		    try {
		      in.close();
		    } catch (IOException e) {} // quietly close
		}

		public void run() {
			final int dataBufferMaxLen = Byte.MAX_VALUE;

			byte[] dataBuffer = new byte[dataBufferMaxLen];

			int msgLen = 0;
			int index = 0;
			boolean start_found = false;

			logger.debug("Data listener started");
			
			try {

				byte[] tmpData = new byte[20];
				int len = -1;

				while ((len = in.read(tmpData)) > 0 && interrupted != true) {
					
					byte[] logData = Arrays.copyOf(tmpData, len);
					logger.trace("Received data (len={}): {}",
							len,
							DatatypeConverter.printHexBinary(logData));
					
					for (int i = 0; i < len; i++) {

						if (index > dataBufferMaxLen) {
							// too many bytes received, try to find new start
							start_found = false;
						}

						if (start_found == false && tmpData[i] > 0) {

							start_found = true;
							index = 0;
							dataBuffer[index++] = tmpData[i];
							msgLen = tmpData[i] + 1;

						} else if (start_found) {

							dataBuffer[index++] = tmpData[i];

							if (index == msgLen) {

								// whole message received, send an event

								byte[] msg = new byte[msgLen];

								for (int j = 0; j < msgLen; j++)
									msg[j] = dataBuffer[j];

								RFXComMessageReceivedEvent event = new RFXComMessageReceivedEvent(
										this);

								try {
									Iterator<RFXComEventListener> iterator = _listeners
											.iterator();

									while (iterator.hasNext()) {
										((RFXComEventListener) iterator.next())
												.packetReceived(event, msg);
									}

								} catch (Exception e) {
									logger.error("Event listener invoking error", e);
								}

								// find new start
								start_found = false;
							}
						}
					}
				}
			} catch (InterruptedIOException e) {
			      Thread.currentThread().interrupt();
			      logger.error("Interrupted via InterruptedIOException");
			} catch (IOException e) {
				logger.error("Reading from serial port failed", e);
			}
			
			logger.debug("Data listener stopped");
		}
	}
}
