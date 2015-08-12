/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.buderus.internal;

import org.openhab.binding.buderus.BuderusBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>This class can parse information from the generic binding format and 
 * provides the Buderus RS232 Gateway Connector binding information from it. It registers as a 
 * {@link BuderusBindingProvider} service as well.</p>
 * 
 * The item binding configuration strings must have a length of exactly 8 characters, including blanks.
 * <p>Here are some examples for valid configuration strings:
 * <ul>
 * 	<li><code>{ buderus="04 08 18" }</code>
 * 	<li><code>{ buderus="04 10 A3" }</code>
 * </ul>
 * 
 * @author Lukas Maertin
 * @since 1.7.0
 */
public class BuderusGenericBindingProvider extends AbstractGenericBindingProvider implements BuderusBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "buderus";
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only StringItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		//bindingConfig = request string
		if (bindingConfig.length() != 8) {
			throw new BindingConfigParseException("Buderus Gateway item binding configuration for request must have a length of exactly 8 characters");
		}
		addBindingConfig(item, new BuderusBindingConfig(bindingConfig));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getRequest(String itemName) {
		BuderusBindingConfig config = (BuderusBindingConfig)bindingConfigs.get(itemName);
		return config != null ? config.request : null;
	}
	
	/**
	 * This is an internal data structure to store information from the item binding
	 * config strings and use it to answer the requests to the Buderus RS232 Gateway
	 * Connector binding provider.
	 */
	static private class BuderusBindingConfig implements BindingConfig {
		private String request;
		
		private BuderusBindingConfig(String request) {
			this.request = request;
		}
	}

}
