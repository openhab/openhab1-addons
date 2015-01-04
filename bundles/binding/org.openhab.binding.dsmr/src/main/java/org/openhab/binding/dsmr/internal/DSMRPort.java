/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import org.openhab.binding.dsmr.internal.messages.OBISMessage;
import org.openhab.binding.dsmr.internal.messages.OBISMsgFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements the DSMR port for energy meters that comply to the
 * Dutch Smart Meter Requirements.
 * <p>
 * This class provides a simple public interface: read and close.
 * <p>
 * The read method will claim OS resources if necessary. If the read method
 * encounters problems it will automatically close itself
 * <p>
 * The close method can be called asynchronous and will release OS resources.
 * <p>
 * In this way the DSMR port can restore the connection automatically
 * <p>
 * <code>
 * An example DSMR telegram in accordance to IEC 62056-21 Mode D.<br>
 * /ISk5\2MT382-1000<br>
 * 0-0:96.1.1(4B384547303034303436333935353037)<br>
 * 1-0:1.8.1(12345.678*kWh)<br>
 * 1-0:1.8.2(12345.678*kWh)<br>
 * 1-0:2.8.1(12345.678*kWh)<br>
 * 1-0:2.8.2(12345.678*kWh)<br>
 * 0-0:96.14.0(0002)<br>
 * 1-0:1.7.0(001.19*kW)<br>
 * 1-0:2.7.0(000.00*kW)<br>
 * 0-0:17.0.0(016*A)<br>
 * 0-0:96.3.10(1)<br>
 * 0-0:96.13.1(303132333435363738)<br>
 * 0-0:96.13.0(303132333435363738393A3B3C3D3E3F303132333435363738393A3B3C3D3E3F<br>
 * 303132333435363738393A3B3C3D3E3F303132333435363738393A3B3C3D3E3F<br>
 * 303132333435363738393A3B3C3D3E3F)<br>
 * 0-1:96.1.0(3232323241424344313233343536373839)<br>
 * 0-1:24.1.0(03)<br>
 * 0-1:24.3.0(090212160000)(00)(60)(1)(0-1:24.2.1)(m3)<br>
 * (00000.000)<br>
 * 0-1:24.4.0(1)<br>
 * !<br>
 * </code>
 * 
 * @author M. Volaart
 * @since 1.7.0
 */
public class DSMRPort {
	/* Internal state based on DSMR specification */
	private enum ReadState {
		WAIT_FOR_START, IDENTIFICATION, DATA, END
	};

	/* logger */
	private static final Logger logger = LoggerFactory
			.getLogger(DSMRPort.class);

	/* private object variables */
	private final String portName;
	private final DSMRVersion version;
	private final int timeoutMSec;

	/* serial port resources */
	private SerialPort serialPort;
	private BufferedReader reader;

	/* state variables */
	private ReadState readerState;
	private boolean isOpen = false;

	/* helpers */
	private OBISMsgFactory factory;

	/*
	 * The portLock is used for the shared data used when opening and closing
	 * the port. The following shared data must be guarded by the lock:
	 * SerialPort, BufferedReader, isOpen
	 */
	private Object portLock = new Object();

	/**
	 * Creates a new DSMRPort. This is only a reference to a port. The port will
	 * not be opened nor it is checked if the DSMR Port can successfully be
	 * opened.
	 * 
	 * @param portName
	 *            Device identifier of the post (e.g. /dev/ttyUSB0)
	 * @param version
	 *            Version of the DSMR Specification. See {@link DSMRVersion}
	 * @param dsmrMeters
	 *            List of available {@link DSMRMeter} in the binding
	 * @param timeoutMSec
	 *            communication timeout in milliseconds
	 */
	public DSMRPort(String portName, DSMRVersion version,
			List<DSMRMeter> dsmrMeters, int timeoutMSec) {
		this.portName = portName;
		this.version = version;
		this.timeoutMSec = timeoutMSec;

		factory = new OBISMsgFactory(version, dsmrMeters);
	}

	/**
	 * Returns whether or not the port is open
	 * 
	 * @return true if the DSMRPort is open, false otherwise
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Closes the DSMRPort and release OS resources
	 */
	public void close() {
		synchronized (portLock) {
			logger.info("Closing DSMR port");

			isOpen = false;
			// Close resources
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					logger.debug("Failed to close reader", ioe);
				}
			}
			if (serialPort != null) {
				serialPort.close();
			}

