/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.networkupstools.internal;

import org.openhab.binding.networkupstools.NetworkUpsToolsBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class parses information from the generic binding format and 
 * provides NetworkUpsTools binding information from it. It registers as a 
 * {@link NetworkUpsToolsBindingProvider} service as well.</p>
 * 
 * <p>Here is an examples for valid binding configuration string:
 * <ul>
 * 	<li><code>{ networkupstools = "nas:output.voltage" }</code> - binds to "output.voltage" property of UPS defined as "nas" in openhab.cfg</li>
 * </ul>   
 *  
 * @author jaroslawmazgaj
 * @since 1.7.0
 */
public class NetworkUpsToolsGenericBindingProvider extends AbstractGenericBindingProvider implements NetworkUpsToolsBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "networkupstools";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem || item instanceof NumberItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only String-, Switch- and NumberItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length > 2) {
			throw new BindingConfigParseException("NetworkHealth configuration can contain three parts at max");
		}
		
		NetworkUpsToolsBindingConfig config = new NetworkUpsToolsBindingConfig();
		config.ups = configParts[0];
		config.property = configParts[1];
		config.itemType = item.getClass();
		
		addBindingConfig(item, config);		
	}
	
	
	@Override
	public String getUps(String itemName) {
		NetworkUpsToolsBindingConfig config = (NetworkUpsToolsBindingConfig) bindingConfigs.get(itemName);
		return config.ups;
	}
	
	@Override
	public String getProperty(String itemName) {
		NetworkUpsToolsBindingConfig config = (NetworkUpsToolsBindingConfig) bindingConfigs.get(itemName);
		return config.property;
	}
	
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		NetworkUpsToolsBindingConfig config = (NetworkUpsToolsBindingConfig) bindingConfigs.get(itemName);
		return config.itemType;
	};
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the NetworkUpsTools
	 * binding provider.
	 *
	 */
	class NetworkUpsToolsBindingConfig implements BindingConfig {
		public Class<? extends Item> itemType;
		public String ups;
		public String property;
	}
	
	
}
