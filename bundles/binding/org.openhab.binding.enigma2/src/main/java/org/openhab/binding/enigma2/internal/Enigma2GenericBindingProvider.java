/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal;

import org.openhab.binding.enigma2.Enigma2BindingProvider;
import org.openhab.core.items.Item;
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
 * Some valid examples are:
 * 
 * Number actualVolume {enigma2="[main:volume]"}
 * 
 * String actualChannel {enigma2="[main:channel]"}
 * 
 * Switch pause {enigma2="main:pause"}
 * 
 * Switch mute {enigma2="main:mute"}
 * 
 * Switch power {enigma2="main:powerstate"}
 * 
 * Switch customRcCommand {enigma2="main:remotecontrol:113"}
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public class Enigma2GenericBindingProvider extends
		AbstractGenericBindingProvider implements Enigma2BindingProvider {

	private static Logger logger = LoggerFactory
			.getLogger(Enigma2GenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "enigma2";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch-, Number, String and DimmerItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * [deviceId:command:value]
	 * 
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig == null || item == null) {
			logger.error(
					"invalid input for processBindingConfiguration, item={}, bindingConfig={}",
					item, bindingConfig);
			return;
		}

		/*
		 * remove unnecessary chars
		 */
		String strippedBindingConfig = bindingConfig.replace("[", "")
				.replace("]", "").trim();

		/*
		 * get elements
		 */
		String[] elements = strippedBindingConfig.split(":");
		Enigma2BindingConfig config = null;

		/*
		 * check for valid command
		 */
		Enigma2Command cmdId = null;
		try {
			cmdId = Enigma2Command.valueOf(elements[1].toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("Unknown command: "
					+ elements[1]);
		}

		if (elements.length == 2) {
			config = new Enigma2BindingConfig(item, elements[0], cmdId, null);
		} else if (elements.length == 3) {
			config = new Enigma2BindingConfig(item, elements[0], cmdId,
					elements[2]);
		} else {
			throw new BindingConfigParseException(
					"Configuration must have at at least 2 or 3 elements separated by ':'");
		}

		logger.debug(
				"Found \"{}\" binding config for deviceId \"{}\". Command is \"{}\" and value is \"{}\"",
				elements[0], elements[1], elements.length > 2 ? elements[2]
						: "-");

		addBindingConfig(item, config);
	}

	@Override
	public Enigma2BindingConfig getBindingConfigFor(String itemName) {
		return (Enigma2BindingConfig) this.bindingConfigs.get(itemName);
	}

	@Override
	public Class<? extends Item> getItemType(String name) {
		Enigma2BindingConfig bindingConfig = (Enigma2BindingConfig) this.bindingConfigs
				.get(name);
		return bindingConfig.getItem().getClass();
	}
}
