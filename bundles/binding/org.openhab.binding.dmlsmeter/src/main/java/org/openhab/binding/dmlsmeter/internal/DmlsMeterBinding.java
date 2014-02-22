/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmlsmeter.internal;

import java.util.Dictionary;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeoutException;

import org.openhab.binding.dmlsmeter.DmlsMeterBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
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
public class DmlsMeterBinding extends AbstractActiveBinding<DmlsMeterBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(DmlsMeterBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the dmlsMeter
	 * server (optional, defaults to 1 Minute)
	 */
	private long refreshInterval = 60*1000; // in ms
	
	/** the serial port to use for connecting to the metering device */
    private static String serialPort;
   
	/**  Delay of baud rate change in ms. Default is 0. USB to serial converters often require a delay of up to 250ms */
    private static int baudRateChangeDelay = 0;

	/**  Enable handling of echos caused by some optical tranceivers */
    private static boolean echoHandling = true;
    
	public DmlsMeterBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "dmlsMeter Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		
		DmlsMeterReader dmlsMeterReader = new DmlsMeterReader(serialPort, baudRateChangeDelay, echoHandling);
		List<DataSet> dataSets =  dmlsMeterReader.query();
		
		if (dataSets == null) return;
		
		for (DmlsMeterBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String obis = provider.getObis(itemName);
				if (obis != null) {					
					DataSet dataSet = getDataSetByObis(dataSets, obis);
					if(dataSet != null){
						String value = dataSet.getValue();
						eventPublisher.postUpdate(itemName, new DecimalType(value));
					}
				}
			}
		}		
	}
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			if (StringUtils.isNotBlank((String) config.get("refresh"))) {
				refreshInterval = Long.parseLong((String) config.get("refresh"));
			}
			
			// get connection configuration from openhab config file 
			serialPort = (String) config.get("serialPort");
			
			if (serialPort == null) {
				throw new ConfigurationException("serialPort", "SerialPort is not configured!");
			}
			
		
			// to overwrote the default baud rate change delay
			if (StringUtils.isNotBlank((String) config.get("baudRateChangeDelay"))) {
				baudRateChangeDelay = Integer.parseInt((String) config.get("baudRateChangeDelay"));
			}
			
			// to overwrite the default echoHandling
			if (StringUtils.isNotBlank((String) config.get("echoHandling"))) {
				echoHandling = Boolean.parseBoolean((String) config.get("echoHandling"));
			}
			
			setProperlyConfigured(true);
		}
	}

	/**
	 * get a DataSet in a List of DataSet which represents an obis
	 * @obis to be searched
	 * @return a DataSet of the corresponding obis
	 */
	private DataSet getDataSetByObis(List<DataSet> dataSets, String obis) {

		for (DataSet dataSet : dataSets) {
			if(dataSet.getId() == obis){
				
			}
		}
		return null;
	}
}

