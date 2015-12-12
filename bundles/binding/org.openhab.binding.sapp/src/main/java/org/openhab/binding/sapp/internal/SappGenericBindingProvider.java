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
import org.openhab.binding.sapp.SappUpdatePendingRequestsProvider;
import org.openhab.binding.sapp.internal.configs.SappBindingConfig;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigContactItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigDimmerItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigNumberItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigRollershutterItem;
import org.openhab.binding.sapp.internal.configs.SappBindingConfigSwitchItem;
import org.openhab.binding.sapp.internal.model.SappPnmas;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
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
	 * pending update requests
	 */
	private SappUpdatePendingRequestsProvider sappUpdatePendingRequests = new SappUpdatePendingRequests();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sapp";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		logger.debug("validating item '{}' against config '{}'", item, bindingConfig);

		if (item instanceof SwitchItem) {
			; // OK, nothing to validate
		} else if (item instanceof ContactItem) {
			; // OK, nothing to validate
		} else if (item instanceof NumberItem) {
			; // OK, nothing to validate
		} else if (item instanceof RollershutterItem) {
			; // OK, nothing to validate
		} else if (item instanceof DimmerItem) {
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
		logger.debug("processing binding configuration for context {}", context);
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			if (item instanceof SwitchItem && !(item instanceof DimmerItem)) {
				SappBindingConfigSwitchItem sappBindingConfigSwitchItem = new SappBindingConfigSwitchItem(item, bindingConfig);
				addBindingConfig(item, sappBindingConfigSwitchItem);
			} else if (item instanceof ContactItem) {
				SappBindingConfigContactItem sappBindingConfigContactItem = new SappBindingConfigContactItem(item, bindingConfig);
				addBindingConfig(item, sappBindingConfigContactItem);
			} else if (item instanceof NumberItem) {
				SappBindingConfigNumberItem sappBindingConfigNumberItem = new SappBindingConfigNumberItem(item, bindingConfig);
				addBindingConfig(item, sappBindingConfigNumberItem);
			} else if (item instanceof RollershutterItem) {
				SappBindingConfigRollershutterItem sappBindingConfigRollershutterItem = new SappBindingConfigRollershutterItem(item, bindingConfig);
				addBindingConfig(item, sappBindingConfigRollershutterItem);
			} else if (item instanceof DimmerItem) {
				SappBindingConfigDimmerItem sappBindingConfigDimmerItem = new SappBindingConfigDimmerItem(item, bindingConfig);
				addBindingConfig(item, sappBindingConfigDimmerItem);
			} else {
				throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + " - not yet implemented, please check your *.items configuration");
			}
		} else {
			logger.warn("bindingConfig is NULL (item={}) -> processing bindingConfig aborted!", item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addBindingConfig(Item item, BindingConfig config) {
		super.addBindingConfig(item, config);
		sappUpdatePendingRequests.addPendingUpdateRequest(item.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SappBindingConfig getBindingConfig(String itemName) {
		return (SappBindingConfig) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, SappPnmas> getPnmasMap() {
		return pnmasMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getVirtualCachedValue(int address) {
		return virtualsCache.get(address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVirtualCachedValue(int address, int value) {
		virtualsCache.put(address, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getInputCachedValue(int address) {
		return inputsCache.get(address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInputCachedValue(int address, int value) {
		inputsCache.put(address, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getOutputCachedValue(int address) {
		return outputsCache.get(address);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOutputCachedValue(int address, int value) {
		outputsCache.put(address, value);
	}

	@Override
	public SappUpdatePendingRequestsProvider getSappUpdatePendingRequests() {
		return sappUpdatePendingRequests;
	}
}
