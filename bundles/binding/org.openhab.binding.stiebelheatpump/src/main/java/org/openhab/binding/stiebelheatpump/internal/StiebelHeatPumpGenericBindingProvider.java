/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.internal;

import org.openhab.binding.stiebelheatpump.StiebelHeatPumpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Peter Kreutzer
 * @since 1.5.0
 */
public class StiebelHeatPumpGenericBindingProvider extends
		AbstractGenericBindingProvider implements
		StiebelHeatPumpBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(StiebelHeatPumpGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "stiebelheatpump";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
		logger.debug(bindingConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		StiebelHeatPumpBindingConfig config = new StiebelHeatPumpBindingConfig();
		config.parameter = bindingConfig.trim();
		config.itemType = item.getClass();
		addBindingConfig(item, config);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public String getParameter(String itemName) {
		StiebelHeatPumpBindingConfig config = (StiebelHeatPumpBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.parameter : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		StiebelHeatPumpBindingConfig config = (StiebelHeatPumpBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	class StiebelHeatPumpBindingConfig implements BindingConfig {
		public String parameter;
		public Class<? extends Item> itemType;
	}

}
