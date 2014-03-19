/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.asterisk.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.asterisk.AsteriskBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Asterisk binding information from it. It registers as a 
 * {@link AsteriskBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ asterisk="active" }</code> - receives status updates on active calls</li>
 * </ul>
 * These binding configurations can be used on either SwitchItems or CallItems. 
 * For SwitchItems, it will obviously receive ON at the beginning and OFF at the
 * end. CallItems will be filled with origination and the destination number.
 * </p>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public class AsteriskGenericBindingProvider extends AbstractGenericBindingProvider implements AsteriskBindingProvider {

	static final Logger logger = LoggerFactory.getLogger(AsteriskGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "asterisk";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof CallItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Call- and SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig != null) {
			AsteriskBindingConfig config = parseBindingConfig(item, bindingConfig);
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
	
	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an
	 * appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected AsteriskBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		try {
			return new AsteriskBindingConfig(item.getClass(), AsteriskBindingTypes.fromString(bindingConfig));
		}
		catch (IllegalArgumentException iae) {
			throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
		}
	}

	public Class<? extends Item> getItemType(String itemName) {
		AsteriskBindingConfig config = (AsteriskBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType: null;
	}

	public AsteriskBindingTypes getType(String itemName) {
		AsteriskBindingConfig config = (AsteriskBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.type : null;
	}

	public String[] getItemNamesByType(AsteriskBindingTypes type) {
		Set<String> itemNames = new HashSet<String>();
		for(Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			AsteriskBindingConfig fbConfig = (AsteriskBindingConfig) entry.getValue();
			if(fbConfig.type.equals(type)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	
	static class AsteriskBindingConfig implements BindingConfig {
		
		final Class<? extends Item> itemType;
		final AsteriskBindingTypes type;
		
		public AsteriskBindingConfig(Class<? extends Item> itemType, AsteriskBindingTypes type) {
			this.itemType = itemType;
			this.type = type;
		}
		
	}

}
