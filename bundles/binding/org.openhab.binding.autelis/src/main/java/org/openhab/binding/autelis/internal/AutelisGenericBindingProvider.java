/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.autelis.internal;

import java.util.Set;

import org.openhab.binding.autelis.AutelisBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Autelis Pool control binding configurations.
 * <p>Sample configuration strings:
 * <ul>
 *  <li><code>{autelis="system.version"}</code></li>
 *  <li><code>{autelis="temp.poolsp"}</code></li>
 *  <li><code>{autelis="temp.pooltemp"}</code></li>
 *  <li><code>{autelis="chlor.salt"}</code></li>
 *  <li><code>{autelis="equipment.circuit1"}</code></li>
 *  <li><code>{autelis="pumps.pump1"}</code></li>
 *  <li><code>{autelis="lightscmd"}</code></li>
 * </ul>
 * <p> only 'equipment.*', 'temp.*' and 'lightscmd' items can be updated from openhab, everything else is read only.
 * 
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class AutelisGenericBindingProvider extends AbstractGenericBindingProvider implements AutelisBindingProvider {
	private static final Logger logger = 
			LoggerFactory.getLogger(AutelisGenericBindingProvider.class);
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "autelis";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch, Dimmer, Number and String Items are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		//read values must contain a '.' like equipment.circuit1 or be lightcmd
		if(bindingConfig.indexOf('.') < 0 && !bindingConfig.equalsIgnoreCase("lightscmd")){
			logger.warn("Item {}'s configuration ({}) is not valid, please use the pattern 'parentType.childType' or lightscmd ", item.getName(), bindingConfig);
			return;
		}
		
		AutelisBindingConfig config = new AutelisBindingConfig(bindingConfig);
		
		addBindingConfig(item, config);	

		super.processBindingConfiguration(context, item, bindingConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(String itemName) {
		for (Set<Item> items : contextMap.values()) {
			if (items != null) {
				for (Item item : items) {
					if (itemName.equals(item.getName())) {
						return item;
					}
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAutelisBindingConfigString(String itemName){
		return ((AutelisBindingConfig) this.bindingConfigs.get(itemName)).getConfig();
	}
	
	/**
	 * Class to hold binding specific configuration details
	 * 
	 * @author Dan Cunningham
	 * @since 1.7.0
	 */
	public class AutelisBindingConfig implements BindingConfig {
		String config;

		public AutelisBindingConfig(String config) {
			super();
			this.config = config;
		}

		public String getConfig() {
			return config;
		}

		public void setConfig(String config) {
			this.config = config;
		}
	}
}
