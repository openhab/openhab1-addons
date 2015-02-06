/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.caldav_presence.internal;

import org.openhab.binding.caldav_presence.CalDavPresenceBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class CalDavPresenceBindingProviderImpl extends AbstractGenericBindingProvider implements CalDavPresenceBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(CalDavPresenceBindingProviderImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "caldavPresence";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only SwitchItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (bindingConfig == null) {
			logger.debug("binding-configuration is currently not set for item: " + item.getName());
		}
		
		logger.debug("adding item: {}", item.getName());
		this.bindingConfigs.put(item.getName(), new CalDavPresenceConfig(bindingConfig));
	}

	@Override
	public CalDavPresenceConfig getConfig(String item) {
		return (CalDavPresenceConfig) this.bindingConfigs.get(item);
	}
}
