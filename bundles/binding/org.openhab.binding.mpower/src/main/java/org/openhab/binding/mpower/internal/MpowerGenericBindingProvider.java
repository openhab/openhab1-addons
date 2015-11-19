/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpower.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mpower.MpowerBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerGenericBindingProvider extends
		AbstractGenericBindingProvider implements MpowerBindingProvider {
	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("(.*):(.*):(.*)");
	private Map<String, MpowerBindingConfig> addressMap = new HashMap<String, MpowerBindingConfig>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mpower";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches())
			throw new BindingConfigParseException("Config for item '"
					+ item.getName() + "' could not be parsed.");

		String mPowerInstance = matcher.group(1);
		String socketString = matcher.group(2);
		int socket = Integer.parseInt(socketString);
		String variable = matcher.group(3);

		MpowerBindingConfig config = null;
		if (addressMap.containsKey(mPowerInstance)) {
			config = addressMap.get(mPowerInstance);
		} else {
			config = new MpowerBindingConfig(mPowerInstance);
			addressMap.put(mPowerInstance, config);
		}

		if ("voltage".equals(variable)) {
			config.setVoltageItemName(socket, item.getName());
		}

		if ("power".equals(variable)) {
			config.setPowerItemName(socket, item.getName());
		}

		if ("switch".equals(variable)) {
			config.setSwitchItemName(socket, item.getName());
		}

		if ("energy".equals(variable)) {
			config.setEnergyItemName(socket, item.getName());
		}

		if ("energytoday".equals(variable)) {
			config.setEnergyTodayItemName(socket, item.getName());
		}
		config.resetCache(socket);
		addBindingConfig(item, config);
	}

	@Override
	public MpowerBindingConfig getConfigForItemName(String itemName) {
		for (Map.Entry<String, MpowerBindingConfig> entry : addressMap
				.entrySet()) {
			if (entry.getValue().containsItemName(itemName)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public MpowerBindingConfig getConfigForAddress(String address) {
		if (addressMap.containsKey(address)) {
			return addressMap.get(address);
		}
		return null;
	}
}
