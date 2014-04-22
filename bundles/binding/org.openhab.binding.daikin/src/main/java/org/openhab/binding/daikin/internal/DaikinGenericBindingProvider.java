/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.daikin.internal;

import org.openhab.binding.daikin.DaikinBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class can parse information from the generic binding format and 
 * provides Daikin binding information from it. It registers as a 
 * {@link DaikinBindingProvider} service as well.</p>
 * 
 * @author Ben Jones
 * @since 1.5.0
 */
public class DaikinGenericBindingProvider extends AbstractGenericBindingProvider implements DaikinBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(DaikinGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "daikin";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		DaikinBindingConfig config = new DaikinBindingConfig(item.getName(), bindingConfig);
		if (item.getClass() != config.getCommandType().getSupportedItemType()) {
			throw new BindingConfigParseException("Item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only '" + config.getCommandType().getSupportedItemType().getSimpleName() 
					+ "' are allowed for command '" + config.getCommandType().toString() 
					+ "' - please check your *.items configuration");			
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig != null) {
			DaikinBindingConfig config = new DaikinBindingConfig(item.getName(), bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("Daikin binding config is NULL for item '{}' -> process bindingConfig aborted!", item.getName());
		}
	}

	@Override
	public DaikinBindingConfig getBindingConfig(String itemName) {
		return (DaikinBindingConfig) bindingConfigs.get(itemName);
	}	
}
