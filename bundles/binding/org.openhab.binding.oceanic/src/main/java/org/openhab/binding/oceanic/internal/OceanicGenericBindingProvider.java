/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oceanic.internal;

import java.io.InvalidClassException;
import org.openhab.binding.oceanic.OceanicBindingProvider;
import org.openhab.binding.oceanic.OceanicValueSelector;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Oceanic bindingconfigurations are very simple and consist of three parts, the name of the serial port
 * and the variable to read, and a polling period (in seconds) to update the value
 * 
 * Number volume {oceanic="/dev/tty12345:totalflow:10"}
 * 
 * The variables that can be read  from the watersoftener are defined in the OceanicValueSelector enum. 
 * 
 * @author Karel Goderis
 * @since 1.5.0
 */

public class OceanicGenericBindingProvider extends AbstractGenericBindingProvider implements OceanicBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(OceanicGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "oceanic";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof NumberItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, String-, Number-, or DateTime Items are allowed - please check your *.items configuration");
		}	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {

			OceanicBindingConfig config = new OceanicBindingConfig();

			String valueSelectorString = null;
			String pollingIntervalString = null;

			String[] configParts = bindingConfig.trim().split(":");

			if (configParts.length != 3) {
				throw new BindingConfigParseException(
						"RME binding must contain three parts separated by ':'");
			}

			config.serialPort = configParts[0].trim();	
			valueSelectorString = configParts[1].trim();
			pollingIntervalString = configParts[2].trim();

			try {

				OceanicValueSelector.validateBinding(valueSelectorString,
						item);

				config.valueSelector = valueSelectorString;
				config.pollingInterval = Integer.parseInt(pollingIntervalString);

				addBindingConfig(item, config);


			} catch (IllegalArgumentException e1) {
				throw new BindingConfigParseException(
						"Invalid value selector '" + valueSelectorString + "'!");

			} catch (InvalidClassException e1) {
				throw new BindingConfigParseException(
						"Invalid item type for value selector '"
								+ valueSelectorString + "'!");
			}
		} else {
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	class OceanicBindingConfig implements BindingConfig {
		String serialPort;
		String valueSelector;
		int pollingInterval;
	}

	
	public String getSerialPort(String itemName) {
		OceanicBindingConfig config = (OceanicBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.serialPort : null;
	}

	
	public String getValueSelector(String itemName) {
		OceanicBindingConfig config = (OceanicBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.valueSelector : null;
	}

	public int getPollingInterval(String itemName) {
		OceanicBindingConfig config = (OceanicBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.pollingInterval : null;
	}
	
}
