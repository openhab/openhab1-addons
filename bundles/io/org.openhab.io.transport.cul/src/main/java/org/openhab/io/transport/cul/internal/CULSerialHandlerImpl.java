/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TooManyListenersException;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for culfw based devices which communicate via serial port
 * (cullite for example). This is based on rxtx and assumes constant parameters
 * for the serial port.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULSerialHandlerImpl extends AbstractCULHandler implements
		SerialPortEventListener {

	private static final Map<String, Integer> validParitiesMap;
	static {
		Map<String, Integer> parities = new HashMap<String, Integer>();
		parities.put("EVEN", SerialPort.PARITY_EVEN);
		parities.put("ODD", SerialPort.PARITY_ODD);
		parities.put("MARK", SerialPort.PARITY_MARK);
		parities.put("NONE", SerialPort.PARITY_NONE);
		parities.put("SPACE", SerialPort.PARITY_SPACE);
		validParitiesMap = Collections.unmodifiableMap(parities);
	}

	private static final List<Integer> validBaudrateMap;
	static {
		Integer baudrates[] = {75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200};
		validBaudrateMap = Collections.unmodifiableList(Arrays.asList(baudrates));
	}

	private final static Logger log = LoggerFactory
			.getLogger(CULSerialHandlerImpl.class);

	private final static String KEY_BAUDRATE = "baudrate";
	private final static String KEY_PARITY = "parity";

	private SerialPort serialPort;
	private Integer baudRate = 9600;
	private Integer parityMode = SerialPort.PARITY_EVEN;
	private InputStream is;
	private OutputStream os;

	/**
	 * Default Constructor
	 *
	 * @param deviceName
	 *            String representing the device.
	 * @param mode
	 *            The RF mode for which the device will be configured.
	 */
	public CULSerialHandlerImpl(String deviceName, CULMode mode) {
		this(deviceName, mode, null);
	}

	/**
	 * Constructor including property map for specific configuration.
	 *
	 * @param deviceName
	 *            String representing the device.
	 * @param mode
	 *            The RF mode for which the device will be configured.
	 * @param properties
	 *            Property Map containing specific configuration for serial
	 *            device connection.
	 *            <ul>
	 *            <li>"baudrate" (Integer) Setup baudrate</li>
	 *            <li>"parity" (Integer) Setup parity bit handling.
	 *            (http://show.
	 *            docjava.com/book/cgij/code/data/j4pDoc/constant-values
	 *            .html#serialPort.rxtx.SerialPortInterface.PARITY_NONE)
	 *            </ul>
	 */
	public CULSerialHandlerImpl(String deviceName, CULMode mode,
			Map<String, ?> properties) {
		super(deviceName, mode);

		if(properties==null) return;
		
		final String configuredBaudRate = (String) properties.get(KEY_BAUDRATE);
		Integer tmpBaudRate=baudrateFromConfig(configuredBaudRate);
		if(tmpBaudRate!=null){
			baudRate=tmpBaudRate;
			log.info("Update config, {} = {}", KEY_BAUDRATE, baudRate);
		}
		
		
		final String configuredParity = (String) properties.get(KEY_PARITY);
		
		Integer parsedParityNumber=parityFromConfig(configuredParity);	
		if(parsedParityNumber!=null){
			parityMode=parsedParityNumber;
			log.info("Update config, {} = {} ({})", KEY_PARITY,
				convertParityModeToString(parityMode), parityMode);
		}

	}

	private Integer parityFromConfig(final String configuredParity) {
		if (StringUtils.isNotBlank(configuredParity)) {
			try {
				if (isValidParity(configuredParity)) {
					return validParitiesMap.get(configuredParity
							.toUpperCase());
				} else { // allow literal parity assignment?
					int parsedParityNumber = Integer.parseInt(configuredParity);
					if (isValidParity(parsedParityNumber)) {
						return parsedParityNumber;
					} else {
						log.error(
								"The configured '{}' value is invalid. The value '{}' has to be one of {}.",
								KEY_PARITY, parsedParityNumber,
								validParitiesMap.keySet());
					}
				}
			} catch (NumberFormatException e) {
				log.error("Error parsing config key '{}'. Use one of {}.",
						KEY_PARITY, validParitiesMap.keySet());
			}
		}
		return null;
	}

	/**
	 * calculate baudrate from config String
	 * @param configuredBaudRate
	 * @return baud Rate or null if failed
	 */
	private Integer baudrateFromConfig(final String configuredBaudRate) {
		if (StringUtils.isNotBlank(configuredBaudRate)) {
			try {
				int tmpBaudRate = Integer.parseInt(configuredBaudRate);
				if(validBaudrateMap.contains(tmpBaudRate)) {
					return tmpBaudRate;
				} else {
					log.error(
							"Error parsing config parameter '{}'. Value = {} is not a valid baudrate. Value must be in [75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200]",
							KEY_BAUDRATE, tmpBaudRate);
				}
			} catch (NumberFormatException e) {
				log.error(
						"Error parsing config parameter '{}' to integer. Value = {}",
						KEY_BAUDRATE, configuredBaudRate);
			}
			
		}
		return null;

	}

	/**
	 * Checks if mode is a valid input for 'SerialPort' - class
	 *
	 * @param mode
	 * @return true if valid
	 */
	private boolean isValidParity(int mode) {
		return validParitiesMap.containsValue(mode);
	}

	/**
	 * Checks if mode is a valid input for 'SerialPort' - class
	 *
	 * @param mode
	 * @return true if valid
	 */
	private boolean isValidParity(String mode) {
		return validParitiesMap.containsKey(mode.toUpperCase());
	}

	/**
	 * converts modes integer representation into a readable sting
	 *
	 * @param mode
	 * @return text if mode was valid, otherwise "invalid mode"
	 */
	private String convertParityModeToString(int mode) {
		if (validParitiesMap.containsValue(mode)) {
			for (Entry<String, Integer> parity : validParitiesMap.entrySet()) {
				if (parity.getValue().equals(mode)) {
					return parity.getKey();
				}
			}
		}
		return "invalid mode";
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				processNextLine();
			} catch (CULCommunicationException e) {
				log.error("Serial CUL connection read failed for " + deviceName);
			}
		}
	}

	@Override
	protected void openHardware() throws CULDeviceException {
		log.debug("Opening serial CUL connection for " + deviceName);
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(deviceName);
			if (portIdentifier.isCurrentlyOwned()) {
				throw new CULDeviceException("The port " + deviceName
						+ " is currenty used by "
						+ portIdentifier.getCurrentOwner());
			}
			CommPort port = portIdentifier
					.open(this.getClass().getName(), 2000);
			if (!(port instanceof SerialPort)) {
				throw new CULDeviceException("The device " + deviceName
						+ " is not a serial port");
			}
			serialPort = (SerialPort) port;

			serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, parityMode);
			is = serialPort.getInputStream();
			os = serialPort.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is));
			bw = new BufferedWriter(new OutputStreamWriter(os));

			serialPort.notifyOnDataAvailable(true);
			log.debug("Adding serial port event listener");
			serialPort.addEventListener(this);
		} catch (NoSuchPortException e) {
			throw new CULDeviceException(e);
		} catch (PortInUseException e) {
			throw new CULDeviceException(e);
		} catch (UnsupportedCommOperationException e) {
			throw new CULDeviceException(e);
		} catch (IOException e) {
			throw new CULDeviceException(e);
		} catch (TooManyListenersException e) {
			throw new CULDeviceException(e);
		}

	}

	@Override
	protected void closeHardware() {
		log.debug("Closing serial device " + deviceName);
		if (serialPort != null) {
			serialPort.removeEventListener();
		}
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
			if (serialPort != null) {
				serialPort.close();
			}
		}

	}

	/**
	 * Compares KEY_BAUDRATE and KEY_PARITY to equality in properties map. The
	 * values are not required, so if they're not present, they're handled as
	 * equal, too!
	 */
	@Override
	public boolean arePropertiesEqual(Map<String, ?> properties) {

		if (properties == null) {
			return false;
		}

		
		parityFromConfig(KEY_BAUDRATE);
		
		// check baudrate
		if(properties.containsKey(KEY_BAUDRATE)){
			Integer configured=baudrateFromConfig((String) properties.get(KEY_BAUDRATE));
			if(configured==null) return false;
			if(!configured.equals(baudRate)) return false;
		}
		
		// check parity
		if(properties.containsKey(KEY_PARITY)){
			Integer configured=parityFromConfig((String) properties.get(KEY_PARITY));
			if(configured==null) return false;
			if(!configured.equals(parityMode)) return false;
		}

		return true;
	}

	/**
	 * Check if a key in a map is present and in that case check equality of the
	 * value to {value}.
	 *
	 * @param properties
	 * @param key
	 *            The key of the given properties
	 * @param value
	 *            Has to equal the properties value of key
	 */
	private boolean mapContainsEqualValueByKey(Map<String, ?> properties,
			String key, Integer value) {
		boolean result = true;
		if (properties.containsKey(key)) {
			try {
				int mapValue = Integer.parseInt((String) properties.get(key));
				result = (value == mapValue);
			} catch (NumberFormatException e) {
				result = false;
			}
		}
		return result;
	}
}
