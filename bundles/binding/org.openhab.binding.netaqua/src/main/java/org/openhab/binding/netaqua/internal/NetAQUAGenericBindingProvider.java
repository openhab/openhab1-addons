/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netaqua.internal;

import org.openhab.binding.netaqua.NetAQUABindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Markus Fritze
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @since 1.7.0
 */
public class NetAQUAGenericBindingProvider extends AbstractGenericBindingProvider implements NetAQUABindingProvider {

	private static Logger logger = LoggerFactory.getLogger(NetAQUAGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "netaqua";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
/*
		if (!(item instanceof NumberItem || item instanceof StringItem || item instanceof ContactItem)) {
			throw new BindingConfigParseException(
				"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() + 
				"', only NumberItems and StringItems are allowed - please check your *.items configuration");
		}
*/
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NetAQUAItemType getItemType(String itemName) { 
		final NetAQUABindingConfig config = (NetAQUABindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.property : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getItemIndex(String itemName) { 
		final NetAQUABindingConfig config = (NetAQUABindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.index : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getItemIndexB(String itemName) { 
		final NetAQUABindingConfig config = (NetAQUABindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.indexB : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, final String bindingConfig) throws BindingConfigParseException {
		logger.debug("Processing binding configuration: bindingConfig '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			final NetAQUABindingConfig config = new NetAQUABindingConfig();
			final String[] configParts = bindingConfig.split(",");
			config.index = -1;
			config.indexB = -1;
			switch(configParts.length) {
			case 3:
				config.indexB = Integer.parseInt(configParts[2]);
			case 2:
				config.index = Integer.parseInt(configParts[1]);
			case 1:
				config.property = NetAQUAItemType.fromString(configParts[0]);
				break;
			case 0:
				config.property = NetAQUAItemType.fromString(bindingConfig);
			}
			logger.debug("Adding binding: '{}'", config);
			addBindingConfig(item, config);
		} catch (NetAQUAException ne) {
			logger.error(ne.getMessage());
		}
	}
	
		
	private static class NetAQUABindingConfig implements BindingConfig {

		NetAQUAItemType property;
		Integer index;
		Integer indexB;

		@Override
		public String toString() {
			return "NetAQUABindingConfig [item=" + this.property.getItem() + ", index="+  + this.index + ", indexB="+  + this.indexB + "]";
		}
	}
	
}
