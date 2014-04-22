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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.stiebelheatpump.StiebelHeatPumpBindingProvider;
import org.openhab.binding.stiebelheatpump.protocol.Request;
import org.openhab.binding.stiebelheatpump.protocol.StiebelHeatPumpConnector;
import org.openhab.binding.stiebelheatpump.protocol.StiebelHeatPumpSerialConnector;
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

	/** the serial port to use for connecting to the heat pump */
	private static String serialPort;

	/** baud rate for the serial port */
	private static int baudRate = DEFAULT_BAUD_RATE;

	/** timeout for the serial port */
	private static int serialTimeout = DEFAULT_SERIAL_TIMEOUT;

	/** version number of heat pump */
	private static String version = "";

	/** heat pump request definition */
	private static List<Request> heatPumpConfiguration = null;

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

	/**
	 * connects to the stiebel heat pump connector depending on the project
	 * property the "StiebelHeatPumpSimulate" the simulator is connected
	 * 
	 * @return stiebelheatpumpconnectos object
	 */
	private final StiebelHeatPumpConnector getStiebelHeatPumpConnector() {
		if (connector != null) {
			return connector;
		}

		// if (System.getProperty("StiebelHeatPumpSimulate") != null) {
		// connector = new StiebelHeatPumpSimulator();
		// } else
		if (serialPort != null) {
			connector = new StiebelHeatPumpSerialConnector(serialPort, baudRate);
		}
		return connector;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("Connecting to Stiebel heat pump ...");
		connector = getStiebelHeatPumpConnector();
		try {
			connector.connect();

			Map<String, String> heatPumpData = new HashMap<String, String>();
			for (Request request : heatPumpConfiguration) {
				Map<String, String> requestData = connector
						.getHeatPumpData(request);
				heatPumpData.putAll(requestData);
			}

			for (StiebelHeatPumpBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String parameter = provider.getParameter(itemName);
					if (parameter != null
							&& heatPumpData.containsKey(parameter)) {
						String heatpumpValue = heatPumpData.get(parameter);
						Class<? extends Item> itemType = provider
								.getItemType(itemName);
						if (itemType.isAssignableFrom(NumberItem.class)) {
							double value = Double.parseDouble(heatpumpValue);
							eventPublisher.postUpdate(itemName,
									new DecimalType(value));
						}
						if (itemType.isAssignableFrom(StringItem.class)) {
							String value = heatpumpValue;
							eventPublisher.postUpdate(itemName, new StringType(
									value));
						}
					}
				}
			}
		} catch (StiebelHeatPumpException e) {
		}
		finally{
			try {
				connector.disconnect();
			} catch (StiebelHeatPumpException e) {
			}
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

			if (!getHeatPumpConfiguration()) {
				setProperlyConfigured(false);
				logger.warn("Could not load Binding configuration! Can't find heat pump configuration.");
				return;
			}
			if (!getHeatPumpVersion()) {
				logger.warn("Could not get version info from heat pump!");
				setProperlyConfigured(false);
				return;
			}

			setProperlyConfigured(true);
			logger.debug("Binding configuration loaded.");
		}
	}

	/**
	 * This method reads version info from heat pump and verifies if it is the
	 * same as in user configuration
	 * 
	 * @return true if heat pump version matches configuration
	 */
	private boolean getHeatPumpVersion() {

		logger.debug("Loading heat pump version information ...");
		Request versionRequest = null;

		for (Request request : heatPumpConfiguration) {
			logger.debug(
					"Request : Name -> {}, Description -> {} , RequestByte -> {}",
					request.getName(), request.getDescription(),
					DatatypeConverter.printHexBinary(new byte[] { request
							.getRequestByte() }));
			if (request.getName().equalsIgnoreCase("Version")) {
				versionRequest = request;
				logger.debug("Loaded Request : "
						+ versionRequest.getDescription());
			}
		}

		if (versionRequest == null) {
			logger.debug("Request could not be found in configuration");
			return false;
		}

		String heatpumpVersion;
		try {
			connector = getStiebelHeatPumpConnector();
			connector.connect();

			heatpumpVersion = connector.getHeatPumpVersion(versionRequest);
			if (!heatpumpVersion.equalsIgnoreCase(version)) {
				logger.error(
						"The heat pump version {} does not match the configuration version {}!",
						heatpumpVersion, version);
				return false;
			}
			connector.version = version;
			
			// we can remove the version request for normal periodic polling of data
			heatPumpConfiguration.remove(versionRequest);
			
			return true;
		} catch (StiebelHeatPumpException e) {
			logger.error("Stiebel heatpump version could not be read from heat pump! "
					+ e.toString());
		}
		finally{
			try {
				connector.disconnect();
			} catch (StiebelHeatPumpException e) {
			}
		}
		
		return false;
	}

	/**
	 * This method looks up all files in resource and List of Request objects
	 * into xml file
	 * 
	 * @return true if heat pump configuration for version could be found and
	 *         loaded
	 */
	private boolean getHeatPumpConfiguration() {
		ConfigLocator configLocator = new ConfigLocator(version + ".xml");
		heatPumpConfiguration = configLocator.getConfig();

		if (heatPumpConfiguration != null && !heatPumpConfiguration.isEmpty()) {
			logger.info("Loaded heat pump configuration {}.xml .", version);
			logger.info("Configuration file contains {} requests.",
					heatPumpConfiguration.size());
			return true;
		}
		logger.warn("Could not load heat pump configuration file for {}!",
				version);
		return false;
	}
}
