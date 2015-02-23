/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dsmr.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import org.openhab.binding.dsmr.DSMRBindingProvider;
import org.openhab.binding.dsmr.internal.cosem.CosemValue;
import org.openhab.binding.dsmr.internal.messages.OBISMessage;
import org.openhab.binding.dsmr.internal.messages.OBISMsgFactory;
import org.openhab.binding.dsmr.internal.p1telegram.P1TelegramParser;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class periodically read out the P1 port of the Smart Meter.
 * <p>
 * The frequency is once every 10 seconds. This frequency is based on the update
 * interval of the Smart Meter. The resulting values will be posted to the event
 * bus.
 * <p>
 * At this moment the binding supports only a single Smart Meter.
 * <p>
 * The binding needs the following configuration parameters from openhab.cfg:
 * <p>
 * <ul>
 * <li>dsmr.port (serial port device)
 * <li>dsmr.<metertype>.chanel (M-Bus channel of the specified meter type gas,
 * water, heating, cooling, generic, slaveelectricity)
 * </ul>
 * <p>
 * The implementation of the binding is based on the Dutch Smart Meter
 * Requirements (DSMR)
 * 
 * @author M.Volaart
 * @since 1.7.0
 */
public class DSMRBinding extends AbstractActiveBinding<DSMRBindingProvider>
		implements ManagedService {

	/** Update interval as specified by DSMR */
	public static final int DSMR_UPDATE_INTERVAL = 10000;

	/* Logger */
	private static final Logger logger = LoggerFactory
			.getLogger(DSMRBinding.class);

	/* Serial port (configurable via openhab.cfg) */
	private String port = "";

	/* Meter - channel mapping (configurable via openhab.cfg) */
	private final List<DSMRMeter> dsmrMeters = new ArrayList<DSMRMeter>();

	/* DSMR Port object */
	private DSMRPort dsmrPort;

	/*
	 * the refresh interval which is used to poll values from the DSMR server
	 * 
	 * Since we the device only updates every 10 seconds we let openHAB
	 * introduce a pause before execute is called again.
	 * 
	 * We use here half the update interval time so we have some time to read
	 * the serial port (refreshInterval starts after previous executes ends)
	 */
	private long refreshInterval = DSMR_UPDATE_INTERVAL / 2;

	/**
	 * Default Constructor
	 */
	public DSMRBinding() {
	}

	/**
	 * Activate the binding. We don't do anything special here.
	 * <p>
	 * To simplify synchronization issues we initiate the DSMR Port in the
	 * execute() method
	 */
	public void activate() {
		logger.debug("Activate DSMRBinding");
	}

	/**
	 * Deactivates the binding.
	 * <p>
	 * This will close the DSMR Port
	 */
	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		logger.info("Deactivating DSMRBinding");
		if (dsmrPort != null) {
			logger.debug("Closing DSMR port");
			dsmrPort.close();
		} else {
			logger.info("DSMR port was not initialised");
		}
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
		return "DSMR Binding";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Check if there are any item bindings
		if (!bindingsExist()) {
			logger.debug("There is no existing DSMR binding configuration => refresh cycle aborted!");
			return;
		}

		// Check if a valid DSMR port exists. Open a new one if necessary
		if (dsmrPort == null || !dsmrPort.isOpen()) {
			logger.debug("Creating DSMR Port:" + port);

			dsmrPort = new DSMRPort(port, new P1TelegramParser(
					new OBISMsgFactory(dsmrMeters)), DSMR_UPDATE_INTERVAL / 2,
					DSMR_UPDATE_INTERVAL * 2);
		}

		// Read the DSMRPort
		List<OBISMessage> messages = dsmrPort.read();
		logger.debug("Received " + messages.size() + " messages");

		// Publish messages on the event bus
		for (OBISMessage msg : messages) {
			logger.debug("Read message:" + msg);
			for (DSMRBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String dsmrItemId = provider.getDSMRItemID(itemName);
					for (CosemValue<? extends State> openHABValue : msg
							.getOpenHABValues()) {
						// DSMR items with an empty dsmrItemId are filtered
						// automatically
						if (dsmrItemId.equals(openHABValue.getDsmrItemId())) {
							logger.debug("Publish data(" + dsmrItemId + ") to "
									+ itemName);

							eventPublisher.postUpdate(itemName,
									openHABValue.getValue());
						}
					}
				}
			}
		}
	}

	/**
	 * Read the dsmr:port and dsmr:<metertype>.channel properties
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug("updated() is called!");
		if (config != null) {
			// Read port string
			String portString = (String) config.get("port");
			logger.debug("dsmr:port=" + portString);
			if (StringUtils.isNotBlank(portString)) {
				port = portString;
			} else {
				logger.warn("dsmr:port setting is empty");
			}

			/*
			 * Read the channel configuration
			 */
			dsmrMeters.clear();

			for (DSMRMeterType meterType : DSMRMeterType.values()) {
				String channelConfigValue = (String) config
						.get(meterType.channelConfigKey);
				logger.debug("dsmr:" + meterType.channelConfigKey + "="
						+ channelConfigValue);

				if (StringUtils.isNotBlank(channelConfigValue)) {
					try {
						dsmrMeters.add(new DSMRMeter(meterType, Integer
								.parseInt(channelConfigValue)));
					} catch (NumberFormatException nfe) {
						logger.warn("Invalid value " + channelConfigValue
								+ " for dsmr:" + meterType.channelConfigKey
								+ ". Ignore mapping!", nfe);
					}
				} else {
					switch (meterType) {
					case NA:
						break; // Filter special DSMRMeterType
					case ELECTRICITY:
						break; // Always channel 0, configuration not needed
					default:
						logger.info("dsmr:" + meterType.channelConfigKey
								+ " setting is empty");
					}
				}
			}

			// Validate minimal configuration
			if (port.length() > 0) {
				logger.debug("Configuration succeeded");
				setProperlyConfigured(true);
			} else {
				logger.debug("Configuration failed");
				setProperlyConfigured(false);
			}
		}
	}
}
