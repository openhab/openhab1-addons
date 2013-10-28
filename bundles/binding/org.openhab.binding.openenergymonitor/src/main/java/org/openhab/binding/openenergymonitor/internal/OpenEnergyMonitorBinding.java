/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.internal;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

import net.astesana.javaluator.DoubleEvaluator;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.openenergymonitor.OpenEnergyMonitorBindingProvider;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorConnector;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorDataParser;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorParserRule;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorSerialConnector;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorSimulator;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorUDPConnector;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Binding to receive data from Open Energy Monitor devices.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorBinding extends
		AbstractBinding<OpenEnergyMonitorBindingProvider> implements
		ManagedService, BindingChangeListener {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorBinding.class);

	/* configuration variables for communication */
	private int udpPort = 9997;
	private String serialPort = null;
	private boolean simulate = false;

	private ItemRegistry itemRegistry;

	private OpenEnergyMonitorDataParser dataParser = null;

	/** Thread to handle messages from Open Energy Monitor devices */
	private MessageListener messageListener = null;

	private OpenEnergyMonitorValueStore valueStore = new OpenEnergyMonitorValueStore();
	private int lastRecordedDay = Calendar.getInstance().get(
			Calendar.DAY_OF_YEAR);

	public OpenEnergyMonitorBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		messageListener.setInterrupted(true);
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		initializeItem(itemName);
	}

	/**
	 * Initialize item value from item registry.
	 * 
	 * @param itemType
	 * 
	 */
	private void initializeItem(String itemName) {
		for (OpenEnergyMonitorBindingProvider provider : providers) {
			OpenEnergyMonitorFunctionType function = provider
					.getFunction(itemName);

			if (function != null) {
				try {
					Item item = getItemFromItemName(itemName);

					if (item != null) {
						State currentState = item.getState();
						if (currentState.getClass() != UnDefType.class) {
							double val = ((DecimalType) currentState)
									.doubleValue();
							logger.debug(
									"Restore current state of the item {} = {}",
									itemName, val);
							valueStore.setValue(itemName, val);
						}
					}
				} catch (Exception e) {
					logger.debug("initializeItem failed", e);
				}
			}
		}
	}

	/**
	 * Returns the {@link Item} for the given <code>itemName</code> or
	 * <code>null</code> if there is no or to many corresponding Items
	 * 
	 * @param itemName
	 * 
	 * @return the {@link Item} for the given <code>itemName</code> or
	 *         <code>null</code> if there is no or to many corresponding Items
	 */
	private Item getItemFromItemName(String itemName) {
		try {
			return itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e) {
			logger.error("Couldn't find item for itemName '" + itemName + "'");
		}

		return null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (config != null) {

			HashMap<String, OpenEnergyMonitorParserRule> parsingRules = new HashMap<String, OpenEnergyMonitorParserRule>();

			Enumeration<String> keys = config.keys();

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				String value = (String) config.get(key);

				if ("udpPort".equals(key)) {
					if (StringUtils.isNotBlank(value)) {
						udpPort = Integer.parseInt(value);
					}
				} else if ("serialPort".equals(key)) {
					serialPort = value;
				} else if ("simulate".equals(key)) {
					if (StringUtils.isNotBlank(value)) {
						simulate = Boolean.parseBoolean(value);
					}
				} else {

					// process all data parsing rules
					try {
						OpenEnergyMonitorParserRule rule = new OpenEnergyMonitorParserRule(value);
						parsingRules.put(key, rule);
					} catch (OpenEnergyMonitorException e) {
						throw new ConfigurationException(key, "invalid parser rule", e);
					}
				}

			}

			if (parsingRules != null) {

				dataParser = new OpenEnergyMonitorDataParser(parsingRules);
			}
			
			messageListener = new MessageListener();
			messageListener.start();
		}
	}

	/**
	 * The MessageListener runs as a separate thread.
	 * 
	 * Thread listening message from Open Energy Monitoring devices and send
	 * updates to openHAB bus.
	 * 
	 */
	private class MessageListener extends Thread {

		private boolean interrupted = false;

		MessageListener() {
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
			messageListener.interrupt();
		}

		@Override
		public void run() {

			logger.debug("Open Energy Monitor message listener started");

			OpenEnergyMonitorConnector connector;

			if (simulate == true)
				connector = new OpenEnergyMonitorSimulator();
			else if (serialPort != null)
				connector = new OpenEnergyMonitorSerialConnector(serialPort);
			else
				connector = new OpenEnergyMonitorUDPConnector(udpPort);

			try {
				connector.connect();
			} catch (OpenEnergyMonitorException e) {
				logger.error(
						"Error occured when connecting to Open Energy Monitor device",
						e);
			}

			long lastTime = 0;

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				try {
					// Wait a packet (blocking)
					byte[] data = connector.receiveDatagram();

					long dataReceived = System.currentTimeMillis();
					long timeElapsed = dataReceived
							- (lastTime == 0 ? dataReceived : lastTime);
					lastTime = dataReceived;

					logger.trace("Received data (len={}): {}", data.length,
							DatatypeConverter.printHexBinary(data));

					logger.debug("time elapsed {}ms", timeElapsed);

					HashMap<String, Number> vals = dataParser.parseData(data);

					for (OpenEnergyMonitorBindingProvider provider : providers) {
						for (String itemName : provider.getItemNames()) {

							for (Entry<String, Number> entry : vals.entrySet()) {
								String key = entry.getKey();
								Number value = entry.getValue();

								if (key != null && value != null) {

									boolean found = false;

									org.openhab.core.types.State state = null;

									String variable = provider.getVariable(itemName);

									if (variable.equals(key)) {
										OpenEnergyMonitorFunctionType function = provider.getFunction(itemName);
										state = calculate(itemName, function, timeElapsed, value);
										found = true;

									} else if (variable.contains(key) && variable.matches(".*[+-/*^%].*")) {
										logger.debug("Eval key={}, variable={}", key, variable);

										String tmp = replaceVariables(vals, variable);
										double result = new DoubleEvaluator().evaluate(tmp);
										logger.debug("Eval '{}={}={}'", variable, tmp, result);

										OpenEnergyMonitorFunctionType function = provider.getFunction(itemName);
										state = calculate(itemName, function, timeElapsed, result);
										found = true;
									}

									if (found) {

										state = transformData(
												provider.getTransformationType(itemName),
												provider.getTransformationFunction(itemName),
												state);

										if (state != null) {
											eventPublisher.postUpdate(itemName, state);
											break;
										}

									}

								}
							}
						}

					}

				} catch (OpenEnergyMonitorException e) {

					logger.error(
							"Error occured when received data from Open Energy Monitor device",
							e);
				}
			}

			try {
				connector.disconnect();
			} catch (OpenEnergyMonitorException e) {
				logger.error(
						"Error occured when disconnecting form Open Energy Monitor device",
						e);
			}

		}

	}

	/**
	 * Calculate energy quantities by function.
	 * 
	 */
	private org.openhab.core.types.State calculate(String itemName,
			OpenEnergyMonitorFunctionType function, long timeElapsed,
			Number value) {

		org.openhab.core.types.State state = null;

		if (function != null) {
			double result;

			switch (function) {
			case KWH:
				result = calcEnergy(value.doubleValue(), timeElapsed) / 1000;
				result = valueStore.incValue(itemName, result);
				state = new DecimalType(result);
				break;

			case KWHD:
				int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
				result = calcEnergy(value.doubleValue(), timeElapsed) / 1000;
				if (currentDay != lastRecordedDay) {
					valueStore.setValue(itemName, result);
					lastRecordedDay = currentDay;
				} else {
					result = valueStore.incValue(itemName, result);
				}
				state = new DecimalType(result);
				break;

			case CUMULATIVE:
				double latestValue = valueStore.getValue(itemName);
				if (value.doubleValue() < latestValue) {
					result = valueStore.incValue(itemName, value.doubleValue());
				} else {
					valueStore.setValue(itemName, value.doubleValue());
				}
				state = new DecimalType(value.doubleValue());
				break;
			}
		} else {
			state = new DecimalType(value.doubleValue());
		}

		return state;
	}

	private String replaceVariables(HashMap<String, Number> vals,
			String variable) {
		for (Entry<String, Number> entry : vals.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			variable = variable.replace(key, String.valueOf(value));

		}

		return variable;
	}

	/**
	 * Calculate consumed energy from power.
	 * 
	 */
	private Double calcEnergy(Double power, long timeElapsed) {

		return (power * timeElapsed) / 3600000;
	}

	/**
	 * Transform received data by Transformation service.
	 * 
	 */
	protected org.openhab.core.types.State transformData(
			String transformationType, String transformationFunction,
			org.openhab.core.types.State data) {
		
		if (transformationType != null && transformationFunction != null) {
			String transformedResponse = null;

			try {
				TransformationService transformationService = TransformationHelper
						.getTransformationService(
								OpenEnergyMonitorActivator.getContext(),
								transformationType);
				if (transformationService != null) {
					transformedResponse = transformationService.transform(
							transformationFunction, String.valueOf(data));
				} else {
					logger.warn(
							"couldn't transform response because transformationService of type '{}' is unavailable",
							transformationType);
				}
			} catch (TransformationException te) {
				logger.error(
						"transformation throws exception [transformation type="
								+ transformationType
								+ ", transformation function="
								+ transformationFunction + ", response=" + data
								+ "]", te);
			}

			logger.debug("transformed response is '{}'", transformedResponse);

			if (transformedResponse != null) {
				return new DecimalType(transformedResponse);
			}
		}

		return data;
	}
}
