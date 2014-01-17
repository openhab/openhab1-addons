/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.swegonventilation.SwegonVentilationBindingProvider;
import org.openhab.binding.swegonventilation.protocol.SwegonVentilationConnector;
import org.openhab.binding.swegonventilation.protocol.SwegonVentilationDataParser;
import org.openhab.binding.swegonventilation.protocol.SwegonVentilationSerialConnector;
import org.openhab.binding.swegonventilation.protocol.SwegonVentilationSimulator;
import org.openhab.binding.swegonventilation.protocol.SwegonVentilationUDPConnector;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Binding to receive data from Swegon ventilation system.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationBinding extends
		AbstractBinding<SwegonVentilationBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(SwegonVentilationBinding.class);

	/* configuration variables for communication */
	private int udpPort = 9998;
	private String serialPort = null;
	private boolean simulator = false;

	/** Thread to handle messages from heat pump */
	private MessageListener messageListener = null;

	public SwegonVentilationBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		
		if (messageListener != null) {
			messageListener.setInterrupted(true);
		}
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true
				: false);

		if (config != null) {

			String portString = (String) config.get("udpPort");
			if (StringUtils.isNotBlank(portString)) {
				udpPort = Integer.parseInt(portString);
			}

			serialPort = (String) config.get("serialPort");

			String simulateString = (String) config.get("simulate");
			if (StringUtils.isNotBlank(simulateString)) {
				simulator = Boolean.parseBoolean(simulateString);
			}
		}
		
		messageListener = new MessageListener();
		messageListener.start();

	}

	/**
	 * Convert device value to OpenHAB state.
	 * 
	 * @param itemType
	 * @param value
	 * 
	 * @return a {@link State}
	 */
	private State convertDeviceValueToOpenHabState(Class<? extends Item> itemType, Integer value) {
		State state = UnDefType.UNDEF;

		try {
			if (itemType == SwitchItem.class) {
				state = value == 0 ? OnOffType.OFF : OnOffType.ON;
				
			} else if (itemType == NumberItem.class) {
				state = new DecimalType(value);
				
			} 
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", value, itemType);
		}
		
		return state;
	}

	/**
	 * The MessageListener runs as a separate thread.
	 * 
	 * Thread listening message from Swegon ventilation system and send updates
	 * to openHAB bus.
	 * 
	 */
	private class MessageListener extends Thread {

		private boolean interrupted = false;

		MessageListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
			this.interrupt();
		}

		@Override
		public void run() {

			logger.debug("Swegon ventilation system message listener started");

			SwegonVentilationConnector connector;

			if (simulator == true)
				connector = new SwegonVentilationSimulator();
			else if (serialPort != null)
				connector = new SwegonVentilationSerialConnector(serialPort);
			else
				connector = new SwegonVentilationUDPConnector(udpPort);

			try {
				connector.connect();
			} catch (SwegonVentilationException e) {
				logger.error(
						"Error occured when connecting to Swegon ventilation system",
						e);
			}

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				try {
					// Wait a packet (blocking)
					byte[] data = connector.receiveDatagram();

					logger.trace("Received data (len={}): {}", data.length,
							DatatypeConverter.printHexBinary(data));

					HashMap<SwegonVentilationCommandType, Integer> regValues = SwegonVentilationDataParser
							.parseData(data);

					if (regValues != null) {

						logger.debug("regValues (len={}): {}",
								regValues.size(), regValues);

						Set<Map.Entry<SwegonVentilationCommandType, Integer>> set = regValues
								.entrySet();

						for (Entry<SwegonVentilationCommandType, Integer> val : set) {

							SwegonVentilationCommandType cmdType = val.getKey();
							Integer value = val.getValue();

							for (SwegonVentilationBindingProvider provider : providers) {
								for (String itemName : provider.getItemNames()) {

									SwegonVentilationCommandType commandType = provider
											.getCommandType(itemName);

									if (commandType.equals(cmdType)) {
										Class<? extends Item> itemType = provider.getItemType(itemName);

										org.openhab.core.types.State state = convertDeviceValueToOpenHabState(itemType, value);

										eventPublisher.postUpdate(itemName,
												state);
									}
								}
							}

						}

					}

				} catch (SwegonVentilationException e) {

					logger.error(
							"Error occured when received data from Swegon ventilation system",
							e);
				}
			}

			try {
				connector.disconnect();
			} catch (SwegonVentilationException e) {
				logger.error(
						"Error occured when disconnecting form Swegon ventilation system",
						e);
			}

		}

	}

}
