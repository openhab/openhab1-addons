/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.openhab.binding.plex.PlexBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class is responsible for parsing the Plex binding configuration.</p>
 * 
 * <p>
 * Some valid binding configuration strings:
 * <ul>
 * <li><code>{plex="ffffffff-eeee-dddd-cccc-ba9876543210#state"}</code> - State (Stopped, Buffering, Playing or Paused) of the Plex client
 * </li>
 * <li><code>{plex="ffffffff-eeee-dddd-cccc-ba9876543210#playback/progress"}</code> - Progress of the media playing (percentage) on specific Plex client
 * </ul>
 * </p>
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class PlexGenericBindingProvider extends AbstractGenericBindingProvider implements PlexBindingProvider {

	private static final Logger logger = 
			LoggerFactory.getLogger(PlexGenericBindingProvider.class);
	
	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("^(.)+#(.)+$");
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "plex";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem 
		   || item instanceof StringItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Dimmer-, String and DateTimeItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		bindingConfig = bindingConfig.trim();
		
		if (CONFIG_PATTERN.matcher(bindingConfig).matches()) {
			PlexBindingConfig config = new PlexBindingConfig();
			
			String fields[] = bindingConfig.split("#");
			config.setItemName(item.getName());
			config.setMachineIdentifier(fields[0]);
			config.setProperty(fields[1]);
			
			logger.info("Plex item {} bound to client {} property {}", config.getItemName(), config.getMachineIdentifier(), config.getProperty());
			
			addBindingConfig(item, config);	
		} else {
			logger.error("Item config {} does not match <player-id>#<property> pattern", bindingConfig);
		}
	}

	@Override
	public PlexBindingConfig getConfig(String machineIdentifier, String property) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			PlexBindingConfig config = (PlexBindingConfig) entry.getValue();
			if (config.getMachineIdentifier().equals(machineIdentifier) && config.getProperty().equals(property))
				return config;
		}
		return null;
	}

	@Override
	public PlexBindingConfig getConfig(String itemName) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			PlexBindingConfig config = (PlexBindingConfig) entry.getValue();
			if (config.getItemName().equals(itemName))
				return config;
		}
		return null;
	}
}
