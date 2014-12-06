/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.stiebelheatpump.StiebelHeatPumpBindingProvider;
import org.openhab.binding.stiebelheatpump.protocol.DataParser;
import org.openhab.binding.stiebelheatpump.protocol.ProtocolConnector;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition;
import org.openhab.binding.stiebelheatpump.protocol.Request;
import org.openhab.binding.stiebelheatpump.protocol.SerialConnector;
import org.openhab.binding.stiebelheatpump.protocol.RecordDefinition.Type;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stiebel heat pump binding implementation.
 * 
 * @author Peter Kreutzer
 * @since 1.5.0
 */
public class StiebelHeatPumpBinding extends
		AbstractActiveBinding<StiebelHeatPumpBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpBinding.class);

	// configuration defaults for optional properties
	private static int DEFAULT_BAUD_RATE = 9600;
	private static int DEFAULT_SERIAL_TIMEOUT = 5;

	/**
	 * the refresh interval which is used to poll values from the heat pump
	 * server (optional, defaults to 1 Minute)
	 */
	private long refreshInterval = 60000; // in ms

	/** the serial port to use for connecting to the heat pump */
	private String serialPort;

	/** baud rate for the serial port */
	private int baudRate = DEFAULT_BAUD_RATE;

	/** timeout for the serial port */
	private int serialTimeout = DEFAULT_SERIAL_TIMEOUT;

	/** version number of heat pump */
	private String version = "";

	/** heat pump request definition */
	private List<Request> heatPumpConfiguration = new ArrayList<Request>();
	Request versionRequest;
	
	public StiebelHeatPumpBinding() {
	}

	public void activate() {

	}

	public void deactivate() {
	
	}

	/**
	 * @{inheritDoc
	 */
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	protected String getName() {
		return "stiebelheatpump Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	protected void execute() {
		logger.debug("Refresh heat pump sensor and status values ...");
		try {
			CommunicationService communicationService = new CommunicationService(
					serialPort, baudRate, heatPumpConfiguration);
			Map<String, String> data = new HashMap<String, String>();
			
			data = communicationService.getStatus();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				logger.info("Data {} has value {}", entry.getKey(),
						entry.getValue());
			}
			publishValues(data);
			data = communicationService.getSensors();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				logger.info("Data {} has value {}", entry.getKey(),
						entry.getValue());
			}
			publishValues(data);

			communicationService.finalizer();
			
		} catch (StiebelHeatPumpException e) {
			logger.error("Could not read data from heat pump! "
					+ e.toString());
		}
	}

	/**
	 * @{inheritDoc
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("Received command {} for item {}", command, itemName);
		for (StiebelHeatPumpBindingProvider provider : providers) {
			for (String name : provider.getItemNames()) {
				if (!name.equals(itemName)) {
					continue;
				}
				String parameter = provider.getParameter(name);
				logger.debug(
						"Found item {} with heat pump parameter {} in providers",
						itemName, parameter);
				//try {
					//setStateOnHeatPump(command, parameter);
				//} catch (StiebelHeatPumpException e) {
				//	logger.error("could not set new value!");
				//}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		serialPort = null;
		baudRate = DEFAULT_BAUD_RATE;
		serialTimeout = DEFAULT_SERIAL_TIMEOUT;

		logger.debug("Loading stiebelheatpump binding configuration.");

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
				baudRate = Integer.parseInt((String) config.get("baudRate"));
			}
			if (StringUtils.isNotBlank((String) config.get("serialTimeout"))) {
				serialTimeout = Integer.parseInt((String) config
						.get("serialTimeout"));
			}
			if (StringUtils.isNotBlank((String) config.get("version"))) {
				version = (String) config.get("version");
			}

			boolean isInitialized = getInitialHeatPumpData();

			logger.info(
					"Created heatpump configuration with  serialport:{}, baudrate:{}, version:{} ",
					serialPort, baudRate, version);

			setProperlyConfigured(isInitialized);
		}
	}

	/**
	 * This method reads initially all information from the heat pump
	 * 
	 * @return true if heat pump information could be successfully read
	 */
	private boolean getInitialHeatPumpData() {
		try {
			CommunicationService communicationService = new CommunicationService(
					serialPort, baudRate);
			Map<String, String> data = new HashMap<String, String>();

			heatPumpConfiguration =  communicationService.getHeatPumpConfiguration(version + ".xml");
			String version = communicationService.getversion();
			logger.info("Heat pump has version {}", version);

			data = communicationService.getSettings();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				logger.info("Data {} has value {}", entry.getKey(),
						entry.getValue());
			}
			publishValues(data);

			data = communicationService.getStatus();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				logger.info("Data {} has value {}", entry.getKey(),
						entry.getValue());
			}
			publishValues(data);
			data = communicationService.getSensors();
			for (Map.Entry<String, String> entry : data.entrySet()) {
				logger.info("Data {} has value {}", entry.getKey(),
						entry.getValue());
			}
			publishValues(data);
			
			communicationService.setTime();
			
			communicationService.finalizer();
			
			return true;
		} catch (StiebelHeatPumpException e) {
			logger.error("Stiebel heatpump version could not be read from heat pump! "
					+ e.toString());
		}

		return false;
	}

	/**
	 * This method publishes all values on the event bus
	 * 
	 * @param heatPumpData
	 *            as map of provider parameter and value
	 */
	private void publishValues(Map<String, String> heatPumpData) {
		for (StiebelHeatPumpBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String parameter = provider.getParameter(itemName);
				if (parameter != null && heatPumpData.containsKey(parameter)) {
					String heatpumpValue = heatPumpData.get(parameter);
					Class<? extends Item> itemType = provider
							.getItemType(itemName);
					if (itemType.isAssignableFrom(NumberItem.class)) {
						eventPublisher.postUpdate(itemName, new DecimalType(
								heatpumpValue));
					}
					if (itemType.isAssignableFrom(StringItem.class)) {
						String value = heatpumpValue;
						eventPublisher.postUpdate(itemName, new StringType(
								value));
					}
				}
			}
		}
	}

}
