/**
 * 
 */
package org.openhab.binding.dmlsmeter.internal;

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
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.4.0
 */
public class DmlsMeterReaderImpl implements DmlsMeterReader {

	private static final Logger logger = LoggerFactory.getLogger(DmlsMeterReaderImpl.class);

	private final DmlsMeterDeviceConfig config;

	private final String name;

	public DmlsMeterReaderImpl(String name, DmlsMeterDeviceConfig config) {
		this.name = name;
		this.config = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.dmlsmeter.internal.DmlsMeterReader#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.dmlsmeter.internal.DmlsMeterReader#getConfig()
	 */
	@Override
	public DmlsMeterDeviceConfig getConfig() {
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.dmlsmeter.internal.DmlsMeterReader#read()
	 */
	@Override
	public Map<String, DataSet> read() {
		// the frequently executed code (polling) goes here ...
		Map<String, DataSet> dataSetMap = new HashMap<String, DataSet>();

		Connection connection = new Connection(config.getSerialPort(), config.getEchoHandling(), config.getBaudRateChangeDelay());

		try {
			connection.open();
		} catch (IOException e) {
			logger.error("Failed to open serial port {}: {}", config.getSerialPort(), e.getMessage());
			return dataSetMap;
		}

		List<DataSet> dataSets = null;
		try {
			dataSets = connection.read();
			for (DataSet dataSet : dataSets) {
				logger.debug("DataSet: {};{};{}", dataSet.getId(), dataSet.getValue(), dataSet.getUnit());
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
