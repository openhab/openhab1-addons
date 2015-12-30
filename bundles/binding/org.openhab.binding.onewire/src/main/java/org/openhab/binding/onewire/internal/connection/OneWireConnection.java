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
import org.openhab.binding.onewire.internal.deviceproperties.AbstractOneWireDevicePropertyBindingConfig;
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

	private static final Logger logger = LoggerFactory.getLogger(OneWireConnection.class);

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
				logger.info("Connected to owserver [IP '" + cvIp + "' Port '" + cvPort + "']");
				cvIsEstablished = true;
				return true;
			} catch (Exception exception) {
				logger.error("Couldn't connect to owserver [IP '" + cvIp + "' Port '" + cvPort + "']: ", exception.getLocalizedMessage());
				cvIsEstablished = false;
				return false;
			}
		} else {
			logger.warn("Couldn't connect to owserver because of missing connection parameters [IP '{}' Port '{}'].", cvIp, cvPort);
			return false;
		}
	}

	/**
	 * Reconnects to owserver
	 * 
	 * @return
	 */
	public static synchronized boolean reconnect() {
		logger.info("Trying to reconnect to owserver...");
		try {
			cvOwConnection.disconnect();
		} catch (Exception lvException) {
			logger.error("Error while disconnecting from owserver: " + lvException, lvException);
		}
		cvOwConnection = null;
		return connect();
	}

	public static synchronized void updated(Dictionary<String, ?> pvConfig) throws ConfigurationException {
		if (pvConfig != null) {
			logger.debug("OneWire configuration present. Setting up owserver connection.");
			cvIp = (String) pvConfig.get("ip");

			String lvPortConfig = (String) pvConfig.get("port");
			if (StringUtils.isNotBlank(lvPortConfig)) {
				cvPort = Integer.parseInt(lvPortConfig);
			}

			String lvTempScaleString = (String) pvConfig.get("tempscale");
			if (StringUtils.isNotBlank(lvTempScaleString)) {
				try {
					cvTempScale = OwTemperatureScale.valueOf(lvTempScaleString);
				} catch (IllegalArgumentException iae) {
					String lvFehlertext = "Unknown temperature scale '" + lvTempScaleString + "'. Valid values are CELSIUS, FAHRENHEIT, KELVIN or RANKINE.";
					logger.error(lvFehlertext,iae);
					throw new ConfigurationException("onewire:tempscale", lvFehlertext);
				}
			}

			String lvRetryString = (String) pvConfig.get("retry");
			if (StringUtils.isNotBlank(lvRetryString)) {
				cvRetry = Integer.parseInt(lvRetryString);
			}

			if (cvOwConnection == null) {
				logger.debug("Not connected to owserver yet. Trying to connect...");
				if (!connect()) {
					logger.warn("Inital connection to owserver failed!");
				} else {
					logger.debug("Success: connected to owserver.");
				}
			}
		} else {
			logger.info("OneWireBinding configuration is not present. Please check your configuration file or if not needed remove the OneWireBinding addon.");
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
		logger.debug("check if device exisits '{}': ", new Object[] { lvDevicePath });

		return OneWireConnection.getConnection().exists(lvDevicePath);
	}

	/**
	 * Read a Value for a device property from 1-Wire network
	 * 
	 * @param pvDevicePropertyPath
	 * @return device property value as String
	 */
	public static synchronized String readFromOneWire(AbstractOneWireDevicePropertyBindingConfig pvBindingConfig) {
		String lvDevicePropertyPath = pvBindingConfig.getDevicePropertyPath();

		int lvAttempt = 1;
		while (lvAttempt <= cvRetry) {
			try {
				logger.debug("trying to read from '{}', read attempt={}", new Object[] { lvDevicePropertyPath, lvAttempt });
				if (checkIfDeviceExists(lvDevicePropertyPath)) {
					String lvReadValue = OneWireConnection.getConnection().read(lvDevicePropertyPath);
					logger.debug("Read value '{}' from {}, read attempt={}", new Object[] { lvReadValue, lvDevicePropertyPath, lvAttempt });

					// Test
					if (pvBindingConfig.isIgnore85CPowerOnResetValues()) {
						double lvReadDouble = Double.parseDouble(lvReadValue);
						if (lvReadDouble == 85.0) {
							logger.debug("reading from path " + lvDevicePropertyPath + " attempt " + lvAttempt + " Ignoring 85°C value");
						} else {
							return lvReadValue;
						}
					} else {
						return lvReadValue;
					}
				} else {
					logger.info("there is no device for path {}, read attempt={}", new Object[] { lvDevicePropertyPath, lvAttempt });
				}
			} catch (OwfsException oe) {
				String lvLogText = "reading from path " + lvDevicePropertyPath + " attempt " + lvAttempt + " throws exception";
				if (pvBindingConfig.isIgnoreReadErrors()) {
					logger.debug(lvLogText, oe);
				} else {
					logger.error(lvLogText, oe);
					reconnect();
				}
			} catch (IOException ioe) {
				logger.error("couldn't establish network connection while read attempt " + lvAttempt + " '" + lvDevicePropertyPath + "' ip:port=" + cvIp + ":" + cvPort, ioe);
				reconnect();
			} catch (NumberFormatException lvNumberFormatException) {
				logger.error("Ignoring 85C PowerOnReset values can only be used with temperature sensors! Read a value, which is not a number");
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
				logger.debug("trying to write '{}' to '{}', write attempt={}", new Object[] { pvValue, pvDevicePropertyPath, lvAttempt });
				if (checkIfDeviceExists(pvDevicePropertyPath)) {
					OneWireConnection.getConnection().write(pvDevicePropertyPath, pvValue);
					return; // Success, exit
				} else {
					logger.info("there is no device for path {}, write attempt={}", new Object[] { pvDevicePropertyPath, lvAttempt });
				}
			} catch (OwfsException oe) {
				logger.error("writing " + pvValue + " to path " + pvDevicePropertyPath + " attempt " + lvAttempt + " throws exception", oe);
				reconnect();
			} catch (IOException ioe) {
				logger.error("couldn't establish network connection while write attempt " + lvAttempt + " to '" + pvDevicePropertyPath + "' ip:port=" + cvIp + ":" + cvPort, ioe);
				reconnect();
			} finally {
				lvAttempt++;
			}
		}
	}
}
