/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.internal;

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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
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
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorBinding.class);

	/* configuration variables for communication */
	private int udpPort = 9997;
	private String serialPort = null;
	private boolean simulate = false;

	private OpenEnergyMonitorDataParser dataParser = null;

	/** Thread to handle messages from Open Energy Monitor devices */
	private MessageListener messageListener = null;

	public OpenEnergyMonitorBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		messageListener.setInterrupted(true);
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

			// as long as no interrupt is requested, continue running
			while (!interrupted) {

				try {
					// Wait a packet (blocking)
					byte[] data = connector.receiveDatagram();

					logger.trace("Received data (len={}): {}", data.length,
							DatatypeConverter.printHexBinary(data));

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
										state = new DecimalType(value.doubleValue());
										found = true;

									} else if (variable.contains(key) && variable.matches(".*[+-/*^%].*")) {
										logger.debug("Eval key={}, variable={}", key, variable);

										String tmp = replaceVariables(vals, variable);
										
										try {
											double result = new DoubleEvaluator().evaluate(tmp);
											logger.debug("Eval '{}={}={}'", variable, tmp, result);
											state = new DecimalType(result);
											found = true;

										} catch (Exception e) {
											logger.error(
													"Error occured during data evaluation",
													e);
										}
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
