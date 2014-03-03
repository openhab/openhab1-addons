/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.stiebelheatpump.StiebelHeatPumpBindingProvider;
import org.openhab.binding.stiebelheatpump.protocol.StiebelHeatPumpConnector;
import org.openhab.binding.stiebelheatpump.protocol.StiebelHeatPumpSerialConnector;
import org.openhab.binding.stiebelheatpump.protocol.StiebelHeatPumpSimulator;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class StiebelHeatPumpBinding extends
		AbstractActiveBinding<StiebelHeatPumpBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpBinding.class);

    // configuration defaults for optional properties
	private static int DEFAULT_BAUD_RATE = 57600;
	private static int DEFAULT_SERIAL_TIMEOUT = 5;

	/**
	 * the refresh interval which is used to poll values from the dmlsMeter
	 * server (optional, defaults to 1 Minute)
	 */
	private long refreshInterval = 60 * 1000; // in ms

	/** the serial port to use for connecting to the heat pump*/
	private static String serialPort;

	/** baud rate for the serial port */
	private static int baudRate = DEFAULT_BAUD_RATE;

	/** timeout for the serial port */
	private static int serialTimeout = DEFAULT_SERIAL_TIMEOUT;
		
	private StiebelHeatPumpConnector connector;

	public StiebelHeatPumpBinding() {
	}

	public void activate() {

	}

	public void deactivate() {
		connector = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "stiebelheatpump Refresh Service";
	}
	
	private final StiebelHeatPumpConnector getStiebelHeatPumpConnector() {
		if (connector != null) {
			return connector;
		}
		
		if (System.getProperty("StiebelHeatPumpSimulate") != null) {
			connector = new StiebelHeatPumpSimulator();
		} else if (serialPort != null) {
			connector = new StiebelHeatPumpSerialConnector(serialPort,baudRate);
		}
		return connector;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		
		StiebelHeatPumpConnector connector = getStiebelHeatPumpConnector();
		try {
			connector.connect();
			String version = connector.getHeatPumpVersion();
			for (StiebelHeatPumpBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					eventPublisher.postUpdate(itemName, new StringType(version));					
				}			
			}
			connector.disconnect();
		} catch (StiebelHeatPumpException e) {
		}
		
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		
        serialPort = null;
        baudRate = DEFAULT_BAUD_RATE;
        serialTimeout = DEFAULT_SERIAL_TIMEOUT;
		
		if (config == null || config.isEmpty()) {
			logger.warn("Empty or null configuration. Ignoring.");            	
			return;
		}
        
		if (config != null) {
			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			if (StringUtils.isNotBlank((String) config.get("refresh"))) {
				refreshInterval = Long
						.parseLong((String) config.get("refresh"));
			}
			if (StringUtils.isNotBlank((String) config.get("serialPort"))) {
				serialPort = (String) config.get("serialPort");
			}
			if (StringUtils.isNotBlank((String) config.get("baudRate"))) {
				baudRate = Integer
						.parseInt((String) config.get("baudRate"));
			}
			if (StringUtils.isNotBlank((String) config.get("serialTimeout"))) {
				serialTimeout = Integer
						.parseInt((String) config.get("serialTimeout"));
			}

			setProperlyConfigured(true);
			
		}		
	}
}
