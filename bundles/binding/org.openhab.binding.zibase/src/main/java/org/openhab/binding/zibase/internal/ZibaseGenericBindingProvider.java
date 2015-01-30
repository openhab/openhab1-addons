/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.zibase.ZibaseBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * All Items are then stored internally with their specific settings.
 *
 * @author Julien Tiphaine
 * @since 1.7.0
 */
public class ZibaseGenericBindingProvider extends AbstractGenericBindingProvider implements ZibaseBindingProvider {

	/**
	 * generic logger
	 */
	static final Logger logger = LoggerFactory.getLogger(ZibaseGenericBindingProvider.class);
	
	/**
	 * Map that allows to get config by item as given by openHab 
	 */
	static final HashMap<String, ZibaseBindingConfig> itemNameMap = new HashMap<String, ZibaseBindingConfig>();
	
	/**
	 * Map that allows to get config by unique ID
	 */
	static final HashMap<String, ZibaseBindingConfig> itemUniqueIdMap = new HashMap<String, ZibaseBindingConfig>();
	
	/**
	 * Map that allows to get items name by an RfId
	 */
	static final HashMap<String, Vector<String>> itemIdNameMap = new HashMap<String, Vector<String>>();
	
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
		
		if (!(ZibaseBindingConfig.isConfigValid(bindingConfig))) {
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
		
		String[] itemConfig = StringUtils.split(bindingConfig,ZibaseBindingConfig.CONFIG_SEPARATOR);
		
		ZibaseBindingConfig config = ZibaseBindingConfig.factory(itemConfig);
		String id = config.getId();
		
		itemNameMap.put(item.getName(), config);
		itemUniqueIdMap.put(item.getName()+"_"+id, config);
		
		if (itemIdNameMap.containsKey(id)) {
			if(!itemIdNameMap.get(id).contains(item.getName())) {
				itemIdNameMap.get(id).add(item.getName());
			}
		} else {
			Vector<String> vector = new Vector<String>();
			vector.add(item.getName());
			itemIdNameMap.put(id, vector);
		}
		
		logger.info("Added " + item.getName() + " (id=" + id + ")");
		
		addBindingConfig(item, config);
	}
	

	/**
	 * get item config by its name. In case an item is assigned several zibase items RfId, using this method may lead to 
	 * unexpected result as it will return the lastest config associated to the itemName
	 * @param itemName
	 * @return the item's config
	 */
	public ZibaseBindingConfig getItemConfig(String itemName) {
		logger.debug("retreive config for item : " + itemName);
		ZibaseBindingConfig config = itemNameMap.get(itemName);
		
		return config;
	}
	
	/**
	 * get item config by a unique ID
	 * @param RfId
	 * @return the item's config
	 */
	public ZibaseBindingConfig getItemConfigByUniqueId(String uniqueId) {
		logger.debug("retreive config for item : " + uniqueId);
		ZibaseBindingConfig config = itemUniqueIdMap.get(uniqueId);
		
		return config;
	}
	
	/**
	 * get the list of item names that use the same RfId
	 * @param id
	 * @return
	 */
	public Vector<String> getItemNamesById(String rfId) {
		return itemIdNameMap.get(rfId);
	}
}
