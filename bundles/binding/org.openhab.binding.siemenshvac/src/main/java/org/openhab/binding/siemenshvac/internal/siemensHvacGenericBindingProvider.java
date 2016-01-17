/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.siemenshvac.internal;

import java.util.Map;

import org.openhab.binding.siemenshvac.siemensHvacBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author laurent@clae.net
 * @since 1.9.0-SNAPSHOT
 */
public class siemensHvacGenericBindingProvider extends
		AbstractGenericBindingProvider implements siemensHvacBindingProvider {
	private static final Logger logger = LoggerFactory
			.getLogger(siemensHvacGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		logger.debug("siemensHvac:hvac getBindingType()!");
		return "siemenshvac";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		logger.debug("siemensHvac:hvac processBindingConfiguration()!");
		super.processBindingConfiguration(context, item, bindingConfig);
		siemensHvacBindingConfig config = new siemensHvacBindingConfig(
				item.getName(), bindingConfig);

		// parse bindingconfig here ...

		addBindingConfig(item, config);
	}

	public Map<String, BindingConfig> getBindingConfigs() {
		return bindingConfigs;
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author laurent@clae.net
	 * @since 1.8.0-SNAPSHOT
	 */
	class siemensHvacBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private String name;
		private String bindingConfig;
		private String dpt;
		private String type;

		public siemensHvacBindingConfig(String name, String bindingConfig) {
			this.name = name;
			this.bindingConfig = bindingConfig;

			String[] bindingValues = bindingConfig.split(";");

			for (String bindingValue : bindingValues) {
				String[] bindingParts = bindingValue.split(":");

				String bindingKey = bindingParts[0];
				String bindingVal = bindingParts[1];

				if (bindingKey.equals("dpt"))
					this.dpt = bindingVal;
				else if (bindingKey.equals("type"))
					this.type = bindingVal;
			}
		}

		public String getDpt() {
			return dpt;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

	}

}
