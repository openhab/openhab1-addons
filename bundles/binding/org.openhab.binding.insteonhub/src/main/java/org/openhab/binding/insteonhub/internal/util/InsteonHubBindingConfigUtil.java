/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.binding.insteonhub.internal.InsteonHubBindingConfig;
import org.openhab.binding.insteonhub.internal.InsteonHubBindingDeviceInfo;

/**
 * Utility functions for dealing with hub/device configurations
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBindingConfigUtil {

	public static InsteonHubBindingConfig getConfigForItem(
			Collection<InsteonHubBindingProvider> providers, String itemName) {
		// check each provider
		for (InsteonHubBindingProvider provider : providers) {
			// lookup item config in this provider
			InsteonHubBindingConfig config = provider.getItemConfig(itemName);
			// if configuration was found, return it
			if (config != null) {
				return config;
			}
		}
		// configuration not found => return null
		return null;
	}

	public static Collection<InsteonHubBindingConfig> getConfigsForDevice(
			Collection<InsteonHubBindingProvider> providers, String hubId,
			String deviceId) {
		// create deviceInfo object for lookup
		InsteonHubBindingDeviceInfo deviceInfo = new InsteonHubBindingDeviceInfo(
				hubId, deviceId);

		List<InsteonHubBindingConfig> configs = new LinkedList<InsteonHubBindingConfig>();
		// check each provider
		for (InsteonHubBindingProvider provider : providers) {
			// lookup hubId+device itemNames for this provider
			Set<String> itemNames = provider.getDeviceItemNames(deviceInfo);
			// loop through found item names
			for (String itemName : itemNames) {
				// lookup configuration for item name
				InsteonHubBindingConfig config = provider
						.getItemConfig(itemName);
				// if configuration is found, add it to collection
				if (config != null) {
					configs.add(config);
				}
			}
		}
		return configs;
	}

	public static Set<InsteonHubBindingDeviceInfo> getConfiguredDevices(
			Collection<InsteonHubBindingProvider> providers) {
		Set<InsteonHubBindingDeviceInfo> ret = new HashSet<InsteonHubBindingDeviceInfo>();
		// check each provider
		for (InsteonHubBindingProvider provider : providers) {
			ret.addAll(provider.getConfiguredDevices());
		}
		return ret;
	}
}
