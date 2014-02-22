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
 * @author Günter Speckhofer
 *
 */
public class DmlsMeterReaderImpl implements DmlsMeterReader {
	
	private static final Logger logger = LoggerFactory.getLogger(DmlsMeterReaderImpl.class);
	
	
	
	/** the serial port to use for connecting to the metering device */
    private final String serialPort;
   
	/**  Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms */
    private final int baudRateChangeDelay;

	/**  Enable handling of echos caused by some optical tranceivers */
    private final boolean echoHandling;



	public DmlsMeterReaderImpl(String serialPort, int baudRateChangeDelay,boolean echoHandling) {
		super();
		this.serialPort = serialPort;
		this.baudRateChangeDelay = baudRateChangeDelay;
		this.echoHandling = echoHandling;
	}



	/* (non-Javadoc)
	 * @see org.openhab.binding.dmlsmeter.internal.DmlsMeterReader#read()
	 */
	@Override
	public Map<String, DataSet> read() {
		// the frequently executed code (polling) goes here ...
		Map<String,DataSet> dataSetMap = new HashMap<String, DataSet>();
		
		Connection connection = new Connection(serialPort, echoHandling, baudRateChangeDelay);

		try {
			connection.open();
		} catch (IOException e) {
			logger.error("Failed to open serial port: " + e.getMessage());
			return dataSetMap;
		}

		List<DataSet> dataSets = null;
		try {
			dataSets = connection.read();
			for (DataSet dataSet : dataSets) {
				logger.debug("DataSet: {};{};{}",dataSet.getId(), dataSet.getValue(), dataSet.getUnit());
				dataSetMap.put(dataSet.getId(), dataSet);
			}		
		} catch (IOException e) {
			logger.error("IOException while trying to read: " + e.getMessage());	
		} catch (TimeoutException e) {
			logger.error("Read attempt timed out");
		} finally {
			connection.close();
		}
		
		return dataSetMap;
	}

}
