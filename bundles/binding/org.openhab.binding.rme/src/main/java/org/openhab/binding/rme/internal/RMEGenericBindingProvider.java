/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rme.internal;

import java.io.InvalidClassException;
import java.util.regex.Pattern;

import org.openhab.binding.rme.RMEBindingProvider;
import org.openhab.binding.rme.RMEValueSelector;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * sonos commands will be limited to the simple commands that take only up to one input parameter. unpnp actions
 * requiring input variables could potentially take their inputs from elsewhere in the binding, e.g. config parameters
 * or other
 *
 * sonos="[ON:office:play], [OFF:office:stop]" - switch items for ordinary sonos commands
 * 		using openhab command : player name : sonos command as format
 * 
 * sonos="[office:getcurrenttrack]" - string and number items for UPNP service variable updates using
 * 		using player_name : somecommand, where somecommand takes a simple input/output value from/to the string
 * 
 * @author Karel Goderis
 * @author Pauli Anttila
 * @since 1.1.0
 */

public class RMEGenericBindingProvider extends AbstractGenericBindingProvider implements RMEBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(RMEGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "rme";
	}

	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and Dimmer Items are allowed - please check your *.items configuration");
		}	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {

			RMEBindingConfig config = new RMEBindingConfig();

			String valueSelectorString = null;

			String[] configParts = bindingConfig.trim().split(":");

			if (configParts.length != 2) {
				throw new BindingConfigParseException(
						"RME binding must contain two parts separated by ':'");
			}

			config.serialPort = configParts[0].trim();	
			valueSelectorString = configParts[1].trim();

			try {

				RMEValueSelector.validateBinding(valueSelectorString,
						item);

				config.valueSelector = RMEValueSelector
						.getValueSelector(valueSelectorString);

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

	class RMEBindingConfig implements BindingConfig {
		String serialPort;
		RMEValueSelector valueSelector;
	}

	
	public String getSerialPort(String itemName) {
		RMEBindingConfig config = (RMEBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.serialPort : null;
	}

	
	public RMEValueSelector getValueSelector(String itemName) {
		RMEBindingConfig config = (RMEBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.valueSelector : null;
	}

}
