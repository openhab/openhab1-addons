/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.iec6205621meter.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.openmuc.j62056.Connection;
import org.openmuc.j62056.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a serial named meter with its communication configuration.
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.5.0
 */
public class Meter {

	private static final Logger logger = LoggerFactory
			.getLogger(Meter.class);

	private final MeterConfig config;

	private final String name;

	public Meter(String name, MeterConfig config) {
		this.name = name;
		this.config = config;
	}

	/**
	 * Return the name of meter
	 * 
	 * @return the name of meter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the configuration of this meter
	 * 
	 * @return the meter configuration
	 */
	public MeterConfig getConfig() {
		return config;
	}

	/**
	 * Reads data from meter
	 * 
	 * @return a map of DataSet objects with the obis as key.
	 */
	public Map<String, DataSet> read() {
		// the frequently executed code (polling) goes here ...
		Map<String, DataSet> dataSetMap = new HashMap<String, DataSet>();

		Connection connection = new Connection(config.getSerialPort(),
				config.getEchoHandling(), config.getBaudRateChangeDelay());

		try {
			connection.open();
		} catch (IOException e) {
			logger.error("Failed to open serial port {}: {}",
					config.getSerialPort(), e.getMessage());
			return dataSetMap;
		}

		List<DataSet> dataSets = null;
		try {
			dataSets = connection.read();
			for (DataSet dataSet : dataSets) {
				logger.debug("DataSet: {};{};{}", dataSet.getId(),
						dataSet.getValue(), dataSet.getUnit());
				dataSetMap.put(dataSet.getId(), dataSet);
			}
		} catch (IOException e) {
			logger.error("IOException while trying to read: {}", e.getMessage());
		} catch (TimeoutException e) {
			logger.error("Read attempt timed out");
		} finally {
			connection.close();
		}

		return dataSetMap;
	}

}
