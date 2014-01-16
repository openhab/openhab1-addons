/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.internal;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.epsonprojector.EpsonProjectorBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Epson projector device binding information from it.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>epsonprojector="hometheater:Power:60000"</code></li>
 * <li><code>epsonprojector=">hometheater:KeyCode"</code></li>
 * <li><code>epsonprojector="<hometheater:LampTime:3600000"</code></li>
 * <li><code>epsonprojector="hometheater:Source:ON,60000"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorGenericBindingProvider extends
		AbstractGenericBindingProvider implements EpsonProjectorBindingProvider {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(EpsonProjectorGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "epsonprojector";
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
							+ "', only SwitchItem, NumberItem and StringItem are allowed - please check your *.items configuration");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		EpsonProjectorBindingConfig config = new EpsonProjectorBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		config.inBinding = true;
		config.outBinding = true;
		config.itemType = item.getClass();

		if (bindingConfig.startsWith("<")) {

			if (configParts.length != 3) {
				throw new BindingConfigParseException(
						"Epson projector in binding must contain 3 parts separated by ':'");
			}

			config.outBinding = false;
			config.deviceID = configParts[0].trim().replace("<", "");
			parseRefreshPeriod(configParts[2], config);
			
		} else if (bindingConfig.startsWith(">")) {
			
			if (configParts.length != 2) {
				throw new BindingConfigParseException(
						"Epson projector out binding must contain 2 parts separated by ':'");
			}

			config.inBinding = false;
			config.deviceID = configParts[0].trim().replace(">", "");

		} else {
			
			if (configParts.length != 3) {
				throw new BindingConfigParseException(
						"Epson projector bi-directional binding must contain 3 parts separated by ':'");
			}

			config.deviceID = configParts[0].trim();
			parseRefreshPeriod(configParts[2], config);
		}

		config.commandType = getCommandTypeFromString(configParts[1].trim(), item);

		addBindingConfig(item, config);
	}

	private void parseRefreshPeriod(String refreshPeriodString, EpsonProjectorBindingConfig config) throws BindingConfigParseException {
		
		if (refreshPeriodString.trim().contains(",")) {
			
			String[] refreshIntervalParts = refreshPeriodString.trim().split(",");
			
			if (refreshIntervalParts.length != 2) {
				throw new BindingConfigParseException(
						"Epson projector refresh interval must contain 1-2 parts separated by ','");
			}
			
			if (refreshIntervalParts[0].trim().equals("ON")) {
				config.refreshInterval =  Integer.valueOf(refreshIntervalParts[1].trim());
				config.refreshOnlyWhenPowerOn = true;
				return;
			}
			
		}
		
		config.refreshInterval = Integer.valueOf(refreshPeriodString);
		config.refreshOnlyWhenPowerOn = false;
	}
	
	private EpsonProjectorCommandType getCommandTypeFromString(String commandTypeString, Item item) throws BindingConfigParseException {
		
		EpsonProjectorCommandType commandType = null;
		
		try {
			EpsonProjectorCommandType.validateBinding(commandTypeString, item.getClass());

			commandType = EpsonProjectorCommandType.getCommandType(commandTypeString);

		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("Invalid command type '"
					+ commandTypeString + "'!");

		} catch (InvalidClassException e) {
			throw new BindingConfigParseException(
					"Invalid item type for command type '" + commandTypeString
							+ "'!");

		}
		
		return commandType;
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public String getDeviceId(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.deviceID : null;
	}

	@Override
	public EpsonProjectorCommandType getCommandType(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.commandType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getRefreshInterval(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null && config.inBinding == true ? config.refreshInterval : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInBinding(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.inBinding: null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOutBinding(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.outBinding: null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean refreshOnlyWhenPowerOn(String itemName) {
		EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
				.get(itemName);
		return config != null && config.inBinding == true ? config.refreshOnlyWhenPowerOn: null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			EpsonProjectorBindingConfig config = (EpsonProjectorBindingConfig) bindingConfigs
					.get(itemName);
			if (config.inBinding == true) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the Epson projector
	 * binding provider.
	 */
	static class EpsonProjectorBindingConfig implements BindingConfig {

		public Class<? extends Item> itemType = null;
		public String deviceID = null;
		public EpsonProjectorCommandType commandType = null;
		public int refreshInterval = 0;
		public boolean inBinding = false;
		public boolean outBinding = true;
		public boolean refreshOnlyWhenPowerOn = false;
		
		@Override
		public String toString() {
			return "ExecBindingConfigElement ["
					+ ", itemType=" + itemType
					+ ", deviceID=" + deviceID
					+ ", commandType=" + commandType
					+ ", refreshInterval=" + refreshInterval
					+ ", inBinding=" + inBinding + "]";
		}

	}
}
