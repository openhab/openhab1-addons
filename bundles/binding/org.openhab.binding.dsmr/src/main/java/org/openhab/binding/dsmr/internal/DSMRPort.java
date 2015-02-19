/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import org.openhab.binding.dsmr.internal.messages.OBISMessage;
import org.openhab.binding.dsmr.internal.p1telegram.P1TelegramParser;
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
	/* logger */
	private static final Logger logger = LoggerFactory
			.getLogger(DSMRPort.class);

	private enum PortState {
		CLOSED, AUTO_DETECT, OPENED;
	}

	private enum PortSpeed {
		LOW_SPEED, HIGH_SPEED
	}

	/* private object variables */
	private final String portName;
	private final int readTimeoutMSec;
	private final int autoDetectTimeoutMSec;
	private long autoDetectTS;

	/* serial port resources */
	private SerialPort serialPort;
	private BufferedInputStream bis;
	private byte[] buffer = new byte[1024]; // 1K

	/* state variables */
	private PortState portState;
	private PortSpeed portSpeed;

	/* helpers */
	private P1TelegramParser p1Parser;

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
	 * @param p1Parser
	 *            {@link P1TelegramParser}
	 * @param readTimeoutMSec
	 *            communication timeout in milliseconds
	 * @param autoDetectTimeoutMSec
	 *            timeout for auto detection in milliseconds (after this period
	 *            the Serial Port speed will be changed)
	 */
	public DSMRPort(String portName, P1TelegramParser p1Parser,
			int readTimeoutMSec, int autoDetectTimeoutMSec) {
		this.portName = portName;
		this.readTimeoutMSec = readTimeoutMSec;
		this.autoDetectTimeoutMSec = autoDetectTimeoutMSec;
		this.p1Parser = p1Parser;

		portSpeed = PortSpeed.HIGH_SPEED;
		portState = PortState.CLOSED;
	}

	/**
	 * Returns whether or not the port is open
	 * 
	 * @return true if the DSMRPort is open, false otherwise
	 */
	public boolean isOpen() {
		return portState != PortState.CLOSED;
	}

	/**
	 * Closes the DSMRPort and release OS resources
	 */
	public void close() {
		synchronized (portLock) {
			logger.info("Closing DSMR port");

			portState = PortState.CLOSED;

			// Close resources
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException ioe) {
					logger.debug("Failed to close reader", ioe);
				}
			}
			if (serialPort != null) {
				serialPort.close();
			}

			// Release resources
			bis = null;
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
		List<OBISMessage> receivedMessages = new LinkedList<OBISMessage>();

		handlePortState();

		// open port if it is not open
		if (portState == PortState.CLOSED) {
			logger.warn("Could not open DSMRPort, no values will be read");

			close();

			return receivedMessages;
		}

		try {
			// Read without block
			int bytesAvailable = bis.available();
			while (bytesAvailable > 0) {
				int bytesRead = bis.read(buffer, 0,
						Math.min(bytesAvailable, buffer.length));

				if (bytesRead > 0) {
					receivedMessages.addAll(p1Parser.parseData(buffer, 0,
							bytesRead));
				} else {
					logger.debug("Expected bytes " + bytesAvailable
							+ " to read, but " + bytesRead + " bytes were read");
				}
				bytesAvailable = bis.available();
			}
		} catch (IOException ioe) {
			/*
			 * Read is interrupted. This can be due to a broken connection or
			 * closing the port
			 */
			if (portState == PortState.CLOSED) {
				// Closing on purpose
				logger.info("Read aborted: DSMRPort is closed");
			} else {
				// Closing due to broken connection

				logger.warn("DSMRPort is not available anymore, closing port");
				logger.debug("Caused by:", ioe);

				close();
			}
		} catch (NullPointerException npe) {
			if (portState == PortState.CLOSED) {
				// Port was closed
				logger.info("Read aborted: DSMRPort is closed");
			} else {
				logger.error("Unexpected problem occured", npe);

				close();
			}
		}

		if (portState == PortState.AUTO_DETECT && receivedMessages.size() > 0) {
			portState = PortState.OPENED;
		}
		return receivedMessages;
	}

	/**
	 * Checks the current port state and initiate actions based on it. 
	 * <ul>
	 * <li>CLOSED --> Port will be opened 
	 * <li>AUTO_DETECT --> Auto detect period will be evaluated 
	 * <li>OPENED --> Nothing has to be done
	 * </ul>
	 */
	private void handlePortState() {
		switch (portState) {
		case CLOSED:
			if (open()) {
				portState = PortState.AUTO_DETECT;
				autoDetectTS = System.currentTimeMillis();
			}
			break;
		case AUTO_DETECT:
			if ((System.currentTimeMillis() - autoDetectTS) > autoDetectTimeoutMSec) {
				switchPortSpeed();
				close();
				if (open()) {
					portState = PortState.AUTO_DETECT;
					autoDetectTS = System.currentTimeMillis();
				}
			}
			break;
		case OPENED:
			/* do nothing */
			break;
		}
	}

	/**
	 * Switch the Serial Port speed (LOW --> HIGH and vice versa).
	 */
	private void switchPortSpeed() {
		switch (portSpeed) {
		case HIGH_SPEED:
			portSpeed = PortSpeed.LOW_SPEED;
			break;
		case LOW_SPEED:
			portSpeed = PortSpeed.HIGH_SPEED;
			break;
		}
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
			if (portState != PortState.CLOSED) {
				return true;
			}

			try {
				// Opening Operating System Serial Port
				logger.debug("Creating CommPortIdentifier");
				CommPortIdentifier portIdentifier = CommPortIdentifier
						.getPortIdentifier(portName);
				logger.debug("Opening CommPortIdentifier");
				CommPort commPort = portIdentifier.open(
						"org.openhab.binding.dsmr", readTimeoutMSec);
				logger.debug("Configure serial port");
				serialPort = (SerialPort) commPort;
				serialPort.enableReceiveThreshold(1);
				serialPort.enableReceiveTimeout(readTimeoutMSec);

				// Configure Serial Port based on specified port speed
				logger.debug("Configure serial port speed " + portSpeed);
				switch (portSpeed) {
				case LOW_SPEED:
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_7,
							SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
					serialPort.setDTR(false);
					serialPort.setRTS(true);

					break;
				case HIGH_SPEED:
					serialPort.setSerialPortParams(115200,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);

					break;
				default:
					logger.error("Invalid speed, closing port");

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
				bis = new BufferedInputStream(serialPort.getInputStream());
			} catch (IOException ioe) {
				logger.error(
						"Failed to get inputstream for serialPort. Closing port",
						ioe);

				return false;
			}
			logger.info("DSMR Port opened successful");

			return true;
		}
	}
}
