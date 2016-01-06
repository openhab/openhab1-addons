/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sallegra.internal;

import org.openhab.binding.sallegra.SallegraBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is for parsing the items configuration.
 * 
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 */
public class SallegraGenericBindingProvider extends AbstractGenericBindingProvider implements SallegraBindingProvider {

	private static Logger logger = LoggerFactory.getLogger(SallegraGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sallegra";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * [deviceId:command:value]
	 * 
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig == null || item == null) {
			logger.error("invalid null input, item={}, bindingConfig={}", item, bindingConfig);
			return;
		}

		/*
		 * remove unnecessary chars
		 */
		String strippedBindingConfig = bindingConfig.replace("[", "").replace("]", "").trim();

		/*
		 * get configpart
		 */
		String[] configpart = strippedBindingConfig.split(":");
		SallegraBindingConfig config = null;

		/*
		 * check for valid command
		 */
		if (configpart.length == 3) {
			SallegraCommand cmdId = null;
			try {
				cmdId = SallegraCommand.valueOf(configpart[1].toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new BindingConfigParseException("Unknown command: " + configpart[1]);
			}

			config = new SallegraBindingConfig(configpart[0], cmdId, configpart[2]);
			config.setItem(item.getName());
		} else {
			throw new BindingConfigParseException(
					"Configuration must have at at least 2 or 3 configpart separated by ':'");
		}

		logger.debug("Found \"{}\" binding config for deviceId \"{}\". Command is \"{}\" and value is \"{}\"",
				configpart[0], configpart[1], configpart.length > 2 ? configpart[2] : "-");

		addBindingConfig(item, config);
	}

	@Override
	public SallegraBindingConfig getBindingConfigFor(String itemName) {
		return (SallegraBindingConfig) this.bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}

	@Override
	public Class<? extends Item> getItemType(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
