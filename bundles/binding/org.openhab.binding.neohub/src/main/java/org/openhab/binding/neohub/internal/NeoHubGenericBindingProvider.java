/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.neohub.NeoHubBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class NeoHubGenericBindingProvider extends
		AbstractGenericBindingProvider implements NeoHubBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "neohub";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Number- and String- or SwitchItems are allowed - please check your *.items configuration");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		final String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length != 2) {
			throw new BindingConfigParseException(
					"neohub binding configuration must contain exactly two parts seperated by a colon. Device:Property.");
		}
		final String device = configParts[0];
		final String property = configParts[1];

		if (StringUtils.isEmpty(device)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' has to define a device name - please check your *.items configuration");
		}

		final List<String> bindings = NeoStatProperty.getBindings();

		if (StringUtils.isEmpty(property)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' has to define a property ("
					+ StringUtils.join(bindings, ", ")
					+ ") value - please check your *.items configuration");
		}

		final NeoHubBindingConfig config = new NeoHubBindingConfig();
		config.device = device;
		config.property = NeoStatProperty.fromBinding(property);

		if (config.property == null) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' has to define an property ("
					+ StringUtils.join(bindings, ", ")
					+ ") value. unkown value: " + property
					+ " - please check your *.items configuration");

		}

		addBindingConfig(item, config);
	}

	@Override
	public NeoStatProperty getNeoStatProperty(final String itemName) {
		return getNeoHubBindingConfig(itemName).property;
	}

	@Override
	public String getNeoStatDevice(final String itemName) {
		return getNeoHubBindingConfig(itemName).device;
	}
	
	private NeoHubBindingConfig getNeoHubBindingConfig(final String itemName) {
		final NeoHubBindingConfig config = (NeoHubBindingConfig) this.bindingConfigs.get(itemName);
		if(config == null) {
			throw new IllegalStateException(String.format("No binding configured for item %s", itemName));
		}
		return config;
	}

	/**
	 * Small data structure to store device and property pair.
	 * 
	 */
	private class NeoHubBindingConfig implements BindingConfig {
		/**
		 * Value of device parameter in items configuration file. Must not be
		 * <code>null</code>.
		 */
		public String device;
		/**
		 * Value of property parameter in items configuration file. Must not be
		 * <code>null</code>.
		 */
		public NeoStatProperty property;
	}

}
