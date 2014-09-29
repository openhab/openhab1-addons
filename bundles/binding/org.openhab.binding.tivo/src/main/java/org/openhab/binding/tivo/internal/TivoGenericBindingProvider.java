/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tivo.internal;

import org.openhab.binding.tivo.TivoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.4.0
 */
public class TivoGenericBindingProvider extends AbstractGenericBindingProvider implements TivoBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "tivo";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		TivoBindingConfig config = new TivoBindingConfig();
		
		//parse bindingconfig here ...
		config.tivoCommand = bindingConfig;
		
		addBindingConfig(item, config);		
	}
	
	@Override
	public String getTivoCommand(String itemName) {
		TivoBindingConfig config = (TivoBindingConfig) bindingConfigs.get(itemName);
		return config.tivoCommand;
	}
	
	
	class TivoBindingConfig implements BindingConfig {
		private String tivoCommand;
	}
}
