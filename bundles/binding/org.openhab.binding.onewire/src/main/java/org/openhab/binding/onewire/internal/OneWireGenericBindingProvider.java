/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal;

import java.util.Map;
import java.util.Set;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class handles Binding configurations
 * It registers as a {@link OneWireBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * The syntax of the binding configuration is listed in each available OneWireDevicePropertyBindingConfig class
 * <p>
 * 
 * @author Thomas.Eichstaedt-Engelen, Dennis Riegelbauer
 * @since 0.6.0
 */
public class OneWireGenericBindingProvider extends AbstractGenericBindingProvider implements OneWireBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(OneWireGenericBindingProvider.class);

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	public String getBindingType() {
		return "onewire";
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#validateItemType(org.openhab.core.items.Item, java.lang.String)
	 */
	public void validateItemType(Item pvItem, String pvBindingConfig) throws BindingConfigParseException {
		logger.debug("validateItemType: " + pvItem.getName() + " - bindingConfig:" + pvBindingConfig);

		if (OneWireBindingConfigFactory.isValidItemType(pvItem, pvBindingConfig)) {
			return;
		}
		throw new BindingConfigParseException("item '" + pvItem.getName() + "' is of type '" + pvItem.getClass().getSimpleName()
				+ "', only Number- Contact- and Switch type is allowed - please check your *.items configuration");
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#processBindingConfiguration(java.lang.String, org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String pvContext, Item pvItem, String pvBindingConfig) throws BindingConfigParseException {
		OneWireBindingConfig pvDevicePropertyBindingConfig = OneWireBindingConfigFactory.createOneWireDeviceProperty(pvItem, pvBindingConfig);

		addBindingConfig(pvItem, pvDevicePropertyBindingConfig);

		super.processBindingConfiguration(pvContext, pvItem, pvBindingConfig);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.OneWireBindingProvider#getItem(java.lang.String)
	 */
	public Item getItem(String pvItemName) {
		for (Set<Item> lvItems : contextMap.values()) {
			if (lvItems != null) {
				for (Item lvItem : lvItems) {
					if (pvItemName.equals(lvItem.getName())) {
						return lvItem;
					}
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.OneWireBindingProvider#getBindingConfig(java.lang.String)
	 */
	public OneWireBindingConfig getBindingConfig(String pvItemName) {
		return (OneWireBindingConfig) bindingConfigs.get(pvItemName);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.onewire.OneWireBindingProvider#getBindingConfigs()
	 */
	public Map<String, BindingConfig> getBindingConfigs() {
		return bindingConfigs;
	}
}
