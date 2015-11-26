/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_command.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.caldav_command.CalDavBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * caldav="itemName:Livingroom_Temperature_Set type:VALUE"
 * caldav="itemName:Livingroom_Temperature_Set type:DATE"
 * caldav="itemName:Livingroom_Temperature_Set type:DISABLE"
 * 
 * @author Robert Delbrück
 * @since 1.8.0
 */
public class CalDavBindingProviderImpl extends AbstractGenericBindingProvider implements CalDavBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(CalDavBindingProviderImpl.class);
	
	private static final String REGEX_ITEM_NAME = "itemName:([^ ]+)";
	private static final String REGEX_TYPE = "type:([^ ]+)";
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "caldavCommand";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// nothing to validate, all item types are allowed
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig == null) {
			logger.debug("binding-configuration is currently not set for item: {}", item.getName());
		}
		
		String itemName = null;
		Matcher mItemName = Pattern.compile(REGEX_ITEM_NAME).matcher(bindingConfig);
		if (mItemName.find()) {
			itemName = mItemName.group(1);
		}
		
		String typeString = null;
		Matcher mType = Pattern.compile(REGEX_TYPE).matcher(bindingConfig);
		if (mType.find()) {
			typeString = mType.group(1);
		}
		CalDavType type = null;
		try {
			type = CalDavType.valueOf(typeString);
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("cannot read required parameter 'type' for item: " + item.getName());
		}
		
		logger.debug("adding item: {}", item.getName());
		this.addBindingConfig(item, new CalDavNextEventConfig(item.getName(), itemName, type));
	}

	@Override
	public CalDavNextEventConfig getConfig(String item) {
		return (CalDavNextEventConfig) this.bindingConfigs.get(item);
	}

	@Override
	public List<CalDavNextEventConfig> getConfigForListenerItem(String item) {
		List<CalDavNextEventConfig> list = new ArrayList<CalDavNextEventConfig>();
		
		for (BindingConfig bindingConfig : this.bindingConfigs.values()) {
			if (!(bindingConfig instanceof CalDavNextEventConfig)) {
				continue;
			}
			
			CalDavNextEventConfig config = (CalDavNextEventConfig) bindingConfig;
			
			if (config.getItemNameToListenTo().equals(item)) {
				list.add(config);
			}
		}
		
		return list;
	}
}
