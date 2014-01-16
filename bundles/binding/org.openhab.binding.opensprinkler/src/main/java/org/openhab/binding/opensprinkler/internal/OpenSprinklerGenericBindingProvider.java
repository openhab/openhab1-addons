/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.opensprinkler.internal;

import org.openhab.binding.opensprinkler.OpenSprinklerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration. A valid
 * items binding configuration file will look like the following:
 * 
 * <pre>
 * Switch Sprinkler_Station_0      "Station 0" { openSprinkler="0" }
 * Switch Sprinkler_Station_1      "Station 1" { openSprinkler="1" }
 * </pre>
 * 
 * <p>You can see in this example that two stations are exposed as items. 
 * The first station exposed is the 0th port (i.e. the left-most pin on the 
 * OpenSprinkler), and the second station is the 1st port (the 
 * second-to-left-most pin on the OpenSprinkler). Note that there is no 
 * requirement to use the stations in order - you can open and close any station. 
 * 
 * @author Jonathan Giles (http://www.jonathangiles.net)
 * @since 1.3.0
 */
public class OpenSprinklerGenericBindingProvider extends AbstractGenericBindingProvider implements OpenSprinklerBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "openSprinkler";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		OpenSprinklerBindingConfig config = new OpenSprinklerBindingConfig();
		
		//parse bindingconfig here ...
		config.stationNumber = Integer.parseInt(bindingConfig);
		
		addBindingConfig(item, config);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public int getStationNumber(String itemName) {
		OpenSprinklerBindingConfig config = (OpenSprinklerBindingConfig) bindingConfigs.get(itemName);
		return config.stationNumber;
	}
	

	/**
	 * Custom configuration for OpenSprinkler
	 */
	class OpenSprinklerBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private int stationNumber = -1;
	}
	
}
