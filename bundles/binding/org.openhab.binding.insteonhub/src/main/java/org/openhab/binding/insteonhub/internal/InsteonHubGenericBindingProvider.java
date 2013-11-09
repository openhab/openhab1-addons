/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubGenericBindingProvider extends
		AbstractGenericBindingProvider implements InsteonHubBindingProvider {

	// map of itemNames configured for a device
	// key=deviceInfo, value=items
	private final Map<InsteonHubBindingDeviceInfo, Set<String>> deviceItems = new HashMap<InsteonHubBindingDeviceInfo, Set<String>>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "insteonhub";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig != null) {
			// parse binding configuration
			InsteonHubBindingConfig config = InsteonHubBindingConfig.parse(
					item.getName(), bindingConfig);
			// add binding configuration
			addBindingConfig(item, config);
			// add to hubid+device map
			Set<String> deviceItems = getOrCreateDeviceItems(config
					.getDeviceInfo());
			deviceItems.add(config.getItemName());
		}
	}

	@Override
	public InsteonHubBindingConfig getItemConfig(String itemName) {
		return ((InsteonHubBindingConfig) bindingConfigs.get(itemName));
	}

	@Override
	public void getConfiguredDevices(
			Set<InsteonHubBindingDeviceInfo> devicesToPopulate) {
		synchronized (deviceItems) {
			devicesToPopulate.addAll(deviceItems.keySet());
		}
	}

	@Override
	public void getDeviceItemNames(InsteonHubBindingDeviceInfo deviceInfo,
			Set<String> itemNamesToPopulate) {
		synchronized (deviceItems) {
			Set<String> items = deviceItems.get(deviceInfo);
			if (items != null) {
				itemNamesToPopulate.addAll(items);
			}
		}
	}

	private Set<String> getOrCreateDeviceItems(
			InsteonHubBindingDeviceInfo deviceInfo) {
		synchronized (deviceItems) {
			Set<String> items = deviceItems.get(deviceInfo);
			if (items == null) {
				items = new HashSet<String>();
				deviceItems.put(deviceInfo, items);
			}
			return items;
		}
	}

}
