/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.isy;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.isy.internal.ISYControl;
import org.openhab.binding.isy.internal.ISYNodeType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Tim Diekmann
 * @author Jon Bullen
 * @since 1.7.0
 * 
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
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof ContactItem || item instanceof SwitchItem 
				|| item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch, Contact, String and Number Items are supported yet - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		ISYBindingConfig config = parseConfig(item, bindingConfig);

		addBindingConfig(item, config);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("added item {} [{}]", item, config);
		}
	}

	/**
	 * @see org.openhab.binding.isy.ISYBindingProvider#getBindingConfigFromItemName(java.lang.String)
	 */
	@Override
	public ISYBindingConfig getBindingConfigFromItemName(final String itemName) {
		for (BindingConfig config : this.bindingConfigs.values()) {
			ISYBindingConfig isyconfig = (ISYBindingConfig) config;

			if (itemName.equals(isyconfig.getItemName())) {
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

			if (cmd.equals(isyconfig.getControlCommand().name())
					&& address.equals(isyconfig.getAddress())) {
				result.add(isyconfig);
			}
		}

		return result;
	}

	/**
	 * 
	 * @param item				The item being processed for this config. 
	 * @param bindingConfig		The string configuration against this item in the config file. 
	 * @return					A {@link ISYBindingConfig} with the ISY reference information. 
	 */
	private ISYBindingConfig parseConfig(final Item item,
			final String bindingConfig) {

		ISYNodeType type;
		String controller = null;
		String address = null;
		ISYControl command = null;
		
		// item has already been validated. Use validateItemType to catch unsupported types. 
		if (item instanceof GroupItem) {
			type = ISYNodeType.GROUP;
		} else if (item instanceof ContactItem) {
			type = ISYNodeType.CONTACT;
		} else if (item instanceof NumberItem) {
			type = ISYNodeType.NUMBER;
		} else if (item instanceof StringItem) {
			type = ISYNodeType.STRING;
		} else {
			type = ISYNodeType.SWITCH;
		}

		
		
		// Valid Keys:
		// ctrl, type, cmd, addr
		String[] arr = bindingConfig.split(",");
		for (String str : arr) {
			String[] pair = str.split("=");
			String key = pair[0];
			String value = pair[1];

			switch (key) {
				case "ctrl":
					controller = value.replace('.', ' ');
					break;
				case "addr":
					address = value.replace('.', ' ');
					break;
				case "type":
					if ("thermostat".equalsIgnoreCase(value)) {
						type = ISYNodeType.THERMOSTAT;
					}
					break;
				case "cmd":
					try {
						command = ISYControl.valueOf(value.toUpperCase());
					} catch (IllegalArgumentException ie) {
						this.logger.warn("Unsupported cmd {}", value);
						command = ISYControl.UNDEFINED;
					}
					break;
			}
		}

		// Set the address to be the same as the controller as it is just a single device. 
		if (address == null) {
			address = controller;
		}

		return new ISYBindingConfig(item, type, address, address, command);
	}

}
