/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.k8055.internal;

import org.openhab.binding.k8055.k8055BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Anthony Green
 * @since 1.5.0
 */
public class k8055GenericBindingProvider extends AbstractGenericBindingProvider
		implements k8055BindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(k8055GenericBindingProvider.class);

	// Details of the available hardware
	static final int NUM_DIGITAL_INPUTS = 5;
	static final int NUM_DIGITAL_OUTPUTS = 8;
	static final int NUM_ANALOG_INPUTS = 2;
	static final int NUM_ANALOG_OUTPUTS = 2;

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "k8055";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		parseBindingConfig(bindingConfig);
	}

	/**
	 * Parse binding string. Examples of what is allowed are:
	 * 
	 * DIGITAL_IN:1 ANALOG_IN:2 DIGITAL_OUT:3
	 * 
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		logger.debug("processing binding config: " + item.getName() + "; "
				+ bindingConfig);
		addBindingConfig(item, parseBindingConfig(bindingConfig));
	}

	protected k8055BindingConfig parseBindingConfig(String bindingConfig)
			throws BindingConfigParseException {
		k8055BindingConfig config = new k8055BindingConfig();

		String[] configParts = bindingConfig.split(":");
		if (configParts.length != 2) {
			throw new BindingConfigParseException(
					"Unable to parse k8055 binding string: " + bindingConfig
							+ ".  Incorrect number of colons.");
		}

		try {
			config.ioNumber = Integer.parseInt(configParts[1]);
			config.ioType = IOType.valueOf(configParts[0]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(
					"Unable to parse k8055 binding string: " + bindingConfig
							+ ". Could not parse input number: "
							+ configParts[1]);
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException(
					"Unable to parse k8055 binding string: " + bindingConfig
							+ ". Invalid input type: " + configParts[0]);
		}

		// Verify config is actually valid given the hardware
		if (config.ioNumber < 1) {
			throw new BindingConfigParseException(
					"Unable to parse k8055 binding string: " + bindingConfig
							+ ". IO channel must be greater than equal to 1 ");
		} else if ((config.ioNumber > NUM_DIGITAL_INPUTS && config.ioType
				.equals(IOType.DIGITAL_IN))
				|| (config.ioNumber > NUM_DIGITAL_OUTPUTS && config.ioType
						.equals(IOType.DIGITAL_OUT))
				|| (config.ioNumber > NUM_ANALOG_INPUTS && config.ioType
						.equals(IOType.ANALOG_IN))
				|| (config.ioNumber > NUM_ANALOG_OUTPUTS && config.ioType
						.equals(IOType.ANALOG_OUT))) {
			throw new BindingConfigParseException(
					"Unable to parse k8055 binding string: "
							+ bindingConfig
							+ ". IO channel number was greater than the number of physical channels ");
		}

		return config;
	}

	public class k8055BindingConfig implements BindingConfig {

		/**
		 * Type of input or output
		 */
		IOType ioType;

		/**
		 * Which Input or Output number is this?
		 */
		int ioNumber;
	}

	@Override
	public k8055BindingConfig getItemConfig(String itemName) {
		return (k8055BindingConfig) bindingConfigs.get(itemName);
	}

}
