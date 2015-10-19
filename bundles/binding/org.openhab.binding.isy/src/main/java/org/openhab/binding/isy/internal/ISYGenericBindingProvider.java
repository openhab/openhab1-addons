/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.isy.ISYBindingConfig;
import org.openhab.binding.isy.ISYBindingConfig.Type;
import org.openhab.binding.isy.ISYBindingProvider;
import org.openhab.binding.isy.ISYControl;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.GroupItem;
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
 * @author Tim Diekmann
 * @since 1.7.0
 */
public class ISYGenericBindingProvider extends AbstractGenericBindingProvider
		implements ISYBindingProvider {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "isy";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof ContactItem || item instanceof SwitchItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch-, Contact-, and NumberItems are supported yet - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		ISYBindingConfig config = parseConfig(item, bindingConfig);

		addBindingConfig(item, config);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("added item {} [{}]", item, config.address);
		}
	}

	/**
	 * @see org.openhab.binding.isy.ISYBindingProvider#getBindingConfigFromItemName(java.lang.String)
	 */
	@Override
	public ISYBindingConfig getBindingConfigFromItemName(final String itemName) {
		for (BindingConfig config : this.bindingConfigs.values()) {
			ISYBindingConfig isyconfig = (ISYBindingConfig) config;

			if (itemName.equals(isyconfig.item.getName())) {
				return isyconfig;
			}
		}

		return null;
	}

	/**
	 * @see org.openhab.binding.isy.ISYBindingProvider#getBindingConfigFromAddress(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public Collection<ISYBindingConfig> getBindingConfigFromAddress(
			final String address, final String cmd) {
		Collection<ISYBindingConfig> result = new ArrayList<>();

		for (BindingConfig config : this.bindingConfigs.values()) {
			ISYBindingConfig isyconfig = (ISYBindingConfig) config;

			if (cmd.equals(isyconfig.cmd.name())
					&& address.equals(isyconfig.address)) {
				result.add(isyconfig);
			}
		}

		return result;
	}

	private ISYBindingConfig parseConfig(final Item item,
			final String bindingConfig) {

		ISYBindingConfig config = new ISYBindingConfig();
		config.item = item;

		if (item instanceof GroupItem) {
			config.type = Type.GROUP;
		} else if (item instanceof ContactItem) {
			config.type = Type.CONTACT;
		} else if (item instanceof NumberItem) {
			config.type = Type.NUMBER;
		} else {
			config.type = Type.SWITCH;
		}

		String[] arr = bindingConfig.split(",");
		for (String str : arr) {
			String[] pair = str.split("=");
			String key = pair[0];
			String value = pair[1];

			switch (key) {
			case "ctrl":
				config.controller = value.replace('.', ' ');
				break;
			case "addr":
				config.address = value.replace('.', ' ');
				break;
			case "type":
				if ("thermostat".equalsIgnoreCase(value)) {
					config.type = Type.THERMOSTAT;
				}
				break;
			case "cmd":
				try {
					config.cmd = ISYControl.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException ie) {
					this.logger.warn("Unsupported cmd {}", value);
					config.cmd = ISYControl.UNDEFINED;
				}
				break;
			}
		}

		if (config.address == null) {
			config.address = config.controller;
		}

		return config;
	}

}
