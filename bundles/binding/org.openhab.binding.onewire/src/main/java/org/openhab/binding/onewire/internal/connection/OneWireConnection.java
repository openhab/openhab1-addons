/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.connection;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.owfs.jowfsclient.Enums.OwBusReturn;
import org.owfs.jowfsclient.Enums.OwPersistence;
import org.owfs.jowfsclient.Enums.OwTemperatureScale;
import org.owfs.jowfsclient.OwfsConnection;
import org.owfs.jowfsclient.OwfsConnectionConfig;
import org.owfs.jowfsclient.OwfsConnectionFactory;
import org.owfs.jowfsclient.OwfsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class establishes the connection to the 1-Wire-bus.
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireConnection {

	private static final Logger LOGGER = LoggerFactory.getLogger(OneWireConnection.class);

	/**
	 * Connection to the owserver server
	 */
	private static OwfsConnection cvOwConnection = null;

	/**
	 * ip of the owserver (must be set in obenHab.cfg)
	 */
	private static String cvIp = null;

	/**
	 * port of the owserver (can be set in obenHab.cfg)
	 */
	private static int cvPort = 4304;

	/**
	 * Default TempScale is Celsius (can be set in obenHab.cfg)
	 */
	private static OwTemperatureScale cvTempScale = OwTemperatureScale.CELSIUS;

	/**
	 * the retry count in case no valid value was returned upon read (optional, defaults to 3)
	 */
	private static int cvRetry = 3;

	/**
	 * signals that the connection is established
	 */
	private static boolean cvIsEstablished = false;

	/**
	 * Returns a OwfsConnection
	 * 
	 * @return the OwfsConnection network link
	 */
	public static synchronized OwfsConnection getConnection() {
		if (cvOwConnection == null) {
			if (!connect()) {
				return null;
			}
		}
		return cvOwConnection;
	}

	/**
	 * Tries to connect either by IP or serial bus, depending on supplied config data.
	 * 
	 * @return true if connection was established, false otherwise
	 */
	public static synchronized boolean connect() {
		if (cvIp != null && cvPort > 0) {
			OwfsConnectionFactory owfsConnectorFactory = new OwfsConnectionFactory(cvIp, cvPort);
			OwfsConnectionConfig owConnectionConfig = new OwfsConnectionConfig(cvIp, cvPort);
			owConnectionConfig.setTemperatureScale(cvTempScale);
			owConnectionConfig.setPersistence(OwPersistence.ON);
			owConnectionConfig.setBusReturn(OwBusReturn.ON);
			owfsConnectorFactory.setConnectionConfig(owConnectionConfig);

			try {
				cvOwConnection = owfsConnectorFactory.createNewConnection();
				cvOwConnection.listDirectory("/");
				LOGGER.info("Connected to owserver [IP '" + cvIp + "' Port '" + cvPort + "']");
				cvIsEstablished = true;
				return true;
			} catch (Exception exception) {
				LOGGER.error("Couldn't connect to owserver [IP '" + cvIp + "' Port '" + cvPort + "']: ", exception.getLocalizedMessage());
				cvIsEstablished = false;
				return false;
			}
		} else {
			LOGGER.warn("Couldn't connect to owserver because of missing connection parameters [IP '{}' Port '{}'].", cvIp, cvPort);
			return false;
		}
	}

	/**
	 * Reconnects to owserver
	 * 
	 * @return
	 */
	public static synchronized boolean reconnect() {
		LOGGER.info("Trying to reconnect to owserver...");
		try {
			cvOwConnection.disconnect();
		} catch (Exception lvException) {
			LOGGER.error("Error while disconnecting from owserver: " + lvException, lvException);
		}
		cvOwConnection = null;
		return connect();
	}

	public static synchronized void updated(Dictionary<String, ?> pvConfig) throws ConfigurationException {
		if (pvConfig != null) {
			LOGGER.debug("OneWire configuration present. Setting up owserver connection.");
			cvIp = (String) pvConfig.get("ip");

			String lvPortConfig = (String) pvConfig.get("port");
			if (StringUtils.isNotBlank(lvPortConfig)) {
				cvPort = Integer.parseInt(lvPortConfig);
			}

			String lvTempScaleString = (String) pvConfig.get("tempscale");
			if (StringUtils.isNotBlank(lvTempScaleString)) {
				try {
					cvTempScale = OwTemperatureScale.valueOf("tempScaleString");
				} catch (IllegalArgumentException iae) {
					throw new ConfigurationException("onewire:tempscale", "Unknown temperature scale '" + lvTempScaleString + "'. Valid values are CELSIUS, FAHRENHEIT, KELVIN or RANKIN.");
				}
			}

			String lvRetryString = (String) pvConfig.get("retry");
			if (StringUtils.isNotBlank(lvRetryString)) {
				cvRetry = Integer.parseInt(lvRetryString);
			}

			if (cvOwConnection == null) {
				LOGGER.debug("Not connected to owserver yet. Trying to connect...");
				if (!connect()) {
					LOGGER.warn("Inital connection to owserver failed!");
				} else {
					LOGGER.debug("Success: connected to owserver.");
				}
			}
		} else {
			LOGGER.info("OneWireBinding configuration is not present. Please check your configuration file or if not needed remove the OneWireBinding addon.");
		}
	}

	/**
	 * @return boolean, is the connection to oserver established
	 */
	public static boolean isConnectionEstablished() {
		return cvIsEstablished;
	}

	/**
	 * Checks if an device exists in 1-Wire network
	 * 
	 * @param pvDevicePropertyPath
	 * @return
	 * @throws IOException
	 * @throws OwfsException
	 */
	private static synchronized boolean checkIfDeviceExists(String pvDevicePropertyPath) throws IOException, OwfsException {
		String[] pvDevicePropertyPathParts = pvDevicePropertyPath.trim().split("/");

		String lvDevicePath = pvDevicePropertyPathParts[0];
		LOGGER.debug("check if device exisits '{}': ", new Object[] { lvDevicePath });

		return OneWireConnection.getConnection().exists(lvDevicePath);
	}

	/**
	 * Read a Value for a device property from 1-Wire network
	 * 
	 * @param pvDevicePropertyPath
	 * @return device property value as String
	 */
	public static synchronized String readFromOneWire(String pvDevicePropertyPath) {		
		int lvAttempt = 1;
		while (lvAttempt <= cvRetry) {
			try {
				LOGGER.debug("trying to read from '{}', read attempt={}", new Object[] { pvDevicePropertyPath, lvAttempt });
				if (checkIfDeviceExists(pvDevicePropertyPath)) {
					String lvReadValue = OneWireConnection.getConnection().read(pvDevicePropertyPath);
					LOGGER.debug("Read value '{}' from {}, read attempt={}", new Object[] { lvReadValue, pvDevicePropertyPath, lvAttempt });
					return lvReadValue;
				} else {
					LOGGER.info("there is no device for path {}, read attempt={}", new Object[] { pvDevicePropertyPath, lvAttempt });
				}
			} catch (OwfsException oe) {
				LOGGER.error("reading from path " + pvDevicePropertyPath + " attempt " + lvAttempt + " throws exception", oe);
				reconnect();
			} catch (IOException ioe) {
				LOGGER.error("couldn't establish network connection while read attempt " + lvAttempt + " '" + pvDevicePropertyPath + "' ip:port=" + cvIp + ":" + cvPort, ioe);
				reconnect();
			} finally {
				lvAttempt++;
			}
		}

		return null;
	}

	/**
	 * Writes String to 1-Wire device property
	 * 
	 * @param pvDevicePropertyPath
	 * @param pvValue
	 */
	public static synchronized void writeToOneWire(String pvDevicePropertyPath, String pvValue) {
		int lvAttempt = 1;
		while (lvAttempt <= cvRetry) {
			try {
				LOGGER.debug("trying to write '{}' to '{}', write attempt={}", new Object[] { pvValue, pvDevicePropertyPath, lvAttempt });
				if (checkIfDeviceExists(pvDevicePropertyPath)) {
					OneWireConnection.getConnection().write(pvDevicePropertyPath, pvValue);
					return; // Success, exit
				} else {
					LOGGER.info("there is no device for path {}, write attempt={}", new Object[] { pvDevicePropertyPath, lvAttempt });
				}
			} catch (OwfsException oe) {
				LOGGER.error("writing " + pvValue + " to path " + pvDevicePropertyPath + " attempt " + lvAttempt + " throws exception", oe);
				reconnect();
			} catch (IOException ioe) {
				LOGGER.error("couldn't establish network connection while write attempt " + lvAttempt + " to '" + pvDevicePropertyPath + "' ip:port=" + cvIp + ":" + cvPort, ioe);
				reconnect();
			} finally {
				lvAttempt++;
			}
		}
	}
}
