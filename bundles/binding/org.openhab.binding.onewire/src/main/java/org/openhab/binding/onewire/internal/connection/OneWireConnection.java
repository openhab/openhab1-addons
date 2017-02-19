/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.connection;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Objects;

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
 * @author Chris Carman (added server connection retry logic)
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
     * The number of retries that will be attempted after a failed connection attempt.
     * Optional, defaults to 3. 0 means no retries will be attempted.
     */
    private static int cvServerRetries = 3;

    /**
     * The time to wait between connection attempts. Optional, defaults to 60 seconds.
     * May not be less than 5 seconds.
     */
    private static int cvServerRetryInterval = 60;

    /**
     * signals that the connection is established
     */
    private static boolean cvIsEstablished = false;

    /**
     * Returns an OwfsConnection
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
        OwfsConnectionFactory owfsConnectorFactory = new OwfsConnectionFactory(cvIp, cvPort);
        OwfsConnectionConfig owConnectionConfig = new OwfsConnectionConfig(cvIp, cvPort);
        owConnectionConfig.setTemperatureScale(cvTempScale);
        owConnectionConfig.setPersistence(OwPersistence.ON);
        owConnectionConfig.setBusReturn(OwBusReturn.ON);
        owfsConnectorFactory.setConnectionConfig(owConnectionConfig);

        cvOwConnection = owfsConnectorFactory.createNewConnection();

        boolean connected = false;
        int attempts = 0, retriesRemaining = cvServerRetries;
        List<String> result = null;

        try {
            result = cvOwConnection.listDirectory("/");
            if (result != null) {
                connected = true;
            } else {
                cvIsEstablished = false;
            }
        } catch (OwfsException oe) {
            logger.warn("Unexpected owfs exception: {}", oe.getMessage(), oe);
        } catch (IOException e) {
            logger.warn("Unexpected connection failure.", e);
        }

        while (!connected && retriesRemaining > 0) {
            logger.warn("Connection failed. Will retry in {} seconds.", cvServerRetryInterval);
            synchronized (cvOwConnection) {
                try {
                    cvOwConnection.wait(cvServerRetryInterval * 1000L);
                } catch (InterruptedException e) {
                    logger.debug("Wait was interrupted.");
                }
            }
            attempts++;
            retriesRemaining--;
            logger.info("Retrying failed connection... Attempt {} of {}.", attempts, cvServerRetries);
            try {
                result = cvOwConnection.listDirectory("/");
                if (result != null) {
                    connected = true;
                }
            } catch (OwfsException oe) {
                logger.warn("Unexpected owfs exception: {}", oe.getMessage(), oe);
            } catch (IOException e) {
                logger.warn("Unexpected connection failure.", e);
            }
        }

        if (!connected) {
            logger.error("Couldn't connect to owserver [IP '{}' Port '{}']", cvIp, cvPort);
            cvIsEstablished = false;
            return false;
        }

        logger.info("Connected to owserver [IP '{}' Port '{}']", cvIp, cvPort);
        cvIsEstablished = true;
        return true;
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
            logger.error("Error while disconnecting from owserver: ", lvException);
        }
        cvOwConnection = null;
        cvIsEstablished = false;
        return connect();
    }

    public static synchronized void updated(Dictionary<String, ?> pvConfig) throws ConfigurationException {
        if (pvConfig == null) {
            logger.debug(
                    "OneWireBinding configuration is not present. Please check your configuration file or if not needed remove the OneWireBinding addon.");
            return;
        }

        logger.debug("OneWire configuration present. Setting up owserver connection.");
        cvIp = Objects.toString(pvConfig.get("ip"), null);
        if (StringUtils.isBlank(cvIp)) {
            logger.error("owserver IP address was configured as an empty string.");
            throw new ConfigurationException("onewire:ip", "owserver IP address was configured as an empty string.");
        }

        String lvPortConfig = Objects.toString(pvConfig.get("port"), null);
        if (StringUtils.isNotBlank(lvPortConfig)) {
            cvPort = Integer.parseInt(lvPortConfig);
        }
        if (cvPort < 1) {
            logger.error("owserver port was configured with an invalid value: {}", cvPort);
            throw new ConfigurationException("onewire:port",
                    "owserver port was configured with an invalid value: " + cvPort);
        }
        logger.debug("owserver ip:port = {}:{}", cvIp, cvPort);

        String lvTempScaleString = Objects.toString(pvConfig.get("tempscale"), null);
        if (StringUtils.isNotBlank(lvTempScaleString)) {
            try {
                cvTempScale = OwTemperatureScale.valueOf(lvTempScaleString);
            } catch (IllegalArgumentException iae) {
                String lvFehlertext = "Unknown temperature scale '" + lvTempScaleString
                        + "'. Valid values are CELSIUS, FAHRENHEIT, KELVIN or RANKINE.";
                logger.error(lvFehlertext, iae);
                throw new ConfigurationException("onewire:tempscale", lvFehlertext);
            }
        }

        String lvRetryString = Objects.toString(pvConfig.get("retry"), null);
        if (StringUtils.isNotBlank(lvRetryString)) {
            cvRetry = Integer.parseInt(lvRetryString);
        }
        logger.debug("onewire:retry = {}", cvRetry);

        String lvServerRetries = Objects.toString(pvConfig.get("server_retries"), null);
        if (StringUtils.isNotBlank(lvServerRetries)) {
            cvServerRetries = Integer.parseInt(lvServerRetries);
        }
        logger.debug("onewire:server_retries = {}", cvServerRetries);

        String lvRetryIntervalString = Objects.toString(pvConfig.get("server_retryInterval"), null);
        if (StringUtils.isNotBlank(lvRetryIntervalString)) {
            cvServerRetryInterval = Integer.parseInt(lvRetryIntervalString);
            if (cvServerRetryInterval < 5 && cvServerRetryInterval > 0) {
                logger.info("server_retryInterval was set to {}. Using the minimum allowed value of 5 instead.",
                        cvServerRetryInterval);
                cvServerRetryInterval = 5;
            }
        }
        logger.debug("onewire:server_retryInterval = {} seconds", cvServerRetryInterval);

        if (cvOwConnection == null) {
            logger.debug("Not connected to owserver yet. Trying to connect...");
            if (!connect()) {
                logger.warn("Connection to owserver failed!");
            } else {
                logger.debug("Success: connected to owserver.");
            }
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
    private static synchronized boolean checkIfDeviceExists(String pvDevicePropertyPath)
            throws IOException, OwfsException {
        String[] pvDevicePropertyPathParts = pvDevicePropertyPath.trim().split("/");

        String lvDevicePath = pvDevicePropertyPathParts[0];
        logger.debug("check if device exists '{}': ", new Object[] { lvDevicePath });

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
                logger.debug("trying to read from '{}', read attempt={}",
                        new Object[] { lvDevicePropertyPath, lvAttempt });
                if (checkIfDeviceExists(lvDevicePropertyPath)) {
                    String lvReadValue = OneWireConnection.getConnection().read(lvDevicePropertyPath);
                    logger.debug("Read value '{}' from {}, read attempt={}",
                            new Object[] { lvReadValue, lvDevicePropertyPath, lvAttempt });

                    // Test
                    if (pvBindingConfig.isIgnore85CPowerOnResetValues()) {
                        double lvReadDouble = Double.parseDouble(lvReadValue);
                        if (lvReadDouble == 85.0) {
                            logger.debug("reading from path '{}' attempt {}. Ignoring 85C value", lvDevicePropertyPath,
                                    lvAttempt);
                        } else {
                            return lvReadValue;
                        }
                    } else {
                        return lvReadValue;
                    }
                } else {
                    logger.info("there is no device for path {}, read attempt={}",
                            new Object[] { lvDevicePropertyPath, lvAttempt });
                }
            } catch (OwfsException oe) {
                String lvLogText = "reading from path " + lvDevicePropertyPath + " attempt " + lvAttempt
                        + " throws exception";
                if (pvBindingConfig.isIgnoreReadErrors()) {
                    logger.debug(lvLogText, oe);
                } else {
                    logger.error(lvLogText, oe);
                    reconnect();
                }
            } catch (IOException ioe) {
                logger.error("couldn't establish network connection while read attempt {} '{}' ip:port={}:{}",
                        lvAttempt, lvDevicePropertyPath, cvIp, cvPort, ioe);
                reconnect();
            } catch (NumberFormatException lvNumberFormatException) {
                logger.error(
                        "Ignoring 85C PowerOnReset values can only be used with temperature sensors! Read a value, which is not a number");
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
                logger.debug("Trying to write '{}' to '{}', write attempt={}", pvValue, pvDevicePropertyPath,
                        lvAttempt);
                if (checkIfDeviceExists(pvDevicePropertyPath)) {
                    OneWireConnection.getConnection().write(pvDevicePropertyPath, pvValue);
                    return; // Success, exit
                } else {
                    logger.info("There is no device for path {}, write attempt={}", pvDevicePropertyPath, lvAttempt);
                }
            } catch (OwfsException oe) {
                logger.error("Writing {} to path {} attempt {} threw an exception", pvValue, pvDevicePropertyPath,
                        lvAttempt, oe);
                reconnect();
            } catch (IOException ioe) {
                logger.error("Couldn't establish network connection while write attempt {} to '{}' ip:port={}:{}",
                        lvAttempt, pvDevicePropertyPath, cvIp, cvPort, ioe);
                reconnect();
            } finally {
                lvAttempt++;
            }
        }
    }
}