			// Release resources
			reader = null;
			serialPort = null;
		}
	}

	/**
	 * Reads a complete telegram from the DSMR port.
	 * <p>
	 * If the read is successful a list of received @{link OBISMessage} is
	 * returned. If the read encounters problems the port will be closed and a
	 * list of received {@link OBISMessage} is returned.
	 * <p>
	 * It is a technically valid that the read succeeds with an empty list. Most
	 * likely there is a configuration problem of the global DSMR binding
	 * 
	 * @return List of {@link OBISMessage} with 0 or more entries
	 */
	public List<OBISMessage> read() {
		List<OBISMessage> messages = new ArrayList<OBISMessage>();
		long startTime = System.currentTimeMillis();

		// open port if it is not open
		if (!open()) {
			logger.warn("Could not open DSMRPort, no values will be read");

			close();

			return messages;
		}

		// Initialize readerState
		readerState = ReadState.WAIT_FOR_START;

		try {
			// wait till we reached the end of the telegram or a timeout
			while (readerState != ReadState.END
					&& ((System.currentTimeMillis() - startTime) < (2 * timeoutMSec))) {
				String line = reader.readLine();
				logger.trace(line);
				logger.debug("Reader state: " + readerState);

				switch (readerState) {
				case WAIT_FOR_START:
					if (line.startsWith("/")) {
						readerState = ReadState.IDENTIFICATION;
					}
					break;
				case IDENTIFICATION:
					if (line.length() == 0) {
						readerState = ReadState.DATA;
					}
					break;
				case DATA:
					if (line.startsWith("!")) {
						readerState = ReadState.END;
					} else {
						OBISMessage msg = factory.getMessage(line);
						if (msg != null) {
							messages.add(msg);
						}
					}
					break;
				case END:
					break;
				default:
					logger.warn("Unsupported state:" + readerState);
					break;
				}
			}
			if (readerState != ReadState.END) {
				logger.error("Reading took too long and is aborted (readingtime: "
						+ (System.currentTimeMillis() - startTime) + " ms)");
			}
		} catch (IOException ioe) {
			/*
			 * Read is interrupted. This can be due to a broken connection or
			 * closing the port
			 */
			if (!isOpen) {
				// Closing on purpose
				logger.info("Read aborted: DSMRPort is closed");
			} else {
				// Closing due to broken connection

				logger.warn("DSMRPort is not available anymore, closing port");
				logger.debug("Caused by:", ioe);

				close();
			}
		} catch (NullPointerException npe) {
			if (!isOpen) {
				// Port was closed
				logger.info("Read aborted: DSMRPort is closed");
			} else {
				logger.error("Unexpected problem occured", npe);

				close();
			}
		}

		// Return all received messages
		return messages;
	}

	/**
	 * Opens the Operation System Serial Port
	 * <p>
	 * This method opens the port and set Serial Port parameters according to
	 * the DSMR specification. Since the specification is clear about these
	 * parameters there are not configurable.
	 * <p>
	 * If there are problem while opening the port, it is the responsibility of
	 * the calling method to handle this situation (and for example close the
	 * port again).
	 * <p>
	 * Opening an already open port is harmless. The method will return
	 * immediately
	 * 
	 * @return true if opening was successful (or port was already open), false
	 *         otherwise
	 */
	private boolean open() {
		synchronized (portLock) {
			// Sanity check
			if (isOpen) {
				return true;
			}

			try {
				// Opening Operating System Serial Port
				logger.debug("Creating CommPortIdentifier");
				CommPortIdentifier portIdentifier = CommPortIdentifier
						.getPortIdentifier(portName);
				logger.debug("Opening CommPortIdentifier");
				CommPort commPort = portIdentifier.open(
						"org.openhab.binding.dsmr", 2000);
				logger.debug("Configure serial port");
				serialPort = (SerialPort) commPort;
				serialPort.enableReceiveThreshold(1);
				serialPort.enableReceiveTimeout(timeoutMSec);

				// Configure Serial Port based on specified DSMR version
				logger.debug("Configure serial port based on version "
						+ version);
				switch (version) {
				case V21:
				case V22:
				case V30:
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_7,
							SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
					serialPort.setDTR(false);
					serialPort.setRTS(true);

					break;
				case V40:
				case V404:
					serialPort.setSerialPortParams(115200,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);

					break;
				default:
					logger.error("Invalid version, closing port");

					return false;
				}
			} catch (NoSuchPortException nspe) {
				logger.error("Could not open port: " + portName, nspe);

				return false;
			} catch (PortInUseException piue) {
				logger.error("Port already in use: " + portName, piue);

				return false;
			} catch (UnsupportedCommOperationException ucoe) {
				logger.error("Port is not suitable: " + portName, ucoe);

				return false;
			}

			// SerialPort is ready, open the reader
			logger.info("SerialPort opened successful");
			try {
				reader = new BufferedReader(new InputStreamReader(
						serialPort.getInputStream()));
			} catch (IOException ioe) {
				logger.error(
						"Failed to get inputstream for serialPort. Closing port",
						ioe);

				return false;
			}
			logger.info("DSMR Port opened successful");

			isOpen = true;

			return isOpen;
		}
	}
}
