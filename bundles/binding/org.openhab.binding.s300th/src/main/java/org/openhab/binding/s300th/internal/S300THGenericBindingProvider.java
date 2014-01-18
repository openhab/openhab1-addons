/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.s300th.internal;

import org.openhab.binding.s300th.S300THBindingProvider;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class S300THGenericBindingProvider extends AbstractGenericBindingProvider implements S300THBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "s300th";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * item config in the style {s300th="address=A; datapoint=DATAPOINT"}
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		S300THBindingConfig config = new S300THBindingConfig();
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
				config.datapoint = Datapoint.valueOf(keyValue[1]);
			}
		}
		config.item = item;

		addBindingConfig(item, config);
	}

	public static class S300THBindingConfig implements BindingConfig {
		public enum Datapoint {
			TEMPERATURE, HUMIDITY, RAIN, WIND, IS_RAINING;
		}

		public String address;
		public Item item;
		public Datapoint datapoint;
	}

	@Override
	public S300THBindingConfig getBindingConfigForAddressAndDatapoint(String address, Datapoint datapoint) {
		for (BindingConfig config : this.bindingConfigs.values()) {
			S300THBindingConfig s300thConfig = (S300THBindingConfig) config;
			if (s300thConfig.address.equals(address) && s300thConfig.datapoint == datapoint) {
				return (S300THBindingConfig) config;
			}
		}
		return null;
	}

}
