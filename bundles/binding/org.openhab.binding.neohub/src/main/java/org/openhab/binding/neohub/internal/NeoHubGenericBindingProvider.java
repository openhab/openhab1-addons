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
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.binding.neohub.NeoHubBindingProvider;
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
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class NeoHubGenericBindingProvider extends
		AbstractGenericBindingProvider implements NeoHubBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(NeoHubGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "neohub";
	}

	/**
	 * @{inheritDoc}
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
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			// TODO: maybe refactor this into a nicer way of handling parameters
			final JSONObject obj = new JSONObject(bindingConfig);
			if (!obj.has(NeoHubBindingConfig.ParameterDevice)
					|| StringUtils.isEmpty(obj
							.getString(NeoHubBindingConfig.ParameterDevice))) {
				throw new BindingConfigParseException(
						"item '"
								+ item.getName()
								+ "' has to define a device name - please check your *.items configuration");
			}

			final List<String> bindings = NeoStatProperty.getBindings();

			if (!obj.has(NeoHubBindingConfig.ParameterProperty)
					|| StringUtils.isEmpty(obj
							.getString(NeoHubBindingConfig.ParameterProperty))
					|| !bindings.contains(obj
							.getString(NeoHubBindingConfig.ParameterProperty))) {
				throw new BindingConfigParseException("item '" + item.getName()
						+ "' has to define a property ("
						+ StringUtils.join(bindings, ", ")
						+ ") value - please check your *.items configuration");
			}

			final NeoHubBindingConfig config = new NeoHubBindingConfig();
			config.device = obj.getString(NeoHubBindingConfig.ParameterDevice);
			config.property = NeoStatProperty.fromBinding(obj
					.getString(NeoHubBindingConfig.ParameterProperty));

			if (config.property == null) {
				throw new BindingConfigParseException(
						"item '"
								+ item.getName()
								+ "' has to define an property (TODO: values here) value. unkown value: "
								+ obj.getString("property")
								+ " - please check your *.items configuration");

			}

			addBindingConfig(item, config);
		} catch (JSONException e) {
			logger.debug("failed to parse: " + bindingConfig, e);
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' has an invalid configuration string '" + bindingConfig
					+ "' - please check your *.items configuration");
		}
	}

	@Override
	public NeoStatProperty getNeoStatProperty(String itemName) {
		return ((NeoHubBindingConfig) this.bindingConfigs.get(itemName)).property;
	}

	@Override
	public String getNeoStatDevice(String itemName) {
		return ((NeoHubBindingConfig) this.bindingConfigs.get(itemName)).device;
	}

	/**
	 * Small data structure to store device and property pair.
	 *
	 */
	private class NeoHubBindingConfig implements BindingConfig {
		/**
		 * Name of device parameter in items configuration file.
		 */
		static final String ParameterDevice = "device";
		/**
		 * Name of property parameter in items configuration file.
		 */
		static final String ParameterProperty = "property";
		/**
		 * Value of device parameter in items configuration file.
		 * Must not be <code>null</code>.
		 */
		public String device;
		/**
		 * Value of property parameter in items configuration file.
		 * Must not be <code>null</code>.
		 */
		public NeoStatProperty property;
	}

}
