/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.diyonxbee.internal;

import java.util.List;

import org.openhab.binding.diyonxbee.DiyOnXBeeBindingProvider;
import org.openhab.binding.diyonxbee.internal.DiyOnXBeeGenericBindingProvider.DiyOnXBeeBindingConfig.DIRECTION;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author juergen.richtsfeld@gmail.com
 * @since 1.9
 */
public class DiyOnXBeeGenericBindingProvider extends AbstractGenericBindingProvider implements DiyOnXBeeBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "diyonxbee";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(bindingConfig.startsWith("<") || bindingConfig.startsWith(">"))) {
			throw new BindingConfigParseException("config of " + item.getName() + " has to start with '<' or '>'");
		}
		if (bindingConfig.indexOf(':') < 0) {
			throw new BindingConfigParseException("config of " + item.getName() + " is not in the form address:id");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig.startsWith("<")) {
			final DiyOnXBeeBindingConfig config = parseBindingConfig(item, bindingConfig, DIRECTION.IN);
			addBindingConfig(item, config);
		} else if (bindingConfig.startsWith(">")) {
			final DiyOnXBeeBindingConfig config = parseBindingConfig(item, bindingConfig, DIRECTION.OUT);
			addBindingConfig(item, config);
		}
	}

	private DiyOnXBeeBindingConfig parseBindingConfig(Item item, String bindingConfig, DIRECTION direction)
			throws BindingConfigParseException {
		final DiyOnXBeeBindingConfig config = new DiyOnXBeeBindingConfig();
		config.direction = direction;
		final String id = bindingConfig.trim().substring(1);
		final String[] parts = id.split(":");
		if (parts.length > 3) {
			throw new BindingConfigParseException("config of " + item.getName() + " is not in the form address:id");
		}
		config.remote = parts[0];
		config.id = parts[1];
		if (parts.length == 3) {
			config.maxValue = parseMaxValue(parts[2]);
		} else {
			config.maxValue = null;
		}
		config.types = item.getAcceptedDataTypes();
		return config;
	}

	private Integer parseMaxValue(final String numberString) {
		try {
			final int parsed = Integer.parseInt(numberString);
			if (parsed < 1) {
				return null;
			}
			return parsed;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public Boolean autoUpdate(String itemName) {
		final DiyOnXBeeBindingConfig bindingConfig = (DiyOnXBeeBindingConfig) bindingConfigs.get(itemName);
		if (bindingConfig == null)
			return null;
		return bindingConfig.direction == DIRECTION.IN;
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Jürgen Richtsfeld
	 * @since 1.8
	 */
	static class DiyOnXBeeBindingConfig implements BindingConfig {

		enum DIRECTION {
			/**
			 * binding for sensors
			 */
			IN,

			/**
			 * binding for actors
			 */
			OUT
		};

		DIRECTION direction;

		/**
		 * the value identifier. has to be unique for a specific {@link #remote}
		 * .
		 */
		String id;

		/**
		 * the address of the XBee-message.
		 */
		String remote;

		List<Class<? extends State>> types;

		/**
		 * an optional max value. if it's set, the sent value will be divided by
		 * it
		 */
		public Integer maxValue;
	}

	private DiyOnXBeeBindingConfig getBinding(final String itemName) {
		return (DiyOnXBeeBindingConfig) bindingConfigs.get(itemName);
	}

	@Override
	public String getId(String itemName) {
		return getBinding(itemName).id;
	}

	@Override
	public String getRemote(String itemName) {
		return getBinding(itemName).remote;
	}

	@Override
	public boolean isSensor(String itemName) {
		return getBinding(itemName).direction == DIRECTION.IN;
	}

	@Override
	public List<Class<? extends State>> getAvailableItemTypes(String itemName) {
		return getBinding(itemName).types;
	}

	@Override
	public Integer getMaxValue(String itemName) {
		return getBinding(itemName).maxValue;
	}
}
