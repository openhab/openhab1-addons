/**ALWA
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.zibase.zibaseBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * For command item : expected format is { RfId,protocol } 
 * For informations item : expected format is { RfId,valueToGet }
 * 
 * valueToGet is constrained by valueStateMap which list tag names the
 * zibase can send
 * 
 * TODO: handle zibase variables, calendars and scenarios
 * 
 * @author Julien Tiphaine
 * @since 1.6.0
 */
public class zibaseGenericBindingProvider extends AbstractGenericBindingProvider implements zibaseBindingProvider {

	/**
	 * generic logger
	 */
	static final Logger logger = LoggerFactory.getLogger(zibaseGenericBindingProvider.class);
	
	/**
	 * Map that allows to get config by item as given by openHab 
	 */
	static final HashMap<String, zibaseBindingConfig> itemMap = new HashMap<String, zibaseBindingConfig>();
	
	/**
	 * Map that allows to get items name by an RfId
	 */
	static final HashMap<String, Vector<String>> idMap = new HashMap<String, Vector<String>>();
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "zibase";
	}


	/**
	 * @{inheritDoc}
	 * @TODO: validate ITEM config
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		
		if (!(zibaseBindingConfig.isConfigValid(bindingConfig))) {
			throw new BindingConfigParseException("Bad item configuration for '" + item.getName());
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		logger.debug("read item : " + item);
		logger.debug("read config : " + bindingConfig);
		
		String[] itemConfig = StringUtils.split(bindingConfig,zibaseBindingConfig.CONFIG_SEPARATOR);
		
		zibaseBindingConfig config = zibaseBindingConfig.factory(itemConfig);
		itemMap.put(item.getName(), config);

		if (idMap.containsKey(config.getId())) {
			idMap.get(config.getId()).add(item.getName());
		} else {
			Vector<String> vector = new Vector<String>();
			vector.add(item.getName());
			idMap.put(config.getId(), vector);
		}
		
		logger.info("adding " + item.getName());
		addBindingConfig(item, config);
	}
	
	
	/**
	 * get item config by its name
	 * @param itemName
	 * @return the item's config
	 */
	public zibaseBindingConfig getItemConfig(String itemName) {
		logger.debug("retreive config for item : " + itemName);
		zibaseBindingConfig config = itemMap.get(itemName);
		
		return config;
	}
	
	
	/**
	 * get the list of item names that use the same RfId
	 * @param id
	 * @return
	 */
	public Vector<String> getItemNamesById(String rfId) {
		return idMap.get(rfId);
	}
}