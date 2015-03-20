/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hms.internal;

import org.openhab.binding.hms.HMSBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Thomas Urmann
 * @since 1.6.0
 */
public class HMSGenericBindingProvider extends AbstractGenericBindingProvider implements HMSBindingProvider {

	public String getBindingType() {
		return "hms";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		HMSBindingConfig config = new HMSBindingConfig();

		String[] parts = bindingConfig.split(";");
		for (String part : parts) {
			String[] keyValue = part.split("=");
			// to avoid beginner error
			keyValue[0] = keyValue[0].trim();
			keyValue[1] = keyValue[1].trim();
			if (keyValue[0].equals("address")) {
				config.address = keyValue[1];
			}
			if (keyValue[0].equals("datapoint")) {
				config.datapoint = HMSBindingConfig.Datapoint.valueOf(keyValue[1]);
			}
		}
		config.item = item;

		addBindingConfig(item, config);
	}

	public static class HMSBindingConfig implements BindingConfig {
		public enum Datapoint {
			TEMPERATURE, HUMIDITY;
		}

		public String address;
		public Item item;
		public Datapoint datapoint;
	}

	@Override
	public HMSBindingConfig getBindingConfigForAddressAndDatapoint(String device, HMSBindingConfig.Datapoint datapoint) {
		for (BindingConfig config : this.bindingConfigs.values()) {
			HMSBindingConfig hmsConfig = (HMSBindingConfig) config;
			if (hmsConfig.address.equals(device) && hmsConfig.datapoint == datapoint) {
				return (HMSBindingConfig) config;
			}
		}
		return null;
	}
}
