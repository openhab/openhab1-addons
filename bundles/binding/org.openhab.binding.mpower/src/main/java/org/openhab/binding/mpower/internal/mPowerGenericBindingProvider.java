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

import org.openhab.binding.mpower.mPowerBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author magcode
 * @since 1.0.0
 */
public class mPowerGenericBindingProvider extends
		AbstractGenericBindingProvider implements mPowerBindingProvider {
	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("(.*):(.*):(.*)");
	private Map<String, mPowerBindingConfig> addressMap = new HashMap<String, mPowerBindingConfig>();

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
		String socket = matcher.group(2);
		String variable = matcher.group(3);

		mPowerBindingConfig config = null;
		if (addressMap.containsKey(mPowerInstance)) {
			config = addressMap.get(mPowerInstance);
		} else {
			config = new mPowerBindingConfig(mPowerInstance);
			addressMap.put(mPowerInstance, config);
		}

		if ("voltage".equals(variable)) {
			config.setVoltageItemName(Integer.parseInt(socket), item.getName());
		}

		if ("power".equals(variable)) {
			config.setPowerItemName(Integer.parseInt(socket), item.getName());
		}

		if ("switch".equals(variable)) {
			config.setSwitchItemName(Integer.parseInt(socket), item.getName());
		}

		addBindingConfig(item, config);
	}

	@Override
	public mPowerBindingConfig getConfigForItemName(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public mPowerBindingConfig getConfigForAddress(String address) {
		if (super.bindingConfigs.containsKey(address)) {
			return (mPowerBindingConfig) super.bindingConfigs.get(address);
		}
		return null;
	}

}
