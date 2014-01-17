/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * MAX!Cube binding information from it. It registers as a
 * {@link MaxCubeBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Example for a valid binding configuration strings:
 * </p>
 * <ul>
 * <li><code>{ maxcube="JEQ0304492" }</code> - returns the corresponding value of the default attribute based on the MAX device type.</li>
 * <li>{ maxcube="JEQ304492:type=valve" } - returns the corresponding valve position in percentage. Only available for heating thermostats.</li>
 * <li>{ maxcube="JEQ304492:type=battery" } - returns the curent battery state as text.</li>
 * </ul>
 * @author Andreas Heil
 * @since 1.4.0
 */
public class MaxCubeGenericBindingProvider extends AbstractGenericBindingProvider implements MaxCubeBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "maxcube";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof DimmerItem || item instanceof ContactItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number-, Dimmer-, Contact- and StringItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length < 1) {
			throw new BindingConfigParseException("MAX!Cube configuration requires at least serial number for a MAX!Cube device.");
		}

		MaxCubeBindingConfig config = new MaxCubeBindingConfig();

		item.getName();

		config.serialNumber = configParts[0];

		for (int i = 1; i < configParts.length; i++) {
			String[] bindingToken = configParts[i].split("=");
			if (bindingToken[0].equals("type")) {
				if (bindingToken[1].equals("valve")) {
					config.bindingType = BindingType.VALVE;
				} else if (bindingToken[1].equals("battery")) {
					config.bindingType = BindingType.BATTERY;
				}
			}
		}

		addBindingConfig(item, config);
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the MAX!Cube binding
	 * provider.
	 */
	static private class MaxCubeBindingConfig implements BindingConfig {
		public String serialNumber;
		public BindingType bindingType;
	}

	/**
	 * Return the serial number for the given <code>itemName</code>.
	 * 
	 * @param itemName
	 *            the itemName to return the corresponding MAX serial number
	 */
	@Override
	public String getSerialNumber(String itemName) {
		MaxCubeBindingConfig config = (MaxCubeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.serialNumber : null;
	}
	
	/**
	 * Return the specified binding type for the given <code>itemName</code> if specified, <code>null</code> otherwise.
	 * 
	 * @param itemName
	 *            the itemName to return the binding type specified
	 */
	@Override
	public BindingType getBindingType(String itemName) {
		MaxCubeBindingConfig config = (MaxCubeBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.bindingType : null;
	}
}
