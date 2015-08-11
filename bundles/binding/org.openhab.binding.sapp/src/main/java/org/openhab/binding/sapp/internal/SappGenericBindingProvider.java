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
import org.openhab.binding.sapp.internal.configs.SappBindingConfig;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigContactItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigNumberItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigSwitchItem;
import org.openhab.binding.sapp.internal.model.SappPnmas;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappGenericBindingProvider extends AbstractGenericBindingProvider implements SappBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(SappGenericBindingProvider.class);

	/**
	 * map of existing pnmas. key is pnmas id.
	 */
	private Map<String, SappPnmas> pnmasMap = new HashMap<String, SappPnmas>();

	/**
	 * map of existing items.
	 */
	private Map<String, Item> items = new HashMap<String, Item>();

	/**
	 * some new items have been loaded  
	 */
	private boolean fullRefreshNeeded = false;

	/**
	 * virtuals cache.
	 */
	private Map<Integer, Integer> virtualsCache = new HashMap<Integer, Integer>();

	/**
	 * inputs cache.
	 */
	private Map<Integer, Integer> inputsCache = new HashMap<Integer, Integer>();

	/**
	 * outputs cache.
	 */
	private Map<Integer, Integer> outputsCache = new HashMap<Integer, Integer>();

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

		if (item instanceof SwitchItem) {
			; // OK, nothing to validate
		} else if (item instanceof ContactItem) {
			; // OK, nothing to validate
		} else if (item instanceof NumberItem) {
			; // OK, nothing to validate
		} else {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + " - not yet implemented, please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug("processing binding configuration for context " + context);
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			if (item instanceof SwitchItem) {
				SappBindingConfigSwitchItem sappBindingConfigSwitchItem = new SappBindingConfigSwitchItem(item, bindingConfig);
				if (pnmasMap.get(sappBindingConfigSwitchItem.getStatus().getPnmasId()) != null && sappBindingConfigSwitchItem.getControl().getPnmasId() != null) {
					addBindingConfig(item, sappBindingConfigSwitchItem);
					fullRefreshNeeded = true;
				} else {
					logger.warn("bad pnmasid in bindingConfig: " + bindingConfig + " -> processing bindingConfig aborted!");
				}
			} else if (item instanceof ContactItem) {
				SappBindingConfigContactItem sappBindingConfigContactItem = new SappBindingConfigContactItem(item, bindingConfig);
				if (pnmasMap.get(sappBindingConfigContactItem.getStatus().getPnmasId()) != null) {
					addBindingConfig(item, sappBindingConfigContactItem);
					fullRefreshNeeded = true;
				} else {
					logger.warn("bad pnmasid in bindingConfig: " + bindingConfig + " -> processing bindingConfig aborted!");
				}
			} else if (item instanceof NumberItem) {
				SappBindingConfigNumberItem sappBindingConfigNumberItem = new SappBindingConfigNumberItem(item, bindingConfig);
				if (pnmasMap.get(sappBindingConfigNumberItem.getStatus().getPnmasId()) != null) {
					addBindingConfig(item, sappBindingConfigNumberItem);
					fullRefreshNeeded = true;
				} else {
					logger.warn("bad pnmasid in bindingConfig: " + bindingConfig + " -> processing bindingConfig aborted!");
				}
			} else {
				throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + " - not yet implemented, please check your *.items configuration");
			}
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
	public Map<String, SappPnmas> getPnmasMap() {
		return pnmasMap;
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

	@Override
	public Integer getVirtualCachedValue(int address) {
		return virtualsCache.get(address);
	}

	@Override
	public void setVirtualCachedValue(int address, int value) {
		virtualsCache.put(address, value);
	}

	@Override
	public Integer getInputCachedValue(int address) {
		return inputsCache.get(address);
	}

	@Override
	public void setInputCachedValue(int address, int value) {
		inputsCache.put(address, value);
	}

	@Override
	public Integer getOutputCachedValue(int address) {
		return outputsCache.get(address);
	}

	@Override
	public void setOutputCachedValue(int address, int value) {
		outputsCache.put(address, value);
	}
}
