/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmlsmeter.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmuc.j62056.DataSet;
import org.openmuc.j62056.Connection;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class DmlsMeterReader {

	private static final Logger logger = 
		LoggerFactory.getLogger(DmlsMeterBinding.class);

	
	/** the serial port to use for connecting to the metering device */
    private String serialPort;
   
	/**  Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms */
    private int baudRateChangeDelay = 0;

	/**  Enable handling of echos caused by some optical tranceivers */
    private boolean echoHandling = true;
    
	public DmlsMeterReader(String serialPort, int baudRateChangeDelay, boolean echoHandling ) {
		this.serialPort = serialPort;
		this.baudRateChangeDelay = baudRateChangeDelay;
		this.echoHandling = echoHandling;
	}		
	
	/**
	 * query  the data from the metering device
	 */
	public List<DataSet> query() {
		// the frequently executed code (polling) goes here ...
		logger.debug("query data from meter");
		List<DataSet> dataSets = null;
		Connection connection = new Connection(serialPort, echoHandling, baudRateChangeDelay);
		
		try {
			connection.open();
		} catch (IOException e) {
			logger.error("Failed to open serial port: " + e.getMessage());
		}
		catch (UnsatisfiedLinkError e)
		{
			logger.error("Native Library missing: " + e.getMessage());
		}

		try {
			dataSets = connection.read();
		} catch (IOException e) {
			logger.error("IOException while trying to read: " + e.getMessage());
			connection.close();
		} catch (TimeoutException e) {
			logger.error("Read attempt timed out");
			connection.close();
		}
		
		if (dataSets == null) return null;
		
		Iterator<DataSet> dataSetIt = dataSets.iterator();

		// print data sets on the following lines
		while (dataSetIt.hasNext()) {
			DataSet dataSet = dataSetIt.next();
			logger.debug(dataSet.getId() + ";" + dataSet.getValue() + ";" + dataSet.getUnit());
		}

		connection.close();
		return dataSets;
	}
}

