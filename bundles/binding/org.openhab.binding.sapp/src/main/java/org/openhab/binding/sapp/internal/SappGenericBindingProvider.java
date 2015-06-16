/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.sapp.SappBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Paolo Denti
 * @since 1.0.0
 */
public class SappGenericBindingProvider extends AbstractGenericBindingProvider implements SappBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(SappGenericBindingProvider.class);

	private Map<String, Item> items = new HashMap<String, Item>();
	
	private boolean fullRefreshNeeded = false;

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sapp";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug(String.format("validating item '%s' against config '%s'", item, bindingConfig));

		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + " - not yet implemented, please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		logger.debug("processing binding configuration for context " + context);

		if (bindingConfig != null) {
			addBindingConfig(item, new SappBindingConfig(item.getName(), bindingConfig));
			fullRefreshNeeded = true;
		} else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addBindingConfig(Item item, BindingConfig config) {
		super.addBindingConfig(item, config);
		items.put(item.getName(), item);
	}

	@Override
	public SappBindingConfig getBindingConfig(String itemName) {
		return (SappBindingConfig) bindingConfigs.get(itemName);
	}

    @Override
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

	@Override
	public boolean isFullRefreshNeeded() {
		return fullRefreshNeeded;
	}

	@Override
	public void setFullRefreshNeeded(boolean fullRefreshNeeded) {
		this.fullRefreshNeeded = fullRefreshNeeded;
		
	}
}
