/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.transport.cul.internal.serial;

import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.transport.cul.CULMode;
import org.openhab.io.transport.cul.internal.CULConfig;
import org.openhab.io.transport.cul.internal.CULConfigFactory;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.SerialPort;

/**
 * Configuration factory for serial device handler implementation.
 *
 * @author Patrick Ruckstuhl
 * @since 1.9.0
 */
public class CULSerialConfigFactory implements CULConfigFactory {
    private final static Logger logger = LoggerFactory.getLogger(CULSerialConfigFactory.class);
    private static final Map<String, Integer> validParitiesMap;
    private static final List<Integer> validBaudrateMap;

    static {
        Map<String, Integer> parities = new HashMap<String, Integer>();
        parities.put("EVEN", SerialPort.PARITY_EVEN);
        parities.put("ODD", SerialPort.PARITY_ODD);
        parities.put("MARK", SerialPort.PARITY_MARK);
        parities.put("NONE", SerialPort.PARITY_NONE);
        parities.put("SPACE", SerialPort.PARITY_SPACE);
        validParitiesMap = Collections.unmodifiableMap(parities);

        Integer baudrates[] = { 75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200 };
        validBaudrateMap = Collections.unmodifiableList(Arrays.asList(baudrates));
    }

    private final static String KEY_BAUDRATE = "baudrate";
    private final static String KEY_PARITY = "parity";

    @Override
    public CULConfig create(String deviceType, String deviceAddress, CULMode mode, Dictionary<String, ?> config)
            throws ConfigurationException {
        int baudRate = 9600;
        final String configuredBaudRate = (String) config.get(KEY_BAUDRATE);
        Integer tmpBaudRate = baudrateFromConfig(configuredBaudRate);
        if (tmpBaudRate != null) {
            baudRate = tmpBaudRate;
            logger.info("Update config, {} = {}", KEY_BAUDRATE, baudRate);
        }

        int parityMode = SerialPort.PARITY_EVEN;
        final String configuredParity = (String) config.get(KEY_PARITY);
        Integer parsedParityNumber = parityFromConfig(configuredParity);
        if (parsedParityNumber != null) {
            parityMode = parsedParityNumber;
            logger.info("Update config, {} = {} ({})", KEY_PARITY, convertParityModeToString(parityMode), parityMode);
        }

        return new CULSerialConfig(deviceType, deviceAddress, mode, baudRate, parityMode);
    }

    private Integer parityFromConfig(final String configuredParity) {
        if (StringUtils.isNotBlank(configuredParity)) {
            try {
                if (isValidParity(configuredParity)) {
                    return validParitiesMap.get(configuredParity.toUpperCase());
                } else { // allow literal parity assignment?
                    int parsedParityNumber = Integer.parseInt(configuredParity);
                    if (isValidParity(parsedParityNumber)) {
                        return parsedParityNumber;
                    } else {
                        logger.error("The configured '{}' value is invalid. The value '{}' has to be one of {}.",
                                KEY_PARITY, parsedParityNumber, validParitiesMap.keySet());
                    }
                }
            } catch (NumberFormatException e) {
                logger.error("Error parsing config key '{}'. Use one of {}.", KEY_PARITY, validParitiesMap.keySet());
            }
        }
        return null;
    }

    /**
     * calculate baudrate from config String
     *
     * @param configuredBaudRate
     * @return baud Rate or null if failed
     */
    private Integer baudrateFromConfig(final String configuredBaudRate) {
        if (StringUtils.isNotBlank(configuredBaudRate)) {
            try {
                int tmpBaudRate = Integer.parseInt(configuredBaudRate);
                if (validBaudrateMap.contains(tmpBaudRate)) {
                    return tmpBaudRate;
                } else {
                    logger.error(
                            "Error parsing config parameter '{}'. Value = {} is not a valid baudrate. Value must be in [75, 110, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200]",
                            KEY_BAUDRATE, tmpBaudRate);
                }
            } catch (NumberFormatException e) {
                logger.error("Error parsing config parameter '{}' to integer. Value = {}", KEY_BAUDRATE,
                        configuredBaudRate);
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

}
