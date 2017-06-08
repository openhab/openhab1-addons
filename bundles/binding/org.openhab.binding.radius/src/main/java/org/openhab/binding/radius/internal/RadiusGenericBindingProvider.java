/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.radius.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.radius.RadiusBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Jan N. Klug
 * @since 1.8.0
 */
public class RadiusGenericBindingProvider extends AbstractGenericBindingProvider implements RadiusBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "radius";
	}

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern IN_BINDING_PATTERN = Pattern
			.compile("<([0-9.:a-zA-Z]+),([0-9.a-zA-Z]+),(ACCESS_REQUEST)");
	private static final Pattern IN_BINDING_ATTRIBUTE_PATTERN = Pattern
			.compile("<([0-9.:a-zA-Z]+),([0-9.a-zA-Z]+),(ACCESS_REQUEST),([_0-9.a-zA-Z]+)");

	// TODO add OUT binding
	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem || item instanceof NumberItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only StringItems, NumberItems and SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			RadiusBindingConfig config = new RadiusBindingConfig();

			Matcher matcher = IN_BINDING_ATTRIBUTE_PATTERN.matcher(bindingConfig);

			if (!matcher.matches()) { // is without attribute
				matcher = IN_BINDING_PATTERN.matcher(bindingConfig);
				if (!matcher.matches()) { // does not match anythig at all: invalid config
					throw new BindingConfigParseException("bindingConfig '" + bindingConfig
							+ "' doesn't contain a valid binding configuration");
				}
			}

			config.itemType = item.getClass();
			config.direction = RadiusBindingConfig.IN;
			config.user = matcher.group(1).toString();
			config.password = matcher.group(2).toString();
			config.radiusType = RadiusTypeCode.getCode(matcher.group(3).toString());

			if (matcher.groupCount() == 4) {
				config.radiusAttribute = RadiusAttribute.getAttribute(matcher.group(4).toString().toUpperCase());
			} else {
				config.radiusAttribute = 0;
			}

			addBindingConfig(item, config);
		}

	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Jan N. Klug
	 * @since 1.8.0
	 */
	class RadiusBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public static final byte IN = 0;
		public static final byte OUT = 1;

		Class<? extends Item> itemType;
		public String user;
		public String password;
		public byte radiusType;
		public byte radiusAttribute;
		public byte direction;

		/**
		 * put whole configuration in String
		 * 
		 * @return configuration string
		 */
		public String toString() {
			return "itemType=" + itemType + " user=" + user + " pass=" + password + "type=" + radiusType + "attribute="
					+ radiusAttribute;
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		RadiusBindingConfig config = (RadiusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public byte getRadiusType(String itemName) {
		RadiusBindingConfig config = (RadiusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.radiusType : 0;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public byte getRadiusAttribute(String itemName) {
		RadiusBindingConfig config = (RadiusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.radiusAttribute : 0;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public String getUserName(String itemName) {
		RadiusBindingConfig config = (RadiusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.user : "";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public String getPassword(String itemName) {
		RadiusBindingConfig config = (RadiusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.password : "";
	}

	/**
	 * @{inheritDoc
	 */
	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			RadiusBindingConfig radiusConfig = (RadiusBindingConfig) bindingConfigs.get(itemName);
			if (radiusConfig.direction == RadiusBindingConfig.IN) {
				inBindings.add(itemName);
			}
		}
		return inBindings;
	}

}
