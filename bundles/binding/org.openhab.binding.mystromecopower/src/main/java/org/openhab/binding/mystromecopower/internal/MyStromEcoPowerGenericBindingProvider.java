/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower.internal;

import org.openhab.binding.mystromecopower.MyStromEcoPowerBindingProvider;
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
 * @author Jordens Christophe
 * @since 1.8.0-SNAPSHOT
 */
/**
 * @author Christophe
 * 
 */
public class MyStromEcoPowerGenericBindingProvider extends
		AbstractGenericBindingProvider implements
		MyStromEcoPowerBindingProvider {
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "mystromecopower";
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
							+ "', only Switch, Number or String is allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		MyStromEcoPowerBindingConfig config = new MyStromEcoPowerBindingConfig();

		// parse bindingconfig here ...
		config.mystromFriendlyName = bindingConfig;
		config.isSwitch = item instanceof SwitchItem;
		config.isNumberItem = item instanceof NumberItem;
		config.isStringItem = item instanceof StringItem;
		addBindingConfig(item, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.mystromecopower.MyStromEcoPowerBindingProvider#
	 * getMystromFriendlyName(java.lang.String)
	 */
	public String getMystromFriendlyName(String itemName) {
		MyStromEcoPowerBindingConfig config = (MyStromEcoPowerBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.mystromFriendlyName : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getIsSwitch(String itemName) {
		MyStromEcoPowerBindingConfig config = (MyStromEcoPowerBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isSwitch : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getIsNumberItem(String itemName) {
		MyStromEcoPowerBindingConfig config = (MyStromEcoPowerBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isNumberItem : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getIsStringItem(String itemName) {
		MyStromEcoPowerBindingConfig config = (MyStromEcoPowerBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.isStringItem : false;
	}

	/**
	 * This class is responsible to transport configuration data.
	 * 
	 * @author Jordens Christophe
	 * @since 1.7.0-SNAPSHOT
	 */
	class MyStromEcoPowerBindingConfig implements BindingConfig {
		/**
		 * The name of the device on the mystrom server.
		 */
		public String mystromFriendlyName;

		/**
		 * Indicates if the openhab item is a switch item.
		 */
		public Boolean isSwitch = false;

		/**
		 * Indicates if the openhab item is a number item.
		 */
		public Boolean isNumberItem = false;

		/**
		 * Indicates if the openhab item is a string item.
		 */
		public Boolean isStringItem = false;
	}
}
