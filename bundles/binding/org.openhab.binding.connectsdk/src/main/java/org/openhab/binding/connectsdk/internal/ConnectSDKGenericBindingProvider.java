/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public class ConnectSDKGenericBindingProvider extends AbstractGenericBindingProvider implements ConnectSDKBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "connectsdk";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		final String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length != 3) {
			throw new BindingConfigParseException(
					"connectsdk binding configuration must contain exactly three parts seperated by colons. Device:Class:Property.");
		}
		final String device = configParts[0];
		final String clazz = configParts[1];
		final String property = configParts[2];
		
		if (StringUtils.isEmpty(device)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' has to define a device name - please check your *.items configuration");
		}
		if (StringUtils.isEmpty(clazz)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' has to define a class name - please check your *.items configuration");
		}

		if (StringUtils.isEmpty(property)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' has to define a property name - please check your *.items configuration");
		}
		final ConnectSDKBindingConfig config = new ConnectSDKBindingConfig();
		
		config.device = device;
		config.clazz = clazz;
		config.property = property;

		// add some validation?
		/*if (config.property == null) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' has to define an property ("
					+ StringUtils.join(bindings, ", ")
					+ ") value. unkown value: " + property
					+ " - please check your *.items configuration");

		}*/

		addBindingConfig(item, config);		
		
	}
	@Override
	public String getPropertyForItem(final String itemName) {
		return getConnectSDKBindingConfig(itemName).property;
	}

	@Override
	public String getDeviceForItem(final String itemName) {
		return getConnectSDKBindingConfig(itemName).device;
	}
	
	@Override
	public String getClassForItem(final String itemName) {
		return getConnectSDKBindingConfig(itemName).clazz;
	}
		
	private ConnectSDKBindingConfig getConnectSDKBindingConfig(String itemName) {
		final ConnectSDKBindingConfig config = (ConnectSDKBindingConfig) bindingConfigs.get(itemName);
		if(config == null) {
			throw new IllegalStateException(String.format("No binding configured for item %s", itemName));
		}
		return config;
	}
	
	
}
